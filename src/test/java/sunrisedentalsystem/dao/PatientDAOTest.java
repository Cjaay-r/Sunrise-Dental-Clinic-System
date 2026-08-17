package sunrisedentalsystem.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import sunrisedentalsystem.model.Patient;

class PatientDAOTest {

    @Test
    void shouldAddPatientToDatabase() throws Exception {

        PatientDAO patientDAO = new PatientDAOImpl();

        Patient patient = new Patient(
                0,
                "Test Patient",
                "Colombo",
                "0771234567"
        );

        boolean result = patientDAO.addPatient(patient);

        assertTrue(result);
    }

    @Test
    void shouldSetGeneratedPatientIdAfterAddingPatient() throws Exception {

        PatientDAO patientDAO = new PatientDAOImpl();

        Patient patient = new Patient(
                0,
                "Generated ID Test",
                "Kandy",
                "0712345678"
        );

        boolean result = patientDAO.addPatient(patient);

        assertTrue(result);
        assertTrue(patient.getPatientId() > 0);
    }

    @Test
    void shouldGetPatientById() throws Exception {

        PatientDAO patientDAO = new PatientDAOImpl();

        Patient patient = new Patient(
                0,
                "Search Test Patient",
                "Galle",
                "0751234567"
        );

        patientDAO.addPatient(patient);

        Patient result =
                patientDAO.getPatientById(patient.getPatientId());

        assertNotNull(result);

        assertEquals(
                patient.getPatientId(),
                result.getPatientId()
        );

        assertEquals(
                "Search Test Patient",
                result.getPatientName()
        );

        assertEquals(
                "Galle",
                result.getAddress()
        );

        assertEquals(
                "0751234567",
                result.getContactNumber()
        );
    }
}