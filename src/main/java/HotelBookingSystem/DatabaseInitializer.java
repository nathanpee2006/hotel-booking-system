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
        createUsersTable();
        createBookingsTable();
        createRoomReservationsTable();
        createPaymentsTable();
    }

    /*
    ROOMS → BOOKINGS: one-to-many (one room, many bookings over time)
    BOOKINGS → ROOM_RESERVATIONS (availability table): one-to-one (one booking creates one reservation block)
     */
    
    private void createRoomsTable() {
        String sql = """
            CREATE TABLE ROOMS (
                room_id INT NOT NULL PRIMARY KEY,
                room_type VARCHAR(20) NOT NULL,
                price DOUBLE NOT NULL,
                room_status VARCHAR(20) NOT NULL
            )
            """;

        executeCreate(sql, "ROOMS");
    }

    private void createUsersTable() {
        String sql = """
            CREATE TABLE USERS (
                user_id  INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                name     VARCHAR(120) NOT NULL,
                email    VARCHAR(255) NOT NULL UNIQUE,
                password VARCHAR(255) NOT NULL,
                role     VARCHAR(10)  NOT NULL
            )
            """;

        executeCreate(sql, "USERS");
    }

    private void createBookingsTable() {
        String sql = """
            CREATE TABLE BOOKINGS (
                booking_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                user_id INT NOT NULL,
                room_id INT NOT NULL,
                start_date DATE NOT NULL,
                end_date DATE NOT NULL,
                booking_status VARCHAR(30) NOT NULL,
                CONSTRAINT fk_booking_user FOREIGN KEY (user_id) REFERENCES USERS(user_id),
                CONSTRAINT fk_booking_room FOREIGN KEY (room_id) REFERENCES ROOMS(room_id)
            )
            """;

        executeCreate(sql, "BOOKINGS");
    }

    private void createRoomReservationsTable() {
        String sql = """
            CREATE TABLE ROOM_RESERVATIONS (
                reservation_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                room_id INT NOT NULL,
                booking_id INT NOT NULL,
                start_date DATE NOT NULL,
                end_date DATE NOT NULL,
                CONSTRAINT fk_res_room FOREIGN KEY (room_id) REFERENCES ROOMS(room_id),
                CONSTRAINT fk_reservation_booking FOREIGN KEY (booking_id) REFERENCES BOOKINGS(booking_id)
            )
            """;

        executeCreate(sql, "ROOM_RESERVATIONS");
    }

    // TBD
    private void createPaymentsTable() {
        String sql = """
            CREATE TABLE PAYMENTS (
                payment_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                booking_id INT NOT NULL,
                amount DECIMAL(10,2) NOT NULL,
                method VARCHAR(20) NOT NULL,
                status VARCHAR(20) DEFAULT 'PENDING',
                paid_at TIMESTAMP,
                CONSTRAINT fk_payment_booking FOREIGN KEY (booking_id) REFERENCES BOOKINGS(booking_id)
            )
        """;

        executeCreate(sql, "PAYMENTS");
    }

    private void executeCreate(String sql, String label) {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Created: " + label);
        } catch (SQLException ex) {
            if ("X0Y32".equals(ex.getSQLState())) {
                System.out.println("Already exists: " + label);
            } else {
                System.out.println("Error creating " + label + ": " + ex.getMessage());
            }
        }
    }
}
