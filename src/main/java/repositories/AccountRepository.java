package repositories;

import models.Account;

import javax.ws.rs.core.Response;
import java.util.Set;

public interface AccountRepository extends Repository<Account> {

    Response add(Account account);

    Response delete(int id);

    Response update(int id, Account updatedAccount);

    Set<Account> getAll();

    Account getById(int id);

    Account getByUsername(String userName);
}
