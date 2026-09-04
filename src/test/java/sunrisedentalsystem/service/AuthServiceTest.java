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
import sunrisedentalsystem.util.PasswordUtil;

class AuthServiceTest {

    private UserDAO userDAO;
    private AuthService authService;

    @BeforeEach
    void setUp() {

        userDAO =
                mock(UserDAO.class);

        authService =
                new AuthServiceImpl(
                        userDAO
                );
    }

    @Test
    void shouldAuthenticateUserWithCorrectCredentials()
            throws Exception {

        String username =
                "admin";

        String password =
                "admin123";

        String passwordHash =
                PasswordUtil.hashPassword(
                        password
                );

        User user =
                mock(User.class);

        when(userDAO
                .getUserByUsername(
                        username
                ))
                .thenReturn(
                        user
                );

        when(user.getPassword())
                .thenReturn(
                        passwordHash
                );

        User result =
                authService.authenticate(
                        username,
                        password
                );

        assertSame(
                user,
                result
        );

        verify(userDAO)
                .getUserByUsername(
                        username
                );
    }

    @Test
    void shouldRejectUserWithIncorrectPassword()
            throws Exception {

        String username =
                "admin";

        String passwordHash =
                PasswordUtil.hashPassword(
                        "correctPassword"
                );

        User user =
                mock(User.class);

        when(userDAO
                .getUserByUsername(
                        username
                ))
                .thenReturn(
                        user
                );

        when(user.getPassword())
                .thenReturn(
                        passwordHash
                );

        User result =
                authService.authenticate(
                        username,
                        "wrongPassword"
                );

        assertNull(
                result
        );

        verify(userDAO)
                .getUserByUsername(
                        username
                );
    }

    @Test
    void shouldRejectUnknownUsername()
            throws Exception {

        String username =
                "unknown";

        when(userDAO
                .getUserByUsername(
                        username
                ))
                .thenReturn(null);

        User result =
                authService.authenticate(
                        username,
                        "password123"
                );

        assertNull(
                result
        );

        verify(userDAO)
                .getUserByUsername(
                        username
                );
    }

    @Test
    void shouldRejectInvalidStoredPasswordHash()
            throws Exception {

        User user =
                mock(User.class);

        when(userDAO
                .getUserByUsername(
                        "admin"
                ))
                .thenReturn(
                        user
                );

        when(user.getPassword())
                .thenReturn(
                        "not-a-valid-bcrypt-hash"
                );

        User result =
                authService.authenticate(
                        "admin",
                        "admin123"
                );

        assertNull(
                result
        );
    }
}