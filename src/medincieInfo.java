//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.table.TableCellRenderer;
//import javax.swing.table.TableColumn;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.sql.*;
//import java.text.SimpleDateFormat;
//import java.awt.Component;
//import javax.swing.JButton;
//import javax.swing.JCheckBox;
//
//
//public class medincieInfo extends JFrame {
//    private JTable table;
//    private DefaultTableModel tableModel;
//    private JTextField searchField;
//    private JButton searchButton;
//
//    public medincieInfo() {
//        setTitle("Medicine Table");
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setSize(800, 600);
//
//        tableModel = new DefaultTableModel();
//        table = new JTable(tableModel);
//
//        JScrollPane scrollPane = new JScrollPane(table);
//        getContentPane().add(scrollPane, BorderLayout.CENTER);
//
//        searchField = new JTextField();
//        searchButton = new JButton("Search");
//
//        JPanel searchPanel = new JPanel();
//        searchPanel.add(searchField);
//        searchPanel.add(searchButton);
//
//        getContentPane().add(searchPanel, BorderLayout.NORTH);
//
//        fetchMedicineData();
//
//        // Add a button column for purchasing
//        tableModel.addColumn("PURCHASE"); // Add the "PURCHASE" column
//
//        TableColumn purchaseColumn = table.getColumnModel().getColumn(table.getColumnCount() - 1);
//        purchaseColumn.setCellRenderer(new ButtonRenderer());
//        purchaseColumn.setCellEditor(new ButtonEditor(new JCheckBox()));
//
//        searchButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                String searchTerm = searchField.getText().trim();
//                searchMedicine(searchTerm);
//            }
//        });
//
//        setVisible(true);
//    }
//
//    private void fetchMedicineData() {
//        try {
//            // Connect to the Oracle database
//            String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:XE";
//            String username = "system";
//            String password = "1234";
//            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
//
//            // Create a SQL statement to select data from the MEDICINE table
//            String sql = "SELECT * FROM MEDICINE";
//            Statement statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery(sql);
//
//            // Get the column names from the ResultSet metadata
//            ResultSetMetaData metaData = resultSet.getMetaData();
//            int columnCount = metaData.getColumnCount();
//            for (int i = 1; i <= columnCount; i++) {
//                String columnName = metaData.getColumnName(i);
//                tableModel.addColumn(columnName);
//            }
//
//            // Fetch data from the ResultSet and add it to the table model
//            while (resultSet.next()) {
//                Object[] rowData = new Object[columnCount + 1]; // +1 for the "Buy" button
//                rowData[0] = resultSet.getObject("ID");
//                rowData[1] = resultSet.getObject("NAME");
//                rowData[2] = resultSet.getObject("MFGDATE");
//                rowData[3] = resultSet.getObject("EXPDATE");
//                rowData[4] = resultSet.getObject("QUANTITY");
//                rowData[5] = resultSet.getObject("COMPNAME");
//                rowData[6] = resultSet.getObject("PRICE");
//                // Add more lines for additional columns
//                rowData[columnCount] = "Buy";
//                tableModel.addRow(rowData);
//            }
//
//            // Close the database connection
//            resultSet.close();
//            statement.close();
//            connection.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void searchMedicine(String searchTerm) {
//        // Clear the table
//        tableModel.setRowCount(0);
//
//        try {
//            // Connect to the Oracle database
//            String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:XE";
//            String username = "system";
//            String password = "1234";
//            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
//
//            // Create a SQL statement to search for medicine based on the name
//            String sql = "SELECT * FROM MEDICINE WHERE UPPER(NAME) LIKE ?";
//            PreparedStatement statement = connection.prepareStatement(sql);
//            statement.setString(1, searchTerm.toUpperCase() + "%");
//            ResultSet resultSet = statement.executeQuery();
//
//            // Fetch data from the ResultSet and add it to the table model
//            while (resultSet.next()) {
//                Object[] rowData = new Object[table.getColumnCount()]; // Assuming the same number of columns as the original data
//                rowData[0] = resultSet.getObject("ID");
//                rowData[1] = resultSet.getObject("NAME");
//                rowData[2] = resultSet.getObject("MFGDATE");
//                rowData[3] = resultSet.getObject("EXPDATE");
//                rowData[4] = resultSet.getObject("QUANTITY");
//                rowData[5] = resultSet.getObject("COMPNAME");
//                rowData[6] = resultSet.getObject("PRICE");
//                // Add more lines for additional columns
//                tableModel.addRow(rowData);
//            }
//
//            // Close the database connection and statement
//            resultSet.close();
//            statement.close();
//            connection.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // Rest of the code...
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                // Assuming the user ID is stored in the Session class as "userId"
//                String userId = Session.getUsername(); // Replace with appropriate method to retrieve user ID
//                if (userId == null) {
//                    System.out.println("User ID not set. Please set the user ID before running the Transaction.");
//                } else {
//                    new medincieInfo();
//                }
//            }
//        });
//    }
//}
