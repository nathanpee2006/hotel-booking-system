package HotelBookingSystem.repository;

import HotelBookingSystem.db.DBManager;
import HotelBookingSystem.model.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcBookingRepository implements IBookingRepository {

    private final IRoomRepository roomRepo;
    private final IUserRepository userRepo;
    private final DBManager db;

    public JdbcBookingRepository(IRoomRepository roomRepo, DBManager db, IUserRepository UserRepo) {
        this.roomRepo = roomRepo;
        this.userRepo = UserRepo;
        this.db = db;
    }

    // -------------------------------------------------------------------------
    // IBookingRepository
    // -------------------------------------------------------------------------
    /**
     * Inserts a booking and returns the DB-generated booking_id. Also inserts
     * the corresponding ROOM_RESERVATIONS row in the same transaction.
     */
    @Override
    public int save(Booking booking) {
        String sql = """
            INSERT INTO BOOKINGS (user_id, room_id, start_date, end_date, booking_status)
            VALUES (?, ?, ?, ?, ?)
            """;

        Connection conn = requireConnection();

        try {
            conn.setAutoCommit(false);

            int generatedId;

            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, booking.getUserId());
                ps.setInt(2, booking.getRoom().getRoomId());
                ps.setDate(3, Date.valueOf(booking.getDateRange().getStart()));
                ps.setDate(4, Date.valueOf(booking.getDateRange().getEnd()));
                ps.setString(5, booking.getBookingStatus().name());
                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) {
                        throw new SQLException("No generated key returned for booking insert.");
                    }
                    generatedId = keys.getInt(1);
                }
            }

            booking.setBookingId(generatedId);
            insertReservation(conn, booking);

            conn.commit();
            return generatedId;

        } catch (SQLException ex) {
            rollback(conn);
            throw new RuntimeException("Failed to save booking", ex);
        } finally {
            restoreAutoCommit(conn);
        }
    }

    @Override
    public Booking findById(int id) {

    String sql = """
        SELECT booking_id, user_id, room_id, start_date, end_date, booking_status
        FROM BOOKINGS WHERE booking_id = ?
        """;

    try (PreparedStatement ps = requireConnection().prepareStatement(sql)) {
        ps.setInt(1, id);

        try (ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) {
                System.out.println("findById(" + id + ") returned NULL (no such booking)");
                return null;
            }

            Booking booking = mapRow(rs);

            System.out.println("findById(" + id + ") returned booking with ID: " + booking.getBookingId());
            return booking;
        }

    } catch (SQLException ex) {
        throw new RuntimeException("Failed to find booking " + id, ex);
    }
}


    @Override
    public void update(Booking booking) {
        String sql = """
            UPDATE BOOKINGS SET
                user_id = ?,
                room_id = ?,
                start_date = ?,
                end_date = ?,
                booking_status = ?
            WHERE booking_id = ?
            """;

        try (PreparedStatement ps = requireConnection().prepareStatement(sql)) {
            ps.setInt(1, booking.getUserId());
            ps.setInt(2, booking.getRoom().getRoomId());
            ps.setDate(3, Date.valueOf(booking.getDateRange().getStart()));
            ps.setDate(4, Date.valueOf(booking.getDateRange().getEnd()));
            ps.setString(5, booking.getBookingStatus().name());
            ps.setInt(6, booking.getBookingId());

            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new RuntimeException("Booking not found: " + booking.getBookingId());
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to update booking " + booking.getBookingId(), ex);
        }
    }

    @Override
    public void delete(int bookingId) {
        Connection conn = requireConnection();

        try {
            conn.setAutoCommit(false);

        // 1. Delete reservation first (fixes FK violation)
            try (PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM ROOM_RESERVATIONS WHERE booking_id = ?")) {
            ps.setInt(1, bookingId);
            ps.executeUpdate();
        }

        // 2. Now delete booking
        try (PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM BOOKINGS WHERE booking_id = ?")) {
            ps.setInt(1, bookingId);
            ps.executeUpdate();
        }

        conn.commit();

        }   catch (SQLException ex) {
            rollback(conn);
            throw new RuntimeException("Failed to delete booking " + bookingId, ex);
        }   finally {
            restoreAutoCommit(conn);
        }
    }


    @Override
    public List<Booking> findByStatus(BookingStatus status) {
        String sql = """
            SELECT booking_id, user_id, room_id, start_date, end_date, booking_status
            FROM BOOKINGS WHERE booking_status = ?
            ORDER BY booking_id
            """;

        return queryBookings(sql, ps -> ps.setString(1, status.name()));
    }

    @Override
    public List<Booking> findByUserId(int userId) {
        String sql = """
            SELECT booking_id, user_id, room_id, start_date, end_date, booking_status
            FROM BOOKINGS WHERE user_id = ?
            ORDER BY booking_id
            """;

        return queryBookings(sql, ps -> ps.setInt(1, userId));
    }
    

    // -------------------------------------------------------------------------
    // Reservation management (booking-scoped)
    // -------------------------------------------------------------------------
    private void insertReservation(Connection conn, Booking booking) throws SQLException {
        String sql = """
            INSERT INTO ROOM_RESERVATIONS (room_id, booking_id, start_date, end_date)
            VALUES (?, ?, ?, ?)
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, booking.getRoom().getRoomId());
            ps.setInt(2, booking.getBookingId());
            ps.setDate(3, Date.valueOf(booking.getDateRange().getStart()));
            ps.setDate(4, Date.valueOf(booking.getDateRange().getEnd()));
            ps.executeUpdate();
        }
    }

    /**
     * Deletes the ROOM_RESERVATIONS row for a booking. Called by BookingManager
     * on cancel, approve cancellation, and confirm checkout.
     */
    public void deleteReservation(int bookingId) {
        String sql = "DELETE FROM ROOM_RESERVATIONS WHERE booking_id = ?";

        try (PreparedStatement ps = requireConnection().prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to delete reservation for booking " + bookingId, ex);
        }
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------
    @FunctionalInterface
    private interface PreparedStatementBinder {

        void bind(PreparedStatement ps) throws SQLException;
    }

    private List<Booking> queryBookings(String sql, PreparedStatementBinder binder) {
        List<Booking> result = new ArrayList<>();

        try (PreparedStatement ps = requireConnection().prepareStatement(sql)) {
            binder.bind(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Booking booking = mapRow(rs);
                    if (booking != null) {
                        result.add(booking);
                    }
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to query bookings", ex);
        }

        return result;
    }

    private Booking mapRow(ResultSet rs) throws SQLException {
        int bookingId = rs.getInt("booking_id");
        int userId = rs.getInt("user_id");
        int roomId = rs.getInt("room_id");
        LocalDate start = rs.getDate("start_date").toLocalDate();
        LocalDate end = rs.getDate("end_date").toLocalDate();
        BookingStatus status = BookingStatus.valueOf(rs.getString("booking_status"));

        Room room = roomRepo.getRoomById(roomId);
        if (room == null) {
            System.out.println("Skipping booking " + bookingId + ": room " + roomId + " not found.");
            return null;
        }

        return new Booking(bookingId, userId, room, new DateRange(start, end), status);
    }

    private void rollback(Connection conn) {
        try {
            conn.rollback();
        } catch (SQLException ex) {
            System.out.println("Rollback failed: " + ex.getMessage());
        }
    }

    private void restoreAutoCommit(Connection conn) {
        try {
            conn.setAutoCommit(true);
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to restore auto-commit", ex);
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
