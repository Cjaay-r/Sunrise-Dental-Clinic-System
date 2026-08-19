package sunrisedentalsystem.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sunrisedentalsystem.dao.TreatmentDAO;
import sunrisedentalsystem.model.Treatment;

class TreatmentServiceTest {

    private TreatmentDAO treatmentDAO;
    private TreatmentService treatmentService;

    @BeforeEach
    void setUp() {

        treatmentDAO = mock(TreatmentDAO.class);

        treatmentService =
                new TreatmentServiceImpl(treatmentDAO);
    }

    @Test
    void shouldAddTreatmentThroughDAO()
            throws Exception {

        Treatment treatment =
                mock(Treatment.class);

        boolean result =
                treatmentService.addTreatment(treatment);

        assertTrue(result);

        verify(treatmentDAO)
                .addTreatment(treatment);
    }

    @Test
    void shouldGetTreatmentById()
            throws Exception {

        int treatmentId = 1;

        Treatment expectedTreatment =
                mock(Treatment.class);

        when(treatmentDAO
                .getTreatmentById(treatmentId))
                .thenReturn(expectedTreatment);

        Treatment result =
                treatmentService
                        .getTreatmentById(treatmentId);

        assertSame(expectedTreatment, result);

        verify(treatmentDAO)
                .getTreatmentById(treatmentId);
    }

    @Test
    void shouldGetAllTreatmentsFromDAO()
            throws Exception {

        Treatment treatment1 =
                mock(Treatment.class);

        Treatment treatment2 =
                mock(Treatment.class);

        List<Treatment> expectedTreatments =
                Arrays.asList(
                        treatment1,
                        treatment2);

        when(treatmentDAO.getAllTreatments())
                .thenReturn(expectedTreatments);

        List<Treatment> result =
                treatmentService.getAllTreatments();

        assertSame(expectedTreatments, result);

        verify(treatmentDAO)
                .getAllTreatments();
    }
}