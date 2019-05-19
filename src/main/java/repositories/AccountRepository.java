package repositories;

import models.Account;

import java.util.Set;

public interface AccountRepository extends Repository<Account> {

    Account add(Account account);

    Account delete(int id);

    Account update(int id, Account updatedAccount);

    Set<Account> getAll();

    Account getById(int id);

    Account getByUsername(String userName);
}
