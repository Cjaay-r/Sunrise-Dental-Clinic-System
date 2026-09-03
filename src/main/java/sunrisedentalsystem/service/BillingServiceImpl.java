package sunrisedentalsystem.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import sunrisedentalsystem.dao.AppointmentDAO;
import sunrisedentalsystem.dao.AppointmentDAOImpl;
import sunrisedentalsystem.dao.BillDAO;
import sunrisedentalsystem.model.Appointment;
import sunrisedentalsystem.model.AppointmentStatus;
import sunrisedentalsystem.model.Bill;

public class BillingServiceImpl
        implements BillingService {

    private final BillDAO billDAO;

    private final AppointmentDAO appointmentDAO;

    public BillingServiceImpl(
            BillDAO billDAO) {

        this(
                billDAO,
                new AppointmentDAOImpl()
        );
    }

    public BillingServiceImpl(
            BillDAO billDAO,
            AppointmentDAO appointmentDAO) {

        this.billDAO =
                billDAO;

        this.appointmentDAO =
                appointmentDAO;
    }

    @Override
    public Bill calculateAndSaveBill(
            Bill bill)
            throws SQLException {

        bill.calculateTotal();

        billDAO.addBill(
                bill
        );

        return bill;
    }

    @Override
    public Bill generateBill(
            int appointmentNo,
            int userId)
            throws SQLException {

        Bill existingBill =
                billDAO
                        .getBillByAppointmentNo(
                                appointmentNo
                        );

        if (existingBill != null) {

            throw new IllegalStateException(
                    "A bill has already been generated for this appointment."
            );
        }

        Appointment appointment =
                appointmentDAO
                        .getAppointmentByNumber(
                                String.valueOf(
                                        appointmentNo
                                )
                        );

        if (appointment == null) {

            throw new IllegalStateException(
                    "Appointment not found."
            );
        }

        if (appointment.getStatus()
                == AppointmentStatus.CANCELLED) {

            throw new IllegalStateException(
                    "Cancelled appointments cannot be billed."
            );
        }

        if (appointment.getTreatment()
                == null) {

            throw new IllegalStateException(
                    "Treatment information is unavailable."
            );
        }

        double treatmentCost =
                appointment
                        .getTreatment()
                        .getTreatmentPrice();

        Bill bill =
                new Bill(
                        0,
                        appointmentNo,
                        0,
                        CONSULTATION_FEE,
                        treatmentCost,
                        LocalDate.now()
                );

        billDAO.addBill(
                bill,
                userId
        );

        Bill savedBill =
                billDAO
                        .getBillByAppointmentNo(
                                appointmentNo
                        );

        if (savedBill != null) {

            return savedBill;
        }

        return bill;
    }

    @Override
    public Appointment getAppointmentForBilling(
            int appointmentNo)
            throws SQLException {

        return appointmentDAO
                .getAppointmentByNumber(
                        String.valueOf(
                                appointmentNo
                        )
                );
    }

    @Override
    public Bill getBillByAppointmentNo(
            int appointmentNo)
            throws SQLException {

        return billDAO
                .getBillByAppointmentNo(
                        appointmentNo
                );
    }

    @Override
    public List<Bill> getAllBills()
            throws SQLException {

        return billDAO
                .getAllBills();
    }
}