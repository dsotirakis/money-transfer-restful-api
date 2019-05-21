package repositories;

import repositories.impl.AccountRepositoryImpl;
import repositories.impl.TransactionRepositoryImpl;
import repositories.impl.UserRepositoryImpl;

/**
 * This class contains static methods and repositories, in order to be widely accessible to every instance anytime.
 */
public class RepositoryGenerator {

    private static final UserRepository userRepo = new UserRepositoryImpl();
    private static final AccountRepository accountRepo = new AccountRepositoryImpl();
    private static final TransactionRepository transactionRepo = new TransactionRepositoryImpl();

    public RepositoryGenerator() {}

    public static UserRepository getUserRepository() {
        return userRepo;
    }

    public static AccountRepository getAccountRepository() {
        return accountRepo;
    }

    public static TransactionRepository getTransactionRepository() {
        return transactionRepo;
    }
}
