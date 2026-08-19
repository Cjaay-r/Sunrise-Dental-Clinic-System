package sunrisedentalsystem.service;

import java.sql.SQLException;

import sunrisedentalsystem.dao.PatientDAO;
import sunrisedentalsystem.model.Patient;

public class PatientServiceImpl
        implements PatientService {

    private final PatientDAO patientDAO;

    public PatientServiceImpl(
            PatientDAO patientDAO) {

        this.patientDAO = patientDAO;
    }

    @Override
    public Patient registerPatient(
            Patient patient)
            throws SQLException {

        boolean added =
                patientDAO.addPatient(patient);

        if (added) {
            return patient;
        }

        return null;
    }

    @Override
    public Patient searchPatient(
            int patientId)
            throws SQLException {

        return patientDAO
                .getPatientById(patientId);
    }
}