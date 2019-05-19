package utilities;

import models.Account;

public class Utils {

    public static boolean hasSufficientAmountToPay(Account payee, double amount) {
        return payee.getBalance() >= amount;
    }

}
