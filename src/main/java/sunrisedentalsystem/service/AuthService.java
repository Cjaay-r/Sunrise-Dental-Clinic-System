package sunrisedentalsystem.service;

import java.sql.SQLException;

import sunrisedentalsystem.model.User;

public interface AuthService {

    User authenticate(
            String username,
            String password) throws SQLException;
}