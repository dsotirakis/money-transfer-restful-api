package utilities;

import models.Account;

import java.util.Currency;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This is a utilities class, providing handful tools for the whole platform.
 */
public class Utils {

    /**
     * This method is responsible for indicating if a payee has sufficient amount of money to make a transaction.
     *
     * @param payee the user that is going to pay.
     * @param amount the amount of money to be paid.
     *
     * @return if the payee has sufficient amount of money to pay.
     */
    public static boolean hasSufficientAmountToPay(Account payee, double amount) {
        return payee.getBalance() >= amount;
    }

    /**
     * This method is responsible for telling if a currency code is valid. It uses the Java Currency class.
     *
     * @param currencyCode the currencyCode provided.
     *
     * @return if the currencyCode is valid.
     */
    public static boolean isValidCurrencyCode(String currencyCode) {
        return !getAllCurrencies().contains(currencyCode);
    }

    /**
     * This method is a getter method, responsible for returning all the available valid currencies.
     *
     * @return a set containing all the valid currencies.
     */
    private static Set<String> getAllCurrencies()
    {
        return Currency.getAvailableCurrencies().stream()
                .map(Currency::getCurrencyCode)
                .collect(Collectors.toSet());
    }

}
