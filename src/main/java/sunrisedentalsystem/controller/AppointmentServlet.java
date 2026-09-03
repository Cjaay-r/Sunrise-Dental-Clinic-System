package sunrisedentalsystem.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sunrisedentalsystem.dao.AppointmentDAOImpl;
import sunrisedentalsystem.dao.DentistDAOImpl;
import sunrisedentalsystem.dao.PatientDAOImpl;
import sunrisedentalsystem.dao.TreatmentDAOImpl;
import sunrisedentalsystem.model.Appointment;
import sunrisedentalsystem.model.AppointmentStatus;
import sunrisedentalsystem.model.Dentist;
import sunrisedentalsystem.model.Patient;
import sunrisedentalsystem.model.Treatment;
import sunrisedentalsystem.model.User;
import sunrisedentalsystem.service.AppointmentService;
import sunrisedentalsystem.service.AppointmentServiceImpl;
import sunrisedentalsystem.service.DentistService;
import sunrisedentalsystem.service.DentistServiceImpl;
import sunrisedentalsystem.service.EmailService;
import sunrisedentalsystem.service.EmailServiceImpl;
import sunrisedentalsystem.service.PatientService;
import sunrisedentalsystem.service.PatientServiceImpl;
import sunrisedentalsystem.service.TreatmentService;
import sunrisedentalsystem.service.TreatmentServiceImpl;

@WebServlet("/appointment")
public class AppointmentServlet
        extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private AppointmentService appointmentService;

    private PatientService patientService;

    private DentistService dentistService;

    private TreatmentService treatmentService;

    private EmailService emailService;

    public AppointmentServlet() {
    }

    AppointmentServlet(
            AppointmentService appointmentService) {

        this.appointmentService =
                appointmentService;
    }

    AppointmentServlet(
            AppointmentService appointmentService,
            PatientService patientService,
            DentistService dentistService,
            TreatmentService treatmentService,
            EmailService emailService) {

        this.appointmentService =
                appointmentService;

        this.patientService =
                patientService;

        this.dentistService =
                dentistService;

        this.treatmentService =
                treatmentService;

        this.emailService =
                emailService;
    }

    @Override
    public void init()
            throws ServletException {

        if (appointmentService == null) {

            appointmentService =
                    new AppointmentServiceImpl(
                            new AppointmentDAOImpl()
                    );
        }

        if (patientService == null) {

            patientService =
                    new PatientServiceImpl(
                            new PatientDAOImpl()
                    );
        }

        if (dentistService == null) {

            dentistService =
                    new DentistServiceImpl(
                            new DentistDAOImpl()
                    );
        }

        if (treatmentService == null) {

            treatmentService =
                    new TreatmentServiceImpl(
                            new TreatmentDAOImpl()
                    );
        }

        if (emailService == null) {

            emailService =
                    new EmailServiceImpl();
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

        if ("register".equals(action)) {

            if (!isStaff(session)) {

                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Staff access required."
                );

                return;
            }

            showRegisterAppointmentPage(
                    request,
                    response
            );

            return;
        }

        if ("cancel".equals(action)
                && !isStaff(session)) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Staff access required."
            );

            return;
        }

        String appointmentNo =
                request.getParameter(
                        "appointmentNo"
                );

        try {

            if (isEmpty(appointmentNo)) {

                showAppointmentManagement(
                        request,
                        response
                );

                return;
            }

            if (!isValidAppointmentNo(
                    appointmentNo)) {

                request.setAttribute(
                        "errorMessage",
                        "Invalid appointment number."
                );

                showAppointmentManagement(
                        request,
                        response
                );

                return;
            }

            Appointment appointment =
                    appointmentService
                            .searchAppointment(
                                    appointmentNo
                            );

            if (appointment == null) {

                request.setAttribute(
                        "errorMessage",
                        "Appointment not found."
                );

                showAppointmentManagement(
                        request,
                        response
                );

                return;
            }

            request.setAttribute(
                    "appointment",
                    appointment
            );

            if ("cancel".equals(action)) {

                request.getRequestDispatcher(
                        "cancelAppointment.jsp"
                ).forward(
                        request,
                        response
                );

                return;
            }

            request.getRequestDispatcher(
                    "appointmentDetails.jsp"
            ).forward(
                    request,
                    response
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to retrieve appointments.",
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

        String action =
                request.getParameter(
                        "action"
                );

        if (("register".equals(action)
                || "cancel".equals(action))
                && !isStaff(session)) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Staff access required."
            );

            return;
        }

        try {

            if ("register".equals(action)) {

                registerAppointment(
                        request,
                        response,
                        session
                );

            } else if ("cancel".equals(action)) {

                cancelAppointment(
                        request,
                        response
                );

            } else {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid appointment action."
                );
            }

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to process appointment request.",
                    e
            );
        }
    }

    private void registerAppointment(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session)
            throws ServletException,
                   IOException,
                   SQLException {

        String patientIdText =
                request.getParameter(
                        "patientId"
                );

        String dentistIdText =
                request.getParameter(
                        "dentistId"
                );

        String treatmentIdText =
                request.getParameter(
                        "treatmentId"
                );

        String appointmentDateText =
                request.getParameter(
                        "appointmentDate"
                );

        String appointmentTimeText =
                request.getParameter(
                        "appointmentTime"
                );

        if (isEmpty(patientIdText)
                || isEmpty(dentistIdText)
                || isEmpty(treatmentIdText)
                || isEmpty(appointmentDateText)
                || isEmpty(appointmentTimeText)) {

            request.setAttribute(
                    "errorMessage",
                    "All appointment fields are required."
            );

            showRegisterAppointmentPage(
                    request,
                    response
            );

            return;
        }

        try {

            int patientId =
                    Integer.parseInt(
                            patientIdText
                    );

            int dentistId =
                    Integer.parseInt(
                            dentistIdText
                    );

            int treatmentId =
                    Integer.parseInt(
                            treatmentIdText
                    );

            if (patientId <= 0
                    || dentistId <= 0
                    || treatmentId <= 0) {

                showInvalidAppointmentDetails(
                        request,
                        response
                );

                return;
            }

            Patient selectedPatient =
                    null;

            if (patientService != null) {

                selectedPatient =
                        patientService
                                .searchPatient(
                                        patientId
                                );

                if (selectedPatient == null) {

                    request.setAttribute(
                            "errorMessage",
                            "Selected patient could not be found."
                    );

                    showRegisterAppointmentPage(
                            request,
                            response
                    );

                    return;
                }
            }

            LocalDate appointmentDate =
                    LocalDate.parse(
                            appointmentDateText
                    );

            LocalTime appointmentTime =
                    LocalTime.parse(
                            appointmentTimeText
                    );

            if (appointmentDate.isBefore(
                    LocalDate.now())) {

                request.setAttribute(
                        "errorMessage",
                        "Appointment date cannot be in the past."
                );

                showRegisterAppointmentPage(
                        request,
                        response
                );

                return;
            }

            boolean available =
                    appointmentService
                            .checkAvailability(
                                    dentistId,
                                    appointmentDate,
                                    appointmentTime
                            );

            if (!available) {

                request.setAttribute(
                        "errorMessage",
                        "Selected appointment slot is unavailable."
                );

                showRegisterAppointmentPage(
                        request,
                        response
                );

                return;
            }

            Patient patient =
                    selectedPatient != null
                    ? selectedPatient
                    : new Patient(
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
                            null,
                            appointmentDate,
                            appointmentTime,
                            AppointmentStatus.SCHEDULED,
                            patient,
                            dentist,
                            treatment
                    );

            User loggedInUser =
                    (User) session.getAttribute(
                            "loggedInUser"
                    );

            Appointment registeredAppointment =
                    appointmentService
                            .registerAppointment(
                                    appointment,
                                    loggedInUser.getUserId()
                            );

            Appointment appointmentDetails =
                    appointmentService
                            .searchAppointment(
                                    registeredAppointment
                                            .getAppointmentNo()
                            );

            if (appointmentDetails == null) {

                appointmentDetails =
                        registeredAppointment;
            }

            String successMessage =
                    createRegistrationSuccessMessage(
                            appointmentDetails,
                            selectedPatient
                    );

            request.setAttribute(
                    "appointment",
                    appointmentDetails
            );

            request.setAttribute(
                    "successMessage",
                    successMessage
            );

            request.getRequestDispatcher(
                    "appointmentDetails.jsp"
            ).forward(
                    request,
                    response
            );

        } catch (NumberFormatException
                 | DateTimeParseException e) {

            showInvalidAppointmentDetails(
                    request,
                    response
            );
        }
    }

    private void cancelAppointment(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException,
                   IOException,
                   SQLException {

        String appointmentNo =
                request.getParameter(
                        "appointmentNo"
                );

        if (isEmpty(appointmentNo)
                || !isValidAppointmentNo(
                        appointmentNo)) {

            request.setAttribute(
                    "errorMessage",
                    "Invalid appointment number."
            );

            showAppointmentManagement(
                    request,
                    response
            );

            return;
        }

        Appointment appointment =
                appointmentService
                        .searchAppointment(
                                appointmentNo
                        );

        if (appointment == null) {

            request.setAttribute(
                    "errorMessage",
                    "Appointment not found."
            );

            showAppointmentManagement(
                    request,
                    response
            );

            return;
        }

        if (appointment.getStatus()
                == AppointmentStatus.CANCELLED) {

            request.setAttribute(
                    "appointment",
                    appointment
            );

            request.setAttribute(
                    "errorMessage",
                    "Appointment is already cancelled."
            );

            request.getRequestDispatcher(
                    "appointmentDetails.jsp"
            ).forward(
                    request,
                    response
            );

            return;
        }

        boolean cancelled =
                appointmentService
                        .cancelAppointment(
                                appointmentNo
                        );

        if (cancelled) {

            Appointment updatedAppointment =
                    appointmentService
                            .searchAppointment(
                                    appointmentNo
                            );

            Appointment appointmentForEmail =
                    updatedAppointment != null
                    ? updatedAppointment
                    : appointment;

            String successMessage =
                    createCancellationSuccessMessage(
                            appointmentForEmail
                    );

            request.setAttribute(
                    "appointment",
                    appointmentForEmail
            );

            request.setAttribute(
                    "successMessage",
                    successMessage
            );

            request.getRequestDispatcher(
                    "appointmentDetails.jsp"
            ).forward(
                    request,
                    response
            );

        } else {

            request.setAttribute(
                    "appointment",
                    appointment
            );

            request.setAttribute(
                    "errorMessage",
                    "Unable to cancel appointment."
            );

            request.getRequestDispatcher(
                    "appointmentDetails.jsp"
            ).forward(
                    request,
                    response
            );
        }
    }

    private String createRegistrationSuccessMessage(
            Appointment appointment,
            Patient selectedPatient) {

        String defaultMessage =
                "Appointment registered successfully.";

        if (selectedPatient == null
                || isEmpty(
                        selectedPatient.getEmail()
                )) {

            return defaultMessage;
        }

        if (appointment == null
                || appointment.getPatient() == null
                || emailService == null) {

            return defaultMessage
                    + " Confirmation email could not be sent.";
        }

        appointment
                .getPatient()
                .setEmail(
                        selectedPatient.getEmail()
                );

        boolean sent =
                sendConfirmationEmailSafely(
                        appointment
                );

        if (sent) {

            return defaultMessage
                    + " Confirmation email sent.";
        }

        return defaultMessage
                + " Confirmation email could not be sent.";
    }

    private String createCancellationSuccessMessage(
            Appointment appointment) {

        String defaultMessage =
                "Appointment cancelled successfully.";

        if (appointment == null
                || appointment.getPatient() == null) {

            return defaultMessage;
        }

        String email =
                findPatientEmail(
                        appointment
                );

        if (isEmpty(email)) {

            return defaultMessage;
        }

        appointment
                .getPatient()
                .setEmail(
                        email
                );

        if (emailService == null) {

            return defaultMessage
                    + " Cancellation email could not be sent.";
        }

        boolean sent =
                sendCancellationEmailSafely(
                        appointment
                );

        if (sent) {

            return defaultMessage
                    + " Cancellation email sent.";
        }

        return defaultMessage
                + " Cancellation email could not be sent.";
    }

    private String findPatientEmail(
            Appointment appointment) {

        if (appointment == null
                || appointment.getPatient() == null) {

            return null;
        }

        String existingEmail =
                appointment
                        .getPatient()
                        .getEmail();

        if (!isEmpty(existingEmail)) {

            return existingEmail;
        }

        if (patientService == null) {

            return null;
        }

        int patientId =
                appointment
                        .getPatient()
                        .getPatientId();

        if (patientId <= 0) {

            return null;
        }

        try {

            Patient patient =
                    patientService
                            .searchPatient(
                                    patientId
                            );

            if (patient != null) {

                return patient.getEmail();
            }

        } catch (SQLException e) {

            return null;
        }

        return null;
    }

    private boolean sendConfirmationEmailSafely(
            Appointment appointment) {

        try {

            return emailService
                    .sendAppointmentConfirmation(
                            appointment
                    );

        } catch (RuntimeException e) {

            return false;
        }
    }

    private boolean sendCancellationEmailSafely(
            Appointment appointment) {

        try {

            return emailService
                    .sendAppointmentCancellation(
                            appointment
                    );

        } catch (RuntimeException e) {

            return false;
        }
    }

    private void showRegisterAppointmentPage(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException,
                   IOException {

        try {

            List<Dentist> dentists =
                    dentistService != null
                    ? dentistService.getAllDentists()
                    : List.of();

            List<Treatment> treatments =
                    treatmentService != null
                    ? treatmentService.getAllTreatments()
                    : List.of();

            Patient selectedPatient =
                    null;

            List<Patient> patientResults =
                    new ArrayList<>();

            String patientIdText =
                    request.getParameter(
                            "patientId"
                    );

            if (!isEmpty(patientIdText)
                    && patientService != null) {

                try {

                    int patientId =
                            Integer.parseInt(
                                    patientIdText
                            );

                    if (patientId > 0) {

                        selectedPatient =
                                patientService
                                        .searchPatient(
                                                patientId
                                        );
                    }

                } catch (NumberFormatException e) {

                    selectedPatient =
                            null;
                }
            }

            String patientSearch =
                    request.getParameter(
                            "patientSearch"
                    );

            if (!isEmpty(patientSearch)
                    && patientService != null) {

                String searchValue =
                        patientSearch.trim();

                if (searchValue.matches("\\d+")) {

                    Patient patient =
                            patientService
                                    .searchPatientByContactNumber(
                                            searchValue
                                    );

                    if (patient != null) {

                        patientResults.add(
                                patient
                        );
                    }

                } else {

                    patientResults =
                            patientService
                                    .searchPatientsByName(
                                            searchValue
                                    );
                }
            }

            request.setAttribute(
                    "dentists",
                    dentists
            );

            request.setAttribute(
                    "treatments",
                    treatments
            );

            request.setAttribute(
                    "selectedPatient",
                    selectedPatient
            );

            request.setAttribute(
                    "patientResults",
                    patientResults
            );

            request.setAttribute(
                    "patientSearch",
                    patientSearch
            );

            request.getRequestDispatcher(
                    "registerAppointment.jsp"
            ).forward(
                    request,
                    response
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to load appointment registration data.",
                    e
            );
        }
    }

    private void showAppointmentManagement(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException,
                   IOException,
                   SQLException {

        List<Appointment> allAppointments =
                appointmentService
                        .getAllAppointments();

        int scheduledCount =
                0;

        int cancelledCount =
                0;

        for (Appointment appointment
                : allAppointments) {

            if (appointment.getStatus()
                    == AppointmentStatus.SCHEDULED) {

                scheduledCount++;

            } else if (appointment.getStatus()
                    == AppointmentStatus.CANCELLED) {

                cancelledCount++;
            }
        }

        String selectedStatus =
                request.getParameter(
                        "status"
                );

        if (!"SCHEDULED".equals(
                    selectedStatus)
                && !"CANCELLED".equals(
                        selectedStatus)) {

            selectedStatus =
                    "ALL";
        }

        List<Appointment> displayedAppointments =
                new ArrayList<>();

        for (Appointment appointment
                : allAppointments) {

            if ("ALL".equals(
                    selectedStatus)
                    || appointment
                            .getStatus()
                            .name()
                            .equals(
                                    selectedStatus
                            )) {

                displayedAppointments.add(
                        appointment
                );
            }
        }

        request.setAttribute(
                "appointments",
                displayedAppointments
        );

        request.setAttribute(
                "scheduledCount",
                scheduledCount
        );

        request.setAttribute(
                "cancelledCount",
                cancelledCount
        );

        request.setAttribute(
                "selectedStatus",
                selectedStatus
        );

        request.getRequestDispatcher(
                "searchAppointment.jsp"
        ).forward(
                request,
                response
        );
    }

    private void showInvalidAppointmentDetails(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException,
                   IOException {

        request.setAttribute(
                "errorMessage",
                "Invalid appointment details."
        );

        showRegisterAppointmentPage(
                request,
                response
        );
    }

    private boolean isValidAppointmentNo(
            String appointmentNo) {

        try {

            return Integer.parseInt(
                    appointmentNo
            ) > 0;

        } catch (NumberFormatException e) {

            return false;
        }
    }

    private boolean isStaff(
            HttpSession session) {

        return "STAFF".equals(
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