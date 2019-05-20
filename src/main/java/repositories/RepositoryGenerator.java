package repositories;

import repositories.impl.AccountRepositoryImpl;
import repositories.impl.UserRepositoryImpl;

public class RepositoryGenerator {

    private static final UserRepository userRepo = new UserRepositoryImpl();
    private static final AccountRepository accountRepo = new AccountRepositoryImpl();

    public RepositoryGenerator() {}

    public static UserRepository getUserRepository() {
        return userRepo;
    }

    public static AccountRepository getAccountRepository() {
        return accountRepo;
    }
}
