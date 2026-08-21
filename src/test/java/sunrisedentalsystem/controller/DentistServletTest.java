package sunrisedentalsystem.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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

import sunrisedentalsystem.model.Dentist;
import sunrisedentalsystem.model.User;
import sunrisedentalsystem.service.DentistService;

class DentistServletTest {

    private DentistService dentistService;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    private User loggedInUser;

    private DentistServlet dentistServlet;

    @BeforeEach
    void setUp() {

        dentistService =
                mock(DentistService.class);

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

        dentistServlet =
                new DentistServlet(
                        dentistService
                );
    }

    @Test
    void shouldRedirectToLoginWhenUserIsNotLoggedIn()
            throws Exception {

        when(request.getSession(false))
                .thenReturn(null);

        dentistServlet.doGet(
                request,
                response
        );

        verify(response)
                .sendRedirect("login.jsp");
    }

    @Test
    void shouldDisplayAllDentistsForLoggedInUser()
            throws Exception {

        stubLoggedInUser("STAFF");

        List<Dentist> dentists =
                List.of(
                        new Dentist(
                                1,
                                "Dr. Silva",
                                "Orthodontics",
                                "0771234567"
                        ),
                        new Dentist(
                                2,
                                "Dr. Perera",
                                "General Dentistry",
                                "0712345678"
                        )
                );

        when(request.getParameter("dentistId"))
                .thenReturn(null);

        when(dentistService.getAllDentists())
                .thenReturn(dentists);

        when(request.getRequestDispatcher(
                "dentistList.jsp"))
                .thenReturn(dispatcher);

        dentistServlet.doGet(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "dentists",
                        dentists
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldDisplayDentistWhenDentistExists()
            throws Exception {

        stubLoggedInUser("STAFF");

        Dentist dentist =
                new Dentist(
                        2,
                        "Dr. Perera",
                        "General Dentistry",
                        "0712345678"
                );

        when(request.getParameter("dentistId"))
                .thenReturn("2");

        when(dentistService.searchDentist(2))
                .thenReturn(dentist);

        when(request.getRequestDispatcher(
                "dentistDetails.jsp"))
                .thenReturn(dispatcher);

        dentistServlet.doGet(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "dentist",
                        dentist
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldShowErrorWhenDentistDoesNotExist()
            throws Exception {

        stubLoggedInUser("STAFF");

        when(request.getParameter("dentistId"))
                .thenReturn("999");

        when(dentistService.searchDentist(999))
                .thenReturn(null);

        when(request.getRequestDispatcher(
                "dentistList.jsp"))
                .thenReturn(dispatcher);

        dentistServlet.doGet(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "errorMessage",
                        "Dentist not found."
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldRejectInvalidDentistId()
            throws Exception {

        stubLoggedInUser("STAFF");

        when(request.getParameter("dentistId"))
                .thenReturn("ABC");

        when(request.getRequestDispatcher(
                "dentistList.jsp"))
                .thenReturn(dispatcher);

        dentistServlet.doGet(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "errorMessage",
                        "Invalid dentist ID."
                );

        verify(dentistService, never())
                .searchDentist(
                        org.mockito.ArgumentMatchers
                                .anyInt()
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldAddDentistWhenUserIsAdmin()
            throws Exception {

        stubLoggedInUser("ADMIN");

        when(request.getParameter("action"))
                .thenReturn("add");

        when(request.getParameter("dentistName"))
                .thenReturn("Dr. Silva");

        when(request.getParameter("specialization"))
                .thenReturn("Orthodontics");

        when(request.getParameter("contactNumber"))
                .thenReturn("0771234567");

        when(dentistService.addDentist(
                any(Dentist.class)))
                .thenReturn(true);

        when(request.getRequestDispatcher(
                "dentistDetails.jsp"))
                .thenReturn(dispatcher);

        dentistServlet.doPost(
                request,
                response
        );

        ArgumentCaptor<Dentist> captor =
                ArgumentCaptor.forClass(
                        Dentist.class
                );

        verify(dentistService)
                .addDentist(
                        captor.capture()
                );

        Dentist dentist =
                captor.getValue();

        assertEquals(
                "Dr. Silva",
                dentist.getDentistName()
        );

        assertEquals(
                "Orthodontics",
                dentist.getSpecialization()
        );

        assertEquals(
                "0771234567",
                dentist.getContactNumber()
        );

        verify(request)
                .setAttribute(
                        "dentist",
                        dentist
                );

        verify(request)
                .setAttribute(
                        "successMessage",
                        "Dentist added successfully."
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldRejectDentistWhenRequiredFieldsAreEmpty()
            throws Exception {

        stubLoggedInUser("ADMIN");

        when(request.getParameter("action"))
                .thenReturn("add");

        when(request.getParameter("dentistName"))
                .thenReturn("");

        when(request.getParameter("specialization"))
                .thenReturn("");

        when(request.getParameter("contactNumber"))
                .thenReturn("");

        when(request.getRequestDispatcher(
                "addDentist.jsp"))
                .thenReturn(dispatcher);

        dentistServlet.doPost(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "errorMessage",
                        "All dentist fields are required."
                );

        verify(dentistService, never())
                .addDentist(
                        any(Dentist.class)
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldForbidStaffFromModifyingDentists()
            throws Exception {

        stubLoggedInUser("STAFF");

        when(request.getParameter("action"))
                .thenReturn("add");

        dentistServlet.doPost(
                request,
                response
        );

        verify(response)
                .sendError(
                        HttpServletResponse
                                .SC_FORBIDDEN,
                        "Admin access required."
                );
    }

    @Test
    void shouldUpdateDentistWhenUserIsAdmin()
            throws Exception {

        stubLoggedInUser("ADMIN");

        when(request.getParameter("action"))
                .thenReturn("update");

        when(request.getParameter("dentistId"))
                .thenReturn("2");

        when(request.getParameter("dentistName"))
                .thenReturn("Dr. Fernando");

        when(request.getParameter("specialization"))
                .thenReturn("Endodontics");

        when(request.getParameter("contactNumber"))
                .thenReturn("0751234567");

        when(dentistService.updateDentist(
                any(Dentist.class)))
                .thenReturn(true);

        when(request.getRequestDispatcher(
                "dentistDetails.jsp"))
                .thenReturn(dispatcher);

        dentistServlet.doPost(
                request,
                response
        );

        ArgumentCaptor<Dentist> captor =
                ArgumentCaptor.forClass(
                        Dentist.class
                );

        verify(dentistService)
                .updateDentist(
                        captor.capture()
                );

        Dentist dentist =
                captor.getValue();

        assertEquals(
                2,
                dentist.getDentistId()
        );

        assertEquals(
                "Dr. Fernando",
                dentist.getDentistName()
        );

        assertEquals(
                "Endodontics",
                dentist.getSpecialization()
        );

        assertEquals(
                "0751234567",
                dentist.getContactNumber()
        );

        verify(request)
                .setAttribute(
                        "successMessage",
                        "Dentist updated successfully."
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldDeleteDentistWhenUserIsAdmin()
            throws Exception {

        stubLoggedInUser("ADMIN");

        when(request.getParameter("action"))
                .thenReturn("delete");

        when(request.getParameter("dentistId"))
                .thenReturn("2");

        when(dentistService.deleteDentist(2))
                .thenReturn(true);

        dentistServlet.doPost(
                request,
                response
        );

        verify(dentistService)
                .deleteDentist(2);

        verify(response)
                .sendRedirect("dentist");
    }

    @Test
    void shouldThrowServletExceptionWhenDentistServiceFails()
            throws Exception {

        stubLoggedInUser("ADMIN");

        when(request.getParameter("action"))
                .thenReturn("add");

        when(request.getParameter("dentistName"))
                .thenReturn("Dr. Silva");

        when(request.getParameter("specialization"))
                .thenReturn("Orthodontics");

        when(request.getParameter("contactNumber"))
                .thenReturn("0771234567");

        when(dentistService.addDentist(
                any(Dentist.class)))
                .thenThrow(
                        new SQLException(
                                "Database error"
                        )
                );

        assertThrows(
                ServletException.class,
                () -> dentistServlet.doPost(
                        request,
                        response
                )
        );
    }

    private void stubLoggedInUser(
            String role) {

        when(request.getSession(false))
                .thenReturn(session);

        when(session.getAttribute(
                "loggedInUser"))
                .thenReturn(loggedInUser);

        when(session.getAttribute("role"))
                .thenReturn(role);
    }
}