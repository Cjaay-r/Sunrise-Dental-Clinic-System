package sunrisedentalsystem.service;

import java.sql.SQLException;
import java.util.List;

import sunrisedentalsystem.model.Appointment;
import sunrisedentalsystem.model.Bill;

public interface BillingService {

    double CONSULTATION_FEE =
            2500.00;

    Bill calculateAndSaveBill(
            Bill bill)
            throws SQLException;

    Bill generateBill(
            int appointmentNo,
            int userId)
            throws SQLException;

    Appointment getAppointmentForBilling(
            int appointmentNo)
            throws SQLException;

    Bill getBillByAppointmentNo(
            int appointmentNo)
            throws SQLException;

    List<Bill> getAllBills()
            throws SQLException;
}