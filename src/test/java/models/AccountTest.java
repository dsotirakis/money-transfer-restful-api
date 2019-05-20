package models;

import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

/**
 * Unit test for the Account class.
 */
class AccountTest {

    private Account account = new Account(1, "name@gmail.com", "password", 1.0, "USD");

    @Test
    void getId() {
        assertEquals(account.getId(), 1);
    }

    @Test
    void getUserName() {
        assertEquals(account.getUsername(), "name@gmail.com");
    }

    @Test
    void getPassword() {
        assertEquals(account.getPassword(), "password");
    }

    @Test
    void getBalance() {
        assertEquals(account.getBalance(), 1.0, 0.0);
    }

    @Test
    void getCurrency() {
        assertEquals(account.getCurrency(), "USD");
    }

    @Test
    void setUser() {
        User user = new User("name", "surname", "name@gmail.com");
        account.setUser(user);
        assertEquals(account.getUser(), user);
    }
}
