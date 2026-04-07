package HotelBookingSystem;

public class BookingManager {

    private final IRoomRepository roomRepo;
    private final IBookingRepository bookingRepo;
    private final IPaymentProcessor paymentProcessor;

    public BookingManager(IRoomRepository roomRepo,
            IBookingRepository bookingRepo,
            IPaymentProcessor paymentProcessor) {
        this.roomRepo = roomRepo;
        this.bookingRepo = bookingRepo;
        this.paymentProcessor = paymentProcessor;
    }

    public boolean checkAvailability(Room room, DateRange dateRange) {
        return room.checkAvailability(dateRange.getStart(), dateRange.getEnd());
    }

    public Booking createBooking(Customer customer, Room room, DateRange dateRange) {
        if (!checkAvailability(room, dateRange)) {
            throw new IllegalStateException("Room is not available");
        }

        int newId = bookingRepo.generateId();
        Booking booking = new Booking(newId, customer, room, dateRange, BookingStatus.PENDING);

        bookingRepo.save(booking);
        room.reserve(dateRange.getStart(), dateRange.getEnd());
        roomRepo.updateRoom(room);

        return booking;
    }

    public void cancelBooking(int bookingId) {
        Booking booking = bookingRepo.findById(bookingId);

        if (booking == null) {
            throw new IllegalArgumentException("Booking not found");
        }

        paymentProcessor.refund(booking.getAmount());

        booking.cancel();
        bookingRepo.update(booking);

        booking.getRoom().release(booking.getDateRange().getStart(), booking.getDateRange().getEnd());
        roomRepo.updateRoom(booking.getRoom());
    }

    public void completeBooking(int bookingId) {
        Booking booking = bookingRepo.findById(bookingId);

        if (booking == null) {
            throw new IllegalArgumentException("Booking not found");
        }

        paymentProcessor.process(booking.getAmount());

        booking.completeBooking();
        bookingRepo.update(booking);

        booking.getRoom().occupy();
        roomRepo.updateRoom(booking.getRoom());
    }
}
