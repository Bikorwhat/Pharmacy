import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboard extends JFrame {
    public Dashboard() {
        setTitle("Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(900, 290));
        getContentPane().setBackground(new Color(173, 216, 230));
        String userId = Session.getUsername();
        JLabel tf = new JLabel("Welcome " + userId);

        // Create JMenu items
        JMenuItem infoMenuItem = new JMenuItem("User Detail");
        JMenuItem buyMenuItem = new JMenuItem("Buy Medicine");
        JMenuItem methodMenuItem = new JMenuItem("Edit Method");
        JMenuItem historyMenuItem = new JMenuItem("User History");
        JMenuItem compInfoMenuItem = new JMenuItem("Company Info");
        JMenuItem checkMenuItem = new JMenuItem("Symptom&Medicine");
        JMenuItem logoutMenuItem = new JMenuItem("Logout");

        // Add ActionListener to each JMenuItem
        infoMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UserDetail userDetail = new UserDetail(Dashboard.this);
                userDetail.setVisible(true);
            }
        });

        buyMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	MedicineGUI medicineGUI = new MedicineGUI(Dashboard.this);
                medicineGUI.setVisible(true);
            }
        });
//        methodMenuItem.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                Method methodGUI = new Method(Dashboard.this, userId);
//                methodGUI.setVisible(true);
//            }
//        });


        historyMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UserHistory userHistory = new UserHistory(Dashboard.this);
                userHistory.displayTableDataForId();
                userHistory.setVisible(true);
            }
        });

        compInfoMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                compInfo compInfo = new compInfo();
                compInfo.setVisible(true);
            }
        });
        checkMenuItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                calcFev calcFevWindow = new calcFev();
                calcFevWindow.setVisible(true);
            }
        });

        logoutMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to logout?",
                        "Logout Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    dispose(); // Close the dashboard window
                    login.main(null);
                }
            }
        });

        // Create JMenuBar and add JMenu items
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(infoMenuItem);
        menuBar.add(buyMenuItem);
//        menuBar.add(methodMenuItem);
        menuBar.add(historyMenuItem);
        menuBar.add(compInfoMenuItem);
        menuBar.add(checkMenuItem);
        menuBar.add(logoutMenuItem);
        
        

        // Set the JMenuBar to the JFrame
        setJMenuBar(menuBar);

        // Create layout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.insets = new Insets(10, 10, 10, 10);
        panel.add(tf, constraints);

        add(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Dashboard();
            }
        });
    }
}
