import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class test extends JFrame {

    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;
    private JTextField textField_3;
    private JTextField textField_4;
    private JTextField textField_5;
    private JTextField textField_6;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    test frame = new test();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public test() {
    	setTitle("Add Medicine");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(300, 130, 667, 401);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
             MedicineDatabaseApp.main(null); // Open the dashboard when the "test" GUI is closed
            }
        });
        JLabel lblNewLabel = new JLabel("id");
        lblNewLabel.setBounds(35, 73, 46, 14);
        contentPane.add(lblNewLabel);

        JLabel lblName = new JLabel("name");
        lblName.setBounds(35, 110, 46, 14);
        contentPane.add(lblName);

        JLabel lblQuantity = new JLabel("quantity");
        lblQuantity.setBounds(35, 145, 46, 14);
        contentPane.add(lblQuantity);

        JLabel lblMfgDate = new JLabel("mfg date");
        lblMfgDate.setBounds(35, 178, 46, 14);
        contentPane.add(lblMfgDate);

        JLabel lblExpDate = new JLabel("exp date");
        lblExpDate.setBounds(35, 209, 46, 14);
        contentPane.add(lblExpDate);
        JLabel lblCompanyName = new JLabel("companyname");
        lblCompanyName.setBounds(325, 73, 86, 14);
        contentPane.add(lblCompanyName);
        JLabel lblRate = new JLabel("price");
        lblRate.setBounds(325, 110, 86, 14);
        contentPane.add(lblRate);

       

        textField = new JTextField();
        textField.setBounds(125, 70, 132, 20);
        textField.setEditable(false); // Set the field as uneditable
        contentPane.add(textField);
        textField.setColumns(10);

        textField_1 = new JTextField();
        textField_1.setBounds(125, 107, 132, 20);
        textField_1.setColumns(10);
        contentPane.add(textField_1);

        textField_2 = new JTextField();
        textField_2.setBounds(125, 142, 132, 20);
        textField_2.setColumns(10);
        contentPane.add(textField_2);

        textField_3 = new JTextField("yyyy-MM-dd"); // MFG Date
        textField_3.setBounds(125, 175, 132, 20);
        contentPane.add(textField_3);

        // Add focus listener to MFG Date text field
        textField_3.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // When the text field gains focus, clear the placeholder text
                if (textField_3.getText().equals("yyyy-MM-dd")) {
                    textField_3.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                // When the text field loses focus, restore the placeholder text if it's empty
                if (textField_3.getText().isEmpty()) {
                    textField_3.setText("yyyy-MM-dd");
                }
            }
        });

        textField_4 = new JTextField("yyyy-MM-dd"); // EXP Date
        textField_4.setBounds(125, 206, 132, 20);
        contentPane.add(textField_4);

        // Add focus listener to EXP Date text field
        textField_4.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // When the text field gains focus, clear the placeholder text
                if (textField_4.getText().equals("yyyy-MM-dd")) {
                    textField_4.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                // When the text field loses focus, restore the placeholder text if it's empty
                if (textField_4.getText().isEmpty()) {
                    textField_4.setText("yy-MM-dd");
                }
            }
        });
        
        textField_5 = new JTextField();
        textField_5.setColumns(10);
        textField_5.setBounds(418, 70, 132, 20);
        contentPane.add(textField_5);
        textField_6 = new JTextField();
        textField_6.setColumns(10);
        textField_6.setBounds(418, 110, 132, 20);
        contentPane.add(textField_6);

        JButton btnNewButton = new JButton("add");
        btnNewButton.setBounds(310, 200, 80, 23);
        contentPane.add(btnNewButton);
        JButton back = new JButton("Return");
        back.setBounds(448, 268, 89, 23);
        contentPane.add(back);
        
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Redirect to history class
                dispose();
                MedicineDatabaseApp.main(null);
                
            }  });
       
        
        // Add ActionListener to the "add" button
     // ...

     // Add ActionListener to the "add" button
     btnNewButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
             try {
                 // Connect to the database
                 Class.forName("oracle.jdbc.driver.OracleDriver");
                 Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "1234");

                 // Create a prepared statement
                 String insertQuery = "INSERT INTO MEDICINE (NAME, MFGDATE, EXPDATE, QUANTITY, COMPNAME, PRICE) "
                	        + "VALUES (?, ?, ?, ?, ?, ?)";
                 PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

                 // Set values for the prepared statement
//                 preparedStatement.setString(1, textField.getText());
                 preparedStatement.setString(1, textField_1.getText());
                 DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                 DateTimeFormatter dateFormatter = new DateTimeFormatterBuilder()
                         .parseCaseInsensitive()
                         .appendPattern("[yyyy-M-d][yyyy-MM-dd]")
                         .toFormatter();
                 LocalDate mfgDate = LocalDate.parse(textField_3.getText(), dateFormatter);

                 LocalDate expDate = LocalDate.parse(textField_4.getText(), dateFormatter);
                 if (expDate.isBefore(mfgDate)) {
                     throw new IllegalArgumentException("Expiration date cannot be less than manufacturing date.");
                 }
                 // Validate QUANTITY and PRICE fields
                 String quantityStr = textField_2.getText();
                 String priceStr = textField_6.getText();
                 String compname = textField_5.getText();
                 String medicineName = textField_1.getText();
                 System.out.println("MFG Date: " + textField_3.getText());
                 System.out.println("EXP Date: " + textField_4.getText());
                 if ( medicineName.isEmpty()) {
                     throw new IllegalArgumentException(" medicine name cannot be empty.");
                 }
                 if (compname.isEmpty()) {
                     throw new IllegalArgumentException("Company name cannot be empty.");
                 }
                 // Check if QUANTITY is a valid integer
                 if (!quantityStr.matches("\\d+")) {
                     throw new IllegalArgumentException("Quantity must be a valid integer.");
                 }

                 // Check if PRICE is a valid double
                 if (!priceStr.matches("\\d+(\\.\\d+)?")) {
                     throw new IllegalArgumentException("Price must be a valid number.");
                 }

                 int quantity = Integer.parseInt(quantityStr);
                 double price = Double.parseDouble(priceStr);

                 preparedStatement.setDate(4, java.sql.Date.valueOf(expDate));
                 DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                 String formattedMfgDate = mfgDate.format(outputFormatter);
                 String formattedExpDate = expDate.format(outputFormatter);

                 preparedStatement.setString(2, formattedMfgDate);
                 preparedStatement.setString(3, formattedExpDate);

                 preparedStatement.setInt(4, quantity);
                 preparedStatement.setString(5, textField_5.getText());
                 preparedStatement.setDouble(6, price);

                 // Execute the prepared statement
                 preparedStatement.executeUpdate();

                 // Close the prepared statement and connection
                 preparedStatement.close();
                 connection.close();

                 // Show success message
                 JOptionPane.showMessageDialog(null, "Data added successfully!");
                 dispose();
                 MedicineDatabaseApp.main(null);

                 // Clear the text fields
//                 textField.setText("");
                 textField_1.setText("");
                 textField_2.setText("");
                 textField_3.setText("");
                 textField_4.setText("");
                 textField_5.setText("");
                 textField_6.setText("");
             } catch (NumberFormatException ex) {
                 JOptionPane.showMessageDialog(null, "Error: " + "Please enter valid numeric values for Quantity and Price.");
             } catch (IllegalArgumentException ex) {
                 // Display an error message for date format or invalid numeric values
                 JOptionPane.showMessageDialog(null, "enter valid data."+ex.getMessage());
             } catch (Exception ex) {
                 ex.printStackTrace();
                 JOptionPane.showMessageDialog(null, "Error:enter valid data format." + ex.getMessage());
             }
         }
     });

     // ...

    }
}
