package HotelBookingSystem.app;

import HotelBookingSystem.model.*;
import HotelBookingSystem.db.DBManager;
import HotelBookingSystem.service.*;
import HotelBookingSystem.repository.IRoomRepository;
import java.awt.CardLayout;
import java.awt.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Stack;
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
    private UserRole selectedRole = null;
    private User currentUser;

    public GUICommands(MainWindow window, AuthService authService, DBManager db, BookingManager manager, IRoomRepository roomRepo) {
        this.window = window;
        this.loginPanel = window.logInPanel; // use the SAME instance
        this.authService = authService;
        this.db = db;
        this.registerPanel = window.registerPanel;
        navigationStack.push("startupPanel");
        this.manager = manager;
        this.roomRepo = roomRepo;
    }

    // PANEL SWITCHING (show/hide)
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

    // BACK BUTTON
    public void handleBack() {

        if (!navigationStack.isEmpty()) {
            String previous = navigationStack.pop();
            switchPanel(previous);
        }

    }

    //LOG IN METHOD
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
            User authenticated = authService.login(email, password);
            currentUser = authenticated;

            if (authenticated.getRole() == null) {
                JOptionPane.showMessageDialog(window,
                        "This account has no assigned role.",
                        "Role Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Switch to correct panel based on DB role
            switch (authenticated.getRole()) {

                case CUSTOMER:
                    switchPanel("customerPanel");
                    break;

                case CLERK:
                    switchPanel("clerkPanel");
                    break;

                default:
                    JOptionPane.showMessageDialog(window,
                            "Unknown role: " + authenticated.getRole(),
                            "Role Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
            }

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(window,
                    ex.getMessage(),
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    //REGISTER METHOD
    public void handleRegister() {

        String email = window.registerPanel.emailTextField.getText().trim();
        String password = window.registerPanel.passwordTextField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(window,
                    "Please enter both email and password.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Register user as CUSTOMER by default
            authService.register(email, email, password, UserRole.CUSTOMER);

            JOptionPane.showMessageDialog(window,
                    "Registration successful! You can now log in.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Go back to login screen
            switchPanel("loginPanel");

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(window,
                    ex.getMessage(),
                    "Registration Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    //Room Loader
    public void loadAvailableRoomList(CustomerPanel panel) {
        try {
            List<Room> availableRooms = db.findAvailableRooms(); // get rooms from DB
            DefaultListModel<String> model = new DefaultListModel<>();

            for (Room room : availableRooms) {
                model.addElement(room.getRoomId() + " - " + room.getRoomType() + " - $" + room.getPrice());
            }

            panel.roomList.setModel(model);

            // Optional: switch to the list view if using CardLayout
            CardLayout cl = (CardLayout) panel.roomContentPanel.getLayout();
            cl.show(panel.roomContentPanel, "list");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel, "Failed to load rooms: " + e.getMessage());
        }
    }

    // Creating Bookings
    public void createBookingForSelectedRoom(String roomIdText) {
        try {
            // Extract room ID
            String roomId = roomIdText.replace("Room ID: ", "").trim();

            // Load Room object
            Room room = db.findRoomById(Integer.parseInt(roomId));
            if (room == null) {
                JOptionPane.showMessageDialog(window, "Room not found.");
                return;
            }

            // Get logged-in user
            User user = getUser();
            if (!(user instanceof Customer customer)) {
                JOptionPane.showMessageDialog(window, "Only customers can create bookings.");
                return;
            }

            // TEMPORARY date range
            LocalDate start = LocalDate.now();
            LocalDate end = start.plusDays(1);
            DateRange range = new DateRange(start, end);

            // Create Booking object (bookingId ignored because DB generates it)
            Booking booking = new Booking(
                    0, // ignored by DB
                    room,
                    range,
                    BookingStatus.PENDING
            );

            // Save booking to DB
            db.saveBooking(booking);

            // Update room status
            db.updateRoomStatus(room.getRoomId(), RoomStatus.RESERVED);

            JOptionPane.showMessageDialog(window,
                    "Booking created successfully!",
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

    //Getters
    public DBManager getDb() {
        return db;
    }

    public User getUser() {
        return currentUser;
    }

}
