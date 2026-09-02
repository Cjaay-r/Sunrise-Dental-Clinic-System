package sunrisedentalsystem.service;

import java.sql.SQLException;
import java.util.List;

import sunrisedentalsystem.dao.StaffDAO;
import sunrisedentalsystem.model.Staff;

public class StaffServiceImpl
        implements StaffService {

    private final StaffDAO staffDAO;

    public StaffServiceImpl(
            StaffDAO staffDAO) {

        this.staffDAO =
                staffDAO;
    }

    @Override
    public Staff addStaff(
            Staff staff)
            throws SQLException {

        staffDAO.addStaff(
                staff
        );

        return staff;
    }

    @Override
    public Staff getStaffById(
            int staffId)
            throws SQLException {

        return staffDAO
                .getStaffById(
                        staffId
                );
    }

    @Override
    public List<Staff> searchStaffByName(
            String staffName)
            throws SQLException {

        return staffDAO
                .searchStaffByName(
                        staffName
                );
    }

    @Override
    public List<Staff> getAllStaff()
            throws SQLException {

        return staffDAO
                .getAllStaff();
    }

    @Override
    public boolean updateStaff(
            Staff staff)
            throws SQLException {

        return staffDAO
                .updateStaff(
                        staff
                );
    }

    @Override
    public boolean usernameExists(
            String username)
            throws SQLException {

        return staffDAO
                .usernameExists(
                        username
                );
    }
}