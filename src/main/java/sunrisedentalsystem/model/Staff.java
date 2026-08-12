package sunrisedentalsystem.model;

public class Staff extends User {

    private String staffName;
    private String contactNumber;

    public Staff() {
    }

    public Staff(int userId, String username, String password,
                 String staffName, String contactNumber) {

        super(userId, username, password);
        this.staffName = staffName;
        this.contactNumber = contactNumber;
    }

    @Override
    public String getRole() {
        return "STAFF";
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}