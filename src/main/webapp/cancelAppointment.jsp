<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="sunrisedentalsystem.model.Appointment" %>

<%
    if (session.getAttribute("loggedInUser") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    if (!"STAFF".equals(
            session.getAttribute("role"))) {

        response.sendError(
                HttpServletResponse.SC_FORBIDDEN,
                "Staff access required."
        );

        return;
    }

    Appointment appointment =
            (Appointment) request.getAttribute(
                    "appointment"
            );

    if (appointment == null) {
        response.sendRedirect("appointment");
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
        Cancel Appointment - Sunrise Dental Clinic
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
                Cancel Appointment
            </h1>

            <p>
                Confirm before cancelling
                this appointment.
            </p>

        </div>

        <a href="appointment?appointmentNo=<%= appointment.getAppointmentNo() %>"
           class="back-button">

            Appointment Details

        </a>

    </div>


    <div class="appointment-details-card">

        <div class="appointment-cancel-warning">

            <h2>
                Are you sure?
            </h2>

            <p>
                This appointment will remain
                in the system but its status
                will be changed to CANCELLED.
            </p>

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
                        Date
                    </span>

                    <strong>
                        <%= appointment.getAppointmentDate() %>
                    </strong>

                </div>


                <div class="appointment-detail-item">

                    <span>
                        Time
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

            <a href="appointment?appointmentNo=<%= appointment.getAppointmentNo() %>"
               class="secondary-button">

                Keep Appointment

            </a>


            <form action="appointment"
                  method="post"
                  class="appointment-inline-form">

                <input type="hidden"
                       name="action"
                       value="cancel">

                <input type="hidden"
                       name="appointmentNo"
                       value="<%= appointment.getAppointmentNo() %>">

                <button type="submit"
                        class="appointment-confirm-cancel">

                    Confirm Cancellation

                </button>

            </form>

        </div>

    </div>

</div>

</body>

</html>