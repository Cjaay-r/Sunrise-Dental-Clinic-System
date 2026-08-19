package sunrisedentalsystem.service;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sunrisedentalsystem.dao.UserDAO;
import sunrisedentalsystem.model.User;

class AuthServiceTest {

    private UserDAO userDAO;
    private AuthService authService;

    @BeforeEach
    void setUp() {

        userDAO = mock(UserDAO.class);

        authService =
                new AuthServiceImpl(userDAO);
    }

    @Test
    void shouldAuthenticateUserWithCorrectCredentials()
            throws Exception {

        String username = "admin";
        String password = "admin123";

        User user = mock(User.class);

        when(userDAO.getUserByUsername(username))
                .thenReturn(user);

        when(user.getPassword())
                .thenReturn(password);

        User result =
                authService.authenticate(
                        username,
                        password);

        assertSame(user, result);

        verify(userDAO)
                .getUserByUsername(username);
    }

    @Test
    void shouldRejectUserWithIncorrectPassword()
            throws Exception {

        String username = "admin";

        User user = mock(User.class);

        when(userDAO.getUserByUsername(username))
                .thenReturn(user);

        when(user.getPassword())
                .thenReturn("correctPassword");

        User result =
                authService.authenticate(
                        username,
                        "wrongPassword");

        assertNull(result);

        verify(userDAO)
                .getUserByUsername(username);
    }

    @Test
    void shouldRejectUnknownUsername()
            throws Exception {

        String username = "unknown";

        when(userDAO.getUserByUsername(username))
                .thenReturn(null);

        User result =
                authService.authenticate(
                        username,
                        "password123");

        assertNull(result);

        verify(userDAO)
                .getUserByUsername(username);
    }
}