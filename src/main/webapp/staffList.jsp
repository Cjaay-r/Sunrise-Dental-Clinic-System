<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.List" %>
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

    List<Staff> staffList =
            (List<Staff>) request.getAttribute(
                    "staffList"
            );

    String errorMessage =
            (String) request.getAttribute(
                    "errorMessage"
            );

    String searchName =
            (String) request.getAttribute(
                    "searchName"
            );

    String staffIdValue =
            request.getParameter("staffId") != null
            ? request.getParameter("staffId")
            : "";

    String staffNameValue =
            searchName != null
            ? searchName
            : "";

    int staffCount =
            staffList != null
            ? staffList.size()
            : 0;
%>

<!DOCTYPE html>

<html>

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        Staff Management - Sunrise Dental Clinic
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
                Manage Staff
            </h1>

            <p>
                Create staff accounts, search staff members
                and maintain staff information.
            </p>

        </div>

        <a href="dashboard.jsp"
           class="back-button">

            Dashboard

        </a>

    </div>


    <% if (errorMessage != null) { %>

        <div class="form-message error staff-page-message">
            <%= errorMessage %>
        </div>

    <% } %>


    <div class="staff-top-grid">

        <div class="staff-search-panel">

            <div class="staff-panel-heading">

                <div>

                    <p class="page-label">
                        SEARCH STAFF
                    </p>

                    <h2>
                        Find Staff Member
                    </h2>

                    <p>
                        Search using either Staff ID
                        or Staff Name.
                    </p>

                </div>

            </div>


            <div class="staff-search-grid">

                <form action="staff"
                      method="get"
                      class="management-form">

                    <div class="form-group">

                        <label for="staffId">
                            Search by Staff ID
                        </label>

                        <input
                            type="number"
                            id="staffId"
                            name="staffId"
                            min="1"
                            placeholder="Enter staff ID"
                            value="<%= staffIdValue %>"
                            required>

                    </div>

                    <button type="submit"
                            class="primary-button">

                        Search by ID

                    </button>

                </form>


                <form action="staff"
                      method="get"
                      class="management-form">

                    <div class="form-group">

                        <label for="staffName">
                            Search by Staff Name
                        </label>

                        <input
                            type="text"
                            id="staffName"
                            name="staffName"
                            placeholder="Enter full or partial name"
                            value="<%= staffNameValue %>"
                            required>

                    </div>

                    <button type="submit"
                            class="primary-button">

                        Search by Name

                    </button>

                </form>

            </div>

        </div>


        <div class="staff-add-panel">

            <p class="page-label">
                STAFF ACCOUNT
            </p>

            <h2>
                Add Staff Member
            </h2>

            <p>
                Create a staff profile and login
                account for a new employee.
            </p>

            <a href="staff?action=add"
               class="secondary-button">

                Add Staff

            </a>

        </div>

    </div>


    <div class="staff-directory-panel">

        <div class="staff-directory-header">

            <div>

                <p class="page-label">
                    STAFF DIRECTORY
                </p>

                <h2>
                    Registered Staff
                </h2>

                <% if (searchName != null) { %>

                    <p class="staff-search-result-text">
                        Results for
                        "<strong><%= searchName %></strong>"
                    </p>

                <% } %>

            </div>


            <div class="staff-directory-actions">

                <% if (searchName != null) { %>

                    <a href="staff"
                       class="staff-clear-search">

                        Show All Staff

                    </a>

                <% } %>

                <span class="staff-record-count">

                    <%= staffCount %>

                    <%= staffCount == 1
                            ? "Staff Member"
                            : "Staff Members" %>

                </span>

            </div>

        </div>


        <% if (staffList != null
                && !staffList.isEmpty()) { %>

            <div class="staff-table-wrapper">

                <table class="staff-table">

                    <thead>

                        <tr>

                            <th>
                                Staff ID
                            </th>

                            <th>
                                Staff Name
                            </th>

                            <th>
                                Username
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

                    <% for (Staff staff : staffList) { %>

                        <tr>

                            <td>

                                <strong>
                                    #<%= staff.getStaffId() %>
                                </strong>

                            </td>

                            <td>
                                <%= staff.getStaffName() %>
                            </td>

                            <td>

                                <span class="staff-username">
                                    <%= staff.getUsername() %>
                                </span>

                            </td>

                            <td>
                                <%= staff.getContactNumber() %>
                            </td>

                            <td>

                                <a
                                    href="staff?staffId=<%= staff.getStaffId() %>"
                                    class="staff-table-link">

                                    View

                                </a>

                            </td>

                        </tr>

                    <% } %>

                    </tbody>

                </table>

            </div>

        <% } else { %>

            <div class="staff-empty-state">

                <% if (searchName != null) { %>

                    <h3>
                        No Matching Staff Found
                    </h3>

                    <p>
                        No staff members matched
                        the name you entered.
                    </p>

                    <a href="staff"
                       class="primary-link">

                        View All Staff

                    </a>

                <% } else { %>

                    <h3>
                        No Staff Members Found
                    </h3>

                    <p>
                        There are currently no staff
                        records available.
                    </p>

                    <a href="staff?action=add"
                       class="primary-link">

                        Add Staff Member

                    </a>

                <% } %>

            </div>

        <% } %>

    </div>

</div>

</body>

</html>