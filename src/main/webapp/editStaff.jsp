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

    String errorMessage =
            (String) request.getAttribute(
                    "errorMessage"
            );

    if (staff == null) {
        response.sendRedirect("staff");
        return;
    }

    String staffName =
            request.getParameter("staffName") != null
            ? request.getParameter("staffName")
            : staff.getStaffName();

    String contactNumber =
            request.getParameter("contactNumber") != null
            ? request.getParameter("contactNumber")
            : staff.getContactNumber();

    String username =
            request.getParameter("username") != null
            ? request.getParameter("username")
            : staff.getUsername();
%>

<!DOCTYPE html>

<html>

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        Edit Staff - Sunrise Dental Clinic
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
                Edit Staff
            </h1>

            <p>
                Update staff profile and account
                information.
            </p>

        </div>

        <a
            href="staff?staffId=<%= staff.getStaffId() %>"
            class="back-button">

            Staff Details

        </a>

    </div>


    <div class="staff-form-layout">

        <div class="staff-form-panel">

            <div class="staff-panel-heading">

                <p class="page-label">
                    EDIT PROFILE
                </p>

                <h2>
                    <%= staff.getStaffName() %>
                </h2>

                <p>
                    Update the staff member's current
                    information.
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
                       value="update">

                <input type="hidden"
                       name="staffId"
                       value="<%= staff.getStaffId() %>">


                <div class="staff-form-grid">

                    <div class="form-group">

                        <label for="staffName">
                            Staff Name
                        </label>

                        <input
                            type="text"
                            id="staffName"
                            name="staffName"
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
                          value="<%= contactNumber %>"
                          maxlength="10"
                          inputmode="numeric"
                          pattern="[0-9]{1,10}"
                          required>

                    </div>


                    <div class="form-group staff-full-field">

                        <label for="username">
                            Username
                        </label>

                        <input
                            type="text"
                            id="username"
                            name="username"
                            value="<%= username %>"
                            required>

                    </div>

                </div>


                <div class="staff-form-actions">

                    <a
                        href="staff?staffId=<%= staff.getStaffId() %>"
                        class="staff-cancel-button">

                        Cancel

                    </a>

                    <button type="submit"
                            class="primary-button">

                        Save Changes

                    </button>

                </div>

            </form>

        </div>


        <div class="staff-account-info">

            <p class="page-label">
                ACCOUNT INFORMATION
            </p>

            <h2>
                Staff Account
            </h2>

            <div class="staff-account-row">

                <span>
                    Staff ID
                </span>

                <strong>
                    #<%= staff.getStaffId() %>
                </strong>

            </div>

            <div class="staff-account-row">

                <span>
                    User ID
                </span>

                <strong>
                    #<%= staff.getUserId() %>
                </strong>

            </div>

            <div class="staff-account-row">

                <span>
                    Role
                </span>

                <strong>
                    STAFF
                </strong>

            </div>

            <p class="staff-info-note">
                Staff and User IDs are system generated
                and cannot be changed.
            </p>

        </div>

    </div>

</div>

</body>

</html>