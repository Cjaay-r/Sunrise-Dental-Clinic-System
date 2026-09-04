package sunrisedentalsystem.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

        patientService =
                mock(PatientService.class);

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
                .thenReturn(session);

        when(session.getAttribute(
                "loggedInUser"))
                .thenReturn(loggedInUser);

        when(session.getAttribute(
                "role"))
                .thenReturn("STAFF");

        when(request.getRequestDispatcher(
                anyString()))
                .thenReturn(dispatcher);

        patientServlet =
                new PatientServlet(
                        patientService
                );
    }

    @Test
    void shouldOpenPatientSearchPage()
            throws Exception {

        patientServlet.doGet(
                request,
                response
        );

        verify(request)
                .getRequestDispatcher(
                        "searchPatient.jsp"
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldAllowAdminToOpenPatientSearchPage()
            throws Exception {

        when(session.getAttribute(
                "role"))
                .thenReturn("ADMIN");

        patientServlet.doGet(
                request,
                response
        );

        verify(request)
                .getRequestDispatcher(
                        "searchPatient.jsp"
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

        when(request.getContextPath())
                .thenReturn(
                        "/sunrisedentalsystem"
                );

        patientServlet.doGet(
                request,
                response
        );

        verify(response)
                .sendRedirect(
                        "/sunrisedentalsystem/login.jsp"
                );

        verifyNoInteractions(
                patientService
        );
    }

    @Test
    void shouldRejectAdminPatientRegistration()
            throws Exception {

        when(session.getAttribute(
                "role"))
                .thenReturn("ADMIN");

        patientServlet.doPost(
                request,
                response
        );

        verify(response)
                .sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Staff access required."
                );

        verifyNoInteractions(
                patientService
        );
    }

    @Test
    void shouldRegisterPatientWithEmailWhenDetailsAreValid()
            throws Exception {

        when(request.getParameter(
                "patientName"))
                .thenReturn(
                        "Kyle John"
                );

        when(request.getParameter(
                "address"))
                .thenReturn(
                        "Colombo"
                );

        when(request.getParameter(
                "contactNumber"))
                .thenReturn(
                        "0771234567"
                );

        when(request.getParameter(
                "email"))
                .thenReturn(
                        "kyle@example.com"
                );

        when(patientService
                .registerPatient(
                        any(Patient.class)
                ))
                .thenAnswer(
                        invocation -> {

                            Patient patient =
                                    invocation.getArgument(
                                            0
                                    );

                            patient.setPatientId(
                                    5
                            );

                            return patient;
                        }
                );

        patientServlet.doPost(
                request,
                response
        );

        ArgumentCaptor<Patient> captor =
                ArgumentCaptor.forClass(
                        Patient.class
                );

        verify(patientService)
                .registerPatient(
                        captor.capture()
                );

        Patient patient =
                captor.getValue();

        assertEquals(
                "Kyle John",
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

        assertEquals(
                "kyle@example.com",
                patient.getEmail()
        );

        verify(request)
                .setAttribute(
                        "successMessage",
                        "Patient registered successfully."
                );

        verify(request)
                .getRequestDispatcher(
                        "patientDetails.jsp"
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldRegisterPatientWithoutEmail()
            throws Exception {

        when(request.getParameter(
                "patientName"))
                .thenReturn(
                        "David Silva"
                );

        when(request.getParameter(
                "address"))
                .thenReturn(
                        "Kandy"
                );

        when(request.getParameter(
                "contactNumber"))
                .thenReturn(
                        "0712345678"
                );

        when(request.getParameter(
                "email"))
                .thenReturn("");

        when(patientService
                .registerPatient(
                        any(Patient.class)
                ))
                .thenAnswer(
                        invocation ->
                                invocation.getArgument(
                                        0
                                )
                );

        patientServlet.doPost(
                request,
                response
        );

        ArgumentCaptor<Patient> captor =
                ArgumentCaptor.forClass(
                        Patient.class
                );

        verify(patientService)
                .registerPatient(
                        captor.capture()
                );

        assertNull(
                captor
                        .getValue()
                        .getEmail()
        );
    }

    @Test
    void shouldRejectInvalidEmail()
            throws Exception {

        when(request.getParameter(
                "patientName"))
                .thenReturn(
                        "Kyle John"
                );

        when(request.getParameter(
                "address"))
                .thenReturn(
                        "Colombo"
                );

        when(request.getParameter(
                "contactNumber"))
                .thenReturn(
                        "0771234567"
                );

        when(request.getParameter(
                "email"))
                .thenReturn(
                        "invalid-email"
                );

        patientServlet.doPost(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "errorMessage",
                        "Enter a valid email address."
                );

        verify(request)
                .getRequestDispatcher(
                        "registerPatient.jsp"
                );

        verify(patientService, never())
                .registerPatient(
                        any(Patient.class)
                );
    }

    @Test
    void shouldRejectPatientWhenRequiredFieldsAreEmpty()
            throws Exception {

        when(request.getParameter(
                "patientName"))
                .thenReturn("");

        when(request.getParameter(
                "address"))
                .thenReturn("");

        when(request.getParameter(
                "contactNumber"))
                .thenReturn("");

        patientServlet.doPost(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "errorMessage",
                        "All required patient fields must be completed."
                );

        verify(request)
                .getRequestDispatcher(
                        "registerPatient.jsp"
                );

        verify(patientService, never())
                .registerPatient(
                        any(Patient.class)
                );
    }

    @Test
    void shouldShowErrorWhenPatientRegistrationFails()
            throws Exception {

        when(request.getParameter(
                "patientName"))
                .thenReturn(
                        "Kyle John"
                );

        when(request.getParameter(
                "address"))
                .thenReturn(
                        "Colombo"
                );

        when(request.getParameter(
                "contactNumber"))
                .thenReturn(
                        "0771234567"
                );

        when(request.getParameter(
                "email"))
                .thenReturn(
                        "kyle@example.com"
                );

        when(patientService
                .registerPatient(
                        any(Patient.class)
                ))
                .thenReturn(null);

        patientServlet.doPost(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "errorMessage",
                        "Unable to register patient."
                );

        verify(request)
                .getRequestDispatcher(
                        "registerPatient.jsp"
                );
    }

    @Test
    void shouldSearchPatientsByName()
            throws Exception {

        List<Patient> patients =
                List.of(
                        createPatient(
                                2,
                                "Kyle John",
                                "0771234567"
                        ),
                        createPatient(
                                3,
                                "Kyle Fernando",
                                "0712345678"
                        )
                );

        when(request.getParameter(
                "searchType"))
                .thenReturn(
                        "name"
                );

        when(request.getParameter(
                "searchValue"))
                .thenReturn(
                        "Kyle"
                );

        when(patientService
                .searchPatientsByName(
                        "Kyle"
                ))
                .thenReturn(
                        patients
                );

        patientServlet.doGet(
                request,
                response
        );

        verify(patientService)
                .searchPatientsByName(
                        "Kyle"
                );

        verify(request)
                .setAttribute(
                        "patientResults",
                        patients
                );

        verify(request)
                .getRequestDispatcher(
                        "searchPatient.jsp"
                );
    }

    @Test
    void shouldDisplayPhoneSearchResult()
            throws Exception {

        Patient patient =
                createPatient(
                        2,
                        "Kyle John",
                        "0771234567"
                );

        when(request.getParameter(
                "searchType"))
                .thenReturn(
                        "phone"
                );

        when(request.getParameter(
                "searchValue"))
                .thenReturn(
                        "0771234567"
                );

        when(patientService
                .searchPatientByContactNumber(
                        "0771234567"
                ))
                .thenReturn(
                        patient
                );

        patientServlet.doGet(
                request,
                response
        );

        verify(patientService)
                .searchPatientByContactNumber(
                        "0771234567"
                );

        verify(request)
                .setAttribute(
                        "patientResults",
                        List.of(patient)
                );

        verify(request)
                .getRequestDispatcher(
                        "searchPatient.jsp"
                );
    }

    @Test
    void shouldShowEmptyResultsWhenPhoneNumberDoesNotExist()
            throws Exception {

        when(request.getParameter(
                "searchType"))
                .thenReturn(
                        "phone"
                );

        when(request.getParameter(
                "searchValue"))
                .thenReturn(
                        "0779999999"
                );

        when(patientService
                .searchPatientByContactNumber(
                        "0779999999"
                ))
                .thenReturn(null);

        patientServlet.doGet(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "patientResults",
                        List.of()
                );

        verify(request)
                .getRequestDispatcher(
                        "searchPatient.jsp"
                );
    }

    @Test
    void shouldDisplayPatientUsingInternalPatientReference()
            throws Exception {

        Patient patient =
                createPatient(
                        2,
                        "Kyle John",
                        "0771234567"
                );

        when(request.getParameter(
                "patientId"))
                .thenReturn(
                        "2"
                );

        when(patientService
                .searchPatient(
                        2
                ))
                .thenReturn(
                        patient
                );

        patientServlet.doGet(
                request,
                response
        );

        verify(patientService)
                .searchPatient(
                        2
                );

        verify(request)
                .setAttribute(
                        "patient",
                        patient
                );

        verify(request)
                .getRequestDispatcher(
                        "patientDetails.jsp"
                );
    }

    @Test
    void shouldRejectInvalidPatientReference()
            throws Exception {

        when(request.getParameter(
                "patientId"))
                .thenReturn(
                        "ABC"
                );

        patientServlet.doGet(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "errorMessage",
                        "Invalid patient reference."
                );

        verify(request)
                .getRequestDispatcher(
                        "searchPatient.jsp"
                );
    }

    @Test
    void shouldRejectInvalidPatientSearchType()
            throws Exception {

        when(request.getParameter(
                "searchType"))
                .thenReturn(
                        "invalid"
                );

        when(request.getParameter(
                "searchValue"))
                .thenReturn(
                        "Kyle"
                );

        patientServlet.doGet(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "errorMessage",
                        "Invalid patient search type."
                );

        verify(request)
                .getRequestDispatcher(
                        "searchPatient.jsp"
                );
    }

    @Test
    void shouldThrowServletExceptionWhenPatientServiceFails()
            throws Exception {

        when(request.getParameter(
                "searchType"))
                .thenReturn(
                        "name"
                );

        when(request.getParameter(
                "searchValue"))
                .thenReturn(
                        "Kyle"
                );

        when(patientService
                .searchPatientsByName(
                        "Kyle"
                ))
                .thenThrow(
                        new SQLException(
                                "Database error"
                        )
                );

        ServletException exception =
                assertThrows(
                        ServletException.class,
                        () ->
                                patientServlet.doGet(
                                        request,
                                        response
                                )
                );

        assertEquals(
                "Unable to search patient.",
                exception.getMessage()
        );
    }

    private Patient createPatient(
            int patientId,
            String patientName,
            String contactNumber) {

        return new Patient(
                patientId,
                patientName,
                "Colombo",
                contactNumber,
                "patient@example.com"
        );
    }
}