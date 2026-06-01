package HotelBookingSystem;

public class PaymentResult {

    private final boolean success;
    private final String errorMessage;

    private PaymentResult(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public static PaymentResult success() {
        return new PaymentResult(true, null);
    }

    public static PaymentResult failure(String errorMessage) {
        return new PaymentResult(false, errorMessage);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
