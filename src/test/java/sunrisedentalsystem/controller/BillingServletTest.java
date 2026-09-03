package sunrisedentalsystem.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
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

import sunrisedentalsystem.model.Appointment;
import sunrisedentalsystem.model.AppointmentStatus;
import sunrisedentalsystem.model.Bill;
import sunrisedentalsystem.model.Dentist;
import sunrisedentalsystem.model.Patient;
import sunrisedentalsystem.model.Treatment;
import sunrisedentalsystem.model.User;
import sunrisedentalsystem.service.BillingService;

class BillingServletTest {

    private BillingService billingService;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private HttpSession session;

    private RequestDispatcher dispatcher;

    private User loggedInUser;

    private BillingServlet billingServlet;

    @BeforeEach
    void setUp() {

        billingService =
                mock(BillingService.class);

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
                        "STAFF"
                );

        when(loggedInUser.getUserId())
                .thenReturn(
                        2
                );

        when(request.getRequestDispatcher(
                anyString()))
                .thenReturn(
                        dispatcher
                );

        billingServlet =
                new BillingServlet(
                        billingService
                );
    }

    @Test
    void shouldDisplayBillingManagement()
            throws Exception {

        List<Bill> bills =
                List.of(
                        createBill(
                                1,
                                10
                        ),
                        createBill(
                                2,
                                11
                        )
                );

        when(billingService
                .getAllBills())
                .thenReturn(
                        bills
                );

        billingServlet.doGet(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "bills",
                        bills
                );

        verify(request)
                .getRequestDispatcher(
                        "searchBill.jsp"
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

        billingServlet.doGet(
                request,
                response
        );

        verify(response)
                .sendRedirect(
                        "login.jsp"
                );

        verifyNoInteractions(
                billingService
        );
    }

    @Test
    void shouldOpenGenerateBillPageForStaff()
            throws Exception {

        when(request.getParameter(
                "action"))
                .thenReturn(
                        "generate"
                );

        billingServlet.doGet(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "consultationFee",
                        BillingService.CONSULTATION_FEE
                );

        verify(request)
                .getRequestDispatcher(
                        "generateBill.jsp"
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldRejectAdminWhenOpeningGenerateBillPage()
            throws Exception {

        when(session.getAttribute(
                "role"))
                .thenReturn(
                        "ADMIN"
                );

        when(request.getParameter(
                "action"))
                .thenReturn(
                        "generate"
                );

        billingServlet.doGet(
                request,
                response
        );

        verify(response)
                .sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Staff access required."
                );

        verifyNoInteractions(
                billingService
        );
    }

    @Test
    void shouldPreviewAppointmentBeforeGeneratingBill()
            throws Exception {

        Appointment appointment =
                createAppointment(
                        AppointmentStatus.SCHEDULED
                );

        when(request.getParameter(
                "action"))
                .thenReturn(
                        "generate"
                );

        when(request.getParameter(
                "appointmentNo"))
                .thenReturn(
                        "10"
                );

        when(billingService
                .getBillByAppointmentNo(
                        10
                ))
                .thenReturn(null);

        when(billingService
                .getAppointmentForBilling(
                        10
                ))
                .thenReturn(
                        appointment
                );

        billingServlet.doGet(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "consultationFee",
                        2500.00
                );

        verify(request)
                .setAttribute(
                        "appointment",
                        appointment
                );

        verify(request)
                .setAttribute(
                        "treatmentCost",
                        5000.00
                );

        verify(request)
                .setAttribute(
                        "totalAmount",
                        7500.00
                );

        verify(request)
                .getRequestDispatcher(
                        "generateBill.jsp"
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldRejectPreviewWhenBillAlreadyExists()
            throws Exception {

        Bill existingBill =
                createBill(
                        5,
                        10
                );

        when(request.getParameter(
                "action"))
                .thenReturn(
                        "generate"
                );

        when(request.getParameter(
                "appointmentNo"))
                .thenReturn(
                        "10"
                );

        when(billingService
                .getBillByAppointmentNo(
                        10
                ))
                .thenReturn(
                        existingBill
                );

        billingServlet.doGet(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "errorMessage",
                        "A bill has already been generated for this appointment."
                );

        verify(request)
                .setAttribute(
                        "existingBill",
                        existingBill
                );

        verify(billingService, never())
                .getAppointmentForBilling(
                        10
                );

        verify(request)
                .getRequestDispatcher(
                        "generateBill.jsp"
                );
    }

    @Test
    void shouldRejectPreviewWhenAppointmentDoesNotExist()
            throws Exception {

        when(request.getParameter(
                "action"))
                .thenReturn(
                        "generate"
                );

        when(request.getParameter(
                "appointmentNo"))
                .thenReturn(
                        "999"
                );

        when(billingService
                .getBillByAppointmentNo(
                        999
                ))
                .thenReturn(null);

        when(billingService
                .getAppointmentForBilling(
                        999
                ))
                .thenReturn(null);

        billingServlet.doGet(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "errorMessage",
                        "Appointment not found."
                );

        verify(request)
                .getRequestDispatcher(
                        "generateBill.jsp"
                );
    }

    @Test
    void shouldRejectPreviewWhenAppointmentIsCancelled()
            throws Exception {

        Appointment appointment =
                createAppointment(
                        AppointmentStatus.CANCELLED
                );

        when(request.getParameter(
                "action"))
                .thenReturn(
                        "generate"
                );

        when(request.getParameter(
                "appointmentNo"))
                .thenReturn(
                        "10"
                );

        when(billingService
                .getBillByAppointmentNo(
                        10
                ))
                .thenReturn(null);

        when(billingService
                .getAppointmentForBilling(
                        10
                ))
                .thenReturn(
                        appointment
                );

        billingServlet.doGet(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "errorMessage",
                        "Cancelled appointments cannot be billed."
                );

        verify(request)
                .getRequestDispatcher(
                        "generateBill.jsp"
                );
    }

    @Test
    void shouldGenerateBillForStaff()
            throws Exception {

        Bill bill =
                createBill(
                        5,
                        10
                );

        Appointment appointment =
                createAppointment(
                        AppointmentStatus.SCHEDULED
                );

        when(request.getParameter(
                "action"))
                .thenReturn(
                        "generate"
                );

        when(request.getParameter(
                "appointmentNo"))
                .thenReturn(
                        "10"
                );

        when(billingService
                .generateBill(
                        10,
                        2
                ))
                .thenReturn(
                        bill
                );

        when(billingService
                .getAppointmentForBilling(
                        10
                ))
                .thenReturn(
                        appointment
                );

        billingServlet.doPost(
                request,
                response
        );

        verify(billingService)
                .generateBill(
                        10,
                        2
                );

        verify(request)
                .setAttribute(
                        "successMessage",
                        "Bill generated successfully."
                );

        verify(request)
                .setAttribute(
                        "bill",
                        bill
                );

        verify(request)
                .setAttribute(
                        "appointment",
                        appointment
                );

        verify(request)
                .getRequestDispatcher(
                        "billReceipt.jsp"
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldRejectAdminWhenGeneratingBill()
            throws Exception {

        when(session.getAttribute(
                "role"))
                .thenReturn(
                        "ADMIN"
                );

        when(request.getParameter(
                "action"))
                .thenReturn(
                        "generate"
                );

        billingServlet.doPost(
                request,
                response
        );

        verify(response)
                .sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Staff access required."
                );

        verifyNoInteractions(
                billingService
        );
    }

    @Test
    void shouldRejectGenerationWhenAppointmentNumberIsEmpty()
            throws Exception {

        when(request.getParameter(
                "action"))
                .thenReturn(
                        "generate"
                );

        when(request.getParameter(
                "appointmentNo"))
                .thenReturn(
                        ""
                );

        billingServlet.doPost(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "errorMessage",
                        "Appointment number is required."
                );

        verify(request)
                .setAttribute(
                        "consultationFee",
                        BillingService.CONSULTATION_FEE
                );

        verify(request)
                .getRequestDispatcher(
                        "generateBill.jsp"
                );

        verify(billingService, never())
                .generateBill(
                        10,
                        2
                );
    }

    @Test
    void shouldShowGenerationErrorFromService()
            throws Exception {

        when(request.getParameter(
                "action"))
                .thenReturn(
                        "generate"
                );

        when(request.getParameter(
                "appointmentNo"))
                .thenReturn(
                        "10"
                );

        when(billingService
                .generateBill(
                        10,
                        2
                ))
                .thenThrow(
                        new IllegalStateException(
                                "A bill has already been generated for this appointment."
                        )
                );

        billingServlet.doPost(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "errorMessage",
                        "A bill has already been generated for this appointment."
                );

        verify(request)
                .setAttribute(
                        "consultationFee",
                        BillingService.CONSULTATION_FEE
                );

        verify(request)
                .getRequestDispatcher(
                        "generateBill.jsp"
                );
    }

    @Test
    void shouldDisplayBillWhenBillExists()
            throws Exception {

        Bill bill =
                createBill(
                        5,
                        10
                );

        Appointment appointment =
                createAppointment(
                        AppointmentStatus.SCHEDULED
                );

        when(request.getParameter(
                "appointmentNo"))
                .thenReturn(
                        "10"
                );

        when(billingService
                .getBillByAppointmentNo(
                        10
                ))
                .thenReturn(
                        bill
                );

        when(billingService
                .getAppointmentForBilling(
                        10
                ))
                .thenReturn(
                        appointment
                );

        billingServlet.doGet(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "bill",
                        bill
                );

        verify(request)
                .setAttribute(
                        "appointment",
                        appointment
                );

        verify(request)
                .getRequestDispatcher(
                        "billReceipt.jsp"
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldShowErrorWhenBillDoesNotExist()
            throws Exception {

        when(request.getParameter(
                "appointmentNo"))
                .thenReturn(
                        "999"
                );

        when(billingService
                .getBillByAppointmentNo(
                        999
                ))
                .thenReturn(null);

        when(billingService
                .getAllBills())
                .thenReturn(
                        List.of()
                );

        billingServlet.doGet(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "errorMessage",
                        "Bill not found."
                );

        verify(request)
                .getRequestDispatcher(
                        "searchBill.jsp"
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

        when(request.getParameter(
                "appointmentNo"))
                .thenReturn(
                        "ABC"
                );

        when(billingService
                .getAllBills())
                .thenReturn(
                        List.of()
                );

        billingServlet.doGet(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "errorMessage",
                        "Invalid appointment number."
                );

        verify(request)
                .getRequestDispatcher(
                        "searchBill.jsp"
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldThrowServletExceptionWhenBillingFails()
            throws Exception {

        when(request.getParameter(
                "action"))
                .thenReturn(
                        "generate"
                );

        when(request.getParameter(
                "appointmentNo"))
                .thenReturn(
                        "10"
                );

        when(billingService
                .generateBill(
                        10,
                        2
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
                                billingServlet
                                        .doPost(
                                                request,
                                                response
                                        )
                );

        assertEquals(
                "Unable to generate bill.",
                exception.getMessage()
        );
    }

    private Appointment createAppointment(
            AppointmentStatus status) {

        Patient patient =
                new Patient(
                        1,
                        "Test Patient",
                        "Colombo",
                        "0771234567"
                );

        Dentist dentist =
                new Dentist(
                        2,
                        "Dr. Silva"
                );

        Treatment treatment =
                new Treatment(
                        3,
                        "Cleaning",
                        5000.00
                );

        return new Appointment(
                "10",
                LocalDate.now(),
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

    private Bill createBill(
            int billId,
            int appointmentNo) {

        return new Bill(
                billId,
                appointmentNo,
                2,
                2500.00,
                5000.00,
                LocalDate.now()
        );
    }
}