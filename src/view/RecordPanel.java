package view;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import handler.RecordHandler;

public class RecordPanel extends TitlePanel {

	public RecordPanel(MainFrame mainFrame) {
		super(mainFrame, "기록", true);

		Vector<Vector<Object>> records = RecordHandler.getInstance().readAll();
		
		DefaultTableModel model = new DefaultTableModel(new String[]{"이름", "점수", "단계"}, 0) {
			@Override
			public Class getColumnClass(int column) {
				switch (column) {
                case 0:
                    return String.class;
                case 1:
                    return Integer.class;
                case 2:
                    return Integer.class;
                default:
                    return String.class;
				}
			}
		};
		records.forEach(record -> model.addRow(record));
		
		JTable table = new JTable(model);
		table.setEnabled(false);
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
		table.setRowSorter(sorter);

		List<SortKey> sortKeys = new ArrayList<>(3);
		sortKeys.add(new SortKey(1, SortOrder.DESCENDING));
		sortKeys.add(new SortKey(2, SortOrder.DESCENDING));
		sortKeys.add(new SortKey(0, SortOrder.ASCENDING));
		sorter.setSortKeys(sortKeys);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setSize(400, 400);
		scrollPane.setLocation(300, 136);
		add(scrollPane);
	}

}
