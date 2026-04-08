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

        System.out.println("=== Welcome to the Hotel Booking System ===");

        // LOGIN STEP
        System.out.println("Login as:");
        System.out.println("1. Customer");
        System.out.println("2. Hotel Clerk");

        int userType = sc.nextInt();

        if (userType == 1) {
            runCustomerMenu(sc, manager, roomRepo, bookingRepo);
        } else if (userType == 2) {
//            runClerkMenu(sc, manager);
        } else {
            System.out.println("Invalid choice. Exiting...");
        }

        // What is the purpose of this? Can this be moved to runClerkMenu
        
    }

    private static void runCustomerMenu(Scanner sc, BookingManager manager, IRoomRepository roomRepo, IBookingRepository bookingRepo) {

        while (true) {
            System.out.println("\n=== CUSTOMER MENU ===");
            System.out.println("1. Book a room");
            System.out.println("2. Cancel booking");
            System.out.println("3. Checkout");
            System.out.println("4. Exit");

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
                    System.out.printf("%-6s %-12s %-10s %-10s%n", "ID", "Type", "Price", "Capacity");
                    System.out.println("----------------------------------------------");
                    for (Room r : availableRooms) {
                        System.out.printf("%-6d %-12s $%-9.2f %-10d%n",
                                r.getRoomId(),
                                r.getRoomType().getDisplayName(),
                                r.getPrice(),
                                r.getRoomType().getDefaultCapacity()
                        );
                    }
                    System.out.println();

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
                    TODO: Currently, other customers can cancel others bookings.
                    Add some validation (not sure yet how).
                        
                    When is the cancellation period? If the booking is in a PENDING state? Can a booking that is COMPLETED be cancelled?
                    
                        Allowed:
                        Jan cancels his own booking.
                    
                        Not Allowed:
                        Jan cancels Booking of Nathan.
                 */
                case 2:
                    System.out.print("Enter your email: ");
                    sc.nextLine();
                    String cancelEmail = sc.nextLine().trim();

                    System.out.print("Enter booking ID: ");
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
                    TODO: Customer checkout feature?
                        Customer checks out. (Room status: OCCUPIED)
                        Hotel clerk needs to confirm?
                        Room status: AVAILABLE
                 */
                case 3:
                	sc.nextLine(); 

                    System.out.print("Enter your email: ");
                    String checkoutEmail = sc.nextLine().trim();

                    System.out.print("Enter booking ID: ");
                    int checkoutId = sc.nextInt();

                    try {
                        manager.requestCheckout(checkoutId, checkoutEmail);
                    } catch (IllegalArgumentException | IllegalStateException | SecurityException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    return;

                case 4:
                    return;

            }
        }
    }

//    private static void runClerkMenu(Scanner sc, BookingManager manager) {
//
//        HotelClerk clerk = new HotelClerk(100, "Clerk", "clerk@hotel.com", manager);
//
//        while (true) {
//            System.out.println("\n=== HOTEL CLERK MENU ===");
//            System.out.println("1. Confirm checkout");
//            System.out.println("2. View schedule");
//            System.out.println("3. Exit");
//
//            int choice = sc.nextInt();
//
//            switch (choice) {
//                case 1:
//                    System.out.print("Enter booking ID: ");
//                    int bookingId = sc.nextInt();
//                    clerk.confirmCheckout(bookingId);
//                    break;
//
//                case 2:
//                    clerk.viewSchedule();
//                    break;
//
//                case 3:
//                    return;
//            }
//        }
//    }
    /*
     * while (true) {
            System.out.println("=== HOTEL BOOKING SYSTEM ===");
            System.out.println("1. Book a room");
            System.out.println("2. Cancel booking");
            System.out.println("3. Complete booking");
            System.out.println("4. Exit");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    List<Room> availableRooms = roomRepo.getAllRooms().stream()
                            .filter(r -> r.getStatus() == RoomStatus.AVAILABLE)
                            .toList();

                    if (availableRooms.isEmpty()) {
                        System.out.println("No rooms are currently available.");
                        break;
                    }

                    System.out.println("\n--- Available Rooms ---");
                    System.out.printf("%-6s %-12s %-10s %-10s%n", "ID", "Type", "Price", "Capacity");
                    System.out.println("----------------------------------------------");
                    for (Room r : availableRooms) {
                        System.out.printf("%-6d %-12s $%-9.2f %-10d%n",
                                r.getRoomId(),
                                r.getRoomType().getDisplayName(),
                                r.getPrice(),
                                r.getRoomType().getDefaultCapacity()
                        );
                    }
                    System.out.println();

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

                    Booking booking = manager.createBooking(customer, room, dr);
                    System.out.println("Booking created with ID: " + booking.getBookingId());
                    break;

                case 2:
                    System.out.print("Enter your email: ");
                    sc.nextLine();
                    String cancelEmail = sc.nextLine().trim();

                    System.out.print("Enter booking ID: ");
                    int cancelId = sc.nextInt();

                    try {
                        manager.cancelBooking(cancelId, cancelEmail);
                        System.out.println("Booking cancelled.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    } catch (SecurityException e) {
                        System.out.println("Error: " + e.getMessage());
                    } catch (IllegalStateException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 3:
                    List<Booking> pendingBookings = bookingRepo.findByStatus(BookingStatus.PENDING);

                    if (pendingBookings.isEmpty()) {
                        System.out.println("No pending bookings.");
                        break;
                    }

                    System.out.println("\n--- Pending Bookings ---");
                    System.out.printf("%-6s %-16s %-24s %-6s %-14s %-10s%n",
                            "ID", "Customer", "Email", "Room", "Type", "Dates");
                    System.out.println("----------------------------------------------------------------------");
                    for (Booking b : pendingBookings) {
                        System.out.printf("%-6d %-16s %-24s %-6d %-14s %s to %s%n",
                                b.getBookingId(),
                                b.getCustomer().getName(),
                                b.getCustomer().getEmail(),
                                b.getRoom().getRoomId(),
                                b.getRoom().getRoomType().getDisplayName(),
                                b.getDateRange().getStart(),
                                b.getDateRange().getEnd()
                        );
                    }
                    System.out.println();
                    System.out.print("Enter booking ID: ");
                    int completeId = sc.nextInt();
                    manager.completeBooking(completeId);
                    System.out.println("Booking completed");
                    break;

                case 4:
                    sc.close();
                    return;
            }
        }
     */
}
