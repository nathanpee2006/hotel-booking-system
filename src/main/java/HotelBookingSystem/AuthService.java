package HotelBookingSystem;

import org.mindrot.jbcrypt.BCrypt;

public class AuthService {

    private final IUserRepository userRepo;
    private final BookingManager manager;

    public AuthService(IUserRepository userRepo, BookingManager manager) {
        this.userRepo = userRepo;
        this.manager = manager;
    }

    /**
     * Registers a new user. Throws if the email is already taken.
     */
    public void register(String name, String email, String plainPassword, UserRole role) {
        if (userRepo.findByEmail(email) != null) {
            throw new IllegalArgumentException("An account with this email already exists.");
        }

        String hashed = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        userRepo.save(name, email, hashed, role);
    }

    /**
     * Authenticates a user by email and password. Returns a fully wired
     * Customer or HotelClerk on success. Throws IllegalArgumentException if
     * credentials are invalid.
     */
    public User login(String email, String plainPassword) {
        String storedHash = userRepo.findPasswordHash(email);

        // Use the same generic message for both cases to avoid email enumeration
        if (storedHash == null || !BCrypt.checkpw(plainPassword, storedHash)) {
            throw new IllegalArgumentException("Invalid email or password.");
        }

        User user = userRepo.findByEmail(email);

        // Attach BookingManager now that the user is authenticated
        return switch (user.getRole()) {
            case CUSTOMER ->
                new Customer(user.getUserId(), user.getName(), user.getEmail(), manager);
            case CLERK ->
                new HotelClerk(user.getUserId(), user.getName(), user.getEmail(), manager);
        };
    }

}
