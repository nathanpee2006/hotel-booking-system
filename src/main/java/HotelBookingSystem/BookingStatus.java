package HotelBookingSystem;

public enum BookingStatus {

    PENDING("Pending"),
    CANCELLED("Cancelled"),
    COMPLETED("Completed"),
    CANCELLATION_REQUESTED("Cancellation Requested"),
    CHECKOUT_REQUESTED("Checkout Requested"),
    CHECKED_OUT("Checked Out");

    private String status;

    private BookingStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
