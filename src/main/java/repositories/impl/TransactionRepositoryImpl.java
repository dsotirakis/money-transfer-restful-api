package repositories.impl;

import models.Account;
import models.Transaction;
import org.apache.commons.dbutils.DbUtils;
import repositories.InMemoryDatabase;
import repositories.RepositoryGenerator;
import repositories.TransactionRepository;
import utilities.Utils;

import javax.ws.rs.WebApplicationException;
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

public class TransactionRepositoryImpl implements TransactionRepository {

    private final Set<Transaction> transactionCache = new HashSet<>();
    private Set<Account> accountCache = new HashSet<>();

    public TransactionRepositoryImpl() {
        if (RepositoryGenerator.getAccountRepository().getAll().isEmpty()) {
            new AccountRepositoryImpl();
            accountCache = RepositoryGenerator.getAccountRepository().getAll();
        }
        cacheTransactions();
    }

    private void cacheTransactions() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = InMemoryDatabase.getConnection();
            statement = Objects.requireNonNull(connection).prepareStatement("SELECT * FROM Transactions");
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Transaction transaction = new Transaction(
                        resultSet.getInt("ID"),
                        resultSet.getInt("ACCOUNTTO"),
                        resultSet.getInt("ACCOUNTFROM"),
                        resultSet.getDouble("AMOUNT"));
                transactionCache.add(transaction);
            }
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(connection, statement, resultSet);
        }
    }

    @Override
    public Set<Transaction> getAll() {
        return transactionCache;
    }

    @Override
    public Transaction getById(int id) {
        Optional<Transaction> optionalAccount = transactionCache.stream()
                .filter(transaction -> transaction.getId() == id)
                .findAny();

        return optionalAccount.orElse(null);
    }

    @Override
    public Response makeTransaction(Transaction transaction) {
        if (updateAccounts(transaction.getAccountTo(), transaction.getAccountFrom(), transaction.getAmount()))
            return Response.status(Response.Status.NOT_FOUND).build();

        Connection connection;
        PreparedStatement statement;
        try {
            connection = InMemoryDatabase.getConnection();
            //Set auto commit to false
            Objects.requireNonNull(connection).setAutoCommit(false);

            String string = ("INSERT INTO TRANSACTIONS (accountTo, accountFrom, amount) VALUES (?, ?, ?)");

            // Used prepare statement methods in order not to pass direct strings, to care about SQL Injection issues.
            statement = connection.prepareStatement(string);
            statement.setInt(1, transaction.getAccountTo());
            statement.setInt(2, transaction.getAccountFrom());
            statement.setDouble(3, transaction.getAmount());

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

        transactionCache.add(transaction);

        return Response.status(Response.Status.OK).build();
    }

    private boolean updateAccounts(int accountIdTo, int accountIdFrom, double amount) {
        Optional<Account> optionalAccountTo = accountCache.stream()
                .filter(account -> account.getId() == accountIdTo)
                .findAny();

        Optional<Account> optionalAccountFrom = accountCache.stream()
                .filter(account -> account.getId() == accountIdFrom)
                .findAny();

        if (!optionalAccountTo.isPresent() || !optionalAccountFrom.isPresent()) {
            return true;
        }

        Account accountTo = optionalAccountTo.get();
        Account accountFrom = optionalAccountFrom.get();

        // TODO: 5/19/19 Check that bad request here.
        if (!Utils.hasSufficientAmountToPay(accountFrom, amount)) {
            throw new WebApplicationException("Not sufficient amount left", Response.Status.BAD_REQUEST);
        }

        Account updatedAccountTo = new Account(accountTo.getId(),
                accountTo.getUsername(),
                accountTo.getPassword(),
                accountTo.getBalance() + amount);

        Account updatedAccountFrom = new Account(accountFrom.getId(),
                accountFrom.getUsername(),
                accountFrom.getPassword(),
                accountFrom.getBalance() - amount);

        return RepositoryGenerator.getAccountRepository().update(updatedAccountTo.getId(), updatedAccountTo) == null ||
                RepositoryGenerator.getAccountRepository().update(updatedAccountFrom.getId(), updatedAccountFrom) == null;
    }
}
