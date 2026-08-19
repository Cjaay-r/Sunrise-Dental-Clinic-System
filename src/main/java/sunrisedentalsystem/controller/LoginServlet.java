package sunrisedentalsystem.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sunrisedentalsystem.dao.UserDAOImpl;
import sunrisedentalsystem.model.User;
import sunrisedentalsystem.service.AuthService;
import sunrisedentalsystem.service.AuthServiceImpl;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private AuthService authService;

    public LoginServlet() {
    }

    LoginServlet(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void init() throws ServletException {

        if (authService == null) {
            authService = new AuthServiceImpl(new UserDAOImpl());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null
                || username.trim().isEmpty()
                || password == null
                || password.trim().isEmpty()) {

            request.setAttribute(
                    "errorMessage",
                    "Username and password are required."
            );

            request.getRequestDispatcher("login.jsp")
                    .forward(request, response);

            return;
        }

        try {

            User user =
                    authService.authenticate(username, password);

            if (user != null) {

                HttpSession session = request.getSession();

                session.setAttribute(
                        "loggedInUser",
                        user
                );

                session.setAttribute(
                        "role",
                        user.getRole()
                );

                response.sendRedirect("dashboard.jsp");

            } else {

                request.setAttribute(
                        "errorMessage",
                        "Invalid username or password."
                );

                request.getRequestDispatcher("login.jsp")
                        .forward(request, response);
            }

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to authenticate user.",
                    e
            );
        }
    }
}