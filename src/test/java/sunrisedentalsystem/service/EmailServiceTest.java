package sunrisedentalsystem.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Transport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import sunrisedentalsystem.model.Appointment;
import sunrisedentalsystem.model.Dentist;
import sunrisedentalsystem.model.Patient;
import sunrisedentalsystem.model.Treatment;

class EmailServiceTest {

    private Appointment appointment;
    private Patient patient;
    private Dentist dentist;
    private Treatment treatment;
    private EmailService emailService;

    @BeforeEach
    void setUp() {

        appointment =
                mock(Appointment.class);

        patient =
                mock(Patient.class);

        dentist =
                mock(Dentist.class);

        treatment =
                mock(Treatment.class);

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

        when(appointment.getAppointmentNo())
                .thenReturn(
                        "8"
                );

        when(appointment.getAppointmentDate())
                .thenReturn(
                        LocalDate.of(
                                2026,
                                9,
                                16
                        )
                );

        when(appointment.getAppointmentTime())
                .thenReturn(
                        LocalTime.of(
                                7,
                                1
                        )
                );

        when(patient.getPatientName())
                .thenReturn(
                        "Nuwan"
                );

        when(patient.getEmail())
                .thenReturn(
                        "nuwan@example.com"
                );

        when(dentist.getDentistName())
                .thenReturn(
                        "Dr. Nimal Perera"
                );

        when(treatment.getTreatmentType())
                .thenReturn(
                        "Dental Filling"
                );

        emailService =
                new EmailServiceImpl(
                        createConfiguration()
                );
    }

    @Test
    void shouldSendAppointmentConfirmationEmail() {

        try (MockedStatic<Transport> transport =
                     mockStatic(
                             Transport.class
                     )) {

            boolean result =
                    emailService
                            .sendAppointmentConfirmation(
                                    appointment
                            );

            assertTrue(
                    result
            );

            transport.verify(
                    () ->
                            Transport.send(
                                    org.mockito.ArgumentMatchers
                                            .any(
                                                    Message.class
                                            )
                            )
            );
        }
    }

    @Test
    void shouldSendAppointmentCancellationEmail() {

        try (MockedStatic<Transport> transport =
                     mockStatic(
                             Transport.class
                     )) {

            boolean result =
                    emailService
                            .sendAppointmentCancellation(
                                    appointment
                            );

            assertTrue(
                    result
            );

            transport.verify(
                    () ->
                            Transport.send(
                                    org.mockito.ArgumentMatchers
                                            .any(
                                                    Message.class
                                            )
                            )
            );
        }
    }

    @Test
    void shouldNotSendEmailWhenPatientEmailIsMissing() {

        when(patient.getEmail())
                .thenReturn(null);

        try (MockedStatic<Transport> transport =
                     mockStatic(
                             Transport.class
                     )) {

            boolean result =
                    emailService
                            .sendAppointmentConfirmation(
                                    appointment
                            );

            assertFalse(
                    result
            );

            transport.verify(
                    () ->
                            Transport.send(
                                    org.mockito.ArgumentMatchers
                                            .any(
                                                    Message.class
                                            )
                            ),
                    never()
            );
        }
    }

    @Test
    void shouldNotSendEmailWhenAppointmentIsMissing() {

        try (MockedStatic<Transport> transport =
                     mockStatic(
                             Transport.class
                     )) {

            boolean result =
                    emailService
                            .sendAppointmentConfirmation(
                                    null
                            );

            assertFalse(
                    result
            );

            transport.verify(
                    () ->
                            Transport.send(
                                    org.mockito.ArgumentMatchers
                                            .any(
                                                    Message.class
                                            )
                            ),
                    never()
            );
        }
    }

    @Test
    void shouldNotSendEmailWhenConfigurationIsMissing() {

        EmailService unconfiguredService =
                new EmailServiceImpl(
                        new Properties()
                );

        try (MockedStatic<Transport> transport =
                     mockStatic(
                             Transport.class
                     )) {

            boolean result =
                    unconfiguredService
                            .sendAppointmentConfirmation(
                                    appointment
                            );

            assertFalse(
                    result
            );

            transport.verify(
                    () ->
                            Transport.send(
                                    org.mockito.ArgumentMatchers
                                            .any(
                                                    Message.class
                                            )
                            ),
                    never()
            );
        }
    }

    private Properties createConfiguration() {

        Properties properties =
                new Properties();

        properties.setProperty(
                "mail.host",
                "smtp.gmail.com"
        );

        properties.setProperty(
                "mail.port",
                "587"
        );

        properties.setProperty(
                "mail.username",
                "clinic@example.com"
        );

        properties.setProperty(
                "mail.password",
                "test-password"
        );

        properties.setProperty(
                "mail.from",
                "clinic@example.com"
        );

        return properties;
    }
}