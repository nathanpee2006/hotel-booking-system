package HotelBookingSystem;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseStorage {

    private final Connection conn;

    public DataBaseStorage(Connection conn) {
        this.conn = conn;
    }

    public void seed() {
        insertRooms();
        insertCustomers();
        insertBookings();
    }

    private void insertRooms() {
        String sql = """
            INSERT INTO ROOMS (room_number, type, price, status) VALUES
            (‘201’,’DOUBLE’,180.0,’AVAILABLE’),
            (‘202’,’DOUBLE’,180.0,’AVAILABLE’),
            (‘203’,’DOUBLE’,180.0,’AVAILABLE’),
            (‘204’,’DOUBLE’,180.0,’AVAILABLE’),
            (‘205’,’DOUBLE’,180.0,’AVAILABLE’),
            (‘401’,’SUITE’,200.0,’AVAILABLE’),
            (‘402’,’SUITE’,200.0,’AVAILABLE’),
            (‘403’,’SUITE’,200.0,’AVAILABLE’),
            (‘404’,’SUITE’,200.0,’AVAILABLE’),
            (‘405’,’DELUXE’,300.0,’AVAILABLE’),
            (‘101’,’SINGLE’,120.0,’AVAILABLE’),
            (‘102’,’SINGLE’,120.0,’AVAILABLE’),
            (‘103’,’SINGLE’,120.0,’AVAILABLE’),
            (‘104’,’SINGLE’,120.0,’AVAILABLE’),
            (‘105’,’SINGLE’,120.0,’AVAILABLE’),
            (‘301’,’TWIN’,150.0,’AVAILABLE’),
            (‘302’,’TWIN’,150.0,’AVAILABLE’),
            (‘303’,’TWIN’,150.0,’AVAILABLE’),
            (‘304’,’TWIN’,150.0,’AVAILABLE’),
            (‘305’,’TWIN’,150.0,’AVAILABLE’),
            (‘501’,’DELUXE’,300.0,’AVAILABLE’),
            (‘502’,’DELUXE’,300.0,’AVAILABLE’),
            (‘503’,’PENTHOUSE’,500.0,’AVAILABLE’),
            (‘504’,’PENTHOUSE’,500.0,’AVAILABLE’),
            (‘505’,’PENTHOUSE’,500.0,’AVAILABLE’)
            """;

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Sample ROOMS inserted.");
        } catch (SQLException ex) {
            System.out.println("ROOMS already seeded or error: " + ex.getMessage());
        }
    }

    private void insertCustomers() {
        String sql = "";
/*INSERT INTO CUSTOMERS (first_name, last_name, email, phone) VALUES
            ()*/
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Sample CUSTOMERS inserted.");
        } catch (SQLException ex) {
            System.out.println("CUSTOMERS already seeded or error: " + ex.getMessage());
        }
    }

    private void insertBookings() {
        String sql = "";
/*INSERT INTO BOOKINGS (customer_id, room_id, check_in, check_out, status) VALUES
            ()*/
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Sample BOOKINGS inserted.");
        } catch (SQLException ex) {
            System.out.println("BOOKINGS already seeded or error: " + ex.getMessage());
        }
    }
}
