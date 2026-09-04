package sunrisedentalsystem.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import sunrisedentalsystem.model.ClinicReport;
import sunrisedentalsystem.util.DatabaseConnection;

class ReportDAOTest {

    private Connection connection;

    private CallableStatement callableStatement;

    private ResultSet resultSet;

    private MockedStatic<DatabaseConnection>
            databaseConnectionMock;

    private ReportDAO reportDAO;

    @BeforeEach
    void setUp()
            throws Exception {

        connection =
                mock(Connection.class);

        callableStatement =
                mock(CallableStatement.class);

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

        when(connection
                .prepareCall(
                        anyString()
                ))
                .thenReturn(
                        callableStatement
                );

        when(callableStatement
                .executeQuery())
                .thenReturn(
                        resultSet
                );

        reportDAO =
                new ReportDAOImpl();
    }

    @AfterEach
    void tearDown() {

        databaseConnectionMock.close();
    }

    @Test
    void shouldGenerateClinicReportForSelectedDate()
            throws Exception {

        LocalDate reportDate =
                LocalDate.of(
                        2026,
                        9,
                        4
                );

        when(resultSet.next())
                .thenReturn(true);

        when(resultSet.getInt(
                "total_appointments"))
                .thenReturn(2);

        when(resultSet.getInt(
                "scheduled_appointments"))
                .thenReturn(2);

        when(resultSet.getInt(
                "cancelled_appointments"))
                .thenReturn(0);

        when(resultSet.getInt(
                "bills_generated"))
                .thenReturn(1);

        when(resultSet.getDouble(
                "total_revenue"))
                .thenReturn(
                        8500.00
                );

        when(resultSet.getString(
                "most_common_treatment"))
                .thenReturn(
                        "Pediatric Treatment"
                );

        when(resultSet.getInt(
                "most_common_treatment_count"))
                .thenReturn(2);

        ClinicReport report =
                reportDAO.getClinicReport(
                        reportDate
                );

        assertNotNull(
                report
        );

        assertEquals(
                reportDate,
                report.getReportDate()
        );

        assertEquals(
                2,
                report.getTotalAppointments()
        );

        assertEquals(
                2,
                report.getScheduledAppointments()
        );

        assertEquals(
                0,
                report.getCancelledAppointments()
        );

        assertEquals(
                1,
                report.getBillsGenerated()
        );

        assertEquals(
                8500.00,
                report.getTotalRevenue()
        );

        assertEquals(
                "Pediatric Treatment",
                report.getMostCommonTreatment()
        );

        assertEquals(
                2,
                report.getMostCommonTreatmentCount()
        );

        verify(connection)
                .prepareCall(
                        "{CALL sp_get_daily_clinic_summary(?)}"
                );

        verify(callableStatement)
                .setDate(
                        1,
                        Date.valueOf(
                                reportDate
                        )
                );

        verify(callableStatement)
                .executeQuery();
    }

    @Test
    void shouldHandleDateWithNoReportData()
            throws Exception {

        LocalDate reportDate =
                LocalDate.of(
                        2026,
                        9,
                        5
                );

        when(resultSet.next())
                .thenReturn(true);

        when(resultSet.getInt(
                "total_appointments"))
                .thenReturn(0);

        when(resultSet.getInt(
                "scheduled_appointments"))
                .thenReturn(0);

        when(resultSet.getInt(
                "cancelled_appointments"))
                .thenReturn(0);

        when(resultSet.getInt(
                "bills_generated"))
                .thenReturn(0);

        when(resultSet.getDouble(
                "total_revenue"))
                .thenReturn(0.0);

        when(resultSet.getString(
                "most_common_treatment"))
                .thenReturn(null);

        when(resultSet.getInt(
                "most_common_treatment_count"))
                .thenReturn(0);

        ClinicReport report =
                reportDAO.getClinicReport(
                        reportDate
                );

        assertNotNull(
                report
        );

        assertEquals(
                reportDate,
                report.getReportDate()
        );

        assertEquals(
                0,
                report.getTotalAppointments()
        );

        assertEquals(
                0,
                report.getScheduledAppointments()
        );

        assertEquals(
                0,
                report.getCancelledAppointments()
        );

        assertEquals(
                0,
                report.getBillsGenerated()
        );

        assertEquals(
                0.0,
                report.getTotalRevenue()
        );

        assertNull(
                report.getMostCommonTreatment()
        );

        assertEquals(
                0,
                report.getMostCommonTreatmentCount()
        );

        verify(callableStatement)
                .setDate(
                        1,
                        Date.valueOf(
                                reportDate
                        )
                );

        verify(callableStatement)
                .executeQuery();
    }
}