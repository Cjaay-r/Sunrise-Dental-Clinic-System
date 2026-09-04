package sunrisedentalsystem.service;

import sunrisedentalsystem.model.Appointment;

public interface EmailService {

    boolean sendAppointmentConfirmation(
            Appointment appointment);

    boolean sendAppointmentCancellation(
            Appointment appointment);
}