package sunrisedentalsystem.service;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sunrisedentalsystem.dao.PatientDAO;
import sunrisedentalsystem.model.Patient;

class PatientServiceTest {

    private PatientDAO patientDAO;

    private PatientService patientService;

    @BeforeEach
    void setUp() {

        patientDAO =
                mock(PatientDAO.class);

        patientService =
                new PatientServiceImpl(
                        patientDAO
                );
    }

    @Test
    void shouldRegisterPatientThroughDAO()
            throws Exception {

        Patient patient =
                mock(Patient.class);

        when(patientDAO
                .addPatient(
                        patient
                ))
                .thenReturn(true);

        Patient result =
                patientService
                        .registerPatient(
                                patient
                        );

        assertSame(
                patient,
                result
        );

        verify(patientDAO)
                .addPatient(
                        patient
                );
    }

    @Test
    void shouldReturnNullWhenPatientRegistrationFails()
            throws Exception {

        Patient patient =
                mock(Patient.class);

        when(patientDAO
                .addPatient(
                        patient
                ))
                .thenReturn(false);

        Patient result =
                patientService
                        .registerPatient(
                                patient
                        );

        assertNull(
                result
        );
    }

    @Test
    void shouldSearchPatientById()
            throws Exception {

        Patient expectedPatient =
                mock(Patient.class);

        when(patientDAO
                .getPatientById(
                        5
                ))
                .thenReturn(
                        expectedPatient
                );

        Patient result =
                patientService
                        .searchPatient(
                                5
                        );

        assertSame(
                expectedPatient,
                result
        );

        verify(patientDAO)
                .getPatientById(
                        5
                );
    }

    @Test
    void shouldSearchPatientByContactNumber()
            throws Exception {

        Patient expectedPatient =
                mock(Patient.class);

        when(patientDAO
                .getPatientByContactNumber(
                        "0771234567"
                ))
                .thenReturn(
                        expectedPatient
                );

        Patient result =
                patientService
                        .searchPatientByContactNumber(
                                "0771234567"
                        );

        assertSame(
                expectedPatient,
                result
        );

        verify(patientDAO)
                .getPatientByContactNumber(
                        "0771234567"
                );
    }

    @Test
    void shouldSearchPatientsByName()
            throws Exception {

        List<Patient> expectedPatients =
                List.of(
                        mock(Patient.class),
                        mock(Patient.class)
                );

        when(patientDAO
                .searchPatientsByName(
                        "Kyle"
                ))
                .thenReturn(
                        expectedPatients
                );

        List<Patient> result =
                patientService
                        .searchPatientsByName(
                                "Kyle"
                        );

        assertSame(
                expectedPatients,
                result
        );

        verify(patientDAO)
                .searchPatientsByName(
                        "Kyle"
                );
    }

    @Test
    void shouldGetAllPatients()
            throws Exception {

        List<Patient> expectedPatients =
                List.of(
                        mock(Patient.class),
                        mock(Patient.class)
                );

        when(patientDAO
                .getAllPatients())
                .thenReturn(
                        expectedPatients
                );

        List<Patient> result =
                patientService
                        .getAllPatients();

        assertSame(
                expectedPatients,
                result
        );

        verify(patientDAO)
                .getAllPatients();
    }
}