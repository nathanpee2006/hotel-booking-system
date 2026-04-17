package HotelBookingSystem;

public class Customer extends User{
	
	private final BookingManager manager;
	
	public Customer(String name, String email, BookingManager manager) {
		super(name, email);
		this.manager = manager;
	}

    public Booking createBooking(Customer customer, Room room, DateRange dateRange) {
        return manager.createBooking(customer, room, dateRange);
    }
    
    public void cancelBooking(int bookingId, String customerEmail) {
    	manager.cancelBooking(bookingId, customerEmail);
    }
    
    public void completeBooking(int bookingId) {
    	manager.completeBooking(bookingId);
    }
    
    public void requestCancellation(int bookingId, String customerEmail) {
    	manager.requestCancellation(bookingId, customerEmail);
    }
    
    public void requestCheckout(int bookingId, String customerEmail) {
    	manager.requestCheckout(bookingId, customerEmail);
    }

}
