package sunrisedentalsystem.dao;

import java.sql.SQLException;
import java.util.List;

import sunrisedentalsystem.model.Bill;

public interface BillDAO {

    void addBill(Bill bill)
            throws SQLException;

    void addBill(
            Bill bill,
            int userId)
            throws SQLException;

    Bill getBillById(
            int billId)
            throws SQLException;

    Bill getBillByAppointmentNo(
            int appointmentNo)
            throws SQLException;

    List<Bill> getAllBills()
            throws SQLException;
}