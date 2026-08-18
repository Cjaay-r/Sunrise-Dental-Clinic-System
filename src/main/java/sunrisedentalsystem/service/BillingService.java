package sunrisedentalsystem.service;

import java.sql.SQLException;
import java.util.List;

import sunrisedentalsystem.model.Bill;

public interface BillingService {

    Bill calculateAndSaveBill(Bill bill) throws SQLException;

    Bill getBillByAppointmentNo(int appointmentNo)
            throws SQLException;

    List<Bill> getAllBills() throws SQLException;
}