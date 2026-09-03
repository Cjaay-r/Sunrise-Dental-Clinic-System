package sunrisedentalsystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import sunrisedentalsystem.dao.AppointmentDAO;
import sunrisedentalsystem.dao.BillDAO;
import sunrisedentalsystem.model.Appointment;
import sunrisedentalsystem.model.AppointmentStatus;
import sunrisedentalsystem.model.Bill;
import sunrisedentalsystem.model.Dentist;
import sunrisedentalsystem.model.Patient;
import sunrisedentalsystem.model.Treatment;

class BillingServiceTest {

    private BillDAO billDAO;

    private AppointmentDAO appointmentDAO;

    private BillingService billingService;

    @BeforeEach
    void setUp() {

        billDAO =
                mock(BillDAO.class);

        appointmentDAO =
                mock(AppointmentDAO.class);

        billingService =
                new BillingServiceImpl(
                        billDAO,
                        appointmentDAO
                );
    }

    @Test
    void shouldCalculateAndSaveBillThroughDAO()
            throws Exception {

        Bill bill =
                mock(Bill.class);

        Bill result =
                billingService
                        .calculateAndSaveBill(
                                bill
                        );

        assertSame(
                bill,
                result
        );

        verify(bill)
                .calculateTotal();

        verify(billDAO)
                .addBill(
                        bill
                );
    }

    @Test
    void shouldGenerateBillUsingAppointmentTreatmentPrice()
            throws Exception {

        int appointmentNo =
                10;

        int userId =
                2;

        Appointment appointment =
                createAppointment(
                        AppointmentStatus.SCHEDULED
                );

        Bill savedBill =
                new Bill(
                        5,
                        appointmentNo,
                        2,
                        2500.00,
                        5000.00,
                        java.time.LocalDate.now()
                );

        when(billDAO
                .getBillByAppointmentNo(
                        appointmentNo
                ))
                .thenReturn(
                        null,
                        savedBill
                );

        when(appointmentDAO
                .getAppointmentByNumber(
                        "10"
                ))
                .thenReturn(
                        appointment
                );

        Bill result =
                billingService
                        .generateBill(
                                appointmentNo,
                                userId
                        );

        ArgumentCaptor<Bill> captor =
                ArgumentCaptor.forClass(
                        Bill.class
                );

        verify(billDAO)
                .addBill(
                        captor.capture(),
                        eq(userId)
                );

        Bill generatedBill =
                captor.getValue();

        assertEquals(
                10,
                generatedBill
                        .getAppointmentNo()
        );

        assertEquals(
                2500.00,
                generatedBill
                        .getConsultationFee()
        );

        assertEquals(
                5000.00,
                generatedBill
                        .getTreatmentCost()
        );

        assertEquals(
                7500.00,
                generatedBill
                        .getTotalAmount()
        );

        assertNotNull(
                generatedBill
                        .getGeneratedDate()
        );

        assertSame(
                savedBill,
                result
        );
    }

    @Test
    void shouldRejectDuplicateBill()
            throws Exception {

        Bill existingBill =
                mock(Bill.class);

        when(billDAO
                .getBillByAppointmentNo(
                        10
                ))
                .thenReturn(
                        existingBill
                );

        IllegalStateException exception =
                assertThrows(
                        IllegalStateException.class,
                        () ->
                                billingService
                                        .generateBill(
                                                10,
                                                2
                                        )
                );

        assertEquals(
                "A bill has already been generated for this appointment.",
                exception.getMessage()
        );

        verifyNoInteractions(
                appointmentDAO
        );

        verify(billDAO, never())
                .addBill(
                        any(Bill.class),
                        eq(2)
                );
    }

    @Test
    void shouldRejectBillWhenAppointmentDoesNotExist()
            throws Exception {

        when(billDAO
                .getBillByAppointmentNo(
                        10
                ))
                .thenReturn(null);

        when(appointmentDAO
                .getAppointmentByNumber(
                        "10"
                ))
                .thenReturn(null);

        IllegalStateException exception =
                assertThrows(
                        IllegalStateException.class,
                        () ->
                                billingService
                                        .generateBill(
                                                10,
                                                2
                                        )
                );

        assertEquals(
                "Appointment not found.",
                exception.getMessage()
        );

        verify(billDAO, never())
                .addBill(
                        any(Bill.class),
                        eq(2)
                );
    }

    @Test
    void shouldRejectCancelledAppointment()
            throws Exception {

        Appointment appointment =
                createAppointment(
                        AppointmentStatus.CANCELLED
                );

        when(billDAO
                .getBillByAppointmentNo(
                        10
                ))
                .thenReturn(null);

        when(appointmentDAO
                .getAppointmentByNumber(
                        "10"
                ))
                .thenReturn(
                        appointment
                );

        IllegalStateException exception =
                assertThrows(
                        IllegalStateException.class,
                        () ->
                                billingService
                                        .generateBill(
                                                10,
                                                2
                                        )
                );

        assertEquals(
                "Cancelled appointments cannot be billed.",
                exception.getMessage()
        );

        verify(billDAO, never())
                .addBill(
                        any(Bill.class),
                        eq(2)
                );
    }

    @Test
    void shouldGetAppointmentForBilling()
            throws Exception {

        Appointment appointment =
                createAppointment(
                        AppointmentStatus.SCHEDULED
                );

        when(appointmentDAO
                .getAppointmentByNumber(
                        "10"
                ))
                .thenReturn(
                        appointment
                );

        Appointment result =
                billingService
                        .getAppointmentForBilling(
                                10
                        );

        assertSame(
                appointment,
                result
        );

        verify(appointmentDAO)
                .getAppointmentByNumber(
                        "10"
                );
    }

    @Test
    void shouldGetBillByAppointmentNumber()
            throws Exception {

        Bill expectedBill =
                mock(Bill.class);

        when(billDAO
                .getBillByAppointmentNo(
                        10
                ))
                .thenReturn(
                        expectedBill
                );

        Bill result =
                billingService
                        .getBillByAppointmentNo(
                                10
                        );

        assertSame(
                expectedBill,
                result
        );

        verify(billDAO)
                .getBillByAppointmentNo(
                        10
                );
    }

    @Test
    void shouldGetAllBillsFromDAO()
            throws Exception {

        List<Bill> expectedBills =
                List.of(
                        mock(Bill.class),
                        mock(Bill.class)
                );

        when(billDAO
                .getAllBills())
                .thenReturn(
                        expectedBills
                );

        List<Bill> result =
                billingService
                        .getAllBills();

        assertSame(
                expectedBills,
                result
        );

        verify(billDAO)
                .getAllBills();
    }

    private Appointment createAppointment(
            AppointmentStatus status) {

        Patient patient =
                new Patient(
                        1,
                        "Test Patient",
                        "Colombo",
                        "0771234567"
                );

        Dentist dentist =
                new Dentist(
                        2,
                        "Dr. Silva"
                );

        Treatment treatment =
                new Treatment(
                        3,
                        "Cleaning",
                        5000.00
                );

        return new Appointment(
                "10",
                java.time.LocalDate.now(),
                java.time.LocalTime.of(
                        10,
                        30
                ),
                status,
                patient,
                dentist,
                treatment
        );
    }
}