package sunrisedentalsystem.dao;

import java.sql.SQLException;
import java.util.List;

import sunrisedentalsystem.model.Dentist;

public interface DentistDAO {

    Dentist getDentistById(int dentistId)
            throws SQLException;

    List<Dentist> getAllDentists()
            throws SQLException;
}