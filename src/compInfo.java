import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class compInfo extends JFrame {

    public compInfo() {
        setTitle("Company Detail");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 300); // Set the size of the JFrame

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        String url = "jdbc:oracle:thin:@localhost:1521:xe"; // Replace with your Oracle DB URL and SID
        String username = "system";
        String password = "1234";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COMPNAME, COUNT(*) AS TOTAL_BUYERS FROM HISTORY GROUP BY COMPNAME ORDER BY TOTAL_BUYERS DESC")) {

            while (rs.next()) {
                String companyName = rs.getString("COMPNAME");
                int totalBuyers = rs.getInt("TOTAL_BUYERS");
                mainPanel.add(createCard(companyName, totalBuyers));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        getContentPane().add(scrollPane);
        setLocationRelativeTo(null); // Center the frame on the screen
    }

    private JPanel createCard(String companyName, int totalBuyers) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout());

        JLabel companyNameLabel = new JLabel("Company Name: " + companyName);
        JLabel totalBuyersLabel = new JLabel("Total Transactions: " + totalBuyers);

        cardPanel.add(companyNameLabel, BorderLayout.NORTH);
        cardPanel.add(totalBuyersLabel, BorderLayout.CENTER);

        // Add some padding around each card
        cardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        return cardPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            compInfo gui = new compInfo();
            gui.setVisible(true);
        });
    }
}
