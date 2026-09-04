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

    String role =
            (String) session.getAttribute("role");

    if (!"STAFF".equals(role)) {
        response.sendError(
                HttpServletResponse.SC_FORBIDDEN,
                "Staff access required."
        );
        return;
    }

    String errorMessage =
            (String) request.getAttribute(
                    "errorMessage"
            );

    Appointment appointment =
            (Appointment) request.getAttribute(
                    "appointment"
            );

    Bill existingBill =
            (Bill) request.getAttribute(
                    "existingBill"
            );

    Double consultationFeeValue =
            (Double) request.getAttribute(
                    "consultationFee"
            );

    Double treatmentCostValue =
            (Double) request.getAttribute(
                    "treatmentCost"
            );

    Double totalAmountValue =
            (Double) request.getAttribute(
                    "totalAmount"
            );

    double consultationFee =
            consultationFeeValue != null
            ? consultationFeeValue
            : 2500.00;

    String appointmentNo =
            request.getParameter("appointmentNo") != null
            ? request.getParameter("appointmentNo")
            : "";
%>

<!DOCTYPE html>

<html>

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        Generate Bill - Sunrise Dental Clinic
    </title>

    <link rel="stylesheet"
          href="css/style.css">

    <link rel="stylesheet"
          href="css/management.css">

    <link rel="stylesheet"
          href="css/billing.css">

</head>

<body>

<div class="management-page">

    <div class="management-header">

        <div>

            <p class="page-label">
                BILLING MANAGEMENT
            </p>

            <h1>
                Generate Bill
            </h1>

            <p>
                Select an appointment and review
                the calculated charges before billing.
            </p>

        </div>

        <a href="billing"
           class="back-button">

            Back to Billing

        </a>

    </div>


    <div class="billing-generate-layout">

        <div class="billing-appointment-search">

            <div class="panel-heading">

                <div>

                    <h2>
                        Find Appointment
                    </h2>

                    <p>
                        Enter the appointment number
                        you want to bill.
                    </p>

                </div>

            </div>


            <% if (errorMessage != null) { %>

                <div class="form-message error">
                    <%= errorMessage %>
                </div>

            <% } %>


            <form action="billing"
                  method="get"
                  class="management-form">

                <input type="hidden"
                       name="action"
                       value="generate">

                <div class="form-group">

                    <label for="appointmentNo">
                        Appointment Number
                    </label>

                    <input
                        type="number"
                        id="appointmentNo"
                        name="appointmentNo"
                        min="1"
                        placeholder="Enter appointment number"
                        value="<%= appointmentNo %>"
                        required>

                </div>

                <button type="submit"
                        class="primary-button">

                    Find Appointment

                </button>

            </form>


            <% if (existingBill != null) { %>

                <div class="existing-bill-box">

                    <span>
                        Existing Bill
                    </span>

                    <strong>
                        Bill #<%= existingBill.getBillId() %>
                    </strong>

                    <a
                        href="billing?appointmentNo=<%= existingBill.getAppointmentNo() %>"
                        class="billing-secondary-link">

                        View Existing Receipt

                    </a>

                </div>

            <% } %>

        </div>


        <div class="billing-fee-panel">

            <p class="page-label">
                CLINIC BILLING
            </p>

            <h2>
                Fee Information
            </h2>

            <div class="billing-fee-row">

                <span>
                    Consultation Fee
                </span>

                <strong>
                    Rs.
                    <%= String.format(
                            "%,.2f",
                            consultationFee
                    ) %>
                </strong>

            </div>

            <p>
                Treatment cost is loaded automatically
                from the appointment's treatment.
            </p>

        </div>

    </div>


    <% if (appointment != null) { %>

        <div class="billing-preview-panel">

            <div class="billing-preview-header">

                <div>

                    <p class="page-label">
                        BILL PREVIEW
                    </p>

                    <h2>
                        Appointment #<%= appointment.getAppointmentNo() %>
                    </h2>

                </div>

                <span class="billing-ready-badge">
                    READY TO BILL
                </span>

            </div>


            <div class="billing-appointment-details">

                <div class="billing-detail-item">

                    <span>
                        Patient
                    </span>

                    <strong>
                        <%= appointment
                                .getPatient()
                                .getPatientName() %>
                    </strong>

                    <small>
                        Patient ID:
                        <%= appointment
                                .getPatient()
                                .getPatientId() %>
                    </small>

                </div>


                <div class="billing-detail-item">

                    <span>
                        Dentist
                    </span>

                    <strong>
                        <%= appointment
                                .getDentist()
                                .getDentistName() %>
                    </strong>

                    <small>
                        Dentist ID:
                        <%= appointment
                                .getDentist()
                                .getDentistId() %>
                    </small>

                </div>


                <div class="billing-detail-item">

                    <span>
                        Treatment
                    </span>

                    <strong>
                        <%= appointment
                                .getTreatment()
                                .getTreatmentType() %>
                    </strong>

                    <small>
                        Treatment ID:
                        <%= appointment
                                .getTreatment()
                                .getTreatmentId() %>
                    </small>

                </div>


                <div class="billing-detail-item">

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


            <div class="billing-calculation-box">

                <div class="billing-calculation-row">

                    <span>
                        Consultation Fee
                    </span>

                    <strong>
                        Rs.
                        <%= String.format(
                                "%,.2f",
                                consultationFee
                        ) %>
                    </strong>

                </div>


                <div class="billing-calculation-row">

                    <span>
                        Treatment Cost
                    </span>

                    <strong>
                        Rs.
                        <%= String.format(
                                "%,.2f",
                                treatmentCostValue
                        ) %>
                    </strong>

                </div>


                <div class="billing-calculation-row total">

                    <span>
                        Total Amount
                    </span>

                    <strong>
                        Rs.
                        <%= String.format(
                                "%,.2f",
                                totalAmountValue
                        ) %>
                    </strong>

                </div>

            </div>


            <div class="billing-preview-actions">

                <a href="billing"
                   class="billing-cancel-button">

                    Cancel

                </a>


                <form action="billing"
                      method="post">

                    <input type="hidden"
                           name="action"
                           value="generate">

                    <input type="hidden"
                           name="appointmentNo"
                           value="<%= appointment.getAppointmentNo() %>">

                    <button type="submit"
                            class="primary-button">

                        Generate Bill

                    </button>

                </form>

            </div>

        </div>

    <% } %>

</div>

</body>

</html>