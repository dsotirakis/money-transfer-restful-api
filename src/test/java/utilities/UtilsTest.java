package utilities;

import models.Account;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

/**
 * Testing for the Utils class.
 */
class UtilsTest {

    private Account account = new Account(1, "userName", "password", new BigDecimal(1.0), "USD");

    @Test
    void hasSufficientAmountToPay() {
        assertTrue(Utils.hasSufficientAmountToPay(account, new BigDecimal(1.0)));
        assertFalse(Utils.hasSufficientAmountToPay(account, new BigDecimal(2.0)));
    }
}
