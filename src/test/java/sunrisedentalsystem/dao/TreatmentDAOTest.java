package sunrisedentalsystem.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import sunrisedentalsystem.model.Treatment;
import sunrisedentalsystem.util.DatabaseConnection;

class TreatmentDAOTest {

    private TreatmentDAO treatmentDAO;

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

        treatmentDAO =
                new TreatmentDAOImpl();
    }

    @AfterEach
    void tearDown() {
        databaseConnectionMock.close();
    }

    @Test
    void shouldAddTreatment() throws Exception {

        Treatment treatment =
                new Treatment(
                        0,
                        "Cleaning",
                        5000.00
                );

        when(connection.prepareStatement(
                anyString(),
                eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(preparedStatement);

        when(preparedStatement.getGeneratedKeys())
                .thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(true);

        when(resultSet.getInt(1))
                .thenReturn(5);

        treatmentDAO.addTreatment(treatment);

        assertEquals(
                5,
                treatment.getTreatmentId()
        );

        verify(preparedStatement)
                .setString(1, "Cleaning");

        verify(preparedStatement)
                .setDouble(2, 5000.00);

        verify(preparedStatement)
                .executeUpdate();
    }

    @Test
    void shouldGetTreatmentById()
            throws Exception {

        when(connection.prepareStatement(anyString()))
                .thenReturn(preparedStatement);

        when(preparedStatement.executeQuery())
                .thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(true);

        when(resultSet.getInt("treatment_id"))
                .thenReturn(2);

        when(resultSet.getString("treatment_type"))
                .thenReturn("Extraction");

        when(resultSet.getDouble("treatment_price"))
                .thenReturn(7500.00);

        Treatment treatment =
                treatmentDAO.getTreatmentById(2);

        assertNotNull(treatment);
        assertEquals(2, treatment.getTreatmentId());

        assertEquals(
                "Extraction",
                treatment.getTreatmentType()
        );

        assertEquals(
                7500.00,
                treatment.getTreatmentPrice()
        );
    }

    @Test
    void shouldGetAllTreatments()
            throws Exception {

        when(connection.prepareStatement(anyString()))
                .thenReturn(preparedStatement);

        when(preparedStatement.executeQuery())
                .thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(true, true, false);

        when(resultSet.getInt("treatment_id"))
                .thenReturn(1, 2);

        when(resultSet.getString("treatment_type"))
                .thenReturn(
                        "Cleaning",
                        "Extraction"
                );

        when(resultSet.getDouble("treatment_price"))
                .thenReturn(
                        5000.00,
                        7500.00
                );

        List<Treatment> treatments =
                treatmentDAO.getAllTreatments();

        assertNotNull(treatments);
        assertEquals(2, treatments.size());
    }

    @Test
    void shouldUpdateTreatment()
            throws Exception {

        Treatment treatment =
                new Treatment(
                        3,
                        "Updated Treatment",
                        9000.00
                );

        when(connection.prepareStatement(anyString()))
                .thenReturn(preparedStatement);

        treatmentDAO.updateTreatment(treatment);

        verify(preparedStatement)
                .setString(
                        1,
                        "Updated Treatment"
                );

        verify(preparedStatement)
                .setDouble(2, 9000.00);

        verify(preparedStatement)
                .setInt(3, 3);

        verify(preparedStatement)
                .executeUpdate();
    }

    @Test
    void shouldDeleteTreatment()
            throws Exception {

        when(connection.prepareStatement(anyString()))
                .thenReturn(preparedStatement);

        treatmentDAO.deleteTreatment(4);

        verify(preparedStatement)
                .setInt(1, 4);

        verify(preparedStatement)
                .executeUpdate();
    }
}