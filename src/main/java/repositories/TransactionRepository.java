package repositories;

import models.Transaction;

import javax.ws.rs.core.Response;
import java.util.Set;

/**
 * This is a transaction repository interface.
 */
public interface TransactionRepository {

    Set<Transaction> getAll();

    Transaction getById(String  id);

    Response makeTransaction(Transaction transaction);

}
