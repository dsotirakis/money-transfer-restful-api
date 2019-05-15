package repositories;

import models.Account;

import java.util.Set;

public interface AccountRepository extends Repository<Account> {

    void add(Account account);

    void delete(Account account);

    Account update(Account account);

    Set<Account> getAll();

    Account getById(String id);
}
