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
            (String) request.getAttribute(
                    "errorMessage"
            );

    String treatmentType =
            request.getParameter("treatmentType") != null
            ? request.getParameter("treatmentType")
            : "";

    String treatmentPrice =
            request.getParameter("treatmentPrice") != null
            ? request.getParameter("treatmentPrice")
            : "";
%>

<!DOCTYPE html>

<html>

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        Add Treatment - Sunrise Dental Clinic
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
                Add New Treatment
            </h1>

            <p>
                Enter the treatment type
                and its current price.
            </p>

        </div>

        <a href="treatment"
           class="back-button">

            Back to Treatments

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
                   value="add">


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

                Add Treatment

            </button>

        </form>


        <div class="details-actions">

            <a href="treatment"
               class="secondary-button">

                Cancel

            </a>

        </div>

    </div>

</div>

</body>

</html>