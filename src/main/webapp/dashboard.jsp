<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="sunrisedentalsystem.model.User" %>

<%
    User loggedInUser =
            (User) session.getAttribute(
                    "loggedInUser"
            );

    if (loggedInUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String role =
            (String) session.getAttribute(
                    "role"
            );

    boolean isAdmin =
            "ADMIN".equals(role);

    boolean isStaff =
            "STAFF".equals(role);
%>

<!DOCTYPE html>

<html>

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        Sunrise Dental Clinic - Dashboard
    </title>

    <link rel="stylesheet"
          href="css/style.css">

    <link rel="stylesheet"
          href="css/dashboard.css">

</head>

<body>

<div class="dashboard-layout">

    <aside class="sidebar">

        <div class="sidebar-brand">

            <div class="sidebar-logo">
                SDC
            </div>

            <div>

                <h2>
                    Sunrise
                </h2>

                <p>
                    Dental Clinic
                </p>

            </div>

        </div>


        <nav class="sidebar-nav">

            <a
                href="<%= request.getContextPath() %>/dashboard.jsp"
                class="nav-item active">

                Dashboard

            </a>

            <a
                href="<%= request.getContextPath() %>/appointment"
                class="nav-item">

                Appointments

            </a>

            <a
                href="<%= request.getContextPath() %>/patient"
                class="nav-item">

                Patients

            </a>

            <a
                href="<%= request.getContextPath() %>/billing"
                class="nav-item">

                Billing

            </a>

            <a
                href="<%= request.getContextPath() %>/dentist"
                class="nav-item">

                Dentists

            </a>

            <a
                href="<%= request.getContextPath() %>/treatment"
                class="nav-item">

                Treatments

            </a>

            <% if (isAdmin) { %>

                <a
                    href="<%= request.getContextPath() %>/staff"
                    class="nav-item">

                    Staff Management

                </a>

            <% } %>

        </nav>


        <div class="sidebar-footer">

            <a
                href="<%= request.getContextPath() %>/logout"
                class="logout-link">

                Logout

            </a>

            <p>
                Appointment & Patient
            </p>

            <p>
                Management System
            </p>

        </div>

    </aside>


    <main class="main-content">

        <header class="top-header">

            <div>

                <p class="header-label">
                    CLINIC MANAGEMENT
                </p>

                <h1>

                    Welcome back,
                    <%= loggedInUser.getUsername() %>

                </h1>

            </div>


            <div class="user-profile">

                <div class="user-avatar">

                    <%= loggedInUser
                            .getUsername()
                            .substring(0, 1)
                            .toUpperCase() %>

                </div>

                <div>

                    <strong>
                        <%= loggedInUser.getUsername() %>
                    </strong>

                    <span class="role-badge">
                        <%= role %>
                    </span>

                </div>

            </div>

        </header>


        <section class="welcome-panel">

            <div>

                <p class="welcome-small">
                    SUNRISE DENTAL CLINIC
                </p>

                <h2>
                    Clinic Management Dashboard
                </h2>

                <p>
                    Access appointments, patient records,
                    treatments and billing from one place.
                </p>

            </div>

            <div class="welcome-mark">
                SDC
            </div>

        </section>


        <div class="section-heading">

            <div>

                <h2>
                    Quick Access
                </h2>

                <p>
                    Select a module to continue.
                </p>

            </div>

        </div>


        <section class="dashboard-grid">

            <a
                href="<%= request.getContextPath() %>/appointment"
                class="dashboard-card">

                <div class="card-number">
                    01
                </div>

                <div class="card-content">

                    <h3>
                        Appointments
                    </h3>

                    <p>

                        <% if (isStaff) { %>

                            Register, search and manage
                            patient appointments.

                        <% } else { %>

                            Search and review
                            appointment records.

                        <% } %>

                    </p>

                </div>

                <div class="card-arrow">
                    →
                </div>

            </a>


            <a
                href="<%= request.getContextPath() %>/patient"
                class="dashboard-card">

                <div class="card-number">
                    02
                </div>

                <div class="card-content">

                    <h3>
                        Patients
                    </h3>

                    <p>
                        Register patients and access
                        patient information.
                    </p>

                </div>

                <div class="card-arrow">
                    →
                </div>

            </a>


            <a
                href="<%= request.getContextPath() %>/billing"
                class="dashboard-card">

                <div class="card-number">
                    03
                </div>

                <div class="card-content">

                    <h3>
                        Billing
                    </h3>

                    <p>

                        <% if (isStaff) { %>

                            Generate bills and access
                            patient receipts.

                        <% } else { %>

                            Search and view generated
                            billing records.

                        <% } %>

                    </p>

                </div>

                <div class="card-arrow">
                    →
                </div>

            </a>


            <a
                href="<%= request.getContextPath() %>/dentist"
                class="dashboard-card">

                <div class="card-number">
                    04
                </div>

                <div class="card-content">

                    <h3>
                        Dentists
                    </h3>

                    <p>

                        <% if (isAdmin) { %>

                            View and manage dentist
                            information.

                        <% } else { %>

                            View registered dentist
                            information.

                        <% } %>

                    </p>

                </div>

                <div class="card-arrow">
                    →
                </div>

            </a>


            <a
                href="<%= request.getContextPath() %>/treatment"
                class="dashboard-card">

                <div class="card-number">
                    05
                </div>

                <div class="card-content">

                    <h3>
                        Treatments
                    </h3>

                    <p>

                        <% if (isAdmin) { %>

                            Manage available treatments
                            and treatment prices.

                        <% } else { %>

                            View available treatments
                            and treatment prices.

                        <% } %>

                    </p>

                </div>

                <div class="card-arrow">
                    →
                </div>

            </a>


            <% if (isAdmin) { %>

                <a
                    href="<%= request.getContextPath() %>/staff"
                    class="dashboard-card">

                    <div class="card-number">
                        06
                    </div>

                    <div class="card-content">

                        <h3>
                            Staff Management
                        </h3>

                        <p>
                            Create staff accounts and
                            maintain staff information.
                        </p>

                    </div>

                    <div class="card-arrow">
                        →
                    </div>

                </a>

            <% } %>

        </section>

    </main>

</div>

</body>

</html>