package sunrisedentalsystem.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sunrisedentalsystem.model.Appointment;
import sunrisedentalsystem.model.AppointmentStatus;
import sunrisedentalsystem.model.Dentist;
import sunrisedentalsystem.model.Patient;
import sunrisedentalsystem.model.Treatment;
import sunrisedentalsystem.util.DatabaseConnection;

class AppointmentDAOTest {

    private AppointmentDAO appointmentDAO;
    private PatientDAO patientDAO;

    private Patient testPatient;
    private Dentist testDentist;
    private Treatment testTreatment;
    private int staffId;

    @BeforeEach
    void setUp() throws Exception {

        appointmentDAO = new AppointmentDAOImpl();
        patientDAO = new PatientDAOImpl();

        testPatient = new Patient(
                0,
                "Appointment Test Patient",
                "Colombo",
                "0771234567"
        );

        patientDAO.addPatient(testPatient);

        testDentist = loadFirstDentist();
        testTreatment = loadFirstTreatment();
        staffId = loadFirstStaffId();
    }

    @AfterEach
    void cleanUp() throws SQLException {

        if (testPatient != null &&
                testPatient.getPatientId() > 0) {

            try (Connection connection =
                    DatabaseConnection.getConnection()) {

                String deleteAppointmentSql =
                        "DELETE FROM appointment WHERE patient_id = ?";

                try (PreparedStatement statement =
                        connection.prepareStatement(deleteAppointmentSql)) {

                    statement.setInt(
                            1,
                            testPatient.getPatientId()
                    );

                    statement.executeUpdate();
                }

                String deletePatientSql =
                        "DELETE FROM patient WHERE patient_id = ?";

                try (PreparedStatement statement =
                        connection.prepareStatement(deletePatientSql)) {

                    statement.setInt(
                            1,
                            testPatient.getPatientId()
                    );

                    statement.executeUpdate();
                }
            }
        }
    }

    @Test
    void shouldAddAppointmentToDatabase() throws Exception {

        Appointment appointment = createTestAppointment();

        boolean result =
                appointmentDAO.addAppointment(
                        appointment,
                        staffId
                );

        assertTrue(result);
    }

    @Test
    void shouldSetGeneratedAppointmentNumber() throws Exception {

        Appointment appointment = createTestAppointment();

        appointmentDAO.addAppointment(
                appointment,
                staffId
        );

        assertNotNull(
                appointment.getAppointmentNo()
        );
    }

    @Test
    void shouldGetAppointmentByNumber() throws Exception {

        Appointment appointment = createTestAppointment();

        appointmentDAO.addAppointment(
                appointment,
                staffId
        );

        Appointment result =
                appointmentDAO.getAppointmentByNumber(
                        appointment.getAppointmentNo()
                );

        assertNotNull(result);

        assertEquals(
                appointment.getAppointmentNo(),
                result.getAppointmentNo()
        );

        assertEquals(
                LocalDate.of(2030, 1, 15),
                result.getAppointmentDate()
        );

        assertEquals(
                LocalTime.of(10, 30),
                result.getAppointmentTime()
        );

        assertEquals(
                AppointmentStatus.SCHEDULED,
                result.getStatus()
        );

        assertEquals(
                testPatient.getPatientId(),
                result.getPatient().getPatientId()
        );

        assertEquals(
                testDentist.getDentistId(),
                result.getDentist().getDentistId()
        );

        assertEquals(
                testTreatment.getTreatmentId(),
                result.getTreatment().getTreatmentId()
        );
    }

    @Test
    void shouldReturnFalseWhenDentistSlotIsAlreadyBooked()
            throws Exception {

        Appointment appointment = createTestAppointment();

        appointmentDAO.addAppointment(
                appointment,
                staffId
        );

        boolean available =
                appointmentDAO.isAppointmentSlotAvailable(
                        testDentist.getDentistId(),
                        LocalDate.of(2030, 1, 15),
                        LocalTime.of(10, 30)
                );

        assertFalse(available);
    }

    @Test
    void shouldReturnTrueWhenDentistSlotIsAvailable()
            throws Exception {

        boolean available =
                appointmentDAO.isAppointmentSlotAvailable(
                        testDentist.getDentistId(),
                        LocalDate.of(2030, 1, 16),
                        LocalTime.of(11, 30)
                );

        assertTrue(available);
    }

    private Appointment createTestAppointment() {

        return new Appointment(
                null,
                LocalDate.of(2030, 1, 15),
                LocalTime.of(10, 30),
                AppointmentStatus.SCHEDULED,
                testPatient,
                testDentist,
                testTreatment
        );
    }

    private Dentist loadFirstDentist()
            throws SQLException {

        String sql =
                "SELECT dentist_id, dentist_name " +
                "FROM dentist LIMIT 1";

        try (Connection connection =
                DatabaseConnection.getConnection();

             Statement statement =
                connection.createStatement();

             ResultSet resultSet =
                statement.executeQuery(sql)) {

            if (resultSet.next()) {

                return new Dentist(
                        resultSet.getInt("dentist_id"),
                        resultSet.getString("dentist_name")
                );
            }
        }

        throw new IllegalStateException(
                "Dentist table must contain at least one dentist."
        );
    }

    private Treatment loadFirstTreatment()
            throws SQLException {

        String sql =
                "SELECT treatment_id, treatment_type, treatment_price " +
                "FROM treatment LIMIT 1";

        try (Connection connection =
                DatabaseConnection.getConnection();

             Statement statement =
                connection.createStatement();

             ResultSet resultSet =
                statement.executeQuery(sql)) {

            if (resultSet.next()) {

                return new Treatment(
                        resultSet.getInt("treatment_id"),
                        resultSet.getString("treatment_type"),
                        resultSet.getDouble("treatment_price")
                );
            }
        }

        throw new IllegalStateException(
                "Treatment table must contain at least one treatment."
        );
    }

    private int loadFirstStaffId()
            throws SQLException {

        String sql =
                "SELECT staff_id FROM staff LIMIT 1";

        try (Connection connection =
                DatabaseConnection.getConnection();

             Statement statement =
                connection.createStatement();

             ResultSet resultSet =
                statement.executeQuery(sql)) {

            if (resultSet.next()) {

                return resultSet.getInt("staff_id");
            }
        }

        throw new IllegalStateException(
                "Staff table must contain at least one staff member."
        );
    }
}