import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Delete extends JFrame {

    private JPanel contentPane;
    private JTextField textField;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Delete frame = new Delete();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Delete() {
    	setTitle("Remove Medicine");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(470, 210, 450, 200);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Enter ID:");
        lblNewLabel.setBounds(50, 50, 100, 14);
        contentPane.add(lblNewLabel);

        textField = new JTextField();
        textField.setBounds(150, 47, 150, 20);
        contentPane.add(textField);
        textField.setColumns(10);

        JButton btnDelete = new JButton("Delete");
        btnDelete.setBounds(150, 90, 100, 23);
        contentPane.add(btnDelete);
        JButton back = new JButton("Return");
        back.setBounds(310, 90, 80, 23);
        contentPane.add(back);
        
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Redirect to history class
                dispose();
                MedicineDatabaseApp.main(null);
                
            }  });

        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // Connect to the database
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system",
                            "1234");

                    // Prepare the delete statement
                    String deleteQuery = "DELETE FROM medicine WHERE id = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);

                    // Set the parameter value for the ID
                    int id = Integer.parseInt(textField.getText());
                    preparedStatement.setInt(1, id);

                    // Execute the delete statement
                    int affectedRows = preparedStatement.executeUpdate();

                    // Close the prepared statement and connection
                    preparedStatement.close();
                    connection.close();

                    if (affectedRows > 0) {
                        JOptionPane.showMessageDialog(null, "Row deleted successfully!");
                    } else {
                        JOptionPane.showMessageDialog(null, "No rows were deleted.");
                    }
                    dispose();
                    MedicineDatabaseApp.main(null);
                    // Clear the text field
                    textField.setText("");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
            }
        });
    }
}
