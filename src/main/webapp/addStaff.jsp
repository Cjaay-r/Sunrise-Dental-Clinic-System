<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

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

    String errorMessage =
            (String) request.getAttribute(
                    "errorMessage"
            );

    String staffName =
            request.getParameter("staffName") != null
            ? request.getParameter("staffName")
            : "";

    String contactNumber =
            request.getParameter("contactNumber") != null
            ? request.getParameter("contactNumber")
            : "";

    String username =
            request.getParameter("username") != null
            ? request.getParameter("username")
            : "";
%>

<!DOCTYPE html>

<html>

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        Add Staff - Sunrise Dental Clinic
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
                Add Staff Member
            </h1>

            <p>
                Create a staff profile and login
                account for a new employee.
            </p>

        </div>

        <a href="staff"
           class="back-button">

            Staff Management

        </a>

    </div>


    <div class="staff-form-layout staff-add-form-layout">

        <div class="staff-form-panel">

            <div class="staff-panel-heading">

                <p class="page-label">
                    STAFF DETAILS
                </p>

                <h2>
                    New Staff Account
                </h2>

                <p>
                    Complete all fields to create
                    the staff member.
                </p>

            </div>


            <% if (errorMessage != null) { %>

                <div class="form-message error">
                    <%= errorMessage %>
                </div>

            <% } %>


            <form action="staff"
                  method="post"
                  class="management-form">

                <input type="hidden"
                       name="action"
                       value="add">


                <div class="staff-form-grid">

                    <div class="form-group">

                        <label for="staffName">
                            Staff Name
                        </label>

                        <input
                            type="text"
                            id="staffName"
                            name="staffName"
                            placeholder="Enter staff member's name"
                            value="<%= staffName %>"
                            required>

                    </div>


                    <div class="form-group">

                        <label for="contactNumber">
                            Contact Number
                        </label>

                        <input
                           type="text"
                           id="contactNumber"
                           name="contactNumber"
                           placeholder="Enter contact number"
                           value="<%= contactNumber %>"
                           maxlength="10"
                           inputmode="numeric"
                           pattern="[0-9]{1,10}"
                           required>

                    </div>


                    <div class="form-group">

                        <label for="username">
                            Username
                        </label>

                        <input
                            type="text"
                            id="username"
                            name="username"
                            placeholder="Create login username"
                            value="<%= username %>"
                            required>

                    </div>


                    <div class="form-group">

                        <label for="password">
                            Password
                        </label>

                        <input
                            type="password"
                            id="password"
                            name="password"
                            placeholder="Create login password"
                            required>

                    </div>

                </div>


                <div class="staff-form-actions">

                    <a href="staff"
                       class="staff-cancel-button">

                        Cancel

                    </a>

                    <button type="submit"
                            class="primary-button">

                        Add Staff Member

                    </button>

                </div>

            </form>

        </div>

    </div>

</div>

</body>

</html>