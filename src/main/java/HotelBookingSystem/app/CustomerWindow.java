/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package HotelBookingSystem.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.JPanel;

/**
 *
 * @author Admin
 */
public class CustomerWindow extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CustomerWindow.class.getName());

    /**
     * Creates new form CustomerWindow
     */
    public CustomerWindow() {
        initComponents();
        
    setSize(800, 600);
    setLocationRelativeTo(null);

    
    JPanel mainContainer = new JPanel(new BorderLayout());

    headerPanel.setPreferredSize(new java.awt.Dimension(0, 100));
    mainContainer.add(headerPanel, BorderLayout.NORTH);
    
    headerPanel.setLayout(new java.awt.GridBagLayout());
    java.awt.GridBagConstraints hgbc = new GridBagConstraints();
    hgbc.gridx = 0;
    hgbc.gridy = 0;
    hgbc.anchor = java.awt.GridBagConstraints.CENTER;
    
    headerPanel.add(customerMenuLabel, hgbc);

    
    JPanel centerPanel = new JPanel(new GridBagLayout());
    java.awt.GridBagConstraints gbc = new GridBagConstraints();
     
    
    JPanel leftPanel = new JPanel(new BorderLayout());
    leftPanel.setBackground(Color.white);
    
    createBookingButton.setPreferredSize(new java.awt.Dimension(150, 50));
    cancelBookingButton.setPreferredSize(new java.awt.Dimension(150, 50));
    checkoutButton.setPreferredSize(new java.awt.Dimension(150, 50));
    logOutButton.setPreferredSize(new java.awt.Dimension(150, 50));

    JPanel topButtons = new JPanel(new GridLayout(3, 1, 10, 10));
    topButtons.add(createBookingButton);
    topButtons.add(cancelBookingButton);
    topButtons.add(checkoutButton);

    leftPanel.add(topButtons, BorderLayout.NORTH);
    topButtons.setBackground(Color.white);

    JPanel bottomPanel = new JPanel(new BorderLayout());
    bottomPanel.add(logOutButton, BorderLayout.SOUTH);
    leftPanel.add(bottomPanel, BorderLayout.SOUTH);

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.weighty = 1.0;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 0.2; 
    centerPanel.add(leftPanel, gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.weightx = 1.0;
    gbc.fill = GridBagConstraints.BOTH;
    centerPanel.add(roomsPanel, gbc);

    mainContainer.add(centerPanel, java.awt.BorderLayout.CENTER);

    
    setContentPane(mainContainer);

    revalidate();
    repaint();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        customerPanel = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        customerMenuLabel = new javax.swing.JLabel();
        roomsPanel = new javax.swing.JPanel();
        createBookingButton = new javax.swing.JButton();
        cancelBookingButton = new javax.swing.JButton();
        checkoutButton = new javax.swing.JButton();
        logOutButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        customerPanel.setBackground(new java.awt.Color(255, 255, 255));

        headerPanel.setBackground(new java.awt.Color(102, 204, 255));

        customerMenuLabel.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        customerMenuLabel.setText("Customer Menu");

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addGap(159, 159, 159)
                .addComponent(customerMenuLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerPanelLayout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(customerMenuLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );

        roomsPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

        javax.swing.GroupLayout roomsPanelLayout = new javax.swing.GroupLayout(roomsPanel);
        roomsPanel.setLayout(roomsPanelLayout);
        roomsPanelLayout.setHorizontalGroup(
            roomsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 353, Short.MAX_VALUE)
        );
        roomsPanelLayout.setVerticalGroup(
            roomsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        createBookingButton.setFont(new java.awt.Font("MS PGothic", 0, 18)); // NOI18N
        createBookingButton.setText("Create Booking");
        createBookingButton.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        createBookingButton.setBorderPainted(false);

        cancelBookingButton.setFont(new java.awt.Font("MS PGothic", 0, 18)); // NOI18N
        cancelBookingButton.setText("Cancel Booking");
        cancelBookingButton.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        cancelBookingButton.setBorderPainted(false);

        checkoutButton.setFont(new java.awt.Font("MS PGothic", 0, 18)); // NOI18N
        checkoutButton.setText("Check Out");
        checkoutButton.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        checkoutButton.setBorderPainted(false);

        logOutButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        logOutButton.setForeground(new java.awt.Color(255, 0, 51));
        logOutButton.setText("Log out");

        javax.swing.GroupLayout customerPanelLayout = new javax.swing.GroupLayout(customerPanel);
        customerPanel.setLayout(customerPanelLayout);
        customerPanelLayout.setHorizontalGroup(
            customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(customerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(createBookingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(checkoutButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cancelBookingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(logOutButton))
                .addGap(18, 18, 18)
                .addComponent(roomsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        customerPanelLayout.setVerticalGroup(
            customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customerPanelLayout.createSequentialGroup()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(roomsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(customerPanelLayout.createSequentialGroup()
                        .addComponent(createBookingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cancelBookingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(checkoutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 83, Short.MAX_VALUE)
                        .addComponent(logOutButton)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(customerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(customerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new CustomerWindow().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelBookingButton;
    private javax.swing.JButton checkoutButton;
    private javax.swing.JButton createBookingButton;
    private javax.swing.JLabel customerMenuLabel;
    private javax.swing.JPanel customerPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JButton logOutButton;
    private javax.swing.JPanel roomsPanel;
    // End of variables declaration//GEN-END:variables
}
