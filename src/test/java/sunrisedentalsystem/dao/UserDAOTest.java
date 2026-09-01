package sunrisedentalsystem.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import sunrisedentalsystem.model.User;
import sunrisedentalsystem.util.DatabaseConnection;

class UserDAOTest {

    private UserDAO userDAO;

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

        userDAO = new UserDAOImpl();
    }

    @AfterEach
    void tearDown() {
        databaseConnectionMock.close();
    }

    @Test
    void shouldReturnAdminUser()
            throws Exception {

        prepareQuery();

        when(resultSet.next())
                .thenReturn(true);

        when(resultSet.getInt("user_id"))
                .thenReturn(1);

        when(resultSet.getString("username"))
                .thenReturn("admin");

        when(resultSet.getString("password_hash"))
                .thenReturn("admin1");

        when(resultSet.getString("admin_name"))
                .thenReturn("System Administrator");

        when(resultSet.getString("staff_name"))
                .thenReturn(null);

        when(resultSet.getString("contact_number"))
                .thenReturn(null);

        User user =
                userDAO.getUserByUsername("admin");

        assertNotNull(user);

        assertEquals(
                1,
                user.getUserId()
        );

        assertEquals(
                "admin",
                user.getUsername()
        );

        assertEquals(
                "ADMIN",
                user.getRole()
        );

        verify(preparedStatement)
                .setString(1, "admin");
    }

    @Test
    void shouldReturnStaffUser()
            throws Exception {

        prepareQuery();

        when(resultSet.next())
                .thenReturn(true);

        when(resultSet.getInt("user_id"))
                .thenReturn(2);

        when(resultSet.getString("username"))
                .thenReturn("staff");

        when(resultSet.getString("password_hash"))
                .thenReturn("staff1");

        when(resultSet.getString("admin_name"))
                .thenReturn(null);

        when(resultSet.getString("staff_name"))
                .thenReturn("Reception Staff");

        when(resultSet.getString("contact_number"))
                .thenReturn("0771234567");

        User user =
                userDAO.getUserByUsername("staff");

        assertNotNull(user);

        assertEquals(
                "STAFF",
                user.getRole()
        );

        assertEquals(
                "staff",
                user.getUsername()
        );
    }

    @Test
    void shouldReturnNullForUnknownUsername()
            throws Exception {

        prepareQuery();

        when(resultSet.next())
                .thenReturn(false);

        User user =
                userDAO.getUserByUsername(
                        "unknown"
                );

        assertNull(user);
    }

    private void prepareQuery()
            throws Exception {

        when(connection.prepareStatement(anyString()))
                .thenReturn(preparedStatement);

        when(preparedStatement.executeQuery())
                .thenReturn(resultSet);
    }
}