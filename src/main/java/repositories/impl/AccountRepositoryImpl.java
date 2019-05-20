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

public class AccountRepositoryImpl implements AccountRepository {

    private static final Set<Account> accountCache = new HashSet<>();

    public AccountRepositoryImpl() {
        cacheAccounts();
    }

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
                        resultSet.getInt("BALANCE"),
                        resultSet.getString("CURRENCY"));
                accountCache.add(account);
            }
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(connection, statement, resultSet);
        }
    }

    public Response add(Account account) {
        User user = RepositoryGenerator.getUserRepository().getByMail(account.getUsername());
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Account username didn't match with any user!").build();
        }
        if (Utils.isValidCurrencyCode(account.getCurrency())) {
            return Response.status(Response.Status.NOT_FOUND).entity("Invalid currency!").build();
        }
        Connection connection;
        PreparedStatement statement;
        try {
            connection = InMemoryDatabase.getConnection();
            //Set auto commit to false
            Objects.requireNonNull(connection).setAutoCommit(false);

            String string = ("INSERT INTO ACCOUNTS (username, password, balance, currency) VALUES (?, ?, ?, ?)");

            // Used prepare statement methods in order not to pass direct strings, to care about SQL Injection issues.
            statement = connection.prepareStatement(string);
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());
            statement.setDouble(3, account.getBalance());
            statement.setString(4, account.getCurrency());

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

        account.setUser(user);
        accountCache.add(account);

        return Response.status(Response.Status.CREATED).entity("Account created successfully!").build();
    }

    public Response delete(int id) {
        Optional<Account> optionalAccount = accountCache.stream()
                .filter(account -> account.getId() == id)
                .findAny();

        if (!optionalAccount.isPresent())
            return accountNotFound();

        Connection connection;
        PreparedStatement statement;
        try {
            connection = InMemoryDatabase.getConnection();
            //Set auto commit to false
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

    public Response update(int id, Account updatedAccount) {
        Optional<Account> optionalAccount = accountCache.stream()
                .filter(account -> account.getId() == id)
                .findAny();

        if (!optionalAccount.isPresent()) {
            return accountNotFound();
        }

        Connection connection;
        PreparedStatement statement;
        try {
            connection = InMemoryDatabase.getConnection();
            //Set auto commit to false
            Objects.requireNonNull(connection).setAutoCommit(false);

            String string = ("UPDATE Accounts SET username = ?, password = ?, balance = ? WHERE id = ?");

            // Used prepare statement methods in order not to pass direct strings, to care about SQL Injection issues.
            statement = connection.prepareStatement(string);
            statement.setString(1, updatedAccount.getUsername());
            statement.setString(2, updatedAccount.getPassword());
            statement.setDouble(3, updatedAccount.getBalance());
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

        updatedAccount = updateIdToUpdatedAccount(optionalAccount.get(), updatedAccount);
        accountCache.add(updatedAccount);

        return Response.noContent().entity("Account updated successfully!").build();
    }

    private Response accountNotFound() {
        return Response.status(Response.Status.NOT_FOUND).entity("Account not found!").build();
    }

    private Account updateIdToUpdatedAccount(Account previousAccount, Account updatedAccount) {
        return new Account(
                previousAccount.getId(),
                updatedAccount.getUsername(),
                updatedAccount.getPassword(),
                updatedAccount.getBalance(),
                updatedAccount.getCurrency());
    }

    public Set<Account> getAll() {
        return accountCache;
    }

    public Account getById(int id) {
        Optional<Account> optionalAccount = accountCache.stream()
                .filter(account -> account.getId() == id)
                .findAny();

        return optionalAccount.orElse(null);
    }

    @Override
    public Account getByUsername(String userName) {
        Optional<Account> optionalAccount = accountCache.stream()
                .filter(account -> account.getUsername().equals(userName))
                .findAny();

        return optionalAccount.orElse(null);
    }
}
