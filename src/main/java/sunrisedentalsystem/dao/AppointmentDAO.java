package sunrisedentalsystem.dao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import sunrisedentalsystem.model.Appointment;

public interface AppointmentDAO {

    boolean addAppointment(Appointment appointment, int staffId)
            throws SQLException;

    Appointment getAppointmentByNumber(String appointmentNo)
            throws SQLException;

    boolean isAppointmentSlotAvailable(
            int dentistId,
            LocalDate appointmentDate,
            LocalTime appointmentTime)
            throws SQLException;
}