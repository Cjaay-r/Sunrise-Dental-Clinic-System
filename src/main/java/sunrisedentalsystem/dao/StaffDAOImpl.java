package sunrisedentalsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import sunrisedentalsystem.model.Staff;
import sunrisedentalsystem.util.DatabaseConnection;

public class StaffDAOImpl
        implements StaffDAO {

    @Override
    public boolean addStaff(
            Staff staff)
            throws SQLException {

    	String userSql =
    	        "INSERT INTO user " +
    	        "(username, password_hash) " +
    	        "VALUES (?, ?)";

        String staffSql =
                "INSERT INTO staff " +
                "(user_id, staff_name, contact_number) " +
                "VALUES (?, ?, ?)";

        Connection connection =
                DatabaseConnection.getConnection();

        try {

            connection.setAutoCommit(false);

            int userId;

            try (PreparedStatement userStatement =
                         connection.prepareStatement(
                                 userSql,
                                 Statement.RETURN_GENERATED_KEYS
                         )) {

                userStatement.setString(
                        1,
                        staff.getUsername()
                );

                userStatement.setString(
                        2,
                        staff.getPassword()
                );

                int userRows =
                        userStatement.executeUpdate();

                if (userRows == 0) {

                    connection.rollback();

                    return false;
                }

                try (ResultSet generatedKeys =
                             userStatement.getGeneratedKeys()) {

                    if (!generatedKeys.next()) {

                        connection.rollback();

                        return false;
                    }

                    userId =
                            generatedKeys.getInt(1);
                }
            }

            int staffId;

            try (PreparedStatement staffStatement =
                         connection.prepareStatement(
                                 staffSql,
                                 Statement.RETURN_GENERATED_KEYS
                         )) {

                staffStatement.setInt(
                        1,
                        userId
                );

                staffStatement.setString(
                        2,
                        staff.getStaffName()
                );

                staffStatement.setString(
                        3,
                        staff.getContactNumber()
                );

                int staffRows =
                        staffStatement.executeUpdate();

                if (staffRows == 0) {

                    connection.rollback();

                    return false;
                }

                try (ResultSet generatedKeys =
                             staffStatement.getGeneratedKeys()) {

                    if (!generatedKeys.next()) {

                        connection.rollback();

                        return false;
                    }

                    staffId =
                            generatedKeys.getInt(1);
                }
            }

            connection.commit();

            staff.setUserId(
                    userId
            );

            staff.setStaffId(
                    staffId
            );

            return true;

        } catch (SQLException e) {

            connection.rollback();

            throw e;

        } finally {

            try {

                connection.setAutoCommit(
                        true
                );

            } finally {

                connection.close();
            }
        }
    }

    @Override
    public Staff getStaffById(
            int staffId)
            throws SQLException {

        String sql =
                "SELECT s.staff_id, s.user_id, " +
                "s.staff_name, s.contact_number, " +
                "u.username, u.password_hash " +
                "FROM staff s " +
                "JOIN user u " +
                "ON s.user_id = u.user_id " +
                "WHERE s.staff_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    staffId
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return createStaff(
                            resultSet
                    );
                }
            }
        }

        return null;
    }

    @Override
    public List<Staff> searchStaffByName(
            String staffName)
            throws SQLException {

        String sql =
                "SELECT s.staff_id, s.user_id, " +
                "s.staff_name, s.contact_number, " +
                "u.username, u.password_hash " +
                "FROM staff s " +
                "JOIN user u " +
                "ON s.user_id = u.user_id " +
                "WHERE LOWER(s.staff_name) LIKE LOWER(?) " +
                "ORDER BY s.staff_name ASC";

        List<Staff> staffList =
                new ArrayList<>();

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    "%" + staffName.trim() + "%"
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    staffList.add(
                            createStaff(
                                    resultSet
                            )
                    );
                }
            }
        }

        return staffList;
    }

    @Override
    public List<Staff> getAllStaff()
            throws SQLException {

        String sql =
                "SELECT s.staff_id, s.user_id, " +
                "s.staff_name, s.contact_number, " +
                "u.username, u.password_hash " +
                "FROM staff s " +
                "JOIN user u " +
                "ON s.user_id = u.user_id " +
                "ORDER BY s.staff_id ASC";

        List<Staff> staffList =
                new ArrayList<>();

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql);

             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                staffList.add(
                        createStaff(
                                resultSet
                        )
                );
            }
        }

        return staffList;
    }

    @Override
    public boolean updateStaff(
            Staff staff)
            throws SQLException {

        String userSql =
                "UPDATE user " +
                "SET username = ? " +
                "WHERE user_id = ?";

        String staffSql =
                "UPDATE staff " +
                "SET staff_name = ?, " +
                "contact_number = ? " +
                "WHERE staff_id = ?";

        Connection connection =
                DatabaseConnection.getConnection();

        try {

            connection.setAutoCommit(false);

            try (PreparedStatement userStatement =
                         connection.prepareStatement(
                                 userSql
                         )) {

                userStatement.setString(
                        1,
                        staff.getUsername()
                );

                userStatement.setInt(
                        2,
                        staff.getUserId()
                );

                userStatement.executeUpdate();
            }

            int affectedRows;

            try (PreparedStatement staffStatement =
                         connection.prepareStatement(
                                 staffSql
                         )) {

                staffStatement.setString(
                        1,
                        staff.getStaffName()
                );

                staffStatement.setString(
                        2,
                        staff.getContactNumber()
                );

                staffStatement.setInt(
                        3,
                        staff.getStaffId()
                );

                affectedRows =
                        staffStatement.executeUpdate();
            }

            if (affectedRows == 0) {

                connection.rollback();

                return false;
            }

            connection.commit();

            return true;

        } catch (SQLException e) {

            connection.rollback();

            throw e;

        } finally {

            try {

                connection.setAutoCommit(
                        true
                );

            } finally {

                connection.close();
            }
        }
    }

    @Override
    public boolean usernameExists(
            String username)
            throws SQLException {

        String sql =
                "SELECT COUNT(*) " +
                "FROM user " +
                "WHERE username = ?";

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

                    return resultSet.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    private Staff createStaff(
            ResultSet resultSet)
            throws SQLException {

        return new Staff(
                resultSet.getInt(
                        "staff_id"
                ),
                resultSet.getInt(
                        "user_id"
                ),
                resultSet.getString(
                        "username"
                ),
                resultSet.getString(
                        "password_hash"
                ),
                resultSet.getString(
                        "staff_name"
                ),
                resultSet.getString(
                        "contact_number"
                )
        );
    }
}