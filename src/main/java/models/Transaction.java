package models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This is the Transaction class, which utilizes a transaction between two end users.
 */
public class Transaction {

    @JsonIgnore
    private String id;
    private int accountTo;
    private int accountFrom;
    private double amount;

    @JsonCreator
    public Transaction(
            @JsonProperty(value = "accountTo", required = true) int accountTo,
            @JsonProperty(value = "accountFrom", required = true) int accountFrom,
            @JsonProperty(value = "amount", required = true) double amount) {
        this.accountTo = accountTo;
        this.accountFrom = accountFrom;
        this.amount = amount;
    }

    public Transaction(String id, int accountTo, int accountFrom, double amount) {
        this.id = id;
        this.accountTo = accountTo;
        this.accountFrom = accountFrom;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", accountTo='" + accountTo + '\'' +
                ", accountFrom='" + accountFrom + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
