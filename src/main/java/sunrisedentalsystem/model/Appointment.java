package sunrisedentalsystem.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {

    private String appointmentNo;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private AppointmentStatus status;

    private Patient patient;
    private Dentist dentist;
    private Treatment treatment;

    public Appointment() {
    }

    public Appointment(String appointmentNo,
                       LocalDate appointmentDate,
                       LocalTime appointmentTime,
                       AppointmentStatus status,
                       Patient patient,
                       Dentist dentist,
                       Treatment treatment) {

        this.appointmentNo = appointmentNo;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.patient = patient;
        this.dentist = dentist;
        this.treatment = treatment;
    }

    public void cancel() {
        this.status = AppointmentStatus.CANCELLED;
    }

    public String getAppointmentNo() {
        return appointmentNo;
    }

    public void setAppointmentNo(String appointmentNo) {
        this.appointmentNo = appointmentNo;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Dentist getDentist() {
        return dentist;
    }

    public void setDentist(Dentist dentist) {
        this.dentist = dentist;
    }

    public Treatment getTreatment() {
        return treatment;
    }

    public void setTreatment(Treatment treatment) {
        this.treatment = treatment;
    }
}