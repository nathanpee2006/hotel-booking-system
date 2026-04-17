package HotelBookingSystem;

public class HotelClerk extends User{
	 	
	private final BookingManager myManager;
	private final IRoomRepository roomRepo;
    private final IBookingRepository bookingRepo;
    private final IPaymentProcessor paymentProcessor;
	
    public HotelClerk(int userId, 
    				String name, 
    				String email, 
    				BookingManager manager,
    				IRoomRepository roomRepo,
    	            IBookingRepository bookingRepo, 
    	            IPaymentProcessor paymentProcessor) { 

        super(userId, name, email); 
        this.myManager = manager;
        this.roomRepo = roomRepo;
        this.bookingRepo = bookingRepo;        
        this.paymentProcessor = paymentProcessor;
    } 
    
    public Booking createBooking(Customer customer, Room room, DateRange dateRange) {
        if (myManager.checkAvailability(room, dateRange)) {
            throw new IllegalStateException("Room is not available");
        }

        int newId = bookingRepo.generateId();
        Booking booking = new Booking(newId, customer, room, dateRange, BookingStatus.PENDING);

        bookingRepo.save(booking);
        room.reserve(dateRange.getStart(), dateRange.getEnd());
        roomRepo.updateRoom(room);

        return booking;
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

    public void confirmCheckout(int bookingId) {

        Booking booking = bookingRepo.findById(bookingId);

        if (booking == null) {
            throw new IllegalArgumentException("Booking not found.");
        }

        if (booking.getBookingStatus() != BookingStatus.CHECKOUT_REQUESTED) {
            throw new IllegalStateException(
                "Checkout can only be confirmed for checkout requests."
            );
        }

        booking.setBookingStatus(BookingStatus.CHECKED_OUT);
        bookingRepo.update(booking);

        Room room = booking.getRoom();
        room.release(booking.getDateRange().getStart(), booking.getDateRange().getEnd());
        roomRepo.updateRoom(room);

        System.out.println("Checkout confirmed for booking ID: " + bookingId);
    }
    
}