<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.time.LocalDate" %>
<%@ page import="java.util.List" %>
<%@ page import="sunrisedentalsystem.model.Patient" %>
<%@ page import="sunrisedentalsystem.model.Dentist" %>
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

    if (!"STAFF".equals(role)) {

        response.sendError(
                HttpServletResponse.SC_FORBIDDEN,
                "Staff access required."
        );

        return;
    }

    String errorMessage =
            (String) request.getAttribute(
                    "errorMessage"
            );

    Patient selectedPatient =
            (Patient) request.getAttribute(
                    "selectedPatient"
            );

    List<Patient> patientResults =
            (List<Patient>) request.getAttribute(
                    "patientResults"
            );

    List<Dentist> dentists =
            (List<Dentist>) request.getAttribute(
                    "dentists"
            );

    List<Treatment> treatments =
            (List<Treatment>) request.getAttribute(
                    "treatments"
            );

    String patientSearch =
            (String) request.getAttribute(
                    "patientSearch"
            );

    if (patientSearch == null) {
        patientSearch = "";
    }

    String selectedDentistId =
            request.getParameter("dentistId") != null
            ? request.getParameter("dentistId")
            : "";

    String selectedTreatmentId =
            request.getParameter("treatmentId") != null
            ? request.getParameter("treatmentId")
            : "";

    String appointmentDate =
            request.getParameter("appointmentDate") != null
            ? request.getParameter("appointmentDate")
            : "";

    String appointmentTime =
            request.getParameter("appointmentTime") != null
            ? request.getParameter("appointmentTime")
            : "";
%>

<!DOCTYPE html>

<html>

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        Register Appointment - Sunrise Dental Clinic
    </title>

    <link rel="stylesheet"
          href="css/style.css">

    <link rel="stylesheet"
          href="css/management.css">

    <link rel="stylesheet"
          href="css/appointment.css">

</head>

<body>

<div class="management-page">

    <div class="management-header">

        <div>

            <p class="page-label">
                APPOINTMENT MANAGEMENT
            </p>

            <h1>
                Register Appointment
            </h1>

            <p>
                Find the patient and create
                a new dental appointment.
            </p>

        </div>

        <a href="appointment"
           class="back-button">

            Appointment Search

        </a>

    </div>


    <div class="appointment-form-panel">

        <% if (errorMessage != null) { %>

            <div class="form-message error">
                <%= errorMessage %>
            </div>

        <% } %>


        <div class="appointment-section">

            <h2>
                Select Patient
            </h2>

            <p>
                Search using the patient's name
                or contact number.
            </p>


            <form action="appointment"
                  method="get"
                  class="management-form">

                <input type="hidden"
                       name="action"
                       value="register">

                <div class="form-group">

                    <label for="patientSearch">
                        Patient Name or Contact Number
                    </label>

                    <input
                        type="text"
                        id="patientSearch"
                        name="patientSearch"
                        placeholder="Enter patient name or contact number"
                        value="<%= patientSearch %>"
                        required>

                </div>

                <button type="submit"
                        class="primary-button">

                    Search Patient

                </button>

            </form>

        </div>


        <% if (!patientSearch.trim().isEmpty()) { %>

            <div class="appointment-section">

                <h2>
                    Search Results
                </h2>


                <% if (patientResults != null
                        && !patientResults.isEmpty()) { %>

                    <div class="appointment-details-grid">

                        <% for (Patient patient
                                : patientResults) { %>

                            <div class="appointment-detail-item">

                                <span>
                                    Patient
                                </span>

                                <strong>
                                    <%= patient.getPatientName() %>
                                </strong>

                                <small>
                                    <%= patient.getContactNumber() %>
                                </small>

                                <small>
                                    <%= patient.getAddress() %>
                                </small>

                                <br>

                                <a
                                    href="appointment?action=register&patientId=<%= patient.getPatientId() %>"
                                    class="appointment-table-link">

                                    Select

                                </a>

                            </div>

                        <% } %>

                    </div>

                    <p>
                        Showing up to 20 matching patients.
                    </p>

                <% } else { %>

                    <div class="appointment-number-note">

                        <strong>
                            No Patients Found
                        </strong>

                        <span>
                            Try another name or contact number.
                        </span>

                    </div>

                <% } %>

            </div>

        <% } %>


        <% if (selectedPatient != null) { %>

            <div class="appointment-section">

                <h2>
                    Selected Patient
                </h2>

                <div class="appointment-details-grid">

                    <div class="appointment-detail-item">

                        <span>
                            Patient Name
                        </span>

                        <strong>
                            <%= selectedPatient.getPatientName() %>
                        </strong>

                    </div>


                    <div class="appointment-detail-item">

                        <span>
                            Contact Number
                        </span>

                        <strong>
                            <%= selectedPatient.getContactNumber() %>
                        </strong>

                    </div>


                    <div class="appointment-detail-item">

                        <span>
                            Address
                        </span>

                        <strong>
                            <%= selectedPatient.getAddress() %>
                        </strong>

                    </div>


                    <div class="appointment-detail-item">

                        <span>
                            Patient ID
                        </span>

                        <strong>
                            #<%= selectedPatient.getPatientId() %>
                        </strong>

                    </div>

                </div>

            </div>

        <% } %>


        <div class="appointment-section">

            <h2>
                Appointment Details
            </h2>


            <form action="appointment"
                  method="post"
                  class="management-form">

                <input type="hidden"
                       name="action"
                       value="register">

                <input type="hidden"
                       name="patientId"
                       value="<%= selectedPatient != null
                               ? selectedPatient.getPatientId()
                               : "" %>">


                <div class="appointment-form-grid">

                    <div class="form-group">

                        <label for="dentistId">
                            Dentist
                        </label>

                        <select
                            id="dentistId"
                            name="dentistId"
                            required>

                            <option value="">
                                Select dentist
                            </option>

                            <% if (dentists != null) {

                                for (Dentist dentist
                                        : dentists) {

                                    String dentistValue =
                                            String.valueOf(
                                                    dentist.getDentistId()
                                            );

                                    boolean selected =
                                            dentistValue.equals(
                                                    selectedDentistId
                                            );
                            %>

                                <option
                                    value="<%= dentist.getDentistId() %>"
                                    <%= selected
                                            ? "selected"
                                            : "" %>>

                                    <%= dentist.getDentistName() %>

                                    <% if (dentist.getSpecialization() != null
                                            && !dentist
                                                    .getSpecialization()
                                                    .trim()
                                                    .isEmpty()) { %>

                                        -
                                        <%= dentist.getSpecialization() %>

                                    <% } %>

                                </option>

                            <%
                                }
                            }
                            %>

                        </select>

                    </div>


                    <div class="form-group">

                        <label for="treatmentId">
                            Treatment
                        </label>

                        <select
                            id="treatmentId"
                            name="treatmentId"
                            required>

                            <option value="">
                                Select treatment
                            </option>

                            <% if (treatments != null) {

                                for (Treatment treatment
                                        : treatments) {

                                    String treatmentValue =
                                            String.valueOf(
                                                    treatment.getTreatmentId()
                                            );

                                    boolean selected =
                                            treatmentValue.equals(
                                                    selectedTreatmentId
                                            );
                            %>

                                <option
                                    value="<%= treatment.getTreatmentId() %>"
                                    <%= selected
                                            ? "selected"
                                            : "" %>>

                                    <%= treatment.getTreatmentType() %>
                                    -
                                    Rs.
                                    <%= String.format(
                                            "%,.2f",
                                            treatment.getTreatmentPrice()
                                    ) %>

                                </option>

                            <%
                                }
                            }
                            %>

                        </select>

                    </div>


                    <div class="form-group">

                        <label for="appointmentDate">
                            Appointment Date
                        </label>

                        <input
                            type="date"
                            id="appointmentDate"
                            name="appointmentDate"
                            min="<%= LocalDate.now() %>"
                            value="<%= appointmentDate %>"
                            required>

                    </div>


                    <div class="form-group">

                        <label for="appointmentTime">
                            Appointment Time
                        </label>

                        <input
                            type="time"
                            id="appointmentTime"
                            name="appointmentTime"
                            value="<%= appointmentTime %>"
                            required>

                    </div>

                </div>


                <div class="appointment-number-note">

                    <% if (selectedPatient != null) { %>

                        <strong>
                            Patient Selected
                        </strong>

                        <span>
                            <%= selectedPatient.getPatientName() %>
                            -
                            <%= selectedPatient.getContactNumber() %>
                        </span>

                    <% } else { %>

                        <strong>
                            Patient Required
                        </strong>

                        <span>
                            Search and select a patient
                            before registering the appointment.
                        </span>

                    <% } %>

                </div>


                <div class="appointment-number-note">

                    <strong>
                        Appointment Number
                    </strong>

                    <span>
                        Generated automatically after
                        successful registration.
                    </span>

                </div>


                <button type="submit"
                        class="primary-button"
                        <%= selectedPatient == null
                                ? "disabled"
                                : "" %>>

                    Register Appointment

                </button>


                <a href="appointment"
                   class="appointment-cancel-link">

                    Cancel

                </a>

            </form>

        </div>

    </div>

</div>

</body>

</html>