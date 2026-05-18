package HotelBookingSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcRoomRepository implements IRoomRepository {

    private final DBManager db;

    public JdbcRoomRepository(DBManager db) {
        this.db = db;
    }

    @Override
    public Room getRoomById(int id) {
        String sql = "SELECT room_id, room_type, price, room_status FROM ROOMS WHERE room_id = ?";
        Connection conn = requireConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                int roomId = rs.getInt("room_id");
                RoomType roomType = RoomType.valueOf(rs.getString("room_type"));
                double price = rs.getDouble("price");
                RoomStatus status = RoomStatus.valueOf(rs.getString("room_status"));

                Room room = new Room(roomId, roomType, price);
                room.setStatus(status);
                loadReservations(conn, room);
                return room;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to load room " + id, ex);
        }
    }

    @Override
    public List<Room> getAllRooms() {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT room_id FROM ROOMS ORDER BY room_id";
        Connection conn = requireConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ids.add(rs.getInt("room_id"));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to load room ids", ex);
        }

        List<Room> rooms = new ArrayList<>();
        for (int id : ids) {
            Room room = getRoomById(id);
            if (room != null) {
                rooms.add(room);
            }
        }
        return rooms;
    }

    /**
     * Updates only the ROOMS row (status, type, price). Reservation
     * insert/delete is handled by JdbcBookingRepository since reservations are
     * booking-scoped.
     */
    @Override
    public void updateRoom(Room room) {
        String sql = """
            UPDATE ROOMS SET room_type = ?, price = ?, room_status = ?
            WHERE room_id = ?
            """;

        try (PreparedStatement ps = requireConnection().prepareStatement(sql)) {
            ps.setString(1, room.getRoomType().name());
            ps.setDouble(2, room.getPrice());
            ps.setString(3, room.getStatus().name());
            ps.setInt(4, room.getRoomId());

            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new RuntimeException("Room not found: " + room.getRoomId());
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to update room " + room.getRoomId(), ex);
        }
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------
    /**
     * Loads reservation date ranges from ROOM_RESERVATIONS for the given room.
     * Uses the booking-scoped rows inserted by JdbcBookingRepository.
     */
    private void loadReservations(Connection conn, Room room) throws SQLException {
        String sql = """
            SELECT start_date, end_date FROM ROOM_RESERVATIONS
            WHERE room_id = ? ORDER BY start_date
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, room.getRoomId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDate start = rs.getDate("start_date").toLocalDate();
                    LocalDate end = rs.getDate("end_date").toLocalDate();
                    room.reserve(start, end);
                }
            }
        }
    }

    private Connection requireConnection() {
        Connection conn = db.getConnection();
        if (conn == null) {
            throw new IllegalStateException("No database connection");
        }
        return conn;
    }
}
