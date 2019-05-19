package utilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test for the Config class.
 */
class ConfigTest {

    @BeforeEach
    void setUp() throws IOException {
        String APPLICATION_PROPERTIES = "src/test/resources/application.properties";
        Config.loadProperties(APPLICATION_PROPERTIES);
    }

    @Test
    void getH2Driver() {
        assertEquals(Config.getProperty("h2_driver"), "org.h2.Driver");
    }

    @Test
    void getH2ConnectionUrl() {
        assertEquals(Config.getProperty("h2_connection_url"), "jdbc:h2:mem:test_database;DB_CLOSE_DELAY=-1");
    }

    @Test
    void getH2User() {
        assertEquals(Config.getProperty("h2_user"), "sa");
    }

    @Test
    void getH2Password() {
        assertEquals(Config.getProperty("h2_password"), "");
    }

    @Test
    void propertiesFileNotFound() {
        assertThrows(IOException.class, () -> Config.loadProperties("dummy/dir"));
    }
}
