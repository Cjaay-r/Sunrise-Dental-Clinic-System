package sunrisedentalsystem.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sunrisedentalsystem.model.Bill;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BillDAOTest {

    private BillDAO billDAO;

    @BeforeEach
    void setUp() {
        billDAO = new BillDAOImpl();
    }

    @Test
    void testAddBill() throws SQLException {

        Bill bill = new Bill(
                0,
                5,
                2,
                1000.00,
                5000.00,
                LocalDate.now()
        );

        billDAO.addBill(bill);

        assertTrue(bill.getBillId() > 0);
    }

    @Test
    void testGetBillById() throws SQLException {

        Bill bill = new Bill(
                0,
                6,
                2,
                1000.00,
                2500.00,
                LocalDate.now()
        );

        billDAO.addBill(bill);

        Bill retrievedBill =
                billDAO.getBillById(bill.getBillId());

        assertNotNull(retrievedBill);
        assertEquals(bill.getBillId(), retrievedBill.getBillId());
    }

    @Test
    void testGetBillByAppointmentNo() throws SQLException {

        Bill bill = new Bill(
                0,
                7,
                2,
                1000.00,
                3000.00,
                LocalDate.now()
        );

        billDAO.addBill(bill);

        Bill retrievedBill =
                billDAO.getBillByAppointmentNo(7);

        assertNotNull(retrievedBill);
        assertEquals(7, retrievedBill.getAppointmentNo());
    }

    @Test
    void testGetAllBills() throws SQLException {

        Bill bill = new Bill(
                0,
                8,
                2,
                1000.00,
                4000.00,
                LocalDate.now()
        );

        billDAO.addBill(bill);

        List<Bill> bills = billDAO.getAllBills();

        assertNotNull(bills);
        assertFalse(bills.isEmpty());

        boolean billFound = bills.stream()
                .anyMatch(b -> b.getBillId() == bill.getBillId());

        assertTrue(billFound);
    }
}