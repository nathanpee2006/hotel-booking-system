package HotelBookingSystem;

import java.awt.CardLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CustomerPanel extends javax.swing.JPanel {

    public GUICommands gui;
    public User user;
    public DBManager db;
    private JLabel roomIdLabel;
    private JLabel roomTypeLabel;
    private JLabel roomPriceLabel;
    private JButton confirmBookingButton;
    private JButton backToListButton;

    
    public CustomerPanel() {
        
        initComponents();
        
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

// Add both views to the CardLayout container
        roomContentPanel.add(jScrollPane1, "list");
        roomContentPanel.add(roomDetailsPanel, "details");

// Replace jScrollPane1 in your layout with roomContentPanel

        roomList.addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting()) {
            String selected = roomList.getSelectedValue();
            if (selected != null) {
                showRoomDetails(selected);
                }
            }
        });
        
        backToListButton.addActionListener(e -> {
        CardLayout cl = (CardLayout) roomContentPanel.getLayout();
        cl.show(roomContentPanel, "list");
    });

    }
    
    private void showRoomDetails(String item) {
        String[] parts = item.split(" - ");
        roomIdLabel.setText("Room ID: " + parts[0]);
        roomTypeLabel.setText("Type: " + parts[1]);
        roomPriceLabel.setText("Price: " + parts[2]);

        CardLayout cl = (CardLayout) roomContentPanel.getLayout();
        cl.show(roomContentPanel, "details");
    }
    
    



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        headerPanel1 = new javax.swing.JPanel();
        WelcomeLabel1 = new java.awt.Label();
        backButton = new javax.swing.JButton();
        createBookingButton = new javax.swing.JButton();
        cancelBookingButton = new javax.swing.JButton();
        myBookingsButton = new javax.swing.JButton();
        checkOutButton = new javax.swing.JButton();
        roomContentPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        roomList = new javax.swing.JList<>();
        roomDetailsPanel = new javax.swing.JPanel();

        jPanel1.setPreferredSize(new java.awt.Dimension(900, 700));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(900, 700));

        headerPanel1.setBackground(new java.awt.Color(102, 204, 255));

        WelcomeLabel1.setAlignment(java.awt.Label.CENTER);
        WelcomeLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        WelcomeLabel1.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        WelcomeLabel1.setName("WelcomeLabel"); // NOI18N
        WelcomeLabel1.setText("Welcome to the Hotel Booking System!"); // NOI18N

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 97, Short.MAX_VALUE)
                .addComponent(WelcomeLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(176, 176, 176))
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

        cancelBookingButton.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        cancelBookingButton.setText("Cancel Booking");
        cancelBookingButton.setBorder(new javax.swing.border.MatteBorder(null));

        myBookingsButton.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        myBookingsButton.setText("My Bookings");
        myBookingsButton.setBorder(new javax.swing.border.MatteBorder(null));

        checkOutButton.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        checkOutButton.setText("Check Out");
        checkOutButton.setBorder(new javax.swing.border.MatteBorder(null));

        roomContentPanel.setLayout(new java.awt.CardLayout());

        roomList.setBorder(new javax.swing.border.MatteBorder(null));
        roomList.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        roomList.setFixedCellHeight(30);
        jScrollPane1.setViewportView(roomList);

        roomContentPanel.add(jScrollPane1, "card2");

        roomDetailsPanel.setBackground(new java.awt.Color(255, 255, 255));
        roomDetailsPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        roomDetailsPanel.setLayout(new java.awt.CardLayout());
        roomContentPanel.add(roomDetailsPanel, "card3");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(headerPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(createBookingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkOutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(myBookingsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelBookingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(roomContentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 713, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(653, 653, 653))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(headerPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(createBookingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cancelBookingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(myBookingsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(checkOutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(358, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(roomContentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 900, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 700, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed

        gui.handleBack();
        
    }//GEN-LAST:event_backButtonActionPerformed

    private void createBookingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createBookingButtonActionPerformed

        gui.loadAvailableRoomList(this);

    }//GEN-LAST:event_createBookingButtonActionPerformed

    public void setGUI(GUICommands gui) {
        this.gui = gui;
        this.user = gui.getUser();
        this.db = gui.getDb();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Label WelcomeLabel1;
    private javax.swing.JButton backButton;
    private javax.swing.JButton cancelBookingButton;
    private javax.swing.JButton checkOutButton;
    private javax.swing.JButton createBookingButton;
    private javax.swing.JPanel headerPanel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton myBookingsButton;
    public javax.swing.JPanel roomContentPanel;
    private javax.swing.JPanel roomDetailsPanel;
    public javax.swing.JList<String> roomList;
    // End of variables declaration//GEN-END:variables
}
