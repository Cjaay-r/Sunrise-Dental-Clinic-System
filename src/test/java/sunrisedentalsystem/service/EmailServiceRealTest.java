package sunrisedentalsystem.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import sunrisedentalsystem.model.Appointment;
import sunrisedentalsystem.model.Dentist;
import sunrisedentalsystem.model.Patient;
import sunrisedentalsystem.model.Treatment;

@EnabledIfSystemProperty(
        named = "runRealEmail",
        matches = "true"
)
class EmailServiceRealTest {

    @Test
    void shouldSendRealAppointmentConfirmationEmail()
            throws Exception {

        Properties configuration =
                new Properties();

        try (InputStream inputStream =
                     getClass()
                             .getClassLoader()
                             .getResourceAsStream(
                                     "email.properties"
                             )) {

            assertNotNull(
                    inputStream
            );

            configuration.load(
                    inputStream
            );
        }

        String recipient =
                configuration.getProperty(
                        "mail.username"
                );

        assertNotNull(
                recipient
        );

        Patient patient =
                mock(Patient.class);

        Dentist dentist =
                mock(Dentist.class);

        Treatment treatment =
                mock(Treatment.class);

        Appointment appointment =
                mock(Appointment.class);

        when(patient.getPatientName())
                .thenReturn(
                        "Test Patient"
                );

        when(patient.getEmail())
                .thenReturn(
                        recipient
                );

        when(dentist.getDentistName())
                .thenReturn(
                        "Dr. Nimal Perera"
                );

        when(treatment.getTreatmentType())
                .thenReturn(
                        "Dental Filling"
                );

        when(appointment.getAppointmentNo())
                .thenReturn(
                        "TEST-001"
                );

        when(appointment.getAppointmentDate())
                .thenReturn(
                        LocalDate.now()
                                .plusDays(1)
                );

        when(appointment.getAppointmentTime())
                .thenReturn(
                        LocalTime.of(
                                10,
                                30
                        )
                );

        when(appointment.getPatient())
                .thenReturn(
                        patient
                );

        when(appointment.getDentist())
                .thenReturn(
                        dentist
                );

        when(appointment.getTreatment())
                .thenReturn(
                        treatment
                );

        EmailService emailService =
                new EmailServiceImpl();

        boolean result =
                emailService
                        .sendAppointmentConfirmation(
                                appointment
                        );

        assertTrue(
                result
        );
    }
}