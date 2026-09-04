package sunrisedentalsystem.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import sunrisedentalsystem.model.Appointment;
import sunrisedentalsystem.model.Patient;

public class EmailServiceImpl
        implements EmailService {

    private final Properties mailProperties;

    private final String username;

    private final String password;

    private final String from;

    private final boolean configured;

    public EmailServiceImpl() {

        Properties configuration =
                loadConfiguration();

        username =
                configuration.getProperty(
                        "mail.username"
                );

        password =
                configuration.getProperty(
                        "mail.password"
                );

        from =
                configuration.getProperty(
                        "mail.from"
                );

        String host =
                configuration.getProperty(
                        "mail.host"
                );

        String port =
                configuration.getProperty(
                        "mail.port"
                );

        configured =
                isPresent(host)
                && isPresent(port)
                && isPresent(username)
                && isPresent(password)
                && isPresent(from);

        mailProperties =
                createMailProperties(
                        host,
                        port
                );
    }

    EmailServiceImpl(
            Properties configuration) {

        username =
                configuration.getProperty(
                        "mail.username"
                );

        password =
                configuration.getProperty(
                        "mail.password"
                );

        from =
                configuration.getProperty(
                        "mail.from"
                );

        String host =
                configuration.getProperty(
                        "mail.host"
                );

        String port =
                configuration.getProperty(
                        "mail.port"
                );

        configured =
                isPresent(host)
                && isPresent(port)
                && isPresent(username)
                && isPresent(password)
                && isPresent(from);

        mailProperties =
                createMailProperties(
                        host,
                        port
                );
    }

    @Override
    public boolean sendAppointmentConfirmation(
            Appointment appointment) {

        if (!canSendToPatient(
                appointment)) {

            return false;
        }

        Patient patient =
                appointment.getPatient();

        String subject =
                "Sunrise Dental Clinic - Appointment Confirmation";

        String body =
                "Dear "
                + patient.getPatientName()
                + ",\n\n"
                + "Your dental appointment has been confirmed.\n\n"
                + "Appointment Number: #"
                + appointment.getAppointmentNo()
                + "\n"
                + "Dentist: "
                + appointment
                        .getDentist()
                        .getDentistName()
                + "\n"
                + "Treatment: "
                + appointment
                        .getTreatment()
                        .getTreatmentType()
                + "\n"
                + "Date: "
                + appointment.getAppointmentDate()
                + "\n"
                + "Time: "
                + appointment.getAppointmentTime()
                + "\n\n"
                + "Please arrive on time for your appointment.\n\n"
                + "Sunrise Dental Clinic";

        return sendEmail(
                patient.getEmail(),
                subject,
                body
        );
    }

    @Override
    public boolean sendAppointmentCancellation(
            Appointment appointment) {

        if (!canSendToPatient(
                appointment)) {

            return false;
        }

        Patient patient =
                appointment.getPatient();

        String subject =
                "Sunrise Dental Clinic - Appointment Cancellation";

        String body =
                "Dear "
                + patient.getPatientName()
                + ",\n\n"
                + "Your dental appointment has been cancelled.\n\n"
                + "Appointment Number: #"
                + appointment.getAppointmentNo()
                + "\n"
                + "Dentist: "
                + appointment
                        .getDentist()
                        .getDentistName()
                + "\n"
                + "Treatment: "
                + appointment
                        .getTreatment()
                        .getTreatmentType()
                + "\n"
                + "Date: "
                + appointment.getAppointmentDate()
                + "\n"
                + "Time: "
                + appointment.getAppointmentTime()
                + "\n\n"
                + "Please contact Sunrise Dental Clinic "
                + "if you need another appointment.\n\n"
                + "Sunrise Dental Clinic";

        return sendEmail(
                patient.getEmail(),
                subject,
                body
        );
    }

    private boolean sendEmail(
            String recipient,
            String subject,
            String body) {

        if (!configured
                || !isPresent(recipient)) {

            return false;
        }

        try {

            Session session =
                    Session.getInstance(
                            mailProperties,
                            new Authenticator() {

                                @Override
                                protected PasswordAuthentication
                                        getPasswordAuthentication() {

                                    return new PasswordAuthentication(
                                            username,
                                            password
                                    );
                                }
                            }
                    );

            Message message =
                    new MimeMessage(
                            session
                    );

            message.setFrom(
                    new InternetAddress(
                            from
                    )
            );

            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(
                            recipient
                    )
            );

            message.setSubject(
                    subject
            );

            message.setText(
                    body
            );

            Transport.send(
                    message
            );

            return true;

        } catch (MessagingException e) {

            return false;
        }
    }

    private boolean canSendToPatient(
            Appointment appointment) {

        if (appointment == null
                || appointment.getPatient() == null) {

            return false;
        }

        return isPresent(
                appointment
                        .getPatient()
                        .getEmail()
        );
    }

    private Properties createMailProperties(
            String host,
            String port) {

        Properties properties =
                new Properties();

        if (host != null) {

            properties.put(
                    "mail.smtp.host",
                    host
            );
        }

        if (port != null) {

            properties.put(
                    "mail.smtp.port",
                    port
            );
        }

        properties.put(
                "mail.smtp.auth",
                "true"
        );

        properties.put(
                "mail.smtp.starttls.enable",
                "true"
        );

        properties.put(
                "mail.smtp.connectiontimeout",
                "5000"
        );

        properties.put(
                "mail.smtp.timeout",
                "5000"
        );

        properties.put(
                "mail.smtp.writetimeout",
                "5000"
        );

        return properties;
    }

    private Properties loadConfiguration() {

        Properties properties =
                new Properties();

        try (InputStream inputStream =
                     getClass()
                             .getClassLoader()
                             .getResourceAsStream(
                                     "email.properties"
                             )) {

            if (inputStream != null) {

                properties.load(
                        inputStream
                );
            }

        } catch (IOException e) {

            return new Properties();
        }

        return properties;
    }

    private boolean isPresent(
            String value) {

        return value != null
                && !value.trim().isEmpty();
    }
}