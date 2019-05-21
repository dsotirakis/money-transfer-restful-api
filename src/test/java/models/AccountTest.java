package models;

import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;

/**
 * Unit test for the Account class.
 */
class AccountTest {

    private Account account = new Account(1, "name@gmail.com", "password", new BigDecimal(1.0), "USD");

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
        assertEquals(account.getBalance(), new BigDecimal(1.0));
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
