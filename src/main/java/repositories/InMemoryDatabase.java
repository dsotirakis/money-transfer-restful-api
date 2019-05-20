package repositories;

import org.h2.tools.RunScript;
import utilities.Config;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class InMemoryDatabase {

    private static String APPLICATION_PROPERTIES = "src/main/resources/application.properties";
    private static boolean DATA_GENERATED = false;

    public static Connection getConnection() throws IOException, ClassNotFoundException, SQLException {
        Config.loadProperties(APPLICATION_PROPERTIES);
        Connection connection;
        try {
            Class.forName(Config.getProperty("h2_driver"));
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("H2 Driver not found", e);
        }
        try {
            connection = DriverManager.getConnection(
                    Config.getProperty("h2_connection_url"),
                    Config.getProperty("h2_user"),
                    Config.getProperty("h2_password"));
            return connection;
        }
        catch (SQLException e) {
            throw new SQLException("Driver Manager couldn't connect", e);
        }
    }

    public static void generateData() {
        if (DATA_GENERATED)
            return;
        Connection connection;
        try {
            connection = getConnection();
            RunScript.execute(connection, new FileReader("src/main/resources/schema-h2.sql"));
            RunScript.execute(connection, new FileReader("src/main/resources/data-h2.sql"));
            DATA_GENERATED = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
