package HotelBookingSystem;
public interface IPaymentProcessor {

	boolean process(double amount);

	boolean refund(double amount);

}