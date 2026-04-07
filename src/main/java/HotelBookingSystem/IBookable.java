package HotelBookingSystem;

import java.time.LocalDate;

public interface IBookable {

    boolean checkAvailability(LocalDate start, LocalDate end);

    void reserve(LocalDate start, LocalDate end);

    void release(LocalDate start, LocalDate end);
}
