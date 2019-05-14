package models;

/**
 * This is the Transaction class, which utilizes a transaction between two end users.
 */
public class Transaction {

    private Account accountFrom;
    private Account accountTo;

    Transaction(Account accountFrom, Account accountTo) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
    }

    public Account getAccountFrom() {
        return accountFrom;
    }

    public Account getAccountTo() {
        return accountTo;
    }
}
