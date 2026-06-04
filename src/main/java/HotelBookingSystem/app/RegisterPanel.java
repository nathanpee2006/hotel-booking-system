package HotelBookingSystem.app;

public class RegisterPanel extends javax.swing.JPanel {

    public GUICommands gui;
    
    public RegisterPanel() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        headerPanel1 = new javax.swing.JPanel();
        WelcomeLabel1 = new java.awt.Label();
        backButton = new javax.swing.JButton();
        logInPanel = new javax.swing.JPanel();
        registerButton = new javax.swing.JButton();
        emailTextField = new javax.swing.JTextField();
        emailLabel = new javax.swing.JLabel();
        passwordTextField = new javax.swing.JPasswordField();
        emailLabel1 = new javax.swing.JLabel();
        showcheck = new javax.swing.JCheckBox();
        closeAppButton = new javax.swing.JButton();
        nameTextField = new javax.swing.JTextField();
        nameLabel = new javax.swing.JLabel();

        jPanel2.setPreferredSize(new java.awt.Dimension(900, 700));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        headerPanel1.setBackground(new java.awt.Color(102, 204, 255));

        WelcomeLabel1.setAlignment(java.awt.Label.CENTER);
        WelcomeLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        WelcomeLabel1.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        WelcomeLabel1.setName("WelcomeLabel"); // NOI18N
        WelcomeLabel1.setText("Register"); // NOI18N

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 78, Short.MAX_VALUE)
                .addComponent(WelcomeLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 541, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(176, 176, 176))
        );
        headerPanel1Layout.setVerticalGroup(
            headerPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(WelcomeLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(headerPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        logInPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        registerButton.setBackground(new java.awt.Color(51, 204, 255));
        registerButton.setFont(new java.awt.Font("DialogInput", 0, 12)); // NOI18N
        registerButton.setText("Register");
        registerButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        registerButton.setBorderPainted(false);
        registerButton.addActionListener(this::registerButtonActionPerformed);

        emailTextField.setToolTipText("email..."); // NOI18N
        emailTextField.setName("password..."); // NOI18N
        emailTextField.addActionListener(this::emailTextFieldActionPerformed);

        emailLabel.setText("Email");

        emailLabel1.setText("Password");

        showcheck.setText("Show");
        showcheck.addActionListener(this::showcheckActionPerformed);

        closeAppButton.setBackground(new java.awt.Color(255, 51, 51));
        closeAppButton.setFont(new java.awt.Font("DialogInput", 0, 12)); // NOI18N
        closeAppButton.setText("Close Program");
        closeAppButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        closeAppButton.setBorderPainted(false);
        closeAppButton.addActionListener(this::closeAppButtonActionPerformed);

        nameLabel.setText("Name");

        javax.swing.GroupLayout logInPanelLayout = new javax.swing.GroupLayout(logInPanel);
        logInPanel.setLayout(logInPanelLayout);
        logInPanelLayout.setHorizontalGroup(
            logInPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logInPanelLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(logInPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(logInPanelLayout.createSequentialGroup()
                        .addGroup(logInPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(passwordTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(registerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(showcheck)
                            .addComponent(closeAppButton, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(emailLabel1))
                        .addGap(1, 1, 1))
                    .addGroup(logInPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(nameLabel)
                        .addGroup(logInPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(emailLabel)
                            .addComponent(emailTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                            .addComponent(nameTextField))))
                .addGap(0, 35, Short.MAX_VALUE))
        );
        logInPanelLayout.setVerticalGroup(
            logInPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, logInPanelLayout.createSequentialGroup()
                .addContainerGap(8, Short.MAX_VALUE)
                .addComponent(nameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(emailLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(emailTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(emailLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(passwordTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(showcheck)
                .addGap(31, 31, 31)
                .addComponent(registerButton)
                .addGap(57, 57, 57)
                .addComponent(closeAppButton)
                .addGap(32, 32, 32))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(310, 310, 310)
                .addComponent(logInPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(headerPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(111, 111, 111)
                .addComponent(logInPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 165, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
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

    private void registerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerButtonActionPerformed

        gui.handleRegister();
    }//GEN-LAST:event_registerButtonActionPerformed

    private void emailTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailTextFieldActionPerformed

    }//GEN-LAST:event_emailTextFieldActionPerformed

    private void showcheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showcheckActionPerformed

        if (showcheck.isSelected()) {
            passwordTextField.setEchoChar((char) 0); // show password
        } else {
            passwordTextField.setEchoChar('•'); // hide password
        }
    }//GEN-LAST:event_showcheckActionPerformed

    private void closeAppButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeAppButtonActionPerformed

        gui.closeProgram();

    }//GEN-LAST:event_closeAppButtonActionPerformed

    public void setGUI(GUICommands gui) {
        this.gui = gui;
    }  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Label WelcomeLabel1;
    private javax.swing.JButton backButton;
    private javax.swing.JButton closeAppButton;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JLabel emailLabel1;
    public javax.swing.JTextField emailTextField;
    private javax.swing.JPanel headerPanel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel logInPanel;
    private javax.swing.JLabel nameLabel;
    public javax.swing.JTextField nameTextField;
    public javax.swing.JPasswordField passwordTextField;
    private javax.swing.JButton registerButton;
    private javax.swing.JCheckBox showcheck;
    // End of variables declaration//GEN-END:variables
}
