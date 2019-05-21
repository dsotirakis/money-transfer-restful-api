package repositories.impl;

import models.Transaction;
import org.junit.jupiter.api.*;
import repositories.InMemoryDatabase;
import repositories.RepositoryGenerator;
import repositories.TransactionRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.MethodOrderer.*;

import java.math.BigDecimal;

/**
 * This is a test class for the transaction repository implementation.
 */
class TransactionRepositoryImplTest {

    private TransactionRepository transactionRepository = RepositoryGenerator.getTransactionRepository();

    @BeforeAll
    static void setUp() {
        InMemoryDatabase.generateData();
    }

    /**
     * The following tests are testing the Create/Update/Delete procedures of the transaction repository implementation
     * tests.
     */
    @Nested
    @DisplayName("Testing CUD procedures")
    @TestMethodOrder(OrderAnnotation.class)
    class TransactionRepositoryCUDTest {

        @Test
        @Order(1)
        void makeTransaction() {
            Transaction transaction = new Transaction(2, 3, new BigDecimal(10.0), "USD");
            transactionRepository.makeTransaction(transaction);
            assertTrue(transactionRepository.getAll().size() != 0);
            assertEquals(RepositoryGenerator.getAccountRepository().getById(2).getBalance().doubleValue(), 20010.0);
            assertEquals(RepositoryGenerator.getAccountRepository().getById(3).getBalance().doubleValue(), 29990.0);
        }

        @Test
        @Order(2)
        void makeTransaction_invalidCurrencyCode() {
            Transaction transaction = new Transaction(1, 2, new BigDecimal(10.0), "ZZZ");
            assertEquals(transactionRepository.makeTransaction(transaction).getStatus(), 404);
        }

        @Test
        void makeTransaction_invalidAccounts() {
            Transaction transaction = new Transaction(10, 2, new BigDecimal(10.0), "USD");
            assertEquals(transactionRepository.makeTransaction(transaction).getStatus(), 404);
        }

        @Test
        void makeTransaction_insufficientAmount() {
            Transaction transaction = new Transaction(2, 3, new BigDecimal(99999.0), "USD");
            assertEquals(transactionRepository.makeTransaction(transaction).getStatus(), 400);
        }
    }
}
