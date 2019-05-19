package repositories.impl;

import models.Account;
import org.apache.commons.dbutils.DbUtils;
import repositories.AccountRepository;
import repositories.InMemoryDatabase;

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
        if (accountCache.isEmpty())
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
                        resultSet.getInt("BALANCE"));
                accountCache.add(account);
            }
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(connection, statement, resultSet);
        }
    }

    public Account add(Account account) {
        Connection connection;
        PreparedStatement statement;
        try {
            connection = InMemoryDatabase.getConnection();
            //Set auto commit to false
            Objects.requireNonNull(connection).setAutoCommit(false);

            String string = ("INSERT INTO ACCOUNTS (username, password, balance) VALUES (?, ?, ?)");

            // Used prepare statement methods in order not to pass direct strings, to care about SQL Injection issues.
            statement = connection.prepareStatement(string);
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());
            statement.setDouble(3, account.getBalance());

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

        accountCache.add(account);

        return null;
    }

    public Account delete(int id) {
        Optional<Account> optionalAccount = accountCache.stream()
                .filter(account -> account.getId() == id)
                .findAny();

        if (!optionalAccount.isPresent())
            return null;

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

        return optionalAccount.get();
    }

    public Account update(int id, Account updatedAccount) {
        Optional<Account> optionalAccount = accountCache.stream()
                .filter(account -> account.getId() == id)
                .findAny();

        if (!optionalAccount.isPresent())
            return null;

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

        return updatedAccount;
    }

    private Account updateIdToUpdatedAccount(Account previousAccount, Account updatedAccount) {
        return new Account(
                previousAccount.getId(),
                updatedAccount.getUsername(),
                updatedAccount.getPassword(),
                updatedAccount.getBalance());
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
