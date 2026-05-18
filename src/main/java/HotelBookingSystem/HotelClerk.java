package HotelBookingSystem;

public class HotelClerk extends User {

    private final BookingManager manager;

    // Authenticated clerk (from DB login)
    public HotelClerk(int userId, String name, String email, BookingManager manager) {
        super(userId, name, email, UserRole.CLERK);
        this.manager = manager;
    }

    // Kept for backward compatibility (CUI)
    public HotelClerk(String name, String email, BookingManager manager) {
        super(name, email);
        this.manager = manager;
    }

    public Booking createBooking(Customer customer, Room room, DateRange dateRange) {
        return manager.createBooking(customer, room, dateRange);
    }

    public void completeBooking(int bookingId) {
        manager.completeBooking(bookingId);
    }

    public void approveCancellation(int bookingId) {
        manager.approveCancellation(bookingId);
    }

    public void confirmCheckout(int bookingId) {
        manager.confirmCheckout(bookingId);
    }

}