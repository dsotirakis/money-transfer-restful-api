package repositories.impl;

import models.Account;
import models.Transaction;
import org.apache.commons.dbutils.DbUtils;
import repositories.InMemoryDatabase;
import repositories.RepositoryGenerator;
import repositories.TransactionRepository;
import utilities.Utils;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * This class implements an transaction repository. It is responsible for manipulating transactions in the database,
 * as well as connecting and caching entities in a static Set, in order to be widely accessible from all the
 * modules.
 */
public class TransactionRepositoryImpl implements TransactionRepository {

    private final Set<Transaction> transactionCache = new HashSet<>();
    private Set<Account> accountCache = RepositoryGenerator.getAccountRepository().getAll();

    public TransactionRepositoryImpl() {

        cacheTransactions();
    }

    /**
     * This method is used in order to cache the transactions. Every time a CRUD operation occurs, the cache is updated.
     */
    private void cacheTransactions() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS");
        try {
            connection = InMemoryDatabase.getConnection();
            statement = Objects.requireNonNull(connection).prepareStatement("SELECT * FROM Transactions");
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Transaction transaction = new Transaction(
                        resultSet.getString("ID"),
                        resultSet.getInt("ACCOUNTTO"),
                        resultSet.getInt("ACCOUNTFROM"),
                        resultSet.getDouble("AMOUNT"),
                        resultSet.getString("CURRENCY"),
                        LocalDate.parse(resultSet.getString("DATE_TRANS"), formatter));
                transactionCache.add(transaction);
            }
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(connection, statement, resultSet);
        }
    }

    /**
     * This method is responsible for returning the transaction cache.
     *
     * @return a set containing all the transactions in the cache.
     */
    @Override
    public Set<Transaction> getAll() {
        return transactionCache;
    }

    /**
     * This method is responsible for returning a transaction based on its id.
     *
     * @param id the id of the transaction to retrieve.
     *
     * @return the retrieved transaction.
     */
    @Override
    public Transaction getById(String id) {
        Optional<Transaction> optionalAccount = transactionCache.stream()
                .filter(transaction -> transaction.getId()
                        .equals(id))
                .findAny();

        return optionalAccount.orElse(null);
    }

    /**
     * This method is responsible for making transactions between user accounts.
     *
     * @param transaction the transaction to be made.
     *
     * @return a response, indicating the result of the POST method.
     */
    @Override
    public synchronized Response makeTransaction(Transaction transaction) {
        if (Utils.isValidCurrencyCode(transaction.getCurrency())) {
            return Response.status(Response.Status.NOT_FOUND).entity("Invalid currency!").build();
        }
        if (updateAccounts(transaction.getAccountTo(), transaction.getAccountFrom(), transaction.getAmount()).getStatus() != 204) {
            return updateAccounts(transaction.getAccountTo(), transaction.getAccountFrom(), transaction.getAmount());
        }

        Connection connection;
        PreparedStatement statement;
        try {
            connection = InMemoryDatabase.getConnection();
            Objects.requireNonNull(connection).setAutoCommit(false);

            String string = ("INSERT INTO TRANSACTIONS (id, accountTo, accountFrom, amount, currency, date_trans) VALUES (?, ?, ?, ?, ?, ?)");

            // Used prepare statement methods in order not to pass direct strings, to care about SQL Injection issues.
            statement = connection.prepareStatement(string);
            statement.setString(1, UUID.randomUUID().toString());
            statement.setInt(2, transaction.getAccountTo());
            statement.setInt(3, transaction.getAccountFrom());
            statement.setDouble(4, transaction.getAmount());
            statement.setString(5, transaction.getCurrency());
            statement.setDate(6, Date.valueOf(LocalDateTime.now().toLocalDate()));

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
        System.out.println(transactionCache.size());

        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * This method is responsible for updating the accounts according to the input ids.
     *
     * @param accountIdTo the account id to give money to.
     * @param accountIdFrom the account id to get money from.
     * @param amount the amount of money to be paid.
     *
     * @return a response, indicating the result of the PUT (update) method.
     */
    private Response updateAccounts(int accountIdTo, int accountIdFrom, double amount) {
        Optional<Account> optionalAccountTo = accountCache.stream()
                .filter(account -> account.getId() == accountIdTo)
                .findAny();

        Optional<Account> optionalAccountFrom = accountCache.stream()
                .filter(account -> account.getId() == accountIdFrom)
                .findAny();

        if (!optionalAccountTo.isPresent() || !optionalAccountFrom.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Account not present.").build();
        }

        Account accountTo = optionalAccountTo.get();
        Account accountFrom = optionalAccountFrom.get();

        if (!Utils.hasSufficientAmountToPay(accountFrom, amount)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Insufficient amount.").build();
        }

        Account updatedAccountTo = new Account(accountTo.getId(),
                accountTo.getUsername(),
                accountTo.getPassword(),
                accountTo.getBalance() + amount,
                accountTo.getCurrency());

        Account updatedAccountFrom = new Account(accountFrom.getId(),
                accountFrom.getUsername(),
                accountFrom.getPassword(),
                accountFrom.getBalance() - amount,
                accountFrom.getCurrency());

        RepositoryGenerator.getAccountRepository().update(updatedAccountTo.getId(), updatedAccountTo);
        RepositoryGenerator.getAccountRepository().update(updatedAccountFrom.getId(), updatedAccountFrom);

        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
