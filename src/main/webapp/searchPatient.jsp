<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
    if (session.getAttribute("loggedInUser") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String errorMessage =
            (String) request.getAttribute("errorMessage");

    String searchType =
            request.getParameter("searchType") != null
            ? request.getParameter("searchType")
            : "id";

    String searchValue =
            request.getParameter("searchValue") != null
            ? request.getParameter("searchValue")
            : "";
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

    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/management.css">

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
                Search for an existing patient using
                their patient ID or phone number.
            </p>

        </div>

        <a href="dashboard.jsp"
           class="back-button">

            Dashboard

        </a>

    </div>


    <div class="management-content">

        <div class="action-panel">

            <div class="panel-heading">

                <div class="panel-number">
                    01
                </div>

                <div>

                    <h2>
                        Search Patient
                    </h2>

                    <p>
                        Choose a search method and enter
                        the patient's details.
                    </p>

                </div>

            </div>


            <% if (errorMessage != null) { %>

                <div class="form-message error">
                    <%= errorMessage %>
                </div>

            <% } %>


            <form action="patient"
                  method="get"
                  class="management-form">

                <div class="form-group">

                    <label for="searchType">
                        Search By
                    </label>

                    <select id="searchType"
                            name="searchType"
                            required>

                        <option value="id"
                            <%= "id".equals(searchType)
                                ? "selected" : "" %>>

                            Patient ID

                        </option>

                        <option value="phone"
                            <%= "phone".equals(searchType)
                                ? "selected" : "" %>>

                            Phone Number

                        </option>

                    </select>

                </div>


                <div class="form-group">

                    <label for="searchValue">
                        Search Value
                    </label>

                    <input
                        type="text"
                        id="searchValue"
                        name="searchValue"
                        maxlength="10"
                        pattern="[0-9]{1,10}"
                        inputmode="numeric"
                        placeholder="Enter patient ID or phone number"
                        title="Enter numbers only, up to 10 digits"
                        required>

                </div>


                <button type="submit"
                        class="primary-button">

                    Search Patient

                </button>

            </form>

        </div>


        <div class="secondary-panel">

            <div class="panel-number">
                02
            </div>

            <h2>
                New Patient?
            </h2>

            <p>
                Register a new patient and save their
                contact information in the system.
            </p>

            <a href="registerPatient.jsp"
               class="secondary-button">

                Register Patient

            </a>

        </div>

    </div>

</div>


<script src="js/patient-search.js"></script>

</body>

</html>