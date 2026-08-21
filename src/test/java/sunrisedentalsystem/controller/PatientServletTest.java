package sunrisedentalsystem.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import sunrisedentalsystem.model.Patient;
import sunrisedentalsystem.model.User;
import sunrisedentalsystem.service.PatientService;

class PatientServletTest {

    private PatientService patientService;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    private User loggedInUser;

    private PatientServlet patientServlet;

    @BeforeEach
    void setUp() {

        patientService = mock(PatientService.class);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);

        loggedInUser = mock(User.class);

        patientServlet =
                new PatientServlet(patientService);
    }

    @Test
    void shouldRejectPatientWhenRequiredFieldsAreEmpty()
            throws Exception {

        when(request.getParameter("patientName"))
                .thenReturn("");

        when(request.getParameter("address"))
                .thenReturn("");

        when(request.getParameter("contactNumber"))
                .thenReturn("");

        when(request.getRequestDispatcher(
                "registerPatient.jsp"))
                .thenReturn(dispatcher);

        patientServlet.doPost(request, response);

        verify(request).setAttribute(
                "errorMessage",
                "All patient fields are required."
        );

        verify(dispatcher)
                .forward(request, response);

        verifyNoInteractions(patientService);
    }

    @Test
    void shouldRedirectToLoginWhenUserIsNotLoggedIn()
            throws Exception {

        stubValidPatientParameters();

        when(request.getSession(false))
                .thenReturn(null);

        patientServlet.doPost(request, response);

        verify(response)
                .sendRedirect("login.jsp");

        verifyNoInteractions(patientService);
    }

    @Test
    void shouldRegisterPatientWhenDetailsAreValid()
            throws Exception {

        stubValidPatientParameters();
        stubLoggedInUser();

        when(patientService.registerPatient(
                any(Patient.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        when(request.getRequestDispatcher(
                "patientDetails.jsp"))
                .thenReturn(dispatcher);

        patientServlet.doPost(request, response);

        ArgumentCaptor<Patient> captor =
                ArgumentCaptor.forClass(Patient.class);

        verify(patientService)
                .registerPatient(
                        captor.capture()
                );

        Patient patient = captor.getValue();

        assertNotNull(patient);

        assertEquals(
                "Kamal Perera",
                patient.getPatientName()
        );

        assertEquals(
                "Colombo",
                patient.getAddress()
        );

        assertEquals(
                "0771234567",
                patient.getContactNumber()
        );

        verify(request).setAttribute(
                eq("patient"),
                any(Patient.class)
        );

        verify(request).setAttribute(
                "successMessage",
                "Patient registered successfully."
        );

        verify(dispatcher)
                .forward(request, response);
    }

    @Test
    void shouldShowErrorWhenPatientRegistrationFails()
            throws Exception {

        stubValidPatientParameters();
        stubLoggedInUser();

        when(patientService.registerPatient(
                any(Patient.class)))
                .thenReturn(null);

        when(request.getRequestDispatcher(
                "registerPatient.jsp"))
                .thenReturn(dispatcher);

        patientServlet.doPost(request, response);

        verify(request).setAttribute(
                "errorMessage",
                "Unable to register patient."
        );

        verify(dispatcher)
                .forward(request, response);
    }

    @Test
    void shouldDisplayPatientWhenPatientExists()
            throws Exception {

        Patient patient = mock(Patient.class);

        when(request.getParameter("patientId"))
                .thenReturn("5");

        when(patientService.searchPatient(5))
                .thenReturn(patient);

        when(request.getRequestDispatcher(
                "patientDetails.jsp"))
                .thenReturn(dispatcher);

        patientServlet.doGet(request, response);

        verify(patientService)
                .searchPatient(5);

        verify(request).setAttribute(
                "patient",
                patient
        );

        verify(dispatcher)
                .forward(request, response);
    }

    @Test
    void shouldShowErrorWhenPatientDoesNotExist()
            throws Exception {

        when(request.getParameter("patientId"))
                .thenReturn("999");

        when(patientService.searchPatient(999))
                .thenReturn(null);

        when(request.getRequestDispatcher(
                "searchPatient.jsp"))
                .thenReturn(dispatcher);

        patientServlet.doGet(request, response);

        verify(request).setAttribute(
                "errorMessage",
                "Patient not found."
        );

        verify(dispatcher)
                .forward(request, response);
    }

    @Test
    void shouldRejectInvalidPatientId()
            throws Exception {

        when(request.getParameter("patientId"))
                .thenReturn("ABC");

        when(request.getRequestDispatcher(
                "searchPatient.jsp"))
                .thenReturn(dispatcher);

        patientServlet.doGet(request, response);

        verify(request).setAttribute(
                "errorMessage",
                "Invalid patient ID."
        );

        verify(dispatcher)
                .forward(request, response);

        verify(patientService, never())
                .searchPatient(
                        org.mockito.ArgumentMatchers.anyInt()
                );
    }

    @Test
    void shouldThrowServletExceptionWhenPatientServiceFails()
            throws Exception {

        stubValidPatientParameters();
        stubLoggedInUser();

        when(patientService.registerPatient(
                any(Patient.class)))
                .thenThrow(
                        new SQLException(
                                "Database error"
                        )
                );

        assertThrows(
                ServletException.class,
                () -> patientServlet
                        .doPost(request, response)
        );
    }

    private void stubValidPatientParameters() {

        when(request.getParameter("patientName"))
                .thenReturn("Kamal Perera");

        when(request.getParameter("address"))
                .thenReturn("Colombo");

        when(request.getParameter("contactNumber"))
                .thenReturn("0771234567");
    }

    private void stubLoggedInUser() {

        when(request.getSession(false))
                .thenReturn(session);

        when(session.getAttribute("loggedInUser"))
                .thenReturn(loggedInUser);
    }
}