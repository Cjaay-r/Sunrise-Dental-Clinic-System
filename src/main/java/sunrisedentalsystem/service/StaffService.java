package sunrisedentalsystem.service;

import java.sql.SQLException;
import java.util.List;

import sunrisedentalsystem.model.Staff;

public interface StaffService {

    Staff addStaff(
            Staff staff)
            throws SQLException;

    Staff getStaffById(
            int staffId)
            throws SQLException;

    List<Staff> searchStaffByName(
            String staffName)
            throws SQLException;

    List<Staff> getAllStaff()
            throws SQLException;

    boolean updateStaff(
            Staff staff)
            throws SQLException;

    boolean usernameExists(
            String username)
            throws SQLException;
}