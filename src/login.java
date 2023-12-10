import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class login extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public login() {
        setTitle("Login");
        setSize(380, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(30, 30, 80, 25);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(120, 30, 150, 25);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(30, 70, 80, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(120, 70, 150, 25);
        add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(15, 110, 80, 25);
        JButton signupButton = new JButton("Signup");
        signupButton.setBounds(215, 110, 80, 25);
     
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (username.equals("root") && password.equals("1234")) {
                    Session.setUsername(username);
                    MedicineDatabaseApp medicineDatabaseApp = new MedicineDatabaseApp();
                    dispose();
                        SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            medicineDatabaseApp.setVisible(true);
                        }
                    });
                } else {
                    // Connect to the database
                    try {
                        Class.forName("oracle.jdbc.driver.OracleDriver");
                        Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "1234");

                        // Create a prepared statement for user
                        String query = "SELECT * FROM USERINFO WHERE USERNAME = ? AND PASSWORD = ?";
                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, username);
                        preparedStatement.setString(2, password);
                        ResultSet resultSet = preparedStatement.executeQuery();

                        // Check if the credentials are correct
                        if (resultSet.next()) {
                            Session.setUsername(username);
                            Dashboard userDetail = new Dashboard();
                            dispose();
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    userDetail.setVisible(true);
                                }
                            });
                        } else {
                            // Authentication failed
                            JOptionPane.showMessageDialog(null, "Wrong authentication credentials. Please try again.",
                                    "Authentication Failed", JOptionPane.ERROR_MESSAGE);
                        }

                        // Close the result set, prepared statement, and connection
                        resultSet.close();
                        preparedStatement.close();
                        connection.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the signup registration window
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JFrame signupFrame = new JFrame("Signup");
                        signupFrame.setSize(400, 300);
                        signupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        signupFrame.setLocationRelativeTo(null);
                        signupFrame.setLayout(null);

                        JLabel usernameLabel = new JLabel("Username:");
                        usernameLabel.setBounds(30, 30, 80, 25);
                        signupFrame.add(usernameLabel);

                        JTextField signupUsernameField = new JTextField();
                        signupUsernameField.setBounds(120, 30, 250, 25);
                        signupFrame.add(signupUsernameField);

                        JLabel passwordLabel = new JLabel("Password:");
                        passwordLabel.setBounds(30, 70, 80, 25);
                        signupFrame.add(passwordLabel);

                        JPasswordField signupPasswordField = new JPasswordField();
                        signupPasswordField.setBounds(120, 70, 250, 25);
                        signupFrame.add(signupPasswordField);

                        JLabel contactLabel = new JLabel("Contact:");
                        contactLabel.setBounds(30, 110, 80, 25);
                        signupFrame.add(contactLabel);

                        JTextField contactField = new JTextField();
                        contactField.setBounds(120, 110, 250, 25);
                        signupFrame.add(contactField);

                        JLabel addressLabel = new JLabel("Address:");
                        addressLabel.setBounds(30, 150, 80, 25);
                        signupFrame.add(addressLabel);

                        JTextField addressField = new JTextField();
                        addressField.setBounds(120, 150, 250, 25);
                        signupFrame.add(addressField);

                        JButton signupSubmitButton = new JButton("Submit");
                        signupSubmitButton.setBounds(150, 190, 100, 25);
                        
                     // Modify the signupSubmitButton ActionListener
                        signupSubmitButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String signupUsername = signupUsernameField.getText();
                                String signupPassword = new String(signupPasswordField.getPassword());
                                String signupContact = contactField.getText();
                                String signupAddress = addressField.getText();

                                // Check if the contact is numeric
                                if (!isNumeric(signupContact)) {
                                    JOptionPane.showMessageDialog(null, "Contact must be numeric.", "Invalid Contact", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }

                                // Check if the contact length is greater than 10 characters
                                if (signupContact.length() != 10) {
                                    JOptionPane.showMessageDialog(null, "Contact must have exactly 10 numeric characters.", "Invalid Contact", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }


                                if (signupUsername.isEmpty() || signupPassword.isEmpty() || signupContact.isEmpty() || signupAddress.isEmpty()) {
                                    JOptionPane.showMessageDialog(null, "Please fill in all the fields.", "Incomplete Fields", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }

                                // Perform signup registration
                                try {
                                    Class.forName("oracle.jdbc.driver.OracleDriver");
                                    Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "1234");

                                    String usernameQuery = "SELECT * FROM USERINFO WHERE USERNAME = ?";
                                    PreparedStatement usernameStatement = connection.prepareStatement(usernameQuery);
                                    usernameStatement.setString(1, signupUsername);
                                    ResultSet usernameResult = usernameStatement.executeQuery();

                                    if (usernameResult.next()) {
                                        // The username already exists in the database
                                        JOptionPane.showMessageDialog(null, "Username already exists. Please choose a different username.", "Username Exists", JOptionPane.ERROR_MESSAGE);
                                    } else {
                                        // Check if the contact already exists in the database
                                        String contactQuery = "SELECT * FROM USERINFO WHERE CONTACT = ?";
                                        PreparedStatement contactStatement = connection.prepareStatement(contactQuery);
                                        contactStatement.setString(1, signupContact);
                                        ResultSet contactResult = contactStatement.executeQuery();

                                        if (contactResult.next()) {
                                            // The contact already exists in the database
                                            JOptionPane.showMessageDialog(null, "Contact already exists. Please choose a different contact.", "Contact Exists", JOptionPane.ERROR_MESSAGE);
                                        } else {
                                            // The username and contact are unique, proceed with signup
                                            String signupQuery = "INSERT INTO USERINFO (USERNAME, PASSWORD, CONTACT, ADDRESS) VALUES (?, ?, ?, ?)";
                                            PreparedStatement signupStatement = connection.prepareStatement(signupQuery);
                                            signupStatement.setString(1, signupUsername);
                                            signupStatement.setString(2, signupPassword);
                                            signupStatement.setString(3, signupContact);
                                            signupStatement.setString(4, signupAddress);

                                            int rowsAffected = signupStatement.executeUpdate();

                                            if (rowsAffected > 0) {
                                                JOptionPane.showMessageDialog(null, "Signup successful. Please login with your new account.", "Signup Successful", JOptionPane.INFORMATION_MESSAGE);
                                            } else {
                                                JOptionPane.showMessageDialog(null, "Signup failed. Please try again.", "Signup Failed", JOptionPane.ERROR_MESSAGE);
                                            }

                                            signupStatement.close();
                                        }

                                        contactResult.close();
                                        contactStatement.close();
                                    }

                                    usernameResult.close();
                                    usernameStatement.close();
                                    connection.close();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                                // Close the signup window
                                signupFrame.dispose();
                            }

							private boolean isNumeric(String str) {
    try {
        Integer.parseInt(str);
        return true;
    } catch (NumberFormatException e) {
        return false;
    }
}
                        });

                        // Add the signupSubmitButton to the signupFrame
                        signupFrame.add(signupSubmitButton);

                        // Display the signupFrame
                        signupFrame.setVisible(true);
                    }
                });
            }
        });
        JButton resetButton = new JButton("ResetPassword");
        resetButton.setBounds(105, 110, 100, 25);
        add(resetButton);
        add(loginButton);
        add(signupButton);
        add(resetButton);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the password reset form
                JFrame resetFrame = new JFrame("Reset Password");
                resetFrame.setSize(400, 300);
                resetFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                resetFrame.setLocationRelativeTo(null);
                resetFrame.setLayout(null);

                JLabel usernameLabel = new JLabel("Username:");
                usernameLabel.setBounds(30, 30, 80, 25);
                resetFrame.add(usernameLabel);

                JTextField resetUsernameField = new JTextField();
                resetUsernameField.setBounds(120, 30, 250, 25);
                resetFrame.add(resetUsernameField);

                JLabel contactLabel = new JLabel("Contact:");
                contactLabel.setBounds(30, 70, 80, 25);
                resetFrame.add(contactLabel);

                JTextField resetContactField = new JTextField();
                resetContactField.setBounds(120, 70, 250, 25);
                resetFrame.add(resetContactField);

                JLabel addressLabel = new JLabel("Address:");
                addressLabel.setBounds(30, 110, 80, 25);
                resetFrame.add(addressLabel);

                JTextField resetAddressField = new JTextField();
                resetAddressField.setBounds(120, 110, 250, 25);
                resetFrame.add(resetAddressField);

                JButton resetSubmitButton = new JButton("Submit");
                resetSubmitButton.setBounds(150, 150, 100, 25);

                resetSubmitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String resetUsername = resetUsernameField.getText();
                        String resetContact = resetContactField.getText();
                        String resetAddress = resetAddressField.getText();

                        // Check if the entered data matches the data in the database
                        try {
                            Class.forName("oracle.jdbc.driver.OracleDriver");
                            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "1234");

                            String query = "SELECT * FROM USERINFO WHERE USERNAME = ? AND CONTACT = ? AND ADDRESS = ?";
                            PreparedStatement preparedStatement = connection.prepareStatement(query);
                            preparedStatement.setString(1, resetUsername);
                            preparedStatement.setString(2, resetContact);
                            preparedStatement.setString(3, resetAddress);

                            ResultSet resultSet = preparedStatement.executeQuery();

                            if (resultSet.next()) {
                                // The entered data matches, proceed with password reset
                                String newPassword = JOptionPane.showInputDialog("Enter new password:");
                                String confirmNewPassword = JOptionPane.showInputDialog("Confirm new password:");

                                if (newPassword.equals(confirmNewPassword)) {
                                    // Update the password in the database
                                    String updateQuery = "UPDATE USERINFO SET PASSWORD = ? WHERE USERNAME = ?";
                                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                                    updateStatement.setString(1, newPassword);
                                    updateStatement.setString(2, resetUsername);
                                    int rowsAffected = updateStatement.executeUpdate();

                                    if (rowsAffected > 0) {
                                        JOptionPane.showMessageDialog(null, "Password reset successful. Please login with your new password.",
                                                "Password Reset Successful", JOptionPane.INFORMATION_MESSAGE);
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Password reset failed. Please try again.",
                                                "Password Reset Failed", JOptionPane.ERROR_MESSAGE);
                                    }

                                    updateStatement.close();
                                } else {
                                    JOptionPane.showMessageDialog(null, "Passwords do not match. Please try again.",
                                            "Password Mismatch", JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Entered data does not match. Please try again.",
                                        "Data Mismatch", JOptionPane.ERROR_MESSAGE);
                            }

                            resultSet.close();
                            preparedStatement.close();
                            connection.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        resetFrame.dispose();
                    }
                });

                resetFrame.add(resetSubmitButton);
                resetFrame.setVisible(true);
            }
        });

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new login().setVisible(true);
            }
        });
    }
}
