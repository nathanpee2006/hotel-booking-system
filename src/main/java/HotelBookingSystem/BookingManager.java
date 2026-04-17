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


    public void requestCheckout(int bookingId, String customerEmail) {

        Booking booking = bookingRepo.findById(bookingId);

        if (booking == null) {
            throw new IllegalArgumentException("Booking not found.");
        }

        if (!booking.getCustomer().getEmail().equalsIgnoreCase(customerEmail)) {
            throw new SecurityException("You can only check out your own booking.");
        }

        if (booking.getBookingStatus() == BookingStatus.PENDING) {
            throw new IllegalStateException("This booking is currently pending.");
        }

        if (booking.getBookingStatus() != BookingStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Checkout is only allowed for completed bookings (COMPLETED)."
            );
        }

        booking.setBookingStatus(BookingStatus.CHECKOUT_REQUESTED);
        bookingRepo.update(booking);

        System.out.println("Checkout request submitted. A hotel clerk will confirm your checkout.");
    }


}
