package sunrisedentalsystem.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import sunrisedentalsystem.model.Dentist;

class DentistDAOTest {

    @Test
    void shouldAddDentistToDatabase()
            throws Exception {

        DentistDAO dentistDAO =
                new DentistDAOImpl();

        Dentist dentist =
                new Dentist(
                        0,
                        "Dr. TDD Test",
                        "Orthodontics",
                        "0771234567"
                );

        boolean result =
                dentistDAO.addDentist(dentist);

        assertTrue(result);

        assertTrue(
                dentist.getDentistId() > 0
        );

        Dentist savedDentist =
                dentistDAO.getDentistById(
                        dentist.getDentistId()
                );

        assertNotNull(savedDentist);

        assertEquals(
                "Dr. TDD Test",
                savedDentist.getDentistName()
        );

        assertEquals(
                "Orthodontics",
                savedDentist.getSpecialization()
        );

        assertEquals(
                "0771234567",
                savedDentist.getContactNumber()
        );

        dentistDAO.deleteDentist(
                dentist.getDentistId()
        );
    }

    @Test
    void shouldGetDentistById()
            throws Exception {

        DentistDAO dentistDAO =
                new DentistDAOImpl();

        Dentist dentist =
                new Dentist(
                        0,
                        "Dr. Search Test",
                        "General Dentistry",
                        "0712345678"
                );

        dentistDAO.addDentist(dentist);

        Dentist result =
                dentistDAO.getDentistById(
                        dentist.getDentistId()
                );

        assertNotNull(result);

        assertEquals(
                "Dr. Search Test",
                result.getDentistName()
        );

        assertEquals(
                "General Dentistry",
                result.getSpecialization()
        );

        dentistDAO.deleteDentist(
                dentist.getDentistId()
        );
    }

    @Test
    void shouldReturnAllDentists()
            throws Exception {

        DentistDAO dentistDAO =
                new DentistDAOImpl();

        Dentist dentist =
                new Dentist(
                        0,
                        "Dr. List Test",
                        "Endodontics",
                        "0751234567"
                );

        dentistDAO.addDentist(dentist);

        List<Dentist> dentists =
                dentistDAO.getAllDentists();

        assertNotNull(dentists);

        assertFalse(
                dentists.isEmpty()
        );

        dentistDAO.deleteDentist(
                dentist.getDentistId()
        );
    }

    @Test
    void shouldUpdateDentistInDatabase()
            throws Exception {

        DentistDAO dentistDAO =
                new DentistDAOImpl();

        Dentist dentist =
                new Dentist(
                        0,
                        "Dr. Before Update",
                        "General Dentistry",
                        "0761111111"
                );

        assertTrue(
                dentistDAO.addDentist(dentist)
        );

        dentist.setDentistName(
                "Dr. After Update"
        );

        dentist.setSpecialization(
                "Orthodontics"
        );

        dentist.setContactNumber(
                "0762222222"
        );

        boolean result =
                dentistDAO.updateDentist(
                        dentist
                );

        assertTrue(result);

        Dentist updatedDentist =
                dentistDAO.getDentistById(
                        dentist.getDentistId()
                );

        assertNotNull(updatedDentist);

        assertEquals(
                "Dr. After Update",
                updatedDentist.getDentistName()
        );

        assertEquals(
                "Orthodontics",
                updatedDentist.getSpecialization()
        );

        assertEquals(
                "0762222222",
                updatedDentist.getContactNumber()
        );

        dentistDAO.deleteDentist(
                dentist.getDentistId()
        );
    }

    @Test
    void shouldDeleteDentistFromDatabase()
            throws Exception {

        DentistDAO dentistDAO =
                new DentistDAOImpl();

        Dentist dentist =
                new Dentist(
                        0,
                        "Dr. Delete Test",
                        "Periodontics",
                        "0701234567"
                );

        assertTrue(
                dentistDAO.addDentist(dentist)
        );

        int dentistId =
                dentist.getDentistId();

        boolean result =
                dentistDAO.deleteDentist(
                        dentistId
                );

        assertTrue(result);

        Dentist deletedDentist =
                dentistDAO.getDentistById(
                        dentistId
                );

        assertNull(deletedDentist);
    }
}