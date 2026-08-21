package sunrisedentalsystem.model;

public class Dentist {

    private int dentistId;

    private String dentistName;

    private String specialization;

    private String contactNumber;

    public Dentist() {
    }

    public Dentist(int dentistId,
                   String dentistName) {

        this.dentistId = dentistId;
        this.dentistName = dentistName;
    }

    public Dentist(int dentistId,
                   String dentistName,
                   String specialization,
                   String contactNumber) {

        this.dentistId = dentistId;
        this.dentistName = dentistName;
        this.specialization = specialization;
        this.contactNumber = contactNumber;
    }

    public int getDentistId() {
        return dentistId;
    }

    public void setDentistId(int dentistId) {
        this.dentistId = dentistId;
    }

    public String getDentistName() {
        return dentistName;
    }

    public void setDentistName(String dentistName) {
        this.dentistName = dentistName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}