package HotelBookingSystem;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomRepository implements IRoomRepository {

    private static final String ROOMS_FILE = "./resources/rooms.csv";
    private static final String RESERVATIONS_FILE = "./resources/room_reservations.csv";

    private final Map<Integer, Room> rooms;

    public RoomRepository() {
        this.rooms = new HashMap<>();
        loadRoomsFromFile();
        loadReservationsFromFile();
    }

    // -------------------------------------------------------------------------
    // IRoomRepository
    // -------------------------------------------------------------------------
    @Override
    public Room getRoomById(int id) {
        return rooms.get(id);
    }

    @Override
    public void updateRoom(Room room) {
        rooms.put(room.getRoomId(), room);
        saveRoomsToFile();
        saveReservationsToFile();
    }

    @Override
    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms.values());
    }

    // -------------------------------------------------------------------------
    // File I/O — Rooms
    // -------------------------------------------------------------------------
    /**
     * CSV format (no header): roomId, roomType, price, roomStatus
     */
    private void loadRoomsFromFile() {
        File file = new File(ROOMS_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length != 4) {
                    System.out.println("Skipping malformed room line: " + line);
                    continue;
                }

                try {
                    int roomId = Integer.parseInt(parts[0].trim());
                    RoomType roomType = RoomType.valueOf(parts[1].trim());
                    double price = Double.parseDouble(parts[2].trim());
                    RoomStatus status = RoomStatus.valueOf(parts[3].trim());

                    Room room = new Room(roomId, roomType, price);
                    room.setStatus(status);
                    rooms.put(roomId, room);

                } catch (Exception e) {
                    System.out.println("Error parsing room line: " + line + " — " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Could not load rooms: " + e.getMessage());
        }
    }

    private void saveRoomsToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ROOMS_FILE))) {
            for (Room r : rooms.values()) {
                String line = String.join(",",
                        String.valueOf(r.getRoomId()),
                        r.getRoomType().name(),
                        String.valueOf(r.getPrice()),
                        r.getStatus().name()
                );
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Could not save rooms: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // File I/O — Reservations
    // -------------------------------------------------------------------------
    /**
     * CSV format (no header): roomId, startDate, endDate
     */
    private void loadReservationsFromFile() {
        File file = new File(RESERVATIONS_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length != 3) {
                    System.out.println("Skipping malformed reservation line: " + line);
                    continue;
                }

                try {
                    int roomId = Integer.parseInt(parts[0].trim());
                    LocalDate start = LocalDate.parse(parts[1].trim());
                    LocalDate end = LocalDate.parse(parts[2].trim());

                    Room room = rooms.get(roomId);
                    if (room == null) {
                        System.out.println("Skipping reservation: room " + roomId + " not found.");
                        continue;
                    }

                    room.reserve(start, end);

                } catch (Exception e) {
                    System.out.println("Error parsing reservation line: " + line + " — " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Could not load reservations: " + e.getMessage());
        }
    }

    private void saveReservationsToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RESERVATIONS_FILE))) {
            for (Room r : rooms.values()) {
                for (LocalDate[] range : r.getReservationRanges()) {
                    String line = String.join(",",
                            String.valueOf(r.getRoomId()),
                            range[0].toString(),
                            range[1].toString()
                    );
                    bw.write(line);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Could not save reservations: " + e.getMessage());
        }
    }
}
