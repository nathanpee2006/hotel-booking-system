package HotelBookingSystem;

import java.util.Random;

public class PaymentProcessor implements IPaymentProcessor{
	
	
	private Random random = new Random();

    @Override
    public boolean process(double amount) {
    	
        System.out.println("Processing payment of $" + amount + "...");
        boolean success = random.nextInt(100) < 90;

        if (success) {
            System.out.println("Payment successful.");
        } else {
            System.out.println("Payment failed.");
        }

        return success;
    }

    @Override
    public boolean refund(double amount) {
    	
        System.out.println("Refunding payment of $" + amount + "...");
        boolean success = random.nextInt(100) < 90;

        if (success) {
            System.out.println("Refund successful.");
        } else {
            System.out.println("Refund failed.");
        }

        return success;
    }

}
