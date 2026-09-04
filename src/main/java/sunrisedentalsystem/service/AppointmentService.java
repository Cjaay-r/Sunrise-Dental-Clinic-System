package sunrisedentalsystem.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import sunrisedentalsystem.model.Appointment;

public interface AppointmentService {

    Appointment registerAppointment(
            Appointment appointment,
            int userId)
            throws SQLException;

    Appointment searchAppointment(
            String appointmentNo)
            throws SQLException;

    List<Appointment> getAllAppointments()
            throws SQLException;

    boolean checkAvailability(
            int dentistId,
            LocalDate appointmentDate,
            LocalTime appointmentTime)
            throws SQLException;

    boolean cancelAppointment(
            String appointmentNo)
            throws SQLException;
}