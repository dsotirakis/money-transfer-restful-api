package repositories.impl;

import models.User;
import org.junit.jupiter.api.*;
import repositories.InMemoryDatabase;
import repositories.RepositoryGenerator;
import repositories.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.MethodOrderer.*;

class UserRepositoryImplTest {

    private UserRepository userRepository = RepositoryGenerator.getUserRepository();

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
            assertEquals(userRepository.getAll().size(), 5);
        }

        @Test
        void getById() {
            assertEquals(userRepository.getById(1).getId(), 1);
            assertEquals(userRepository.getById(2).getId(), 2);
            assertEquals(userRepository.getById(3).getId(), 3);
        }

        @Test
        void getById_idDoesntExist() {
            assertNull(userRepository.getById(6));
        }

        @Test
        void getByName() {
            assertEquals(userRepository.getByName("name1").getName(), "name1");
            assertEquals(userRepository.getByName("name2").getName(), "name2");
            assertEquals(userRepository.getByName("name3").getName(), "name3");
        }

        @Test
        void getByName_nameDoesntExist() {
            assertNull(userRepository.getByName("name6"));
        }

        @Test
        void getBySurname() {
            assertEquals(userRepository.getBySurname("surname1").getSurname(), "surname1");
            assertEquals(userRepository.getBySurname("surname2").getSurname(), "surname2");
            assertEquals(userRepository.getBySurname("surname3").getSurname(), "surname3");
        }

        @Test
        void getBySurname_surnameDoesntExist() {
            assertNull(userRepository.getBySurname("name4"));
        }

        @Test
        void getByMail() {
            assertEquals(userRepository.getByMail("name1@gmail.com").getEmail(), "name1@gmail.com");
            assertEquals(userRepository.getByMail("name2@gmail.com").getEmail(), "name2@gmail.com");
            assertEquals(userRepository.getByMail("name3@gmail.com").getEmail(), "name3@gmail.com");
        }

        @Test
        void getByMail_mailDoesntExist() {
            assertNull(userRepository.getByMail("mail4"));
        }

        @Test
        void getUserCache() {
            assertEquals(userRepository.getAll().size(), 5);
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
        @Order(3)
        void delete() {
            User userToDelete = userRepository.getById(0);
            userRepository.delete(userToDelete.getId());
            assertFalse(userRepository.getAll().contains(userToDelete));
        }

        @Test
        void delete_userDoesntExist() {
            assertEquals(userRepository.delete(6).getStatus(), 404);
        }

        @Test
        @Order(2)
        void update() {
            User updatedUser = new User("name1", "surname1", "newMail");
            userRepository.update(1, updatedUser);
            assertNotNull(userRepository.getById(1));
            assertEquals(userRepository.getById(1).getEmail(), "newMail");

            // Revert user to initial state.
            updatedUser = new User("name1", "surname1", "name1@gmail.com");
            userRepository.update(1, updatedUser);
        }

        @Test
        void update_userDoesntExist() {
            User updatedUser = new User("name1", "surname1", "mail1");
            assertEquals(userRepository.update(6, updatedUser).getStatus(), 404);
        }
    }
}
