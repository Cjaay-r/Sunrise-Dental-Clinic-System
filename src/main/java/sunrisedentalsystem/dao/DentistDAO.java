package sunrisedentalsystem.dao;

import java.sql.SQLException;
import java.util.List;

import sunrisedentalsystem.model.Dentist;

public interface DentistDAO {

    boolean addDentist(Dentist dentist)
            throws SQLException;

    Dentist getDentistById(int dentistId)
            throws SQLException;

    List<Dentist> getAllDentists()
            throws SQLException;

    boolean updateDentist(Dentist dentist)
            throws SQLException;

    boolean deleteDentist(int dentistId)
            throws SQLException;
}