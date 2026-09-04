package sunrisedentalsystem.dao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import sunrisedentalsystem.model.Appointment;

public interface AppointmentDAO {

    boolean addAppointment(
            Appointment appointment,
            int userId)
            throws SQLException;

    Appointment getAppointmentByNumber(
            String appointmentNo)
            throws SQLException;

    List<Appointment> getAllAppointments()
            throws SQLException;

    boolean isAppointmentSlotAvailable(
            int dentistId,
            LocalDate appointmentDate,
            LocalTime appointmentTime)
            throws SQLException;

    boolean cancelAppointment(
            String appointmentNo)
            throws SQLException;
}