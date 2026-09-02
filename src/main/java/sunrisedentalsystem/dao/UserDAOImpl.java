package sunrisedentalsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import sunrisedentalsystem.model.Admin;
import sunrisedentalsystem.model.Staff;
import sunrisedentalsystem.model.User;
import sunrisedentalsystem.util.DatabaseConnection;

public class UserDAOImpl
        implements UserDAO {

    @Override
    public User getUserByUsername(
            String username)
            throws SQLException {

        String sql =
                "SELECT u.user_id, u.username, u.password_hash, " +
                "s.staff_id, s.staff_name, s.contact_number, " +
                "a.admin_name " +
                "FROM user u " +
                "LEFT JOIN staff s " +
                "ON u.user_id = s.user_id " +
                "LEFT JOIN admin a " +
                "ON u.user_id = a.user_id " +
                "WHERE u.username = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    username
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    int userId =
                            resultSet.getInt(
                                    "user_id"
                            );

                    String storedUsername =
                            resultSet.getString(
                                    "username"
                            );

                    String passwordHash =
                            resultSet.getString(
                                    "password_hash"
                            );

                    String adminName =
                            resultSet.getString(
                                    "admin_name"
                            );

                    String staffName =
                            resultSet.getString(
                                    "staff_name"
                            );

                    String contactNumber =
                            resultSet.getString(
                                    "contact_number"
                            );

                    if (adminName != null) {

                        return new Admin(
                                userId,
                                storedUsername,
                                passwordHash,
                                adminName
                        );
                    }

                    if (staffName != null) {

                        int staffId =
                                resultSet.getInt(
                                        "staff_id"
                                );

                        return new Staff(
                                staffId,
                                userId,
                                storedUsername,
                                passwordHash,
                                staffName,
                                contactNumber
                        );
                    }
                }
            }
        }

        return null;
    }
}