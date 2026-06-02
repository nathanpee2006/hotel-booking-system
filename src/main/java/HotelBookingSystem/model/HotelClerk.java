package HotelBookingSystem.model;

import HotelBookingSystem.model.CardDetails;
import HotelBookingSystem.model.DateRange;
import HotelBookingSystem.model.UserRole;
import HotelBookingSystem.model.Room;
import HotelBookingSystem.model.User;
import HotelBookingSystem.model.Booking;
import HotelBookingSystem.service.BookingManager;

public class HotelClerk extends User {

    private final BookingManager manager;

    // Authenticated clerk (from DB login)
    public HotelClerk(int userId, String name, String email, String password, BookingManager manager) {
        super(userId, name, email, UserRole.CLERK, password);
        this.manager = manager;
    }

    // Kept for backward compatibility (CUI)
    public HotelClerk(String name, String email, BookingManager manager) {
        super(name, email);
        this.manager = manager;
    }

    // GUI path — with payment
    public Booking createBooking(Customer customer, Room room, DateRange dateRange, CardDetails card) {
        return manager.createBooking(customer, room, dateRange, card);
    }

    // CUI backward compat path — no payment
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
