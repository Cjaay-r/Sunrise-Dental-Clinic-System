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

import sunrisedentalsystem.dao.TreatmentDAOImpl;
import sunrisedentalsystem.model.Treatment;
import sunrisedentalsystem.service.TreatmentService;
import sunrisedentalsystem.service.TreatmentServiceImpl;

@WebServlet("/treatment")
public class TreatmentServlet
        extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private TreatmentService treatmentService;

    public TreatmentServlet() {
    }

    TreatmentServlet(
            TreatmentService treatmentService) {

        this.treatmentService =
                treatmentService;
    }

    @Override
    public void init()
            throws ServletException {

        if (treatmentService == null) {

            treatmentService =
                    new TreatmentServiceImpl(
                            new TreatmentDAOImpl()
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

        String action =
                request.getParameter(
                        "action"
                );

        if (("add".equals(action)
                || "edit".equals(action)
                || "delete".equals(action))
                && !isAdmin(session)) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Admin access required."
            );

            return;
        }

        String treatmentIdText =
                request.getParameter(
                        "treatmentId"
                );

        try {

            if ("add".equals(action)) {

                request.getRequestDispatcher(
                        "addTreatment.jsp"
                ).forward(
                        request,
                        response
                );

                return;
            }

            if (isEmpty(treatmentIdText)) {

                showTreatmentList(
                        request,
                        response
                );

                return;
            }

            int treatmentId;

            try {

                treatmentId =
                        Integer.parseInt(
                                treatmentIdText
                        );

            } catch (NumberFormatException e) {

                showInvalidTreatmentId(
                        request,
                        response
                );

                return;
            }

            if (treatmentId <= 0) {

                showInvalidTreatmentId(
                        request,
                        response
                );

                return;
            }

            Treatment treatment =
                    treatmentService
                            .getTreatmentById(
                                    treatmentId
                            );

            if (treatment == null) {

                request.setAttribute(
                        "errorMessage",
                        "Treatment not found."
                );

                showTreatmentList(
                        request,
                        response
                );

                return;
            }

            request.setAttribute(
                    "treatment",
                    treatment
            );

            if ("edit".equals(action)) {

                request.getRequestDispatcher(
                        "editTreatment.jsp"
                ).forward(
                        request,
                        response
                );

                return;
            }

            if ("delete".equals(action)) {

                request.getRequestDispatcher(
                        "deleteTreatment.jsp"
                ).forward(
                        request,
                        response
                );

                return;
            }

            request.getRequestDispatcher(
                    "treatmentDetails.jsp"
            ).forward(
                    request,
                    response
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to retrieve treatment.",
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

        if (!isAdmin(session)) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Admin access required."
            );

            return;
        }

        String action =
                request.getParameter(
                        "action"
                );

        try {

            if ("add".equals(action)) {

                addTreatment(
                        request,
                        response
                );

            } else if ("update".equals(action)) {

                updateTreatment(
                        request,
                        response
                );

            } else if ("delete".equals(action)) {

                deleteTreatment(
                        request,
                        response
                );

            } else {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid treatment action."
                );
            }

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to process treatment request.",
                    e
            );
        }
    }

    private void addTreatment(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException,
                   IOException,
                   SQLException {

        String treatmentType =
                request.getParameter(
                        "treatmentType"
                );

        String treatmentPriceText =
                request.getParameter(
                        "treatmentPrice"
                );

        if (isEmpty(treatmentType)
                || isEmpty(treatmentPriceText)) {

            request.setAttribute(
                    "errorMessage",
                    "All treatment fields are required."
            );

            request.getRequestDispatcher(
                    "addTreatment.jsp"
            ).forward(
                    request,
                    response
            );

            return;
        }

        double treatmentPrice;

        try {

            treatmentPrice =
                    Double.parseDouble(
                            treatmentPriceText
                    );

        } catch (NumberFormatException e) {

            showInvalidPrice(
                    request,
                    response,
                    "addTreatment.jsp"
            );

            return;
        }

        if (treatmentPrice < 0) {

            showInvalidPrice(
                    request,
                    response,
                    "addTreatment.jsp"
            );

            return;
        }

        Treatment treatment =
                new Treatment(
                        0,
                        treatmentType.trim(),
                        treatmentPrice
                );

        boolean added =
                treatmentService
                        .addTreatment(
                                treatment
                        );

        if (added) {

            request.setAttribute(
                    "treatment",
                    treatment
            );

            request.setAttribute(
                    "successMessage",
                    "Treatment added successfully."
            );

            request.getRequestDispatcher(
                    "treatmentDetails.jsp"
            ).forward(
                    request,
                    response
            );

        } else {

            request.setAttribute(
                    "errorMessage",
                    "Unable to add treatment."
            );

            request.getRequestDispatcher(
                    "addTreatment.jsp"
            ).forward(
                    request,
                    response
            );
        }
    }

    private void updateTreatment(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException,
                   IOException,
                   SQLException {

        String treatmentIdText =
                request.getParameter(
                        "treatmentId"
                );

        String treatmentType =
                request.getParameter(
                        "treatmentType"
                );

        String treatmentPriceText =
                request.getParameter(
                        "treatmentPrice"
                );

        int treatmentId;

        try {

            treatmentId =
                    Integer.parseInt(
                            treatmentIdText
                    );

        } catch (NumberFormatException e) {

            showInvalidTreatmentId(
                    request,
                    response
            );

            return;
        }

        if (treatmentId <= 0) {

            showInvalidTreatmentId(
                    request,
                    response
            );

            return;
        }

        Treatment existingTreatment =
                treatmentService
                        .getTreatmentById(
                                treatmentId
                        );

        if (existingTreatment == null) {

            request.setAttribute(
                    "errorMessage",
                    "Treatment not found."
            );

            showTreatmentList(
                    request,
                    response
            );

            return;
        }

        if (isEmpty(treatmentType)
                || isEmpty(treatmentPriceText)) {

            request.setAttribute(
                    "treatment",
                    existingTreatment
            );

            request.setAttribute(
                    "errorMessage",
                    "All treatment fields are required."
            );

            request.getRequestDispatcher(
                    "editTreatment.jsp"
            ).forward(
                    request,
                    response
            );

            return;
        }

        double treatmentPrice;

        try {

            treatmentPrice =
                    Double.parseDouble(
                            treatmentPriceText
                    );

        } catch (NumberFormatException e) {

            request.setAttribute(
                    "treatment",
                    existingTreatment
            );

            showInvalidPrice(
                    request,
                    response,
                    "editTreatment.jsp"
            );

            return;
        }

        if (treatmentPrice < 0) {

            request.setAttribute(
                    "treatment",
                    existingTreatment
            );

            showInvalidPrice(
                    request,
                    response,
                    "editTreatment.jsp"
            );

            return;
        }

        Treatment treatment =
                new Treatment(
                        treatmentId,
                        treatmentType.trim(),
                        treatmentPrice
                );

        boolean updated =
                treatmentService
                        .updateTreatment(
                                treatment
                        );

        if (updated) {

            request.setAttribute(
                    "treatment",
                    treatment
            );

            request.setAttribute(
                    "successMessage",
                    "Treatment updated successfully."
            );

            request.getRequestDispatcher(
                    "treatmentDetails.jsp"
            ).forward(
                    request,
                    response
            );

        } else {

            request.setAttribute(
                    "treatment",
                    treatment
            );

            request.setAttribute(
                    "errorMessage",
                    "Unable to update treatment."
            );

            request.getRequestDispatcher(
                    "editTreatment.jsp"
            ).forward(
                    request,
                    response
            );
        }
    }

    private void deleteTreatment(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException,
                   IOException,
                   SQLException {

        String treatmentIdText =
                request.getParameter(
                        "treatmentId"
                );

        int treatmentId;

        try {

            treatmentId =
                    Integer.parseInt(
                            treatmentIdText
                    );

        } catch (NumberFormatException e) {

            showInvalidTreatmentId(
                    request,
                    response
            );

            return;
        }

        if (treatmentId <= 0) {

            showInvalidTreatmentId(
                    request,
                    response
            );

            return;
        }

        boolean deleted =
                treatmentService
                        .deleteTreatment(
                                treatmentId
                        );

        if (deleted) {

            response.sendRedirect(
                    "treatment"
            );

        } else {

            request.setAttribute(
                    "errorMessage",
                    "Unable to delete treatment."
            );

            showTreatmentList(
                    request,
                    response
            );
        }
    }

    private void showTreatmentList(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException,
                   IOException,
                   SQLException {

        List<Treatment> treatments =
                treatmentService
                        .getAllTreatments();

        request.setAttribute(
                "treatments",
                treatments
        );

        request.getRequestDispatcher(
                "treatmentList.jsp"
        ).forward(
                request,
                response
        );
    }

    private void showInvalidPrice(
            HttpServletRequest request,
            HttpServletResponse response,
            String page)
            throws ServletException,
                   IOException {

        request.setAttribute(
                "errorMessage",
                "Invalid treatment price."
        );

        request.getRequestDispatcher(
                page
        ).forward(
                request,
                response
        );
    }

    private void showInvalidTreatmentId(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException,
                   IOException,
                   SQLException {

        request.setAttribute(
                "errorMessage",
                "Invalid treatment ID."
        );

        showTreatmentList(
                request,
                response
        );
    }

    private boolean isAdmin(
            HttpSession session) {

        return "ADMIN".equals(
                session.getAttribute(
                        "role"
                )
        );
    }

    private boolean isEmpty(
            String value) {

        return value == null
                || value.trim().isEmpty();
    }
}