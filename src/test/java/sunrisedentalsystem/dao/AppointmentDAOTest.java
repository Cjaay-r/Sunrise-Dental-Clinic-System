package sunrisedentalsystem.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import sunrisedentalsystem.model.Appointment;
import sunrisedentalsystem.model.AppointmentStatus;
import sunrisedentalsystem.model.Dentist;
import sunrisedentalsystem.model.Patient;
import sunrisedentalsystem.model.Treatment;
import sunrisedentalsystem.util.DatabaseConnection;

class AppointmentDAOTest {

    private AppointmentDAO appointmentDAO;

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    private MockedStatic<DatabaseConnection>
            databaseConnectionMock;

    @BeforeEach
    void setUp() {

        connection = mock(Connection.class);

        preparedStatement =
                mock(PreparedStatement.class);

        resultSet = mock(ResultSet.class);

        databaseConnectionMock =
                mockStatic(DatabaseConnection.class);

        databaseConnectionMock
                .when(DatabaseConnection::getConnection)
                .thenReturn(connection);

        appointmentDAO =
                new AppointmentDAOImpl();
    }

    @AfterEach
    void tearDown() {
        databaseConnectionMock.close();
    }

    @Test
    void shouldAddAppointment() throws Exception {

        Patient patient = new Patient(
                1,
                "John",
                "Colombo",
                "0771234567"
        );

        Dentist dentist = new Dentist(
                2,
                "Dr. Silva",
                "General Dentistry",
                "0711111111"
        );

        Treatment treatment = new Treatment(
                3,
                "Cleaning",
                5000.00
        );

        AppointmentStatus status =
                AppointmentStatus.values()[0];

        Appointment appointment =
                new Appointment(
                        "0",
                        LocalDate.of(2026, 9, 10),
                        LocalTime.of(10, 30),
                        status,
                        patient,
                        dentist,
                        treatment
                );

        when(connection.prepareStatement(
                anyString(),
                eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(preparedStatement);

        when(preparedStatement.executeUpdate())
                .thenReturn(1);

        when(preparedStatement.getGeneratedKeys())
                .thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(true);

        when(resultSet.getInt(1))
                .thenReturn(15);

        boolean result =
                appointmentDAO.addAppointment(
                        appointment,
                        2
                );

        assertTrue(result);

        assertEquals(
                "15",
                appointment.getAppointmentNo()
        );

        verify(preparedStatement)
                .setInt(1, 1);

        verify(preparedStatement)
                .setInt(2, 3);

        verify(preparedStatement)
                .setDate(
                        3,
                        Date.valueOf(
                                LocalDate.of(
                                        2026,
                                        9,
                                        10
                                )
                        )
                );

        verify(preparedStatement)
                .setTime(
                        4,
                        Time.valueOf(
                                LocalTime.of(
                                        10,
                                        30
                                )
                        )
                );

        verify(preparedStatement)
                .setString(5, status.name());

        verify(preparedStatement)
                .setInt(6, 2);

        verify(preparedStatement)
                .setInt(7, 2);
    }

    @Test
    void shouldGetAppointmentByNumber()
            throws Exception {

        AppointmentStatus status =
                AppointmentStatus.values()[0];

        when(connection.prepareStatement(anyString()))
                .thenReturn(preparedStatement);

        when(preparedStatement.executeQuery())
                .thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(true);

        when(resultSet.getInt("appointment_no"))
                .thenReturn(10);

        when(resultSet.getDate("appointment_date"))
                .thenReturn(
                        Date.valueOf("2026-09-10")
                );

        when(resultSet.getTime("appointment_time"))
                .thenReturn(
                        Time.valueOf("09:30:00")
                );

        when(resultSet.getString("status"))
                .thenReturn(status.name());

        when(resultSet.getInt("patient_id"))
                .thenReturn(1);

        when(resultSet.getString("patient_name"))
                .thenReturn("John");

        when(resultSet.getString("address"))
                .thenReturn("Colombo");

        when(resultSet.getString("contact_number"))
                .thenReturn("0771234567");

        when(resultSet.getInt("dentist_id"))
                .thenReturn(2);

        when(resultSet.getString("dentist_name"))
                .thenReturn("Dr. Silva");

        when(resultSet.getInt("treatment_id"))
                .thenReturn(3);

        when(resultSet.getString("treatment_type"))
                .thenReturn("Cleaning");

        when(resultSet.getDouble("treatment_price"))
                .thenReturn(5000.00);

        Appointment appointment =
                appointmentDAO
                        .getAppointmentByNumber("10");

        assertNotNull(appointment);

        assertEquals(
                "10",
                appointment.getAppointmentNo()
        );

        assertEquals(
                "John",
                appointment
                        .getPatient()
                        .getPatientName()
        );

        assertEquals(
                "Dr. Silva",
                appointment
                        .getDentist()
                        .getDentistName()
        );

        assertEquals(
                "Cleaning",
                appointment
                        .getTreatment()
                        .getTreatmentType()
        );

        verify(preparedStatement)
                .setInt(1, 10);
    }

    @Test
    void shouldReturnNullWhenAppointmentDoesNotExist()
            throws Exception {

        when(connection.prepareStatement(anyString()))
                .thenReturn(preparedStatement);

        when(preparedStatement.executeQuery())
                .thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(false);

        Appointment appointment =
                appointmentDAO
                        .getAppointmentByNumber("999");

        assertNull(appointment);
    }

    @Test
    void shouldReturnTrueWhenAppointmentSlotIsAvailable()
            throws Exception {

        when(connection.prepareStatement(anyString()))
                .thenReturn(preparedStatement);

        when(preparedStatement.executeQuery())
                .thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(true);

        when(resultSet.getInt(1))
                .thenReturn(0);

        boolean available =
                appointmentDAO
                        .isAppointmentSlotAvailable(
                                2,
                                LocalDate.of(
                                        2026,
                                        9,
                                        12
                                ),
                                LocalTime.of(
                                        11,
                                        0
                                )
                        );

        assertTrue(available);
    }

    @Test
    void shouldReturnFalseWhenAppointmentSlotIsTaken()
            throws Exception {

        when(connection.prepareStatement(anyString()))
                .thenReturn(preparedStatement);

        when(preparedStatement.executeQuery())
                .thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(true);

        when(resultSet.getInt(1))
                .thenReturn(1);

        boolean available =
                appointmentDAO
                        .isAppointmentSlotAvailable(
                                2,
                                LocalDate.of(
                                        2026,
                                        9,
                                        12
                                ),
                                LocalTime.of(
                                        11,
                                        0
                                )
                        );

        assertFalse(available);
    }
}