package repositories;

import org.h2.tools.RunScript;
import utilities.Config;

import java.io.FileReader;
import java.sql.*;

public class InMemoryDatabase {

    private static String APPLICATION_PROPERTIES = "src/main/resources/application.properties";

    public static Connection getConnection() {
        Config.loadProperties(APPLICATION_PROPERTIES);
        Connection connection;
        try {
            Class.forName(Config.getProperty("h2_driver"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(
                    Config.getProperty("h2_connection_url"),
                    Config.getProperty("h2_user"),
                    Config.getProperty("h2_password"));
            return connection;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void generateData() {
        Connection connection = null;
        try {
            connection = getConnection();
            RunScript.execute(connection, new FileReader("src/main/resources/schema-h2.sql"));
            RunScript.execute(connection, new FileReader("src/main/resources/data-h2.sql"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
