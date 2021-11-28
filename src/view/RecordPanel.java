package view;

import javax.swing.JTable;

public class RecordPanel extends TitlePanel {

	private final MainFrame mainFrame;
	
	public RecordPanel(MainFrame mainFrame) {
		super("기록", mainFrame, true);
		this.mainFrame = mainFrame;

		JTable table = new JTable();
		add(table);
	}
	
}
