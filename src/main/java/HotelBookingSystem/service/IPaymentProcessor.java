package HotelBookingSystem.service;

import HotelBookingSystem.model.CardDetails;
import HotelBookingSystem.model.PaymentResult;

public interface IPaymentProcessor {

    /**
     * Validates card details and processes a payment for the given booking.
     * Returns a PaymentResult indicating success or failure with an error
     * message.
     */
    PaymentResult process(double amount, int bookingId, CardDetails card);

    /**
     * Records a refund for the given booking as a negative amount in PAYMENTS.
     */
    PaymentResult refund(double amount, int bookingId);

}
