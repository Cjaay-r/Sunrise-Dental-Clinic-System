package sunrisedentalsystem.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

import sunrisedentalsystem.model.Staff;
import sunrisedentalsystem.util.DatabaseConnection;

class StaffDAOTest {

    private StaffDAO staffDAO;

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

        staffDAO =
                new StaffDAOImpl();
    }

    @AfterEach
    void tearDown() {

        databaseConnectionMock.close();
    }

    @Test
    void shouldAddStaffAndCreateUserAccount()
            throws Exception {

        PreparedStatement userStatement =
                mock(PreparedStatement.class);

        PreparedStatement staffStatement =
                mock(PreparedStatement.class);

        ResultSet userKeys =
                mock(ResultSet.class);

        ResultSet staffKeys =
                mock(ResultSet.class);

        Staff staff =
                new Staff(
                        0,
                        0,
                        "nimal",
                        "staff123",
                        "Nimal Fernando",
                        "0771234567"
                );

        when(connection.prepareStatement(
                anyString(),
                eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(
                        userStatement,
                        staffStatement
                );

        when(userStatement.executeUpdate())
                .thenReturn(1);

        when(userStatement.getGeneratedKeys())
                .thenReturn(
                        userKeys
                );

        when(userKeys.next())
                .thenReturn(true);

        when(userKeys.getInt(1))
                .thenReturn(3);

        when(staffStatement.executeUpdate())
                .thenReturn(1);

        when(staffStatement.getGeneratedKeys())
                .thenReturn(
                        staffKeys
                );

        when(staffKeys.next())
                .thenReturn(true);

        when(staffKeys.getInt(1))
                .thenReturn(4);

        boolean result =
                staffDAO.addStaff(
                        staff
                );

        assertTrue(
                result
        );

        assertEquals(
                3,
                staff.getUserId()
        );

        assertEquals(
                4,
                staff.getStaffId()
        );

        verify(connection)
                .setAutoCommit(
                        false
                );

        verify(userStatement)
                .setString(
                        1,
                        "nimal"
                );

        verify(userStatement)
                .setString(
                        2,
                        "staff123"
                );

        verify(staffStatement)
                .setInt(
                        1,
                        3
                );

        verify(staffStatement)
                .setString(
                        2,
                        "Nimal Fernando"
                );

        verify(staffStatement)
                .setString(
                        3,
                        "0771234567"
                );

        verify(connection)
                .commit();

        verify(connection)
                .setAutoCommit(
                        true
                );

        verify(connection)
                .close();
    }

    @Test
    void shouldGetStaffById()
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

        stubStaffResult();

        Staff staff =
                staffDAO.getStaffById(
                        2
                );

        assertNotNull(
                staff
        );

        assertEquals(
                2,
                staff.getStaffId()
        );

        assertEquals(
                2,
                staff.getUserId()
        );

        assertEquals(
                "staff",
                staff.getUsername()
        );

        assertEquals(
                "Kyle John",
                staff.getStaffName()
        );

        assertEquals(
                "0771234567",
                staff.getContactNumber()
        );

        verify(preparedStatement)
                .setInt(
                        1,
                        2
                );
    }

    @Test
    void shouldSearchStaffByName()
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

        stubStaffResult();

        List<Staff> staffList =
                staffDAO.searchStaffByName(
                        "Kyle"
                );

        assertNotNull(
                staffList
        );

        assertEquals(
                1,
                staffList.size()
        );

        assertEquals(
                "Kyle John",
                staffList
                        .get(0)
                        .getStaffName()
        );

        verify(preparedStatement)
                .setString(
                        1,
                        "%Kyle%"
                );
    }

    @Test
    void shouldGetAllStaff()
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

        stubStaffResult();

        List<Staff> staffList =
                staffDAO.getAllStaff();

        assertNotNull(
                staffList
        );

        assertEquals(
                1,
                staffList.size()
        );

        assertEquals(
                2,
                staffList
                        .get(0)
                        .getStaffId()
        );

        assertEquals(
                "staff",
                staffList
                        .get(0)
                        .getUsername()
        );
    }

    @Test
    void shouldUpdateStaff()
            throws Exception {

        PreparedStatement userStatement =
                mock(PreparedStatement.class);

        PreparedStatement staffStatement =
                mock(PreparedStatement.class);

        Staff staff =
                new Staff(
                        2,
                        2,
                        "kyle",
                        "staff1",
                        "Kyle Fernando",
                        "0711111111"
                );

        when(connection.prepareStatement(
                anyString()))
                .thenReturn(
                        userStatement,
                        staffStatement
                );

        when(staffStatement.executeUpdate())
                .thenReturn(1);

        boolean result =
                staffDAO.updateStaff(
                        staff
                );

        assertTrue(
                result
        );

        verify(connection)
                .setAutoCommit(
                        false
                );

        verify(userStatement)
                .setString(
                        1,
                        "kyle"
                );

        verify(userStatement)
                .setInt(
                        2,
                        2
                );

        verify(staffStatement)
                .setString(
                        1,
                        "Kyle Fernando"
                );

        verify(staffStatement)
                .setString(
                        2,
                        "0711111111"
                );

        verify(staffStatement)
                .setInt(
                        3,
                        2
                );

        verify(connection)
                .commit();

        verify(connection)
                .setAutoCommit(
                        true
                );

        verify(connection)
                .close();
    }

    @Test
    void shouldReturnTrueWhenUsernameExists()
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

        boolean exists =
                staffDAO.usernameExists(
                        "staff"
                );

        assertTrue(
                exists
        );

        verify(preparedStatement)
                .setString(
                        1,
                        "staff"
                );
    }

    @Test
    void shouldReturnFalseWhenUsernameDoesNotExist()
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

        boolean exists =
                staffDAO.usernameExists(
                        "newstaff"
                );

        assertFalse(
                exists
        );
    }

    private void stubStaffResult()
            throws Exception {

        when(resultSet.getInt(
                "staff_id"))
                .thenReturn(2);

        when(resultSet.getInt(
                "user_id"))
                .thenReturn(2);

        when(resultSet.getString(
                "username"))
                .thenReturn(
                        "staff"
                );

        when(resultSet.getString(
                "password_hash"))
                .thenReturn(
                        "staff1"
                );

        when(resultSet.getString(
                "staff_name"))
                .thenReturn(
                        "Kyle John"
                );

        when(resultSet.getString(
                "contact_number"))
                .thenReturn(
                        "0771234567"
                );
    }
}