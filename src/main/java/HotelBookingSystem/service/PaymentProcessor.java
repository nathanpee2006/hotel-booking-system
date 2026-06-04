package HotelBookingSystem.service;

import HotelBookingSystem.model.CardDetails;
import HotelBookingSystem.db.DBManager;
import HotelBookingSystem.model.PaymentResult;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class PaymentProcessor implements IPaymentProcessor {

    private final DBManager db;

    public PaymentProcessor(DBManager db) {
        this.db = db;
    }

    @Override
    public PaymentResult process(double amount, int bookingId, CardDetails card) {
        // Validate card number
        PaymentResult cardValidation = validateCard(card);
        if (!cardValidation.isSuccess()) {
            return cardValidation;
        }

        // Insert payment record — full card number discarded, only last 4 stored
        try {
            insertPayment(bookingId, amount, card.getLast4(), card.getCardExpiry());
        } catch (SQLException ex) {
            return PaymentResult.failure("Payment could not be recorded: " + ex.getMessage());
        }

        System.out.println("Payment of $" + amount + " processed successfully.");
        return PaymentResult.success();
    }

    @Override
    public PaymentResult refund(double amount, int bookingId) {
        // Negative amount represents a refund
        try {
            insertPayment(bookingId, -amount, null, null);
        } catch (SQLException ex) {
            return PaymentResult.failure("Refund could not be recorded: " + ex.getMessage());
        }

        System.out.println("Refund of $" + amount + " recorded successfully.");
        return PaymentResult.success();
    }

    // -------------------------------------------------------------------------
    // Validation
    // -------------------------------------------------------------------------
    private PaymentResult validateCard(CardDetails card) {
        if (card == null) {
            return PaymentResult.failure("No card details provided.");
        }

        String number = card.getCardNumber();
        String expiry = card.getCardExpiry();

        if (number == null || number.isBlank()) {
            return PaymentResult.failure("Card number is required.");
        }

        if (expiry == null || expiry.isBlank()) {
            return PaymentResult.failure("Expiry date is required.");
        }

        // Strip spaces/dashes before Luhn check
        String sanitized = number.replaceAll("[\\s-]", "");
        if (!isValidLuhn(sanitized)) {
            return PaymentResult.failure("Invalid card number.");
        }

        if (!isValidExpiry(expiry)) {
            return PaymentResult.failure("Card has expired or expiry date is invalid. Use MM-YYYY format.");
        }

        return PaymentResult.success();
    }

    /**
     * Luhn algorithm — standard check digit validation used by all major card
     * networks.
     */
    private boolean isValidLuhn(String number) {
        if (number == null || !number.matches("\\d+")) {
            return false;
        }

        int sum = 0;
        boolean alternate = false;

        for (int i = number.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(number.charAt(i));

            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }

            sum += digit;
            alternate = !alternate;
        }

        return sum % 10 == 0;
    }

    /**
     * Validates expiry is in MM/YYYY format and not in the past.
     */
    private boolean isValidExpiry(String expiry) {
        try {
            YearMonth expiryMonth = YearMonth.parse(expiry, DateTimeFormatter.ofPattern("MM-yyyy"));
            YearMonth now = YearMonth.now();
            return !expiryMonth.isBefore(now);
        } catch (DateTimeParseException ex) {
            return false;
        }
    }

    // -------------------------------------------------------------------------
    // DB
    // -------------------------------------------------------------------------
    private void insertPayment(int bookingId, double amount, String cardLast4, String cardExpiry)
            throws SQLException {
        String sql = """
            INSERT INTO PAYMENTS (booking_id, amount, method, card_last4, card_expiry, paid_at)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement ps = requireConnection().prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ps.setDouble(2, amount);
            ps.setString(3, "CARD");
            ps.setString(4, cardLast4);   // null for refund rows
            ps.setString(5, cardExpiry);  // null for refund rows
            ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
        }
    }

    private Connection requireConnection() {
        Connection conn = db.getConnection();
        if (conn == null) {
            throw new IllegalStateException("No database connection");
        }
        return conn;
    }

}
