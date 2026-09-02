<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="sunrisedentalsystem.model.Dentist" %>

<%
    if (session.getAttribute("loggedInUser") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    Dentist dentist =
            (Dentist) request.getAttribute("dentist");

    String successMessage =
            (String) request.getAttribute("successMessage");

    String role =
            (String) session.getAttribute("role");

    boolean isAdmin =
            "ADMIN".equals(role);

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
        Dentist Details - Sunrise Dental Clinic
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
                Dentist Details
            </h1>

            <p>
                View the dentist's professional
                and contact information.
            </p>

        </div>

        <a href="dentist"
           class="back-button">

            Dentist Directory

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
                D
            </div>

            <div>

                <p>
                    DENTIST RECORD
                </p>

                <h2>
                    <%= dentist.getDentistName() %>
                </h2>

            </div>

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

            <a href="dentist"
               class="secondary-button">

                Back to Directory

            </a>


            <% if (isAdmin) { %>

                <a
                    href="dentist?action=edit&dentistId=<%= dentist.getDentistId() %>"
                    class="primary-link">

                    Edit Dentist

                </a>


                <a href="dentist?action=delete&dentistId=<%= dentist.getDentistId() %>"
                   class="danger-link">

                  Delete Dentist

                     </a>

            <% } %>

        </div>

    </div>

</div>

</body>

</html>