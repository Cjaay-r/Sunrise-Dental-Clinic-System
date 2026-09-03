package sunrisedentalsystem.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

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

        patientDAO =
                new PatientDAOImpl();
    }

    @AfterEach
    void tearDown() {

        databaseConnectionMock.close();
    }

    @Test
    void shouldAddPatient()
            throws Exception {

        Patient patient =
                new Patient(
                        0,
                        "Kyle John",
                        "Colombo",
                        "0771234567"
                );

        when(connection.prepareStatement(
                anyString(),
                eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(
                        preparedStatement
                );

        when(preparedStatement
                .executeUpdate())
                .thenReturn(1);

        when(preparedStatement
                .getGeneratedKeys())
                .thenReturn(
                        resultSet
                );

        when(resultSet.next())
                .thenReturn(true);

        when(resultSet.getInt(1))
                .thenReturn(5);

        boolean result =
                patientDAO.addPatient(
                        patient
                );

        assertTrue(
                result
        );

        assertEquals(
                5,
                patient.getPatientId()
        );

        verify(preparedStatement)
                .setString(
                        1,
                        "Kyle John"
                );

        verify(preparedStatement)
                .setString(
                        2,
                        "Colombo"
                );

        verify(preparedStatement)
                .setString(
                        3,
                        "0771234567"
                );
    }

    @Test
    void shouldGetPatientById()
            throws Exception {

        prepareSinglePatientResult();

        Patient patient =
                patientDAO
                        .getPatientById(
                                5
                        );

        assertNotNull(
                patient
        );

        assertEquals(
                5,
                patient.getPatientId()
        );

        assertEquals(
                "Kyle John",
                patient.getPatientName()
        );

        verify(preparedStatement)
                .setInt(
                        1,
                        5
                );
    }

    @Test
    void shouldGetPatientByContactNumber()
            throws Exception {

        prepareSinglePatientResult();

        Patient patient =
                patientDAO
                        .getPatientByContactNumber(
                                "0771234567"
                        );

        assertNotNull(
                patient
        );

        assertEquals(
                "Kyle John",
                patient.getPatientName()
        );

        assertEquals(
                "0771234567",
                patient.getContactNumber()
        );

        verify(preparedStatement)
                .setString(
                        1,
                        "0771234567"
                );
    }

    @Test
    void shouldSearchPatientsByName()
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
                        false
                );

        stubPatientResult();

        List<Patient> patients =
                patientDAO
                        .searchPatientsByName(
                                "Kyle"
                        );

        assertNotNull(
                patients
        );

        assertEquals(
                1,
                patients.size()
        );

        assertEquals(
                "Kyle John",
                patients
                        .get(0)
                        .getPatientName()
        );

        verify(preparedStatement)
                .setString(
                        1,
                        "%Kyle%"
                );
    }

    @Test
    void shouldGetAllPatients()
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
                        false
                );

        stubPatientResult();

        List<Patient> patients =
                patientDAO
                        .getAllPatients();

        assertNotNull(
                patients
        );

        assertEquals(
                1,
                patients.size()
        );

        assertEquals(
                5,
                patients
                        .get(0)
                        .getPatientId()
        );
    }

    private void prepareSinglePatientResult()
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

        stubPatientResult();
    }

    private void stubPatientResult()
            throws Exception {

        when(resultSet.getInt(
                "patient_id"))
                .thenReturn(5);

        when(resultSet.getString(
                "patient_name"))
                .thenReturn(
                        "Kyle John"
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
    }
}