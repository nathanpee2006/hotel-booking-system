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

    public void cancelBooking(int bookingId, String customerEmail) {
        Booking booking = bookingRepo.findById(bookingId);

        if (booking == null) {
            throw new IllegalArgumentException("Booking not found.");
        }

        if (!booking.getCustomer().getEmail().equalsIgnoreCase(customerEmail)) {
            throw new SecurityException("You are not authorised to cancel this booking.");
        }

        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Only pending bookings can be cancelled this way.");
        }

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

    public void requestCancellation(int bookingId, String customerEmail) {
        Booking booking = bookingRepo.findById(bookingId);

        if (booking == null) {
            throw new IllegalArgumentException("Booking not found.");
        }

        if (!booking.getCustomer().getEmail().equalsIgnoreCase(customerEmail)) {
            throw new SecurityException("You are not authorised to request cancellation for this booking.");
        }

        if (booking.getBookingStatus() != BookingStatus.COMPLETED) {
            throw new IllegalStateException("Only completed bookings can be submitted for cancellation request.");
        }

        booking.requestCancellation();
        bookingRepo.update(booking);
    }

    public void approveCancellation(int bookingId) {
        Booking booking = bookingRepo.findById(bookingId);

        if (booking == null) {
            throw new IllegalArgumentException("Booking not found.");
        }

        if (booking.getBookingStatus() != BookingStatus.CANCELLATION_REQUESTED) {
            throw new IllegalStateException("Booking is not awaiting cancellation approval.");
        }

        paymentProcessor.refund(booking.getAmount());

        booking.cancel();
        bookingRepo.update(booking);

        booking.getRoom().release(booking.getDateRange().getStart(), booking.getDateRange().getEnd());
        roomRepo.updateRoom(booking.getRoom());
    }
}
