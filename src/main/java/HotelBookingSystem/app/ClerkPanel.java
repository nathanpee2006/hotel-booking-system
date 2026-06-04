package HotelBookingSystem.app;

import HotelBookingSystem.model.User;
import HotelBookingSystem.db.DBManager;
import HotelBookingSystem.model.Room;
import java.awt.CardLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ClerkPanel extends javax.swing.JPanel {

    public GUICommands gui;
    public User user;
    public DBManager db;
    private JLabel roomIdLabel;
    private JLabel roomTypeLabel;
    private JLabel roomPriceLabel;
    private JButton confirmBookingButton;
    private JButton backToListButton;
    public JLabel bookingStatusLabel;
    public JPanel bookingDetailsPanel;
    public JPanel pendingDetailsPanel;
    public JLabel bookingIdLabel;
    public JLabel bookingRoomLabel;
    public JLabel bookingDatesLabel;
    public JButton cancelBookingInListButton;
    public JButton checkInButton;
    public JButton backToBookingListButton;
    public JButton approveBookingButton;
    public JButton checkOutButton;
    public JButton cancelPendingButton;
    public JButton backToPendingListButton;
    public JLabel pendingIdLabel;
    public JLabel pendingRoomLabel;
    public JLabel pendingDatesLabel;
    public JLabel pendingStatusLabel;
    public JPanel checkedInDetailsPanel;
    public JLabel checkedInIdLabel;
    public JLabel checkedInRoomLabel;
    public JLabel checkedInDatesLabel;
    public JLabel checkedInStatusLabel;
    public JButton backToCheckedInListButton;
    public JPanel checkoutRequestDetailsPanel;
    public JLabel checkoutReqIdLabel;
    public JLabel checkoutReqRoomLabel;
    public JLabel checkoutReqDatesLabel;
    public JLabel checkoutReqStatusLabel;
    public JButton confirmCheckoutButton;
    public JButton backToCheckoutRequestListButton;

    public ClerkPanel() {

        initComponents();

        // =========================
        // PENDING BOOKINGS PANEL
        // =========================
        pendingDetailsPanel = new JPanel();
        pendingDetailsPanel.setLayout(new BoxLayout(pendingDetailsPanel, BoxLayout.Y_AXIS));
        pendingDetailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        pendingIdLabel = new JLabel();
        pendingRoomLabel = new JLabel();
        pendingDatesLabel = new JLabel();
        pendingStatusLabel = new JLabel();

        approveBookingButton = new JButton("Approve Booking");
        cancelPendingButton = new JButton("Cancel Booking");
        backToPendingListButton = new JButton("Back to List");

        pendingDetailsPanel.add(pendingIdLabel);
        pendingDetailsPanel.add(pendingRoomLabel);
        pendingDetailsPanel.add(pendingDatesLabel);
        pendingDetailsPanel.add(pendingStatusLabel);
        pendingDetailsPanel.add(Box.createVerticalStrut(20));
        pendingDetailsPanel.add(approveBookingButton);
        pendingDetailsPanel.add(cancelPendingButton);
        pendingDetailsPanel.add(backToPendingListButton);

        roomContentPanel.add(pendingDetailsPanel, "pendingDetails");

        backToPendingListButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) roomContentPanel.getLayout();
            cl.show(roomContentPanel, "list");
        });

        // =========================
        // Create BOOKINGS PANEL
        // =========================
        roomDetailsPanel.setLayout(new BoxLayout(roomDetailsPanel, BoxLayout.Y_AXIS));
        roomDetailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        roomIdLabel = new JLabel();
        roomTypeLabel = new JLabel();
        roomPriceLabel = new JLabel();

        confirmBookingButton = new JButton("Confirm Booking");
        backToListButton = new JButton("Back to List");

        roomDetailsPanel.add(roomIdLabel);
        roomDetailsPanel.add(roomTypeLabel);
        roomDetailsPanel.add(roomPriceLabel);
        roomDetailsPanel.add(Box.createVerticalStrut(20));
        roomDetailsPanel.add(confirmBookingButton);
        roomDetailsPanel.add(backToListButton);

        roomContentPanel.add(roomListPanel, "list");
        roomContentPanel.add(roomDetailsPanel, "details");

        roomList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showRoomDetails();
            }
        });

        confirmBookingButton.addActionListener(e -> {
            String roomIdText = roomIdLabel.getText();
            gui.createBookingForSelectedRoom(roomIdText);
        });

        backToListButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) roomContentPanel.getLayout();
            cl.show(roomContentPanel, "list");
        });

        // =========================
        // CHECKOUT BOOKINGS PANEL
        // =========================
        checkoutRequestDetailsPanel = new JPanel();
        checkoutRequestDetailsPanel.setLayout(new BoxLayout(checkoutRequestDetailsPanel, BoxLayout.Y_AXIS));
        checkoutRequestDetailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        checkoutReqIdLabel = new JLabel();
        checkoutReqRoomLabel = new JLabel();
        checkoutReqDatesLabel = new JLabel();
        checkoutReqStatusLabel = new JLabel();

        confirmCheckoutButton = new JButton("Confirm Checkout");
        backToCheckoutRequestListButton = new JButton("Back to List");

        checkoutRequestDetailsPanel.add(checkoutReqIdLabel);
        checkoutRequestDetailsPanel.add(checkoutReqRoomLabel);
        checkoutRequestDetailsPanel.add(checkoutReqDatesLabel);
        checkoutRequestDetailsPanel.add(checkoutReqStatusLabel);
        checkoutRequestDetailsPanel.add(Box.createVerticalStrut(20));
        checkoutRequestDetailsPanel.add(confirmCheckoutButton);
        checkoutRequestDetailsPanel.add(backToCheckoutRequestListButton);

        // Add to CardLayout
        roomContentPanel.add(checkoutRequestDetailsPanel, "checkoutRequestDetails");

        // Back button logic
        backToCheckoutRequestListButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) roomContentPanel.getLayout();
            cl.show(roomContentPanel, "list");
        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        headerPanel1 = new javax.swing.JPanel();
        WelcomeLabel1 = new java.awt.Label();
        backButton = new javax.swing.JButton();
        createBookingButton = new javax.swing.JButton();
        viewPendingButton = new javax.swing.JButton();
        roomContentPanel = new javax.swing.JPanel();
        roomListPanel = new javax.swing.JScrollPane();
        roomList = new javax.swing.JList<>();
        roomDetailsPanel = new javax.swing.JPanel();
        closeAppButton = new javax.swing.JButton();
        checkoutRequestButton = new javax.swing.JButton();

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(900, 700));

        headerPanel1.setBackground(new java.awt.Color(102, 204, 255));

        WelcomeLabel1.setAlignment(java.awt.Label.CENTER);
        WelcomeLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        WelcomeLabel1.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        WelcomeLabel1.setName("WelcomeLabel"); // NOI18N
        WelcomeLabel1.setText("Hotel Clerk Menu"); // NOI18N

        backButton.setText("Back");
        backButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        backButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        backButton.addActionListener(this::backButtonActionPerformed);

        javax.swing.GroupLayout headerPanel1Layout = new javax.swing.GroupLayout(headerPanel1);
        headerPanel1.setLayout(headerPanel1Layout);
        headerPanel1Layout.setHorizontalGroup(
            headerPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(WelcomeLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 752, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(157, 157, 157))
        );
        headerPanel1Layout.setVerticalGroup(
            headerPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(WelcomeLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(headerPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        createBookingButton.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        createBookingButton.setText("Create Booking");
        createBookingButton.setBorder(new javax.swing.border.MatteBorder(null));
        createBookingButton.addActionListener(this::createBookingButtonActionPerformed);

        viewPendingButton.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        viewPendingButton.setText(" Pending Bookings "); // NOI18N
        viewPendingButton.setBorder(new javax.swing.border.MatteBorder(null));
        viewPendingButton.addActionListener(this::viewPendingButtonActionPerformed);

        roomContentPanel.setLayout(new java.awt.CardLayout());

        roomList.setBorder(new javax.swing.border.MatteBorder(null));
        roomList.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        roomList.setFixedCellHeight(30);
        roomListPanel.setViewportView(roomList);

        roomContentPanel.add(roomListPanel, "card2");

        roomDetailsPanel.setBackground(new java.awt.Color(255, 255, 255));
        roomDetailsPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        roomDetailsPanel.setLayout(new java.awt.CardLayout());
        roomContentPanel.add(roomDetailsPanel, "card3");

        closeAppButton.setBackground(new java.awt.Color(255, 51, 51));
        closeAppButton.setFont(new java.awt.Font("Elephant", 0, 12)); // NOI18N
        closeAppButton.setText("Close Program");
        closeAppButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        closeAppButton.setBorderPainted(false);
        closeAppButton.addActionListener(this::closeAppButtonActionPerformed);

        checkoutRequestButton.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        checkoutRequestButton.setText("Check Out Requests"); // NOI18N
        checkoutRequestButton.setBorder(new javax.swing.border.MatteBorder(null));
        checkoutRequestButton.addActionListener(this::checkoutRequestButtonActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(createBookingButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(viewPendingButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(closeAppButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(checkoutRequestButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(roomContentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 707, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(headerPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(headerPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(roomContentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 618, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(createBookingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(viewPendingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(checkoutRequestButton, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(closeAppButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed

        gui.handleBack();

    }//GEN-LAST:event_backButtonActionPerformed

    private void createBookingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createBookingButtonActionPerformed

        gui.loadAvailableRoomList(this);

    }//GEN-LAST:event_createBookingButtonActionPerformed

    private void viewPendingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewPendingButtonActionPerformed

        gui.loadPendingBookings(this);

    }//GEN-LAST:event_viewPendingButtonActionPerformed

    private void checkoutRequestButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkoutRequestButtonActionPerformed

        gui.loadCheckoutRequests(this);

    }//GEN-LAST:event_checkoutRequestButtonActionPerformed

    private void closeAppButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeAppButtonActionPerformed

        gui.closeProgram();

    }//GEN-LAST:event_closeAppButtonActionPerformed

    public void showRoomDetails() {
        String selected = roomList.getSelectedValue();
        if (selected == null) {
            return;
        }

        // Format: "101 - Deluxe - $120"
        String[] parts = selected.split(" - ");
        if (parts.length < 1) {
            return;
        }

        int roomId = Integer.parseInt(parts[0].trim());

        Room room = db.findRoomById(roomId);
        if (room == null) {
            return;
        }

        roomIdLabel.setText("Room ID: " + room.getRoomId());
        roomTypeLabel.setText("Type: " + room.getRoomType());
        roomPriceLabel.setText("Price: $" + room.getPrice());

        CardLayout cl = (CardLayout) roomContentPanel.getLayout();
        cl.show(roomContentPanel, "details");
    }

    public void setGUI(GUICommands gui) {
        this.gui = gui;
        this.user = gui.getUser();
        this.db = gui.getDb();
    }

    public void setCurrentUser(User user) {
        this.user = user;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Label WelcomeLabel1;
    private javax.swing.JButton backButton;
    private javax.swing.JButton checkoutRequestButton;
    private javax.swing.JButton closeAppButton;
    private javax.swing.JButton createBookingButton;
    private javax.swing.JPanel headerPanel1;
    private javax.swing.JPanel jPanel2;
    public javax.swing.JPanel roomContentPanel;
    private javax.swing.JPanel roomDetailsPanel;
    public javax.swing.JList<String> roomList;
    private javax.swing.JScrollPane roomListPanel;
    private javax.swing.JButton viewPendingButton;
    // End of variables declaration//GEN-END:variables
}
