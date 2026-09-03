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

    String errorMessage =
            (String) request.getAttribute(
                    "errorMessage"
            );

    if (treatment == null) {
        response.sendRedirect("treatment");
        return;
    }

    String treatmentType =
            request.getParameter("treatmentType") != null
            ? request.getParameter("treatmentType")
            : treatment.getTreatmentType();

    String treatmentPrice =
            request.getParameter("treatmentPrice") != null
            ? request.getParameter("treatmentPrice")
            : String.valueOf(
                    treatment.getTreatmentPrice()
            );
%>

<!DOCTYPE html>

<html>

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        Edit Treatment - Sunrise Dental Clinic
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
                Edit Treatment
            </h1>

            <p>
                Update the treatment type
                or its current price.
            </p>

        </div>

        <a
            href="treatment?treatmentId=<%= treatment.getTreatmentId() %>"
            class="back-button">

            Back to Treatment Details

        </a>

    </div>


    <div class="treatment-form-panel">

        <% if (errorMessage != null) { %>

            <div class="form-message error">
                <%= errorMessage %>
            </div>

        <% } %>


        <form action="treatment"
              method="post"
              class="management-form">

            <input type="hidden"
                   name="action"
                   value="update">

            <input type="hidden"
                   name="treatmentId"
                   value="<%= treatment.getTreatmentId() %>">


            <div class="form-group">

                <label>
                    Treatment ID
                </label>

                <input
                    type="text"
                    value="<%= treatment.getTreatmentId() %>"
                    disabled>

            </div>


            <div class="form-group">

                <label for="treatmentType">
                    Treatment Type
                </label>

                <input
                    type="text"
                    id="treatmentType"
                    name="treatmentType"
                    maxlength="100"
                    placeholder="Enter treatment type"
                    value="<%= treatmentType %>"
                    required>

            </div>


            <div class="form-group">

                <label for="treatmentPrice">
                    Treatment Price
                </label>

                <input
                    type="number"
                    id="treatmentPrice"
                    name="treatmentPrice"
                    min="0"
                    step="0.01"
                    placeholder="Enter treatment price"
                    value="<%= treatmentPrice %>"
                    required>

            </div>


            <button type="submit"
                    class="primary-button">

                Update Treatment

            </button>

        </form>


        <div class="details-actions">

            <a
                href="treatment?treatmentId=<%= treatment.getTreatmentId() %>"
                class="secondary-button">

                Cancel

            </a>

        </div>

    </div>

</div>

</body>

</html>