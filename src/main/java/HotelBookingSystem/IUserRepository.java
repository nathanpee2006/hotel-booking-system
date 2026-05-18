package HotelBookingSystem;

public interface IUserRepository {

    void save(String name, String email, String hashedPassword, UserRole role);

    User findByEmail(String email);

    User findById(int userId);

    // Returns the stored BCrypt hash for the given email, or null if not found.
    // Intentionally kept on the repo so the hash never travels through the User object.
    String findPasswordHash(String email);

}