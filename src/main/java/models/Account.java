package models;

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
    private double balance;

    @JsonCreator
    public Account(
            @JsonProperty(value = "username", required = true) String username,
            @JsonProperty(value = "password", required = true) String password,
            @JsonProperty(value = "balance", required = true) double balance) {
        this.username = username;
        this.password = password;
        this.balance = balance;
    }

    public Account(int id, String userName, String password, double balance) {
        this.id = id;
        this.username = userName;
        this.password = password;
        this.balance = balance;
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

    public double getBalance() {
        return this.balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", balance='" + balance + '\'' +
                '}';
    }
}
