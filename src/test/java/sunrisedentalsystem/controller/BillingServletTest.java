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

import sunrisedentalsystem.model.Bill;
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

        billingService = mock(BillingService.class);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);

        loggedInUser = mock(User.class);

        billingServlet =
                new BillingServlet(billingService);
    }

    @Test
    void shouldRejectBillWhenRequiredFieldsAreEmpty()
            throws Exception {

        when(request.getParameter("appointmentNo"))
                .thenReturn("");

        when(request.getParameter("consultationFee"))
                .thenReturn("");

        when(request.getParameter("treatmentCost"))
                .thenReturn("");

        when(request.getRequestDispatcher(
                "generateBill.jsp"))
                .thenReturn(dispatcher);

        billingServlet.doPost(request, response);

        verify(request).setAttribute(
                "errorMessage",
                "All billing fields are required."
        );

        verify(dispatcher)
                .forward(request, response);

        verifyNoInteractions(billingService);
    }

    @Test
    void shouldRedirectToLoginWhenUserIsNotLoggedIn()
            throws Exception {

        stubValidBillingParameters();

        when(request.getSession(false))
                .thenReturn(null);

        billingServlet.doPost(request, response);

        verify(response)
                .sendRedirect("login.jsp");

        verifyNoInteractions(billingService);
    }

    @Test
    void shouldGenerateBillWhenDetailsAreValid()
            throws Exception {

        stubValidBillingParameters();
        stubLoggedInUser();

        when(billingService.calculateAndSaveBill(
                any(Bill.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        when(request.getRequestDispatcher(
                "billReceipt.jsp"))
                .thenReturn(dispatcher);

        billingServlet.doPost(request, response);

        ArgumentCaptor<Bill> captor =
                ArgumentCaptor.forClass(Bill.class);

        verify(billingService)
                .calculateAndSaveBill(
                        captor.capture()
                );

        Bill bill = captor.getValue();

        assertNotNull(bill);

        assertEquals(
                101,
                bill.getAppointmentNo()
        );

        assertEquals(
                1,
                bill.getGeneratedByStaffId()
        );

        assertEquals(
                1500.0,
                bill.getConsultationFee()
        );

        assertEquals(
                5000.0,
                bill.getTreatmentCost()
        );

        assertEquals(
                6500.0,
                bill.getTotalAmount()
        );

        assertNotNull(
                bill.getGeneratedDate()
        );

        verify(request).setAttribute(
                eq("bill"),
                any(Bill.class)
        );

        verify(request).setAttribute(
                "successMessage",
                "Bill generated successfully."
        );

        verify(dispatcher)
                .forward(request, response);
    }

    @Test
    void shouldRejectInvalidBillingValues()
            throws Exception {

        when(request.getParameter("appointmentNo"))
                .thenReturn("ABC");

        when(request.getParameter("consultationFee"))
                .thenReturn("invalid");

        when(request.getParameter("treatmentCost"))
                .thenReturn("5000");

        stubLoggedInUser();

        when(request.getRequestDispatcher(
                "generateBill.jsp"))
                .thenReturn(dispatcher);

        billingServlet.doPost(request, response);

        verify(request).setAttribute(
                "errorMessage",
                "Invalid billing details."
        );

        verify(dispatcher)
                .forward(request, response);

        verify(billingService, never())
                .calculateAndSaveBill(
                        any(Bill.class)
                );
    }

    @Test
    void shouldDisplayBillWhenBillExists()
            throws Exception {

        Bill bill = mock(Bill.class);

        when(request.getParameter("appointmentNo"))
                .thenReturn("101");

        when(billingService
                .getBillByAppointmentNo(101))
                .thenReturn(bill);

        when(request.getRequestDispatcher(
                "billReceipt.jsp"))
                .thenReturn(dispatcher);

        billingServlet.doGet(request, response);

        verify(billingService)
                .getBillByAppointmentNo(101);

        verify(request).setAttribute(
                "bill",
                bill
        );

        verify(dispatcher)
                .forward(request, response);
    }

    @Test
    void shouldShowErrorWhenBillDoesNotExist()
            throws Exception {

        when(request.getParameter("appointmentNo"))
                .thenReturn("999");

        when(billingService
                .getBillByAppointmentNo(999))
                .thenReturn(null);

        when(request.getRequestDispatcher(
                "searchBill.jsp"))
                .thenReturn(dispatcher);

        billingServlet.doGet(request, response);

        verify(request).setAttribute(
                "errorMessage",
                "Bill not found."
        );

        verify(dispatcher)
                .forward(request, response);
    }

    @Test
    void shouldThrowServletExceptionWhenBillingFails()
            throws Exception {

        stubValidBillingParameters();
        stubLoggedInUser();

        when(billingService.calculateAndSaveBill(
                any(Bill.class)))
                .thenThrow(
                        new SQLException(
                                "Database error"
                        )
                );

        assertThrows(
                ServletException.class,
                () -> billingServlet
                        .doPost(request, response)
        );
    }

    private void stubValidBillingParameters() {

        when(request.getParameter("appointmentNo"))
                .thenReturn("101");

        when(request.getParameter("consultationFee"))
                .thenReturn("1500.00");

        when(request.getParameter("treatmentCost"))
                .thenReturn("5000.00");
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