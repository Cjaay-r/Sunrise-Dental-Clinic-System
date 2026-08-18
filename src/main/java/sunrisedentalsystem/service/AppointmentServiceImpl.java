package sunrisedentalsystem.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import sunrisedentalsystem.dao.AppointmentDAO;
import sunrisedentalsystem.model.Appointment;

public class AppointmentServiceImpl
        implements AppointmentService {

    private final AppointmentDAO appointmentDAO;

    public AppointmentServiceImpl(
            AppointmentDAO appointmentDAO) {

        this.appointmentDAO = appointmentDAO;
    }

    @Override
    public Appointment registerAppointment(
            Appointment appointment,
            int staffId)
            throws SQLException {

        appointmentDAO.addAppointment(
                appointment,
                staffId);

        return appointment;
    }

    @Override
    public Appointment searchAppointment(
            String appointmentNo)
            throws SQLException {

        return appointmentDAO
                .getAppointmentByNumber(
                        appointmentNo);
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
                        appointmentTime);
    }
}