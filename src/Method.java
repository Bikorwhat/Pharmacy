import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Method extends JFrame {
	
    private JTextField transactionIdField;
    private JComboBox<String> optionComboBox;
    private JButton updateButton;

    private Connection connection;
    private PreparedStatement preparedStatement;

    private String userId; // Current logged-in user

    public Method(JFrame parent, String loggedInUserId) {
        super("Method Update"); // Call the parent class's constructor
        this.userId = loggedInUserId;
        initializeGUI();
        setupDatabaseConnection();
    }


    private void initializeGUI() {
        transactionIdField = new JTextField(10);
        optionComboBox = new JComboBox<>(new String[]{"Pickup", "Delivery"});
        updateButton = new JButton("Update");

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMethod();
            }
        });

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.add(new JLabel("Transaction ID:"));
        panel.add(transactionIdField);
        panel.add(new JLabel("Option:"));
        panel.add(optionComboBox);
        panel.add(updateButton);

        setTitle("Method Update");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupDatabaseConnection() {
        try {
            String url = "jdbc:oracle:thin:@localhost:1521:xe";
            String username = "system";
            String password = "1234";
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to the database.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void updateMethod() {
        String transactionId = transactionIdField.getText();
        String selectedOption = (String) optionComboBox.getSelectedItem();

        try {
            if (selectedOption.equals("Delivery")) {
                String address = JOptionPane.showInputDialog("Enter delivery address:");
                String contactStr = JOptionPane.showInputDialog("Enter contact number:");

                // Parse the contact number to an integer
                int contact;
                try {
                    contact = Integer.parseInt(contactStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Please enter a valid numeric contact number.",
                            "Invalid Input",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return; // Exit the method if the input is not a valid number
                }

                // Validate the contact number
                if (contact <= 0 || contactStr.length() != 10) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Please enter a valid 10-digit positive contact number.",
                            "Invalid Input",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return; // Exit the method if the input is not a valid contact number
                }

                int confirm = JOptionPane.showConfirmDialog(
                        null, 
                        "Extra delivery charges apply on arrival. Do you accept?", 
                        "Delivery Charges Confirmation", 
                        JOptionPane.YES_NO_OPTION
                    );

                    if (confirm != JOptionPane.YES_OPTION) {
                        return; // User declined, do not proceed with update
                    }

                // Insert delivery data into the DELIVERY table
                String insertSql = "INSERT INTO DELIVERY (TRANSACTIONID, ADDRESS, CONTACT, USERNAME) VALUES (?, ?, ?, ?)";
                preparedStatement = connection.prepareStatement(insertSql);
                preparedStatement.setString(1, transactionId);
                preparedStatement.setString(2, address);
                preparedStatement.setInt(3, contact);
                preparedStatement.setString(4, userId);
                preparedStatement.executeUpdate();
            }

            // Update the method in the HISTORY table
            String updateSql = "UPDATE HISTORY SET METHOD = ? WHERE TRANSACTIONID = ?";
            preparedStatement = connection.prepareStatement(updateSql);
            preparedStatement.setString(1, selectedOption);
            preparedStatement.setString(2, transactionId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Method updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                transactionIdField.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update method.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Dashboard dashboard = new Dashboard();
            String loggedInUserId = Session.getUsername();
            new Method(dashboard, loggedInUserId); // Pass the Dashboard instance here
        });
    }
}


