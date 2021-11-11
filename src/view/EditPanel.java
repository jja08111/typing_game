package view;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EditPanel extends JPanel {

	private JTextField textField = new JTextField(20);
	
	private JButton addButton = new JButton("ADD");
	
	private JButton saveButton = new JButton("SAVE");
	
	public EditPanel() {
		setBackground(Color.GRAY);
		setLayout(new FlowLayout());
		add(textField);
		add(addButton);
		add(saveButton);
	}
	
}
