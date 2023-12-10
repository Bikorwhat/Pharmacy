import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.text.ParseException;
import java.util.Arrays;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MedicineDatabaseApp extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;

    public MedicineDatabaseApp() {
        setTitle("Data Display");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	 int confirmed = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);

                if (confirmed == JOptionPane.YES_OPTION) {
                    // User clicked Yes, close the application
                    dispose();
                  
                }
            }
        });

        // Create the table model
        tableModel = new DefaultTableModel();

        // Create the table
        table = new JTable(tableModel);

        // Create a scroll pane and add the table to it
        JScrollPane scrollPane = new JScrollPane(table);

        // Create buttons
        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        JButton updateButton = new JButton("Update");
        JButton deliveryButton = new JButton("Delivery");
        JButton recordButton = new JButton("Record");
        JButton noti = new JButton("Notification");
        JButton trans = new JButton("Edit Transaction");

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deliveryButton);
        buttonPanel.add(recordButton);
        buttonPanel.add(noti);
        buttonPanel.add(trans);

        // Add the button panel and scroll pane to the frame
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Load data from the database and display it in the table
        loadDataFromDatabase();

        // Add action listeners to the buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Redirect to Add class
                dispose();
                test.main(null);
                
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Redirect to Delete class
                dispose();
                Delete.main(null);
                
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Redirect to Update class
                dispose();
                Update.main(null);
                
            }
        });
        deliveryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                delivery deli = new delivery();
                deli.displayTableDataForId();
                deli.setVisible(true);
            }
        });
        recordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Redirect to history class
                dispose();
                history.main(null);
                
            }
        });

     noti.addActionListener(new ActionListener() {
        @Override
         public void actionPerformed(ActionEvent e) {
             // Redirect to the ServerChat class
              dispose();
            dispose();
              Notification.main(null);
           }
     });
     trans.addActionListener(new ActionListener() {
         @Override
          public void actionPerformed(ActionEvent e) {
              // Redirect to the ServerChat class
               dispose();
             dispose();
               ProcessUpdateGUI.main(null);
            }
      });

    }
    


    private void loadDataFromDatabase() {
        try {
            // Connect to the database
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "1234");

            // Create a statement
            Statement statement = connection.createStatement();

            // Execute the query
            ResultSet resultSet = statement.executeQuery("SELECT * FROM medicine");

            // Clear the existing table data
            tableModel.setRowCount(0);

            // Get the column count and column names from the result set
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            String[] columnNames = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                columnNames[i] = metaData.getColumnLabel(i + 1);
            }

            // Add a new column for the edit button
            String[] newColumnNames = Arrays.copyOf(columnNames, columnCount + 1);
            newColumnNames[columnCount] = "Edit";
            
            columnCount++;

            // Set the updated column names in the table model
            tableModel.setColumnIdentifiers(newColumnNames);

            // Add rows to the table model with an edit button in the new column
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 0; i < columnCount - 1; i++) {
                    rowData[i] = resultSet.getObject(i + 1);
                }

                // Add an edit button to the last column
                JButton editButton = new JButton("Edit");
                editButton.addActionListener(e -> {
                	
                    // Implement your edit button logic here
                    // You can use the rowData array to get the data of the selected row
                    int selectedRow = table.getSelectedRow();

                    // Get the data of the selected row
                    Object[] selectedRowData = new Object[rowData.length];
                    for (int i = 0; i < rowData.length; i++) {
                        selectedRowData[i] = table.getValueAt(selectedRow, i);
                    }

                    // Prompt for delete or update
                    int option = JOptionPane.showOptionDialog(
                            null,
                            "Choose an action:",
                            "Edit Row",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            new Object[]{"Update", "Delete"},
                            "Update"
                    );

                    if (option == JOptionPane.YES_OPTION) {
                        // Update logic
                        // You can use the selectedRowData array to get the data for updating
                        System.out.println("Update selected for row: " + Arrays.toString(selectedRowData));
                    } else if (option == JOptionPane.NO_OPTION) {
                        // Delete logic
                        // You can use the selectedRowData array to get the data for deleting
                        System.out.println("Delete selected for row: " + Arrays.toString(selectedRowData));

                        // Perform the deletion in the database
                        java.math.BigDecimal idToDelete = (java.math.BigDecimal) rowData[0];
 // Assuming the ID is in the first column
                        deleteRowFromDatabase(idToDelete);

                        // Remove the row from the table model
                        tableModel.removeRow(selectedRow);
                    }
                });
                rowData[columnCount - 1] = editButton;

                // Add the row to the table model
                tableModel.addRow(rowData);
            }
            ButtonEditor buttonEditor = new ButtonEditor(new JCheckBox());
            table.getColumnModel().getColumn(columnCount - 1).setCellRenderer(new ButtonRenderer());
            table.getColumnModel().getColumn(columnCount - 1).setCellEditor(buttonEditor);


            // Close the result set, statement, and connection
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
    private class ButtonRenderer extends DefaultTableCellRenderer {
        private JButton button;

        public ButtonRenderer() {
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> {
                // Handle button click here
                // You can use the rowData array to get the data of the selected row
                int selectedRow = table.getSelectedRow();
                // Implement your edit button logic here
            });

            // Add MouseListener to the button
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    int selectedRow = table.rowAtPoint(e.getPoint());
                    table.setRowSelectionInterval(selectedRow, selectedRow);
                }
            });
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Component) {
                return (Component) value;
            }

            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(UIManager.getColor("Button.background"));
            }

            button.setText((value == null) ? "" : value.toString());
            return button;
        }

        @Override
        protected void setValue(Object value) {
            if (value instanceof JButton) {
                setIcon(null);
                setText(((JButton) value).getText());
            } else {
                super.setValue(value);
            }
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        private JButton button;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Handle button click here
                    // You can use the rowData array to get the data of the selected row
                    int selectedRow = table.getSelectedRow();

                    // Get the data of the selected row
                    Object[] rowData = new Object[table.getColumnCount()];
                    for (int i = 0; i < rowData.length; i++) {
                        rowData[i] = table.getValueAt(selectedRow, i);
                    }

                    // Prompt for delete or update
                    int option = JOptionPane.showOptionDialog(
                            null,
                            "Choose an action:",
                            "Edit Row",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            new Object[]{"Update", "Delete"},
                            "Update"
                    );

                    if (option == JOptionPane.YES_OPTION) {
                        // Update logic
                        // You can use the rowData array to get the data for updating
                        System.out.println("Update selected for row: " + Arrays.toString(rowData));
                        // Assuming the first column contains the ID
                        java.math.BigDecimal idToUpdate = (java.math.BigDecimal) rowData[0];

                        openUpdateGui(idToUpdate);

                        // Remove the row from the table model
                       
                    } else if (option == JOptionPane.NO_OPTION) {
                        // Delete logic
                        // You can use the rowData array to get the data for deleting
                        System.out.println("Delete selected for row: " + Arrays.toString(rowData));

                        // Perform the deletion in the database
                        java.math.BigDecimal idToDelete = (java.math.BigDecimal) rowData[0];


                        deleteRowFromDatabase(idToDelete);

                        // Remove the row from the table model
                        tableModel.removeRow(selectedRow);
                    }
                }
            });
        }

        

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value instanceof String) {
                button.setText((String) value);
            }
            return button;
        }
    }

    private void deleteRowFromDatabase(java.math.BigDecimal id) {
        try {
            // Connect to the database
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "1234");

            // Prepare a statement with a parameterized query
            String deleteQuery = "DELETE FROM medicine WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                // Set the parameter for the ID
                preparedStatement.setBigDecimal(1, id);

                // Execute the delete query
                preparedStatement.executeUpdate();
            }

            // Close the connection
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void openUpdateGui(java.math.BigDecimal idToUpdate) {
        JFrame updateFrame = new JFrame("Update Row");
        updateFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        updateFrame.setSize(400, 300);

        JTextField idField = new JTextField(10);
        JTextField nameField = new JTextField(20);
        JTextField mfgDateField = new JTextField(20);
        JTextField expDateField = new JTextField(20);
        JTextField quantityField = new JTextField(20);
        JTextField compNameField = new JTextField(20);
        JTextField priceField = new JTextField(10);
        idField.setEditable(false);

        try {
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "1234");
            Statement statement = connection.createStatement();

            // Fetch data for the selected ID
           ResultSet resultSet = statement.executeQuery("SELECT * FROM medicine WHERE id = " + idToUpdate);

            if (resultSet.next()) {
                idField.setText(resultSet.getString("id"));
                nameField.setText(resultSet.getString("name"));
                mfgDateField.setText(resultSet.getString("mfgdate"));
                expDateField.setText(resultSet.getString("expdate"));
                quantityField.setText(resultSet.getString("quantity"));
                compNameField.setText(resultSet.getString("compname"));
                priceField.setText(resultSet.getString("price"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                	 if (!isValidNumericInput(priceField.getText()) || !isValidNumericInput(quantityField.getText())) {
                         JOptionPane.showMessageDialog(updateFrame, "Price and quantity must be numeric values.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                         return; // Do not proceed with the update if validation fails
                     }
                	 if (!isValidDateFormat(mfgDateField.getText()) || !isValidDateFormat(expDateField.getText())) {
                         JOptionPane.showMessageDialog(updateFrame, "Invalid date format. Please use YYYY-MM-DD.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                         return; // Do not proceed with the update if validation fails
                     }
                	 if (!isExpDateAfterMfgDate(mfgDateField.getText(), expDateField.getText())) {
                         JOptionPane.showMessageDialog(updateFrame, "Expiration date must be after manufacturing date.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                         return; // Do not proceed with the update if validation fails
                     }
                    Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "1234");

                    String updateQuery = "UPDATE medicine SET name=?, mfgdate=?, expdate=?, quantity=?, compname=?, price=? WHERE id=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                    preparedStatement.setString(1, nameField.getText());
                    preparedStatement.setString(2, mfgDateField.getText());
                    preparedStatement.setString(3, expDateField.getText());
                    preparedStatement.setString(4, quantityField.getText());
                    preparedStatement.setString(5, compNameField.getText());
                    preparedStatement.setString(6, priceField.getText());
                    preparedStatement.setBigDecimal(7, new  java.math.BigDecimal(idField.getText()));

                    preparedStatement.executeUpdate();

                    // Close the update GUI
                    updateFrame.dispose();
                    loadDataFromDatabase();

                    // Refresh the table to reflect the changes
                    // You may need to implement a method to refresh your table model
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        JPanel panel = new JPanel(new GridLayout(8, 2));
        panel.add(new JLabel("ID:"));
        panel.add(idField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Mfg Date:"));
        panel.add(mfgDateField);
        panel.add(new JLabel("Exp Date:"));
        panel.add(expDateField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);
        panel.add(new JLabel("Company Name:"));
        panel.add(compNameField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(updateButton);
        updateFrame.setLocation(430,210);
        updateFrame.add(panel);
        updateFrame.setVisible(true);
    }
//    private void updateTableModel() {
//        try {
//            // Clear the existing rows
//            while (tableModel.getRowCount() > 0) {
//                tableModel.removeRow(0);
//            }
//            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "1234");
//
//            Statement statement = connection.createStatement();
//            // Fetch the updated data from the database
//          ResultSet  resultSet = statement.executeQuery("SELECT * FROM medicine");
//            while (resultSet.next()) {
//                Object[] rowData = new Object[tableModel.getColumnCount()];
//                for (int i = 0; i < rowData.length; i++) {
//                    rowData[i] = resultSet.getObject(i + 1);
//                }
//                tableModel.addRow(rowData);
//            }
//
//            // Notify the table that the data has changed
//            tableModel.fireTableDataChanged();
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//    }
    private boolean isValidNumericInput(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private boolean isValidDateFormat(String input) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false); // Strict date format parsing

        try {
            Date parsedDate = dateFormat.parse(input);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
    private boolean isExpDateAfterMfgDate(String mfgDate, String expDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false); // Strict date format parsing

        try {
            Date mfgDateObj = dateFormat.parse(mfgDate);
            Date expDateObj = dateFormat.parse(expDate);
            return expDateObj.after(mfgDateObj);
        } catch (ParseException e) {
            return false;
        }
    }
    private void returnToMain() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MedicineDatabaseApp().setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MedicineDatabaseApp().setVisible(true);
            }
        });
    }
}
