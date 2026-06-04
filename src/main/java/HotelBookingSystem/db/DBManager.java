package HotelBookingSystem.db;

import HotelBookingSystem.model.Booking;
import HotelBookingSystem.model.Room;
import HotelBookingSystem.model.RoomStatus;
import HotelBookingSystem.model.RoomType;
import HotelBookingSystem.db.DataBaseStorage;
import HotelBookingSystem.db.DatabaseInitializer;
import HotelBookingSystem.model.Booking;
import HotelBookingSystem.model.BookingStatus;
import HotelBookingSystem.model.Customer;
import HotelBookingSystem.model.DateRange;
import HotelBookingSystem.model.Room;
import HotelBookingSystem.model.RoomStatus;
import HotelBookingSystem.model.RoomType;
import HotelBookingSystem.model.User;
import HotelBookingSystem.repository.JdbcUserRepository;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class DBManager {

    private static final String DEFAULT_URL = "jdbc:derby:HotelBookingSystemDB_Ebd;create=true";
    private final String url; // for tests
    private Connection conn;

    public DBManager() {
        this.url = DEFAULT_URL;
        establishConnection();
    }

    public DBManager(String url) {
        this.url = url;
        establishConnection();
    }

    public static DBManager startup() {
        DBManager db = new DBManager();
        Connection connection = db.getConnection();
        if (connection == null) {
            throw new IllegalStateException("Could not connect to Derby: " + DEFAULT_URL);
        }
        new DatabaseInitializer(connection).initialize();
        new DataBaseStorage(connection).seed();
        return db;
    }

    public static DBManager startup(String url) {
        DBManager db = new DBManager(url);
        Connection connection = db.getConnection();
        if (connection == null) {
            throw new IllegalStateException("Could not connect to Derby: " + url);
        }
        new DatabaseInitializer(connection).initialize();
        new DataBaseStorage(connection).seed();
        return db;
    }

    public Connection getConnection() {
        return this.conn;
    }

    public void establishConnection() {
        if (this.conn == null) {
            try {
                conn = DriverManager.getConnection(url);
                System.out.println(url + " connected successfully.");
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void closeConnections() {
        if (conn != null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void saveBooking(Booking booking) throws SQLException {
        Connection conn = getConnection();
        String sql = "INSERT INTO BOOKINGS (user_id, room_id, start_date, end_date, booking_status) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setInt(1, booking.getUserId());
        stmt.setInt(2, booking.getRoom().getRoomId());
        stmt.setDate(3, Date.valueOf(booking.getDateRange().getStart()));
        stmt.setDate(4, Date.valueOf(booking.getDateRange().getEnd()));
        stmt.setString(5, booking.getBookingStatus().name());

        stmt.executeUpdate();
    }

    public Room findRoomById(int roomId) {
        String sql = "SELECT room_id, room_type, price, room_status FROM ROOMS WHERE room_id = ?";

        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                int id = rs.getInt("room_id");
                String typeStr = rs.getString("room_type");
                double price = rs.getDouble("price");
                String statusStr = rs.getString("room_status");

                // Convert DB strings → enums
                RoomType type = RoomType.valueOf(typeStr.toUpperCase());
                RoomStatus status = RoomStatus.valueOf(statusStr.toUpperCase());

                // Create Room object
                Room room = new Room(id, type, price);

                // Apply status AFTER construction
                room.setStatus(status);

                return room;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void updateRoomStatus(int roomId, RoomStatus newStatus) {
        String sql = "UPDATE ROOMS SET room_status = ? WHERE room_id = ?";

        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus.name());
            stmt.setInt(2, roomId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Room> findAvailableRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT room_id, room_type, price, room_status FROM ROOMS WHERE room_status = 'AVAILABLE'";

        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("room_id");
                String typeStr = rs.getString("room_type");
                double price = rs.getDouble("price");
                String statusStr = rs.getString("room_status");

                // Convert DB strings → enums
                RoomType type = RoomType.valueOf(typeStr.toUpperCase());
                RoomStatus status = RoomStatus.valueOf(statusStr.toUpperCase());

                Room room = new Room(id, type, price);
                room.setStatus(status);

                rooms.add(room);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rooms;
    }
    
    
    public Booking findBookingById(int id) {
    String sql = "SELECT * FROM BOOKINGS WHERE booking_id = ?";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            
            JdbcUserRepository userRepo = new JdbcUserRepository(this);
            
            int userId = rs.getInt("user_id");

            User user = userRepo.findById(userId);

            Customer customer = new Customer(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                null
            );

            Room room = findRoomById(rs.getInt("room_id"));

            DateRange range = new DateRange(
                rs.getDate("start_date").toLocalDate(),
                rs.getDate("end_date").toLocalDate()
            );

            BookingStatus status = BookingStatus.valueOf(rs.getString("booking_status"));

            return new Booking(id, room, range, status);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

        return null;
    }
    
    public void checkInBooking(int bookingId) {
    String sql = "UPDATE BOOKINGS SET booking_status = 'CHECKED_IN' WHERE booking_id = ?";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, bookingId);
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
        }
    }
    
    public void checkOutBooking(int bookingId) {
        String sql = "UPDATE BOOKINGS SET STATUS = 'CHECKED_OUT' WHERE BOOKING_ID = ?";
        // execute update
    }
    
    public void updateBookingStatus(int bookingId, BookingStatus status) {
        String sql = "UPDATE BOOKINGS SET BOOKING_STATUS = ? WHERE BOOKING_ID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setInt(2, bookingId);
            //stmt.executeUpdate();
            
            int rows = stmt.executeUpdate();
            System.out.println("Rows updated: " + rows);
            System.out.println("Updated booking " + bookingId + " to " + status.name());

            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update booking status: " + e.getMessage());
        }
    }




}
