<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="sunrisedentalsystem.model.Treatment" %>

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

    Treatment treatment =
            (Treatment) request.getAttribute(
                    "treatment"
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
        Delete Treatment - Sunrise Dental Clinic
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
                Delete Treatment
            </h1>

            <p>
                Confirm before removing this
                treatment from the system.
            </p>

        </div>

        <a
            href="treatment?treatmentId=<%= treatment.getTreatmentId() %>"
            class="back-button">

            Back to Treatment Details

        </a>

    </div>


    <div class="treatment-details-card">

        <div class="treatment-delete-warning">

            <h2>
                Are you sure?
            </h2>

            <p>
                You are about to permanently delete
                this treatment record.
            </p>

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

            <a
                href="treatment?treatmentId=<%= treatment.getTreatmentId() %>"
                class="secondary-button">

                Cancel

            </a>


            <form action="treatment"
                  method="post"
                  class="treatment-inline-form">

                <input type="hidden"
                       name="action"
                       value="delete">

                <input type="hidden"
                       name="treatmentId"
                       value="<%= treatment.getTreatmentId() %>">

                <button type="submit"
                        class="treatment-delete-button">

                    Confirm Delete

                </button>

            </form>

        </div>

    </div>

</div>

</body>

</html>