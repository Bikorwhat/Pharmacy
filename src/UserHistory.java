import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class UserHistory extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    public UserHistory(JFrame parent) {
        setTitle("History");
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(900, 450));
        int dashboardY = parent.getLocation().y;
        int dashboardHeight = parent.getHeight();
        int userHistoryHeight = getPreferredSize().height;
        int userHistoryY = dashboardY-100;

        // Set the calculated location
        setLocation(parent.getLocation().x-100, userHistoryY);
        table = new JTable();
        scrollPane = new JScrollPane(table);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        pack();
    }

    public void displayTableDataForId() {
        try {
            // Connect to the Oracle database
            String url = "jdbc:oracle:thin:@localhost:1521:xe";
            String username = "system";
            String password = "1234";
            Connection connection = DriverManager.getConnection(url, username, password);

            // Get the ID from the session
            String userId = Session.getUsername();

            // Prepare the query with a WHERE clause for the specific ID
            String query = "SELECT TRANSACTIONID, ID, NAME, QUANTITY, PRICE, TOTAL, COMPNAME, TIME,PROCESS, METHOD FROM HISTORY WHERE ID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userId);

            ResultSet resultSet = statement.executeQuery();

            // Create a table model with the query results
            tableModel = new DefaultTableModel();
            table.setModel(tableModel);

            // Add column headers to the table model
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                tableModel.addColumn(metaData.getColumnLabel(i));
            }

            // Add rows to the table model
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                tableModel.addRow(rowData);
            }

            // Close the database connection
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
