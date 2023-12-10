import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class calcFev extends JFrame {
    public calcFev() {
        setTitle("Symptoms&Medicine");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(550, 450);
        setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1)); // One column, variable rows

        // Create a label indicating the importance of consulting a doctor
        JLabel doctorLabel = new JLabel("Please check up with a doctor for full accuracy.");

        // Create buttons for diseases
        JButton influenzaButton = new JButton("Influenza (Flu)");
        JButton commonColdButton = new JButton("Common Cold");
        JButton strepThroatButton = new JButton("Strep Throat");
        JButton bronchitisButton = new JButton("Bronchitis");
        JButton pneumoniaButton = new JButton("Pneumonia");
        JButton asthmaButton = new JButton("Asthma");
        JButton hypertensionButton = new JButton("Hypertension (High Blood Pressure)");
        JButton diabetesButton = new JButton("Type 2 Diabetes");
        JButton arthritisButton = new JButton("Rheumatoid Arthritis");
        JButton depressionButton = new JButton("Depression");
        JButton anxietyButton = new JButton("Anxiety Disorders");
        JButton hivButton = new JButton("HIV/AIDS");
        JButton heartDiseaseButton = new JButton("Heart Disease");
        JButton alzheimersButton = new JButton("Alzheimer's Disease");
        JButton lbButton = new JButton("Low Blood Pressure");

        // Create a map to store disease information
        Map<String, String[]> diseaseInfo = new HashMap<>();
        diseaseInfo.put("Influenza (Flu)", new String[]{"Symptoms: Fever, cough, sore throat, body aches, fatigue.", "Medicine: Antiviral medications (e.g., oseltamivir) for severe cases, over-the-counter pain relievers (e.g., acetaminophen) for symptom relief."});
        diseaseInfo.put("Common Cold", new String[]{"Symptoms: Runny or stuffy nose, sneezing, sore throat, cough, mild fatigue.", "Medicine: Over-the-counter cold remedies (e.g., decongestants, antihistamines), plenty of fluids, rest."});
        diseaseInfo.put("Strep Throat", new String[]{"Symptoms: Sore throat, fever, swollen tonsils, difficulty swallowing.", "Medicine: Antibiotics (e.g., penicillin) prescribed by a doctor."});
        diseaseInfo.put("Bronchitis", new String[]{"Symptoms: Persistent cough, chest congestion, shortness of breath, fever (in some cases).", "Medicine: Antibiotics (for bacterial bronchitis), cough suppressants, inhalers (for chronic bronchitis)."});
        diseaseInfo.put("Pneumonia", new String[]{"Symptoms: High fever, cough with yellow or green mucus, chest pain, difficulty breathing.", "Medicine: Antibiotics (specific to the type of pneumonia), supportive care."});
        diseaseInfo.put("Asthma", new String[]{"Symptoms: Wheezing, shortness of breath, chest tightness, cough.", "Medicine: Inhalers (e.g., albuterol for quick relief, corticosteroids for long-term control)."});
        diseaseInfo.put("Hypertension (High Blood Pressure)", new String[]{"Symptoms: Often asymptomatic, but can lead to headaches, dizziness, chest pain, shortness of breath.", "Medicine: Antihypertensive medications (e.g., ACE inhibitors, beta-blockers)."});
        diseaseInfo.put("Type 2 Diabetes", new String[]{"Symptoms: Increased thirst, frequent urination, fatigue, blurred vision.", "Medicine: Oral antidiabetic medications (e.g., metformin), insulin (in some cases)."});
        diseaseInfo.put("Rheumatoid Arthritis", new String[]{"Symptoms: Joint pain, swelling, stiffness, fatigue.", "Medicine: Disease-modifying antirheumatic drugs (DMARDs), nonsteroidal anti-inflammatory drugs (NSAIDs)."});
        diseaseInfo.put("Depression", new String[]{"Symptoms: Persistent sadness, loss of interest, changes in appetite or sleep patterns, fatigue.", "Medicine: Antidepressants (e.g., selective serotonin reuptake inhibitors, SSRIs), therapy."});
        diseaseInfo.put("Anxiety Disorders", new String[]{"Symptoms: Excessive worry, restlessness, muscle tension, panic attacks.", "Medicine: Antianxiety medications (e.g., benzodiazepines), therapy."});
        diseaseInfo.put("HIV/AIDS", new String[]{"Symptoms: Early stages may have flu-like symptoms, later stages can lead to severe illnesses due to a weakened immune system.", "Medicine: Antiretroviral therapy (ART) to control HIV, and other medications to treat associated infections."});
        diseaseInfo.put("Heart Disease", new String[]{"Symptoms: Chest pain (angina), shortness of breath, fatigue, irregular heartbeat.", "Medicine: Various medications, including blood thinners, beta-blockers, statins, and nitroglycerin."});
        diseaseInfo.put("Alzheimer's Disease", new String[]{"Symptoms: Memory loss, confusion, mood swings, difficulty with daily tasks.", "Medicine: Medications like cholinesterase inhibitors (e.g., donepezil) for symptom management."});
        diseaseInfo.put("Low Blood Pressure", new String[]{"Symptoms:Dizziness or lightheadedness, especially when standing up from a sitting or lying position (orthostatic hypotension),Fainting (syncope),Blurred vision,Fatigu,Rapid, shallow breathing", "Medicine: Fludrocortisone (for specific cases),Midodrine (for orthostatic hypotension"});

        // Create text areas for symptoms and medicine
        JTextArea symptomsTextArea = new JTextArea();
        JTextArea medicineTextArea = new JTextArea();

        // Add action listeners to the disease buttons
        ActionListener buttonListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JButton clickedButton = (JButton) e.getSource();
                String diseaseName = clickedButton.getText();
                String[] info = diseaseInfo.get(diseaseName);
                if (info != null && info.length == 2) {
                    // Display information in a dialog box
                    JOptionPane.showMessageDialog(calcFev.this, "Symptoms:\n" + info[0] + "\n\nMedicine:\n" + info[1]);
                } else {
                    JOptionPane.showMessageDialog(calcFev.this, "Disease information not available.");
                }
            }
        };

        // Add buttons to the panel and attach listeners
        panel.add(doctorLabel); 
        panel.add(influenzaButton);
        influenzaButton.addActionListener(buttonListener);

        panel.add(commonColdButton);
        commonColdButton.addActionListener(buttonListener);

        panel.add(strepThroatButton);
        strepThroatButton.addActionListener(buttonListener);

        panel.add(bronchitisButton);
        bronchitisButton.addActionListener(buttonListener);

        panel.add(pneumoniaButton);
        pneumoniaButton.addActionListener(buttonListener);

        panel.add(asthmaButton);
        asthmaButton.addActionListener(buttonListener);

        panel.add(hypertensionButton);
        hypertensionButton.addActionListener(buttonListener);

        panel.add(diabetesButton);
        diabetesButton.addActionListener(buttonListener);

        panel.add(arthritisButton);
        arthritisButton.addActionListener(buttonListener);

        panel.add(depressionButton);
        depressionButton.addActionListener(buttonListener);

        panel.add(anxietyButton);
        anxietyButton.addActionListener(buttonListener);

        panel.add(hivButton);
        hivButton.addActionListener(buttonListener);

        panel.add(heartDiseaseButton);
        heartDiseaseButton.addActionListener(buttonListener);

        panel.add(alzheimersButton);
        alzheimersButton.addActionListener(buttonListener);
        
        panel.add(lbButton);
        lbButton.addActionListener(buttonListener);



        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new calcFev();
            }
        });
    }
}
