package sunrisedentalsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import sunrisedentalsystem.model.Dentist;
import sunrisedentalsystem.util.DatabaseConnection;

public class DentistDAOImpl implements DentistDAO {

    @Override
    public boolean addDentist(Dentist dentist)
            throws SQLException {

        String sql =
                "INSERT INTO dentist "
                + "(dentist_name, specialization, contact_number) "
                + "VALUES (?, ?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS
                     )) {

            statement.setString(
                    1,
                    dentist.getDentistName()
            );

            statement.setString(
                    2,
                    dentist.getSpecialization()
            );

            statement.setString(
                    3,
                    dentist.getContactNumber()
            );

            int rowsAffected =
                    statement.executeUpdate();

            if (rowsAffected > 0) {

                try (ResultSet generatedKeys =
                             statement.getGeneratedKeys()) {

                    if (generatedKeys.next()) {

                        dentist.setDentistId(
                                generatedKeys.getInt(1)
                        );
                    }
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public Dentist getDentistById(int dentistId)
            throws SQLException {

        String sql =
                "SELECT dentist_id, dentist_name, "
                + "specialization, contact_number "
                + "FROM dentist "
                + "WHERE dentist_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    dentistId
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return new Dentist(
                            resultSet.getInt(
                                    "dentist_id"
                            ),
                            resultSet.getString(
                                    "dentist_name"
                            ),
                            resultSet.getString(
                                    "specialization"
                            ),
                            resultSet.getString(
                                    "contact_number"
                            )
                    );
                }
            }
        }

        return null;
    }

    @Override
    public List<Dentist> getAllDentists()
            throws SQLException {

        List<Dentist> dentists =
                new ArrayList<>();

        String sql =
                "SELECT dentist_id, dentist_name, "
                + "specialization, contact_number "
                + "FROM dentist "
                + "ORDER BY dentist_name";

        try (Connection connection =
                     DatabaseConnection.getConnection();

             Statement statement =
                     connection.createStatement();

             ResultSet resultSet =
                     statement.executeQuery(sql)) {

            while (resultSet.next()) {

                Dentist dentist =
                        new Dentist(
                                resultSet.getInt(
                                        "dentist_id"
                                ),
                                resultSet.getString(
                                        "dentist_name"
                                ),
                                resultSet.getString(
                                        "specialization"
                                ),
                                resultSet.getString(
                                        "contact_number"
                                )
                        );

                dentists.add(dentist);
            }
        }

        return dentists;
    }

    @Override
    public boolean updateDentist(Dentist dentist)
            throws SQLException {

        String sql =
                "UPDATE dentist "
                + "SET dentist_name = ?, "
                + "specialization = ?, "
                + "contact_number = ? "
                + "WHERE dentist_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    dentist.getDentistName()
            );

            statement.setString(
                    2,
                    dentist.getSpecialization()
            );

            statement.setString(
                    3,
                    dentist.getContactNumber()
            );

            statement.setInt(
                    4,
                    dentist.getDentistId()
            );

            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteDentist(int dentistId)
            throws SQLException {

        String sql =
                "DELETE FROM dentist "
                + "WHERE dentist_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    dentistId
            );

            return statement.executeUpdate() > 0;
        }
    }
}