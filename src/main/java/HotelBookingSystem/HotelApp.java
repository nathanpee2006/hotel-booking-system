package HotelBookingSystem;

import java.util.Scanner;
import java.time.LocalDate;
import java.util.List;

public class HotelApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        IRoomRepository roomRepo = new RoomRepository();
        IBookingRepository bookingRepo = new BookingRepository(roomRepo);
        IPaymentProcessor paymentProcessor = new PaymentProcessor();

        BookingManager manager = new BookingManager(roomRepo, bookingRepo, paymentProcessor);

        while (true) {
            System.out.println("\n=== Welcome to the Hotel Booking System ===");
            System.out.println("Login as:");
            System.out.println("1. Customer");
            System.out.println("2. Hotel Clerk");
            System.out.println("3. Close");

            int userType = sc.nextInt();

            switch (userType) {
                case 1:
                    runCustomerMenu(sc, manager, roomRepo, bookingRepo);
                    break;
                case 2:
                    runClerkMenu(sc, manager, roomRepo, bookingRepo);
                    break;
                case 3:
                    System.out.println("Closing...");
                    sc.close();
                    return;
                default:
                    System.out.println("Please enter a valid option.");
                    break;
            }
        }
    }

    // -------------------------------------------------------------------------
    // Customer Menu
    // -------------------------------------------------------------------------
    private static void runCustomerMenu(Scanner sc, BookingManager manager, IRoomRepository roomRepo, IBookingRepository bookingRepo) {

        while (true) {
            System.out.println("\n=== CUSTOMER MENU ===");
            System.out.println("1. Book a room");
            System.out.println("2. Cancel booking");
            System.out.println("3. Checkout");
            System.out.println("4. Back");

            int choice = sc.nextInt();

            switch (choice) {
                /*
                        ALLOWED:
                            Booking 1: Room 101 -> 04/07 - 04/10 : STATUS OCCUPIED
                            Booking 2: Room 101 -> 04/11 -> 04/12 : STATUS RESERVED
                        NOT ALLOWED:
                            Booking 1: Room 101 -> 04/07 - 04/10
                            Booking 2: Room 101 -> 04/09 -> 04/10
                 */
                case 1:
                    List<Room> availableRooms = roomRepo.getAllRooms();

                    if (availableRooms.isEmpty()) {
                        System.out.println("No rooms are currently available.");
                        break;
                    }

                    System.out.println("\n--- Available Rooms ---");
                    printRoomTable(availableRooms);

                    System.out.print("Enter room ID: ");
                    int roomId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Start date (YYYY-MM-DD): ");
                    LocalDate start = LocalDate.parse(sc.nextLine().trim());

                    System.out.print("End date (YYYY-MM-DD): ");
                    LocalDate end = LocalDate.parse(sc.nextLine().trim());

                    System.out.print("Enter your name: ");
                    String customerName = sc.nextLine().trim();

                    System.out.print("Enter your email: ");
                    String customerEmail = sc.nextLine().trim();

                    Room room = roomRepo.getRoomById(roomId);
                    if (room == null) {
                        System.out.println("Room ID " + roomId + " not found.");
                        break;
                    }

                    Customer customer = new Customer(1, customerName, customerEmail);
                    DateRange dr = new DateRange(start, end);

                    try {
                        Booking booking = manager.createBooking(customer, room, dr);
                        System.out.println("Booking created with ID: " + booking.getBookingId());
                    } catch (IllegalStateException e) {
                        System.out.println("Booking has been already made on this room.");
                    }
                    break;

                /*
                        Allowed:
                        Jan cancels his own booking.

                        Not Allowed:
                        Jan cancels Booking of Nathan.
                 */
                case 2:
                    System.out.print("Enter your email: ");
                    sc.nextLine();
                    String cancelEmail = sc.nextLine().trim();

                    List<Booking> customerBookings = bookingRepo.findByEmail(cancelEmail);

                    if (customerBookings.isEmpty()) {
                        System.out.println("No bookings found for " + cancelEmail + ".");
                        break;
                    }

                    System.out.println("\n--- Your Bookings ---");
                    printBookingTable(customerBookings);

                    System.out.print("Enter booking ID to cancel: ");
                    int cancelId = sc.nextInt();

                    try {
                        Booking found = bookingRepo.findById(cancelId);

                        if (found == null) {
                            System.out.println("Booking not found.");
                            break;
                        }

                        if (found.getBookingStatus() == BookingStatus.PENDING) {
                            manager.cancelBooking(cancelId, cancelEmail);
                            System.out.println("Booking cancelled successfully.");
                        } else if (found.getBookingStatus() == BookingStatus.COMPLETED) {
                            manager.requestCancellation(cancelId, cancelEmail);
                            System.out.println("Cancellation request submitted. Awaiting clerk approval.");
                        } else {
                            System.out.println("Booking cannot be cancelled in its current state: "
                                    + found.getBookingStatus().getStatus());
                        }

                    } catch (IllegalArgumentException | IllegalStateException | SecurityException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                /*
                        Customer checks out. (Room status: OCCUPIED)
                        Hotel clerk needs to confirm.
                        Room status: AVAILABLE
                 */
                case 3:
                    sc.nextLine();

                    System.out.print("Enter your email: ");
                    String checkoutEmail = sc.nextLine().trim();

                    List<Booking> myBookings = bookingRepo.findByEmail(checkoutEmail);

                    if (myBookings.isEmpty()) {
                        System.out.println("No bookings found for email: " + checkoutEmail);
                        break;
                    }

                    System.out.println("\n--- Your Bookings ---");
                    printBookingTable(myBookings);

                    System.out.print("Enter booking ID: ");
                    int checkoutId = sc.nextInt();

                    try {
                        manager.requestCheckout(checkoutId, checkoutEmail);
                        System.out.println("Checkout request submitted. Awaiting clerk confirmation.");
                    } catch (IllegalArgumentException | IllegalStateException | SecurityException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 4:
                    return;
            }
        }
    }

    // -------------------------------------------------------------------------
    // Clerk Menu
    // -------------------------------------------------------------------------
    private static void runClerkMenu(Scanner sc, BookingManager manager, IRoomRepository roomRepo, IBookingRepository bookingRepo, IPaymentProcessor paymentProcessor) {
    	
    	HotelClerk hotelClerk = new HotelClerk(0, "HotelClerkUser", "*****@gmail.com", manager, roomRepo, bookingRepo, paymentProcessor);

        while (true) {
            System.out.println("\n=== HOTEL CLERK MENU ===");
            System.out.println("1. Book a room");
            System.out.println("2. Complete booking");
            System.out.println("3. Approve cancellation request");
            System.out.println("4. Confirm checkout");
            System.out.println("5. Back");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    List<Room> availableRooms = roomRepo.getAllRooms();

                    if (availableRooms.isEmpty()) {
                        System.out.println("No rooms are currently available.");
                        break;
                    }

                    System.out.println("\n--- Available Rooms ---");
                    printRoomTable(availableRooms);

                    System.out.print("Enter room ID: ");
                    int roomId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Start date (YYYY-MM-DD): ");
                    LocalDate start = LocalDate.parse(sc.nextLine().trim());

                    System.out.print("End date (YYYY-MM-DD): ");
                    LocalDate end = LocalDate.parse(sc.nextLine().trim());

                    System.out.print("Enter customer name: ");
                    String customerName = sc.nextLine().trim();

                    System.out.print("Enter customer email: ");
                    String customerEmail = sc.nextLine().trim();

                    Room room = roomRepo.getRoomById(roomId);
                    if (room == null) {
                        System.out.println("Room ID " + roomId + " not found.");
                        break;
                    }

                    Customer customer = new Customer(1, customerName, customerEmail);
                    DateRange dr = new DateRange(start, end);

                    try {
                        Booking booking = hotelClerk.createBooking(customer, room, dr);
                        System.out.println("Booking created with ID: " + booking.getBookingId());
                    } catch (IllegalStateException e) {
                        System.out.println("Booking has been already made on this room.");
                    }
                    break;

                case 2:
                    List<Booking> pendingBookings = bookingRepo.findByStatus(BookingStatus.PENDING);

                    if (pendingBookings.isEmpty()) {
                        System.out.println("No pending bookings.");
                        break;
                    }

                    System.out.println("\n--- Pending Bookings ---");
                    printBookingTable(pendingBookings);

                    System.out.print("Enter booking ID: ");
                    int completeId = sc.nextInt();

                    try {
                        hotelClerk.completeBooking(completeId);
                        System.out.println("Booking completed.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 3:
                    List<Booking> cancellationRequests = bookingRepo.findByStatus(BookingStatus.CANCELLATION_REQUESTED);

                    if (cancellationRequests.isEmpty()) {
                        System.out.println("No cancellation requests.");
                        break;
                    }

                    System.out.println("\n--- Cancellation Requests ---");
                    printBookingTable(cancellationRequests);

                    System.out.print("Enter booking ID to approve (0 to go back): ");
                    int approveId = sc.nextInt();

                    if (approveId == 0) {
                        break;
                    }

                    try {
                        hotelClerk.approveCancellation(approveId);
                        System.out.println("Cancellation approved. Refund issued.");
                    } catch (IllegalArgumentException | IllegalStateException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 4:
                    List<Booking> checkOutRequests = bookingRepo.findByStatus(BookingStatus.CHECKOUT_REQUESTED);

                    if (checkOutRequests.isEmpty()) {
                        System.out.println("No checkout requests.");
                        break;
                    }

                    System.out.println("\n--- Checkout Requests ---");
                    printBookingTable(checkOutRequests);

                    System.out.print("Enter booking ID: ");
                    int checkoutId = sc.nextInt();

                    try {
                        hotelClerk.confirmCheckout(checkoutId);
                        System.out.println("Customer has been checked out.");
                    } catch (IllegalArgumentException | IllegalStateException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 5:
                    return;
            }
        }
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    private static void printBookingTable(List<Booking> bookings) {
        System.out.printf("%-6s %-16s %-24s %-6s %-14s %-22s %-10s%n",
                "ID", "Customer", "Email", "Room", "Type", "Dates", "Status");
        System.out.println("--------------------------------------------------------------------------------------------");
        for (Booking b : bookings) {
            System.out.printf("%-6d %-16s %-24s %-6d %-14s %-22s %-10s%n",
                    b.getBookingId(),
                    b.getCustomer().getName(),
                    b.getCustomer().getEmail(),
                    b.getRoom().getRoomId(),
                    b.getRoom().getRoomType().getDisplayName(),
                    b.getDateRange().getStart() + " to " + b.getDateRange().getEnd(),
                    b.getBookingStatus().getStatus()
            );
        }
        System.out.println();
    }

    private static void printRoomTable(List<Room> rooms) {
        System.out.printf("%-6s %-14s %-10s %-10s%n", "ID", "Type", "Price", "Capacity");
        System.out.println("----------------------------------------------");
        for (Room r : rooms) {
            System.out.printf("%-6d %-14s $%-9.2f %-10d%n",
                    r.getRoomId(),
                    r.getRoomType().getDisplayName(),
                    r.getPrice(),
                    r.getRoomType().getDefaultCapacity()
            );
        }
        System.out.println();
    }
}
