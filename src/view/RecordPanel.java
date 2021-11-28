package view;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

public class RecordPanel extends TitlePanel {

	private final MainFrame mainFrame;
	
	public RecordPanel(MainFrame mainFrame) {
		super("기록");
		this.mainFrame = mainFrame;

		JTable table = new JTable();
		add(table);
	}
	
}
