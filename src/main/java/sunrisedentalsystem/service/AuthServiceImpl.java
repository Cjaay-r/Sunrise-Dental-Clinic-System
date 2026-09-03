package sunrisedentalsystem.service;

import java.sql.SQLException;

import sunrisedentalsystem.dao.UserDAO;
import sunrisedentalsystem.model.User;
import sunrisedentalsystem.util.PasswordUtil;

public class AuthServiceImpl
        implements AuthService {

    private final UserDAO userDAO;

    public AuthServiceImpl(
            UserDAO userDAO) {

        this.userDAO =
                userDAO;
    }

    @Override
    public User authenticate(
            String username,
            String password)
            throws SQLException {

        User user =
                userDAO.getUserByUsername(
                        username
                );

        if (user == null) {

            return null;
        }

        boolean passwordMatches =
                PasswordUtil.matchesPassword(
                        password,
                        user.getPassword()
                );

        if (!passwordMatches) {

            return null;
        }

        return user;
    }
}