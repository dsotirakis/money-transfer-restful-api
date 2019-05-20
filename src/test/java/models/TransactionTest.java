package models;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for the Transaction class.
 */
class TransactionTest {

    private Transaction transaction = new Transaction("1", 1, 2, 1.0);

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
}
