package sunrisedentalsystem.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sunrisedentalsystem.model.Treatment;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TreatmentDAOTest {

    private TreatmentDAO treatmentDAO;
    
    @BeforeEach
    void setUp() {
        treatmentDAO = new TreatmentDAOImpl();
    }

    @Test
    void testAddTreatment() throws SQLException {

        Treatment treatment = new Treatment(
                0,
                "Dental Cleaning",
                5000.00
        );

        treatmentDAO.addTreatment(treatment);

        assertTrue(treatment.getTreatmentId() > 0);
    }

    @Test
    void testGetTreatmentById() throws SQLException {

        Treatment treatment = new Treatment(
                0,
                "Root Canal",
                25000.00
        );

        treatmentDAO.addTreatment(treatment);

        Treatment retrievedTreatment =
                treatmentDAO.getTreatmentById(treatment.getTreatmentId());

        assertNotNull(retrievedTreatment);
        assertEquals("Root Canal", retrievedTreatment.getTreatmentType());
        assertEquals(25000.00, retrievedTreatment.getTreatmentPrice());
    }

    @Test
    void testGetAllTreatments() throws SQLException {

        Treatment treatment = new Treatment(
                0,
                "Dental X-Ray",
                4000.00
        );

        treatmentDAO.addTreatment(treatment);

        List<Treatment> treatments =
                treatmentDAO.getAllTreatments();

        assertNotNull(treatments);
        assertFalse(treatments.isEmpty());

        boolean treatmentFound = treatments.stream()
                .anyMatch(t -> t.getTreatmentId() == treatment.getTreatmentId());

        assertTrue(treatmentFound);
    }
    @Test
    void testUpdateTreatment() throws SQLException {

        Treatment treatment = new Treatment(
                0,
                "Dental Filling",
                8000.00
        );

        treatmentDAO.addTreatment(treatment);

        treatment.setTreatmentType("Premium Dental Filling");
        treatment.setTreatmentPrice(10000.00);

        treatmentDAO.updateTreatment(treatment);

        Treatment updatedTreatment =
                treatmentDAO.getTreatmentById(treatment.getTreatmentId());

        assertEquals(
                "Premium Dental Filling",
                updatedTreatment.getTreatmentType()
        );

        assertEquals(
                10000.00,
                updatedTreatment.getTreatmentPrice()
        );
    }

    @Test
    void testDeleteTreatment() throws SQLException {

        Treatment treatment = new Treatment(
                0,
                "Temporary Treatment",
                3000.00
        );

        treatmentDAO.addTreatment(treatment);

        int treatmentId = treatment.getTreatmentId();

        treatmentDAO.deleteTreatment(treatmentId);

        Treatment deletedTreatment =
                treatmentDAO.getTreatmentById(treatmentId);

        assertNull(deletedTreatment);
    }
}