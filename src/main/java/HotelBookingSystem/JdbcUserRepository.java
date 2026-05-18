package HotelBookingSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcUserRepository implements IUserRepository {

    private final DBManager db;

    public JdbcUserRepository(DBManager db) {
        this.db = db;
    }

    @Override
    public void save(String name, String email, String hashedPassword, UserRole role) {
        String sql = """
            INSERT INTO USERS (name, email, password, role)
            VALUES (?, ?, ?, ?)
            """;

        try (PreparedStatement ps = requireConnection().prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, hashedPassword);
            ps.setString(4, role.name());
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to save user: " + email, ex);
        }
    }

    @Override
    public User findByEmail(String email) {
        String sql = """
            SELECT user_id, name, email, password, role
            FROM USERS WHERE LOWER(email) = LOWER(?)
            """;

        try (PreparedStatement ps = requireConnection().prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return mapRow(rs);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to find user by email: " + email, ex);
        }
    }

    @Override
    public User findById(int userId) {
        String sql = """
            SELECT user_id, name, email, password, role
            FROM USERS WHERE user_id = ?
            """;

        try (PreparedStatement ps = requireConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return mapRow(rs);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to find user by id: " + userId, ex);
        }
    }

    @Override
    public String findPasswordHash(String email) {
        String sql = "SELECT password FROM USERS WHERE LOWER(email) = LOWER(?)";

        try (PreparedStatement ps = requireConnection().prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return rs.getString("password");
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to fetch password hash for: " + email, ex);
        }
    }

    // mapRow intentionally does not attach a BookingManager — AuthService handles that
    private User mapRow(ResultSet rs) throws SQLException {
        int userId = rs.getInt("user_id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        UserRole role = UserRole.valueOf(rs.getString("role"));

        return switch (role) {
            case CUSTOMER -> new Customer(userId, name, email, null);
            case CLERK    -> new HotelClerk(userId, name, email, null);
        };
    }

    private Connection requireConnection() {
        Connection conn = db.getConnection();
        if (conn == null) {
            throw new IllegalStateException("No database connection");
        }
        return conn;
    }

}