package view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import constant.ColorScheme;
import constant.Icons;
import constant.TextStyle;
import handler.RecordHandler;
import handler.Toast;
import model.RecordItem;
import view.component.CharacterPanel;
import view.component.TitlePanel;

public class RecordPanel extends TitlePanel {

	Vector<RecordItem> records;
	
	public RecordPanel(MainFrame mainFrame) {
		super(mainFrame, "기록", true);

		try {
			records = RecordHandler.readAll();
			records.sort(RecordItem.getComparator());
		} catch (IOException e) {
			System.out.println("기록 파일을 읽는 도중에 파일이 없는 등의 문제가 발생했습니다. 콘솔 로그를 확인해보세요.");
			e.printStackTrace();
			return;
		}
		
		initTable();
		initRanker();
	}

	private void initTable() {
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
		records.forEach(record -> model.addRow(record.toVector()));
		
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
		scrollPane.setLocation(556, 136);
		add(scrollPane);
	}
	
	private void initRanker() {
		RankerPanel first = new RankerPanel(1, Icons.BOMB_ITEM_ENEMY, getNameFrom(1));
		first.setLocation(230, 180);
		add(first);
		
		RankerPanel second = new RankerPanel(2, Icons.USER_CHARACTER, getNameFrom(2));
		second.setLocation(100, 280);
		add(second);
		
		RankerPanel third = new RankerPanel(3, Icons.NORMAL_ENEMY, getNameFrom(3));
		third.setLocation(360, 310);
		add(third);
	}
	
	private String getNameFrom(int rank) {
		if (records.size() < rank) return "Empty";
		
		return records.elementAt(rank - 1).getName();
	}
	
	private class RankerPanel extends JPanel {
		
		public RankerPanel(int rank, ImageIcon icon, String name) {
			setLayout(null);
			
			RankerLabel rankerLabel = new RankerLabel(rank);
			add(rankerLabel);
			
			CharacterPanel character = new CharacterPanel(icon, 100, 100, 0);
			character.setSize(100, 100);
			character.setLocation(0, rankerLabel.getHeight());
			add(character);
			
			JLabel nameLabel = new JLabel(name);
			nameLabel.setFont(TextStyle.BODY_TEXT1);
			nameLabel.setHorizontalAlignment(JLabel.CENTER);
			nameLabel.setLocation(0, rankerLabel.getHeight() + character.getHeight());
			nameLabel.setSize(100, 20);
			add(nameLabel);
			
			setSize(rankerLabel.getWidth(), rankerLabel.getHeight() + character.getHeight() + nameLabel.getHeight());
		}
		
		private class RankerLabel extends JLabel {
			
			public RankerLabel(int rank) {
				assert (1 <= rank && rank <= 3);
				
				Color color = Color.black;
				String text = "";
				switch (rank) {
				case 1: 
					text = "1st";
					color = ColorScheme.SECONDARY_VARIANT;
					break;
				case 2: 
					text = "2nd"; 
					color = ColorScheme.PRIMARY;
					break;
				case 3:
					text = "3rd";
					color = Color.black;
					break;
				}
		
				setFont(TextStyle.HEADLINE5);
				setForeground(color);
				setHorizontalAlignment(JLabel.CENTER);
				setText(text);
				setSize(100, 20);
				setLocation(0, 0);
			}
		}
		
	}
	
}
