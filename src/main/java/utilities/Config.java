package utilities;

import java.io.*;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * This is a Config class, which is responsible for returning the properties file to be used from the H2 In-Memory
 * database.
 */
public class Config {

    private static Properties properties = new Properties();

    /**
     * This method is responsible for loading the properties from the properties file.
     *
     * @param propertiesFile the properties file.
     *
     * @throws IOException in case the properties file doesn't exist.
     */
    public static void loadProperties(String propertiesFile) throws IOException {
        try {
            File file = new File(propertiesFile);
            FileInputStream fileInputStream = new FileInputStream(file);
            properties.load(fileInputStream);
            fileInputStream.close();
        } catch (IOException e) {
            throw new IOException("Properties file not found. Exiting...", e);
        }
    }

    /**
     * This method is responsible for getting certain properties as strings.
     *
     * @param property the property to be read.
     *
     * @return the value of the property as string.
     */
    public static String getProperty(String property) {
        return properties.getProperty(property);
    }
}
