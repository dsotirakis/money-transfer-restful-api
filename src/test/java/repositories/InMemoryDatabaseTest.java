package repositories;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.MethodOrderer.*;

/**
 * Testing for the InMemoryDatabase class. This test class tests the connections and the possible exceptions that may
 * occur due to faulty or missing property files.
 */
@TestMethodOrder(OrderAnnotation.class)
class InMemoryDatabaseTest {

    // We want this test to run first and then close the connection when it ends, for the next tests which handle
    // exceptions to run properly.
    @Test
    @Order(1)
    void getConnection_connected() throws IOException, ClassNotFoundException, SQLException {
        Connection connection = InMemoryDatabase.getConnection();
        assertNotNull(connection);
    }

    @Test
    void getConnection_propertiesFileNotFound() throws NoSuchFieldException,
            IllegalAccessException,
            InstantiationException,
            NoSuchMethodException,
            InvocationTargetException {
        InMemoryDatabase object = InMemoryDatabase.class.getConstructor().newInstance();
        Field field = object.getClass().getDeclaredField("APPLICATION_PROPERTIES");

        field.setAccessible(true);
        field.set(object, "dummy/path");

        assertThrows(IOException.class, InMemoryDatabase::getConnection);
    }

    @Test
    void getConnection_emptyApplicationProperties() throws NoSuchFieldException,
            IllegalAccessException,
            InstantiationException,
            NoSuchMethodException,
            InvocationTargetException {
        InMemoryDatabase object = InMemoryDatabase.class.getConstructor().newInstance();
        Field field = object.getClass().getDeclaredField("APPLICATION_PROPERTIES");

        field.setAccessible(true);
        field.set(object, "src/test/resources/halfEmptyApplication.properties");

        assertThrows(SQLException.class, InMemoryDatabase::getConnection);
    }

    @Test
    void getGeneratedData_onlyCheckThatConnectionIsMade() {
        assertDoesNotThrow(InMemoryDatabase::generateData);
    }

    @AfterAll
    static void terminate() throws NoSuchFieldException,
            IllegalAccessException,
            InstantiationException,
            NoSuchMethodException,
            InvocationTargetException {
        InMemoryDatabase object = InMemoryDatabase.class.getConstructor().newInstance();
        Field field = object.getClass().getDeclaredField("APPLICATION_PROPERTIES");

        field.setAccessible(true);
        field.set(object, "src/main/resources/application.properties");

    }

}
