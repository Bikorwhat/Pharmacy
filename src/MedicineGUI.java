import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;

public class MedicineGUI extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton;
    private JRadioButton pickupRadio;
    private JRadioButton deliveryRadio;
    private ButtonGroup deliveryMethodGroup;

    public MedicineGUI(JFrame parent) {
        setTitle("Medicine Table");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 400);
        int dashboardY = parent.getLocation().y;
        int dashboardHeight = parent.getHeight();
        int userHistoryHeight = getPreferredSize().height;
        int userHistoryY = dashboardY-110;

        // Set the calculated location
        setLocation(parent.getLocation().x, userHistoryY);

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200,30)); // Set preferred width
        searchButton = new JButton("Search");

        JPanel searchPanel = new JPanel();
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        getContentPane().add(searchPanel, BorderLayout.NORTH);
        


        fetchMedicineData();

        // Add a button column for purchasing
       

      
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchTerm = searchField.getText().trim();
                searchMedicine(searchTerm);
            }
        });

        setVisible(true);
    }

    private void fetchMedicineData() {
        try {
            // Connect to the Oracle database
            String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:XE";
            String username = "system";
            String password = "1234";
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Create a SQL statement to select data from the MEDICINE table
            String sql = "SELECT * FROM MEDICINE";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            // Get the column names from the ResultSet metadata
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                tableModel.addColumn(columnName);
            }

            // Fetch data from the ResultSet and add it to the table model
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount + 1];
              
// +1 for the "Buy" button
                rowData[0] = resultSet.getObject("ID");
                rowData[1] = resultSet.getObject("NAME");
                rowData[2] = resultSet.getObject("MFGDATE");
                rowData[3] = resultSet.getObject("EXPDATE");
                rowData[4] = resultSet.getObject("QUANTITY");
                rowData[5] = resultSet.getObject("COMPNAME");
                rowData[6] = resultSet.getObject("PRICE");
                // Add more lines for additional columns
                rowData[columnCount] = "Buy";
                tableModel.addRow(rowData);
            }

            // Close the database connection
            resultSet.close();
            statement.close();
            connection.close();
            TableColumn purchaseColumn = table.getColumnModel().getColumn(table.getColumnCount() - 1);
            purchaseColumn.setCellRenderer(new ButtonRenderer());
            purchaseColumn.setCellEditor(new ButtonEditor(new JCheckBox()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void searchMedicine(String searchTerm) {
        // Clear the table
        tableModel.setRowCount(0);

        try {
            // Connect to the Oracle database
            String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:XE";
            String username = "system";
            String password = "1234";
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Create a SQL statement to search for medicine based on the name
            String sql = "SELECT * FROM MEDICINE WHERE UPPER(NAME) LIKE ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, searchTerm.toUpperCase() + "%");
            ResultSet resultSet = statement.executeQuery();

            // Fetch data from the ResultSet and add it to the table model
            while (resultSet.next()) {
                Object[] rowData = new Object[table.getColumnCount()]; 
                rowData[0] = resultSet.getObject("ID");
                rowData[1] = resultSet.getObject("NAME");
                rowData[2] = resultSet.getObject("MFGDATE");
                rowData[3] = resultSet.getObject("EXPDATE");
                rowData[4] = resultSet.getObject("QUANTITY");
                rowData[5] = resultSet.getObject("COMPNAME");
                rowData[6] = resultSet.getObject("PRICE");
               
                tableModel.addRow(rowData);
            }

            // Close the database connection and statement
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Custom renderer for the "Buy" button column
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setText("Buy");
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Custom editor for the "Buy" button column
    class ButtonEditor extends DefaultCellEditor {
        private String label;
        private JButton button;
        private boolean clicked;
        private int row;
        
        
        

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setText("Buy");
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
            button.setText("Buy");
            clicked = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (clicked) {
                // Perform the purchase operation for the selected row
                purchaseMedicine(row);
                return label;
            }
            clicked = false;
            return new String(label);
        }

        public boolean stopCellEditing() {
            clicked = false;
            fireEditingStopped();
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    // Method to handle the purchase operation for the selected row
    private void purchaseMedicine(int row) {
        // Retrieve the selected row's data
        Object[] rowData = new Object[table.getColumnCount() - 1];
        for (int i = 0; i < rowData.length; i++) {
            rowData[i] = table.getValueAt(row, i);
        }
        int medicineId = Integer.parseInt(rowData[0].toString()); // Assuming the medicine ID is in the first column
        String medicineName = rowData[1].toString(); // Assuming the medicine name is in the second column
        int availableQuantity = Integer.parseInt(rowData[4].toString()); // Assuming the quantity is in the fifth column
        double price = Double.parseDouble(rowData[6].toString()); // Assuming the price is in the seventh column
        String compNam=rowData[5].toString();  
        // Prompt the user for the quantity to buy
        String quantityInput = JOptionPane.showInputDialog(this, "Enter the quantity to buy for " + medicineName + ":");
        if (quantityInput == null) {
            return; // User canceled the input
        }

        try {
            int quantityToBuy = Integer.parseInt(quantityInput);

            // Check if the quantity to buy is greater than available quantity
            if (quantityToBuy > availableQuantity) {
                // Show error message for insufficient quantity

                String message = "Insufficient quantity! Available quantity for " + medicineName +
                        " is " + availableQuantity + ". Please enter a lower quantity "+"\n"+"or try other company product,for that you can search on search bar "
                        		+ "typing medicine name.";
                String recommendedMedicines = getRecommendedMedicines(medicineName, quantityToBuy);
                if (!recommendedMedicines.isEmpty()) {
                    message += "\n\nRecommended medicine "+ medicineName+" from other companies:\n" + recommendedMedicines;
                }

                JOptionPane.showMessageDialog(this, message);
                return;
            }
            String[] deliveryOptions = { "Pickup", "Delivery" };
            int deliveryChoice = JOptionPane.showOptionDialog(this,
                    "Select the delivery method:", "Delivery Method",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                    deliveryOptions, deliveryOptions[0]);

            if (deliveryChoice == JOptionPane.CLOSED_OPTION) {
                return; // User canceled the delivery method selection
            }

            String selectedMethod = deliveryOptions[deliveryChoice];
            // Calculate the total amount
            double totalAmount = price * quantityToBuy;

            // Display a confirmation dialog with the purchased details
            String message = "Medicine Name: " + medicineName + "\n";
            message += "Quantity: " + quantityToBuy + "\n";
            message += "Total Price: " + totalAmount + "\n";
            message += "Medicine bought successfully!";
            JOptionPane.showMessageDialog(this, message);

            // Update the quantity in the database
            updateMedicineQuantity(medicineId, quantityToBuy);
            String userId = Session.getUsername(); // Replace with appropriate method to retrieve user ID
            insertPurchaseHistory(userId, medicineName, quantityToBuy, price, totalAmount,compNam,selectedMethod);
            dispose();
          

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity entered!");
        }
    }
    
    private String getRecommendedMedicines(String medicineName, int quantityToBuy) {
        StringBuilder recommendations = new StringBuilder();
        try {
            // Connect to the Oracle database
            String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:XE";
            String username = "system";
            String password = "1234";
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Create a SQL statement to search for other medicines with the same name and higher quantity
            String sql = "SELECT * FROM MEDICINE WHERE UPPER(NAME) = ? AND QUANTITY >= ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, medicineName.toUpperCase());
            statement.setInt(2, quantityToBuy);
            ResultSet resultSet = statement.executeQuery();

            // Fetch data from the ResultSet and add them as recommendations
            while (resultSet.next()) {
                String compName = resultSet.getString("COMPNAME");
                int availableQuantity = resultSet.getInt("QUANTITY");
                recommendations.append("Company Name: ").append(compName).append(", Available Quantity: ").append(availableQuantity).append("\n");
            }

            // Close the database connection and statement
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recommendations.toString();
    }
    private void insertPurchaseHistory(String userId, String medicineName, int quantity, double price, double total,String compNam, String deliveryMethod) {
        try {
            // Connect to the Oracle database
            String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:XE";
            String username = "system";
            String password = "1234";
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Get the current time
            String currentTime = getCurrentTime();

            // Create a SQL statement to insert data into the HISTORY table
            String sql = "INSERT INTO HISTORY (ID, NAME, QUANTITY, PRICE, TOTAL, COMPNAME, TIME, METHOD) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, userId);
            statement.setString(2, medicineName);
            statement.setInt(3, quantity);
            statement.setDouble(4, price);
            statement.setDouble(5, total);
            statement.setString(6,compNam);
            statement.setString(7, currentTime);
            statement.setString(8, deliveryMethod); 

            // Execute the insert statement
            int rowsInserted = statement.executeUpdate();

            // Check if the insert was successful
            if (rowsInserted > 0) {
            	
                System.out.println("Purchase history inserted successfully.");
                
            } else {
                System.out.println("Failed to insert purchase history.");
            }

            // Close the database connection and statement
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getCurrentTime() {
        // Get the current timestamp
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        // Format the timestamp as a string
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(timestamp);
    }

    private void updateMedicineQuantity(int medicineId, int quantityToDeduct) {
        try {
            // Connect to the Oracle database
            String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:XE";
            String username = "system";
            String password = "1234";
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Create a SQL statement to update the quantity in the MEDICINE table
            String sql = "UPDATE MEDICINE SET QUANTITY = QUANTITY - ? WHERE ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, quantityToDeduct);
            statement.setInt(2, medicineId);

             // Execute the update statement
            int rowsUpdated = statement.executeUpdate();

            // Check if the update was successful
            if (rowsUpdated > 0) {
                System.out.println("Medicine quantity updated successfully.");
            } else {
                System.out.println("Failed to update medicine quantity.");
            }

            // Close the database connection and statement
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Assuming the user ID is stored in the Session class as "userId"
                String userId = Session.getUsername(); // Replace with appropriate method to retrieve user ID
                if (userId == null) {
                    System.out.println("User ID not set. Please set the user ID before running the Transaction.");
                } else {
                    new MedicineGUI(null);
                }
            }
        });
    }
}
