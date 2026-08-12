package sunrisedentalsystem.model;

public class Treatment {

    private int treatmentId;
    private String treatmentType;
    private double treatmentPrice;

    public Treatment() {
    }

    public Treatment(int treatmentId, String treatmentType, double treatmentPrice) {
        this.treatmentId = treatmentId;
        this.treatmentType = treatmentType;
        this.treatmentPrice = treatmentPrice;
    }

    public int getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(int treatmentId) {
        this.treatmentId = treatmentId;
    }

    public String getTreatmentType() {
        return treatmentType;
    }

    public void setTreatmentType(String treatmentType) {
        this.treatmentType = treatmentType;
    }

    public double getTreatmentPrice() {
        return treatmentPrice;
    }

    public void setTreatmentPrice(double treatmentPrice) {
        this.treatmentPrice = treatmentPrice;
    }
}