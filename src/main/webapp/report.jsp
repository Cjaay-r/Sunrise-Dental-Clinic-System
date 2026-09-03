<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="sunrisedentalsystem.model.ClinicReport" %>

<%
    if (session.getAttribute("loggedInUser") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String role =
            (String) session.getAttribute(
                    "role"
            );

    if (!"ADMIN".equals(role)) {
        response.sendError(
                403,
                "Admin access required."
        );
        return;
    }

    ClinicReport clinicReport =
            (ClinicReport) request.getAttribute(
                    "clinicReport"
            );

    String errorMessage =
            (String) request.getAttribute(
                    "errorMessage"
            );
%>

<!DOCTYPE html>

<html>

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        Clinic Summary Report - Sunrise Dental Clinic
    </title>

    <link rel="stylesheet"
          href="css/style.css">

    <link rel="stylesheet"
          href="css/management.css">

    <link rel="stylesheet"
          href="css/report.css">

</head>

<body>

<div class="management-page report-page">

    <div class="page-top">

        <div>

            <p class="page-label">
                MANAGEMENT REPORT
            </p>

            <h1>
                Clinic Summary Report
            </h1>

            <p>
                Review daily clinic activity,
                billing and treatment information.
            </p>

        </div>

        <a href="dashboard.jsp"
           class="back-button">

            Back to Dashboard

        </a>

    </div>


    <div class="management-card report-filter">

        <form action="report"
              method="get">

            <div class="report-date-field">

                <label for="reportDate">
                    Report Date
                </label>

                <input type="date"
                       id="reportDate"
                       name="reportDate"
                       value="<%= request.getAttribute("selectedDate") %>"
                       required>

            </div>

            <button type="submit"
                    class="primary-button">

                View Report

            </button>

        </form>

    </div>


    <% if (errorMessage != null) { %>

        <div class="form-message error">

            <%= errorMessage %>

        </div>

    <% } %>


    <% if (clinicReport != null) { %>

        <div class="report-date-heading">

            <span>
                Summary for
            </span>

            <strong>
                <%= clinicReport.getReportDate() %>
            </strong>

        </div>


        <div class="report-summary-grid">

            <div class="report-summary-card">

                <span>
                    Total Appointments
                </span>

                <strong>
                    <%= clinicReport.getTotalAppointments() %>
                </strong>

            </div>


            <div class="report-summary-card">

                <span>
                    Scheduled
                </span>

                <strong>
                    <%= clinicReport.getScheduledAppointments() %>
                </strong>

            </div>


            <div class="report-summary-card">

                <span>
                    Cancelled
                </span>

                <strong>
                    <%= clinicReport.getCancelledAppointments() %>
                </strong>

            </div>


            <div class="report-summary-card">

                <span>
                    Bills Generated
                </span>

                <strong>
                    <%= clinicReport.getBillsGenerated() %>
                </strong>

            </div>

        </div>


        <div class="report-detail-grid">

            <div class="management-card report-detail-card">

                <span>
                    Total Revenue
                </span>

                <strong class="report-money">

                    Rs.
                    <%= String.format(
                            "%,.2f",
                            clinicReport.getTotalRevenue()
                    ) %>

                </strong>

                <p>
                    Revenue from bills generated
                    on the selected date.
                </p>

            </div>


            <div class="management-card report-detail-card">

                <span>
                    Most Common Treatment
                </span>

                <% if (clinicReport
                        .getMostCommonTreatment() != null) { %>

                    <strong>

                        <%= clinicReport
                                .getMostCommonTreatment() %>

                    </strong>

                    <p>

                        <%= clinicReport
                                .getMostCommonTreatmentCount() %>
                        appointment(s)

                    </p>

                <% } else { %>

                    <strong>
                        No treatment data
                    </strong>

                    <p>
                        No scheduled treatment
                        activity was found.
                    </p>

                <% } %>

            </div>

        </div>

    <% } %>

</div>

</body>

</html>