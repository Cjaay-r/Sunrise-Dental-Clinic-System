package sunrisedentalsystem.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sunrisedentalsystem.model.Bill;
import sunrisedentalsystem.util.DatabaseConnection;

class BillDAOTest {

    private BillDAO billDAO;

    private int patientId;
    private int dentistId;
    private int treatmentId;
    private int appointmentNo;

    @BeforeEach
    void setUp() throws Exception {

        billDAO = new BillDAOImpl();

        createTestPatient();
        createTestDentist();
        createTestTreatment();
        createTestAppointment();
    }

    @AfterEach
    void cleanUp() throws Exception {

        try (Connection connection =
                DatabaseConnection.getConnection()) {

            // Delete bill first because it depends on appointment
            try (PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM bill WHERE appointment_no = ?")) {

                ps.setInt(1, appointmentNo);
                ps.executeUpdate();
            }

            // Delete appointment
            try (PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM appointment WHERE appointment_no = ?")) {

                ps.setInt(1, appointmentNo);
                ps.executeUpdate();
            }

            // Delete treatment
            try (PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM treatment WHERE treatment_id = ?")) {

                ps.setInt(1, treatmentId);
                ps.executeUpdate();
            }

            // Delete dentist
            try (PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM dentist WHERE dentist_id = ?")) {

                ps.setInt(1, dentistId);
                ps.executeUpdate();
            }

            // Delete patient
            try (PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM patient WHERE patient_id = ?")) {

                ps.setInt(1, patientId);
                ps.executeUpdate();
            }
        }
    }

    @Test
    void testAddBill() throws Exception {

        Bill bill = new Bill(
                0,
                appointmentNo,
                2,
                1000.00,
                5000.00,
                LocalDate.now()
        );

        billDAO.addBill(bill);

        assertTrue(bill.getBillId() > 0);
    }

    @Test
    void testGetBillById() throws Exception {

        Bill bill = new Bill(
                0,
                appointmentNo,
                2,
                1000.00,
                2500.00,
                LocalDate.now()
        );

        billDAO.addBill(bill);

        Bill retrievedBill =
                billDAO.getBillById(bill.getBillId());

        assertNotNull(retrievedBill);

        assertEquals(
                bill.getBillId(),
                retrievedBill.getBillId()
        );
    }

    @Test
    void testGetBillByAppointmentNo() throws Exception {

        Bill bill = new Bill(
                0,
                appointmentNo,
                2,
                1000.00,
                3000.00,
                LocalDate.now()
        );

        billDAO.addBill(bill);

        Bill retrievedBill =
                billDAO.getBillByAppointmentNo(
                        appointmentNo
                );

        assertNotNull(retrievedBill);

        assertEquals(
                appointmentNo,
                retrievedBill.getAppointmentNo()
        );
    }

    @Test
    void testGetAllBills() throws Exception {

        Bill bill = new Bill(
                0,
                appointmentNo,
                2,
                1000.00,
                4000.00,
                LocalDate.now()
        );

        billDAO.addBill(bill);

        List<Bill> bills =
                billDAO.getAllBills();

        assertNotNull(bills);
        assertFalse(bills.isEmpty());

        boolean billFound =
                bills.stream()
                        .anyMatch(
                                b -> b.getBillId()
                                        == bill.getBillId()
                        );

        assertTrue(billFound);
    }


    private void createTestPatient() throws Exception {

        String sql =
                "INSERT INTO patient "
                + "(patient_name, address, contact_number) "
                + "VALUES (?, ?, ?)";

        try (Connection connection =
                DatabaseConnection.getConnection();

             PreparedStatement ps =
                connection.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )) {

            ps.setString(1, "Bill Test Patient");
            ps.setString(2, "Test Address");
            ps.setString(3, "0770000001");

            ps.executeUpdate();

            try (ResultSet rs =
                    ps.getGeneratedKeys()) {

                if (rs.next()) {
                    patientId = rs.getInt(1);
                }
            }
        }
    }


    private void createTestDentist() throws Exception {

        String sql =
                "INSERT INTO dentist "
                + "(dentist_name, specialization, contact_number) "
                + "VALUES (?, ?, ?)";

        try (Connection connection =
                DatabaseConnection.getConnection();

             PreparedStatement ps =
                connection.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )) {

            ps.setString(
                    1,
                    "Bill Test Dentist"
            );

            ps.setString(
                    2,
                    "General Dentistry"
            );

            ps.setString(
                    3,
                    "0779999999"
            );

            ps.executeUpdate();

            try (ResultSet rs =
                    ps.getGeneratedKeys()) {

                if (rs.next()) {
                    dentistId = rs.getInt(1);
                }
            }
        }
    }

    private void createTestTreatment() throws Exception {

        String sql =
                "INSERT INTO treatment "
                + "(treatment_type, treatment_price) "
                + "VALUES (?, ?)";

        try (Connection connection =
                DatabaseConnection.getConnection();

             PreparedStatement ps =
                connection.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )) {

            ps.setString(
                    1,
                    "Bill Test Treatment"
            );

            ps.setDouble(
                    2,
                    5000.00
            );

            ps.executeUpdate();

            try (ResultSet rs =
                    ps.getGeneratedKeys()) {

                if (rs.next()) {
                    treatmentId = rs.getInt(1);
                }
            }
        }
    }


    private void createTestAppointment() throws Exception {

        String sql =
                "INSERT INTO appointment "
                + "(patient_id, dentist_id, treatment_id, staff_id, "
                + "appointment_date, appointment_time, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection =
                DatabaseConnection.getConnection();

             PreparedStatement ps =
                connection.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )) {

            ps.setInt(
                    1,
                    patientId
            );

            ps.setInt(
                    2,
                    dentistId
            );

            ps.setInt(
                    3,
                    treatmentId
            );

            // Existing test staff
            ps.setInt(
                    4,
                    2
            );

            ps.setDate(
                    5,
                    Date.valueOf(
                            LocalDate.now().plusDays(1)
                    )
            );

            ps.setTime(
                    6,
                    Time.valueOf("10:00:00")
            );

            ps.setString(
                    7,
                    "Scheduled"
            );

            ps.executeUpdate();

            try (ResultSet rs =
                    ps.getGeneratedKeys()) {

                if (rs.next()) {
                    appointmentNo =
                            rs.getInt(1);
                }
            }
        }
    }
}