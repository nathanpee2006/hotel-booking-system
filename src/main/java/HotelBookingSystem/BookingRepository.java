package HotelBookingSystem;

import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingRepository implements IBookingRepository {

    private static final String FILE_PATH = "./resources/bookings.csv";

    private final Map<Integer, Booking> bookings = new HashMap<>();
    private final IRoomRepository roomRepo;

    public BookingRepository(IRoomRepository roomRepo) {
        this.roomRepo = roomRepo;
        loadFromFile();
    }

    // -------------------------------------------------------------------------
    // IBookingRepository
    // -------------------------------------------------------------------------
    @Override
    public void save(Booking booking) {
        bookings.put(booking.getBookingId(), booking);
        saveToFile();
    }

    @Override
    public Booking findById(int id) {
        return bookings.get(id);
    }

    @Override
    public void update(Booking booking) {
        bookings.put(booking.getBookingId(), booking);
        saveToFile();
    }

    @Override
    public int generateId() {
        return bookings.keySet().stream().mapToInt(i -> i).max().orElse(0) + 1;
    }

    @Override
    public List<Booking> findByStatus(BookingStatus status) {
        return bookings.values().stream()
                .filter(b -> b.getBookingStatus() == status)
                .toList();
    }

    @Override
    public List<Booking> findByEmail(String email) {
        return bookings.values().stream()
                .filter(b -> b.getCustomer().getEmail().equalsIgnoreCase(email))
                .toList();
    }

    // -------------------------------------------------------------------------
    // File I/O
    // -------------------------------------------------------------------------
    /**
     * CSV format (no header): bookingId, customerName, customerEmail, roomId,
     * startDate, endDate, bookingStatus
     */
    private void loadFromFile() {
        File file = new File(FILE_PATH);
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
                if (parts.length != 7) {
                    System.out.println("Skipping malformed booking line: " + line);
                    continue;
                }

                try {
                    int bookingId = Integer.parseInt(parts[0].trim());
                    String custName = parts[1].trim();
                    String custEmail = parts[2].trim();
                    int roomId = Integer.parseInt(parts[3].trim());
                    LocalDate start = LocalDate.parse(parts[4].trim());
                    LocalDate end = LocalDate.parse(parts[5].trim());
                    BookingStatus status = BookingStatus.valueOf(parts[6].trim());

                    Customer customer = new Customer(custName, custEmail, null);
                    Room room = roomRepo.getRoomById(roomId);

                    if (room == null) {
                        System.out.println("Skipping booking " + bookingId + ": room " + roomId + " not found.");
                        continue;
                    }

                    DateRange dateRange = new DateRange(start, end);
                    Booking booking = new Booking(bookingId, customer, room, dateRange, status);
                    bookings.put(bookingId, booking);

                } catch (Exception e) {
                    System.out.println("Error parsing booking line: " + line + " — " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Could not load bookings: " + e.getMessage());
        }
    }

    private void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Booking b : bookings.values()) {
                String line = String.join(",",
                        String.valueOf(b.getBookingId()),
                        b.getCustomer().getName(),
                        b.getCustomer().getEmail(),
                        String.valueOf(b.getRoom().getRoomId()),
                        b.getDateRange().getStart().toString(),
                        b.getDateRange().getEnd().toString(),
                        b.getBookingStatus().name()
                );
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Could not save bookings: " + e.getMessage());
        }
    }
}
