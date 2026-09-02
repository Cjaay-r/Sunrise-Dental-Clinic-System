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
        Delete Dentist - Sunrise Dental Clinic
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
                Delete Dentist
            </h1>

            <p>
                Confirm before removing this dentist
                from the system.
            </p>

        </div>

        <a href="dentist?dentistId=<%= dentist.getDentistId() %>"
           class="back-button">

            Dentist Details

        </a>

    </div>


    <div class="details-card">

        <div class="delete-warning">

            <h2>
                Are you sure?
            </h2>

            <p>
                You are about to permanently delete
                this dentist record.
            </p>

        </div>


        <div class="details-grid">

            <div class="detail-item">

                <span>
                    Dentist ID
                </span>

                <strong>
                    <%= dentist.getDentistId() %>
                </strong>

            </div>


            <div class="detail-item">

                <span>
                    Dentist Name
                </span>

                <strong>
                    <%= dentist.getDentistName() %>
                </strong>

            </div>


            <div class="detail-item">

                <span>
                    Specialization
                </span>

                <strong>
                    <%= dentist.getSpecialization() %>
                </strong>

            </div>


            <div class="detail-item">

                <span>
                    Contact Number
                </span>

                <strong>
                    <%= dentist.getContactNumber() %>
                </strong>

            </div>

        </div>


        <div class="details-actions">

            <a href="dentist?dentistId=<%= dentist.getDentistId() %>"
               class="secondary-button">

                Cancel

            </a>


            <form action="dentist"
                  method="post"
                  class="inline-action-form">

                <input type="hidden"
                       name="action"
                       value="delete">

                <input type="hidden"
                       name="dentistId"
                       value="<%= dentist.getDentistId() %>">

                <button type="submit"
                        class="danger-button">

                    Confirm Delete

                </button>

            </form>

        </div>

    </div>

</div>

</body>

</html>