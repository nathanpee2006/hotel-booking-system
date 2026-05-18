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

    /**
     * Creates a booking for an authenticated customer (GUI path). booking_id is
     * assigned by the DB and set back on the returned Booking.
     */
    public Booking createBooking(Customer customer, Room room, DateRange dateRange) {
        if (!checkAvailability(room, dateRange)) {
            throw new IllegalStateException("Room is not available");
        }

        int userId = customer.getUserId();
        Booking booking = new Booking(-1, userId, customer, room, dateRange, BookingStatus.PENDING);

        // save() assigns the real DB-generated ID and sets it on the booking
        bookingRepo.save(booking);

        room.reserve(dateRange.getStart(), dateRange.getEnd());
        roomRepo.updateRoom(room);

        return booking;
    }

    /**
     * Cancels a PENDING booking. Verified by userId (GUI path).
     */
    public void cancelBooking(int bookingId, int userId) {
        Booking booking = bookingRepo.findById(bookingId);

        if (booking == null) {
            throw new IllegalArgumentException("Booking not found.");
        }

        if (booking.getUserId() != userId) {
            throw new SecurityException("You are not authorised to cancel this booking.");
        }

        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Only pending bookings can be cancelled this way.");
        }

        booking.cancel();
        bookingRepo.update(booking);

        deleteReservation(bookingId);

        booking.getRoom().release(booking.getDateRange().getStart(), booking.getDateRange().getEnd());
        roomRepo.updateRoom(booking.getRoom());
    }

    /**
     * Cancels a PENDING booking. Verified by email (CUI backward compat path).
     */
    public void cancelBooking(int bookingId, String customerEmail) {
        Booking booking = bookingRepo.findById(bookingId);

        if (booking == null) {
            throw new IllegalArgumentException("Booking not found.");
        }

        if (booking.getCustomer() == null
                || !booking.getCustomer().getEmail().equalsIgnoreCase(customerEmail)) {
            throw new SecurityException("You are not authorised to cancel this booking.");
        }

        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Only pending bookings can be cancelled this way.");
        }

        booking.cancel();
        bookingRepo.update(booking);

        deleteReservation(bookingId);

        booking.getRoom().release(booking.getDateRange().getStart(), booking.getDateRange().getEnd());
        roomRepo.updateRoom(booking.getRoom());
    }

    public void completeBooking(int bookingId) {
        Booking booking = bookingRepo.findById(bookingId);

        if (booking == null) {
            throw new IllegalArgumentException("Booking not found");
        }

        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Only pending bookings can be completed.");
        }

        paymentProcessor.process(booking.getAmount());

        booking.completeBooking();
        bookingRepo.update(booking);

        booking.getRoom().occupy();
        roomRepo.updateRoom(booking.getRoom());
    }

    /**
     * Requests cancellation of a COMPLETED booking. Verified by userId (GUI
     * path).
     */
    public void requestCancellation(int bookingId, int userId) {
        Booking booking = bookingRepo.findById(bookingId);

        if (booking == null) {
            throw new IllegalArgumentException("Booking not found.");
        }

        if (booking.getUserId() != userId) {
            throw new SecurityException("You are not authorised to request cancellation for this booking.");
        }

        if (booking.getBookingStatus() != BookingStatus.COMPLETED) {
            throw new IllegalStateException("Only completed bookings can be submitted for cancellation request.");
        }

        booking.requestCancellation();
        bookingRepo.update(booking);
    }

    /**
     * Requests cancellation of a COMPLETED booking. Verified by email (CUI
     * backward compat path).
     */
    public void requestCancellation(int bookingId, String customerEmail) {
        Booking booking = bookingRepo.findById(bookingId);

        if (booking == null) {
            throw new IllegalArgumentException("Booking not found.");
        }

        if (booking.getCustomer() == null
                || !booking.getCustomer().getEmail().equalsIgnoreCase(customerEmail)) {
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

        deleteReservation(bookingId);

        booking.getRoom().release(booking.getDateRange().getStart(), booking.getDateRange().getEnd());
        roomRepo.updateRoom(booking.getRoom());
    }

    /**
     * Requests checkout of a COMPLETED booking. Verified by userId (GUI path).
     */
    public void requestCheckout(int bookingId, int userId) {
        Booking booking = bookingRepo.findById(bookingId);

        if (booking == null) {
            throw new IllegalArgumentException("Booking not found.");
        }

        if (booking.getUserId() != userId) {
            throw new SecurityException("You can only check out your own booking.");
        }

        if (booking.getBookingStatus() == BookingStatus.PENDING) {
            throw new IllegalStateException("This booking is currently pending.");
        }

        if (booking.getBookingStatus() != BookingStatus.COMPLETED) {
            throw new IllegalStateException("Checkout is only allowed for completed bookings.");
        }

        booking.setBookingStatus(BookingStatus.CHECKOUT_REQUESTED);
        bookingRepo.update(booking);
    }

    /**
     * Requests checkout of a COMPLETED booking. Verified by email (CUI backward
     * compat path).
     */
    public void requestCheckout(int bookingId, String customerEmail) {
        Booking booking = bookingRepo.findById(bookingId);

        if (booking == null) {
            throw new IllegalArgumentException("Booking not found.");
        }

        if (booking.getCustomer() == null
                || !booking.getCustomer().getEmail().equalsIgnoreCase(customerEmail)) {
            throw new SecurityException("You can only check out your own booking.");
        }

        if (booking.getBookingStatus() == BookingStatus.PENDING) {
            throw new IllegalStateException("This booking is currently pending.");
        }

        if (booking.getBookingStatus() != BookingStatus.COMPLETED) {
            throw new IllegalStateException("Checkout is only allowed for completed bookings.");
        }

        booking.setBookingStatus(BookingStatus.CHECKOUT_REQUESTED);
        bookingRepo.update(booking);
    }

    public void confirmCheckout(int bookingId) {
        Booking booking = bookingRepo.findById(bookingId);

        if (booking == null) {
            throw new IllegalArgumentException("Booking not found.");
        }

        if (booking.getBookingStatus() != BookingStatus.CHECKOUT_REQUESTED) {
            throw new IllegalStateException("Checkout can only be confirmed for checkout requests.");
        }

        booking.setBookingStatus(BookingStatus.CHECKED_OUT);
        bookingRepo.update(booking);

        deleteReservation(bookingId);

        Room room = booking.getRoom();
        room.release(booking.getDateRange().getStart(), booking.getDateRange().getEnd());
        roomRepo.updateRoom(room);

        System.out.println("Checkout confirmed for booking ID: " + bookingId);
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------
    /**
     * Deletes the ROOM_RESERVATIONS row for a booking if the repo supports it.
     * Only JdbcBookingRepository has deleteReservation(); CSV path is a no-op.
     */
    private void deleteReservation(int bookingId) {
        if (bookingRepo instanceof JdbcBookingRepository jdbcRepo) {
            jdbcRepo.deleteReservation(bookingId);
        }
    }

}
