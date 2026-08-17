package sunrisedentalsystem.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sunrisedentalsystem.model.User;

class UserDAOTest {

    private UserDAO userDAO;

    @BeforeEach
    void setUp() {
        userDAO = new UserDAOImpl();
    }

    @Test
    void shouldGetUserByUsername() throws SQLException {

        User user = userDAO.getUserByUsername("admin");

        assertNotNull(user);
        assertEquals(1, user.getUserId());
        assertEquals("admin", user.getUsername());
    }

    @Test
    void shouldReturnStaffRoleForStaffUser() throws SQLException {

        User user = userDAO.getUserByUsername("admin");

        assertNotNull(user);
        assertEquals("STAFF", user.getRole());
    }

    @Test
    void shouldReturnNullForUnknownUsername() throws SQLException {

        User user = userDAO.getUserByUsername(
                "user_that_does_not_exist"
        );

        assertNull(user);
    }
}