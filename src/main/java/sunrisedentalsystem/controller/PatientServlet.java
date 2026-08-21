package sunrisedentalsystem.controller;

import java.io.IOException;
import java.sql.SQLException;

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
public class PatientServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private PatientService patientService;

    public PatientServlet() {
    }

    PatientServlet(PatientService patientService) {
        this.patientService = patientService;
    }

    @Override
    public void init() throws ServletException {

        if (patientService == null) {

            patientService =
                    new PatientServiceImpl(
                            new PatientDAOImpl()
                    );
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String patientName =
                request.getParameter("patientName");

        String address =
                request.getParameter("address");

        String contactNumber =
                request.getParameter("contactNumber");

        if (isEmpty(patientName)
                || isEmpty(address)
                || isEmpty(contactNumber)) {

            request.setAttribute(
                    "errorMessage",
                    "All patient fields are required."
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

        HttpSession session =
                request.getSession(false);

        if (session == null
                || session.getAttribute(
                        "loggedInUser") == null) {

            response.sendRedirect("login.jsp");

            return;
        }

        Patient patient =
                new Patient(
                        0,
                        patientName,
                        address,
                        contactNumber
                );

        try {

            Patient registeredPatient =
                    patientService
                            .registerPatient(patient);

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
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String patientIdText =
                request.getParameter("patientId");

        if (isEmpty(patientIdText)) {

            request.setAttribute(
                    "errorMessage",
                    "Patient ID is required."
            );

            request.getRequestDispatcher(
                    "searchPatient.jsp"
            ).forward(
                    request,
                    response
            );

            return;
        }

        try {

            int patientId =
                    Integer.parseInt(patientIdText);

            if (patientId <= 0) {

                showInvalidPatientId(
                        request,
                        response
                );

                return;
            }

            Patient patient =
                    patientService
                            .searchPatient(patientId);

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

            showInvalidPatientId(
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

    private boolean isEmpty(String value) {

        return value == null
                || value.trim().isEmpty();
    }

    private void showInvalidPatientId(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute(
                "errorMessage",
                "Invalid patient ID."
        );

        request.getRequestDispatcher(
                "searchPatient.jsp"
        ).forward(
                request,
                response
        );
    }
}