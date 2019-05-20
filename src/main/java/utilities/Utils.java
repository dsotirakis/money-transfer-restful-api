package utilities;

import models.Account;

import java.util.Currency;
import java.util.Set;
import java.util.stream.Collectors;

public class Utils {

    public static boolean hasSufficientAmountToPay(Account payee, double amount) {
        return payee.getBalance() >= amount;
    }

    public static boolean isValidCurrencyCode(String currencyCode) {
        return !getAllCurrencies().contains(currencyCode);
    }

    private static Set<String> getAllCurrencies()
    {
        return Currency.getAvailableCurrencies().stream()
                .map(Currency::getCurrencyCode)
                .collect(Collectors.toSet());
    }

}
