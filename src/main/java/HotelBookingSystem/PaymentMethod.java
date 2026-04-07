package HotelBookingSystem;

public abstract class PaymentMethod {
	
    private int paymentId; 

    private double amount; 

    private PaymentStatus paymentStatus; 

  

    public PaymentMethod(int paymentId, double amount, PaymentStatus paymentStatus) { 

        this.paymentId = paymentId; 

        this.amount = amount; 

        this.paymentStatus = paymentStatus; 

    } 

     

    public abstract void processPayment(); 

    public abstract void refundPayment(); 


}
