package sunrisedentalsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import sunrisedentalsystem.model.Patient;
import sunrisedentalsystem.util.DatabaseConnection;

public class PatientDAOImpl implements PatientDAO {

    @Override
    public boolean addPatient(Patient patient) throws SQLException {

        String sql = "INSERT INTO patient "
                + "(patient_name, address, contact_number) "
                + "VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, patient.getPatientName());
            statement.setString(2, patient.getAddress());
            statement.setString(3, patient.getContactNumber());

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {

                    if (generatedKeys.next()) {
                        patient.setPatientId(generatedKeys.getInt(1));
                    }
                }

                return true;
            }

            return false;
        }
    }

    @Override
    public Patient getPatientById(int patientId) throws SQLException {

        String sql =
                "SELECT * FROM patient WHERE patient_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, patientId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    return new Patient(
                            resultSet.getInt("patient_id"),
                            resultSet.getString("patient_name"),
                            resultSet.getString("address"),
                            resultSet.getString("contact_number")
                    );
                }
            }
        }

        return null;
    }
    
    @Override
    public Patient getPatientByContactNumber(
            String contactNumber) throws SQLException {

        String sql =
                "SELECT patient_id, patient_name, address, contact_number " +
                "FROM patient " +
                "WHERE contact_number = ?";

        try (Connection connection =
                DatabaseConnection.getConnection();

             PreparedStatement statement =
                connection.prepareStatement(sql)) {

            statement.setString(1, contactNumber);

            try (ResultSet resultSet =
                    statement.executeQuery()) {

                if (resultSet.next()) {

                    return new Patient(
                            resultSet.getInt("patient_id"),
                            resultSet.getString("patient_name"),
                            resultSet.getString("address"),
                            resultSet.getString("contact_number")
                    );
                }
            }
        }

        return null;
    }
}