package sunrisedentalsystem.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sunrisedentalsystem.dao.BillDAOImpl;
import sunrisedentalsystem.model.Bill;
import sunrisedentalsystem.model.User;
import sunrisedentalsystem.service.BillingService;
import sunrisedentalsystem.service.BillingServiceImpl;

@WebServlet("/billing")
public class BillingServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private BillingService billingService;

    public BillingServlet() {
    }

    BillingServlet(BillingService billingService) {
        this.billingService = billingService;
    }

    @Override
    public void init() throws ServletException {

        if (billingService == null) {

            billingService =
                    new BillingServiceImpl(
                            new BillDAOImpl()
                    );
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String appointmentNoText =
                request.getParameter("appointmentNo");

        String consultationFeeText =
                request.getParameter("consultationFee");

        String treatmentCostText =
                request.getParameter("treatmentCost");

        if (isEmpty(appointmentNoText)
                || isEmpty(consultationFeeText)
                || isEmpty(treatmentCostText)) {

            request.setAttribute(
                    "errorMessage",
                    "All billing fields are required."
            );

            RequestDispatcher dispatcher =
                    request.getRequestDispatcher(
                            "generateBill.jsp"
                    );

            dispatcher.forward(
                    request,
                    response
            );

            return;
        }

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

        User loggedInUser =
                (User) session.getAttribute(
                        "loggedInUser"
                );

        try {

            int appointmentNo =
                    Integer.parseInt(
                            appointmentNoText
                    );

            double consultationFee =
                    Double.parseDouble(
                            consultationFeeText
                    );

            double treatmentCost =
                    Double.parseDouble(
                            treatmentCostText
                    );

            if (appointmentNo <= 0
                    || consultationFee < 0
                    || treatmentCost < 0) {

                showInvalidBillingError(
                        request,
                        response
                );

                return;
            }

            Bill bill =
                    new Bill(
                            0,
                            appointmentNo,
                            loggedInUser.getUserId(),
                            consultationFee,
                            treatmentCost,
                            LocalDate.now()
                    );

            Bill savedBill =
                    billingService
                            .calculateAndSaveBill(
                                    bill
                            );

            request.setAttribute(
                    "bill",
                    savedBill
            );

            request.setAttribute(
                    "successMessage",
                    "Bill generated successfully."
            );

            request.getRequestDispatcher(
                    "billReceipt.jsp"
            ).forward(
                    request,
                    response
            );

        } catch (NumberFormatException e) {

            showInvalidBillingError(
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

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String appointmentNoText =
                request.getParameter(
                        "appointmentNo"
                );

        if (isEmpty(appointmentNoText)) {

            request.setAttribute(
                    "errorMessage",
                    "Appointment number is required."
            );

            request.getRequestDispatcher(
                    "searchBill.jsp"
            ).forward(
                    request,
                    response
            );

            return;
        }

        try {

            int appointmentNo =
                    Integer.parseInt(
                            appointmentNoText
                    );

            Bill bill =
                    billingService
                            .getBillByAppointmentNo(
                                    appointmentNo
                            );

            if (bill != null) {

                request.setAttribute(
                        "bill",
                        bill
                );

                request.getRequestDispatcher(
                        "billReceipt.jsp"
                ).forward(
                        request,
                        response
                );

            } else {

                request.setAttribute(
                        "errorMessage",
                        "Bill not found."
                );

                request.getRequestDispatcher(
                        "searchBill.jsp"
                ).forward(
                        request,
                        response
                );
            }

        } catch (NumberFormatException e) {

            request.setAttribute(
                    "errorMessage",
                    "Invalid appointment number."
            );

            request.getRequestDispatcher(
                    "searchBill.jsp"
            ).forward(
                    request,
                    response
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to retrieve bill.",
                    e
            );
        }
    }

    private boolean isEmpty(String value) {

        return value == null
                || value.trim().isEmpty();
    }

    private void showInvalidBillingError(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute(
                "errorMessage",
                "Invalid billing details."
        );

        request.getRequestDispatcher(
                "generateBill.jsp"
        ).forward(
                request,
                response
        );
    }
}