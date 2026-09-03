package sunrisedentalsystem.model;

import java.time.LocalDate;

public class Bill {

    private int billId;
    private int appointmentNo;
    private int generatedByStaffId;
    private double consultationFee;
    private double treatmentCost;
    private double totalAmount;
    private LocalDate generatedDate;
    private String generatedByStaffName;

    public Bill() {
    }

    public Bill(int billId,
                int appointmentNo,
                int generatedByStaffId,
                double consultationFee,
                double treatmentCost,
                LocalDate generatedDate) {

        this.billId = billId;
        this.appointmentNo = appointmentNo;
        this.generatedByStaffId = generatedByStaffId;
        this.consultationFee = consultationFee;
        this.treatmentCost = treatmentCost;
        this.generatedDate = generatedDate;

        calculateTotal();
    }

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

    public int getAppointmentNo() {
        return appointmentNo;
    }

    public void setAppointmentNo(int appointmentNo) {
        this.appointmentNo = appointmentNo;
    }

    public int getGeneratedByStaffId() {
        return generatedByStaffId;
    }

    public void setGeneratedByStaffId(int generatedByStaffId) {
        this.generatedByStaffId = generatedByStaffId;
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

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(LocalDate generatedDate) {
        this.generatedDate = generatedDate;
    }
    
    public String getGeneratedByStaffName() {

        return generatedByStaffName;
    }

    public void setGeneratedByStaffName(
            String generatedByStaffName) {

        this.generatedByStaffName =
                generatedByStaffName;
    }
}