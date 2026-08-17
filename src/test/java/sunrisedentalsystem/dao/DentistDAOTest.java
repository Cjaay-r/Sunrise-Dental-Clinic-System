package sunrisedentalsystem.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sunrisedentalsystem.model.Dentist;
import sunrisedentalsystem.util.DatabaseConnection;

class DentistDAOTest {

    private DentistDAO dentistDAO;

    @BeforeEach
    void setUp() {

        dentistDAO = new DentistDAOImpl();
    }

    @Test
    void shouldGetDentistById() throws Exception {

        int dentistId = loadFirstDentistId();

        Dentist result =
                dentistDAO.getDentistById(dentistId);

        assertNotNull(result);

        assertEquals(
                dentistId,
                result.getDentistId()
        );
    }

    @Test
    void shouldReturnAllDentists() throws Exception {

        List<Dentist> dentists =
                dentistDAO.getAllDentists();

        assertNotNull(dentists);

        assertFalse(dentists.isEmpty());
    }

    private int loadFirstDentistId()
            throws SQLException {

        String sql =
                "SELECT dentist_id " +
                "FROM dentist " +
                "ORDER BY dentist_id " +
                "LIMIT 1";

        try (Connection connection =
                DatabaseConnection.getConnection();

             Statement statement =
                connection.createStatement();

             ResultSet resultSet =
                statement.executeQuery(sql)) {

            if (resultSet.next()) {

                return resultSet.getInt(
                        "dentist_id"
                );
            }
        }

        throw new IllegalStateException(
                "Dentist table must contain at least one dentist."
        );
    }
}