package sunrisedentalsystem.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sunrisedentalsystem.dao.ReportDAOImpl;
import sunrisedentalsystem.model.ClinicReport;
import sunrisedentalsystem.service.ReportService;
import sunrisedentalsystem.service.ReportServiceImpl;

@WebServlet("/report")
public class ReportServlet
        extends HttpServlet {

    private static final long serialVersionUID =
            1L;

    private ReportService reportService;

    public ReportServlet() {
    }

    ReportServlet(
            ReportService reportService) {

        this.reportService =
                reportService;
    }

    @Override
    public void init()
            throws ServletException {

        if (reportService == null) {

            reportService =
                    new ReportServiceImpl(
                            new ReportDAOImpl()
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
                request.getSession(
                        false
                );

        if (session == null
                || session.getAttribute(
                        "loggedInUser"
                ) == null) {

            response.sendRedirect(
                    "login.jsp"
            );

            return;
        }

        String role =
                (String) session.getAttribute(
                        "role"
                );

        if (!"ADMIN".equals(
                role
        )) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Admin access required."
            );

            return;
        }

        LocalDate reportDate =
                LocalDate.now();

        String dateParameter =
                request.getParameter(
                        "reportDate"
                );

        if (dateParameter != null
                && !dateParameter.trim().isEmpty()) {

            try {

                reportDate =
                        LocalDate.parse(
                                dateParameter
                        );

            } catch (DateTimeParseException e) {

                request.setAttribute(
                        "errorMessage",
                        "Enter a valid report date."
                );
            }
        }

        try {

            ClinicReport report =
                    reportService
                            .generateClinicReport(
                                    reportDate
                            );

            request.setAttribute(
                    "clinicReport",
                    report
            );

            request.setAttribute(
                    "selectedDate",
                    reportDate
            );

            RequestDispatcher dispatcher =
                    request.getRequestDispatcher(
                            "report.jsp"
                    );

            dispatcher.forward(
                    request,
                    response
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to generate clinic report.",
                    e
            );
        }
    }
}