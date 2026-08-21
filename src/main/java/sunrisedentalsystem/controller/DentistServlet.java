package sunrisedentalsystem.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sunrisedentalsystem.dao.DentistDAOImpl;
import sunrisedentalsystem.model.Dentist;
import sunrisedentalsystem.service.DentistService;
import sunrisedentalsystem.service.DentistServiceImpl;

@WebServlet("/dentist")
public class DentistServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private DentistService dentistService;

    public DentistServlet() {
    }

    DentistServlet(
            DentistService dentistService) {

        this.dentistService =
                dentistService;
    }

    @Override
    public void init()
            throws ServletException {

        if (dentistService == null) {

            dentistService =
                    new DentistServiceImpl(
                            new DentistDAOImpl()
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
                    "login.jsp"
            );

            return;
        }

        String dentistIdText =
                request.getParameter(
                        "dentistId"
                );

        try {

            if (isEmpty(dentistIdText)) {

                List<Dentist> dentists =
                        dentistService
                                .getAllDentists();

                request.setAttribute(
                        "dentists",
                        dentists
                );

                request.getRequestDispatcher(
                        "dentistList.jsp"
                ).forward(
                        request,
                        response
                );

                return;
            }

            int dentistId =
                    Integer.parseInt(
                            dentistIdText
                    );

            if (dentistId <= 0) {

                showInvalidDentistId(
                        request,
                        response
                );

                return;
            }

            Dentist dentist =
                    dentistService
                            .searchDentist(
                                    dentistId
                            );

            if (dentist != null) {

                request.setAttribute(
                        "dentist",
                        dentist
                );

                request.getRequestDispatcher(
                        "dentistDetails.jsp"
                ).forward(
                        request,
                        response
                );

            } else {

                request.setAttribute(
                        "errorMessage",
                        "Dentist not found."
                );

                request.getRequestDispatcher(
                        "dentistList.jsp"
                ).forward(
                        request,
                        response
                );
            }

        } catch (NumberFormatException e) {

            showInvalidDentistId(
                    request,
                    response
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to retrieve dentist.",
                    e
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

        if (!"ADMIN".equals(
                session.getAttribute("role"))) {

            response.sendError(
                    HttpServletResponse
                            .SC_FORBIDDEN,
                    "Admin access required."
            );

            return;
        }

        String action =
                request.getParameter("action");

        try {

            if ("add".equals(action)) {

                addDentist(
                        request,
                        response
                );

            } else if ("update".equals(action)) {

                updateDentist(
                        request,
                        response
                );

            } else if ("delete".equals(action)) {

                deleteDentist(
                        request,
                        response
                );

            } else {

                request.setAttribute(
                        "errorMessage",
                        "Invalid dentist action."
                );

                request.getRequestDispatcher(
                        "dentistList.jsp"
                ).forward(
                        request,
                        response
                );
            }

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to process dentist request.",
                    e
            );
        }
    }

    private void addDentist(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException,
                   ServletException,
                   IOException {

        String dentistName =
                request.getParameter(
                        "dentistName"
                );

        String specialization =
                request.getParameter(
                        "specialization"
                );

        String contactNumber =
                request.getParameter(
                        "contactNumber"
                );

        if (isEmpty(dentistName)
                || isEmpty(specialization)
                || isEmpty(contactNumber)) {

            request.setAttribute(
                    "errorMessage",
                    "All dentist fields are required."
            );

            request.getRequestDispatcher(
                    "addDentist.jsp"
            ).forward(
                    request,
                    response
            );

            return;
        }

        Dentist dentist =
                new Dentist(
                        0,
                        dentistName,
                        specialization,
                        contactNumber
                );

        boolean added =
                dentistService
                        .addDentist(dentist);

        if (added) {

            request.setAttribute(
                    "dentist",
                    dentist
            );

            request.setAttribute(
                    "successMessage",
                    "Dentist added successfully."
            );

            request.getRequestDispatcher(
                    "dentistDetails.jsp"
            ).forward(
                    request,
                    response
            );

        } else {

            request.setAttribute(
                    "errorMessage",
                    "Unable to add dentist."
            );

            request.getRequestDispatcher(
                    "addDentist.jsp"
            ).forward(
                    request,
                    response
            );
        }
    }

    private void updateDentist(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException,
                   ServletException,
                   IOException {

        String dentistIdText =
                request.getParameter(
                        "dentistId"
                );

        String dentistName =
                request.getParameter(
                        "dentistName"
                );

        String specialization =
                request.getParameter(
                        "specialization"
                );

        String contactNumber =
                request.getParameter(
                        "contactNumber"
                );

        if (isEmpty(dentistIdText)
                || isEmpty(dentistName)
                || isEmpty(specialization)
                || isEmpty(contactNumber)) {

            request.setAttribute(
                    "errorMessage",
                    "All dentist fields are required."
            );

            request.getRequestDispatcher(
                    "editDentist.jsp"
            ).forward(
                    request,
                    response
            );

            return;
        }

        try {

            int dentistId =
                    Integer.parseInt(
                            dentistIdText
                    );

            if (dentistId <= 0) {

                showInvalidDentistId(
                        request,
                        response
                );

                return;
            }

            Dentist dentist =
                    new Dentist(
                            dentistId,
                            dentistName,
                            specialization,
                            contactNumber
                    );

            boolean updated =
                    dentistService
                            .updateDentist(
                                    dentist
                            );

            if (updated) {

                request.setAttribute(
                        "dentist",
                        dentist
                );

                request.setAttribute(
                        "successMessage",
                        "Dentist updated successfully."
                );

                request.getRequestDispatcher(
                        "dentistDetails.jsp"
                ).forward(
                        request,
                        response
                );

            } else {

                request.setAttribute(
                        "errorMessage",
                        "Unable to update dentist."
                );

                request.getRequestDispatcher(
                        "editDentist.jsp"
                ).forward(
                        request,
                        response
                );
            }

        } catch (NumberFormatException e) {

            showInvalidDentistId(
                    request,
                    response
            );
        }
    }

    private void deleteDentist(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException,
                   ServletException,
                   IOException {

        String dentistIdText =
                request.getParameter(
                        "dentistId"
                );

        if (isEmpty(dentistIdText)) {

            showInvalidDentistId(
                    request,
                    response
            );

            return;
        }

        try {

            int dentistId =
                    Integer.parseInt(
                            dentistIdText
                    );

            if (dentistId <= 0) {

                showInvalidDentistId(
                        request,
                        response
                );

                return;
            }

            boolean deleted =
                    dentistService
                            .deleteDentist(
                                    dentistId
                            );

            if (deleted) {

                response.sendRedirect(
                        "dentist"
                );

            } else {

                request.setAttribute(
                        "errorMessage",
                        "Unable to delete dentist."
                );

                request.getRequestDispatcher(
                        "dentistList.jsp"
                ).forward(
                        request,
                        response
                );
            }

        } catch (NumberFormatException e) {

            showInvalidDentistId(
                    request,
                    response
            );
        }
    }

    private void showInvalidDentistId(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException,
                   IOException {

        request.setAttribute(
                "errorMessage",
                "Invalid dentist ID."
        );

        request.getRequestDispatcher(
                "dentistList.jsp"
        ).forward(
                request,
                response
        );
    }

    private boolean isEmpty(
            String value) {

        return value == null
                || value.trim().isEmpty();
    }
}