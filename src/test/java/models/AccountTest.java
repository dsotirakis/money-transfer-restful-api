package models;

import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

/**
 * Unit test for the Account class.
 */
class AccountTest {

    private Account account = new Account(1, "userName", "password", 1.0);

    @Test
    void getId() {
        assertEquals(account.getId(), 1);
    }

    @Test
    void getUserName() {
        assertEquals(account.getUsername(), "userName");
    }

    @Test
    void getPassword() {
        assertEquals(account.getPassword(), "password");
    }

    @Test
    void getBalance() {
        assertEquals(account.getBalance(), 1.0, 0.0);
    }
}
