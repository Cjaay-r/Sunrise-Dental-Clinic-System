<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

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

    String errorMessage =
            (String) request.getAttribute("errorMessage");

    String dentistName =
            request.getParameter("dentistName") != null
            ? request.getParameter("dentistName")
            : "";

    String specialization =
            request.getParameter("specialization") != null
            ? request.getParameter("specialization")
            : "";

    String contactNumber =
            request.getParameter("contactNumber") != null
            ? request.getParameter("contactNumber")
            : "";
%>

<!DOCTYPE html>

<html>

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        Add Dentist - Sunrise Dental Clinic
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
                Add New Dentist
            </h1>

            <p>
                Enter the dentist's professional
                and contact information.
            </p>

        </div>

        <a href="dentist"
           class="back-button">

            Dentist Directory

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
                   value="add">


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
                    value="<%= dentistName %>"
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
                    value="<%= specialization %>"
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
                    value="<%= contactNumber %>"
                    title="Enter a 10-digit contact number starting with 0"
                    required>

            </div>


            <button type="submit"
                    class="primary-button">

                Add Dentist

            </button>

        </form>


        <div class="details-actions">

            <a href="dentist"
               class="secondary-button">

                Cancel

            </a>

        </div>

    </div>

</div>

</body>

</html>