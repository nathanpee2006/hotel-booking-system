package HotelBookingSystem;

public class HotelClerk extends User{
	 	
	 private final BookingManager manager;

	    public HotelClerk(String name, String email, BookingManager manager) {
	        super(name, email);
	        this.manager = manager;
	    }
	    
	    public HotelClerk(BookingManager manager) {
	    	super(null, null);
	    	this.manager = manager;
	    }

	    public Booking createBooking(Customer customer, Room room, DateRange dateRange) {
	        return manager.createBooking(customer, room, dateRange);
	    }

	    public void completeBooking(int bookingId) {
	        manager.completeBooking(bookingId);
	    }

	    public void approveCancellation(int bookingId) {
	        manager.approveCancellation(bookingId);
	    }

	    public void confirmCheckout(int bookingId) {
	        manager.confirmCheckout(bookingId);
	    }
    
}
