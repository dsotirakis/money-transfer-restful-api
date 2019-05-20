package repositories.impl;


import models.Account;
import models.User;
import org.junit.jupiter.api.*;
import repositories.AccountRepository;
import repositories.InMemoryDatabase;
import repositories.RepositoryGenerator;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.MethodOrderer.*;

class AccountRepositoryImplTest {

    private AccountRepository accountRepository = RepositoryGenerator.getAccountRepository();

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
            assertEquals(accountRepository.getAll().size(), 5);
        }

        @Test
        void getById() {
            assertEquals(accountRepository.getById(1).getId(), 1);
            assertEquals(accountRepository.getById(2).getId(), 2);
            assertEquals(accountRepository.getById(3).getId(), 3);
        }

        @Test
        void getById_idDoesntExist() {
            assertNull(accountRepository.getById(6));
        }

        @Test
        void getByUserName() {
            assertEquals(accountRepository.getByUsername("name1@gmail.com").getUsername(), "name1@gmail.com");
            assertEquals(accountRepository.getByUsername("name2@gmail.com").getUsername(), "name2@gmail.com");
            assertEquals(accountRepository.getByUsername("name3@gmail.com").getUsername(), "name3@gmail.com");
        }

        @Test
        void getByUserName_userNameDoesntExist() {
            assertNull(accountRepository.getByUsername("name6@gmail.com"));
        }

        // Check the static method behavior.
        @Test
        void getAccountCache() {
            assertEquals(RepositoryGenerator.getAccountRepository().getAll().size(), 5);
        }
    }

    @Nested
    @DisplayName("Testing CUD procedures")
    @TestMethodOrder(OrderAnnotation.class)
    class AccountRepositoryCUDTest {

        @Test
        @Order(1)
        void add() {
            RepositoryGenerator.getUserRepository().add(new User("newName", "newSurname", "name4@gmail.com"));
            Account newAccount = new Account("name4@gmail.com", "password4", 100.0, "USD");
            accountRepository.add(newAccount);
            assertTrue(accountRepository.getAll().contains(newAccount));
            RepositoryGenerator.getUserRepository().delete(0);
        }

        @Test
        void add_userForTheAccountDoesntExist() {
            Account newAccount = new Account("name6@gmail.com", "password4", 100.0, "USD");
            accountRepository.add(newAccount);
            assertFalse(accountRepository.getAll().contains(newAccount));
        }

        @Test
        void add_invalidCurrencyCode() {
            RepositoryGenerator.getUserRepository().add(new User("newName", "newSurname", "name4@gmail.com"));
            Account newAccount = new Account("name4@gmail.com", "password4", 100.0, "ZZZ");
            accountRepository.add(newAccount);
            assertFalse(accountRepository.getAll().contains(newAccount));
            RepositoryGenerator.getUserRepository().delete(0);
        }

        @Test
        @Order(2)
        void delete() {
            Account accountToDelete = accountRepository.getById(0);
            accountRepository.delete(accountToDelete.getId());
            assertFalse(accountRepository.getAll().contains(accountToDelete));
        }

        @Test
        void delete_accountDoesntExist() {
            assertEquals(accountRepository.delete(6).getStatus(), 404);
        }

        @Test
        void update() {
            Account updatedAccount = new Account("name1@gmail.com", "password1", 100.0, "USD");
            accountRepository.update(1, updatedAccount);
            assertNotNull(accountRepository.getById(1));
            assertEquals(accountRepository.getById(1).getBalance(), 100.0, 0.0);
        }

        @Test
        void update_accountDoesntExist() {
            Account updatedAccount = new Account("name1@gmail.com", "password1", 100.0, "USD");
            assertEquals(accountRepository.update(6, updatedAccount).getStatus(), 404);
        }
    }
}
