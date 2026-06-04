package HotelBookingSystem.app;

import HotelBookingSystem.model.*;
import HotelBookingSystem.db.DBManager;
import HotelBookingSystem.service.*;
import HotelBookingSystem.repository.*;
import java.awt.CardLayout;
import java.awt.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;

public class GUICommands {

    private final LoginPanel loginPanel;
    private final RegisterPanel registerPanel;
    private final MainWindow window;
    private final AuthService authService;
    private final DBManager db;
    private final Stack<String> navigationStack = new Stack<>();
    private final BookingManager manager;
    private final IRoomRepository roomRepo;
    private final IBookingRepository bookRepo;
    private UserRole selectedRole = null;
    private User currentUser;
     

    public GUICommands(MainWindow window, AuthService authService, DBManager db, BookingManager manager, IRoomRepository roomRepo, IBookingRepository bookRepo) {
        this.window = window;
        this.loginPanel = window.logInPanel; // use the SAME instance
        this.authService = authService;
        this.db = db;
        this.registerPanel = window.registerPanel;
        navigationStack.push("startupPanel");
        this.manager = manager;
        this.roomRepo = roomRepo;
        this.bookRepo = bookRepo;
    }

    // ---------------------------------------------------------
    // PANEL SWITCHING
    // ---------------------------------------------------------
    public void switchPanel(String panelName) {
        CardLayout cl = (CardLayout) window.MainPanel.getLayout();
        cl.show(window.MainPanel, panelName);
    }

    public void goToLogin() {
        navigationStack.push("startupPanel");
        switchPanel("loginPanel");
    }

    public void goToRegister() {
        navigationStack.push("startupPanel");
        switchPanel("registerPanel");
    }

    public void goToStartup() {
        switchPanel("startupPanel");
    }

    // ---------------------------------------------------------
    // BACK BUTTON
    // ---------------------------------------------------------
    public void handleBack() {
        if (!navigationStack.isEmpty()) {
            String previous = navigationStack.pop();
            switchPanel(previous);
        }
    }

    // ---------------------------------------------------------
    // LOGIN
    // ---------------------------------------------------------
    public void handleLogin() {
    String email = loginPanel.emailTextField.getText().trim();
    String password = loginPanel.passwordTextField.getText().trim();

    if (email.isEmpty() || password.isEmpty()) {
        JOptionPane.showMessageDialog(window,
                "Please enter both email and password.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        // Authenticate user
        User authenticated = authService.login(email, password);
        currentUser = authenticated;

        if (authenticated.getRole() == null) {
            JOptionPane.showMessageDialog(window,
                    "This account has no assigned role.",
                    "Role Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // OPTIONAL: Show welcome message using stored name
        JOptionPane.showMessageDialog(window,
                "Welcome, " + authenticated.getName() + "!");

        // Switch to correct panel
        switch (authenticated.getRole()) {
            case CUSTOMER -> {
                // Pass user to customer panel if needed
                window.customerPanel.setCurrentUser(authenticated);
                switchPanel("customerPanel");
            }
            case CLERK -> {
                window.clerkPanel.setCurrentUser(authenticated);
                switchPanel("clerkPanel");
            }
            default -> JOptionPane.showMessageDialog(window,
                    "Unknown role: " + authenticated.getRole(),
                    "Role Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    } catch (IllegalArgumentException ex) {
        JOptionPane.showMessageDialog(window,
                ex.getMessage(),
                "Login Failed",
                JOptionPane.ERROR_MESSAGE);
        }
    }


    // ---------------------------------------------------------
    // REGISTER
    // ---------------------------------------------------------
    public void handleRegister() {

    String name = registerPanel.nameTextField.getText().trim();
    String email = registerPanel.emailTextField.getText().trim();
    String password = registerPanel.passwordTextField.getText().trim();

    // Validate fields
    if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
        JOptionPane.showMessageDialog(window,
                "Please enter name, email, and password.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        // Register user with REAL name
        authService.register(name, email, password, UserRole.CUSTOMER);

        JOptionPane.showMessageDialog(window,
                "Registration successful! You can now log in.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        switchPanel("loginPanel");

    } catch (IllegalArgumentException ex) {
        JOptionPane.showMessageDialog(window,
                ex.getMessage(),
                "Registration Failed",
                JOptionPane.ERROR_MESSAGE);
    }
}


    // ---------------------------------------------------------
    // ROOM LOADERS
    // ---------------------------------------------------------
    
    public void loadAvailableRoomList(JPanel panel) {
    try {
        List<Room> availableRooms = db.findAvailableRooms();
        DefaultListModel<String> model = new DefaultListModel<>();

        for (Room room : availableRooms) {
            model.addElement(room.getRoomId() + " - " + room.getRoomType() + " - $" + room.getPrice());
        }

        // Determine which panel type is calling
        JList<String> list;
        JPanel contentPanel;

        if (panel instanceof CustomerPanel cp) {
            list = cp.roomList;
            contentPanel = cp.roomContentPanel;
        } 
        else if (panel instanceof ClerkPanel clp) {
            list = clp.roomList;
            contentPanel = clp.roomContentPanel;
        } 
        else {
            return; // Unknown panel type
        }

        list.setModel(model);

        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "list");

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(panel, "Failed to load rooms: " + e.getMessage());
    }
    }

    
    public void loadPendingBookings(ClerkPanel panel) {
    try {
        // 1. Fetch all pending bookings
        List<Booking> pending = bookRepo.findByStatus(BookingStatus.PENDING);

        DefaultListModel<String> model = new DefaultListModel<>();

        if (pending.isEmpty()) {
            model.addElement("No pending bookings.");
        } else {
            for (Booking b : pending) {
                model.addElement(
                        "Booking #" + b.getBookingId()
                        + " | Room " + b.getRoom().getRoomId()
                        + " | " + b.getDateRange().getStart()
                        + " → " + b.getDateRange().getEnd()
                        + " | " + b.getBookingStatus()
                );
            }
        }

        // 2. Load into clerk list
        panel.roomList.setModel(model);

        // 3. Remove old listeners to avoid duplicates
        for (var listener : panel.roomList.getListSelectionListeners()) {
            panel.roomList.removeListSelectionListener(listener);
        }

        // 4. Add listener for selecting a booking
        panel.roomList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = panel.roomList.getSelectedValue();
                if (selected != null && selected.startsWith("Booking #")) {
                    showBookingDetails(selected, panel);
                }
            }
        });

        // 5. Show the list card
        CardLayout cl = (CardLayout) panel.roomContentPanel.getLayout();
        cl.show(panel.roomContentPanel, "card2"); // your list card

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(panel, "Failed to load pending bookings: " + e.getMessage());
    }
    }
    
    public void loadCheckedInBookings(ClerkPanel panel) {
    try {
        List<Booking> checkedIn = bookRepo.findByStatus(BookingStatus.CHECKED_IN);

        DefaultListModel<String> model = new DefaultListModel<>();

        if (checkedIn.isEmpty()) {
            model.addElement("No checked-in bookings.");
        } else {
            for (Booking b : checkedIn) {
                model.addElement(
                        "Booking #" + b.getBookingId()
                        + " | Room " + b.getRoom().getRoomId()
                        + " | " + b.getDateRange().getStart()
                        + " → " + b.getDateRange().getEnd()
                        + " | " + b.getBookingStatus()
                );
            }
        }

        panel.roomList.setModel(model);

        // Remove old listeners
        for (var listener : panel.roomList.getListSelectionListeners()) {
            panel.roomList.removeListSelectionListener(listener);
        }

        // Add new listener
        panel.roomList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = panel.roomList.getSelectedValue();
                if (selected != null && selected.startsWith("Booking #")) {
                    showBookingDetails(selected, panel);
                }
            }
        });

        CardLayout cl = (CardLayout) panel.roomContentPanel.getLayout();
        cl.show(panel.roomContentPanel, "list");

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(panel, "Failed to load checked-in bookings: " + e.getMessage());
    }
    }




    // ---------------------------------------------------------
    // CREATE BOOKING FOR SELECTED ROOM
    // ---------------------------------------------------------
    public void createBookingForSelectedRoom(String roomIdText) {
    try {
        // 1. Extract room ID
        int roomId = Integer.parseInt(roomIdText.replace("Room ID: ", "").trim());
        Room room = db.findRoomById(roomId);

        if (room == null) {
            JOptionPane.showMessageDialog(window, "Room not found.");
            return;
        }

        // 2. Determine booking customer
        User user = getUser();
        Customer bookingCustomer;

        if (user instanceof Customer customer) {
            bookingCustomer = customer;
        } else if (user instanceof HotelClerk) {
            // Clerk booking for walk-in guest
            String name = JOptionPane.showInputDialog(window, "Enter customer name:");
            if (name == null || name.isBlank()) {
                JOptionPane.showMessageDialog(window, "Booking cancelled: name required.");
                return;
            }

            String email = JOptionPane.showInputDialog(window, "Enter customer email:");
            if (email == null || email.isBlank()) {
                JOptionPane.showMessageDialog(window, "Booking cancelled: email required.");
                return;
            }

            bookingCustomer = new Customer(-1, name, email, null);
        } else {
            JOptionPane.showMessageDialog(window, "This user type cannot create bookings.");
            return;
        }

        // 3. Ask for dates
        String startInput = JOptionPane.showInputDialog(window, "Enter start date (YYYY-MM-DD):");
        String endInput = JOptionPane.showInputDialog(window, "Enter end date (YYYY-MM-DD):");

        if (startInput == null || endInput == null) {
            JOptionPane.showMessageDialog(window, "Booking cancelled.");
            return;
        }

        LocalDate start = LocalDate.parse(startInput);
        LocalDate end = LocalDate.parse(endInput);

        if (end.isBefore(start)) {
            JOptionPane.showMessageDialog(window, "End date cannot be before start date.");
            return;
        }

        DateRange range = new DateRange(start, end);

        String cardNumber = JOptionPane.showInputDialog(window, "Enter card number:");
        if (cardNumber == null || cardNumber.isBlank()) {
            JOptionPane.showMessageDialog(window, "Booking cancelled: card number required.");
            return;
        }

        String expiry = JOptionPane.showInputDialog(window, "Enter expiry (MM/YYYY):");
        if (expiry == null || expiry.isBlank()) {
            JOptionPane.showMessageDialog(window, "Booking cancelled: expiry required.");
            return;
        }
        
        CardDetails card = new CardDetails(cardNumber, expiry);
        
        // 4. Create booking using BookingManager
        Booking booking = manager.createBooking(
                bookingCustomer.getUserId(),
                room,
                range,
                card
        );

        JOptionPane.showMessageDialog(window,
                "Booking created successfully! ID: " + booking.getBookingId(),
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(window,
                "Failed to create booking: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }


    // ---------------------------------------------------------
    // SHOW MY BOOKINGS
    // ---------------------------------------------------------
    public void showMyBookings(CustomerPanel panel) {
        
        if (!(currentUser instanceof Customer customer)) {
            JOptionPane.showMessageDialog(null, "Only customers can view bookings.");
            return;
        }

        List<Booking> bookings = bookRepo.findByUserId(customer.getUserId());
        DefaultListModel<String> model = new DefaultListModel<>();

        if (bookings.isEmpty()) {
            model.addElement("You have no bookings.");
        } else {
            for (Booking b : bookings) {
                model.addElement(
                        "Booking #" + b.getBookingId()
                        + " | Room " + b.getRoom().getRoomId()
                        + " | " + b.getDateRange().getStart()
                        + " → " + b.getDateRange().getEnd()
                        + " | " + b.getBookingStatus()
                );
            }
        }

        panel.roomList.setModel(model);

        panel.roomList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = panel.roomList.getSelectedValue();
                if (selected != null && selected.startsWith("Booking #")) {
                    showBookingDetails(selected, panel);
                }
            }
        });

        CardLayout cl = (CardLayout) panel.roomContentPanel.getLayout();
        cl.show(panel.roomContentPanel, "list");
    }

    // ---------------------------------------------------------
    // SHOW BOOKING DETAILS
    // ---------------------------------------------------------    
    public void showBookingDetails(String item, JPanel panel) {
        
    try {
        // Extract booking ID
        Pattern pattern = Pattern.compile("Booking #(\\d+)");
        Matcher matcher = pattern.matcher(item);

        
        if (!matcher.find()) {
            throw new IllegalArgumentException("Could not extract booking ID from: " + item);
        }

        int bookingId = Integer.parseInt(matcher.group(1));
        System.out.println("Extracted booking ID: " + bookingId);

        Booking booking = bookRepo.findById(bookingId);
        if (booking == null) {
            JOptionPane.showMessageDialog(panel, "Booking not found.");
            return;
        }

        BookingStatus status = booking.getBookingStatus();

        // ================================
        // CUSTOMER PANEL
        // ================================
        if (panel instanceof CustomerPanel cp) {

            cp.bookingIdLabel.setText("Booking ID: " + booking.getBookingId());
            cp.bookingRoomLabel.setText("Room: " + booking.getRoom().getRoomId());
            cp.bookingDatesLabel.setText("Dates: " +
                    booking.getDateRange().getStart() + " → " +
                    booking.getDateRange().getEnd());
            cp.bookingStatusLabel.setText("Status: " + booking.getBookingStatus());

            // Show/hide buttons
            cp.cancelBookingInListButton.setVisible(status == BookingStatus.PENDING);
            cp.checkInButton.setVisible(status == BookingStatus.COMPLETED);

            // Remove old listeners
            for (var a : cp.cancelBookingInListButton.getActionListeners())
                cp.cancelBookingInListButton.removeActionListener(a);

            for (var a : cp.checkInButton.getActionListeners())
                cp.checkInButton.removeActionListener(a);

            // Add new listeners
            cp.cancelBookingInListButton.addActionListener(e -> cancelBooking(booking, cp));
            cp.checkInButton.addActionListener(e -> checkInBooking(booking, cp));

            // Show panel
            CardLayout cl = (CardLayout) cp.roomContentPanel.getLayout();
            cl.show(cp.roomContentPanel, "bookingDetails");
            return;
        }

        // ================================
        // CLERK PANEL
        // ================================
        if (panel instanceof ClerkPanel clp) {

            // ----- PENDING BOOKINGS -----
            if (status == BookingStatus.PENDING) {

                clp.pendingIdLabel.setText("Booking ID: " + booking.getBookingId());
                clp.pendingRoomLabel.setText("Room: " + booking.getRoom().getRoomId());
                clp.pendingDatesLabel.setText("Dates: " +
                        booking.getDateRange().getStart() + " → " +
                        booking.getDateRange().getEnd());
                clp.pendingStatusLabel.setText("Status: " + booking.getBookingStatus());

                // Remove old listeners
                for (var a : clp.approveBookingButton.getActionListeners())
                    clp.approveBookingButton.removeActionListener(a);

                for (var a : clp.cancelPendingButton.getActionListeners())
                    clp.cancelPendingButton.removeActionListener(a);

                // Add new listeners
                clp.approveBookingButton.addActionListener(e -> approveBooking(booking, clp));
                clp.cancelPendingButton.addActionListener(e -> cancelBooking(booking, clp));

                // Show panel
                CardLayout cl = (CardLayout) clp.roomContentPanel.getLayout();
                cl.show(clp.roomContentPanel, "pendingDetails");
                return;
            }

            // ----- CHECKED-IN BOOKINGS -----
            if (status == BookingStatus.CHECKED_IN) {

                clp.checkedInIdLabel.setText("Booking ID: " + booking.getBookingId());
                clp.checkedInRoomLabel.setText("Room: " + booking.getRoom().getRoomId());
                clp.checkedInDatesLabel.setText("Dates: " +
                        booking.getDateRange().getStart() + " → " +
                        booking.getDateRange().getEnd());
                clp.checkedInStatusLabel.setText("Status: " + booking.getBookingStatus());

                // Remove old listeners
                for (var a : clp.checkOutButton.getActionListeners())
                    clp.checkOutButton.removeActionListener(a);

                // Add new listener
                clp.checkOutButton.addActionListener(e -> checkOutBooking(booking, clp));

                // Show panel
                CardLayout cl = (CardLayout) clp.roomContentPanel.getLayout();
                cl.show(clp.roomContentPanel, "checkedInDetails");
                return;
            }

            // ----- OTHER STATUSES (OPTIONAL) -----
            JOptionPane.showMessageDialog(panel, "This booking cannot be managed by the clerk.");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }



    // ---------------------------------------------------------
    // CANCEL BOOKING (PENDING ONLY)
    // ---------------------------------------------------------
    
    public void cancelBooking(Booking booking, JPanel panel) {
    if (booking.getBookingStatus() != BookingStatus.PENDING) {
        JOptionPane.showMessageDialog(panel, "Only pending bookings can be cancelled.");
        return;
    }

    manager.cancelBooking(booking.getBookingId(), currentUser.getUserId());
    db.updateRoomStatus(booking.getRoom().getRoomId(), RoomStatus.AVAILABLE);

    JOptionPane.showMessageDialog(panel, "Booking cancelled.");

    // Refresh correct list
    if (panel instanceof CustomerPanel cp)
        showMyBookings(cp);
    else if (panel instanceof ClerkPanel clp)
        loadPendingBookings(clp);
    }


    // ---------------------------------------------------------
    // CHECK-IN (COMPLETED ONLY)
    // ---------------------------------------------------------
    public void checkInBooking(Booking booking, JPanel panel) {
    if (booking.getBookingStatus() != BookingStatus.COMPLETED) {
        JOptionPane.showMessageDialog(panel, "Only completed bookings can be checked in.");
        return;
    }

    db.checkInBooking(booking.getBookingId());
    db.updateRoomStatus(booking.getRoom().getRoomId(), RoomStatus.OCCUPIED);

    JOptionPane.showMessageDialog(panel, "Check-in successful!");

    // Refresh correct list
    if (panel instanceof CustomerPanel cp)
        showMyBookings(cp);
    else if (panel instanceof ClerkPanel clp)
        loadPendingBookings(clp);
    }

    
    public void approveBooking(Booking booking, ClerkPanel panel) {

        try {
            if (booking.getBookingStatus() != BookingStatus.PENDING) {
                JOptionPane.showMessageDialog(panel, "Only pending bookings can be approved.");
                return;
            }

            // Update booking status
            db.updateBookingStatus(booking.getBookingId(), BookingStatus.COMPLETED);

            // Reserve the room
            db.updateRoomStatus(booking.getRoom().getRoomId(), RoomStatus.RESERVED);

            JOptionPane.showMessageDialog(panel, "Booking approved!");

            // Refresh list
            loadPendingBookings(panel);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(panel, "Failed to approve booking: " + ex.getMessage());
        }
    }

    
    public void checkOutBooking(Booking booking, ClerkPanel panel) {
    try {
        // Only checked-in bookings can be checked out
        if (booking.getBookingStatus() != BookingStatus.CHECKED_IN) {
            JOptionPane.showMessageDialog(panel,
                    "Only checked-in bookings can be checked out.");
            return;
        }

        // Update booking status in DB
        db.checkOutBooking(booking.getBookingId());

        // Free the room
        db.updateRoomStatus(booking.getRoom().getRoomId(), RoomStatus.AVAILABLE);

        JOptionPane.showMessageDialog(panel,
                "Check-out successful! Room is now available.");

        // Refresh the checked-in list
        loadCheckedInBookings(panel);

    } catch (Exception ex) {
        ex.printStackTrace();
            JOptionPane.showMessageDialog(panel,
                    "Failed to check out booking: " + ex.getMessage());
        }
    }



    // ---------------------------------------------------------
    // CLOSE PROGRAM
    // ---------------------------------------------------------
    public void closeProgram() {
        window.dispose();
    }
    

    // ---------------------------------------------------------
    // GETTERS
    // ---------------------------------------------------------
    public DBManager getDb() {
        return db;
    }

    public User getUser() {
        return currentUser;
    }
}
