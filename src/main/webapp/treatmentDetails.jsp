<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="sunrisedentalsystem.model.Treatment" %>

<%
    if (session.getAttribute("loggedInUser") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    Treatment treatment =
            (Treatment) request.getAttribute(
                    "treatment"
            );

    String successMessage =
            (String) request.getAttribute(
                    "successMessage"
            );

    if (treatment == null) {
        response.sendRedirect("treatment");
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
        Treatment Details - Sunrise Dental Clinic
    </title>

    <link rel="stylesheet"
          href="css/style.css">

    <link rel="stylesheet"
          href="css/management.css">

    <link rel="stylesheet"
          href="css/treatment.css">

</head>

<body>

<div class="management-page">

    <div class="management-header">

        <div>

            <p class="page-label">
                TREATMENT MANAGEMENT
            </p>

            <h1>
                Treatment Details
            </h1>

            <p>
                View the treatment type
                and current treatment price.
            </p>

        </div>

        <a href="treatment"
           class="back-button">

            Treatment Directory

        </a>

    </div>


    <% if (successMessage != null) { %>

        <div class="form-message success">
            <%= successMessage %>
        </div>

    <% } %>


    <div class="treatment-details-card">

        <div class="treatment-details-heading">

            <div class="treatment-badge">
                T
            </div>

            <div>

                <p>
                    TREATMENT RECORD
                </p>

                <h2>
                    <%= treatment.getTreatmentType() %>
                </h2>

            </div>

        </div>


        <div class="treatment-details-grid">

            <div class="treatment-detail-item">

                <span>
                    Treatment ID
                </span>

                <strong>
                    <%= treatment.getTreatmentId() %>
                </strong>

            </div>


            <div class="treatment-detail-item">

                <span>
                    Treatment Type
                </span>

                <strong>
                    <%= treatment.getTreatmentType() %>
                </strong>

            </div>


            <div class="treatment-detail-item">

                <span>
                    Treatment Price
                </span>

                <strong>
                    Rs.
                    <%= String.format(
                            "%,.2f",
                            treatment.getTreatmentPrice()
                    ) %>
                </strong>

            </div>

        </div>


        <div class="treatment-details-actions">

            <a href="treatment"
               class="secondary-button">

                Back to Directory

            </a>


            <a href="treatment?action=edit&treatmentId=<%= treatment.getTreatmentId() %>"
               class="primary-link">

                Edit Treatment

            </a>


            <a href="treatment?action=delete&treatmentId=<%= treatment.getTreatmentId() %>"
               class="treatment-danger-link">

                Delete Treatment

            </a>

        </div>

    </div>

</div>

</body>

</html>