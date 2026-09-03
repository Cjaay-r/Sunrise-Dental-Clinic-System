<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
    if (session.getAttribute("loggedInUser") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String errorMessage =
            (String) request.getAttribute(
                    "errorMessage"
            );

    String patientName =
            request.getParameter("patientName") != null
            ? request.getParameter("patientName")
            : "";

    String address =
            request.getParameter("address") != null
            ? request.getParameter("address")
            : "";

    String contactNumber =
            request.getParameter("contactNumber") != null
            ? request.getParameter("contactNumber")
            : "";

    String email =
            request.getParameter("email") != null
            ? request.getParameter("email")
            : "";
%>

<!DOCTYPE html>

<html>

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        Register Patient - Sunrise Dental Clinic
    </title>

    <link rel="stylesheet"
          href="css/style.css">

    <link rel="stylesheet"
          href="css/management.css">

</head>

<body>

<div class="management-page">

    <div class="management-header">

        <div>

            <p class="page-label">
                PATIENT MANAGEMENT
            </p>

            <h1>
                Register New Patient
            </h1>

            <p>
                Enter the patient's information
                to create a new patient record.
            </p>

        </div>

        <a href="patient"
           class="back-button">

            Back to Patients

        </a>

    </div>


    <div class="form-panel">

        <% if (errorMessage != null) { %>

            <div class="form-message error">
                <%= errorMessage %>
            </div>

        <% } %>


        <form action="patient"
              method="post"
              class="management-form">

            <div class="form-group">

                <label for="patientName">
                    Patient Name
                </label>

                <input
                    type="text"
                    id="patientName"
                    name="patientName"
                    maxlength="100"
                    placeholder="Enter patient name"
                    value="<%= patientName %>"
                    autocomplete="name"
                    required>

            </div>


            <div class="form-group">

                <label for="address">
                    Address
                </label>

                <textarea
                    id="address"
                    name="address"
                    rows="4"
                    maxlength="255"
                    placeholder="Enter patient address"
                    required><%= address %></textarea>

            </div>


            <div class="form-group">

                <label for="contactNumber">
                    Contact Number
                </label>

                <input
                    type="tel"
                    id="contactNumber"
                    name="contactNumber"
                    maxlength="10"
                    pattern="0[0-9]{9}"
                    inputmode="numeric"
                    placeholder="Example: 0771234567"
                    value="<%= contactNumber %>"
                    title="Enter a 10-digit contact number starting with 0"
                    autocomplete="tel"
                    required>

            </div>


            <div class="form-group">

                <label for="email">
                    Email Address
                </label>

                <input
                    type="email"
                    id="email"
                    name="email"
                    maxlength="150"
                    placeholder="Optional email address"
                    value="<%= email %>"
                    autocomplete="email">

            </div>


            <button type="submit"
                    class="primary-button">

                Register Patient

            </button>

        </form>


        <div class="details-actions">

            <a href="patient"
               class="secondary-button">

                Search Patients

            </a>

        </div>

    </div>

</div>

</body>

</html>