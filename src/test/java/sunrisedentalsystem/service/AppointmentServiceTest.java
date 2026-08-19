package sunrisedentalsystem.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sunrisedentalsystem.dao.AppointmentDAO;
import sunrisedentalsystem.model.Appointment;

class AppointmentServiceTest {

    private AppointmentDAO appointmentDAO;
    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {

        appointmentDAO = mock(AppointmentDAO.class);

        appointmentService =
                new AppointmentServiceImpl(appointmentDAO);
    }

    @Test
    void shouldRegisterAppointmentThroughDAO()
            throws Exception {

        Appointment appointment =
                mock(Appointment.class);

        int staffId = 1;

        Appointment result =
                appointmentService.registerAppointment(
                        appointment,
                        staffId);

        assertSame(appointment, result);

        verify(appointmentDAO)
                .addAppointment(
                        appointment,
                        staffId);
    }

    @Test
    void shouldSearchAppointmentByAppointmentNumber()
            throws Exception {

        String appointmentNo = "APT001";

        Appointment expectedAppointment =
                mock(Appointment.class);

        when(appointmentDAO
                .getAppointmentByNumber(appointmentNo))
                .thenReturn(expectedAppointment);

        Appointment result =
                appointmentService
                        .searchAppointment(appointmentNo);

        assertSame(expectedAppointment, result);

        verify(appointmentDAO)
                .getAppointmentByNumber(appointmentNo);
    }

    @Test
    void shouldCheckAppointmentSlotAvailability()
            throws Exception {

        int dentistId = 1;

        LocalDate appointmentDate =
                LocalDate.of(2026, 8, 20);

        LocalTime appointmentTime =
                LocalTime.of(10, 30);

        when(appointmentDAO
                .isAppointmentSlotAvailable(
                        dentistId,
                        appointmentDate,
                        appointmentTime))
                .thenReturn(true);

        boolean result =
                appointmentService.checkAvailability(
                        dentistId,
                        appointmentDate,
                        appointmentTime);

        assertTrue(result);

        verify(appointmentDAO)
                .isAppointmentSlotAvailable(
                        dentistId,
                        appointmentDate,
                        appointmentTime);
    }
}