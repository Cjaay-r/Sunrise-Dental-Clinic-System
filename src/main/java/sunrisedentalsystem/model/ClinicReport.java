package sunrisedentalsystem.model;

import java.time.LocalDate;

public class ClinicReport {

    private LocalDate reportDate;
    private int totalAppointments;
    private int scheduledAppointments;
    private int cancelledAppointments;
    private int billsGenerated;
    private double totalRevenue;
    private String mostCommonTreatment;
    private int mostCommonTreatmentCount;

    public ClinicReport() {
    }

    public ClinicReport(
            LocalDate reportDate,
            int totalAppointments,
            int scheduledAppointments,
            int cancelledAppointments,
            int billsGenerated,
            double totalRevenue,
            String mostCommonTreatment,
            int mostCommonTreatmentCount) {

        this.reportDate =
                reportDate;

        this.totalAppointments =
                totalAppointments;

        this.scheduledAppointments =
                scheduledAppointments;

        this.cancelledAppointments =
                cancelledAppointments;

        this.billsGenerated =
                billsGenerated;

        this.totalRevenue =
                totalRevenue;

        this.mostCommonTreatment =
                mostCommonTreatment;

        this.mostCommonTreatmentCount =
                mostCommonTreatmentCount;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(
            LocalDate reportDate) {

        this.reportDate =
                reportDate;
    }

    public int getTotalAppointments() {
        return totalAppointments;
    }

    public void setTotalAppointments(
            int totalAppointments) {

        this.totalAppointments =
                totalAppointments;
    }

    public int getScheduledAppointments() {
        return scheduledAppointments;
    }

    public void setScheduledAppointments(
            int scheduledAppointments) {

        this.scheduledAppointments =
                scheduledAppointments;
    }

    public int getCancelledAppointments() {
        return cancelledAppointments;
    }

    public void setCancelledAppointments(
            int cancelledAppointments) {

        this.cancelledAppointments =
                cancelledAppointments;
    }

    public int getBillsGenerated() {
        return billsGenerated;
    }

    public void setBillsGenerated(
            int billsGenerated) {

        this.billsGenerated =
                billsGenerated;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(
            double totalRevenue) {

        this.totalRevenue =
                totalRevenue;
    }

    public String getMostCommonTreatment() {
        return mostCommonTreatment;
    }

    public void setMostCommonTreatment(
            String mostCommonTreatment) {

        this.mostCommonTreatment =
                mostCommonTreatment;
    }

    public int getMostCommonTreatmentCount() {
        return mostCommonTreatmentCount;
    }

    public void setMostCommonTreatmentCount(
            int mostCommonTreatmentCount) {

        this.mostCommonTreatmentCount =
                mostCommonTreatmentCount;
    }
}