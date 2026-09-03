package sunrisedentalsystem.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import sunrisedentalsystem.dao.AppointmentDAO;
import sunrisedentalsystem.model.Appointment;

public class AppointmentServiceImpl
        implements AppointmentService {

    private final AppointmentDAO appointmentDAO;

    public AppointmentServiceImpl(
            AppointmentDAO appointmentDAO) {

        this.appointmentDAO =
                appointmentDAO;
    }

    @Override
    public Appointment registerAppointment(
            Appointment appointment,
            int userId)
            throws SQLException {

        appointmentDAO.addAppointment(
                appointment,
                userId
        );

        return appointment;
    }

    @Override
    public Appointment searchAppointment(
            String appointmentNo)
            throws SQLException {

        return appointmentDAO
                .getAppointmentByNumber(
                        appointmentNo
                );
    }

    @Override
    public List<Appointment> getAllAppointments()
            throws SQLException {

        return appointmentDAO
                .getAllAppointments();
    }

    @Override
    public boolean checkAvailability(
            int dentistId,
            LocalDate appointmentDate,
            LocalTime appointmentTime)
            throws SQLException {

        return appointmentDAO
                .isAppointmentSlotAvailable(
                        dentistId,
                        appointmentDate,
                        appointmentTime
                );
    }

    @Override
    public boolean cancelAppointment(
            String appointmentNo)
            throws SQLException {

        return appointmentDAO
                .cancelAppointment(
                        appointmentNo
                );
    }
}