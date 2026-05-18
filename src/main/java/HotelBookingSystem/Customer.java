package HotelBookingSystem;

public class Customer extends User {

    private BookingManager manager;

    // Authenticated customer (from DB login)
    public Customer(int userId, String name, String email, BookingManager manager) {
        super(userId, name, email, UserRole.CUSTOMER);
        this.manager = manager;
    }

    // Kept for backward compatibility (CUI / lightweight construction inside Booking)
    public Customer(String name, String email, BookingManager manager) {
        super(name, email);
        this.manager = manager;
    }

    // Kept for backward compatibility (data-holder use inside BookingRepository / mapRow)
    public Customer(String name, String email) {
        super(name, email);
        this.manager = null;
    }

    public Booking createBooking(Customer customer, Room room, DateRange dateRange) {
        return manager.createBooking(customer, room, dateRange);
    }

    public void cancelBooking(int bookingId, String customerEmail) {
        manager.cancelBooking(bookingId, customerEmail);
    }

    public void requestCancellation(int bookingId, String customerEmail) {
        manager.requestCancellation(bookingId, customerEmail);
    }

    public void requestCheckout(int bookingId, String customerEmail) {
        manager.requestCheckout(bookingId, customerEmail);
    }

}