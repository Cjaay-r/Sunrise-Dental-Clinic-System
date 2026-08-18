package sunrisedentalsystem.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import sunrisedentalsystem.model.Appointment;

public interface AppointmentService {

    Appointment registerAppointment(
            Appointment appointment,
            int staffId) throws SQLException;

    Appointment searchAppointment(
            String appointmentNo) throws SQLException;

    boolean checkAvailability(
            int dentistId,
            LocalDate appointmentDate,
            LocalTime appointmentTime) throws SQLException;
}