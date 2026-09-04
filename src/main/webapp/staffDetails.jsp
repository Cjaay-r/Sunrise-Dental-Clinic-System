<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="sunrisedentalsystem.model.Staff" %>

<%
    if (session.getAttribute("loggedInUser") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String role =
            (String) session.getAttribute("role");

    if (!"ADMIN".equals(role)) {
        response.sendError(
                HttpServletResponse.SC_FORBIDDEN,
                "Admin access required."
        );
        return;
    }

    Staff staff =
            (Staff) request.getAttribute(
                    "staff"
            );

    String successMessage =
            (String) request.getAttribute(
                    "successMessage"
            );

    String errorMessage =
            (String) request.getAttribute(
                    "errorMessage"
            );

    if (staff == null) {
        response.sendRedirect("staff");
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
        Staff Details - Sunrise Dental Clinic
    </title>

    <link rel="stylesheet"
          href="css/style.css">

    <link rel="stylesheet"
          href="css/management.css">

    <link rel="stylesheet"
          href="css/staff.css">

</head>

<body>

<div class="management-page">

    <div class="management-header">

        <div>

            <p class="page-label">
                STAFF MANAGEMENT
            </p>

            <h1>
                Staff Details
            </h1>

            <p>
                View staff profile and account
                information.
            </p>

        </div>

        <a href="staff"
           class="back-button">

            Staff Management

        </a>

    </div>


    <% if (successMessage != null) { %>

        <div class="form-message success staff-page-message">
            <%= successMessage %>
        </div>

    <% } %>


    <% if (errorMessage != null) { %>

        <div class="form-message error staff-page-message">
            <%= errorMessage %>
        </div>

    <% } %>


    <div class="staff-details-card">

        <div class="staff-details-header">

            <div>

                <p class="page-label">
                    STAFF PROFILE
                </p>

                <h2>
                    <%= staff.getStaffName() %>
                </h2>

                <span class="staff-role-badge">
                    STAFF
                </span>

            </div>


            <div class="staff-id-display">

                <span>
                    Staff ID
                </span>

                <strong>
                    #<%= staff.getStaffId() %>
                </strong>

            </div>

        </div>


        <div class="staff-details-section">

            <h3>
                Personal Information
            </h3>

            <div class="staff-details-grid">

                <div class="staff-detail-item">

                    <span>
                        Staff Name
                    </span>

                    <strong>
                        <%= staff.getStaffName() %>
                    </strong>

                </div>


                <div class="staff-detail-item">

                    <span>
                        Contact Number
                    </span>

                    <strong>
                        <%= staff.getContactNumber() %>
                    </strong>

                </div>

            </div>

        </div>


        <div class="staff-details-section">

            <h3>
                Account Information
            </h3>

            <div class="staff-details-grid">

                <div class="staff-detail-item">

                    <span>
                        Username
                    </span>

                    <strong>
                        <%= staff.getUsername() %>
                    </strong>

                </div>


                <div class="staff-detail-item">

                    <span>
                        User ID
                    </span>

                    <strong>
                        #<%= staff.getUserId() %>
                    </strong>

                </div>


                <div class="staff-detail-item">

                    <span>
                        Account Role
                    </span>

                    <strong>
                        STAFF
                    </strong>

                </div>

            </div>

        </div>


        <div class="staff-details-actions">

            <a href="staff"
               class="staff-cancel-button">

                Back to Staff

            </a>

            <a
                href="staff?action=edit&staffId=<%= staff.getStaffId() %>"
                class="primary-link">

                Edit Staff

            </a>

        </div>

    </div>

</div>

</body>

</html>