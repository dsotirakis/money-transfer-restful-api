package repositories.impl;


import models.Account;
import org.junit.jupiter.api.*;
import repositories.AccountRepository;
import repositories.InMemoryDatabase;
import repositories.RepositoryGenerator;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.MethodOrderer.*;

class AccountRepositoryImplTest {

    private AccountRepository accountRepository = new AccountRepositoryImpl();

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
            assertEquals(accountRepository.getAll().size(), 3);
        }

        @Test
        void getById() {
            assertEquals(accountRepository.getById(1).getId(), 1);
            assertEquals(accountRepository.getById(2).getId(), 2);
            assertEquals(accountRepository.getById(3).getId(), 3);
        }

        @Test
        void getById_idDoesntExist() {
            assertNull(accountRepository.getById(4));
        }

        @Test
        void getByUserName() {
            assertEquals(accountRepository.getByUsername("name1").getUsername(), "name1");
            assertEquals(accountRepository.getByUsername("name2").getUsername(), "name2");
            assertEquals(accountRepository.getByUsername("name3").getUsername(), "name3");
        }

        @Test
        void getByUserName_userNameDoesntExist() {
            assertNull(accountRepository.getByUsername("name4"));
        }

        // Check the static method behavior.
        @Test
        void getAccountCache() {
            assertEquals(RepositoryGenerator.getAccountRepository().getAll().size(), 3);
        }
    }

    @Nested
    @DisplayName("Testing CUD procedures")
    @TestMethodOrder(OrderAnnotation.class)
    class AccountRepositoryCUDTest {

        @Test
        @Order(1)
        void add() {
            Account newAccount = new Account("name4", "password4", 100.0);
            accountRepository.add(newAccount);
            assertTrue(accountRepository.getAll().contains(newAccount));
        }

        // TODO: 5/19/19 check id 0
        @Test
        @Order(2)
        void delete() {
            Account accountToDelete = accountRepository.getById(0);
            accountRepository.delete(accountToDelete.getId());
            assertFalse(accountRepository.getAll().contains(accountToDelete));
        }

        @Test
        void delete_accountDoesntExist() {
            assertThrows(NoSuchElementException.class, () -> assertNull(accountRepository.delete(4)));
        }

        @Test
        void update() {
            Account updatedAccount = new Account("name1", "password1", 100.0);
            accountRepository.update(1, updatedAccount);
            assertNotNull(accountRepository.getById(1));
            assertEquals(accountRepository.getById(1).getBalance(), 100.0, 0.0);
        }

        @Test
        void update_accountDoesntExist() {
            Account updatedAccount = new Account("name1", "password1", 100.0);
            assertThrows(NoSuchElementException.class, () -> assertNull(accountRepository.update(4, updatedAccount)));
        }
    }
}
