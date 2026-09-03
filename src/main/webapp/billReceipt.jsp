<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="sunrisedentalsystem.model.Appointment" %>
<%@ page import="sunrisedentalsystem.model.Bill" %>

<%
    if (session.getAttribute("loggedInUser") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    Bill bill =
            (Bill) request.getAttribute(
                    "bill"
            );

    Appointment appointment =
            (Appointment) request.getAttribute(
                    "appointment"
            );

    String successMessage =
            (String) request.getAttribute(
                    "successMessage"
            );

    if (bill == null) {
        response.sendRedirect("billing");
        return;
    }
%>

<!DOCTYPE html>

<html>

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        Bill Receipt - Sunrise Dental Clinic
    </title>

    <link rel="stylesheet"
          href="css/style.css">

    <link rel="stylesheet"
          href="css/management.css">

    <link rel="stylesheet"
          href="css/billing.css">

</head>

<body>

<div class="management-page billing-receipt-page">

    <div class="receipt-page-actions">

        <a href="billing"
           class="back-button">

            Billing Management

        </a>

        <button type="button"
                id="printReceiptButton"
                class="primary-button">

            Print Receipt

        </button>

    </div>


    <% if (successMessage != null) { %>

        <div class="form-message success receipt-message">
            <%= successMessage %>
        </div>

    <% } %>


    <div class="bill-receipt">

        <div class="receipt-header">

            <div>

                <p class="receipt-clinic-label">
                    SUNRISE DENTAL CLINIC
                </p>

                <h1>
                    Bill Receipt
                </h1>

                <p>
                    Dental Appointment Billing
                </p>

            </div>

            <div class="receipt-number">

                <span>
                    Bill Number
                </span>

                <strong>
                    #<%= bill.getBillId() %>
                </strong>

            </div>

        </div>


        <div class="receipt-meta">

            <div>

                <span>
                    Appointment Number
                </span>

                <strong>
                    #<%= bill.getAppointmentNo() %>
                </strong>

            </div>

            <div>

                <span>
                    Generated Date
                </span>

                <strong>
                    <%= bill.getGeneratedDate() %>
                </strong>

            </div>

        </div>


        <% if (appointment != null) { %>

            <div class="receipt-section">

                <h2>
                    Appointment Details
                </h2>

                <div class="receipt-details-grid">

                    <div>

                        <span>
                            Patient
                        </span>

                        <strong>
                            <%= appointment
                                    .getPatient()
                                    .getPatientName() %>
                        </strong>

                        <small>
                            ID:
                            <%= appointment
                                    .getPatient()
                                    .getPatientId() %>
                        </small>

                    </div>


                    <div>

                        <span>
                            Dentist
                        </span>

                        <strong>
                            <%= appointment
                                    .getDentist()
                                    .getDentistName() %>
                        </strong>

                    </div>


                    <div>

                        <span>
                            Treatment
                        </span>

                        <strong>
                            <%= appointment
                                    .getTreatment()
                                    .getTreatmentType() %>
                        </strong>

                    </div>


                    <div>

                        <span>
                            Appointment Date
                        </span>

                        <strong>
                            <%= appointment.getAppointmentDate() %>
                        </strong>

                        <small>
                            <%= appointment.getAppointmentTime() %>
                        </small>

                    </div>

                </div>

            </div>

        <% } %>


        <div class="receipt-section">

            <h2>
                Charges
            </h2>

            <div class="receipt-charge-row">

                <span>
                    Consultation Fee
                </span>

                <strong>
                    Rs.
                    <%= String.format(
                            "%,.2f",
                            bill.getConsultationFee()
                    ) %>
                </strong>

            </div>


            <div class="receipt-charge-row">

                <span>
                    Treatment Cost
                </span>

                <strong>
                    Rs.
                    <%= String.format(
                            "%,.2f",
                            bill.getTreatmentCost()
                    ) %>
                </strong>

            </div>


            <div class="receipt-total-row">

                <span>
                    Total Amount
                </span>

                <strong>
                    Rs.
                    <%= String.format(
                            "%,.2f",
                            bill.getTotalAmount()
                    ) %>
                </strong>

            </div>

        </div>


        <div class="receipt-footer">

            <p>
                Thank you for choosing
                Sunrise Dental Clinic.
            </p>

            <span>
               Generated by:
               <%= bill.getGeneratedByStaffName() %>
             </span>

        </div>

    </div>

</div>

<script src="js/billing.js"></script>

</body>

</html>