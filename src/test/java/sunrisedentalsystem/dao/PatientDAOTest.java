package sunrisedentalsystem.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import sunrisedentalsystem.model.Patient;
import sunrisedentalsystem.util.DatabaseConnection;

class PatientDAOTest {

    private PatientDAO patientDAO;

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    private MockedStatic<DatabaseConnection>
            databaseConnectionMock;

    @BeforeEach
    void setUp() {

        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        databaseConnectionMock =
                mockStatic(DatabaseConnection.class);

        databaseConnectionMock
                .when(DatabaseConnection::getConnection)
                .thenReturn(connection);

        patientDAO = new PatientDAOImpl();
    }

    @AfterEach
    void tearDown() {
        databaseConnectionMock.close();
    }

    @Test
    void shouldAddPatient() throws Exception {

        Patient patient = new Patient(
                0,
                "Test Patient",
                "Colombo",
                "0771234567"
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
                .thenReturn(1);

        boolean result =
                patientDAO.addPatient(patient);

        assertTrue(result);
        assertEquals(1, patient.getPatientId());

        verify(preparedStatement)
                .setString(1, "Test Patient");

        verify(preparedStatement)
                .setString(2, "Colombo");

        verify(preparedStatement)
                .setString(3, "0771234567");

        verify(preparedStatement)
                .executeUpdate();
    }

    @Test
    void shouldGetPatientById() throws Exception {

        when(connection.prepareStatement(anyString()))
                .thenReturn(preparedStatement);

        when(preparedStatement.executeQuery())
                .thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(true);

        when(resultSet.getInt("patient_id"))
                .thenReturn(5);

        when(resultSet.getString("patient_name"))
                .thenReturn("John");

        when(resultSet.getString("address"))
                .thenReturn("Kandy");

        when(resultSet.getString("contact_number"))
                .thenReturn("0712345678");

        Patient patient =
                patientDAO.getPatientById(5);

        assertNotNull(patient);
        assertEquals(5, patient.getPatientId());
        assertEquals("John", patient.getPatientName());
        assertEquals("Kandy", patient.getAddress());
        assertEquals(
                "0712345678",
                patient.getContactNumber()
        );

        verify(preparedStatement)
                .setInt(1, 5);
    }

    @Test
    void shouldGetPatientByContactNumber()
            throws Exception {

        when(connection.prepareStatement(anyString()))
                .thenReturn(preparedStatement);

        when(preparedStatement.executeQuery())
                .thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(true);

        when(resultSet.getInt("patient_id"))
                .thenReturn(8);

        when(resultSet.getString("patient_name"))
                .thenReturn("David");

        when(resultSet.getString("address"))
                .thenReturn("Colombo");

        when(resultSet.getString("contact_number"))
                .thenReturn("0777654321");

        Patient patient =
                patientDAO.getPatientByContactNumber(
                        "0777654321"
                );

        assertNotNull(patient);
        assertEquals(8, patient.getPatientId());
        assertEquals("David", patient.getPatientName());

        verify(preparedStatement)
                .setString(1, "0777654321");
    }

    @Test
    void shouldReturnNullWhenPatientDoesNotExist()
            throws Exception {

        when(connection.prepareStatement(anyString()))
                .thenReturn(preparedStatement);

        when(preparedStatement.executeQuery())
                .thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(false);

        Patient patient =
                patientDAO.getPatientById(999);

        assertNull(patient);
    }
}