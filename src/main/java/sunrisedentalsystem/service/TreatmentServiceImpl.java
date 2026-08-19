package sunrisedentalsystem.service;

import java.sql.SQLException;
import java.util.List;

import sunrisedentalsystem.dao.TreatmentDAO;
import sunrisedentalsystem.model.Treatment;

public class TreatmentServiceImpl
        implements TreatmentService {

    private final TreatmentDAO treatmentDAO;

    public TreatmentServiceImpl(
            TreatmentDAO treatmentDAO) {

        this.treatmentDAO = treatmentDAO;
    }

    @Override
    public boolean addTreatment(
            Treatment treatment)
            throws SQLException {

        treatmentDAO.addTreatment(treatment);

        return true;
    }

    @Override
    public Treatment getTreatmentById(
            int treatmentId)
            throws SQLException {

        return treatmentDAO
                .getTreatmentById(treatmentId);
    }

    @Override
    public List<Treatment> getAllTreatments()
            throws SQLException {

        return treatmentDAO
                .getAllTreatments();
    }
}