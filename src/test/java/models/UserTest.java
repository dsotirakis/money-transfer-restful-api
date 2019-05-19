package models;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for the User class.
 */
class UserTest {

    private User user = new User(1, "name", "surname", "email");

    @Test
    void getId() {
        assertEquals(user.getId(), 1);
    }

    @Test
    void getName() {
        assertEquals(user.getName(), "name");
    }

    @Test
    void getSurname() {
        assertEquals(user.getSurname(), "surname");
    }

    @Test
    void getEmail() {
        assertEquals(user.getEmail(), "email");
    }
}
