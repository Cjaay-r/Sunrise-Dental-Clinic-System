package sunrisedentalsystem.dao;

import sunrisedentalsystem.model.Bill;

import java.sql.SQLException;
import java.util.List;

public interface BillDAO {

    void addBill(Bill bill) throws SQLException;

    Bill getBillById(int billId) throws SQLException;

    Bill getBillByAppointmentNo(int appointmentNo) throws SQLException;

    List<Bill> getAllBills() throws SQLException;
}