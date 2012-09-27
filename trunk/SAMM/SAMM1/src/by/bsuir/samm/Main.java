package by.bsuir.samm;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Main {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 750, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 184, 600);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(5, 5, 179, 37);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblA = new JLabel("a:");
		lblA.setBounds(12, 12, 14, 15);
		panel_1.add(lblA);
		
		textField = new JTextField();
		textField.setBounds(44, 10, 114, 19);
		panel_1.add(textField);
		textField.setColumns(10);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(5, 42, 179, 37);
		panel.add(panel_2);
		panel_2.setLayout(null);
		
		JLabel lblR = new JLabel("R:");
		lblR.setBounds(12, 12, 14, 15);
		panel_2.add(lblR);
		
		textField_1 = new JTextField();
		textField_1.setBounds(44, 10, 114, 19);
		panel_2.add(textField_1);
		textField_1.setColumns(10);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(5, 79, 179, 37);
		panel.add(panel_3);
		panel_3.setLayout(null);
		
		JLabel lblN = new JLabel("n:");
		lblN.setBounds(12, 12, 14, 15);
		panel_3.add(lblN);
		
		textField_2 = new JTextField();
		textField_2.setBounds(44, 10, 114, 19);
		panel_3.add(textField_2);
		textField_2.setColumns(10);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBounds(186, 0, 564, 600);
		frame.getContentPane().add(panel_4);
	}
}
