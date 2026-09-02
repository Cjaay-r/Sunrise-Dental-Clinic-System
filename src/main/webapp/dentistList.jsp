<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.List" %>
<%@ page import="sunrisedentalsystem.model.Dentist" %>

<%
    if (session.getAttribute("loggedInUser") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String role =
            (String) session.getAttribute("role");

    boolean isAdmin =
            "ADMIN".equals(role);

    List<Dentist> dentists =
            (List<Dentist>) request.getAttribute("dentists");

    String errorMessage =
            (String) request.getAttribute("errorMessage");
%>

<!DOCTYPE html>

<html>

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        Dentist Management - Sunrise Dental Clinic
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
                Manage Dentists
            </h1>

            <p>
                View registered dentists and search
                for a dentist using their ID.
            </p>

        </div>

        <a href="dashboard.jsp"
           class="back-button">

            Dashboard

        </a>

    </div>


    <div class="management-content dentist-top-section">

        <div class="action-panel">

            <div class="panel-heading">

                <div class="panel-number">
                    01
                </div>

                <div>

                    <h2>
                        Search Dentist
                    </h2>

                    <p>
                        Enter a dentist ID to view
                        their information.
                    </p>

                </div>

            </div>


            <% if (errorMessage != null) { %>

                <div class="form-message error">
                    <%= errorMessage %>
                </div>

            <% } %>


            <form action="dentist"
                  method="get"
                  class="management-form">

                <div class="form-group">

                    <label for="dentistId">
                        Dentist ID
                    </label>

                    <input
                        type="number"
                        id="dentistId"
                        name="dentistId"
                        min="1"
                        placeholder="Enter dentist ID"
                        required>

                </div>

                <button type="submit"
                        class="primary-button">

                    Search Dentist

                </button>

            </form>

        </div>


        <div class="secondary-panel">

            <div class="panel-number">
                02
            </div>

            <% if (isAdmin) { %>

                <h2>
                    Add New Dentist
                </h2>

                <p>
                    Register a dentist with their
                    specialization and contact details.
                </p>

                <a href="dentist?action=add"
                   class="secondary-button">

                    Add Dentist

                </a>

            <% } else { %>

                <h2>
                    Dentist Records
                </h2>

                <p>
                    View dentist information and
                    availability details in the system.
                </p>

            <% } %>

        </div>

    </div>


    <div class="list-panel">

        <div class="list-panel-header">

            <div>

                <p class="page-label">
                    REGISTERED DENTISTS
                </p>

                <h2>
                    Dentist Directory
                </h2>

            </div>

            <span class="record-count">

                <%= dentists != null
                        ? dentists.size()
                        : 0 %>
                Records

            </span>

        </div>


        <% if (dentists != null
                && !dentists.isEmpty()) { %>

            <div class="management-table-wrapper">

                <table class="management-table">

                    <thead>

                        <tr>

                            <th>
                                ID
                            </th>

                            <th>
                                Dentist Name
                            </th>

                            <th>
                                Specialization
                            </th>

                            <th>
                                Contact Number
                            </th>

                            <th>
                                Action
                            </th>

                        </tr>

                    </thead>


                    <tbody>

                    <% for (Dentist dentist : dentists) { %>

                        <tr>

                            <td>
                                <%= dentist.getDentistId() %>
                            </td>

                            <td>
                                <strong>
                                    <%= dentist.getDentistName() %>
                                </strong>
                            </td>

                            <td>
                                <%= dentist.getSpecialization() %>
                            </td>

                            <td>
                                <%= dentist.getContactNumber() %>
                            </td>

                            <td>

                                <div class="table-actions">

                                    <a
                                        href="dentist?dentistId=<%= dentist.getDentistId() %>"
                                        class="table-link">

                                        View

                                    </a>


                                    <% if (isAdmin) { %>

                                        <a
                                            href="dentist?action=edit&dentistId=<%= dentist.getDentistId() %>"
                                            class="table-link">

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

            <div class="empty-state">

                <h3>
                    No Dentists Found
                </h3>

                <p>
                    There are currently no dentist
                    records available.
                </p>

                <% if (isAdmin) { %>

                    <a href="dentist?action=add"
                       class="primary-link">

                        Add First Dentist

                    </a>

                <% } %>

            </div>

        <% } %>

    </div>

</div>

</body>

</html>