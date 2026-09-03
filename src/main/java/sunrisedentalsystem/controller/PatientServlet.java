package sunrisedentalsystem.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sunrisedentalsystem.dao.PatientDAOImpl;
import sunrisedentalsystem.model.Patient;
import sunrisedentalsystem.service.PatientService;
import sunrisedentalsystem.service.PatientServiceImpl;

@WebServlet("/patient")
public class PatientServlet
        extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private PatientService patientService;

    public PatientServlet() {
    }

    PatientServlet(
            PatientService patientService) {

        this.patientService =
                patientService;
    }

    @Override
    public void init()
            throws ServletException {

        if (patientService == null) {

            patientService =
                    new PatientServiceImpl(
                            new PatientDAOImpl()
                    );
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException,
                   IOException {

        HttpSession session =
                request.getSession(false);

        if (session == null
                || session.getAttribute(
                        "loggedInUser") == null) {

            response.sendRedirect(
                    "login.jsp"
            );

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

        String patientName =
                request.getParameter(
                        "patientName"
                );

        String address =
                request.getParameter(
                        "address"
                );

        String contactNumber =
                request.getParameter(
                        "contactNumber"
                );

        String email =
                request.getParameter(
                        "email"
                );

        if (isEmpty(patientName)
                || isEmpty(address)
                || isEmpty(contactNumber)) {

            request.setAttribute(
                    "errorMessage",
                    "All required patient fields must be completed."
            );

            RequestDispatcher dispatcher =
                    request.getRequestDispatcher(
                            "registerPatient.jsp"
                    );

            dispatcher.forward(
                    request,
                    response
            );

            return;
        }

        if (!isEmpty(email)
                && !isValidEmail(email)) {

            request.setAttribute(
                    "errorMessage",
                    "Enter a valid email address."
            );

            request.getRequestDispatcher(
                    "registerPatient.jsp"
            ).forward(
                    request,
                    response
            );

            return;
        }

        String patientEmail =
                isEmpty(email)
                ? null
                : email.trim();

        Patient patient =
                new Patient(
                        0,
                        patientName.trim(),
                        address.trim(),
                        contactNumber.trim(),
                        patientEmail
                );

        try {

            Patient registeredPatient =
                    patientService
                            .registerPatient(
                                    patient
                            );

            if (registeredPatient != null) {

                request.setAttribute(
                        "patient",
                        registeredPatient
                );

                request.setAttribute(
                        "successMessage",
                        "Patient registered successfully."
                );

                request.getRequestDispatcher(
                        "patientDetails.jsp"
                ).forward(
                        request,
                        response
                );

            } else {

                request.setAttribute(
                        "errorMessage",
                        "Unable to register patient."
                );

                request.getRequestDispatcher(
                        "registerPatient.jsp"
                ).forward(
                        request,
                        response
                );
            }

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to register patient.",
                    e
            );
        }
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException,
                   IOException {

        HttpSession session =
                request.getSession(false);

        if (session == null
                || session.getAttribute(
                        "loggedInUser") == null) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/login.jsp"
            );

            return;
        }

        String patientIdText =
                request.getParameter(
                        "patientId"
                );

        if (!isEmpty(patientIdText)) {

            showPatientById(
                    patientIdText,
                    request,
                    response
            );

            return;
        }

        String searchType =
                request.getParameter(
                        "searchType"
                );

        String searchValue =
                request.getParameter(
                        "searchValue"
                );

        if (isEmpty(searchType)
                || isEmpty(searchValue)) {

            request.getRequestDispatcher(
                    "searchPatient.jsp"
            ).forward(
                    request,
                    response
            );

            return;
        }

        if ("name".equals(searchType)) {

            searchPatientByName(
                    request,
                    response,
                    searchValue
            );

            return;
        }

        if ("phone".equals(searchType)) {

            searchPatientByContactNumber(
                    request,
                    response,
                    searchValue
            );

            return;
        }

        request.setAttribute(
                "errorMessage",
                "Invalid patient search type."
        );

        request.getRequestDispatcher(
                "searchPatient.jsp"
        ).forward(
                request,
                response
        );
    }

    private void showPatientById(
            String patientIdText,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException,
                   IOException {

        try {

            int patientId =
                    Integer.parseInt(
                            patientIdText
                    );

            if (patientId <= 0) {

                request.setAttribute(
                        "errorMessage",
                        "Invalid patient reference."
                );

                request.getRequestDispatcher(
                        "searchPatient.jsp"
                ).forward(
                        request,
                        response
                );

                return;
            }

            Patient patient =
                    patientService
                            .searchPatient(
                                    patientId
                            );

            if (patient != null) {

                request.setAttribute(
                        "patient",
                        patient
                );

                request.getRequestDispatcher(
                        "patientDetails.jsp"
                ).forward(
                        request,
                        response
                );

            } else {

                request.setAttribute(
                        "errorMessage",
                        "Patient not found."
                );

                request.getRequestDispatcher(
                        "searchPatient.jsp"
                ).forward(
                        request,
                        response
                );
            }

        } catch (NumberFormatException e) {

            request.setAttribute(
                    "errorMessage",
                    "Invalid patient reference."
            );

            request.getRequestDispatcher(
                    "searchPatient.jsp"
            ).forward(
                    request,
                    response
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to search patient.",
                    e
            );
        }
    }

    private void searchPatientByName(
            HttpServletRequest request,
            HttpServletResponse response,
            String patientName)
            throws ServletException,
                   IOException {

        try {

            List<Patient> patientResults =
                    patientService
                            .searchPatientsByName(
                                    patientName.trim()
                            );

            request.setAttribute(
                    "patientResults",
                    patientResults
            );

            request.getRequestDispatcher(
                    "searchPatient.jsp"
            ).forward(
                    request,
                    response
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to search patient.",
                    e
            );
        }
    }

    private void searchPatientByContactNumber(
            HttpServletRequest request,
            HttpServletResponse response,
            String contactNumber)
            throws ServletException,
                   IOException {

        try {

            Patient patient =
                    patientService
                            .searchPatientByContactNumber(
                                    contactNumber.trim()
                            );

            List<Patient> patientResults;

            if (patient != null) {

                patientResults =
                        List.of(
                                patient
                        );

            } else {

                patientResults =
                        List.of();
            }

            request.setAttribute(
                    "patientResults",
                    patientResults
            );

            request.getRequestDispatcher(
                    "searchPatient.jsp"
            ).forward(
                    request,
                    response
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to search patient.",
                    e
            );
        }
    }

    private boolean isValidEmail(
            String email) {

        return email.trim().matches(
                "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"
        );
    }

    private boolean isEmpty(
            String value) {

        return value == null
                || value.trim().isEmpty();
    }
}