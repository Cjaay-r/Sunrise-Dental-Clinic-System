package sunrisedentalsystem.service;

import java.sql.SQLException;
import java.util.List;

import sunrisedentalsystem.dao.DentistDAO;
import sunrisedentalsystem.model.Dentist;

public class DentistServiceImpl
        implements DentistService {

    private final DentistDAO dentistDAO;

    public DentistServiceImpl(
            DentistDAO dentistDAO) {

        this.dentistDAO = dentistDAO;
    }

    @Override
    public boolean addDentist(
            Dentist dentist)
            throws SQLException {

        return dentistDAO
                .addDentist(dentist);
    }

    @Override
    public Dentist searchDentist(
            int dentistId)
            throws SQLException {

        return dentistDAO
                .getDentistById(dentistId);
    }

    @Override
    public List<Dentist> getAllDentists()
            throws SQLException {

        return dentistDAO
                .getAllDentists();
    }

    @Override
    public boolean updateDentist(
            Dentist dentist)
            throws SQLException {

        return dentistDAO
                .updateDentist(dentist);
    }

    @Override
    public boolean deleteDentist(
            int dentistId)
            throws SQLException {

        return dentistDAO
                .deleteDentist(dentistId);
    }
}