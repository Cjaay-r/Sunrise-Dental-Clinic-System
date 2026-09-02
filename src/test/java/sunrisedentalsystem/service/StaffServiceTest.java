package sunrisedentalsystem.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sunrisedentalsystem.dao.StaffDAO;
import sunrisedentalsystem.model.Staff;

class StaffServiceTest {

    private StaffDAO staffDAO;

    private StaffService staffService;

    @BeforeEach
    void setUp() {

        staffDAO =
                mock(StaffDAO.class);

        staffService =
                new StaffServiceImpl(
                        staffDAO
                );
    }

    @Test
    void shouldAddStaffThroughDAO()
            throws Exception {

        Staff staff =
                mock(Staff.class);

        Staff result =
                staffService.addStaff(
                        staff
                );

        assertSame(
                staff,
                result
        );

        verify(staffDAO)
                .addStaff(
                        staff
                );
    }

    @Test
    void shouldGetStaffById()
            throws Exception {

        Staff expectedStaff =
                mock(Staff.class);

        when(staffDAO
                .getStaffById(
                        2
                ))
                .thenReturn(
                        expectedStaff
                );

        Staff result =
                staffService.getStaffById(
                        2
                );

        assertSame(
                expectedStaff,
                result
        );

        verify(staffDAO)
                .getStaffById(
                        2
                );
    }

    @Test
    void shouldSearchStaffByName()
            throws Exception {

        List<Staff> expectedStaff =
                List.of(
                        mock(Staff.class),
                        mock(Staff.class)
                );

        when(staffDAO
                .searchStaffByName(
                        "Nimal"
                ))
                .thenReturn(
                        expectedStaff
                );

        List<Staff> result =
                staffService.searchStaffByName(
                        "Nimal"
                );

        assertSame(
                expectedStaff,
                result
        );

        verify(staffDAO)
                .searchStaffByName(
                        "Nimal"
                );
    }

    @Test
    void shouldGetAllStaff()
            throws Exception {

        List<Staff> expectedStaff =
                List.of(
                        mock(Staff.class),
                        mock(Staff.class)
                );

        when(staffDAO
                .getAllStaff())
                .thenReturn(
                        expectedStaff
                );

        List<Staff> result =
                staffService.getAllStaff();

        assertSame(
                expectedStaff,
                result
        );

        verify(staffDAO)
                .getAllStaff();
    }

    @Test
    void shouldUpdateStaffThroughDAO()
            throws Exception {

        Staff staff =
                mock(Staff.class);

        when(staffDAO
                .updateStaff(
                        staff
                ))
                .thenReturn(true);

        boolean result =
                staffService.updateStaff(
                        staff
                );

        assertTrue(
                result
        );

        verify(staffDAO)
                .updateStaff(
                        staff
                );
    }

    @Test
    void shouldCheckWhetherUsernameExists()
            throws Exception {

        when(staffDAO
                .usernameExists(
                        "staff"
                ))
                .thenReturn(true);

        boolean result =
                staffService.usernameExists(
                        "staff"
                );

        assertTrue(
                result
        );

        verify(staffDAO)
                .usernameExists(
                        "staff"
                );
    }

    @Test
    void shouldReturnFalseWhenUsernameDoesNotExist()
            throws Exception {

        when(staffDAO
                .usernameExists(
                        "newstaff"
                ))
                .thenReturn(false);

        boolean result =
                staffService.usernameExists(
                        "newstaff"
                );

        assertFalse(
                result
        );
    }
}