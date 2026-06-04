package HotelBookingSystem.db;

import HotelBookingSystem.db.DataBaseStorage;
import HotelBookingSystem.db.DatabaseInitializer;
import HotelBookingSystem.model.Room;
import HotelBookingSystem.model.RoomStatus;
import HotelBookingSystem.model.RoomType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public final class DBManager {

    private static final String DEFAULT_URL = "jdbc:derby:HotelBookingSystemDB_Ebd;create=true";
    private final String url; // for tests
    private Connection conn;

    public DBManager() {
        this.url = DEFAULT_URL;
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
}
