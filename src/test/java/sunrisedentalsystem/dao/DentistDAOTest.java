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

import sunrisedentalsystem.model.Dentist;
import sunrisedentalsystem.util.DatabaseConnection;

class DentistDAOTest {

    private DentistDAO dentistDAO;

    private Connection connection;
    private PreparedStatement preparedStatement;
    private Statement statement;
    private ResultSet resultSet;

    private MockedStatic<DatabaseConnection>
            databaseConnectionMock;

    @BeforeEach
    void setUp() {

        connection = mock(Connection.class);
        preparedStatement =
                mock(PreparedStatement.class);

        statement = mock(Statement.class);
        resultSet = mock(ResultSet.class);

        databaseConnectionMock =
                mockStatic(DatabaseConnection.class);

        databaseConnectionMock
                .when(DatabaseConnection::getConnection)
                .thenReturn(connection);

        dentistDAO = new DentistDAOImpl();
    }

    @AfterEach
    void tearDown() {
        databaseConnectionMock.close();
    }

    @Test
    void shouldAddDentist() throws Exception {

        Dentist dentist = new Dentist(
                0,
                "Dr. Silva",
                "General Dentistry",
                "0771111111"
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
                .thenReturn(10);

        boolean result =
                dentistDAO.addDentist(dentist);

        assertTrue(result);
        assertEquals(10, dentist.getDentistId());

        verify(preparedStatement)
                .setString(1, "Dr. Silva");

        verify(preparedStatement)
                .setString(
                        2,
                        "General Dentistry"
                );

        verify(preparedStatement)
                .setString(3, "0771111111");
    }

    @Test
    void shouldGetDentistById() throws Exception {

        when(connection.prepareStatement(anyString()))
                .thenReturn(preparedStatement);

        when(preparedStatement.executeQuery())
                .thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(true);

        when(resultSet.getInt("dentist_id"))
                .thenReturn(4);

        when(resultSet.getString("dentist_name"))
                .thenReturn("Dr. Fernando");

        when(resultSet.getString("specialization"))
                .thenReturn("Orthodontics");

        when(resultSet.getString("contact_number"))
                .thenReturn("0712345678");

        Dentist dentist =
                dentistDAO.getDentistById(4);

        assertNotNull(dentist);
        assertEquals(4, dentist.getDentistId());

        assertEquals(
                "Dr. Fernando",
                dentist.getDentistName()
        );

        assertEquals(
                "Orthodontics",
                dentist.getSpecialization()
        );

        verify(preparedStatement)
                .setInt(1, 4);
    }

    @Test
    void shouldGetAllDentists() throws Exception {

        when(connection.createStatement())
                .thenReturn(statement);

        when(statement.executeQuery(anyString()))
                .thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(true, true, false);

        when(resultSet.getInt("dentist_id"))
                .thenReturn(1, 2);

        when(resultSet.getString("dentist_name"))
                .thenReturn(
                        "Dr. Silva",
                        "Dr. Perera"
                );

        when(resultSet.getString("specialization"))
                .thenReturn(
                        "General Dentistry",
                        "Orthodontics"
                );

        when(resultSet.getString("contact_number"))
                .thenReturn(
                        "0771111111",
                        "0772222222"
                );

        List<Dentist> dentists =
                dentistDAO.getAllDentists();

        assertNotNull(dentists);
        assertEquals(2, dentists.size());

        assertEquals(
                "Dr. Silva",
                dentists.get(0).getDentistName()
        );
    }

    @Test
    void shouldUpdateDentist() throws Exception {

        Dentist dentist = new Dentist(
                3,
                "Dr. Updated",
                "Cosmetic Dentistry",
                "0751234567"
        );

        when(connection.prepareStatement(anyString()))
                .thenReturn(preparedStatement);

        when(preparedStatement.executeUpdate())
                .thenReturn(1);

        boolean result =
                dentistDAO.updateDentist(dentist);

        assertTrue(result);

        verify(preparedStatement)
                .setString(1, "Dr. Updated");

        verify(preparedStatement)
                .setString(
                        2,
                        "Cosmetic Dentistry"
                );

        verify(preparedStatement)
                .setString(3, "0751234567");

        verify(preparedStatement)
                .setInt(4, 3);
    }

    @Test
    void shouldDeleteDentist() throws Exception {

        when(connection.prepareStatement(anyString()))
                .thenReturn(preparedStatement);

        when(preparedStatement.executeUpdate())
                .thenReturn(1);

        boolean result =
                dentistDAO.deleteDentist(5);

        assertTrue(result);

        verify(preparedStatement)
                .setInt(1, 5);
    }
}