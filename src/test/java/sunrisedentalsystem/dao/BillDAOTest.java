package sunrisedentalsystem.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import sunrisedentalsystem.model.Bill;
import sunrisedentalsystem.util.DatabaseConnection;

class BillDAOTest {

    private BillDAO billDAO;

    private Connection connection;

    private PreparedStatement preparedStatement;

    private ResultSet resultSet;

    private MockedStatic<DatabaseConnection>
            databaseConnectionMock;

    @BeforeEach
    void setUp() {

        connection =
                mock(Connection.class);

        preparedStatement =
                mock(PreparedStatement.class);

        resultSet =
                mock(ResultSet.class);

        databaseConnectionMock =
                mockStatic(
                        DatabaseConnection.class
                );

        databaseConnectionMock
                .when(
                        DatabaseConnection::getConnection
                )
                .thenReturn(
                        connection
                );

        billDAO =
                new BillDAOImpl();
    }

    @AfterEach
    void tearDown() {

        databaseConnectionMock.close();
    }

    @Test
    void shouldAddBill()
            throws Exception {

        Bill bill =
                new Bill(
                        0,
                        10,
                        2,
                        2500.00,
                        5000.00,
                        LocalDate.of(
                                2026,
                                9,
                                2
                        )
                );

        when(connection.prepareStatement(
                anyString(),
                eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(
                        preparedStatement
                );

        when(preparedStatement.getGeneratedKeys())
                .thenReturn(
                        resultSet
                );

        when(resultSet.next())
                .thenReturn(true);

        when(resultSet.getInt(1))
                .thenReturn(7);

        billDAO.addBill(
                bill
        );

        assertEquals(
                7,
                bill.getBillId()
        );

        verify(preparedStatement)
                .setInt(
                        1,
                        10
                );

        verify(preparedStatement)
                .setInt(
                        2,
                        2
                );

        verify(preparedStatement)
                .setDouble(
                        3,
                        2500.00
                );

        verify(preparedStatement)
                .setDouble(
                        4,
                        5000.00
                );

        verify(preparedStatement)
                .setDouble(
                        5,
                        7500.00
                );

        verify(preparedStatement)
                .setDate(
                        6,
                        Date.valueOf(
                                LocalDate.of(
                                        2026,
                                        9,
                                        2
                                )
                        )
                );

        verify(preparedStatement)
                .executeUpdate();
    }

    @Test
    void shouldAddBillUsingLoggedInUser()
            throws Exception {

        Bill bill =
                new Bill(
                        0,
                        10,
                        0,
                        2500.00,
                        5000.00,
                        LocalDate.of(
                                2026,
                                9,
                                2
                        )
                );

        when(connection.prepareStatement(
                anyString(),
                eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(
                        preparedStatement
                );

        when(preparedStatement.getGeneratedKeys())
                .thenReturn(
                        resultSet
                );

        when(resultSet.next())
                .thenReturn(true);

        when(resultSet.getInt(1))
                .thenReturn(8);

        billDAO.addBill(
                bill,
                2
        );

        assertEquals(
                8,
                bill.getBillId()
        );

        verify(preparedStatement)
                .setInt(
                        1,
                        10
                );

        verify(preparedStatement)
                .setInt(
                        2,
                        2
                );

        verify(preparedStatement)
                .setDouble(
                        3,
                        2500.00
                );

        verify(preparedStatement)
                .setDouble(
                        4,
                        5000.00
                );

        verify(preparedStatement)
                .setDouble(
                        5,
                        7500.00
                );

        verify(preparedStatement)
                .setDate(
                        6,
                        Date.valueOf(
                                LocalDate.of(
                                        2026,
                                        9,
                                        2
                                )
                        )
                );

        verify(preparedStatement)
                .executeUpdate();
    }

    @Test
    void shouldGetBillById()
            throws Exception {

        prepareSingleBillResult();

        Bill bill =
                billDAO.getBillById(
                        5
                );

        assertNotNull(
                bill
        );

        assertEquals(
                5,
                bill.getBillId()
        );

        assertEquals(
                10,
                bill.getAppointmentNo()
        );

        assertEquals(
                2500.00,
                bill.getConsultationFee()
        );

        assertEquals(
                5000.00,
                bill.getTreatmentCost()
        );

        assertEquals(
                7500.00,
                bill.getTotalAmount()
        );

        verify(preparedStatement)
                .setInt(
                        1,
                        5
                );
    }

    @Test
    void shouldGetBillByAppointmentNo()
            throws Exception {

        prepareSingleBillResult();

        Bill bill =
                billDAO
                        .getBillByAppointmentNo(
                                10
                        );

        assertNotNull(
                bill
        );

        assertEquals(
                10,
                bill.getAppointmentNo()
        );

        assertEquals(
                7500.00,
                bill.getTotalAmount()
        );

        verify(preparedStatement)
                .setInt(
                        1,
                        10
                );
    }

    @Test
    void shouldGetAllBills()
            throws Exception {

        when(connection.prepareStatement(
                anyString()))
                .thenReturn(
                        preparedStatement
                );

        when(preparedStatement.executeQuery())
                .thenReturn(
                        resultSet
                );

        when(resultSet.next())
                .thenReturn(
                        true,
                        false
                );

        stubBillColumns();

        List<Bill> bills =
                billDAO.getAllBills();

        assertNotNull(
                bills
        );

        assertEquals(
                1,
                bills.size()
        );

        assertEquals(
                5,
                bills.get(0).getBillId()
        );

        assertEquals(
                7500.00,
                bills.get(0).getTotalAmount()
        );
    }

    private void prepareSingleBillResult()
            throws Exception {

        when(connection.prepareStatement(
                anyString()))
                .thenReturn(
                        preparedStatement
                );

        when(preparedStatement.executeQuery())
                .thenReturn(
                        resultSet
                );

        when(resultSet.next())
                .thenReturn(true);

        stubBillColumns();
    }

    private void stubBillColumns()
            throws Exception {

        when(resultSet.getInt(
                "bill_id"))
                .thenReturn(5);

        when(resultSet.getInt(
                "appointment_no"))
                .thenReturn(10);

        when(resultSet.getInt(
                "generated_by_staff_id"))
                .thenReturn(2);

        when(resultSet.getDouble(
                "consultation_fee"))
                .thenReturn(
                        2500.00
                );

        when(resultSet.getDouble(
                "treatment_cost"))
                .thenReturn(
                        5000.00
                );

        when(resultSet.getDouble(
                "total_amount"))
                .thenReturn(
                        7500.00
                );

        when(resultSet.getDate(
                "generated_date"))
                .thenReturn(
                        Date.valueOf(
                                "2026-09-02"
                        )
                );
    }
}