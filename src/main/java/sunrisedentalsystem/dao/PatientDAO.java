package sunrisedentalsystem.dao;

import java.sql.SQLException;
import java.util.List;

import sunrisedentalsystem.model.Patient;

public interface PatientDAO {

    boolean addPatient(
            Patient patient)
            throws SQLException;

    Patient getPatientById(
            int patientId)
            throws SQLException;

    Patient getPatientByContactNumber(
            String contactNumber)
            throws SQLException;

    List<Patient> searchPatientsByName(
            String patientName)
            throws SQLException;

    List<Patient> getAllPatients()
            throws SQLException;
}