package HotelBookingSystem;

import java.time.LocalDate;

public class DateRange {

    private LocalDate start, end;

    public DateRange(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public boolean overlaps(LocalDate s, LocalDate e) {
        return !(e.isBefore(start) || s.isAfter(end));
    }

    public boolean matches(LocalDate s, LocalDate e) {
        return start.equals(s) && end.equals(e);
    }
}
