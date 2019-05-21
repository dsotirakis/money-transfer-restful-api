package repositories;

import models.Account;

import javax.ws.rs.core.Response;
import java.util.Set;

/**
 * This is an account repository interface which implements methods from the Repository< T > interface.
 */
public interface AccountRepository extends Repository<Account> {

    Response add(Account account);

    Response delete(int id);

    Response update(int id, Account updatedAccount);

    Set<Account> getAll();

    Account getById(int id);

    Account getByUsername(String userName);

    Response objectNotFound();

    Account updateIdOfUpdatedObject(Account previousObject, Account updatedObject);
}
