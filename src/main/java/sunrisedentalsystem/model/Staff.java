package sunrisedentalsystem.model;

public class Staff extends User {

    private int staffId;

    private String staffName;

    private String contactNumber;

    public Staff() {

    }

    public Staff(
            int userId,
            String username,
            String password,
            String staffName,
            String contactNumber) {

        super(
                userId,
                username,
                password
        );

        this.staffName =
                staffName;

        this.contactNumber =
                contactNumber;
    }

    public Staff(
            int staffId,
            int userId,
            String username,
            String password,
            String staffName,
            String contactNumber) {

        super(
                userId,
                username,
                password
        );

        this.staffId =
                staffId;

        this.staffName =
                staffName;

        this.contactNumber =
                contactNumber;
    }

    @Override
    public String getRole() {

        return "STAFF";
    }

    public int getStaffId() {

        return staffId;
    }

    public void setStaffId(
            int staffId) {

        this.staffId =
                staffId;
    }

    public String getStaffName() {

        return staffName;
    }

    public void setStaffName(
            String staffName) {

        this.staffName =
                staffName;
    }

    public String getContactNumber() {

        return contactNumber;
    }

    public void setContactNumber(
            String contactNumber) {

        this.contactNumber =
                contactNumber;
    }
}