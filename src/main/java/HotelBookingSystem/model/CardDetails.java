package HotelBookingSystem.model;

public class CardDetails {

    private final String cardNumber;
    private final String cardExpiry; // MM/YYYY

    public CardDetails(String cardNumber, String cardExpiry) {
        this.cardNumber = cardNumber;
        this.cardExpiry = cardExpiry;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardExpiry() {
        return cardExpiry;
    }

    /**
     * Returns the last 4 digits of the card number for storage. Never store or
     * log the full number beyond validation.
     */
    public String getLast4() {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "0000";
        }
        return cardNumber.substring(cardNumber.length() - 4);
    }

}
