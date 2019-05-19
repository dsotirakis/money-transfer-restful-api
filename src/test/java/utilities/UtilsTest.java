package utilities;

import models.Account;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Testing for the Utils class.
 */
class UtilsTest {

    private Account account = new Account(1, "userName", "password", 1.0);

    @Test
    void hasSufficientAmountToPay() {
        assertTrue(Utils.hasSufficientAmountToPay(account, 1.0));
        assertFalse(Utils.hasSufficientAmountToPay(account, 2.0));
    }
}
