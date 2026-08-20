package sunrisedentalsystem.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sunrisedentalsystem.dao.AppointmentDAOImpl;
import sunrisedentalsystem.model.Appointment;
import sunrisedentalsystem.model.AppointmentStatus;
import sunrisedentalsystem.model.Dentist;
import sunrisedentalsystem.model.Patient;
import sunrisedentalsystem.model.Treatment;
import sunrisedentalsystem.model.User;
import sunrisedentalsystem.service.AppointmentService;
import sunrisedentalsystem.service.AppointmentServiceImpl;

@WebServlet("/appointment")
public class AppointmentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private AppointmentService appointmentService;

    public AppointmentServlet() {
    }

    AppointmentServlet(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Override
    public void init() throws ServletException {

        if (appointmentService == null) {

            appointmentService =
                    new AppointmentServiceImpl(
                            new AppointmentDAOImpl()
                    );
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String appointmentNo =
                request.getParameter("appointmentNo");

        String patientIdText =
                request.getParameter("patientId");

        String dentistIdText =
                request.getParameter("dentistId");

        String treatmentIdText =
                request.getParameter("treatmentId");

        String appointmentDateText =
                request.getParameter("appointmentDate");

        String appointmentTimeText =
                request.getParameter("appointmentTime");

        if (isEmpty(appointmentNo)
                || isEmpty(patientIdText)
                || isEmpty(dentistIdText)
                || isEmpty(treatmentIdText)
                || isEmpty(appointmentDateText)
                || isEmpty(appointmentTimeText)) {

            request.setAttribute(
                    "errorMessage",
                    "All appointment fields are required."
            );

            RequestDispatcher dispatcher =
                    request.getRequestDispatcher(
                            "registerAppointment.jsp"
                    );

            dispatcher.forward(request, response);

            return;
        }

        HttpSession session =
                request.getSession(false);

        if (session == null
                || session.getAttribute("loggedInUser") == null) {

            response.sendRedirect("login.jsp");

            return;
        }

        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        try {

            int patientId =
                    Integer.parseInt(patientIdText);

            int dentistId =
                    Integer.parseInt(dentistIdText);

            int treatmentId =
                    Integer.parseInt(treatmentIdText);

            LocalDate appointmentDate =
                    LocalDate.parse(appointmentDateText);

            LocalTime appointmentTime =
                    LocalTime.parse(appointmentTimeText);

            boolean available =
                    appointmentService.checkAvailability(
                            dentistId,
                            appointmentDate,
                            appointmentTime
                    );

            if (!available) {

                request.setAttribute(
                        "errorMessage",
                        "Selected appointment slot is unavailable."
                );

                request.getRequestDispatcher(
                        "registerAppointment.jsp"
                ).forward(request, response);

                return;
            }

            Patient patient =
                    new Patient(
                            patientId,
                            null,
                            null,
                            null
                    );

            Dentist dentist =
                    new Dentist(
                            dentistId,
                            null
                    );

            Treatment treatment =
                    new Treatment(
                            treatmentId,
                            null,
                            0.0
                    );

            Appointment appointment =
                    new Appointment(
                            appointmentNo,
                            appointmentDate,
                            appointmentTime,
                            AppointmentStatus.SCHEDULED,
                            patient,
                            dentist,
                            treatment
                    );

            Appointment registeredAppointment =
                    appointmentService.registerAppointment(
                            appointment,
                            loggedInUser.getUserId()
                    );

            request.setAttribute(
                    "appointment",
                    registeredAppointment
            );

            request.setAttribute(
                    "successMessage",
                    "Appointment registered successfully."
            );

            request.getRequestDispatcher(
                    "appointmentDetails.jsp"
            ).forward(request, response);

        } catch (NumberFormatException
                 | DateTimeParseException e) {

            request.setAttribute(
                    "errorMessage",
                    "Invalid appointment details."
            );

            request.getRequestDispatcher(
                    "registerAppointment.jsp"
            ).forward(request, response);

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to register appointment.",
                    e
            );
        }
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String appointmentNo =
                request.getParameter("appointmentNo");

        if (isEmpty(appointmentNo)) {

            request.setAttribute(
                    "errorMessage",
                    "Appointment number is required."
            );

            request.getRequestDispatcher(
                    "searchAppointment.jsp"
            ).forward(request, response);

            return;
        }

        try {

            Appointment appointment =
                    appointmentService
                            .searchAppointment(appointmentNo);

            if (appointment != null) {

                request.setAttribute(
                        "appointment",
                        appointment
                );

                request.getRequestDispatcher(
                        "appointmentDetails.jsp"
                ).forward(request, response);

            } else {

                request.setAttribute(
                        "errorMessage",
                        "Appointment not found."
                );

                request.getRequestDispatcher(
                        "searchAppointment.jsp"
                ).forward(request, response);
            }

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to search appointment.",
                    e
            );
        }
    }

    private boolean isEmpty(String value) {

        return value == null
                || value.trim().isEmpty();
    }
}