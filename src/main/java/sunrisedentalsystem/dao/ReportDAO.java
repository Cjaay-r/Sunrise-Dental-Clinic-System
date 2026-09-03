package sunrisedentalsystem.dao;

import java.sql.SQLException;
import java.time.LocalDate;

import sunrisedentalsystem.model.ClinicReport;

public interface ReportDAO {

    ClinicReport getClinicReport(
            LocalDate reportDate)
            throws SQLException;
}