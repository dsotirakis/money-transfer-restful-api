package models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * This is the Transaction class, which utilizes a transaction between two end users.
 */
public class Transaction {

    @JsonIgnore
    private String id;
    private int accountTo;
    private int accountFrom;
    private BigDecimal amount;
    private String currency;
    private LocalDate time;

    @JsonCreator
    public Transaction(
            @JsonProperty(value = "accountTo", required = true) int accountTo,
            @JsonProperty(value = "accountFrom", required = true) int accountFrom,
            @JsonProperty(value = "amount", required = true) BigDecimal amount,
            @JsonProperty(value = "currency", required = true) String currency) {
        this.accountTo = accountTo;
        this.accountFrom = accountFrom;
        this.amount = amount;
        this.currency = currency;
    }

    public Transaction(
            String id,
            int accountTo,
            int accountFrom,
            BigDecimal amount,
            String currency,
            LocalDate time) {
        this.id = id;
        this.accountTo = accountTo;
        this.accountFrom = accountFrom;
        this.amount = amount;
        this.currency = currency;
        this.time = time;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    LocalDate getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", accountTo='" + accountTo + '\'' +
                ", accountFrom='" + accountFrom + '\'' +
                ", amount='" + amount + '\'' +
                ", currency='" + currency + '\'' +
                ", date='" + time.toString() + '\'' +
                '}';
    }
}
