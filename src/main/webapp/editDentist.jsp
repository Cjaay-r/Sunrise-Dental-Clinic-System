<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="sunrisedentalsystem.model.Dentist" %>

<%
    if (session.getAttribute("loggedInUser") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    if (!"ADMIN".equals(session.getAttribute("role"))) {
        response.sendError(
                HttpServletResponse.SC_FORBIDDEN,
                "Admin access required."
        );
        return;
    }

    Dentist dentist =
            (Dentist) request.getAttribute("dentist");

    String errorMessage =
            (String) request.getAttribute("errorMessage");

    if (dentist == null) {
        response.sendRedirect("dentist");
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
        Edit Dentist - Sunrise Dental Clinic
    </title>

    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/management.css">

</head>

<body>

<div class="management-page">

    <div class="management-header">

        <div>

            <p class="page-label">
                DENTIST MANAGEMENT
            </p>

            <h1>
                Edit Dentist
            </h1>

            <p>
                Update the dentist's professional
                and contact information.
            </p>

        </div>

        <a href="dentist?dentistId=<%= dentist.getDentistId() %>"
           class="back-button">

            Dentist Details

        </a>

    </div>


    <div class="form-panel">

        <% if (errorMessage != null) { %>

            <div class="form-message error">
                <%= errorMessage %>
            </div>

        <% } %>


        <form action="dentist"
              method="post"
              class="management-form">

            <input type="hidden"
                   name="action"
                   value="update">

            <input type="hidden"
                   name="dentistId"
                   value="<%= dentist.getDentistId() %>">


            <div class="form-group">

                <label>
                    Dentist ID
                </label>

                <input
                    type="text"
                    value="<%= dentist.getDentistId() %>"
                    disabled>

            </div>


            <div class="form-group">

                <label for="dentistName">
                    Dentist Name
                </label>

                <input
                    type="text"
                    id="dentistName"
                    name="dentistName"
                    maxlength="100"
                    placeholder="Enter dentist name"
                    value="<%= dentist.getDentistName() %>"
                    required>

            </div>


            <div class="form-group">

                <label for="specialization">
                    Specialization
                </label>

                <input
                    type="text"
                    id="specialization"
                    name="specialization"
                    maxlength="100"
                    placeholder="Enter specialization"
                    value="<%= dentist.getSpecialization() %>"
                    required>

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
                    value="<%= dentist.getContactNumber() %>"
                    title="Enter a 10-digit contact number starting with 0"
                    required>

            </div>


            <button type="submit"
                    class="primary-button">

                Update Dentist

            </button>

        </form>


        <div class="details-actions">

            <a href="dentist?dentistId=<%= dentist.getDentistId() %>"
               class="secondary-button">

                Cancel

            </a>

        </div>

    </div>

</div>

</body>

</html>