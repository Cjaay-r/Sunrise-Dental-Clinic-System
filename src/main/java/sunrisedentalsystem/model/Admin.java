package sunrisedentalsystem.model;

public class Admin extends User {

    private String adminName;

    public Admin() {
    }

    public Admin(int userId, String username,
                 String password, String adminName) {

        super(userId, username, password);
        this.adminName = adminName;
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }
}