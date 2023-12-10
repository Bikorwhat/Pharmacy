import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProcessUpdateGUI extends JFrame {
    private JTextField idField;
    private JComboBox<String> processComboBox; // Use JComboBox for process selection
    private JButton updateButton, returnButton;

    public ProcessUpdateGUI() {
        setTitle("Update Process");
        setSize(300, 160);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        idField = new JTextField(10);
        processComboBox = new JComboBox<>(new String[]{"completed", "deleted"}); // Add options to the JComboBox
        updateButton = new JButton("Update");
        returnButton = new JButton("Return");

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProcess();
            }
        });

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new MedicineDatabaseApp().setVisible(true);
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));
        panel.add(new JLabel("ID:"));
        panel.add(idField);
        panel.add(new JLabel("Process (completed/deleted):"));
        panel.add(processComboBox);
        panel.add(updateButton);
        panel.add(returnButton);

        add(panel);
    }

    private void updateProcess() {
        String id = idField.getText();
        String process = processComboBox.getSelectedItem().toString(); // Get the selected item

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "1234");
            String updateQuery = "UPDATE HISTORY SET PROCESS = ? WHERE TRANSACTIONID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, process);
            preparedStatement.setString(2, id);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Process updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No records updated.");
            }

            preparedStatement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ProcessUpdateGUI().setVisible(true);
            }
        });
    }
}
