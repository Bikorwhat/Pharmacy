import java.awt.EventQueue;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JProgressBar;
import java.awt.Color;
import javax.swing.SwingWorker;

public class loadingf extends JFrame {

	private JPanel contentPane;
	JLabel percent;
	JProgressBar progressBar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					loadingf frame = new loadingf();
					frame.setVisible(true);
					LoadingWorker worker = new LoadingWorker(frame);
					worker.execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public loadingf() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Loading..");
		setBounds(100, 100, 560, 365);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 534, 315);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel label1 = new JLabel("");
		label1.setIcon(new ImageIcon("C:\\Users\\Acer\\Downloads\\1212.jpg"));
		label1.setBounds(0, -37, 553, 374);
		panel.add(label1);

		progressBar = new JProgressBar();
		progressBar.setBackground(Color.WHITE);
		progressBar.setBounds(140, 290, 266, 14);
		panel.add(progressBar);

		percent = new JLabel("0%");
		percent.setBounds(255, 279, 46, 14);
		panel.add(percent);
	}

	/**
	 * SwingWorker class to perform the loading task in the background.
	 */
	private static class LoadingWorker extends SwingWorker<Void, Integer> {
		private final loadingf frame;

		public LoadingWorker(loadingf frame) {
			this.frame = frame;
		}

		@Override
		protected Void doInBackground() throws Exception {
			for (int i = 0; i <= 100; i++) {
				Thread.sleep(100);
				publish(i); // Publish the intermediate progress values
			}
			return null;
		}

		@Override
		protected void process(List<Integer> chunks) {
			// Update the GUI with the intermediate progress values
			Integer progress = chunks.get(chunks.size() - 1);
			frame.progressBar.setValue(progress);
			frame.percent.setText(progress + "%");

			if (progress == 100) {
				// Open the Login class after reaching 100% progress
				login login = new login();
				login.setVisible(true);
				frame.dispose(); // Close the loading frame
			}
		}
	}
}
