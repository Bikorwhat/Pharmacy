import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Notification extends JFrame {
    private JTextArea notificationArea;

    public Notification() {
        super("Notifications");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 300);
        setLocation(300,122);
        setLayout(new BorderLayout());

        notificationArea = new JTextArea();
        notificationArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(notificationArea);
        add(scrollPane, BorderLayout.CENTER);

        // Create a JPanel for the bottom area containing the close button
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
             MedicineDatabaseApp.main(null); // Open the dashboard when the "test" GUI is closed
            }
        });

        // Create the close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	dispose();
                MedicineDatabaseApp.main(null);
            }
        });

//        bottomPanel.add(closeButton, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        checkMedicineQuantityAndExpiry();

        setVisible(true);
    }

    private void checkMedicineQuantityAndExpiry() {
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String username = "system";
        String password = "1234";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            String query = "SELECT NAME, COMPNAME, QUANTITY, EXPDATE FROM MEDICINE WHERE QUANTITY < 100";
            ResultSet resultSet = statement.executeQuery(query);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date currentDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DAY_OF_YEAR, 7); // Add 7 days to current date

            while (resultSet.next()) {
                String medicineName = resultSet.getString("NAME");
                int quantity = resultSet.getInt("QUANTITY");
                String comp = resultSet.getString("COMPNAME");
                Date expiryDate = resultSet.getDate("EXPDATE");
                if (expiryDate != null && expiryDate.before(currentDate)) {
                    String notification = "*"+ getCurrentTime()+" Expired: Medicine named: " + medicineName + "\n"+"         of Company named: "
                            + comp + " has already expired (" +
                            sdf.format(expiryDate) + "). ";
                    notificationArea.append(notification + "\n"+ "\n");
                }  else if (expiryDate.equals(calendar.getTime()) || expiryDate.before(calendar.getTime())) {
                    String notification = "*"+ getCurrentTime()+" Expiring Soon: Medicine named: " + medicineName +  "\n"+"           of Company named: "
                            + comp + "'s expiry date is getting near (" +
                            sdf.format(expiryDate) + "). ";
                    notificationArea.append(notification + "\n"+ "\n");
                }

                if (quantity < 100) {
                    String notification1 = "*"+
                            getCurrentTime()+" Low Quantity: Medicine named: " + medicineName + "\n"+ "           of Company named: "
                            + comp + "'s total quantity is less than 100. ";
                    notificationArea.append(notification1 + "\n"+ "\n");
                }

            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the database.");
        }
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Notification());
    }
}
