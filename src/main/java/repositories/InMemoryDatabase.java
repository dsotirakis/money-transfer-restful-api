package repositories;

import utilities.Config;

import java.sql.*;

public class InMemoryDatabase {

    static String APPLICATION_PROPERTIES = "src/main/resources/application.properties";

    InMemoryDatabase() {
        Config.loadProperties(APPLICATION_PROPERTIES);
    }

    public static Connection getConnection() {

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

}
