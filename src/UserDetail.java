import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UserDetail extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField addressField;
    private JTextField contactField;
    private JCheckBox showPasswordCheckbox;

    public UserDetail(JFrame parent) {
        super(parent, "User Information", true);
        setSize(600, 300);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        setLayout(null);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(20, 20, 100, 20);
        usernameField = new JTextField();
        usernameField.setEditable(false);
        usernameField.setBounds(120, 20, 200, 20);
        add(usernameLabel);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(20, 50, 100, 20);
        passwordField = new JPasswordField();
        passwordField.setBounds(120, 50, 200, 20);
        add(passwordLabel);
        add(passwordField);

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setBounds(20, 80, 100, 20);
        addressField = new JTextField();
        addressField.setBounds(120, 80, 200, 20);
        add(addressLabel);
        add(addressField);

        JLabel contactLabel = new JLabel("Contact:");
        contactLabel.setBounds(20, 110, 100, 20);
        contactField = new JTextField();
        contactField.setEditable(false);
        contactField.setBounds(120, 110, 200, 20);
        add(contactLabel);
        add(contactField);

        showPasswordCheckbox = new JCheckBox("Show Password");
        showPasswordCheckbox.setBounds(120, 140, 150, 20);
        showPasswordCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showPasswordCheckbox.isSelected()) {
                    passwordField.setEchoChar((char) 0);
                } else {
                    passwordField.setEchoChar('*');
                }
            }
        });
        add(showPasswordCheckbox);

        JButton updateButton = new JButton("Update");
        updateButton.setBounds(110, 170, 80, 20);
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String address = addressField.getText();

                try {
                    // Connect to the database
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "1234");

                    // Create a prepared statement
                    String updateQuery = "UPDATE userinfo SET password = ?, address = ? WHERE username = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);

                    // Set values for the prepared statement
                    preparedStatement.setString(1, password);
                    preparedStatement.setString(2, address);
                    preparedStatement.setString(3, username);

                    // Execute the prepared statement
                    preparedStatement.executeUpdate();

                    // Close the prepared statement and connection
                    preparedStatement.close();
                    connection.close();

                    JOptionPane.showMessageDialog(null, "User information updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        add(updateButton);

        JButton returnButton = new JButton("Back");
        returnButton.setBounds(200, 170, 80, 20);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the UserDetail window
                dispose();
            }
        });
        add(returnButton);

        loadDataFromDatabase();
    }

    private void loadDataFromDatabase() {
        try {
            // Connect to the database
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "1234");

            // Create a statement
            Statement statement = connection.createStatement();

            // Execute the query
            String query = "SELECT * FROM USERINFO WHERE USERNAME='" + Session.getUsername() + "'";
            ResultSet resultSet = statement.executeQuery(query);

            // Display the user information in the fields
            if (resultSet.next()) {
                usernameField.setText(resultSet.getString("username"));
                passwordField.setText(resultSet.getString("password"));
                contactField.setText(resultSet.getString("contact"));
                addressField.setText(resultSet.getString("address"));
            }

            // Close the result set, statement, and connection
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame mainFrame = new JFrame();
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.setTitle("Main Frame");
                mainFrame.setPreferredSize(new Dimension(800, 600));
                mainFrame.pack();
                mainFrame.setLocationRelativeTo(null);

                Dashboard dashboard = new Dashboard();
                dashboard.setVisible(true);

                mainFrame.setVisible(true);

                // Create and show the UserDetail dialog when "Info" menu is clicked
                UserDetail userDetail = new UserDetail(mainFrame);
                userDetail.setVisible(true);
            }
        });
    }
}
