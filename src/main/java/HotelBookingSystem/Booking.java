package HotelBookingSystem;

public class Booking implements ICancelable {

    private int bookingId;
    private int userId;           
    private Customer customer;    // kept for CUI backward compatibility
    private Room room;
    private DateRange dateRange;
    private BookingStatus bookingStatus;

    // Full constructor — used by JdbcBookingRepository (has both userId and Customer)
    public Booking(int bookingId, int userId, Customer customer, Room room, DateRange dateRange, BookingStatus bookingStatus) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.customer = customer;
        this.room = room;
        this.dateRange = dateRange;
        this.bookingStatus = bookingStatus;
    }

    // Old constructor — kept for CUI and CSV BookingRepository backward compatibility
    public Booking(int bookingId, Customer customer, Room room, DateRange dateRange, BookingStatus bookingStatus) {
        this.bookingId = bookingId;
        this.userId = (customer != null && customer.isAuthenticated()) ? customer.getUserId() : -1;
        this.customer = customer;
        this.room = room;
        this.dateRange = dateRange;
        this.bookingStatus = bookingStatus;
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

    @Override
    public void cancel() {
        bookingStatus = BookingStatus.CANCELLED;
    }

    public void completeBooking() {
        bookingStatus = BookingStatus.COMPLETED;
    }

    public void requestCancellation() {
        bookingStatus = BookingStatus.CANCELLATION_REQUESTED;
    }

    public void setBookingStatus(BookingStatus status) {
        this.bookingStatus = status;
    }
}