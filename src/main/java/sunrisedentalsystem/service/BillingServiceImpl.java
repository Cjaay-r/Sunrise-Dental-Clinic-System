package sunrisedentalsystem.service;

import java.sql.SQLException;
import java.util.List;

import sunrisedentalsystem.dao.BillDAO;
import sunrisedentalsystem.model.Bill;

public class BillingServiceImpl
        implements BillingService {

    private final BillDAO billDAO;

    public BillingServiceImpl(BillDAO billDAO) {
        this.billDAO = billDAO;
    }

    @Override
    public Bill calculateAndSaveBill(Bill bill)
            throws SQLException {

        bill.calculateTotal();

        billDAO.addBill(bill);

        return bill;
    }

    @Override
    public Bill getBillByAppointmentNo(
            int appointmentNo)
            throws SQLException {

        return billDAO
                .getBillByAppointmentNo(
                        appointmentNo);
    }

    @Override
    public List<Bill> getAllBills()
            throws SQLException {

        return billDAO.getAllBills();
    }
}