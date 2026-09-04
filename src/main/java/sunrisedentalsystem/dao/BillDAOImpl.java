package sunrisedentalsystem.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import sunrisedentalsystem.model.Bill;
import sunrisedentalsystem.util.DatabaseConnection;

public class BillDAOImpl
        implements BillDAO {

    @Override
    public void addBill(
            Bill bill)
            throws SQLException {

        String sql =
                "INSERT INTO bill " +
                "(appointment_no, generated_by_staff_id, " +
                "consultation_fee, treatment_cost, " +
                "total_amount, generated_date) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS
                     )) {

            statement.setInt(
                    1,
                    bill.getAppointmentNo()
            );

            statement.setInt(
                    2,
                    bill.getGeneratedByStaffId()
            );

            statement.setDouble(
                    3,
                    bill.getConsultationFee()
            );

            statement.setDouble(
                    4,
                    bill.getTreatmentCost()
            );

            statement.setDouble(
                    5,
                    bill.calculateTotal()
            );

            statement.setDate(
                    6,
                    Date.valueOf(
                            bill.getGeneratedDate()
                    )
            );

            statement.executeUpdate();

            try (ResultSet generatedKeys =
                         statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {

                    bill.setBillId(
                            generatedKeys.getInt(1)
                    );
                }
            }
        }
    }

    @Override
    public void addBill(
            Bill bill,
            int userId)
            throws SQLException {

        String sql =
                "INSERT INTO bill " +
                "(appointment_no, generated_by_staff_id, " +
                "consultation_fee, treatment_cost, " +
                "total_amount, generated_date) " +
                "VALUES (?, " +
                "(SELECT staff_id FROM staff WHERE user_id = ?), " +
                "?, ?, ?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS
                     )) {

            statement.setInt(
                    1,
                    bill.getAppointmentNo()
            );

            statement.setInt(
                    2,
                    userId
            );

            statement.setDouble(
                    3,
                    bill.getConsultationFee()
            );

            statement.setDouble(
                    4,
                    bill.getTreatmentCost()
            );

            statement.setDouble(
                    5,
                    bill.calculateTotal()
            );

            statement.setDate(
                    6,
                    Date.valueOf(
                            bill.getGeneratedDate()
                    )
            );

            statement.executeUpdate();

            try (ResultSet generatedKeys =
                         statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {

                    bill.setBillId(
                            generatedKeys.getInt(1)
                    );
                }
            }
        }
    }

    @Override
    public Bill getBillById(
            int billId)
            throws SQLException {

    	String sql =
    	        "SELECT b.*, s.staff_name " +
    	        "FROM bill b " +
    	        "JOIN staff s " +
    	        "ON b.generated_by_staff_id = s.staff_id " +
    	        "WHERE b.bill_id = ?";
    	
        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    billId
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return createBill(
                            resultSet
                    );
                }
            }
        }

        return null;
    }

    @Override
    public Bill getBillByAppointmentNo(
            int appointmentNo)
            throws SQLException {

    	String sql =
    	        "SELECT b.*, s.staff_name " +
    	        "FROM bill b " +
    	        "JOIN staff s " +
    	        "ON b.generated_by_staff_id = s.staff_id " +
    	        "WHERE b.appointment_no = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    appointmentNo
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return createBill(
                            resultSet
                    );
                }
            }
        }

        return null;
    }

    @Override
    public List<Bill> getAllBills()
            throws SQLException {

    	String sql =
    	        "SELECT b.*, s.staff_name " +
    	        "FROM bill b " +
    	        "JOIN staff s " +
    	        "ON b.generated_by_staff_id = s.staff_id " +
    	        "ORDER BY b.generated_date DESC, " +
    	        "b.bill_id DESC";
    	
        List<Bill> bills =
                new ArrayList<>();

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql);

             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                bills.add(
                        createBill(
                                resultSet
                        )
                );
            }
        }

        return bills;
    }

    private Bill createBill(
            ResultSet resultSet)
            throws SQLException {

        Bill bill =
                new Bill(
                        resultSet.getInt(
                                "bill_id"
                        ),
                        resultSet.getInt(
                                "appointment_no"
                        ),
                        resultSet.getInt(
                                "generated_by_staff_id"
                        ),
                        resultSet.getDouble(
                                "consultation_fee"
                        ),
                        resultSet.getDouble(
                                "treatment_cost"
                        ),
                        resultSet.getDate(
                                "generated_date"
                        ).toLocalDate()
                );

        bill.setTotalAmount(
                resultSet.getDouble(
                        "total_amount"
                )
        );
        
        bill.setGeneratedByStaffName(
                resultSet.getString(
                        "staff_name"
                )
        );

        return bill;
    }
}