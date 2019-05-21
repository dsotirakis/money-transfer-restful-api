package models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This is the User class, which is responsible for implementing the end-user of the application.
 */
public class User {

    @JsonIgnore
    private  int id;
    private  String name;
    private  String surname;
    private  String email;

    @JsonCreator
    public User(
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "surname", required = true) String surname,
            @JsonProperty(value = "email", required = true) String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public User(int id,
                String name,
                String surname,
                String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public int getId() {
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }


}
