package HotelBookingSystem;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    private final Connection conn;

    public DatabaseInitializer(Connection conn) {
        this.conn = conn;
    }

    public void initialize() {
        createRoomsTable();
        createCustomersTable();
        createBookingsTable();
        createPaymentsTable();
        createClerksTable();
        createRoomReservationsTable();
    }

    private void createRoomsTable() {
        String sql = """
            CREATE TABLE ROOMS (
                room_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                room_number VARCHAR(10) NOT NULL,
                type VARCHAR(20) NOT NULL,
                price DECIMAL(10,2) NOT NULL,
                status VARCHAR(20) DEFAULT 'AVAILABLE'
            )
        """;

        executeCreate(sql, "ROOMS");
    }

    private void createCustomersTable() {
        String sql = """
            CREATE TABLE CUSTOMERS (
                customer_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                first_name VARCHAR(50) NOT NULL,
                last_name VARCHAR(50) NOT NULL,
                email VARCHAR(100) UNIQUE NOT NULL,
                phone VARCHAR(20)
            )
        """;

        executeCreate(sql, "CUSTOMERS");
    }

    private void createBookingsTable() {
        String sql = """
            CREATE TABLE BOOKINGS (
                booking_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                customer_id INT NOT NULL,
                room_id INT NOT NULL,
                check_in DATE NOT NULL,
                check_out DATE NOT NULL,
                status VARCHAR(20) DEFAULT 'PENDING',
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                     
                CONSTRAINT fk_booking_customer
                    FOREIGN KEY (customer_id) REFERENCES CUSTOMERS(customer_id),
                CONSTRAINT fk_booking_room
                    FOREIGN KEY (room_id) REFERENCES ROOMS(room_id)
            )
        """;

        executeCreate(sql, "BOOKINGS");
    }

    private void createPaymentsTable() {
        String sql = """
            CREATE TABLE PAYMENTS (
                payment_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                booking_id INT NOT NULL,
                amount DECIMAL(10,2) NOT NULL,
                method VARCHAR(20) NOT NULL,
                status VARCHAR(20) DEFAULT 'PENDING',
                paid_at TIMESTAMP,
                CONSTRAINT fk_payment_booking
                    FOREIGN KEY (booking_id) REFERENCES BOOKINGS(booking_id)
            )
        """;

        executeCreate(sql, "PAYMENTS");
    }

    private void createClerksTable() {
        String sql = """
            CREATE TABLE CLERKS (
                clerk_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                username VARCHAR(50) UNIQUE NOT NULL,
                password VARCHAR(100) NOT NULL,
                role VARCHAR(20) DEFAULT 'CLERK'
            )
        """;

        executeCreate(sql, "CLERKS");
    }

    private void createRoomReservationsTable() {
        String sql = """
            CREATE TABLE ROOM_RESERVATIONS (
                reservation_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                room_id INT NOT NULL,
                booking_id INT NOT NULL,
                date DATE NOT NULL,
                CONSTRAINT fk_reservation_room
                    FOREIGN KEY (room_id) REFERENCES ROOMS(room_id),
                CONSTRAINT fk_reservation_booking
                    FOREIGN KEY (booking_id) REFERENCES BOOKINGS(booking_id)
            )
        """;

        executeCreate(sql, "ROOM_RESERVATIONS");
    }

    private void executeCreate(String sql, String tableName) {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Created table: " + tableName);
        } catch (SQLException ex) {
            if ("X0Y32".equals(ex.getSQLState())) {
                System.out.println("Table already exists: " + tableName);
            } else {
                System.out.println("Error creating table " + tableName + ": " + ex.getMessage());
            }
        }
    }
}
