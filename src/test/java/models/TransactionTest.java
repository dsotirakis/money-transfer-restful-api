package models;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for the Transaction class.
 */
class TransactionTest {

    private final LocalDate localDate = LocalDate.now();

    private Transaction transaction = new Transaction("1", 1, 2, 1.0, "USD", localDate);

    @Test
    void getId() {
        assertEquals(transaction.getId(), "1");
    }

    @Test
    void getAccountTo() {
        assertEquals(transaction.getAccountTo(), 1);
    }

    @Test
    void getAccountFrom() {
        assertEquals(transaction.getAccountFrom(), 2);
    }

    @Test
    void getAmount() {
        assertEquals(transaction.getAmount(), 1.0, 0.0);
    }

    @Test
    void getCurrency() {
        assertEquals(transaction.getCurrency(), "USD");
    }

    @Test
    void getLocalDate() {
        assertEquals(transaction.getTime(), localDate);
    }
}
