package sunrisedentalsystem.dao;

import sunrisedentalsystem.model.Treatment;

import java.sql.SQLException;
import java.util.List;

public interface TreatmentDAO {

    void addTreatment(Treatment treatment) throws SQLException;

    Treatment getTreatmentById(int treatmentId) throws SQLException;

    List<Treatment> getAllTreatments() throws SQLException;

    void updateTreatment(Treatment treatment) throws SQLException;

    void deleteTreatment(int treatmentId) throws SQLException;
}