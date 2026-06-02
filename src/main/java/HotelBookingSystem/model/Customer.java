package HotelBookingSystem.model;

import HotelBookingSystem.model.CardDetails;
import HotelBookingSystem.model.DateRange;
import HotelBookingSystem.model.UserRole;
import HotelBookingSystem.model.Room;
import HotelBookingSystem.model.User;
import HotelBookingSystem.model.Booking;
import HotelBookingSystem.service.BookingManager;

public class Customer extends User {

    private BookingManager manager;

    // Authenticated customer
    public Customer(int userId, String name, String email, BookingManager manager) {
        super(userId, name, email, UserRole.CUSTOMER);
        this.manager = manager;
    }

    // Lightweight data-holder inside Booking / mapRow
    public Customer(int userId, String name, String email) {
        super(userId, name, email, UserRole.CUSTOMER);
        this.manager = null;
    }

    public Booking createBooking(Room room, DateRange dateRange, CardDetails card) {
        return manager.createBooking(getUserId(), room, dateRange, card);
    }

    public void cancelBooking(int bookingId) {
        manager.cancelBooking(bookingId, getUserId());
    }

    public void requestCancellation(int bookingId) {
        manager.requestCancellation(bookingId, getUserId());
    }

    public void requestCheckout(int bookingId) {
        manager.requestCheckout(bookingId, getUserId());
    }
}
