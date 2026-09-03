<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="sunrisedentalsystem.model.User" %>

<%
    User loggedInUser =
            (User) session.getAttribute(
                    "loggedInUser"
            );

    if (loggedInUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String role =
            (String) session.getAttribute(
                    "role"
            );

    boolean isAdmin =
            "ADMIN".equals(role);

    boolean isStaff =
            "STAFF".equals(role);
%>

<!DOCTYPE html>

<html>

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        Help - Sunrise Dental Clinic
    </title>

    <link rel="stylesheet"
          href="css/style.css">

    <link rel="stylesheet"
          href="css/management.css">

    <link rel="stylesheet"
          href="css/help.css">

</head>

<body>

<div class="management-page">

    <div class="management-header">

        <div>

            <p class="page-label">
                SYSTEM HELP
            </p>

            <h1>
                Help & User Guide
            </h1>

            <p>
                Step-by-step guidance for using
                the Sunrise Dental Clinic system.
            </p>

        </div>

        <a href="dashboard.jsp"
           class="back-button">

            Back to Dashboard

        </a>

    </div>


    <div class="help-intro">

        <div>

            <p class="page-label">
                GETTING STARTED
            </p>

            <h2>
                Welcome, <%= loggedInUser.getUsername() %>
            </h2>

            <p>
                You are currently signed in as
                <strong><%= role %></strong>.
                Use the guide below to complete
                common clinic tasks.
            </p>

        </div>

        <span class="help-role-badge">
            <%= role %>
        </span>

    </div>


    <% if (isStaff) { %>

        <div class="help-section-heading">

            <p class="page-label">
                STAFF GUIDE
            </p>

            <h2>
                Daily Clinic Tasks
            </h2>

            <p>
                Follow these steps when handling
                patients, appointments and billing.
            </p>

        </div>


        <div class="help-grid">

            <div class="help-card">

                <h3>
                    Register a Patient
                </h3>

                <p>
                    Open Patient Management and select
                    Register Patient.
                </p>

                <ul>
                    <li>Enter the patient's name.</li>
                    <li>Enter the patient's address.</li>
                    <li>Enter the 10-digit contact number.</li>
                    <li>Select Register Patient.</li>
                </ul>

                <a href="registerPatient.jsp"
                   class="help-link">

                    Open Patient Registration

                </a>

            </div>


            <div class="help-card">

                <h3>
                    Find a Patient
                </h3>

                <p>
                    Patients can be found without
                    remembering their patient ID.
                </p>

                <ul>
                    <li>Open Patient Management.</li>
                    <li>Search using the patient's name or phone number.</li>
                    <li>Select View from the matching result.</li>
                    <li>Review the patient's saved information.</li>
                </ul>

                <a href="patient"
                   class="help-link">

                    Open Patient Management

                </a>

            </div>


            <div class="help-card">

                <h3>
                    Register an Appointment
                </h3>

                <p>
                    Create an appointment for an
                    existing registered patient.
                </p>

                <ul>
                    <li>Open Appointment Management.</li>
                    <li>Select Register Appointment.</li>
                    <li>Search and select the patient.</li>
                    <li>Select the dentist and treatment.</li>
                    <li>Select the appointment date and time.</li>
                    <li>Submit the appointment.</li>
                </ul>

                <a href="appointment?action=register"
                   class="help-link">

                    Register Appointment

                </a>

            </div>


            <div class="help-card">

                <h3>
                    Search an Appointment
                </h3>

                <p>
                    Use the generated appointment number
                    to view an appointment.
                </p>

                <ul>
                    <li>Open Appointment Management.</li>
                    <li>Enter the appointment number.</li>
                    <li>Select Search Appointment.</li>
                    <li>Review the patient, dentist and treatment details.</li>
                </ul>

                <a href="appointment"
                   class="help-link">

                    Open Appointments

                </a>

            </div>


            <div class="help-card">

                <h3>
                    Cancel an Appointment
                </h3>

                <p>
                    Scheduled appointments can be
                    cancelled when required.
                </p>

                <ul>
                    <li>Search for the appointment.</li>
                    <li>Open its appointment details.</li>
                    <li>Select the cancellation option.</li>
                    <li>Confirm the cancellation.</li>
                </ul>

                <a href="appointment"
                   class="help-link">

                    Find Appointment

                </a>

            </div>


            <div class="help-card">

                <h3>
                    Generate a Bill
                </h3>

                <p>
                    Bills are generated from
                    scheduled appointments.
                </p>

                <ul>
                    <li>Open Billing Management.</li>
                    <li>Select Generate Bill.</li>
                    <li>Enter the appointment number.</li>
                    <li>Review the consultation and treatment charges.</li>
                    <li>Select Generate Bill.</li>
                    <li>Print the generated receipt when required.</li>
                </ul>

                <a href="billing?action=generate"
                   class="help-link">

                    Generate Bill

                </a>

            </div>


            <div class="help-card">

                <h3>
                    View Dentists
                </h3>

                <p>
                    Staff can view registered dentists
                    and their information.
                </p>

                <ul>
                    <li>Open Dentist Management.</li>
                    <li>View the dentist directory.</li>
                    <li>Use a dentist ID to search for a specific record.</li>
                    <li>Select View to open full details.</li>
                </ul>

                <a href="dentist"
                   class="help-link">

                    View Dentists

                </a>

            </div>


            <div class="help-card">

                <h3>
                    View Treatments
                </h3>

                <p>
                    Staff can view available treatments
                    and current prices.
                </p>

                <ul>
                    <li>Open Treatment Management.</li>
                    <li>View the treatment directory.</li>
                    <li>Check the treatment type and current price.</li>
                    <li>Staff cannot add, edit or delete treatments.</li>
                </ul>

                <a href="treatment"
                   class="help-link">

                    View Treatments

                </a>

            </div>

        </div>

    <% } %>


    <% if (isAdmin) { %>

        <div class="help-section-heading">

            <p class="page-label">
                ADMINISTRATOR GUIDE
            </p>

            <h2>
                Administration Tasks
            </h2>

            <p>
                Use the administration tools to
                maintain clinic system records.
            </p>

        </div>


        <div class="help-grid">

            <div class="help-card">

                <h3>
                    Manage Staff Accounts
                </h3>

                <p>
                    Create and maintain accounts
                    used by clinic staff.
                </p>

                <ul>
                    <li>Open Staff Management.</li>
                    <li>Select Add Staff Member to create an account.</li>
                    <li>Search staff by ID or name.</li>
                    <li>Open a staff record to view its details.</li>
                    <li>Select Edit Staff to update the record.</li>
                </ul>

                <a href="staff"
                   class="help-link">

                    Open Staff Management

                </a>

            </div>


            <div class="help-card">

                <h3>
                    Manage Dentists
                </h3>

                <p>
                    Administrators maintain
                    dentist information.
                </p>

                <ul>
                    <li>Open Dentist Management.</li>
                    <li>Add new dentist records when required.</li>
                    <li>Search or open an existing dentist.</li>
                    <li>Edit dentist information when needed.</li>
                </ul>

                <a href="dentist"
                   class="help-link">

                    Open Dentist Management

                </a>

            </div>


            <div class="help-card">

                <h3>
                    Manage Treatments
                </h3>

                <p>
                    Administrators maintain treatment
                    types and their current prices.
                </p>

                <ul>
                    <li>Open Treatment Management.</li>
                    <li>Add a new treatment and price.</li>
                    <li>View existing treatment information.</li>
                    <li>Edit treatment information when required.</li>
                    <li>Delete a treatment only when appropriate.</li>
                </ul>

                <a href="treatment"
                   class="help-link">

                    Open Treatment Management

                </a>

            </div>


            <div class="help-card">

                <h3>
                    Review Appointments
                </h3>

                <p>
                    Administrators can review
                    clinic appointment records.
                </p>

                <ul>
                    <li>Open Appointment Management.</li>
                    <li>Search using an appointment number.</li>
                    <li>View scheduled and cancelled appointments.</li>
                    <li>Open an appointment to review its details.</li>
                </ul>

                <a href="appointment"
                   class="help-link">

                    View Appointments

                </a>

            </div>


            <div class="help-card">

                <h3>
                    Review Billing
                </h3>

                <p>
                    Administrators can view
                    generated billing records.
                </p>

                <ul>
                    <li>Open Billing Management.</li>
                    <li>Review the generated bill directory.</li>
                    <li>Search using an appointment number.</li>
                    <li>Open the receipt to review bill information.</li>
                </ul>

                <a href="billing"
                   class="help-link">

                    View Billing

                </a>

            </div>

        </div>

    <% } %>


    <div class="help-exit-section">

        <div>

            <p class="page-label">
                EXIT SYSTEM
            </p>

            <h2>
                Finish Your Session Safely
            </h2>

            <p>
                When you have finished using the system,
                select Exit System. Your current login
                session will end and you will return to
                the login page.
            </p>

        </div>

        <a href="<%= request.getContextPath() %>/logout"
           class="help-exit-button">

            Exit System

        </a>

    </div>

</div>

</body>

</html>