package sunrisedentalsystem.service;

import java.sql.SQLException;
import java.util.List;

import sunrisedentalsystem.model.Patient;

public interface PatientService {

    Patient registerPatient(
            Patient patient)
            throws SQLException;

    Patient searchPatient(
            int patientId)
            throws SQLException;

    Patient searchPatientByContactNumber(
            String contactNumber)
            throws SQLException;

    List<Patient> searchPatientsByName(
            String patientName)
            throws SQLException;

    List<Patient> getAllPatients()
            throws SQLException;
}