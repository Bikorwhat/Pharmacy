import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingUtilities;

public class Update extends JFrame {

    private JPanel contentPane;
    private JTextField textFieldEnterId;
    private JTextField textFieldId;
    private JTextField textFieldName;
    private JTextField textFieldMfgDate;
    private JTextField textFieldExpDate;
    private JTextField textFieldQuantity;
    private JTextField textFieldCompName;
    private JTextField textFieldRate;

    private int retrievedId; // Variable to store the retrieved ID

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Update frame = new Update();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Update() {
    	setTitle("Update Medicine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
        setBounds(400, 130, 467, 401);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblEnterId = new JLabel("Enter ID:");
        lblEnterId.setBounds(50, 30, 80, 14);
        contentPane.add(lblEnterId);

        textFieldEnterId = new JTextField();
        textFieldEnterId.setBounds(150, 27, 150, 20);
        contentPane.add(textFieldEnterId);
        textFieldEnterId.setColumns(10);

        JButton btnRetrieve = new JButton("Retrieve");
        btnRetrieve.setBounds(310, 26, 100, 23);

        JButton back = new JButton("Return");
        back.setBounds(310, 260, 110, 23);
        contentPane.add(btnRetrieve);
        contentPane.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Redirect to history class
                dispose();
                MedicineDatabaseApp.main(null);
            }
        });
        btnRetrieve.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // Connect to the database
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system",
                            "1234");

                    // Retrieve the existing data based on the entered ID
                    String selectQuery = "SELECT * FROM medicine WHERE id = ?";
                    PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
                    selectStatement.setInt(1, Integer.parseInt(textFieldEnterId.getText()));
                    ResultSet resultSet = selectStatement.executeQuery();

                    // Populate the text fields with the existing data
                    if (resultSet.next()) {
                        retrievedId = resultSet.getInt("id"); // Store the retrieved ID
                        textFieldId.setText(resultSet.getString("id"));
                        textFieldName.setText(resultSet.getString("name"));
                        textFieldMfgDate.setText(resultSet.getString("mfgdate"));
                        textFieldExpDate.setText(resultSet.getString("expdate"));
                        textFieldQuantity.setText(resultSet.getString("quantity"));
                        textFieldCompName.setText(resultSet.getString("compname"));
                        textFieldRate.setText(resultSet.getString("price"));

                        // Enable editing in the text fields
                        
                        textFieldName.setEditable(true);
                        textFieldMfgDate.setEditable(true);
                        textFieldExpDate.setEditable(true);
                        textFieldQuantity.setEditable(true);
                        textFieldRate.setEditable(true);
                    } else {
                        textFieldId.setText("");
                        textFieldName.setText("");
                        textFieldMfgDate.setText("");
                        textFieldExpDate.setText("");
                        textFieldQuantity.setText("");
                        textFieldCompName.setText("");
                        textFieldRate.setText("");

                        // Disable editing in the text fields
                        textFieldId.setEditable(false);
                        textFieldName.setEditable(false);
                        textFieldMfgDate.setEditable(false);
                        textFieldExpDate.setEditable(false);
                        textFieldQuantity.setEditable(false);
                        textFieldRate.setEditable(false);

                        JOptionPane.showMessageDialog(null, "No record found with the provided ID.", "Record Not Found",
                                JOptionPane.INFORMATION_MESSAGE);
                    }

                    // Close the result set, prepared statement, and connection
                    resultSet.close();
                    selectStatement.close();
                    connection.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(50, 80, 80, 14);
        contentPane.add(lblId);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(50, 110, 80, 14);
        contentPane.add(lblName);

        JLabel lblMfgDate = new JLabel("Mfg Date:");
        lblMfgDate.setBounds(50, 140, 80, 14);
        contentPane.add(lblMfgDate);

        JLabel lblExpDate = new JLabel("Exp Date:");
        lblExpDate.setBounds(50, 170, 80, 14);
        contentPane.add(lblExpDate);

        JLabel lblQuantity = new JLabel("Quantity:");
        lblQuantity.setBounds(50, 200, 80, 14);
        contentPane.add(lblQuantity);

        JLabel lblCompName = new JLabel("Comp Name:");
        lblCompName.setBounds(50, 230, 80, 14);
        contentPane.add(lblCompName);

        JLabel lblRate = new JLabel("Rate:");
        lblRate.setBounds(50, 260, 80, 14);
        contentPane.add(lblRate);

        textFieldId = new JTextField();
        textFieldId.setEditable(false);
        textFieldId.setBounds(150, 77, 150, 20);
        contentPane.add(textFieldId);
        textFieldId.setColumns(10);

        textFieldName = new JTextField();
        textFieldName.setEditable(false);
        textFieldName.setBounds(150, 107, 150, 20);
        contentPane.add(textFieldName);
        textFieldName.setColumns(10);

        textFieldMfgDate = new JTextField();
        textFieldMfgDate.setEditable(false);
        textFieldMfgDate.setBounds(150, 137, 150, 20);
        contentPane.add(textFieldMfgDate);
        textFieldMfgDate.setColumns(10);

        textFieldExpDate = new JTextField();
        textFieldExpDate.setEditable(false);
        textFieldExpDate.setBounds(150, 167, 150, 20);
        contentPane.add(textFieldExpDate);
        textFieldExpDate.setColumns(10);

        textFieldQuantity = new JTextField();
        textFieldQuantity.setEditable(false);
        textFieldQuantity.setBounds(150, 197, 150, 20);
        contentPane.add(textFieldQuantity);
        textFieldQuantity.setColumns(10);

        textFieldCompName = new JTextField();
        textFieldCompName.setEditable(false);
        textFieldCompName.setBounds(150, 227, 150, 20);
        contentPane.add(textFieldCompName);
        textFieldCompName.setColumns(10);

        textFieldRate = new JTextField();
        textFieldRate.setEditable(false);
        textFieldRate.setBounds(150, 257, 150, 20);
        contentPane.add(textFieldRate);
        textFieldRate.setColumns(10);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBounds(150, 290, 100, 23);
        contentPane.add(btnUpdate);
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // Connect to the database
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system",
                            "1234");

                    // Update the record with the modified data
                    String updateQuery = "UPDATE medicine SET id = ?, name = ?, mfgdate = ?, expdate = ?, quantity = ?, compname = ?, price = ? WHERE id = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    String quantityStr = textFieldQuantity.getText();
                    String priceStr =  textFieldRate.getText();
                    if (!quantityStr.matches("\\d+")) {
                        throw new IllegalArgumentException("Quantity must be a valid integer.");
                    }

                    // Check if PRICE is a valid double
                    if (!priceStr.matches("\\d+(\\.\\d+)?")) {
                        throw new IllegalArgumentException("Price must be a valid number.");
                    }
                    String mfgDateStr = textFieldMfgDate.getText();
                    if (!mfgDateStr.matches("\\d{4}-(\\d{1,2})-(\\d{1,2})")) {
                        throw new IllegalArgumentException("Invalid MfgDate format.  Please use yyyy-MM/m-dd/d.");
                    }

                    // Validate ExpDate format
                    String expDateStr = textFieldExpDate.getText();
                    if (!expDateStr.matches("\\d{4}-(\\d{1,2})-(\\d{1,2})")) {
                        throw new IllegalArgumentException("Invalid ExpDate format. Please use yyyy-MM/m-dd/d ");
                    }
                    updateStatement.setInt(1, Integer.parseInt(textFieldId.getText()));
                    updateStatement.setString(2, textFieldName.getText());
                    updateStatement.setString(3, textFieldMfgDate.getText());
                    updateStatement.setString(4, textFieldExpDate.getText());
                    updateStatement.setString(5, textFieldQuantity.getText());
                    updateStatement.setString(6, textFieldCompName.getText());
                    updateStatement.setDouble(7, Double.parseDouble(textFieldRate.getText()));
                    updateStatement.setInt(8, retrievedId); // Use the retrieved ID for the WHERE clause

                    int rowsAffected = updateStatement.executeUpdate();

                    // Close the prepared statement and connection
                    updateStatement.close();
                    connection.close();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Record updated successfully.", "Update Successful",
                                JOptionPane.INFORMATION_MESSAGE);

                        // Clear the text fields and disable editing
                        textFieldId.setText("");
                        textFieldName.setText("");
                        textFieldMfgDate.setText("");
                        textFieldExpDate.setText("");
                        textFieldQuantity.setText("");
                        textFieldCompName.setText("");
                        textFieldRate.setText("");

                        textFieldId.setEditable(false);
                        textFieldName.setEditable(false);
                        textFieldMfgDate.setEditable(false);
                        textFieldExpDate.setEditable(false);
                        textFieldQuantity.setEditable(false);
                        textFieldRate.setEditable(false);
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to update the record.", "Update Failed",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
               
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
