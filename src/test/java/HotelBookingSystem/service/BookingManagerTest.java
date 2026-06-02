package HotelBookingSystem.service;

import HotelBookingSystem.model.*;
import HotelBookingSystem.repository.IBookingRepository;
import HotelBookingSystem.repository.IRoomRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class BookingManagerTest {

    // -------------------------------------------------------------------------
    // Mocks
    // -------------------------------------------------------------------------
    @Mock
    private IRoomRepository roomRepo;
    @Mock
    private IBookingRepository bookingRepo;
    @Mock
    private IPaymentProcessor paymentProcessor;

    private BookingManager manager;

    // -------------------------------------------------------------------------
    // Shared test fixtures
    // -------------------------------------------------------------------------
    private Room room;
    private DateRange dateRange;
    private CardDetails validCard;
    private final int USER_ID = 1;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        manager = new BookingManager(roomRepo, bookingRepo, paymentProcessor);

        room = new Room(101, RoomType.SINGLE, 120.0);
        dateRange = new DateRange(LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 5));
        validCard = new CardDetails("4532015112830366", "12/2027");
    }

    // -------------------------------------------------------------------------
    // createBooking
    // -------------------------------------------------------------------------
    @Test
    public void createBooking_roomAvailable_returnsBookingWithPendingStatus() {
        when(bookingRepo.save(any(Booking.class))).thenReturn(1);
        when(paymentProcessor.process(anyDouble(), anyInt(), any(CardDetails.class)))
                .thenReturn(PaymentResult.success());

        Booking booking = manager.createBooking(USER_ID, room, dateRange, validCard);

        assertNotNull(booking);
        assertEquals(BookingStatus.PENDING, booking.getBookingStatus());
        assertEquals(USER_ID, booking.getUserId());
    }

    @Test(expected = IllegalStateException.class)
    public void createBooking_roomNotAvailable_throwsIllegalStateException() {
        room.reserve(LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 5));

        manager.createBooking(USER_ID, room, dateRange, validCard);
    }

    @Test
    public void createBooking_paymentFails_deletesBookingAndThrows() {
        when(bookingRepo.save(any(Booking.class))).thenReturn(1);
        when(paymentProcessor.process(anyDouble(), anyInt(), any(CardDetails.class)))
                .thenReturn(PaymentResult.failure("Invalid card number."));

        try {
            manager.createBooking(USER_ID, room, dateRange, validCard);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException ex) {
            assertEquals("Invalid card number.", ex.getMessage());
            verify(bookingRepo).delete(1);
        }
    }

    @Test
    public void createBooking_paymentSucceeds_roomIsReserved() {
        when(bookingRepo.save(any(Booking.class))).thenReturn(1);
        when(paymentProcessor.process(anyDouble(), anyInt(), any(CardDetails.class)))
                .thenReturn(PaymentResult.success());

        manager.createBooking(USER_ID, room, dateRange, validCard);

        assertFalse(room.checkAvailability(dateRange.getStart(), dateRange.getEnd()));
        verify(roomRepo).updateRoom(room);
    }

    // -------------------------------------------------------------------------
    // cancelBooking
    // -------------------------------------------------------------------------
    @Test
    public void cancelBooking_validUserAndPendingStatus_bookingIsCancelled() {
        Booking booking = new Booking(1, USER_ID, room, dateRange, BookingStatus.PENDING);
        when(bookingRepo.findById(1)).thenReturn(booking);

        manager.cancelBooking(1, USER_ID);

        assertEquals(BookingStatus.CANCELLED, booking.getBookingStatus());
        verify(bookingRepo).update(booking);
        verify(roomRepo).updateRoom(room);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cancelBooking_bookingNotFound_throwsIllegalArgumentException() {
        when(bookingRepo.findById(99)).thenReturn(null);

        manager.cancelBooking(99, USER_ID);
    }

    @Test(expected = SecurityException.class)
    public void cancelBooking_wrongUserId_throwsSecurityException() {
        Booking booking = new Booking(1, USER_ID, room, dateRange, BookingStatus.PENDING);
        when(bookingRepo.findById(1)).thenReturn(booking);

        manager.cancelBooking(1, 99);
    }

    @Test(expected = IllegalStateException.class)
    public void cancelBooking_bookingNotPending_throwsIllegalStateException() {
        Booking booking = new Booking(1, USER_ID, room, dateRange, BookingStatus.COMPLETED);
        when(bookingRepo.findById(1)).thenReturn(booking);

        manager.cancelBooking(1, USER_ID);
    }

    // -------------------------------------------------------------------------
    // completeBooking
    // -------------------------------------------------------------------------
    @Test
    public void completeBooking_pendingBooking_statusIsCompletedAndRoomOccupied() {
        Booking booking = new Booking(1, USER_ID, room, dateRange, BookingStatus.PENDING);
        when(bookingRepo.findById(1)).thenReturn(booking);
        when(paymentProcessor.process(anyDouble(), anyInt(), any()))
                .thenReturn(PaymentResult.success());

        manager.completeBooking(1);

        assertEquals(BookingStatus.COMPLETED, booking.getBookingStatus());
        assertEquals(RoomStatus.OCCUPIED, room.getStatus());
        verify(bookingRepo).update(booking);
        verify(roomRepo).updateRoom(room);
    }

    @Test(expected = IllegalArgumentException.class)
    public void completeBooking_bookingNotFound_throwsIllegalArgumentException() {
        when(bookingRepo.findById(99)).thenReturn(null);

        manager.completeBooking(99);
    }

    @Test(expected = IllegalStateException.class)
    public void completeBooking_bookingNotPending_throwsIllegalStateException() {
        Booking booking = new Booking(1, USER_ID, room, dateRange, BookingStatus.COMPLETED);
        when(bookingRepo.findById(1)).thenReturn(booking);

        manager.completeBooking(1);
    }

    // -------------------------------------------------------------------------
    // requestCancellation
    // -------------------------------------------------------------------------
    @Test
    public void requestCancellation_validUserAndCompletedStatus_statusIsCancellationRequested() {
        Booking booking = new Booking(1, USER_ID, room, dateRange, BookingStatus.COMPLETED);
        when(bookingRepo.findById(1)).thenReturn(booking);

        manager.requestCancellation(1, USER_ID);

        assertEquals(BookingStatus.CANCELLATION_REQUESTED, booking.getBookingStatus());
        verify(bookingRepo).update(booking);
    }

    @Test(expected = IllegalArgumentException.class)
    public void requestCancellation_bookingNotFound_throwsIllegalArgumentException() {
        when(bookingRepo.findById(99)).thenReturn(null);

        manager.requestCancellation(99, USER_ID);
    }

    @Test(expected = SecurityException.class)
    public void requestCancellation_wrongUserId_throwsSecurityException() {
        Booking booking = new Booking(1, USER_ID, room, dateRange, BookingStatus.COMPLETED);
        when(bookingRepo.findById(1)).thenReturn(booking);

        manager.requestCancellation(1, 99);
    }

    @Test(expected = IllegalStateException.class)
    public void requestCancellation_bookingNotCompleted_throwsIllegalStateException() {
        Booking booking = new Booking(1, USER_ID, room, dateRange, BookingStatus.PENDING);
        when(bookingRepo.findById(1)).thenReturn(booking);

        manager.requestCancellation(1, USER_ID);
    }

    // -------------------------------------------------------------------------
    // approveCancellation
    // -------------------------------------------------------------------------
    @Test
    public void approveCancellation_cancellationRequested_bookingCancelledAndRefundIssued() {
        Booking booking = new Booking(1, USER_ID, room, dateRange, BookingStatus.CANCELLATION_REQUESTED);
        when(bookingRepo.findById(1)).thenReturn(booking);
        when(paymentProcessor.refund(anyDouble(), anyInt())).thenReturn(PaymentResult.success());

        manager.approveCancellation(1);

        assertEquals(BookingStatus.CANCELLED, booking.getBookingStatus());
        verify(paymentProcessor).refund(booking.getAmount(), 1);
        verify(bookingRepo).update(booking);
        verify(roomRepo).updateRoom(room);
    }

    @Test(expected = IllegalArgumentException.class)
    public void approveCancellation_bookingNotFound_throwsIllegalArgumentException() {
        when(bookingRepo.findById(99)).thenReturn(null);

        manager.approveCancellation(99);
    }

    @Test(expected = IllegalStateException.class)
    public void approveCancellation_bookingNotCancellationRequested_throwsIllegalStateException() {
        Booking booking = new Booking(1, USER_ID, room, dateRange, BookingStatus.COMPLETED);
        when(bookingRepo.findById(1)).thenReturn(booking);

        manager.approveCancellation(1);
    }

    // -------------------------------------------------------------------------
    // requestCheckout
    // -------------------------------------------------------------------------
    @Test
    public void requestCheckout_validUserAndCompletedStatus_statusIsCheckoutRequested() {
        Booking booking = new Booking(1, USER_ID, room, dateRange, BookingStatus.COMPLETED);
        when(bookingRepo.findById(1)).thenReturn(booking);

        manager.requestCheckout(1, USER_ID);

        assertEquals(BookingStatus.CHECKOUT_REQUESTED, booking.getBookingStatus());
        verify(bookingRepo).update(booking);
    }

    @Test(expected = IllegalArgumentException.class)
    public void requestCheckout_bookingNotFound_throwsIllegalArgumentException() {
        when(bookingRepo.findById(99)).thenReturn(null);

        manager.requestCheckout(99, USER_ID);
    }

    @Test(expected = SecurityException.class)
    public void requestCheckout_wrongUserId_throwsSecurityException() {
        Booking booking = new Booking(1, USER_ID, room, dateRange, BookingStatus.COMPLETED);
        when(bookingRepo.findById(1)).thenReturn(booking);

        manager.requestCheckout(1, 99);
    }

    @Test(expected = IllegalStateException.class)
    public void requestCheckout_bookingPending_throwsIllegalStateException() {
        Booking booking = new Booking(1, USER_ID, room, dateRange, BookingStatus.PENDING);
        when(bookingRepo.findById(1)).thenReturn(booking);

        manager.requestCheckout(1, USER_ID);
    }

    @Test(expected = IllegalStateException.class)
    public void requestCheckout_bookingNotCompleted_throwsIllegalStateException() {
        Booking booking = new Booking(1, USER_ID, room, dateRange, BookingStatus.CANCELLATION_REQUESTED);
        when(bookingRepo.findById(1)).thenReturn(booking);

        manager.requestCheckout(1, USER_ID);
    }

    // -------------------------------------------------------------------------
    // confirmCheckout
    // -------------------------------------------------------------------------
    @Test
    public void confirmCheckout_checkoutRequested_statusIsCheckedOutAndRoomReleased() {
        Booking booking = new Booking(1, USER_ID, room, dateRange, BookingStatus.CHECKOUT_REQUESTED);
        when(bookingRepo.findById(1)).thenReturn(booking);

        manager.confirmCheckout(1);

        assertEquals(BookingStatus.CHECKED_OUT, booking.getBookingStatus());
        verify(bookingRepo).update(booking);
        verify(roomRepo).updateRoom(room);
    }

    @Test(expected = IllegalArgumentException.class)
    public void confirmCheckout_bookingNotFound_throwsIllegalArgumentException() {
        when(bookingRepo.findById(99)).thenReturn(null);

        manager.confirmCheckout(99);
    }

    @Test(expected = IllegalStateException.class)
    public void confirmCheckout_bookingNotCheckoutRequested_throwsIllegalStateException() {
        Booking booking = new Booking(1, USER_ID, room, dateRange, BookingStatus.COMPLETED);
        when(bookingRepo.findById(1)).thenReturn(booking);

        manager.confirmCheckout(1);
    }

}
