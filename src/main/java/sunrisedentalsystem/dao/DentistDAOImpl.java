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
    public Dentist getDentistById(int dentistId)
            throws SQLException {

        String sql =
                "SELECT dentist_id, dentist_name " +
                "FROM dentist " +
                "WHERE dentist_id = ?";

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
                "SELECT dentist_id, dentist_name " +
                "FROM dentist " +
                "ORDER BY dentist_name";

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
                                )
                        );

                dentists.add(dentist);
            }
        }

        return dentists;
    }
}