package HotelBookingSystem;

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

    // Kept for backward compatibility (CUI / lightweight construction)
    public User(String name, String email) {
        this.userId = -1;
        this.name = name;
        this.email = email;
        this.role = null;
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
}