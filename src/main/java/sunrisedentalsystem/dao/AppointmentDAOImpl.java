package sunrisedentalsystem.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import sunrisedentalsystem.model.Appointment;
import sunrisedentalsystem.model.AppointmentStatus;
import sunrisedentalsystem.model.Dentist;
import sunrisedentalsystem.model.Patient;
import sunrisedentalsystem.model.Treatment;
import sunrisedentalsystem.util.DatabaseConnection;

public class AppointmentDAOImpl implements AppointmentDAO {

    @Override
    public boolean addAppointment(
            Appointment appointment,
            int staffId) throws SQLException {

        String sql =
                "INSERT INTO appointment " +
                "(patient_id, treatment_id, appointment_date, " +
                "appointment_time, status, dentist_id, staff_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection =
                DatabaseConnection.getConnection();

             PreparedStatement statement =
                connection.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )) {

            statement.setInt(
                    1,
                    appointment.getPatient().getPatientId()
            );

            statement.setInt(
                    2,
                    appointment.getTreatment().getTreatmentId()
            );

            statement.setDate(
                    3,
                    Date.valueOf(
                            appointment.getAppointmentDate()
                    )
            );

            statement.setTime(
                    4,
                    Time.valueOf(
                            appointment.getAppointmentTime()
                    )
            );

            statement.setString(
                    5,
                    appointment.getStatus().name()
            );

            statement.setInt(
                    6,
                    appointment.getDentist().getDentistId()
            );

            statement.setInt(
                    7,
                    staffId
            );

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {

                try (ResultSet generatedKeys =
                        statement.getGeneratedKeys()) {

                    if (generatedKeys.next()) {

                        appointment.setAppointmentNo(
                                String.valueOf(
                                        generatedKeys.getInt(1)
                                )
                        );
                    }
                }

                return true;
            }

            return false;
        }
    }

    @Override
    public Appointment getAppointmentByNumber(
            String appointmentNo) throws SQLException {

        String sql =
                "SELECT " +
                "a.appointment_no, " +
                "a.appointment_date, " +
                "a.appointment_time, " +
                "a.status, " +

                "p.patient_id, " +
                "p.patient_name, " +
                "p.address, " +
                "p.contact_number, " +

                "d.dentist_id, " +
                "d.dentist_name, " +

                "t.treatment_id, " +
                "t.treatment_type, " +
                "t.treatment_price " +

                "FROM appointment a " +

                "JOIN patient p " +
                "ON a.patient_id = p.patient_id " +

                "JOIN dentist d " +
                "ON a.dentist_id = d.dentist_id " +

                "JOIN treatment t " +
                "ON a.treatment_id = t.treatment_id " +

                "WHERE a.appointment_no = ?";

        try (Connection connection =
                DatabaseConnection.getConnection();

             PreparedStatement statement =
                connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    Integer.parseInt(appointmentNo)
            );

            try (ResultSet resultSet =
                    statement.executeQuery()) {

                if (resultSet.next()) {

                    Patient patient =
                            new Patient(
                                    resultSet.getInt(
                                            "patient_id"
                                    ),
                                    resultSet.getString(
                                            "patient_name"
                                    ),
                                    resultSet.getString(
                                            "address"
                                    ),
                                    resultSet.getString(
                                            "contact_number"
                                    )
                            );

                    Dentist dentist =
                            new Dentist(
                                    resultSet.getInt(
                                            "dentist_id"
                                    ),
                                    resultSet.getString(
                                            "dentist_name"
                                    )
                            );

                    Treatment treatment =
                            new Treatment(
                                    resultSet.getInt(
                                            "treatment_id"
                                    ),
                                    resultSet.getString(
                                            "treatment_type"
                                    ),
                                    resultSet.getDouble(
                                            "treatment_price"
                                    )
                            );

                    return new Appointment(
                            String.valueOf(
                                    resultSet.getInt(
                                            "appointment_no"
                                    )
                            ),

                            resultSet.getDate(
                                    "appointment_date"
                            ).toLocalDate(),

                            resultSet.getTime(
                                    "appointment_time"
                            ).toLocalTime(),

                            AppointmentStatus.valueOf(
                                    resultSet.getString(
                                            "status"
                                    )
                            ),

                            patient,
                            dentist,
                            treatment
                    );
                }
            }
        }

        return null;
    }

    @Override
    public boolean isAppointmentSlotAvailable(
            int dentistId,
            LocalDate appointmentDate,
            LocalTime appointmentTime)
            throws SQLException {

        String sql =
                "SELECT COUNT(*) " +
                "FROM appointment " +
                "WHERE dentist_id = ? " +
                "AND appointment_date = ? " +
                "AND appointment_time = ? " +
                "AND status <> 'CANCELLED'";

        try (Connection connection =
                DatabaseConnection.getConnection();

             PreparedStatement statement =
                connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    dentistId
            );

            statement.setDate(
                    2,
                    Date.valueOf(appointmentDate)
            );

            statement.setTime(
                    3,
                    Time.valueOf(appointmentTime)
            );

            try (ResultSet resultSet =
                    statement.executeQuery()) {

                if (resultSet.next()) {

                    int count = resultSet.getInt(1);

                    return count == 0;
                }
            }
        }

        return false;
    }
}