package sunrisedentalsystem.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import sunrisedentalsystem.model.Appointment;
import sunrisedentalsystem.model.AppointmentStatus;
import sunrisedentalsystem.model.User;
import sunrisedentalsystem.service.AppointmentService;

class AppointmentServletTest {

    private AppointmentService appointmentService;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    private User loggedInUser;

    private AppointmentServlet appointmentServlet;

    @BeforeEach
    void setUp() {

        appointmentService = mock(AppointmentService.class);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);

        loggedInUser = mock(User.class);

        appointmentServlet =
                new AppointmentServlet(appointmentService);
    }

    @Test
    void shouldRejectRegistrationWhenRequiredFieldsAreEmpty()
            throws Exception {

        when(request.getParameter("appointmentNo"))
                .thenReturn("");

        when(request.getParameter("patientId"))
                .thenReturn("");

        when(request.getParameter("dentistId"))
                .thenReturn("");

        when(request.getParameter("treatmentId"))
                .thenReturn("");

        when(request.getParameter("appointmentDate"))
                .thenReturn("");

        when(request.getParameter("appointmentTime"))
                .thenReturn("");

        when(request.getRequestDispatcher(
                "registerAppointment.jsp"))
                .thenReturn(dispatcher);

        appointmentServlet.doPost(request, response);

        verify(request).setAttribute(
                "errorMessage",
                "All appointment fields are required."
        );

        verify(dispatcher).forward(request, response);

        verifyNoInteractions(appointmentService);
    }

    @Test
    void shouldRedirectToLoginWhenUserIsNotLoggedIn()
            throws Exception {

        stubValidRegistrationParameters();

        when(request.getSession(false))
                .thenReturn(null);

        appointmentServlet.doPost(request, response);

        verify(response).sendRedirect("login.jsp");

        verifyNoInteractions(appointmentService);
    }

    @Test
    void shouldRejectAppointmentWhenSlotIsUnavailable()
            throws Exception {

        stubValidRegistrationParameters();
        stubLoggedInUser();

        LocalDate date =
                LocalDate.of(2026, 8, 20);

        LocalTime time =
                LocalTime.of(10, 30);

        when(appointmentService.checkAvailability(
                2,
                date,
                time))
                .thenReturn(false);

        when(request.getRequestDispatcher(
                "registerAppointment.jsp"))
                .thenReturn(dispatcher);

        appointmentServlet.doPost(request, response);

        verify(request).setAttribute(
                "errorMessage",
                "Selected appointment slot is unavailable."
        );

        verify(dispatcher).forward(request, response);

        verify(appointmentService, never())
                .registerAppointment(
                        any(Appointment.class),
                        eq(1)
                );
    }

    @Test
    void shouldRegisterAppointmentWhenDetailsAreValid()
            throws Exception {

        stubValidRegistrationParameters();
        stubLoggedInUser();

        LocalDate date =
                LocalDate.of(2026, 8, 20);

        LocalTime time =
                LocalTime.of(10, 30);

        when(appointmentService.checkAvailability(
                2,
                date,
                time))
                .thenReturn(true);

        when(appointmentService.registerAppointment(
                any(Appointment.class),
                eq(1)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        when(request.getRequestDispatcher(
                "appointmentDetails.jsp"))
                .thenReturn(dispatcher);

        appointmentServlet.doPost(request, response);

        ArgumentCaptor<Appointment> captor =
                ArgumentCaptor.forClass(Appointment.class);

        verify(appointmentService)
                .registerAppointment(
                        captor.capture(),
                        eq(1)
                );

        Appointment appointment =
                captor.getValue();

        assertNotNull(appointment);

        assertEquals(
                "APT001",
                appointment.getAppointmentNo()
        );

        assertEquals(
                LocalDate.of(2026, 8, 20),
                appointment.getAppointmentDate()
        );

        assertEquals(
                LocalTime.of(10, 30),
                appointment.getAppointmentTime()
        );

        assertEquals(
                AppointmentStatus.SCHEDULED,
                appointment.getStatus()
        );

        assertEquals(
                5,
                appointment.getPatient().getPatientId()
        );

        assertEquals(
                2,
                appointment.getDentist().getDentistId()
        );

        assertEquals(
                3,
                appointment.getTreatment().getTreatmentId()
        );

        verify(request).setAttribute(
                eq("appointment"),
                any(Appointment.class)
        );

        verify(request).setAttribute(
                "successMessage",
                "Appointment registered successfully."
        );

        verify(dispatcher).forward(request, response);
    }

    @Test
    void shouldDisplayAppointmentWhenAppointmentExists()
            throws Exception {

        Appointment appointment =
                mock(Appointment.class);

        when(request.getParameter("appointmentNo"))
                .thenReturn("APT001");

        when(appointmentService.searchAppointment("APT001"))
                .thenReturn(appointment);

        when(request.getRequestDispatcher(
                "appointmentDetails.jsp"))
                .thenReturn(dispatcher);

        appointmentServlet.doGet(request, response);

        verify(appointmentService)
                .searchAppointment("APT001");

        verify(request).setAttribute(
                "appointment",
                appointment
        );

        verify(dispatcher).forward(request, response);
    }

    @Test
    void shouldShowErrorWhenAppointmentDoesNotExist()
            throws Exception {

        when(request.getParameter("appointmentNo"))
                .thenReturn("APT999");

        when(appointmentService.searchAppointment("APT999"))
                .thenReturn(null);

        when(request.getRequestDispatcher(
                "searchAppointment.jsp"))
                .thenReturn(dispatcher);

        appointmentServlet.doGet(request, response);

        verify(request).setAttribute(
                "errorMessage",
                "Appointment not found."
        );

        verify(dispatcher).forward(request, response);
    }

    private void stubValidRegistrationParameters() {

        when(request.getParameter("appointmentNo"))
                .thenReturn("APT001");

        when(request.getParameter("patientId"))
                .thenReturn("5");

        when(request.getParameter("dentistId"))
                .thenReturn("2");

        when(request.getParameter("treatmentId"))
                .thenReturn("3");

        when(request.getParameter("appointmentDate"))
                .thenReturn("2026-08-20");

        when(request.getParameter("appointmentTime"))
                .thenReturn("10:30");
    }

    private void stubLoggedInUser() {

        when(request.getSession(false))
                .thenReturn(session);

        when(session.getAttribute("loggedInUser"))
                .thenReturn(loggedInUser);

        when(loggedInUser.getUserId())
                .thenReturn(1);
    }
}