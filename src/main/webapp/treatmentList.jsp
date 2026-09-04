<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.List" %>
<%@ page import="sunrisedentalsystem.model.Treatment" %>

<%
    if (session.getAttribute("loggedInUser") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String role =
            (String) session.getAttribute(
                    "role"
            );

    boolean isAdmin =
            "ADMIN".equals(role);

    List<Treatment> treatments =
            (List<Treatment>) request.getAttribute(
                    "treatments"
            );

    String errorMessage =
            (String) request.getAttribute(
                    "errorMessage"
            );

    int treatmentCount =
            treatments != null
            ? treatments.size()
            : 0;
%>

<!DOCTYPE html>

<html>

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        Treatment Management - Sunrise Dental Clinic
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
                Manage Treatments
            </h1>

            <p>
                View available treatments and
                current treatment prices.
            </p>

        </div>

        <a href="dashboard.jsp"
           class="back-button">

            Dashboard

        </a>

    </div>


    <div class="treatment-top-section">

        <div class="action-panel">

            <div class="panel-heading">

                <div>

                    <h2>
                        Search Treatment
                    </h2>

                    <p>
                        Enter a treatment ID to
                        view its information.
                    </p>

                </div>

            </div>


            <% if (errorMessage != null) { %>

                <div class="form-message error">
                    <%= errorMessage %>
                </div>

            <% } %>


            <form action="treatment"
                  method="get"
                  class="management-form">

                <div class="form-group">

                    <label for="treatmentId">
                        Treatment ID
                    </label>

                    <input
                        type="number"
                        id="treatmentId"
                        name="treatmentId"
                        min="1"
                        placeholder="Enter treatment ID"
                        required>

                </div>

                <button type="submit"
                        class="primary-button">

                    Search Treatment

                </button>

            </form>

        </div>


        <div class="treatment-add-panel">

            <% if (isAdmin) { %>

                <h2>
                    Add New Treatment
                </h2>

                <p>
                    Add a treatment type and
                    set its current price.
                </p>

                <a href="treatment?action=add"
                   class="secondary-button">

                    Add Treatment

                </a>

            <% } else { %>

                <h2>
                    Treatment Records
                </h2>

                <p>
                    View available dental
                    treatments and prices.
                </p>

            <% } %>

        </div>

    </div>


    <div class="treatment-list-panel">

        <div class="treatment-list-header">

            <div>

                <p class="page-label">
                    AVAILABLE TREATMENTS
                </p>

                <h2>
                    Treatment Directory
                </h2>

            </div>

            <span class="treatment-record-count">

                <%= treatmentCount %>

                <%= treatmentCount == 1
                        ? "Record"
                        : "Records" %>

            </span>

        </div>


        <% if (treatments != null
                && !treatments.isEmpty()) { %>

            <div class="treatment-table-wrapper">

                <table class="treatment-table">

                    <thead>

                        <tr>

                            <th>
                                ID
                            </th>

                            <th>
                                Treatment Type
                            </th>

                            <th>
                                Treatment Price
                            </th>

                            <th>
                                Action
                            </th>

                        </tr>

                    </thead>

                    <tbody>

                    <% for (Treatment treatment
                            : treatments) { %>

                        <tr>

                            <td>
                                <%= treatment.getTreatmentId() %>
                            </td>

                            <td>

                                <strong>
                                    <%= treatment.getTreatmentType() %>
                                </strong>

                            </td>

                            <td class="treatment-price">

                                Rs.
                                <%= String.format(
                                        "%,.2f",
                                        treatment.getTreatmentPrice()
                                ) %>

                            </td>

                            <td>

                                <div class="treatment-actions">

                                    <a
                                        href="treatment?treatmentId=<%= treatment.getTreatmentId() %>"
                                        class="treatment-action-link">

                                        View

                                    </a>

                                    <% if (isAdmin) { %>

                                        <a
                                            href="treatment?action=edit&treatmentId=<%= treatment.getTreatmentId() %>"
                                            class="treatment-action-link">

                                            Edit

                                        </a>

                                    <% } %>

                                </div>

                            </td>

                        </tr>

                    <% } %>

                    </tbody>

                </table>

            </div>

        <% } else { %>

            <div class="treatment-empty-state">

                <h3>
                    No Treatments Found
                </h3>

                <p>
                    There are currently no
                    treatment records available.
                </p>

                <% if (isAdmin) { %>

                    <a href="treatment?action=add"
                       class="primary-link">

                        Add First Treatment

                    </a>

                <% } %>

            </div>

        <% } %>

    </div>

</div>

</body>

</html>