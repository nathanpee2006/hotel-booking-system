package HotelBookingSystem;

import java.util.List;

public interface IBookingRepository {

    public void save(Booking booking);

    public Booking findById(int id);

    public void update(Booking booking);

    public int generateId();

    public List<Booking> findByStatus(BookingStatus status);

}
