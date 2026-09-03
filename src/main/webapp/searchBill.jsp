<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.List" %>
<%@ page import="sunrisedentalsystem.model.Bill" %>

<%
    if (session.getAttribute("loggedInUser") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String role =
            (String) session.getAttribute("role");

    boolean isStaff =
            "STAFF".equals(role);

    String errorMessage =
            (String) request.getAttribute(
                    "errorMessage"
            );

    List<Bill> bills =
            (List<Bill>) request.getAttribute(
                    "bills"
            );

    int billCount =
            bills != null
            ? bills.size()
            : 0;

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
        Billing Management - Sunrise Dental Clinic
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
                Manage Billing
            </h1>

            <p>
                Search existing bills and
                manage patient billing records.
            </p>

        </div>

        <a href="dashboard.jsp"
           class="back-button">

            Back to Dashboard

        </a>

    </div>


    <div class="billing-home-grid">

        <div class="billing-search-panel">

            <div class="panel-heading">

                <div>

                    <h2>
                        Search Bill
                    </h2>

                    <p>
                        Enter an appointment number
                        to find its generated bill.
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

                    Search Bill

                </button>

            </form>

        </div>


        <div class="billing-generate-panel">

            <% if (isStaff) { %>

                <h2>
                    Generate Bill
                </h2>

                <p>
                    Generate a bill from a
                    scheduled appointment.
                </p>

                <a href="billing?action=generate"
                   class="secondary-button">

                    Generate Bill

                </a>

            <% } else { %>

                <h2>
                    Billing Records
                </h2>

                <p>
                    Search and view existing
                    patient billing records.
                </p>

            <% } %>

        </div>

    </div>


    <div class="billing-info-strip">

        <div>

            <span class="billing-info-label">
                Consultation Fee
            </span>

            <strong>
                Rs. 2,500.00
            </strong>

        </div>

        <div>

            <span class="billing-info-label">
                Treatment Cost
            </span>

            <strong>
                Automatic
            </strong>

        </div>

        <div>

            <span class="billing-info-label">
                Total Calculation
            </span>

            <strong>
                Automatic
            </strong>

        </div>

    </div>


    <div class="billing-directory-panel">

        <div class="billing-directory-header">

            <div>

                <p class="page-label">
                    BILL DIRECTORY
                </p>

                <h2>
                    Generated Bills
                </h2>

            </div>

            <span class="billing-record-count">

                <%= billCount %>

                <%= billCount == 1
                        ? "Bill"
                        : "Bills" %>

            </span>

        </div>


        <% if (bills != null
                && !bills.isEmpty()) { %>

            <div class="billing-table-wrapper">

                <table class="billing-table">

                    <thead>

                        <tr>

                            <th>
                                Bill ID
                            </th>

                            <th>
                                Appointment
                            </th>

                            <th>
                                Consultation
                            </th>

                            <th>
                                Treatment
                            </th>

                            <th>
                                Total
                            </th>

                            <th>
                                Date
                            </th>

                            <th>
                                Action
                            </th>

                        </tr>

                    </thead>

                    <tbody>

                    <% for (Bill bill : bills) { %>

                        <tr>

                            <td>

                                <strong>
                                    #<%= bill.getBillId() %>
                                </strong>

                            </td>

                            <td>
                                #<%= bill.getAppointmentNo() %>
                            </td>

                            <td>
                                Rs.
                                <%= String.format(
                                        "%,.2f",
                                        bill.getConsultationFee()
                                ) %>
                            </td>

                            <td>
                                Rs.
                                <%= String.format(
                                        "%,.2f",
                                        bill.getTreatmentCost()
                                ) %>
                            </td>

                            <td>

                                <strong class="billing-total-value">

                                    Rs.
                                    <%= String.format(
                                            "%,.2f",
                                            bill.getTotalAmount()
                                    ) %>

                                </strong>

                            </td>

                            <td>
                                <%= bill.getGeneratedDate() %>
                            </td>

                            <td>

                                <a
                                    href="billing?appointmentNo=<%= bill.getAppointmentNo() %>"
                                    class="billing-table-link">

                                    View

                                </a>

                            </td>

                        </tr>

                    <% } %>

                    </tbody>

                </table>

            </div>

        <% } else { %>

            <div class="billing-empty-state">

                <h3>
                    No Bills Generated
                </h3>

                <p>
                    There are currently no
                    billing records available.
                </p>

                <% if (isStaff) { %>

                    <a href="billing?action=generate"
                       class="primary-link">

                        Generate First Bill

                    </a>

                <% } %>

            </div>

        <% } %>

    </div>

</div>

</body>

</html>