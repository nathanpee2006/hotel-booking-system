package HotelBookingSystem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Room implements IBookable {

    private int roomId;
    private RoomType roomType;
    private double price;
    private RoomStatus status;

    private List<DateRange> reservations = new ArrayList<>();

    public Room(int roomId, RoomType roomType, double price) {
        this.roomId = roomId;
        this.roomType = roomType;
        this.price = price;
        this.status = RoomStatus.AVAILABLE;
    }

    public int getRoomId()       { return this.roomId; }
    public RoomType getRoomType(){ return this.roomType; }
    public double getPrice()     { return this.price; }
    public RoomStatus getStatus(){ return this.status; }

    /** Used by RoomRepository */
    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    @Override
    public boolean checkAvailability(LocalDate start, LocalDate end) {
        for (DateRange r : reservations) {
            if (r.overlaps(start, end)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void reserve(LocalDate start, LocalDate end) {
        reservations.add(new DateRange(start, end));
        status = RoomStatus.RESERVED;
    }

    @Override
    public void release(LocalDate start, LocalDate end) {
        reservations.removeIf(r -> r.matches(start, end));
        if (reservations.isEmpty()) {
            status = RoomStatus.AVAILABLE;
        }
    }
    
    public void occupy() {
        status = RoomStatus.OCCUPIED;
    }

    /**
     * Returns all reservation date ranges as LocalDate pairs [start, end].
     * Used by RoomRepository to persist reservations to file.
     */
    public List<LocalDate[]> getReservationRanges() {
        List<LocalDate[]> ranges = new ArrayList<>();
        for (DateRange r : reservations) {
            ranges.add(new LocalDate[]{ r.getStart(), r.getEnd() });
        }
        return ranges;
    }
}