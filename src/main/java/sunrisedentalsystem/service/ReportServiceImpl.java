package sunrisedentalsystem.service;

import java.sql.SQLException;
import java.time.LocalDate;

import sunrisedentalsystem.dao.ReportDAO;
import sunrisedentalsystem.model.ClinicReport;

public class ReportServiceImpl
        implements ReportService {

    private final ReportDAO reportDAO;

    public ReportServiceImpl(
            ReportDAO reportDAO) {

        this.reportDAO =
                reportDAO;
    }

    @Override
    public ClinicReport generateClinicReport(
            LocalDate reportDate)
            throws SQLException {

        if (reportDate == null) {

            throw new IllegalArgumentException(
                    "Report date is required."
            );
        }

        return reportDAO.getClinicReport(
                reportDate
        );
    }
}