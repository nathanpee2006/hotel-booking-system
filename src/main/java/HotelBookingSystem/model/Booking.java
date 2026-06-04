package HotelBookingSystem.model;

public class Booking {

    private int bookingId;
    private int userId;
    private Customer customer;    // kept for CUI backward compatibility
    private Room room;
    private DateRange dateRange;
    private BookingStatus bookingStatus;

    /**
     * Constructor for creating a new booking. bookingId defaults to 0 — DB
     * assigns the real ID after save().
     */
    public Booking(int userId, Room room, DateRange dateRange, BookingStatus bookingStatus) {
        this.bookingId = 0;
        this.userId = userId;
        this.room = room;
        this.dateRange = dateRange;
        this.bookingStatus = bookingStatus;
    }

    /**
     * Constructor for loading an existing booking from the DB. bookingId is
     * already known.
     */
    public Booking(int bookingId, int userId, Room room, DateRange dateRange, BookingStatus bookingStatus) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.room = room;
        this.dateRange = dateRange;
        this.bookingStatus = bookingStatus;
    }
    
    @Override
    public String toString() {
        return "Booking #" + bookingId +
               " | Room " + room.getRoomId() +
               " | " + dateRange.getStart() +
               " → " + dateRange.getEnd() +
               " | " + bookingStatus;
    }


    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getUserId() {
        return userId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Room getRoom() {
        return room;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public double getAmount() {
        return room.getPrice();
    }

    public void setBookingStatus(BookingStatus status) {
        this.bookingStatus = status;
    }
}
