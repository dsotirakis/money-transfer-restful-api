package repositories.impl;

import models.User;
import org.junit.jupiter.api.*;
import repositories.InMemoryDatabase;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.MethodOrderer.*;

public class UserRepositoryImplTest {

    private UserRepositoryImpl userRepository = new UserRepositoryImpl();

    @BeforeAll
    static void setUp() {
        InMemoryDatabase.generateData();
    }

    @Nested
    @DisplayName("Testing getters")
    @TestMethodOrder(OrderAnnotation.class)
    class AccountRepositoryGettersTest {

        @Test
        void getAll() {
            assertEquals(userRepository.getAll().size(), 3);
        }

        @Test
        void getById() {
            assertEquals(userRepository.getById(1).getId(), 1);
            assertEquals(userRepository.getById(2).getId(), 2);
            assertEquals(userRepository.getById(3).getId(), 3);
        }

        @Test
        void getById_idDoesntExist() {
            assertNull(userRepository.getById(4));
        }

        @Test
        void getByName() {
            assertEquals(userRepository.getByName("name1").getName(), "name1");
            assertEquals(userRepository.getByName("name2").getName(), "name2");
            assertEquals(userRepository.getByName("name3").getName(), "name3");
        }

        @Test
        void getByName_nameDoesntExist() {
            assertNull(userRepository.getByName("name4"));
        }

        @Test
        void getUserCache() {
            assertEquals(userRepository.getAll().size(), 3);
        }
    }

    @Nested
    @DisplayName("Testing CUD procedures")
    @TestMethodOrder(OrderAnnotation.class)
    class AccountRepositoryCUDTest {

        @Test
        @Order(1)
        void add() {
            User newUser = new User("name4", "surname4", "email4");
            userRepository.add(newUser);
            assertTrue(userRepository.getAll().contains(newUser));
        }

        @Test
        @Order(2)
        void delete() {
            userRepository.getAll().forEach(e -> System.out.println(e.getId()));
            User userToDelete = userRepository.getById(0);
            userRepository.delete(userToDelete.getId());
            assertFalse(userRepository.getAll().contains(userToDelete));
        }

        @Test
        void delete_accountDoesntExist() {
            assertNull(userRepository.delete(4));
        }

        @Test
        void update() {
            User updatedUser = new User("name1", "surname1", "newMail");
            userRepository.getAll().forEach(e -> System.out.println(e.getId()));
            userRepository.update(1, updatedUser);
            assertNotNull(userRepository.getById(1));
            assertEquals(userRepository.getById(1).getEmail(), "newMail");
        }

        @Test
        void update_accountDoesntExist() {
            User updatedUser = new User("name1", "surname1", "mail1");
            assertNull(userRepository.update(4, updatedUser));
        }
    }
}
