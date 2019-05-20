package repositories.impl;

import models.Transaction;
import org.junit.jupiter.api.*;
import repositories.InMemoryDatabase;
import repositories.RepositoryGenerator;
import repositories.TransactionRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.MethodOrderer.*;

class TransactionRepositoryImplTest {

    private TransactionRepository transactionRepository = RepositoryGenerator.getTransactionRepository();

    @BeforeAll
    static void setUp() {
        InMemoryDatabase.generateData();
    }

    @Nested
    @DisplayName("Testing CUD procedures")
    @TestMethodOrder(OrderAnnotation.class)
    class TransactionRepositoryCUDTest {

        @Test
        @Order(1)
        void makeTransaction() {
            Transaction transaction = new Transaction(2, 3, 10.0, "USD");
            transactionRepository.makeTransaction(transaction);
            assertTrue(transactionRepository.getAll().size() != 0);
            assertEquals(RepositoryGenerator.getAccountRepository().getById(2).getBalance(), 20010.0);
            assertEquals(RepositoryGenerator.getAccountRepository().getById(3).getBalance(), 29990.0);
        }

        @Test
        @Order(2)
        void makeTransaction_invalidCurrencyCode() {
            Transaction transaction = new Transaction(1, 2, 10.0, "ZZZ");
            assertEquals(transactionRepository.makeTransaction(transaction).getStatus(), 404);
        }

        @Test
        void makeTransaction_invalidAccounts() {
            Transaction transaction = new Transaction(10, 2, 10.0, "USD");
            assertEquals(transactionRepository.makeTransaction(transaction).getStatus(), 404);
        }

        @Test
        void makeTransaction_insufficientAmount() {
            Transaction transaction = new Transaction(2, 3, 99999.0, "USD");
            assertEquals(transactionRepository.makeTransaction(transaction).getStatus(), 400);
        }
    }
}
