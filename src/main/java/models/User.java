package models;

/**
 * This is the User class, which is responsible for implementing the end-user of the application.
 */
public class User {

    private String id;
    private String name;
    private String surname;
    private String email;

    User(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }


}
