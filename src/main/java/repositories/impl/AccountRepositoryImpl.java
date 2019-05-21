package repositories.impl;

import models.Account;
import models.User;
import org.apache.commons.dbutils.DbUtils;
import repositories.AccountRepository;
import repositories.InMemoryDatabase;
import repositories.RepositoryGenerator;
import utilities.Utils;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * This class implements an account repository. It is responsible for manipulating accounts in the database,
 * as well as connecting and caching entities in a static Set, in order to be widely accessible from all the
 * modules.
 */
public class AccountRepositoryImpl implements AccountRepository {

    private static final Set<Account> accountCache = new HashSet<>();

    public AccountRepositoryImpl() {
        cacheAccounts();
    }

    /**
     * This method is used in order to cache the accounts. Every time a CRUD operation occurs, the cache is updated.
     */
    private static void cacheAccounts() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = InMemoryDatabase.getConnection();
            statement = Objects.requireNonNull(connection).prepareStatement("SELECT * FROM Accounts");
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Account account = new Account(
                        resultSet.getInt("ID"),
                        resultSet.getString("USERNAME"),
                        resultSet.getString("PASSWORD"),
                        resultSet.getBigDecimal("BALANCE"),
                        resultSet.getString("CURRENCY"));
                accountCache.add(account);
            }
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(connection, statement, resultSet);
        }
    }

    /**
     * This method adds a new account to the database.
     *
     * @param newAccount the new account to be stored.
     *
     * @return a response, indicating the result of the POST method.
     */
    public Response add(Account newAccount) {
        User user = RepositoryGenerator.getUserRepository().getByMail(newAccount.getUsername());
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Account username didn't match with any user!").build();
        }
        if (Utils.isValidCurrencyCode(newAccount.getCurrency())) {
            return Response.status(Response.Status.NOT_FOUND).entity("Invalid currency!").build();
        }
        Connection connection;
        PreparedStatement statement;
        try {
            connection = InMemoryDatabase.getConnection();
            Objects.requireNonNull(connection).setAutoCommit(false);

            String string = ("INSERT INTO ACCOUNTS (username, password, balance, currency) VALUES (?, ?, ?, ?)");

            // Used prepare statement methods in order not to pass direct strings, to care about SQL Injection issues.
            statement = connection.prepareStatement(string);
            statement.setString(1, newAccount.getUsername());
            statement.setString(2, newAccount.getPassword());
            statement.setBigDecimal(3, newAccount.getBalance());
            statement.setString(4, newAccount.getCurrency());

            statement.executeUpdate();

            //CLose the PreparedStatement object
            statement.close();

            //Close the Connection Object
            connection.commit();

        }
        catch(Exception ex)
        {
            System.out.println(ex.toString());
        }

        newAccount.setUser(user);
        accountCache.add(newAccount);

        return Response.status(Response.Status.CREATED).entity("Account created successfully!").build();
    }

    /**
     * This method deletes an account from the database.
     *
     * @param id the account id to be deleted.
     *
     * @return a response, indicating the result of the DELETE method.
     */
    public Response delete(int id) {
        Optional<Account> optionalAccount = accountCache.stream()
                .filter(account -> account.getId() == id)
                .findAny();

        if (!optionalAccount.isPresent())
            return objectNotFound();

        Connection connection;
        PreparedStatement statement;
        try {
            connection = InMemoryDatabase.getConnection();
            Objects.requireNonNull(connection).setAutoCommit(false);

            String string = ("DELETE FROM Accounts WHERE id = ? ");

            // Used prepare statement methods in order not to pass direct strings, to care about SQL Injection issues.
            statement = connection.prepareStatement(string);
            statement.setInt(1, id);

            statement.executeUpdate();

            //CLose the PreparedStatement object
            statement.close();

            //Close the Connection Object
            connection.commit();
        }
        catch(Exception ex)
        {
            System.out.println(ex.toString());
        }

        accountCache.remove(optionalAccount.get());

        return Response.status(Response.Status.NO_CONTENT).entity("Account deleted successfully!").build();
    }

    /**
     * This method updates an account in the database.
     *
     * @param id the account's id to be updated.
     * @param updatedAccount the new account attributes.
     *
     * @return a response, indicating the result of the PUT method.
     */
    public synchronized Response update(int id, Account updatedAccount) {
        Optional<Account> optionalAccount = accountCache.stream()
                .filter(account -> account.getId() == id)
                .findAny();

        if (!optionalAccount.isPresent()) {
            return objectNotFound();
        }

        Connection connection;
        PreparedStatement statement;
        try {
            connection = InMemoryDatabase.getConnection();
            Objects.requireNonNull(connection).setAutoCommit(false);

            String string = ("UPDATE Accounts SET username = ?, password = ?, balance = ? WHERE id = ?");

            // Used prepare statement methods in order not to pass direct strings, to care about SQL Injection issues.
            statement = connection.prepareStatement(string);
            statement.setString(1, updatedAccount.getUsername());
            statement.setString(2, updatedAccount.getPassword());
            statement.setBigDecimal(3, updatedAccount.getBalance());
            statement.setInt(4, id);

            statement.executeUpdate();

            //CLose the PreparedStatement object
            statement.close();

            //Close the Connection Object
            connection.commit();
        }
        catch(Exception ex)
        {
            System.out.println(ex.toString());
        }

        accountCache.remove(optionalAccount.get());

        updatedAccount = updateIdOfUpdatedObject(optionalAccount.get(), updatedAccount);
        accountCache.add(updatedAccount);

        return Response.status(Response.Status.NO_CONTENT).entity("Account updated successfully!").build();
    }

    /**
     * This method is responsible for returning a NOT_FOUND response in case the account does not exist.
     *
     * @return a response, indicating that the account is not found.
     */
    public Response objectNotFound() {
        return Response.status(Response.Status.NOT_FOUND).entity("Account not found!").build();
    }

    /**
     * This method makes sure that the new updated account is stored to the proper id of the previous one.
     *
     * @param previousAccount the previous account to be replaced.
     * @param updatedAccount the updated account.
     *
     * @return the new account object.
     */
    public Account updateIdOfUpdatedObject(Account previousAccount, Account updatedAccount) {
        return new Account(
                previousAccount.getId(),
                updatedAccount.getUsername(),
                updatedAccount.getPassword(),
                updatedAccount.getBalance(),
                updatedAccount.getCurrency());
    }

    /**
     * This method is responsible for returning the account cache.
     *
     * @return a set containing all the accounts in the cache.
     */
    public Set<Account> getAll() {
        return accountCache;
    }

    /**
     * This method is responsible for returning an account based on its id.
     *
     * @param id the id of the account to retrieve.
     *
     * @return the retrieved account.
     */
    public Account getById(int id) {
        Optional<Account> optionalAccount = accountCache.stream()
                .filter(account -> account.getId() == id)
                .findAny();

        return optionalAccount.orElse(null);
    }

    /**
     * This method is responsible for returning an account based on its username.
     *
     * @param userName the username of the account to retrieve.
     *
     * @return the retrieved account.
     */
    @Override
    public Account getByUsername(String userName) {
        Optional<Account> optionalAccount = accountCache.stream()
                .filter(account -> account.getUsername().equals(userName))
                .findAny();

        return optionalAccount.orElse(null);
    }
}
