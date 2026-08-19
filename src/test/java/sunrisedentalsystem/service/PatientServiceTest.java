package sunrisedentalsystem.service;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sunrisedentalsystem.dao.PatientDAO;
import sunrisedentalsystem.model.Patient;

class PatientServiceTest {

    private PatientDAO patientDAO;
    private PatientService patientService;

    @BeforeEach
    void setUp() {

        patientDAO = mock(PatientDAO.class);

        patientService =
                new PatientServiceImpl(patientDAO);
    }

    @Test
    void shouldRegisterPatientThroughDAO()
            throws Exception {

        Patient patient =
                mock(Patient.class);

        when(patientDAO.addPatient(patient))
                .thenReturn(true);

        Patient result =
                patientService.registerPatient(patient);

        assertSame(patient, result);

        verify(patientDAO)
                .addPatient(patient);
    }

    @Test
    void shouldReturnNullWhenPatientCannotBeRegistered()
            throws Exception {

        Patient patient =
                mock(Patient.class);

        when(patientDAO.addPatient(patient))
                .thenReturn(false);

        Patient result =
                patientService.registerPatient(patient);

        assertNull(result);

        verify(patientDAO)
                .addPatient(patient);
    }

    @Test
    void shouldSearchPatientById()
            throws Exception {

        int patientId = 1;

        Patient expectedPatient =
                mock(Patient.class);

        when(patientDAO
                .getPatientById(patientId))
                .thenReturn(expectedPatient);

        Patient result =
                patientService
                        .searchPatient(patientId);

        assertSame(expectedPatient, result);

        verify(patientDAO)
                .getPatientById(patientId);
    }
}