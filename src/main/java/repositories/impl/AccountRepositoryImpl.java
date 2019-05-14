package repositories.impl;

import models.Account;
import repositories.AccountRepository;

import java.util.HashSet;
import java.util.Set;

public class AccountRepositoryImpl implements AccountRepository {

    Set<Account> accounts;

    AccountRepositoryImpl() {
        this.accounts = new HashSet<>();
    }

    public void add(Account account) {
        this.accounts.add(account);
    }

    public void delete(Account account) {
        this.accounts.remove(account);
    }

    public Account update(Account account) {
        return null;
    }

    public Set<Account> getAll() {
        return this.accounts;
    }

    public Account getById(String id) {
        return accounts.stream().filter(e -> e.getId().equals(id)).findAny().get();
    }
}
