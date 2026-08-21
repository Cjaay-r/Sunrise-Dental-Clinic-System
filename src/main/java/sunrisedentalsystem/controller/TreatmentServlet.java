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

import sunrisedentalsystem.dao.TreatmentDAOImpl;
import sunrisedentalsystem.model.Treatment;
import sunrisedentalsystem.service.TreatmentService;
import sunrisedentalsystem.service.TreatmentServiceImpl;

@WebServlet("/treatment")
public class TreatmentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private TreatmentService treatmentService;

    public TreatmentServlet() {
    }

    TreatmentServlet(TreatmentService treatmentService) {
        this.treatmentService = treatmentService;
    }

    @Override
    public void init() throws ServletException {

        if (treatmentService == null) {

            treatmentService =
                    new TreatmentServiceImpl(
                            new TreatmentDAOImpl()
                    );
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

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

            RequestDispatcher dispatcher =
                    request.getRequestDispatcher(
                            "addTreatment.jsp"
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

            response.sendRedirect(
                    "login.jsp"
            );

            return;
        }

        try {

            double treatmentPrice =
                    Double.parseDouble(
                            treatmentPriceText
                    );

            if (treatmentPrice < 0) {

                showInvalidPrice(
                        request,
                        response
                );

                return;
            }

            Treatment treatment =
                    new Treatment(
                            0,
                            treatmentType,
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

        } catch (NumberFormatException e) {

            showInvalidPrice(
                    request,
                    response
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to add treatment.",
                    e
            );
        }
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String treatmentIdText =
                request.getParameter(
                        "treatmentId"
                );

        try {

            /*
             * If no ID is supplied,
             * display all available treatments.
             */
            if (isEmpty(treatmentIdText)) {

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

                return;
            }

            int treatmentId =
                    Integer.parseInt(
                            treatmentIdText
                    );

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

            if (treatment != null) {

                request.setAttribute(
                        "treatment",
                        treatment
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
                        "Treatment not found."
                );

                request.getRequestDispatcher(
                        "searchTreatment.jsp"
                ).forward(
                        request,
                        response
                );
            }

        } catch (NumberFormatException e) {

            showInvalidTreatmentId(
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

    private boolean isEmpty(String value) {

        return value == null
                || value.trim().isEmpty();
    }

    private void showInvalidPrice(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute(
                "errorMessage",
                "Invalid treatment price."
        );

        request.getRequestDispatcher(
                "addTreatment.jsp"
        ).forward(
                request,
                response
        );
    }

    private void showInvalidTreatmentId(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute(
                "errorMessage",
                "Invalid treatment ID."
        );

        request.getRequestDispatcher(
                "searchTreatment.jsp"
        ).forward(
                request,
                response
        );
    }
}