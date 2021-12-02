package view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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

import constant.Icons;
import constant.TextStyle;
import handler.RecordHandler;
import handler.Toast;
import model.RecordItem;

public class RecordPanel extends TitlePanel {

	Vector<RecordItem> records;
	
	public RecordPanel(MainFrame mainFrame) {
		super(mainFrame, "기록", true);

		try {
			records = RecordHandler.readAll();
			records.sort(RecordItem.getComparator());
		} catch (IOException e) {
			Toast.show("기록 파일을 읽는 도중에 파일 입출력 에러가 발생했습니다. 콘솔 로그를 확인해보세요.", 7000, getComponentPopupMenu());
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
			
			JLabel rankLabel = new JLabel(getRankLabelText(rank));
			rankLabel.setFont(TextStyle.HEADLINE5);
			rankLabel.setHorizontalAlignment(JLabel.CENTER);
			rankLabel.setSize(100, 20);
			rankLabel.setLocation(0, 0);
			add(rankLabel);
			
			CharacterPanel character = new CharacterPanel(icon, 100, 100, 0);
			character.setSize(100, 100);
			character.setLocation(0, rankLabel.getHeight());
			add(character);
			
			JLabel nameLabel = new JLabel(name);
			nameLabel.setFont(TextStyle.BODY_TEXT1);
			nameLabel.setHorizontalAlignment(JLabel.CENTER);
			nameLabel.setLocation(0, rankLabel.getHeight() + character.getHeight());
			nameLabel.setSize(100, 20);
			add(nameLabel);
			
			setSize(rankLabel.getWidth(), rankLabel.getHeight() + character.getHeight() + nameLabel.getHeight());
		}
		
		private String getRankLabelText(int rank) {
			assert (1 <= rank && rank <= 3);
			
			switch (rank) {
			case 1: return "1st";
			case 2: return "2nd";
			case 3: return "3rd";
			}
			return null;
		}
	}
	
}
