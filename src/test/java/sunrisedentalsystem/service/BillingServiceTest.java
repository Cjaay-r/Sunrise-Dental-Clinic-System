package sunrisedentalsystem.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sunrisedentalsystem.dao.BillDAO;
import sunrisedentalsystem.model.Bill;

class BillingServiceTest {

    private BillDAO billDAO;
    private BillingService billingService;

    @BeforeEach
    void setUp() {

        billDAO = mock(BillDAO.class);

        billingService =
                new BillingServiceImpl(billDAO);
    }

    @Test
    void shouldCalculateAndSaveBillThroughDAO()
            throws Exception {

        Bill bill = mock(Bill.class);

        Bill result =
                billingService
                        .calculateAndSaveBill(bill);

        assertSame(bill, result);

        verify(bill).calculateTotal();

        verify(billDAO).addBill(bill);
    }

    @Test
    void shouldGetBillByAppointmentNumber()
            throws Exception {

        int appointmentNo = 1;

        Bill expectedBill =
                mock(Bill.class);

        when(billDAO
                .getBillByAppointmentNo(
                        appointmentNo))
                .thenReturn(expectedBill);

        Bill result =
                billingService
                        .getBillByAppointmentNo(
                                appointmentNo);

        assertSame(expectedBill, result);

        verify(billDAO)
                .getBillByAppointmentNo(
                        appointmentNo);
    }

    @Test
    void shouldGetAllBillsFromDAO()
            throws Exception {

        Bill bill1 = mock(Bill.class);
        Bill bill2 = mock(Bill.class);

        List<Bill> expectedBills =
                Arrays.asList(bill1, bill2);

        when(billDAO.getAllBills())
                .thenReturn(expectedBills);

        List<Bill> result =
                billingService.getAllBills();

        assertSame(expectedBills, result);

        verify(billDAO).getAllBills();
    }
}