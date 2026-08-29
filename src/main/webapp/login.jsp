<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Sunrise Dental Clinic - Login</title>

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/css/style.css">

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/css/login.css">

</head>

<body>

    <div class="login-page">

        <div class="login-card">

            <div class="login-brand">

                <div class="brand-logo">
                    SDC
                </div>

                <h1>Sunrise Dental Clinic</h1>

                <p class="brand-subtitle">
                    Appointment & Patient Management System
                </p>

                <div class="brand-line"></div>

                <p class="brand-description">
                    Securely manage patients, appointments,
                    treatments and billing from one system.
                </p>

            </div>

            <div class="login-form-section">

                <div class="login-heading">

                    <p class="small-heading">
                        STAFF ACCESS
                    </p>

                    <h2>Welcome Back</h2>

                    <p>
                        Enter your account details to continue.
                    </p>

                </div>


                <%
                    String errorMessage =
                            (String) request.getAttribute("errorMessage");

                    if (errorMessage != null) {
                %>

                    <div class="error-message">
                        <%= errorMessage %>
                    </div>

                <%
                    }
                %>


                <form action="<%= request.getContextPath() %>/login"
                      method="post">

                    <div class="form-group">

                        <label for="username">
                            Username
                        </label>

                        <input
                            type="text"
                            id="username"
                            name="username"
                            placeholder="Enter your username"
                            autocomplete="username"
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
                            placeholder="Enter your password"
                            autocomplete="current-password"
                            required>

                    </div>


                    <button type="submit"
                            class="login-button">

                        Login

                    </button>

                </form>


                <p class="login-footer">
                    Authorized clinic staff only
                </p>

            </div>

        </div>

    </div>

</body>

</html>