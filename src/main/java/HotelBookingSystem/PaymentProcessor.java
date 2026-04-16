package HotelBookingSystem;

import java.util.Random;

public class PaymentProcessor implements IPaymentProcessor {

    @Override
    public boolean process(double amount) {
        System.out.println("Processing payment of $" + amount + "... Payment successful.");
        return true;
    }

    @Override
    public boolean refund(double amount) {
        System.out.println("Refunding $" + amount + "... Refund successful.");
        return true;
    }

}
