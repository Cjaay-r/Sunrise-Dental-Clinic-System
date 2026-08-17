package sunrisedentalsystem.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input =
                DatabaseConnection.class
                        .getClassLoader()
                        .getResourceAsStream("db.properties")) {

            if (input == null) {
                throw new RuntimeException(
                        "Database configuration file not found.");
            }

            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to load database configuration.", e);
        }
    }

    public static Connection getConnection() throws SQLException {

        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        return DriverManager.getConnection(
                url,
                username,
                password
        );
    }
}