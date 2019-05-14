package models;

/**
 * This is the Account class, which corresponds to a user, with specific credentials.
 */
public class Account {

    private String id;
    private String username;
    private String password;
    private User user;

    Account(String userName, String password, User user) {
        this.username = userName;
        this.password = password;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public User getUser() {
        return this.user;
    }

}
