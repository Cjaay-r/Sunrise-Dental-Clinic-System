package sunrisedentalsystem.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sunrisedentalsystem.dao.BillDAOImpl;
import sunrisedentalsystem.model.Appointment;
import sunrisedentalsystem.model.AppointmentStatus;
import sunrisedentalsystem.model.Bill;
import sunrisedentalsystem.model.User;
import sunrisedentalsystem.service.BillingService;
import sunrisedentalsystem.service.BillingServiceImpl;

@WebServlet("/billing")
public class BillingServlet
        extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private BillingService billingService;

    public BillingServlet() {
    }

    BillingServlet(
            BillingService billingService) {

        this.billingService =
                billingService;
    }

    @Override
    public void init()
            throws ServletException {

        if (billingService == null) {

            billingService =
                    new BillingServiceImpl(
                            new BillDAOImpl()
                    );
        }
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException,
                   IOException {

        HttpSession session =
                request.getSession(false);

        if (session == null
                || session.getAttribute(
                        "loggedInUser") == null) {

            response.sendRedirect(
                    "login.jsp"
            );

            return;
        }

        String action =
                request.getParameter(
                        "action"
                );

        try {

            if ("generate".equals(action)) {

                if (!isStaff(session)) {

                    response.sendError(
                            HttpServletResponse.SC_FORBIDDEN,
                            "Staff access required."
                    );

                    return;
                }

                showGenerateBill(
                        request,
                        response
                );

                return;
            }

            String appointmentNoText =
                    request.getParameter(
                            "appointmentNo"
                    );

            if (isEmpty(
                    appointmentNoText)) {

                showBillingManagement(
                        request,
                        response
                );

                return;
            }

            int appointmentNo =
                    parseAppointmentNumber(
                            appointmentNoText
                    );

            if (appointmentNo <= 0) {

                request.setAttribute(
                        "errorMessage",
                        "Invalid appointment number."
                );

                showBillingManagement(
                        request,
                        response
                );

                return;
            }

            Bill bill =
                    billingService
                            .getBillByAppointmentNo(
                                    appointmentNo
                            );

            if (bill == null) {

                request.setAttribute(
                        "errorMessage",
                        "Bill not found."
                );

                showBillingManagement(
                        request,
                        response
                );

                return;
            }

            showBillReceipt(
                    bill,
                    request,
                    response
            );

        } catch (NumberFormatException e) {

            request.setAttribute(
                    "errorMessage",
                    "Invalid appointment number."
            );

            try {

                showBillingManagement(
                        request,
                        response
                );

            } catch (SQLException sqlException) {

                throw new ServletException(
                        "Unable to retrieve bills.",
                        sqlException
                );
            }

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to retrieve billing information.",
                    e
            );
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException,
                   IOException {

        HttpSession session =
                request.getSession(false);

        if (session == null
                || session.getAttribute(
                        "loggedInUser") == null) {

            response.sendRedirect(
                    "login.jsp"
            );

            return;
        }

        if (!isStaff(session)) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Staff access required."
            );

            return;
        }

        String action =
                request.getParameter(
                        "action"
                );

        if (!"generate".equals(action)) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid billing action."
            );

            return;
        }

        String appointmentNoText =
                request.getParameter(
                        "appointmentNo"
                );

        if (isEmpty(
                appointmentNoText)) {

            request.setAttribute(
                    "errorMessage",
                    "Appointment number is required."
            );

            request.setAttribute(
                    "consultationFee",
                    BillingService.CONSULTATION_FEE
            );

            request.getRequestDispatcher(
                    "generateBill.jsp"
            ).forward(
                    request,
                    response
            );

            return;
        }

        try {

            int appointmentNo =
                    parseAppointmentNumber(
                            appointmentNoText
                    );

            if (appointmentNo <= 0) {

                showInvalidBillingError(
                        request,
                        response
                );

                return;
            }

            User loggedInUser =
                    (User) session.getAttribute(
                            "loggedInUser"
                    );

            Bill savedBill =
                    billingService
                            .generateBill(
                                    appointmentNo,
                                    loggedInUser
                                            .getUserId()
                            );

            request.setAttribute(
                    "successMessage",
                    "Bill generated successfully."
            );

            showBillReceipt(
                    savedBill,
                    request,
                    response
            );

        } catch (NumberFormatException e) {

            showInvalidBillingError(
                    request,
                    response
            );

        } catch (IllegalStateException e) {

            request.setAttribute(
                    "errorMessage",
                    e.getMessage()
            );

            request.setAttribute(
                    "consultationFee",
                    BillingService.CONSULTATION_FEE
            );

            request.getRequestDispatcher(
                    "generateBill.jsp"
            ).forward(
                    request,
                    response
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to generate bill.",
                    e
            );
        }
    }

    private void showGenerateBill(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException,
                   IOException,
                   SQLException {

        request.setAttribute(
                "consultationFee",
                BillingService.CONSULTATION_FEE
        );

        String appointmentNoText =
                request.getParameter(
                        "appointmentNo"
                );

        if (isEmpty(
                appointmentNoText)) {

            request.getRequestDispatcher(
                    "generateBill.jsp"
            ).forward(
                    request,
                    response
            );

            return;
        }

        int appointmentNo;

        try {

            appointmentNo =
                    parseAppointmentNumber(
                            appointmentNoText
                    );

        } catch (NumberFormatException e) {

            request.setAttribute(
                    "errorMessage",
                    "Invalid appointment number."
            );

            request.getRequestDispatcher(
                    "generateBill.jsp"
            ).forward(
                    request,
                    response
            );

            return;
        }

        if (appointmentNo <= 0) {

            request.setAttribute(
                    "errorMessage",
                    "Invalid appointment number."
            );

            request.getRequestDispatcher(
                    "generateBill.jsp"
            ).forward(
                    request,
                    response
            );

            return;
        }

        Bill existingBill =
                billingService
                        .getBillByAppointmentNo(
                                appointmentNo
                        );

        if (existingBill != null) {

            request.setAttribute(
                    "errorMessage",
                    "A bill has already been generated for this appointment."
            );

            request.setAttribute(
                    "existingBill",
                    existingBill
            );

            request.getRequestDispatcher(
                    "generateBill.jsp"
            ).forward(
                    request,
                    response
            );

            return;
        }

        Appointment appointment =
                billingService
                        .getAppointmentForBilling(
                                appointmentNo
                        );

        if (appointment == null) {

            request.setAttribute(
                    "errorMessage",
                    "Appointment not found."
            );

            request.getRequestDispatcher(
                    "generateBill.jsp"
            ).forward(
                    request,
                    response
            );

            return;
        }

        if (appointment.getStatus()
                == AppointmentStatus.CANCELLED) {

            request.setAttribute(
                    "errorMessage",
                    "Cancelled appointments cannot be billed."
            );

            request.getRequestDispatcher(
                    "generateBill.jsp"
            ).forward(
                    request,
                    response
            );

            return;
        }

        double treatmentCost =
                appointment
                        .getTreatment()
                        .getTreatmentPrice();

        double totalAmount =
                BillingService.CONSULTATION_FEE
                        + treatmentCost;

        request.setAttribute(
                "appointment",
                appointment
        );

        request.setAttribute(
                "treatmentCost",
                treatmentCost
        );

        request.setAttribute(
                "totalAmount",
                totalAmount
        );

        request.getRequestDispatcher(
                "generateBill.jsp"
        ).forward(
                request,
                response
        );
    }

    private void showBillingManagement(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException,
                   IOException,
                   SQLException {

        List<Bill> bills =
                billingService
                        .getAllBills();

        request.setAttribute(
                "bills",
                bills
        );

        request.getRequestDispatcher(
                "searchBill.jsp"
        ).forward(
                request,
                response
        );
    }

    private void showBillReceipt(
            Bill bill,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException,
                   IOException,
                   SQLException {

        Appointment appointment =
                billingService
                        .getAppointmentForBilling(
                                bill.getAppointmentNo()
                        );

        request.setAttribute(
                "bill",
                bill
        );

        request.setAttribute(
                "appointment",
                appointment
        );

        request.getRequestDispatcher(
                "billReceipt.jsp"
        ).forward(
                request,
                response
        );
    }

    private int parseAppointmentNumber(
            String appointmentNoText) {

        return Integer.parseInt(
                appointmentNoText.trim()
        );
    }

    private boolean isStaff(
            HttpSession session) {

        return "STAFF".equals(
                session.getAttribute(
                        "role"
                )
        );
    }

    private boolean isEmpty(
            String value) {

        return value == null
                || value.trim().isEmpty();
    }

    private void showInvalidBillingError(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException,
                   IOException {

        request.setAttribute(
                "errorMessage",
                "Invalid billing details."
        );

        request.setAttribute(
                "consultationFee",
                BillingService.CONSULTATION_FEE
        );

        request.getRequestDispatcher(
                "generateBill.jsp"
        ).forward(
                request,
                response
        );
    }
}