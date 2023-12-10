//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.pdf.PdfWriter;
//
//public class TransactionApp extends JFrame {
//    private JComboBox<String> transactionComboBox;
//    private JRadioButton pickUpRadioButton, deliveryRadioButton;
//    private JButton generatePDFButton;
//
//    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
//    private static final String DB_USER = "system";
//    private static final String DB_PASSWORD = "1234";
//
//    public TransactionApp() {
//        setTitle("Transaction PDF Generator");
//        setSize(400, 200);
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        setLayout(new FlowLayout());
//
//        transactionComboBox = new JComboBox<>();
//        pickUpRadioButton = new JRadioButton("Pick Up");
//        deliveryRadioButton = new JRadioButton("Delivery");
//        generatePDFButton = new JButton("Generate PDF");
//
//        ButtonGroup methodGroup = new ButtonGroup();
//        methodGroup.add(pickUpRadioButton);
//        methodGroup.add(deliveryRadioButton);
//
//        // Add components to the frame
//        add(new JLabel("Select Transaction ID:"));
//        add(transactionComboBox);
//        add(new JLabel("Select Method:"));
//        add(pickUpRadioButton);
//        add(deliveryRadioButton);
//        add(generatePDFButton);
//
//        generatePDFButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String selectedTransaction = (String) transactionComboBox.getSelectedItem();
//                String method = pickUpRadioButton.isSelected() ? "Pick Up" : "Delivery";
//
//                try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
//                    // Update the METHOD column in the HISTORY table
//                    // ... (use PreparedStatement to update)
//
//                    // Generate PDF content
//                    String pdfContent = "Transaction ID: " + selectedTransaction + "\n"
//                            + "Method: " + method + "\n"
//                            + "ID: ...\n";  // Include other data from the database
//
//                    generatePDF(pdfContent);
//                    JOptionPane.showMessageDialog(TransactionApp.this, "PDF generated successfully.");
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                    JOptionPane.showMessageDialog(TransactionApp.this, "An error occurred.");
//                }
//            }
//        });
//
//        // Initialize transactionComboBox with data from the database
//        // ...
//
//        setVisible(true);
//    }
//
//    private void generatePDF(String content) throws DocumentException {
//        Document document = new Document();
//        try {
//            PdfWriter.getInstance(document, new FileOutputStream("transaction.pdf"));
//            document.open();
//            document.add(new Paragraph(content));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } finally {
//            document.close();
//        }
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new TransactionApp());
//    }
//}
