package HotelBookingSystem;

import java.util.List;

public interface IBookingRepository {

    /**
     * Persists a new booking and returns the generated booking_id.
     */
    int save(Booking booking);

    Booking findById(int id);

    void update(Booking booking);

    List<Booking> findByStatus(BookingStatus status);

    List<Booking> findByEmail(String email);

    List<Booking> findByUserId(int userId);

}
