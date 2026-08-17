package sunrisedentalsystem.dao;

import java.sql.SQLException;

import sunrisedentalsystem.model.Patient;

public interface PatientDAO {

    boolean addPatient(Patient patient) throws SQLException;

    Patient getPatientById(int patientId) throws SQLException;
}