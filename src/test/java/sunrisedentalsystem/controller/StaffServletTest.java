package sunrisedentalsystem.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import sunrisedentalsystem.model.Staff;
import sunrisedentalsystem.model.User;
import sunrisedentalsystem.service.StaffService;

class StaffServletTest {

    private StaffService staffService;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private HttpSession session;

    private RequestDispatcher dispatcher;

    private User loggedInUser;

    private StaffServlet staffServlet;

    @BeforeEach
    void setUp() {

        staffService =
                mock(StaffService.class);

        request =
                mock(HttpServletRequest.class);

        response =
                mock(HttpServletResponse.class);

        session =
                mock(HttpSession.class);

        dispatcher =
                mock(RequestDispatcher.class);

        loggedInUser =
                mock(User.class);

        when(request.getSession(false))
                .thenReturn(
                        session
                );

        when(session.getAttribute(
                "loggedInUser"))
                .thenReturn(
                        loggedInUser
                );

        when(session.getAttribute(
                "role"))
                .thenReturn(
                        "ADMIN"
                );

        when(request.getRequestDispatcher(
                anyString()))
                .thenReturn(
                        dispatcher
                );

        staffServlet =
                new StaffServlet(
                        staffService
                );
    }

    @Test
    void shouldDisplayAllStaffForAdmin()
            throws Exception {

        List<Staff> staffList =
                List.of(
                        createStaff(
                                2,
                                "Kyle John",
                                "staff"
                        ),
                        createStaff(
                                3,
                                "Nimal Fernando",
                                "nimal"
                        )
                );

        when(staffService
                .getAllStaff())
                .thenReturn(
                        staffList
                );

        staffServlet.doGet(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "staffList",
                        staffList
                );

        verify(request)
                .getRequestDispatcher(
                        "staffList.jsp"
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldRedirectToLoginWhenUserIsNotLoggedIn()
            throws Exception {

        when(request.getSession(false))
                .thenReturn(null);

        staffServlet.doGet(
                request,
                response
        );

        verify(response)
                .sendRedirect(
                        "login.jsp"
                );

        verifyNoInteractions(
                staffService
        );
    }

    @Test
    void shouldRejectStaffUser()
            throws Exception {

        when(session.getAttribute(
                "role"))
                .thenReturn(
                        "STAFF"
                );

        staffServlet.doGet(
                request,
                response
        );

        verify(response)
                .sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Admin access required."
                );

        verifyNoInteractions(
                staffService
        );
    }

    @Test
    void shouldOpenAddStaffPage()
            throws Exception {

        when(request.getParameter(
                "action"))
                .thenReturn(
                        "add"
                );

        staffServlet.doGet(
                request,
                response
        );

        verify(request)
                .getRequestDispatcher(
                        "addStaff.jsp"
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldAddStaffWhenDetailsAreValid()
            throws Exception {

        stubValidAddParameters();

        when(staffService
                .usernameExists(
                        "nimal"
                ))
                .thenReturn(false);

        when(staffService
                .addStaff(
                        any(Staff.class)
                ))
                .thenAnswer(
                        invocation -> {

                            Staff staff =
                                    invocation.getArgument(
                                            0
                                    );

                            staff.setUserId(
                                    3
                            );

                            staff.setStaffId(
                                    3
                            );

                            return staff;
                        }
                );

        staffServlet.doPost(
                request,
                response
        );

        ArgumentCaptor<Staff> captor =
                ArgumentCaptor.forClass(
                        Staff.class
                );

        verify(staffService)
                .addStaff(
                        captor.capture()
                );

        Staff staff =
                captor.getValue();

        assertEquals(
                "Nimal Fernando",
                staff.getStaffName()
        );

        assertEquals(
                "0771234567",
                staff.getContactNumber()
        );

        assertEquals(
                "nimal",
                staff.getUsername()
        );

        assertEquals(
                "staff123",
                staff.getPassword()
        );

        verify(request)
                .setAttribute(
                        eq("staff"),
                        any(Staff.class)
                );

        verify(request)
                .setAttribute(
                        "successMessage",
                        "Staff member added successfully."
                );

        verify(request)
                .getRequestDispatcher(
                        "staffDetails.jsp"
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldRejectAddWhenFieldsAreEmpty()
            throws Exception {

        when(request.getParameter(
                "action"))
                .thenReturn(
                        "add"
                );

        when(request.getParameter(
                "staffName"))
                .thenReturn(
                        ""
                );

        staffServlet.doPost(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "errorMessage",
                        "All staff fields are required."
                );

        verify(request)
                .getRequestDispatcher(
                        "addStaff.jsp"
                );

        verify(staffService, never())
                .addStaff(
                        any(Staff.class)
                );
    }

    @Test
    void shouldRejectDuplicateUsername()
            throws Exception {

        stubValidAddParameters();

        when(staffService
                .usernameExists(
                        "nimal"
                ))
                .thenReturn(true);

        staffServlet.doPost(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "errorMessage",
                        "Username already exists."
                );

        verify(request)
                .getRequestDispatcher(
                        "addStaff.jsp"
                );

        verify(staffService, never())
                .addStaff(
                        any(Staff.class)
                );
    }

    @Test
    void shouldDisplayStaffById()
            throws Exception {

        Staff staff =
                createStaff(
                        2,
                        "Kyle John",
                        "staff"
                );

        when(request.getParameter(
                "staffId"))
                .thenReturn(
                        "2"
                );

        when(staffService
                .getStaffById(
                        2
                ))
                .thenReturn(
                        staff
                );

        staffServlet.doGet(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "staff",
                        staff
                );

        verify(request)
                .getRequestDispatcher(
                        "staffDetails.jsp"
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldShowErrorWhenStaffIdIsInvalid()
            throws Exception {

        when(request.getParameter(
                "staffId"))
                .thenReturn(
                        "ABC"
                );

        when(staffService
                .getAllStaff())
                .thenReturn(
                        List.of()
                );

        staffServlet.doGet(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "errorMessage",
                        "Invalid staff ID."
                );

        verify(request)
                .getRequestDispatcher(
                        "staffList.jsp"
                );
    }

    @Test
    void shouldShowErrorWhenStaffDoesNotExist()
            throws Exception {

        when(request.getParameter(
                "staffId"))
                .thenReturn(
                        "999"
                );

        when(staffService
                .getStaffById(
                        999
                ))
                .thenReturn(null);

        when(staffService
                .getAllStaff())
                .thenReturn(
                        List.of()
                );

        staffServlet.doGet(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "errorMessage",
                        "Staff member not found."
                );

        verify(request)
                .getRequestDispatcher(
                        "staffList.jsp"
                );
    }

    @Test
    void shouldSearchStaffByName()
            throws Exception {

        List<Staff> staffList =
                List.of(
                        createStaff(
                                3,
                                "Nimal Fernando",
                                "nimal"
                        )
                );

        when(request.getParameter(
                "staffName"))
                .thenReturn(
                        "Nim"
                );

        when(staffService
                .searchStaffByName(
                        "Nim"
                ))
                .thenReturn(
                        staffList
                );

        staffServlet.doGet(
                request,
                response
        );

        verify(staffService)
                .searchStaffByName(
                        "Nim"
                );

        verify(request)
                .setAttribute(
                        "staffList",
                        staffList
                );

        verify(request)
                .setAttribute(
                        "searchName",
                        "Nim"
                );

        verify(request)
                .getRequestDispatcher(
                        "staffList.jsp"
                );
    }

    @Test
    void shouldOpenEditStaffPage()
            throws Exception {

        Staff staff =
                createStaff(
                        2,
                        "Kyle John",
                        "staff"
                );

        when(request.getParameter(
                "action"))
                .thenReturn(
                        "edit"
                );

        when(request.getParameter(
                "staffId"))
                .thenReturn(
                        "2"
                );

        when(staffService
                .getStaffById(
                        2
                ))
                .thenReturn(
                        staff
                );

        staffServlet.doGet(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "staff",
                        staff
                );

        verify(request)
                .getRequestDispatcher(
                        "editStaff.jsp"
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldUpdateStaffWhenDetailsAreValid()
            throws Exception {

        Staff staff =
                createStaff(
                        2,
                        "Kyle John",
                        "staff"
                );

        when(request.getParameter(
                "action"))
                .thenReturn(
                        "update"
                );

        when(request.getParameter(
                "staffId"))
                .thenReturn(
                        "2"
                );

        when(request.getParameter(
                "staffName"))
                .thenReturn(
                        "Kyle Fernando"
                );

        when(request.getParameter(
                "contactNumber"))
                .thenReturn(
                        "0711111111"
                );

        when(request.getParameter(
                "username"))
                .thenReturn(
                        "kyle"
                );

        when(staffService
                .getStaffById(
                        2
                ))
                .thenReturn(
                        staff,
                        staff
                );

        when(staffService
                .usernameExists(
                        "kyle"
                ))
                .thenReturn(false);

        when(staffService
                .updateStaff(
                        staff
                ))
                .thenReturn(true);

        staffServlet.doPost(
                request,
                response
        );

        assertEquals(
                "Kyle Fernando",
                staff.getStaffName()
        );

        assertEquals(
                "0711111111",
                staff.getContactNumber()
        );

        assertEquals(
                "kyle",
                staff.getUsername()
        );

        verify(staffService)
                .updateStaff(
                        staff
                );

        verify(request)
                .setAttribute(
                        "successMessage",
                        "Staff member updated successfully."
                );

        verify(request)
                .getRequestDispatcher(
                        "staffDetails.jsp"
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldRejectDuplicateUsernameWhenUpdating()
            throws Exception {

        Staff staff =
                createStaff(
                        2,
                        "Kyle John",
                        "staff"
                );

        when(request.getParameter(
                "action"))
                .thenReturn(
                        "update"
                );

        when(request.getParameter(
                "staffId"))
                .thenReturn(
                        "2"
                );

        when(request.getParameter(
                "staffName"))
                .thenReturn(
                        "Kyle John"
                );

        when(request.getParameter(
                "contactNumber"))
                .thenReturn(
                        "0771234567"
                );

        when(request.getParameter(
                "username"))
                .thenReturn(
                        "admin"
                );

        when(staffService
                .getStaffById(
                        2
                ))
                .thenReturn(
                        staff
                );

        when(staffService
                .usernameExists(
                        "admin"
                ))
                .thenReturn(true);

        staffServlet.doPost(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "errorMessage",
                        "Username already exists."
                );

        verify(request)
                .getRequestDispatcher(
                        "editStaff.jsp"
                );

        verify(staffService, never())
                .updateStaff(
                        any(Staff.class)
                );
    }

    @Test
    void shouldThrowServletExceptionWhenStaffServiceFails()
            throws Exception {

        when(staffService
                .getAllStaff())
                .thenThrow(
                        new SQLException(
                                "Database error"
                        )
                );

        ServletException exception =
                assertThrows(
                        ServletException.class,
                        () ->
                                staffServlet.doGet(
                                        request,
                                        response
                                )
                );

        assertEquals(
                "Unable to retrieve staff information.",
                exception.getMessage()
        );
    }

    private void stubValidAddParameters() {

        when(request.getParameter(
                "action"))
                .thenReturn(
                        "add"
                );

        when(request.getParameter(
                "staffName"))
                .thenReturn(
                        "Nimal Fernando"
                );

        when(request.getParameter(
                "contactNumber"))
                .thenReturn(
                        "0771234567"
                );

        when(request.getParameter(
                "username"))
                .thenReturn(
                        "nimal"
                );

        when(request.getParameter(
                "password"))
                .thenReturn(
                        "staff123"
                );
    }

    private Staff createStaff(
            int staffId,
            String staffName,
            String username) {

        return new Staff(
                staffId,
                staffId,
                username,
                "staff123",
                staffName,
                "0771234567"
        );
    }
}