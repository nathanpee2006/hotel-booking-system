package HotelBookingSystem.model;

public abstract class User {

    private int userId;
    private String name;
    private String email;
    private UserRole role;

    // Used by authenticated users (from DB)
    public User(int userId, String name, String email, UserRole role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }

    public boolean isAuthenticated() {
        return userId != -1;
    }
    
    public void setRole(UserRole role){
        this.role = role;
    }
}
