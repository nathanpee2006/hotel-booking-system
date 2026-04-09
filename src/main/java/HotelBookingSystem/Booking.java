package HotelBookingSystem;

public class Booking implements ICancelable {

    private int bookingId;
    private Customer customer;
    private Room room;
    private DateRange dateRange;
    private BookingStatus bookingStatus;

    public Booking(int newId, Customer customer, Room room, DateRange dateRange, BookingStatus pending) {
        this.bookingId = newId;
        this.customer = customer;
        this.room = room;
        this.dateRange = dateRange;
        this.bookingStatus = pending;
    }

    public int getBookingId() {
        return bookingId;
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
