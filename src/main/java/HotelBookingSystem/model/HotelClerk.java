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

    // Authenticated clerk 
    public HotelClerk(int userId, String name, String email, BookingManager manager) {
        super(userId, name, email, UserRole.CLERK);
        this.manager = manager;
    }

    public HotelClerk(int userId, String name, String email) {
        super(userId, name, email, UserRole.CLERK);
        this.manager = null;
    }

    public Booking createBooking(int userId, Room room, DateRange dateRange, CardDetails card) {
        return manager.createBooking(userId, room, dateRange, card);
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
