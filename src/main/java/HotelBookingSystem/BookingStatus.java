package HotelBookingSystem;

public enum BookingStatus {

    PENDING("Pending"),
    CANCELLED("Cancelled"),
    COMPLETED("Completed"),
    CANCELLATION_REQUESTED("Cancellation Requested"),
    CHECKOUT_REQUESTED("Checkout Requested");

    private String status;
    private BookingStatus stat;

    private BookingStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
    
    public BookingStatus getBookingStatus() {
    	return this.stat;
    }
    
}
