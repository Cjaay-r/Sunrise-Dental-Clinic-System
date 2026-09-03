package sunrisedentalsystem.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
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

    private PreparedStatement appointmentStatement;
    private PreparedStatement billingStatement;
    private PreparedStatement treatmentStatement;

    private ResultSet appointmentResultSet;
    private ResultSet billingResultSet;
    private ResultSet treatmentResultSet;

    private MockedStatic<DatabaseConnection>
            databaseConnectionMock;

    private ReportDAO reportDAO;

    @BeforeEach
    void setUp()
            throws Exception {

        connection =
                mock(Connection.class);

        appointmentStatement =
                mock(PreparedStatement.class);

        billingStatement =
                mock(PreparedStatement.class);

        treatmentStatement =
                mock(PreparedStatement.class);

        appointmentResultSet =
                mock(ResultSet.class);

        billingResultSet =
                mock(ResultSet.class);

        treatmentResultSet =
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
                .prepareStatement(
                        anyString()
                ))
                .thenReturn(
                        appointmentStatement,
                        billingStatement,
                        treatmentStatement
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
                        3
                );

        when(appointmentStatement
                .executeQuery())
                .thenReturn(
                        appointmentResultSet
                );

        when(appointmentResultSet
                .next())
                .thenReturn(true);

        when(appointmentResultSet
                .getInt(
                        "total_appointments"
                ))
                .thenReturn(10);

        when(appointmentResultSet
                .getInt(
                        "scheduled_appointments"
                ))
                .thenReturn(7);

        when(appointmentResultSet
                .getInt(
                        "cancelled_appointments"
                ))
                .thenReturn(3);


        when(billingStatement
                .executeQuery())
                .thenReturn(
                        billingResultSet
                );

        when(billingResultSet
                .next())
                .thenReturn(true);

        when(billingResultSet
                .getInt(
                        "bills_generated"
                ))
                .thenReturn(6);

        when(billingResultSet
                .getDouble(
                        "total_revenue"
                ))
                .thenReturn(
                        63000.00
                );


        when(treatmentStatement
                .executeQuery())
                .thenReturn(
                        treatmentResultSet
                );

        when(treatmentResultSet
                .next())
                .thenReturn(true);

        when(treatmentResultSet
                .getString(
                        "treatment_type"
                ))
                .thenReturn(
                        "Dental Filling"
                );

        when(treatmentResultSet
                .getInt(
                        "treatment_count"
                ))
                .thenReturn(4);


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
                10,
                report.getTotalAppointments()
        );

        assertEquals(
                7,
                report.getScheduledAppointments()
        );

        assertEquals(
                3,
                report.getCancelledAppointments()
        );

        assertEquals(
                6,
                report.getBillsGenerated()
        );

        assertEquals(
                63000.00,
                report.getTotalRevenue()
        );

        assertEquals(
                "Dental Filling",
                report.getMostCommonTreatment()
        );

        assertEquals(
                4,
                report.getMostCommonTreatmentCount()
        );


        Date sqlDate =
                Date.valueOf(
                        reportDate
                );

        verify(appointmentStatement)
                .setDate(
                        1,
                        sqlDate
                );

        verify(billingStatement)
                .setDate(
                        1,
                        sqlDate
                );

        verify(treatmentStatement)
                .setDate(
                        1,
                        sqlDate
                );
    }

    @Test
    void shouldHandleDateWithNoTreatmentData()
            throws Exception {

        LocalDate reportDate =
                LocalDate.of(
                        2026,
                        9,
                        4
                );

        when(appointmentStatement
                .executeQuery())
                .thenReturn(
                        appointmentResultSet
                );

        when(appointmentResultSet
                .next())
                .thenReturn(true);

        when(appointmentResultSet
                .getInt(
                        "total_appointments"
                ))
                .thenReturn(0);

        when(appointmentResultSet
                .getInt(
                        "scheduled_appointments"
                ))
                .thenReturn(0);

        when(appointmentResultSet
                .getInt(
                        "cancelled_appointments"
                ))
                .thenReturn(0);


        when(billingStatement
                .executeQuery())
                .thenReturn(
                        billingResultSet
                );

        when(billingResultSet
                .next())
                .thenReturn(true);

        when(billingResultSet
                .getInt(
                        "bills_generated"
                ))
                .thenReturn(0);

        when(billingResultSet
                .getDouble(
                        "total_revenue"
                ))
                .thenReturn(0.0);


        when(treatmentStatement
                .executeQuery())
                .thenReturn(
                        treatmentResultSet
                );

        when(treatmentResultSet
                .next())
                .thenReturn(false);


        ClinicReport report =
                reportDAO.getClinicReport(
                        reportDate
                );


        assertNotNull(
                report
        );

        assertEquals(
                0,
                report.getTotalAppointments()
        );

        assertEquals(
                0,
                report.getBillsGenerated()
        );

        assertEquals(
                0.0,
                report.getTotalRevenue()
        );

        assertEquals(
                null,
                report.getMostCommonTreatment()
        );

        assertEquals(
                0,
                report.getMostCommonTreatmentCount()
        );
    }
}