package sunrisedentalsystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sunrisedentalsystem.dao.DentistDAO;
import sunrisedentalsystem.model.Dentist;

class DentistServiceTest {

    private DentistDAO dentistDAO;

    private DentistService dentistService;

    @BeforeEach
    void setUp() {

        dentistDAO =
                mock(DentistDAO.class);

        dentistService =
                new DentistServiceImpl(
                        dentistDAO
                );
    }

    @Test
    void shouldAddDentistThroughDAO()
            throws Exception {

        Dentist dentist =
                new Dentist(
                        0,
                        "Dr. Silva",
                        "Orthodontics",
                        "0771234567"
                );

        when(dentistDAO.addDentist(dentist))
                .thenReturn(true);

        boolean result =
                dentistService
                        .addDentist(dentist);

        assertTrue(result);

        verify(dentistDAO)
                .addDentist(dentist);
    }

    @Test
    void shouldReturnFalseWhenDentistCannotBeAdded()
            throws Exception {

        Dentist dentist =
                new Dentist(
                        0,
                        "Dr. Silva",
                        "Orthodontics",
                        "0771234567"
                );

        when(dentistDAO.addDentist(dentist))
                .thenReturn(false);

        boolean result =
                dentistService
                        .addDentist(dentist);

        assertFalse(result);

        verify(dentistDAO)
                .addDentist(dentist);
    }

    @Test
    void shouldSearchDentistThroughDAO()
            throws Exception {

        Dentist dentist =
                new Dentist(
                        2,
                        "Dr. Perera",
                        "General Dentistry",
                        "0712345678"
                );

        when(dentistDAO.getDentistById(2))
                .thenReturn(dentist);

        Dentist result =
                dentistService
                        .searchDentist(2);

        assertSame(
                dentist,
                result
        );

        verify(dentistDAO)
                .getDentistById(2);
    }

    @Test
    void shouldReturnAllDentistsFromDAO()
            throws Exception {

        List<Dentist> dentists =
                List.of(
                        new Dentist(
                                1,
                                "Dr. Silva",
                                "Orthodontics",
                                "0771234567"
                        ),
                        new Dentist(
                                2,
                                "Dr. Perera",
                                "General Dentistry",
                                "0712345678"
                        )
                );

        when(dentistDAO.getAllDentists())
                .thenReturn(dentists);

        List<Dentist> result =
                dentistService
                        .getAllDentists();

        assertEquals(
                2,
                result.size()
        );

        assertSame(
                dentists,
                result
        );

        verify(dentistDAO)
                .getAllDentists();
    }

    @Test
    void shouldUpdateDentistThroughDAO()
            throws Exception {

        Dentist dentist =
                new Dentist(
                        2,
                        "Dr. Fernando",
                        "Endodontics",
                        "0751234567"
                );

        when(dentistDAO.updateDentist(dentist))
                .thenReturn(true);

        boolean result =
                dentistService
                        .updateDentist(dentist);

        assertTrue(result);

        verify(dentistDAO)
                .updateDentist(dentist);
    }

    @Test
    void shouldDeleteDentistThroughDAO()
            throws Exception {

        when(dentistDAO.deleteDentist(2))
                .thenReturn(true);

        boolean result =
                dentistService
                        .deleteDentist(2);

        assertTrue(result);

        verify(dentistDAO)
                .deleteDentist(2);
    }
}