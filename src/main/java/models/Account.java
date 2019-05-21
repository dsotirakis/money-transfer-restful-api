package models;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This is the Account class, which corresponds to an account, with specific credentials.
 */
public class Account {

    @JsonIgnore
    private int id;
    private String username;
    private String password;
    private BigDecimal balance;
    private String currency;
    private User user;

    @JsonCreator
    public Account(
            @JsonProperty(value = "username", required = true) String username,
            @JsonProperty(value = "password", required = true) String password,
            @JsonProperty(value = "balance", required = true) BigDecimal balance,
            @JsonProperty(value = "currency", required = true) String currency) {
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.currency = currency;
    }

    public Account(int id, String userName, String password, BigDecimal balance, String currency) {
        this.id = id;
        this.username = userName;
        this.password = password;
        this.balance = balance;
        this.currency = currency;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public String getCurrency() {
        return currency;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", balance='" + balance + '\'' +
                ", currency='" + currency + '\'' +
                ", user='" + user + '\'' +
                '}';
    }
}
