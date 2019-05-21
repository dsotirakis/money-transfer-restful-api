package repositories;

import models.Account;
import models.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is a test class for the repository generator.
 */
class RepositoryGeneratorTest {

    @BeforeAll
    static void setUp() {
        InMemoryDatabase.generateData();
    }

    @Nested
    @DisplayName("UserRepositoryGenerator Tests")
    class UserRepositoryGeneratorTest {

        @Test
        void getUserRepository_assertNotNull() {
            assertNotNull(RepositoryGenerator.getUserRepository());
        }

        @Test
        void getUserRepository_checkSize() {
            assertEquals(RepositoryGenerator.getUserRepository().getAll().size(), 5);
        }

        @Test
        void getUserRepository_checkIds() {
            Set<Integer> idSet = RepositoryGenerator.getUserRepository().getAll().stream()
                    .map(User::getId)
                    .collect(Collectors.toSet());
            assertTrue(idSet.containsAll(new HashSet<Integer>() {{ add(1); add(2); add(3); }}));
        }
    }

    @Nested
    @DisplayName("AccountRepositoryGenerator Tests")
    class AccountRepositoryGeneratorTest {

        @Test
        void getAccountRepository_assertNotNull() {
            assertNotNull(RepositoryGenerator.getAccountRepository());
        }

        @Test
        void getAccountRepository_checkSize() {
            assertEquals(RepositoryGenerator.getAccountRepository().getAll().size(), 5);
        }

        @Test
        void getAccountRepository_checkIds() {
            Set<Integer> idSet = RepositoryGenerator.getAccountRepository().getAll().stream()
                    .map(Account::getId)
                    .collect(Collectors.toSet());
            assertTrue(idSet.containsAll(new HashSet<Integer>() {{ add(1); add(2); add(3); }}));
        }
    }
}
