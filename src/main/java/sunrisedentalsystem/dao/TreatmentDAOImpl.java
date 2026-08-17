package sunrisedentalsystem.dao;

import sunrisedentalsystem.model.Treatment;
import sunrisedentalsystem.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TreatmentDAOImpl implements TreatmentDAO {

    @Override
    public void addTreatment(Treatment treatment) throws SQLException {

        String sql = "INSERT INTO treatment (treatment_type, treatment_price) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, treatment.getTreatmentType());
            statement.setDouble(2, treatment.getTreatmentPrice());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    treatment.setTreatmentId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public Treatment getTreatmentById(int treatmentId) throws SQLException {

        String sql = "SELECT treatment_id, treatment_type, treatment_price " +
                     "FROM treatment WHERE treatment_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, treatmentId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    return new Treatment(
                            resultSet.getInt("treatment_id"),
                            resultSet.getString("treatment_type"),
                            resultSet.getDouble("treatment_price")
                    );
                }
            }
        }

        return null;
    }

    @Override
    public List<Treatment> getAllTreatments() throws SQLException {

        String sql = "SELECT treatment_id, treatment_type, treatment_price FROM treatment";

        List<Treatment> treatments = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Treatment treatment = new Treatment(
                        resultSet.getInt("treatment_id"),
                        resultSet.getString("treatment_type"),
                        resultSet.getDouble("treatment_price")
                );

                treatments.add(treatment);
            }
        }

        return treatments;
    }

    @Override
    public void updateTreatment(Treatment treatment) throws SQLException {

        String sql = "UPDATE treatment " +
                     "SET treatment_type = ?, treatment_price = ? " +
                     "WHERE treatment_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, treatment.getTreatmentType());
            statement.setDouble(2, treatment.getTreatmentPrice());
            statement.setInt(3, treatment.getTreatmentId());

            statement.executeUpdate();
        }
    }

    @Override
    public void deleteTreatment(int treatmentId) throws SQLException {

        String sql = "DELETE FROM treatment WHERE treatment_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, treatmentId);

            statement.executeUpdate();
        }
    }
}