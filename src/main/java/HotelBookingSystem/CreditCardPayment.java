package HotelBookingSystem;

import java.time.LocalDate;

public class CreditCardPayment extends PaymentMethod{

   private String cardNumber; 

   private LocalDate expiry; 

 

   public CreditCardPayment(int paymentId, double amount, PaymentStatus paymentStatus, String cardNumber, LocalDate expiry) { 

       super(paymentId, amount, paymentStatus); 

       this.cardNumber = cardNumber; 

       this.expiry = expiry; 

   } 

    

   @Override 

   public void processPayment() { 

        
   }
    

   @Override 

   public void refundPayment() { 

        
   } 
	
}
