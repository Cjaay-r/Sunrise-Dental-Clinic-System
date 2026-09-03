package sunrisedentalsystem.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
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

        ClinicReport report =
                new ClinicReport();

        report.setReportDate(
                reportDate
        );

        loadAppointmentSummary(
                report,
                reportDate
        );

        loadBillingSummary(
                report,
                reportDate
        );

        loadMostCommonTreatment(
                report,
                reportDate
        );

        return report;
    }

    private void loadAppointmentSummary(
            ClinicReport report,
            LocalDate reportDate)
            throws SQLException {

        String sql =
                "SELECT COUNT(*) AS total_appointments, " +
                "SUM(CASE WHEN status = 'SCHEDULED' THEN 1 ELSE 0 END) " +
                "AS scheduled_appointments, " +
                "SUM(CASE WHEN status = 'CANCELLED' THEN 1 ELSE 0 END) " +
                "AS cancelled_appointments " +
                "FROM appointment " +
                "WHERE appointment_date = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(
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
                }
            }
        }
    }

    private void loadBillingSummary(
            ClinicReport report,
            LocalDate reportDate)
            throws SQLException {

        String sql =
                "SELECT COUNT(*) AS bills_generated, " +
                "COALESCE(SUM(total_amount), 0) AS total_revenue " +
                "FROM bill " +
                "WHERE generated_date = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(
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
                }
            }
        }
    }

    private void loadMostCommonTreatment(
            ClinicReport report,
            LocalDate reportDate)
            throws SQLException {

        String sql =
                "SELECT t.treatment_type, " +
                "COUNT(*) AS treatment_count " +
                "FROM appointment a " +
                "JOIN treatment t " +
                "ON a.treatment_id = t.treatment_id " +
                "WHERE a.appointment_date = ? " +
                "AND a.status <> 'CANCELLED' " +
                "GROUP BY t.treatment_id, t.treatment_type " +
                "ORDER BY treatment_count DESC, " +
                "t.treatment_type ASC " +
                "LIMIT 1";

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(
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

                    report.setMostCommonTreatment(
                            resultSet.getString(
                                    "treatment_type"
                            )
                    );

                    report.setMostCommonTreatmentCount(
                            resultSet.getInt(
                                    "treatment_count"
                            )
                    );

                } else {

                    report.setMostCommonTreatment(
                            null
                    );

                    report.setMostCommonTreatmentCount(
                            0
                    );
                }
            }
        }
    }
}