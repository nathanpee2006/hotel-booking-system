package HotelBookingSystem;

public enum BookingStatus {
	
	  PENDING("Pending"), CONFIRMED("Confirmed"), CANCELLED("Cancelled"), COMPLETED("Completed"); 
	  
	private String status;
	
	private BookingStatus(String stats) {
		this.status = stats;
	}
	
	  public String getStatus() {
		  return this.status;
	  }

}
