package HotelBookingSystem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseStorage {

    private final Connection conn;

    public DataBaseStorage(Connection conn) {
        this.conn = conn;
    }

    public void seed() {
        insertRooms();
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
}
