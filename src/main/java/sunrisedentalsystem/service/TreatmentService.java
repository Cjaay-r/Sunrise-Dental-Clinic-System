package sunrisedentalsystem.service;

import java.sql.SQLException;
import java.util.List;

import sunrisedentalsystem.model.Treatment;

public interface TreatmentService {

    boolean addTreatment(
            Treatment treatment)
            throws SQLException;

    Treatment getTreatmentById(
            int treatmentId)
            throws SQLException;

    List<Treatment> getAllTreatments()
            throws SQLException;

    boolean updateTreatment(
            Treatment treatment)
            throws SQLException;

    boolean deleteTreatment(
            int treatmentId)
            throws SQLException;
}