<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

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

    Appointment appointment =
            (Appointment) request.getAttribute(
                    "appointment"
            );

    String successMessage =
            (String) request.getAttribute(
                    "successMessage"
            );

    String errorMessage =
            (String) request.getAttribute(
                    "errorMessage"
            );

    if (appointment == null) {
        response.sendRedirect("appointment");
        return;
    }

    boolean isCancelled =
            appointment.getStatus()
                    == AppointmentStatus.CANCELLED;
%>

<!DOCTYPE html>

<html>

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        Appointment Details - Sunrise Dental Clinic
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
                Appointment Details
            </h1>

            <p>
                View the complete information
                for this appointment.
            </p>

        </div>

        <a href="appointment"
           class="back-button">

            Appointment Search

        </a>

    </div>


    <% if (successMessage != null) { %>

        <div class="form-message success">
            <%= successMessage %>
        </div>

    <% } %>


    <% if (errorMessage != null) { %>

        <div class="form-message error">
            <%= errorMessage %>
        </div>

    <% } %>


    <div class="appointment-details-card">

        <div class="appointment-details-heading">

            <div class="appointment-details-badge">
                A
            </div>

            <div>

                <p>
                    APPOINTMENT RECORD
                </p>

                <h2>
                    Appointment
                    #<%= appointment.getAppointmentNo() %>
                </h2>

            </div>


            <span class="<%= isCancelled
                    ? "appointment-status cancelled"
                    : "appointment-status scheduled" %>">

                <%= appointment.getStatus() %>

            </span>

        </div>


        <div class="appointment-section">

            <h3>
                Appointment Information
            </h3>


            <div class="appointment-details-grid">

                <div class="appointment-detail-item">

                    <span>
                        Appointment Number
                    </span>

                    <strong>
                        <%= appointment.getAppointmentNo() %>
                    </strong>

                </div>


                <div class="appointment-detail-item">

                    <span>
                        Appointment Date
                    </span>

                    <strong>
                        <%= appointment.getAppointmentDate() %>
                    </strong>

                </div>


                <div class="appointment-detail-item">

                    <span>
                        Appointment Time
                    </span>

                    <strong>
                        <%= appointment.getAppointmentTime() %>
                    </strong>

                </div>

            </div>

        </div>


        <div class="appointment-section">

            <h3>
                Patient
            </h3>


            <div class="appointment-details-grid">

                <div class="appointment-detail-item">

                    <span>
                        Patient ID
                    </span>

                    <strong>
                        <%= appointment
                                .getPatient()
                                .getPatientId() %>
                    </strong>

                </div>


                <div class="appointment-detail-item appointment-wide-item">

                    <span>
                        Patient Name
                    </span>

                    <strong>
                        <%= appointment
                                .getPatient()
                                .getPatientName() %>
                    </strong>

                </div>

            </div>

        </div>


        <div class="appointment-section">

            <h3>
                Dentist
            </h3>


            <div class="appointment-details-grid">

                <div class="appointment-detail-item">

                    <span>
                        Dentist ID
                    </span>

                    <strong>
                        <%= appointment
                                .getDentist()
                                .getDentistId() %>
                    </strong>

                </div>


                <div class="appointment-detail-item appointment-wide-item">

                    <span>
                        Dentist Name
                    </span>

                    <strong>
                        <%= appointment
                                .getDentist()
                                .getDentistName() %>
                    </strong>

                </div>

            </div>

        </div>


        <div class="appointment-section">

            <h3>
                Treatment
            </h3>


            <div class="appointment-details-grid">

                <div class="appointment-detail-item">

                    <span>
                        Treatment ID
                    </span>

                    <strong>
                        <%= appointment
                                .getTreatment()
                                .getTreatmentId() %>
                    </strong>

                </div>


                <div class="appointment-detail-item">

                    <span>
                        Treatment Type
                    </span>

                    <strong>
                        <%= appointment
                                .getTreatment()
                                .getTreatmentType() %>
                    </strong>

                </div>


                <div class="appointment-detail-item">

                    <span>
                        Treatment Price
                    </span>

                    <strong>
                        Rs.
                        <%= String.format(
                                "%,.2f",
                                appointment
                                        .getTreatment()
                                        .getTreatmentPrice()
                        ) %>
                    </strong>

                </div>

            </div>

        </div>


        <div class="appointment-details-actions">

            <a href="appointment"
               class="secondary-button">

                Back to Search

            </a>


            <% if (isStaff) { %>

                <a href="appointment?action=register"
                   class="primary-link">

                    Register New Appointment

                </a>

            <% } %>


            <% if (isStaff && !isCancelled) { %>

                <a
                    href="appointment?action=cancel&appointmentNo=<%= appointment.getAppointmentNo() %>"
                    class="appointment-cancel-link">

                    Cancel Appointment

                </a>

            <% } %>

        </div>

    </div>

</div>

</body>

</html>