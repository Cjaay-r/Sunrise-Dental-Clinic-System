package sunrisedentalsystem.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import sunrisedentalsystem.model.ClinicReport;
import sunrisedentalsystem.util.DatabaseConnection;

public class ReportDAOImpl
        implements ReportDAO {

    @Override
    public ClinicReport getClinicReport(
            LocalDate reportDate)
            throws SQLException {

        String sql =
                "{CALL sp_get_daily_clinic_summary(?)}";

        ClinicReport report =
                new ClinicReport();

        report.setReportDate(
                reportDate
        );

        try (Connection connection =
                     DatabaseConnection.getConnection();

             CallableStatement statement =
                     connection.prepareCall(
                             sql
                     )) {

            statement.setDate(
                    1,
                    Date.valueOf(
                            reportDate
                    )
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    report.setTotalAppointments(
                            resultSet.getInt(
                                    "total_appointments"
                            )
                    );

                    report.setScheduledAppointments(
                            resultSet.getInt(
                                    "scheduled_appointments"
                            )
                    );

                    report.setCancelledAppointments(
                            resultSet.getInt(
                                    "cancelled_appointments"
                            )
                    );

                    report.setBillsGenerated(
                            resultSet.getInt(
                                    "bills_generated"
                            )
                    );

                    report.setTotalRevenue(
                            resultSet.getDouble(
                                    "total_revenue"
                            )
                    );

                    report.setMostCommonTreatment(
                            resultSet.getString(
                                    "most_common_treatment"
                            )
                    );

                    report.setMostCommonTreatmentCount(
                            resultSet.getInt(
                                    "most_common_treatment_count"
                            )
                    );
                }
            }
        }

        return report;
    }
}