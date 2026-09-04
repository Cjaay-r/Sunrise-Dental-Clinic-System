package sunrisedentalsystem.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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

        connection =
                mock(Connection.class);

        preparedStatement =
                mock(PreparedStatement.class);

        resultSet =
                mock(ResultSet.class);

        databaseConnectionMock =
                mockStatic(
                        DatabaseConnection.class
                );

        databaseConnectionMock
                .when(
                        DatabaseConnection::getConnection
                )
                .thenReturn(
                        connection
                );

        appointmentDAO =
                new AppointmentDAOImpl();
    }

    @AfterEach
    void tearDown() {

        databaseConnectionMock.close();
    }

    @Test
    void shouldAddAppointment()
            throws Exception {

        Patient patient =
                new Patient(
                        1,
                        "John",
                        "Colombo",
                        "0771234567"
                );

        Dentist dentist =
                new Dentist(
                        2,
                        "Dr. Silva",
                        "General Dentistry",
                        "0711111111"
                );

        Treatment treatment =
                new Treatment(
                        3,
                        "Cleaning",
                        5000.00
                );

        Appointment appointment =
                new Appointment(
                        null,
                        LocalDate.of(
                                2026,
                                9,
                                10
                        ),
                        LocalTime.of(
                                10,
                                30
                        ),
                        AppointmentStatus.SCHEDULED,
                        patient,
                        dentist,
                        treatment
                );

        when(connection.prepareStatement(
                anyString(),
                eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(
                        preparedStatement
                );

        when(preparedStatement.executeUpdate())
                .thenReturn(1);

        when(preparedStatement.getGeneratedKeys())
                .thenReturn(
                        resultSet
                );

        when(resultSet.next())
                .thenReturn(true);

        when(resultSet.getInt(1))
                .thenReturn(15);

        boolean result =
                appointmentDAO
                        .addAppointment(
                                appointment,
                                2
                        );

        assertTrue(result);

        assertEquals(
                "15",
                appointment.getAppointmentNo()
        );

        verify(preparedStatement)
                .setInt(
                        1,
                        1
                );

        verify(preparedStatement)
                .setInt(
                        2,
                        3
                );

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
                .setString(
                        5,
                        AppointmentStatus
                                .SCHEDULED
                                .name()
                );

        verify(preparedStatement)
                .setInt(
                        6,
                        2
                );

        verify(preparedStatement)
                .setInt(
                        7,
                        2
                );
    }

    @Test
    void shouldGetAppointmentByNumber()
            throws Exception {

        when(connection.prepareStatement(
                anyString()))
                .thenReturn(
                        preparedStatement
                );

        when(preparedStatement.executeQuery())
                .thenReturn(
                        resultSet
                );

        when(resultSet.next())
                .thenReturn(true);

        stubAppointmentResult(
                10,
                "John",
                "Dr. Silva",
                "Cleaning",
                AppointmentStatus.SCHEDULED
        );

        Appointment appointment =
                appointmentDAO
                        .getAppointmentByNumber(
                                "10"
                        );

        assertNotNull(
                appointment
        );

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

        assertEquals(
                AppointmentStatus.SCHEDULED,
                appointment.getStatus()
        );

        verify(preparedStatement)
                .setInt(
                        1,
                        10
                );
    }

    @Test
    void shouldReturnNullWhenAppointmentDoesNotExist()
            throws Exception {

        when(connection.prepareStatement(
                anyString()))
                .thenReturn(
                        preparedStatement
                );

        when(preparedStatement.executeQuery())
                .thenReturn(
                        resultSet
                );

        when(resultSet.next())
                .thenReturn(false);

        Appointment appointment =
                appointmentDAO
                        .getAppointmentByNumber(
                                "999"
                        );

        assertNull(
                appointment
        );
    }

    @Test
    void shouldReturnAllAppointments()
            throws Exception {

        when(connection.prepareStatement(
                anyString()))
                .thenReturn(
                        preparedStatement
                );

        when(preparedStatement.executeQuery())
                .thenReturn(
                        resultSet
                );

        when(resultSet.next())
                .thenReturn(
                        true,
                        true,
                        false
                );

        when(resultSet.getInt(
                "appointment_no"))
                .thenReturn(
                        1,
                        2
                );

        when(resultSet.getDate(
                "appointment_date"))
                .thenReturn(
                        Date.valueOf(
                                "2026-09-10"
                        ),
                        Date.valueOf(
                                "2026-09-11"
                        )
                );

        when(resultSet.getTime(
                "appointment_time"))
                .thenReturn(
                        Time.valueOf(
                                "09:30:00"
                        ),
                        Time.valueOf(
                                "11:00:00"
                        )
                );

        when(resultSet.getString(
                "status"))
                .thenReturn(
                        AppointmentStatus
                                .SCHEDULED
                                .name(),
                        AppointmentStatus
                                .CANCELLED
                                .name()
                );

        when(resultSet.getInt(
                "patient_id"))
                .thenReturn(
                        1,
                        2
                );

        when(resultSet.getString(
                "patient_name"))
                .thenReturn(
                        "John",
                        "Kamal"
                );

        when(resultSet.getString(
                "address"))
                .thenReturn(
                        "Colombo",
                        "Kandy"
                );

        when(resultSet.getString(
                "contact_number"))
                .thenReturn(
                        "0771234567",
                        "0712345678"
                );

        when(resultSet.getInt(
                "dentist_id"))
                .thenReturn(
                        2,
                        3
                );

        when(resultSet.getString(
                "dentist_name"))
                .thenReturn(
                        "Dr. Silva",
                        "Dr. Perera"
                );

        when(resultSet.getInt(
                "treatment_id"))
                .thenReturn(
                        3,
                        4
                );

        when(resultSet.getString(
                "treatment_type"))
                .thenReturn(
                        "Cleaning",
                        "Dental Filling"
                );

        when(resultSet.getDouble(
                "treatment_price"))
                .thenReturn(
                        5000.00,
                        8500.00
                );

        List<Appointment> appointments =
                appointmentDAO
                        .getAllAppointments();

        assertEquals(
                2,
                appointments.size()
        );

        assertEquals(
                "1",
                appointments
                        .get(0)
                        .getAppointmentNo()
        );

        assertEquals(
                "John",
                appointments
                        .get(0)
                        .getPatient()
                        .getPatientName()
        );

        assertEquals(
                AppointmentStatus.SCHEDULED,
                appointments
                        .get(0)
                        .getStatus()
        );

        assertEquals(
                "2",
                appointments
                        .get(1)
                        .getAppointmentNo()
        );

        assertEquals(
                "Kamal",
                appointments
                        .get(1)
                        .getPatient()
                        .getPatientName()
        );

        assertEquals(
                AppointmentStatus.CANCELLED,
                appointments
                        .get(1)
                        .getStatus()
        );
    }

    @Test
    void shouldReturnTrueWhenAppointmentSlotIsAvailable()
            throws Exception {

        when(connection.prepareStatement(
                anyString()))
                .thenReturn(
                        preparedStatement
                );

        when(preparedStatement.executeQuery())
                .thenReturn(
                        resultSet
                );

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

        assertTrue(
                available
        );
    }

    @Test
    void shouldReturnFalseWhenAppointmentSlotIsTaken()
            throws Exception {

        when(connection.prepareStatement(
                anyString()))
                .thenReturn(
                        preparedStatement
                );

        when(preparedStatement.executeQuery())
                .thenReturn(
                        resultSet
                );

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

        assertFalse(
                available
        );
    }

    @Test
    void shouldCancelAppointment()
            throws Exception {

        when(connection.prepareStatement(
                anyString()))
                .thenReturn(
                        preparedStatement
                );

        when(preparedStatement.executeUpdate())
                .thenReturn(1);

        boolean cancelled =
                appointmentDAO
                        .cancelAppointment(
                                "10"
                        );

        assertTrue(
                cancelled
        );

        verify(preparedStatement)
                .setInt(
                        1,
                        10
                );
    }

    private void stubAppointmentResult(
            int appointmentNo,
            String patientName,
            String dentistName,
            String treatmentType,
            AppointmentStatus status)
            throws Exception {

        when(resultSet.getInt(
                "appointment_no"))
                .thenReturn(
                        appointmentNo
                );

        when(resultSet.getDate(
                "appointment_date"))
                .thenReturn(
                        Date.valueOf(
                                "2026-09-10"
                        )
                );

        when(resultSet.getTime(
                "appointment_time"))
                .thenReturn(
                        Time.valueOf(
                                "09:30:00"
                        )
                );

        when(resultSet.getString(
                "status"))
                .thenReturn(
                        status.name()
                );

        when(resultSet.getInt(
                "patient_id"))
                .thenReturn(1);

        when(resultSet.getString(
                "patient_name"))
                .thenReturn(
                        patientName
                );

        when(resultSet.getString(
                "address"))
                .thenReturn(
                        "Colombo"
                );

        when(resultSet.getString(
                "contact_number"))
                .thenReturn(
                        "0771234567"
                );

        when(resultSet.getInt(
                "dentist_id"))
                .thenReturn(2);

        when(resultSet.getString(
                "dentist_name"))
                .thenReturn(
                        dentistName
                );

        when(resultSet.getInt(
                "treatment_id"))
                .thenReturn(3);

        when(resultSet.getString(
                "treatment_type"))
                .thenReturn(
                        treatmentType
                );

        when(resultSet.getDouble(
                "treatment_price"))
                .thenReturn(
                        5000.00
                );
    }
}