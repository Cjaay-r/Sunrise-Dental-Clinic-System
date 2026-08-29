package sunrisedentalsystem.service;

import java.sql.SQLException;

import sunrisedentalsystem.model.Patient;

public interface PatientService {

    Patient registerPatient(
            Patient patient) throws SQLException;

    Patient searchPatient(
            int patientId) throws SQLException;
    
    Patient searchPatientByContactNumber(
            String contactNumber)
            throws SQLException;
}