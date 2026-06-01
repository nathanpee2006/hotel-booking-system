package HotelBookingSystem;

public abstract class User {

    private int userId;
    private String name;
    private String email;
    private UserRole role;
    private String password;

    // Used by authenticated users (from DB)
    public User(int userId, String name, String email, UserRole role, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.password = password;
    }

    // Kept for backward compatibility (CUI / lightweight construction)
    public User(String name, String email) {
        this.userId = -1;
        this.name = name;
        this.email = email;
        this.role = null;
    }
    
    public User(String name, String email, String password, UserRole role) {
        this.userId = -1; 
        this.name = name;
        this.email = email;
        this.password = password;
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
    
    public String getPasswrod() {
        return password;
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
