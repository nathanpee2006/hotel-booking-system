package HotelBookingSystem.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.mindrot.jbcrypt.BCrypt;

public class DataBaseStorage {

    private final Connection conn;

    public DataBaseStorage(Connection conn) {
        this.conn = conn;
    }

    public void seed() {
        insertRooms();
        insertDemoUsers(conn);
    }

    private boolean isRoomsTableEmpty() throws SQLException {
        String sql = "SELECT COUNT(*) FROM ROOMS";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            rs.next();
            return rs.getInt(1) == 0;
        }
    }

    private void insertRooms() {
        try {
            if (!isRoomsTableEmpty()) {
                System.out.println("ROOMS already has data; skipping seed.");
                return;
            }
        } catch (SQLException ex) {
            System.out.println("Could not check ROOMS count: " + ex.getMessage());
            return;
        }

        String sql = """
                INSERT INTO ROOMS (room_id, room_type, price, room_status) VALUES
                (101, 'SINGLE',     120.0, 'AVAILABLE'),
                (102, 'SINGLE',     120.0, 'AVAILABLE'),
                (103, 'SINGLE',     120.0, 'AVAILABLE'),
                (104, 'SINGLE',     120.0, 'AVAILABLE'),
                (105, 'SINGLE',     120.0, 'AVAILABLE'),
                (201, 'DOUBLE',     180.0, 'AVAILABLE'),
                (202, 'DOUBLE',     180.0, 'AVAILABLE'),
                (203, 'DOUBLE',     180.0, 'AVAILABLE'),
                (204, 'DOUBLE',     180.0, 'AVAILABLE'),
                (205, 'DOUBLE',     180.0, 'AVAILABLE'),
                (301, 'TWIN',       150.0, 'AVAILABLE'),
                (302, 'TWIN',       150.0, 'AVAILABLE'),
                (303, 'TWIN',       150.0, 'AVAILABLE'),
                (304, 'TWIN',       150.0, 'AVAILABLE'),
                (305, 'TWIN',       150.0, 'AVAILABLE'),
                (401, 'SUITE',      200.0, 'AVAILABLE'),
                (402, 'SUITE',      200.0, 'AVAILABLE'),
                (403, 'SUITE',      200.0, 'AVAILABLE'),
                (404, 'SUITE',      200.0, 'AVAILABLE'),
                (405, 'DELUXE',     300.0, 'AVAILABLE'),
                (501, 'DELUXE',     300.0, 'AVAILABLE'),
                (502, 'DELUXE',     300.0, 'AVAILABLE'),
                (503, 'PENTHOUSE',  500.0, 'AVAILABLE'),
                (504, 'PENTHOUSE',  500.0, 'AVAILABLE'),
                (505, 'PENTHOUSE',  500.0, 'AVAILABLE')
                """;

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Sample ROOMS inserted.");
        } catch (SQLException ex) {
            System.out.println("Failed to seed ROOMS: " + ex.getMessage());
        }
    }
    
    public boolean insertUser(String name, String email, String password, String role) {
    String sql = """
        INSERT INTO USERS (name, email, password, role)
        VALUES (?, ?, ?, ?)
    """;

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, name);
        stmt.setString(2, email);
        stmt.setString(3, password);
        stmt.setString(4, role);
        stmt.executeUpdate();
        System.out.println("User inserted: " + email);
        return true;

    } catch (SQLException ex) {
        System.out.println("Failed to insert user: " + ex.getMessage());
        return false;
    }
    }
    
    public void insertDemoUsers(Connection conn) {
    String sql = "INSERT INTO USERS (name, email, password, role) VALUES (?, ?, ?, ?)";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        // Hash passwords
        String clerkHash = BCrypt.hashpw("hclerkdemo", BCrypt.gensalt());
        String customerHash = BCrypt.hashpw("customerdemo", BCrypt.gensalt());

        // Clerk demo
        stmt.setString(1, "HotelClerk Demo");
        stmt.setString(2, "hclerkdemo@aut.nz");
        stmt.setString(3, clerkHash);
        stmt.setString(4, "CLERK");
        stmt.executeUpdate();

        // Customer demo
        stmt.setString(1, "Customer Demo");
        stmt.setString(2, "customerdemo@aut.nz");
        stmt.setString(3, customerHash);
        stmt.setString(4, "CUSTOMER");
        stmt.executeUpdate();

        System.out.println("Demo users inserted with hashed passwords.");

    }   catch (SQLException ex) {
            System.out.println("Failed to insert demo users: " + ex.getMessage());
        }
    }

    
}
