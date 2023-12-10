import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class history extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    private JTextField idField;
    private JButton showButton;
    private JButton back;

    public history() {
        setTitle("Table Data");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(600, 450));

        table = new JTable();
        scrollPane = new JScrollPane(table);

        idField = new JTextField(10);
        showButton = new JButton("Show History");
        back = new JButton("return");
        showButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayTableDataForId();
            }
        });
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Redirect to history class
                dispose();
                MedicineDatabaseApp.main(null);
                
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("ID:"));
        inputPanel.add(idField);
        inputPanel.add(showButton);
        inputPanel.add(back);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    public void displayTableData() {
        try {
            // Connect to the Oracle database
            String url = "jdbc:oracle:thin:@localhost:1521:xe";
            String username = "system";
            String password = "1234";
            Connection connection = DriverManager.getConnection(url, username, password);

            // Query the HISTORY table
            String query = "SELECT TRANSACTIONID, ID, NAME, QUANTITY, PRICE, TOTAL, COMPNAME, TIME, PROCESS FROM HISTORY";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Create a table model with the query results
            DefaultTableModel tableModel = new DefaultTableModel();
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

    public void displayTableDataForId() {
        try {
            // Connect to the Oracle database
            String url = "jdbc:oracle:thin:@localhost:1521:xe";
            String username = "system";
            String password = "1234";
            Connection connection = DriverManager.getConnection(url, username, password);

            // Get the ID from the input field
            String id = idField.getText().trim();

            // Prepare the query with a WHERE clause for the specific ID
            String query = "SELECT TRANSACTIONID, ID, NAME, QUANTITY, PRICE, TOTAL,COMPNAME, TIME,PROCESS FROM HISTORY WHERE ID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id);

            ResultSet resultSet = statement.executeQuery();

            // Create a table model with the query results
            DefaultTableModel tableModel = new DefaultTableModel();
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            history example = new history();
            example.displayTableData();
            example.setVisible(true);
        });
    }
}
