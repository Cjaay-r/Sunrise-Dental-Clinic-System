package sunrisedentalsystem.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sunrisedentalsystem.model.User;
import sunrisedentalsystem.service.AuthService;

class LoginServletTest {

    private AuthService authService;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    private LoginServlet loginServlet;

    @BeforeEach
    void setUp() {

        authService = mock(AuthService.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);

        loginServlet = new LoginServlet(authService);
    }

    @Test
    void shouldRejectEmptyUsernameAndPassword() throws Exception {

        when(request.getParameter("username")).thenReturn("");
        when(request.getParameter("password")).thenReturn("");

        when(request.getRequestDispatcher("login.jsp"))
                .thenReturn(dispatcher);

        loginServlet.doPost(request, response);

        verify(request).setAttribute(
                "errorMessage",
                "Username and password are required."
        );

        verify(dispatcher).forward(request, response);

        verify(authService, never())
                .authenticate(
                        org.mockito.ArgumentMatchers.anyString(),
                        org.mockito.ArgumentMatchers.anyString()
                );
    }

    @Test
    void shouldLoginUserWithValidCredentials() throws Exception {

        User user = mock(User.class);

        when(request.getParameter("username"))
                .thenReturn("admin");

        when(request.getParameter("password"))
                .thenReturn("admin123");

        when(authService.authenticate("admin", "admin123"))
                .thenReturn(user);

        when(user.getRole()).thenReturn("ADMIN");

        when(request.getSession()).thenReturn(session);

        loginServlet.doPost(request, response);

        verify(authService)
                .authenticate("admin", "admin123");

        verify(session)
                .setAttribute("loggedInUser", user);

        verify(session)
                .setAttribute("role", "ADMIN");

        verify(response)
                .sendRedirect("dashboard.jsp");
    }

    @Test
    void shouldRejectInvalidCredentials() throws Exception {

        when(request.getParameter("username"))
                .thenReturn("wronguser");

        when(request.getParameter("password"))
                .thenReturn("wrongpassword");

        when(authService.authenticate(
                "wronguser",
                "wrongpassword"))
                .thenReturn(null);

        when(request.getRequestDispatcher("login.jsp"))
                .thenReturn(dispatcher);

        loginServlet.doPost(request, response);

        verify(request).setAttribute(
                "errorMessage",
                "Invalid username or password."
        );

        verify(dispatcher)
                .forward(request, response);
    }

    @Test
    void shouldThrowServletExceptionWhenAuthenticationFails()
            throws Exception {

        when(request.getParameter("username"))
                .thenReturn("admin");

        when(request.getParameter("password"))
                .thenReturn("admin123");

        when(authService.authenticate("admin", "admin123"))
                .thenThrow(new SQLException("Database error"));

        assertThrows(
                ServletException.class,
                () -> loginServlet.doPost(request, response)
        );
    }
}