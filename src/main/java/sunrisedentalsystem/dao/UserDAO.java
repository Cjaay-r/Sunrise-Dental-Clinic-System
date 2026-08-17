package sunrisedentalsystem.dao;

import java.sql.SQLException;

import sunrisedentalsystem.model.User;

public interface UserDAO {

    User getUserByUsername(String username) throws SQLException;
}