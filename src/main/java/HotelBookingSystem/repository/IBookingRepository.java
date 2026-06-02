package HotelBookingSystem.repository;

import HotelBookingSystem.model.Booking;
import HotelBookingSystem.model.BookingStatus;
import java.util.List;

public interface IBookingRepository {

    /**
     * Persists a new booking and returns the generated booking_id.
     */
    int save(Booking booking);

    Booking findById(int id);

    void update(Booking booking);

    List<Booking> findByStatus(BookingStatus status);

    List<Booking> findByUserId(int userId);

    void delete(int bookingId);
}
