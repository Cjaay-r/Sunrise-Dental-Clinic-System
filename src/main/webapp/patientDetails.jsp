<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="sunrisedentalsystem.model.Patient" %>

<%
    if (session.getAttribute("loggedInUser") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    Patient patient =
            (Patient) request.getAttribute("patient");

    String successMessage =
            (String) request.getAttribute("successMessage");

    if (patient == null) {
        response.sendRedirect("patient");
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
        Patient Details - Sunrise Dental Clinic
    </title>

    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/management.css">

</head>

<body>

<div class="management-page">

    <div class="management-header">

        <div>

            <p class="page-label">
                PATIENT MANAGEMENT
            </p>

            <h1>
                Patient Details
            </h1>

            <p>
                View the patient's registered information.
            </p>

        </div>

        <a href="dashboard.jsp"
           class="back-button">

            Dashboard

        </a>

    </div>


    <% if (successMessage != null) { %>

        <div class="form-message success">
            <%= successMessage %>
        </div>

    <% } %>


    <div class="details-card">

        <div class="details-heading">

            <div class="patient-badge">
                P
            </div>

            <div>

                <p>
                    PATIENT RECORD
                </p>

                <h2>
                    <%= patient.getPatientName() %>
                </h2>

            </div>

        </div>


        <div class="details-grid">

            <div class="detail-item">

                <span>
                    Patient ID
                </span>

                <strong>
                    <%= patient.getPatientId() %>
                </strong>

            </div>


            <div class="detail-item">

                <span>
                    Patient Name
                </span>

                <strong>
                    <%= patient.getPatientName() %>
                </strong>

            </div>


            <div class="detail-item">

                <span>
                    Contact Number
                </span>

                <strong>
                    <%= patient.getContactNumber() %>
                </strong>

            </div>


            <div class="detail-item wide">

                <span>
                    Address
                </span>

                <strong>
                    <%= patient.getAddress() %>
                </strong>

            </div>

        </div>


        <div class="details-actions">

            <a href="patient"
               class="secondary-button">

                Search Another Patient

            </a>

            <a href="registerPatient.jsp"
               class="primary-link">

                Register New Patient

            </a>

        </div>

    </div>

</div>

</body>

</html>