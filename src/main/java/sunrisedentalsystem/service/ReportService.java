package sunrisedentalsystem.service;

import java.sql.SQLException;
import java.time.LocalDate;

import sunrisedentalsystem.model.ClinicReport;

public interface ReportService {

    ClinicReport generateClinicReport(
            LocalDate reportDate)
            throws SQLException;
}