package sunrisedentalsystem.model;

public class Bill {

    private int billId;
    private double consultationFee;
    private double treatmentCost;
    private double totalAmount;

    public Bill() {
    }

    public Bill(int billId, double consultationFee, double treatmentCost) {
        this.billId = billId;
        this.consultationFee = consultationFee;
        this.treatmentCost = treatmentCost;
    }
    
    // calculate bill is implemented
    public double calculateTotal() {
        totalAmount = consultationFee + treatmentCost;
        return totalAmount;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }

    public double getTreatmentCost() {
        return treatmentCost;
    }

    public void setTreatmentCost(double treatmentCost) {
        this.treatmentCost = treatmentCost;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}