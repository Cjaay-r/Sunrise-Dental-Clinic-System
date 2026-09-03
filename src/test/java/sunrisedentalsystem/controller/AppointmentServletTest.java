package sunrisedentalsystem.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import sunrisedentalsystem.model.Appointment;
import sunrisedentalsystem.model.AppointmentStatus;
import sunrisedentalsystem.model.Dentist;
import sunrisedentalsystem.model.Patient;
import sunrisedentalsystem.model.Treatment;
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

        appointmentService =
                mock(AppointmentService.class);

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

        when(session.getAttribute("loggedInUser"))
                .thenReturn(loggedInUser);

        when(session.getAttribute("role"))
                .thenReturn("STAFF");

        when(loggedInUser.getUserId())
                .thenReturn(2);

        when(request.getRequestDispatcher(anyString()))
                .thenReturn(dispatcher);

        appointmentServlet =
                new AppointmentServlet(
                        appointmentService
                );
    }

    @Test
    void shouldOpenRegisterAppointmentPage()
            throws Exception {

        when(request.getParameter("action"))
                .thenReturn("register");

        appointmentServlet.doGet(
                request,
                response
        );

        verify(request)
                .getRequestDispatcher(
                        "registerAppointment.jsp"
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldDisplayAppointmentOverview()
            throws Exception {

        List<Appointment> appointments =
                List.of(
                        createAppointment(
                                "1",
                                AppointmentStatus.SCHEDULED
                        ),
                        createAppointment(
                                "2",
                                AppointmentStatus.CANCELLED
                        ),
                        createAppointment(
                                "3",
                                AppointmentStatus.SCHEDULED
                        )
                );

        when(request.getParameter("appointmentNo"))
                .thenReturn(null);

        when(request.getParameter("status"))
                .thenReturn(null);

        when(appointmentService.getAllAppointments())
                .thenReturn(appointments);

        appointmentServlet.doGet(
                request,
                response
        );

        verify(request).setAttribute(
                "scheduledCount",
                2
        );

        verify(request).setAttribute(
                "cancelledCount",
                1
        );

        verify(request).setAttribute(
                "selectedStatus",
                "ALL"
        );

        verify(request).setAttribute(
                "appointments",
                appointments
        );

        verify(request)
                .getRequestDispatcher(
                        "searchAppointment.jsp"
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldFilterScheduledAppointments()
            throws Exception {

        Appointment scheduledOne =
                createAppointment(
                        "1",
                        AppointmentStatus.SCHEDULED
                );

        Appointment cancelled =
                createAppointment(
                        "2",
                        AppointmentStatus.CANCELLED
                );

        Appointment scheduledTwo =
                createAppointment(
                        "3",
                        AppointmentStatus.SCHEDULED
                );

        when(request.getParameter("appointmentNo"))
                .thenReturn(null);

        when(request.getParameter("status"))
                .thenReturn("SCHEDULED");

        when(appointmentService.getAllAppointments())
                .thenReturn(
                        List.of(
                                scheduledOne,
                                cancelled,
                                scheduledTwo
                        )
                );

        appointmentServlet.doGet(
                request,
                response
        );

        verify(request).setAttribute(
                "scheduledCount",
                2
        );

        verify(request).setAttribute(
                "cancelledCount",
                1
        );

        verify(request).setAttribute(
                "selectedStatus",
                "SCHEDULED"
        );

        verify(request).setAttribute(
                "appointments",
                List.of(
                        scheduledOne,
                        scheduledTwo
                )
        );
    }

    @Test
    void shouldFilterCancelledAppointments()
            throws Exception {

        Appointment scheduled =
                createAppointment(
                        "1",
                        AppointmentStatus.SCHEDULED
                );

        Appointment cancelled =
                createAppointment(
                        "2",
                        AppointmentStatus.CANCELLED
                );

        when(request.getParameter("appointmentNo"))
                .thenReturn(null);

        when(request.getParameter("status"))
                .thenReturn("CANCELLED");

        when(appointmentService.getAllAppointments())
                .thenReturn(
                        List.of(
                                scheduled,
                                cancelled
                        )
                );

        appointmentServlet.doGet(
                request,
                response
        );

        verify(request).setAttribute(
                "scheduledCount",
                1
        );

        verify(request).setAttribute(
                "cancelledCount",
                1
        );

        verify(request).setAttribute(
                "selectedStatus",
                "CANCELLED"
        );

        verify(request).setAttribute(
                "appointments",
                List.of(
                        cancelled
                )
        );
    }

    @Test
    void shouldRedirectToLoginWhenUserIsNotLoggedIn()
            throws Exception {

        when(request.getSession(false))
                .thenReturn(null);

        appointmentServlet.doGet(
                request,
                response
        );

        verify(response)
                .sendRedirect(
                        "login.jsp"
                );

        verifyNoInteractions(
                appointmentService
        );
    }

    @Test
    void shouldRejectAdminWhenOpeningRegisterPage()
            throws Exception {

        when(session.getAttribute("role"))
                .thenReturn("ADMIN");

        when(request.getParameter("action"))
                .thenReturn("register");

        appointmentServlet.doGet(
                request,
                response
        );

        verify(response).sendError(
                HttpServletResponse.SC_FORBIDDEN,
                "Staff access required."
        );

        verifyNoInteractions(
                appointmentService
        );
    }

    @Test
    void shouldRejectRegistrationWhenRequiredFieldsAreEmpty()
            throws Exception {

        when(request.getParameter("action"))
                .thenReturn("register");

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

        appointmentServlet.doPost(
                request,
                response
        );

        verify(request).setAttribute(
                "errorMessage",
                "All appointment fields are required."
        );

        verify(request)
                .getRequestDispatcher(
                        "registerAppointment.jsp"
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );

        verifyNoInteractions(
                appointmentService
        );
    }

    @Test
    void shouldRejectInvalidAppointmentDetails()
            throws Exception {

        when(request.getParameter("action"))
                .thenReturn("register");

        when(request.getParameter("patientId"))
                .thenReturn("invalid");

        when(request.getParameter("dentistId"))
                .thenReturn("2");

        when(request.getParameter("treatmentId"))
                .thenReturn("3");

        when(request.getParameter("appointmentDate"))
                .thenReturn(
                        LocalDate.now()
                                .plusDays(5)
                                .toString()
                );

        when(request.getParameter("appointmentTime"))
                .thenReturn("10:30");

        appointmentServlet.doPost(
                request,
                response
        );

        verify(request).setAttribute(
                "errorMessage",
                "Invalid appointment details."
        );

        verify(request)
                .getRequestDispatcher(
                        "registerAppointment.jsp"
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldRejectAppointmentWhenDateIsInPast()
            throws Exception {

        LocalDate pastDate =
                LocalDate.now()
                        .minusDays(1);

        stubRegistrationParameters(
                pastDate,
                LocalTime.of(10, 30)
        );

        appointmentServlet.doPost(
                request,
                response
        );

        verify(request).setAttribute(
                "errorMessage",
                "Appointment date cannot be in the past."
        );

        verify(appointmentService, never())
                .checkAvailability(
                        any(Integer.class),
                        any(LocalDate.class),
                        any(LocalTime.class)
                );
    }

    @Test
    void shouldRejectAppointmentWhenSlotIsUnavailable()
            throws Exception {

        LocalDate date =
                LocalDate.now()
                        .plusDays(5);

        LocalTime time =
                LocalTime.of(10, 30);

        stubRegistrationParameters(
                date,
                time
        );

        when(appointmentService
                .checkAvailability(
                        2,
                        date,
                        time
                ))
                .thenReturn(false);

        appointmentServlet.doPost(
                request,
                response
        );

        verify(request).setAttribute(
                "errorMessage",
                "Selected appointment slot is unavailable."
        );

        verify(appointmentService, never())
                .registerAppointment(
                        any(Appointment.class),
                        eq(2)
                );
    }

    @Test
    void shouldRegisterAppointmentWhenDetailsAreValid()
            throws Exception {

        LocalDate date =
                LocalDate.now()
                        .plusDays(5);

        LocalTime time =
                LocalTime.of(10, 30);

        stubRegistrationParameters(
                date,
                time
        );

        when(appointmentService
                .checkAvailability(
                        2,
                        date,
                        time
                ))
                .thenReturn(true);

        when(appointmentService
                .registerAppointment(
                        any(Appointment.class),
                        eq(2)
                ))
                .thenAnswer(invocation -> {

                    Appointment appointment =
                            invocation.getArgument(0);

                    appointment.setAppointmentNo(
                            "1"
                    );

                    return appointment;
                });

        Appointment databaseAppointment =
                createAppointment(
                        "1",
                        AppointmentStatus.SCHEDULED
                );

        when(appointmentService
                .searchAppointment("1"))
                .thenReturn(
                        databaseAppointment
                );

        appointmentServlet.doPost(
                request,
                response
        );

        ArgumentCaptor<Appointment> captor =
                ArgumentCaptor.forClass(
                        Appointment.class
                );

        verify(appointmentService)
                .registerAppointment(
                        captor.capture(),
                        eq(2)
                );

        Appointment appointment =
                captor.getValue();

        assertEquals(
                "1",
                appointment.getAppointmentNo()
        );

        assertEquals(
                date,
                appointment.getAppointmentDate()
        );

        assertEquals(
                time,
                appointment.getAppointmentTime()
        );

        assertEquals(
                AppointmentStatus.SCHEDULED,
                appointment.getStatus()
        );

        assertEquals(
                5,
                appointment
                        .getPatient()
                        .getPatientId()
        );

        assertEquals(
                2,
                appointment
                        .getDentist()
                        .getDentistId()
        );

        assertEquals(
                3,
                appointment
                        .getTreatment()
                        .getTreatmentId()
        );

        verify(request).setAttribute(
                "appointment",
                databaseAppointment
        );

        verify(request).setAttribute(
                "successMessage",
                "Appointment registered successfully."
        );

        verify(request)
                .getRequestDispatcher(
                        "appointmentDetails.jsp"
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldDisplayAppointmentWhenAppointmentExists()
            throws Exception {

        Appointment appointment =
                createAppointment(
                        "1",
                        AppointmentStatus.SCHEDULED
                );

        when(request.getParameter("appointmentNo"))
                .thenReturn("1");

        when(appointmentService
                .searchAppointment("1"))
                .thenReturn(
                        appointment
                );

        appointmentServlet.doGet(
                request,
                response
        );

        verify(appointmentService)
                .searchAppointment(
                        "1"
                );

        verify(request).setAttribute(
                "appointment",
                appointment
        );

        verify(request)
                .getRequestDispatcher(
                        "appointmentDetails.jsp"
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldShowErrorWhenAppointmentDoesNotExist()
            throws Exception {

        when(request.getParameter("appointmentNo"))
                .thenReturn("999");

        when(appointmentService
                .searchAppointment("999"))
                .thenReturn(null);

        when(appointmentService
                .getAllAppointments())
                .thenReturn(List.of());

        appointmentServlet.doGet(
                request,
                response
        );

        verify(request).setAttribute(
                "errorMessage",
                "Appointment not found."
        );

        verify(request)
                .getRequestDispatcher(
                        "searchAppointment.jsp"
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldRejectInvalidAppointmentNumber()
            throws Exception {

        when(request.getParameter("appointmentNo"))
                .thenReturn("ABC");

        when(appointmentService
                .getAllAppointments())
                .thenReturn(List.of());

        appointmentServlet.doGet(
                request,
                response
        );

        verify(request).setAttribute(
                "errorMessage",
                "Invalid appointment number."
        );

        verify(request)
                .getRequestDispatcher(
                        "searchAppointment.jsp"
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldOpenCancelConfirmationPage()
            throws Exception {

        Appointment appointment =
                createAppointment(
                        "1",
                        AppointmentStatus.SCHEDULED
                );

        when(request.getParameter("action"))
                .thenReturn("cancel");

        when(request.getParameter("appointmentNo"))
                .thenReturn("1");

        when(appointmentService
                .searchAppointment("1"))
                .thenReturn(
                        appointment
                );

        appointmentServlet.doGet(
                request,
                response
        );

        verify(request).setAttribute(
                "appointment",
                appointment
        );

        verify(request)
                .getRequestDispatcher(
                        "cancelAppointment.jsp"
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldCancelAppointmentWhenAppointmentIsScheduled()
            throws Exception {

        Appointment scheduledAppointment =
                createAppointment(
                        "1",
                        AppointmentStatus.SCHEDULED
                );

        Appointment cancelledAppointment =
                createAppointment(
                        "1",
                        AppointmentStatus.CANCELLED
                );

        when(request.getParameter("action"))
                .thenReturn("cancel");

        when(request.getParameter("appointmentNo"))
                .thenReturn("1");

        when(appointmentService
                .searchAppointment("1"))
                .thenReturn(
                        scheduledAppointment,
                        cancelledAppointment
                );

        when(appointmentService
                .cancelAppointment("1"))
                .thenReturn(true);

        appointmentServlet.doPost(
                request,
                response
        );

        verify(appointmentService)
                .cancelAppointment(
                        "1"
                );

        verify(appointmentService, times(2))
                .searchAppointment(
                        "1"
                );

        verify(request).setAttribute(
                "appointment",
                cancelledAppointment
        );

        verify(request).setAttribute(
                "successMessage",
                "Appointment cancelled successfully."
        );

        verify(request)
                .getRequestDispatcher(
                        "appointmentDetails.jsp"
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldRejectCancellationWhenAppointmentIsAlreadyCancelled()
            throws Exception {

        Appointment appointment =
                createAppointment(
                        "1",
                        AppointmentStatus.CANCELLED
                );

        when(request.getParameter("action"))
                .thenReturn("cancel");

        when(request.getParameter("appointmentNo"))
                .thenReturn("1");

        when(appointmentService
                .searchAppointment("1"))
                .thenReturn(
                        appointment
                );

        appointmentServlet.doPost(
                request,
                response
        );

        verify(request).setAttribute(
                "appointment",
                appointment
        );

        verify(request).setAttribute(
                "errorMessage",
                "Appointment is already cancelled."
        );

        verify(appointmentService, never())
                .cancelAppointment(
                        "1"
                );
    }

    @Test
    void shouldShowErrorWhenCancellationFails()
            throws Exception {

        Appointment appointment =
                createAppointment(
                        "1",
                        AppointmentStatus.SCHEDULED
                );

        when(request.getParameter("action"))
                .thenReturn("cancel");

        when(request.getParameter("appointmentNo"))
                .thenReturn("1");

        when(appointmentService
                .searchAppointment("1"))
                .thenReturn(
                        appointment
                );

        when(appointmentService
                .cancelAppointment("1"))
                .thenReturn(false);

        appointmentServlet.doPost(
                request,
                response
        );

        verify(request).setAttribute(
                "appointment",
                appointment
        );

        verify(request).setAttribute(
                "errorMessage",
                "Unable to cancel appointment."
        );
    }

    @Test
    void shouldThrowServletExceptionWhenAppointmentServiceFails()
            throws Exception {

        when(request.getParameter("appointmentNo"))
                .thenReturn(null);

        when(appointmentService
                .getAllAppointments())
                .thenThrow(
                        new SQLException(
                                "Database error"
                        )
                );

        ServletException exception =
                assertThrows(
                        ServletException.class,
                        () ->
                                appointmentServlet.doGet(
                                        request,
                                        response
                                )
                );

        assertEquals(
                "Unable to retrieve appointments.",
                exception.getMessage()
        );
    }

    private void stubRegistrationParameters(
            LocalDate date,
            LocalTime time) {

        when(request.getParameter("action"))
                .thenReturn("register");

        when(request.getParameter("patientId"))
                .thenReturn("5");

        when(request.getParameter("dentistId"))
                .thenReturn("2");

        when(request.getParameter("treatmentId"))
                .thenReturn("3");

        when(request.getParameter("appointmentDate"))
                .thenReturn(
                        date.toString()
                );

        when(request.getParameter("appointmentTime"))
                .thenReturn(
                        time.toString()
                );
    }

    private Appointment createAppointment(
            String appointmentNo,
            AppointmentStatus status) {

        Patient patient =
                new Patient(
                        5,
                        "Test Patient",
                        "Colombo",
                        "0771234567"
                );

        Dentist dentist =
                new Dentist(
                        2,
                        "Dr. Test Dentist"
                );

        Treatment treatment =
                new Treatment(
                        3,
                        "Dental Filling",
                        8500.0
                );

        return new Appointment(
                appointmentNo,
                LocalDate.now()
                        .plusDays(5),
                LocalTime.of(
                        10,
                        30
                ),
                status,
                patient,
                dentist,
                treatment
        );
    }
}