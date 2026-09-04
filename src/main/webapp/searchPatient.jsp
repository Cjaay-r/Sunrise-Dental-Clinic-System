<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.List" %>
<%@ page import="sunrisedentalsystem.model.Patient" %>

<%
    if (session.getAttribute("loggedInUser") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String role =
            (String) session.getAttribute(
                    "role"
            );

    boolean isStaff =
            "STAFF".equals(role);

    String errorMessage =
            (String) request.getAttribute(
                    "errorMessage"
            );

    List<Patient> patientResults =
            (List<Patient>) request.getAttribute(
                    "patientResults"
            );

    String searchType =
            request.getParameter("searchType") != null
            ? request.getParameter("searchType")
            : "";

    String searchValue =
            request.getParameter("searchValue") != null
            ? request.getParameter("searchValue")
            : "";

    String nameValue =
            "name".equals(searchType)
            ? searchValue
            : "";

    String phoneValue =
            "phone".equals(searchType)
            ? searchValue
            : "";

    boolean searchPerformed =
            ("name".equals(searchType)
            || "phone".equals(searchType))
            && !searchValue.trim().isEmpty();
%>

<!DOCTYPE html>

<html>

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        Patient Management - Sunrise Dental Clinic
    </title>

    <link rel="stylesheet"
          href="css/style.css">

    <link rel="stylesheet"
          href="css/management.css">

    <link rel="stylesheet"
          href="css/patient.css">

</head>

<body>

<div class="management-page">

    <div class="management-header">

        <div>

            <p class="page-label">
                PATIENT MANAGEMENT
            </p>

            <h1>
                Find a Patient
            </h1>

            <p>
                Search for an existing patient
                using their name or phone number.
            </p>

        </div>

        <a href="dashboard.jsp"
           class="back-button">

            Dashboard

        </a>

    </div>


    <% if (errorMessage != null) { %>

        <div class="form-message error patient-page-message">

            <%= errorMessage %>

        </div>

    <% } %>


    <div class="patient-search-layout">

        <div class="patient-search-panel">

            <h2>
                Search Patient
            </h2>

            <p>
                Find a patient by name or
                registered phone number.
            </p>


            <div class="patient-search-methods">

                <form action="patient"
                      method="get"
                      class="management-form">

                    <input type="hidden"
                           name="searchType"
                           value="name">

                    <div class="form-group">

                        <label for="patientNameSearch">
                            Patient Name
                        </label>

                        <input
                            type="text"
                            id="patientNameSearch"
                            name="searchValue"
                            placeholder="Enter full or partial name"
                            value="<%= nameValue %>"
                            required>

                    </div>

                    <button type="submit"
                            class="primary-button">

                        Search by Name

                    </button>

                </form>


                <form action="patient"
                      method="get"
                      class="management-form">

                    <input type="hidden"
                           name="searchType"
                           value="phone">

                    <div class="form-group">

                        <label for="patientPhoneSearch">
                            Phone Number
                        </label>

                        <input
                            type="text"
                            id="patientPhoneSearch"
                            name="searchValue"
                            maxlength="10"
                            pattern="[0-9]{1,10}"
                            inputmode="numeric"
                            placeholder="Enter phone number"
                            value="<%= phoneValue %>"
                            required>

                    </div>

                    <button type="submit"
                            class="primary-button">

                        Search by Phone

                    </button>

                </form>

            </div>

        </div>


        <div class="patient-register-panel">

            <% if (isStaff) { %>

                <h2>
                    New Patient?
                </h2>

                <p>
                    Register a new patient and
                    save their information in the system.
                </p>

                <a href="registerPatient.jsp"
                   class="secondary-button">

                    Register Patient

                </a>

            <% } else { %>

                <h2>
                    Patient Records
                </h2>

                <p>
                    Administrators can search and
                    review registered patient information.
                </p>

            <% } %>

        </div>

    </div>


    <% if (searchPerformed) { %>

        <div class="patient-results-panel">

            <div class="patient-results-header">

                <div>

                    <p class="page-label">
                        SEARCH RESULTS
                    </p>

                    <h2>
                        Matching Patients
                    </h2>

                </div>

                <span class="patient-result-count">

                    <%= patientResults != null
                            ? patientResults.size()
                            : 0 %>

                    <%= patientResults != null
                            && patientResults.size() == 1
                            ? "Result"
                            : "Results" %>

                </span>

            </div>


            <% if (patientResults != null
                    && !patientResults.isEmpty()) { %>

                <div class="patient-table-wrapper">

                    <table class="patient-table">

                        <thead>

                            <tr>

                                <th>
                                    Patient Name
                                </th>

                                <th>
                                    Phone Number
                                </th>

                                <th>
                                    Address
                                </th>

                                <th>
                                    Action
                                </th>

                            </tr>

                        </thead>

                        <tbody>

                        <% for (Patient patient
                                : patientResults) { %>

                            <tr>

                                <td>

                                    <strong>
                                        <%= patient.getPatientName() %>
                                    </strong>

                                </td>

                                <td>
                                    <%= patient.getContactNumber() %>
                                </td>

                                <td>
                                    <%= patient.getAddress() %>
                                </td>

                                <td>

                                    <a
                                        href="patient?patientId=<%= patient.getPatientId() %>"
                                        class="patient-view-link">

                                        View

                                    </a>

                                </td>

                            </tr>

                        <% } %>

                        </tbody>

                    </table>

                </div>

            <% } else { %>

                <div class="patient-empty-state">

                    <h3>
                        No Patients Found
                    </h3>

                    <p>
                        No patients matched the
                        information you entered.
                    </p>

                </div>

            <% } %>

        </div>

    <% } %>

</div>

</body>

</html>