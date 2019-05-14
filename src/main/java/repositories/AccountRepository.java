package repositories;

import models.Account;

import java.util.Set;

public interface AccountRepository extends Repository<Account> {

    public void add(Account account);

    public void delete(Account account);

    public Account update(Account account);

    public Set<Account> getAll();

    public Account getById(String id);
}
