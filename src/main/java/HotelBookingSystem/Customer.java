package HotelBookingSystem;

public class Customer extends User {

    private BookingManager manager;

    // Authenticated customer (GUI path)
    public Customer(int userId, String name, String email, BookingManager manager) {
        super(userId, name, email, UserRole.CUSTOMER);
        this.manager = manager;
    }

    // Kept for backward compatibility (CUI)
    public Customer(String name, String email, BookingManager manager) {
        super(name, email);
        this.manager = manager;
    }

    // Kept for backward compatibility (lightweight data-holder inside Booking / mapRow)
    public Customer(String name, String email) {
        super(name, email);
        this.manager = null;
    }

    public Booking createBooking(Customer customer, Room room, DateRange dateRange) {
        return manager.createBooking(customer, room, dateRange);
    }

    // GUI path — verified by userId
    public void cancelBooking(int bookingId) {
        manager.cancelBooking(bookingId, getUserId());
    }

    // CUI backward compat path — verified by email
    public void cancelBooking(int bookingId, String customerEmail) {
        manager.cancelBooking(bookingId, customerEmail);
    }

    // GUI path — verified by userId
    public void requestCancellation(int bookingId) {
        manager.requestCancellation(bookingId, getUserId());
    }

    // CUI backward compat path — verified by email
    public void requestCancellation(int bookingId, String customerEmail) {
        manager.requestCancellation(bookingId, customerEmail);
    }

    // GUI path — verified by userId
    public void requestCheckout(int bookingId) {
        manager.requestCheckout(bookingId, getUserId());
    }

    // CUI backward compat path — verified by email
    public void requestCheckout(int bookingId, String customerEmail) {
        manager.requestCheckout(bookingId, customerEmail);
    }

}