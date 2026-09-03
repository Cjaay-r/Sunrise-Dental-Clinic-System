<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.List" %>
<%@ page import="sunrisedentalsystem.model.Appointment" %>
<%@ page import="sunrisedentalsystem.model.AppointmentStatus" %>

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

    String appointmentNo =
            request.getParameter("appointmentNo") != null
            ? request.getParameter("appointmentNo")
            : "";

    List<Appointment> appointments =
            (List<Appointment>) request.getAttribute(
                    "appointments"
            );

    Integer scheduledCountValue =
            (Integer) request.getAttribute(
                    "scheduledCount"
            );

    Integer cancelledCountValue =
            (Integer) request.getAttribute(
                    "cancelledCount"
            );

    String selectedStatus =
            (String) request.getAttribute(
                    "selectedStatus"
            );

    int scheduledCount =
            scheduledCountValue != null
            ? scheduledCountValue
            : 0;

    int cancelledCount =
            cancelledCountValue != null
            ? cancelledCountValue
            : 0;

    if (selectedStatus == null) {
        selectedStatus = "ALL";
    }

    int displayedCount =
            appointments != null
            ? appointments.size()
            : 0;
%>

<!DOCTYPE html>

<html>

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        Appointment Management - Sunrise Dental Clinic
    </title>

    <link rel="stylesheet"
          href="css/style.css">

    <link rel="stylesheet"
          href="css/management.css">

    <link rel="stylesheet"
          href="css/appointment.css">

</head>

<body>

<div class="management-page">

    <div class="management-header">

        <div>

            <p class="page-label">
                APPOINTMENT MANAGEMENT
            </p>

            <h1>
                Manage Appointments
            </h1>

            <p>
                Search appointments and view
                current appointment records.
            </p>

        </div>

        <a href="dashboard.jsp"
           class="back-button">

            Back to Dashboard

        </a>

    </div>


    <div class="appointment-home-grid">

        <div class="appointment-search-panel">

            <div class="panel-heading">

                <div>

                    <h2>
                        Search Appointment
                    </h2>

                    <p>
                        Enter an appointment number
                        to view its details.
                    </p>

                </div>

            </div>


            <% if (errorMessage != null) { %>

                <div class="form-message error">
                    <%= errorMessage %>
                </div>

            <% } %>


            <form action="appointment"
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

                    Search Appointment

                </button>

            </form>

        </div>


        <div class="appointment-register-panel">

            <% if (isStaff) { %>

                <h2>
                    Register Appointment
                </h2>

                <p>
                    Create a new appointment
                    for a registered patient.
                </p>

                <a href="appointment?action=register"
                   class="secondary-button">

                    Register Appointment

                </a>

            <% } else { %>

                <h2>
                    Appointment Records
                </h2>

                <p>
                    Search and view registered
                    appointment information.
                </p>

            <% } %>

        </div>

    </div>


    <div class="appointment-overview-heading">

        <div>

            <p class="page-label">
                APPOINTMENT OVERVIEW
            </p>

            <h2>
                Current Appointment Status
            </h2>

        </div>

    </div>


    <div class="appointment-summary-grid">

        <a href="appointment?status=SCHEDULED"
           class="appointment-summary-card scheduled-summary">

            <div>

                <span class="summary-label">
                    Scheduled
                </span>

                <strong>
                    <%= scheduledCount %>
                </strong>

            </div>

            <span class="summary-view">
                View Scheduled
            </span>

        </a>


        <a href="appointment?status=CANCELLED"
           class="appointment-summary-card cancelled-summary">

            <div>

                <span class="summary-label">
                    Cancelled
                </span>

                <strong>
                    <%= cancelledCount %>
                </strong>

            </div>

            <span class="summary-view">
                View Cancelled
            </span>

        </a>

    </div>


    <div class="appointment-directory-panel">

        <div class="appointment-directory-header">

            <div>

                <p class="page-label">
                    APPOINTMENT DIRECTORY
                </p>

                <h2>
                    Appointment Records
                </h2>

            </div>


            <span class="appointment-record-count">

                <%= displayedCount %>

                <%= displayedCount == 1
                        ? "Record"
                        : "Records" %>

            </span>

        </div>


        <div class="appointment-filter-bar">

            <a href="appointment"
               class="appointment-filter <%= "ALL".equals(selectedStatus)
                       ? "active"
                       : "" %>">

                All

            </a>


            <a href="appointment?status=SCHEDULED"
               class="appointment-filter <%= "SCHEDULED".equals(selectedStatus)
                       ? "active"
                       : "" %>">

                Scheduled

            </a>


            <a href="appointment?status=CANCELLED"
               class="appointment-filter <%= "CANCELLED".equals(selectedStatus)
                       ? "active"
                       : "" %>">

                Cancelled

            </a>

        </div>


        <% if (appointments != null
                && !appointments.isEmpty()) { %>

            <div class="appointment-table-wrapper">

                <table class="appointment-table">

                    <thead>

                        <tr>

                            <th>
                                No
                            </th>

                            <th>
                                Patient
                            </th>

                            <th>
                                Dentist
                            </th>

                            <th>
                                Treatment
                            </th>

                            <th>
                                Date
                            </th>

                            <th>
                                Time
                            </th>

                            <th>
                                Status
                            </th>

                            <th>
                                Action
                            </th>

                        </tr>

                    </thead>


                    <tbody>

                    <% for (Appointment appointment
                            : appointments) { %>

                        <%
                            boolean cancelled =
                                    appointment.getStatus()
                                            == AppointmentStatus.CANCELLED;
                        %>

                        <tr>

                            <td>

                                <strong>
                                    #<%= appointment.getAppointmentNo() %>
                                </strong>

                            </td>


                            <td>
                                <%= appointment
                                        .getPatient()
                                        .getPatientName() %>
                            </td>


                            <td>
                                <%= appointment
                                        .getDentist()
                                        .getDentistName() %>
                            </td>


                            <td>
                                <%= appointment
                                        .getTreatment()
                                        .getTreatmentType() %>
                            </td>


                            <td>
                                <%= appointment.getAppointmentDate() %>
                            </td>


                            <td>
                                <%= appointment.getAppointmentTime() %>
                            </td>


                            <td>

                                <span class="<%= cancelled
                                        ? "appointment-table-status cancelled"
                                        : "appointment-table-status scheduled" %>">

                                    <%= appointment.getStatus() %>

                                </span>

                            </td>


                            <td>

                                <a
                                    href="appointment?appointmentNo=<%= appointment.getAppointmentNo() %>"
                                    class="appointment-table-link">

                                    View

                                </a>

                            </td>

                        </tr>

                    <% } %>

                    </tbody>

                </table>

            </div>

        <% } else { %>

            <div class="appointment-empty-state">

                <h3>
                    No Appointments Found
                </h3>

                <% if ("SCHEDULED".equals(selectedStatus)) { %>

                    <p>
                        There are currently no
                        scheduled appointments.
                    </p>

                <% } else if ("CANCELLED".equals(selectedStatus)) { %>

                    <p>
                        There are currently no
                        cancelled appointments.
                    </p>

                <% } else { %>

                    <p>
                        There are currently no
                        appointment records available.
                    </p>

                <% } %>


                <% if (isStaff) { %>

                    <a href="appointment?action=register"
                       class="primary-link">

                        Register Appointment

                    </a>

                <% } %>

            </div>

        <% } %>

    </div>

</div>

</body>

</html>