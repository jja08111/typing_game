package view;

import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import handler.TextSourceHandler;
import handler.Toast;
import view.component.DefaultButton;
import view.component.TitlePanel;

/**
 * ���� ����� ��Ÿ���� �ܾ� ����� ������ �� �ִ� ȭ���̴�.
 */
public class WordEditPanel extends TitlePanel {
	
	public static final int MAX_WORD_LENGTH = 30;
	
	private final DefaultListModel<String> model = new DefaultListModel<String>();
	
	private final WordList list = new WordList();
	
	private final JTextField textField = new JTextField(20);
	
	private final JPanel buttonPane = new JPanel();
	
	private final DefaultButton addButton = new DefaultButton("�߰�");
	
	private final DefaultButton removeButton  = new DefaultButton("����");
	
	public WordEditPanel(MainFrame mainFrame) {
		super(mainFrame, "�ܾ� ����", true);
		
		initScrollPane();
		initTextField();
		initAddButton();
		initRemoveButton();
		initButtonPane();
		
		// �ؽ�Ʈ �ʵ忡 ��Ŀ���� �ش�.
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				textField.requestFocus();
				removeComponentListener(this);
			}
		});
	}
	
	private void initScrollPane() {
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setSize(390, 300);
		scrollPane.setLocation(306, 144);
		add(scrollPane);
	}
	
	private void initTextField() {
		textField.addKeyListener(new KeyAdapter() {
			@Override 
			public void keyTyped(KeyEvent e) {
				char typedChar = e.getKeyChar();
				if (typedChar == '\n') {
					addWord();
					return;
				}
				
				// ���� �Է��� �����Ѵ�.
				if (Character.isWhitespace(typedChar)) {
					e.consume();
					return;
				}
				
				int inputLength = textField.getText().length();
				// 20�� �̻��� �Է��� �� ���� �Ѵ�.
				if (inputLength >= MAX_WORD_LENGTH) 
					e.consume();
				
				boolean isAddButtonEnabled = addButton.isEnabled();

				// ��� ���ڶ� �Էµ� ��츸 �߰� ��ư�� Ȱ��ȭ ��Ų��. �̶� ����Ű, �齺���̽�, �̽�������, ����Ʈ ��ư�� �����̴�.
				if (!isAddButtonEnabled 
						&& typedChar != '\n' 
						&& typedChar != '\b' 
						&& typedChar != 0x1b
						&& typedChar != 127)
					addButton.setEnabled(true);
				else if (isAddButtonEnabled && inputLength == 0) {
					addButton.setEnabled(false);
				}
			}
		});
	}
	
	private void initAddButton() {
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addWord();
				addButton.setEnabled(false);
			}
		});
		// �ʱ⿡�� ������ �Է����� ���ϵ��� ��Ȱ��ȭ�Ѵ�.
		addButton.setEnabled(false);
	}
	
	private void initRemoveButton() {
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedValue = list.getSelectedValue();
			
				TextSourceHandler.getInstance().remove(selectedValue);
				model.removeElement(selectedValue);
				Toast.show("�ܾ� '" + selectedValue + "'��(��) ������", WordEditPanel.this);
				
				removeButton.setEnabled(false);
			}
		});
		// ����Ʈ���� �ƹ��͵� �������� ���� �ʱ⿡�� ��Ȱ��ȭ�Ѵ�.
		removeButton.setEnabled(false);
	}
	
	private void initButtonPane() {
		buttonPane.setLayout(new BoxLayout(buttonPane,BoxLayout.LINE_AXIS));
		buttonPane.add(textField);
		buttonPane.add(addButton);
		buttonPane.add(Box.createHorizontalStrut(5));
		buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
		buttonPane.add(Box.createHorizontalStrut(5));
		buttonPane.add(removeButton);
		buttonPane.setSize(400, 24);
		buttonPane.setLocation(304, 472);
		add(buttonPane);
	}
	
	/**
	 * �ؽ�Ʈ �ʵ忡 �Էµ� �ܾ �����Ѵ�. �Էµ� �ܾ ������ ��� �����Ѵ�.
	 */
	private void addWord() {
		String input = textField.getText();
		
		if (input.isBlank()) return;
		// �ؽ�Ʈ ���忡 ������ ��츸 ����Ʈ���� �߰��Ѵ�. 
		try {
			TextSourceHandler.getInstance().add(input);
			
			model.add(model.getSize(), input);	
			// ���� �߰��� �ܾ ���̰� ��ũ�� ��ġ�� �ٲ۴�.
			list.ensureIndexIsVisible(model.getSize() - 1);
			Toast.show("�ܾ� �߰���", 2000, WordEditPanel.this);
		} catch (IllegalArgumentException e1) {
			Toast.show(e1.getMessage(), WordEditPanel.this);
		} catch (IOException e1) {
			showFileIOExceptionToast();
			e1.printStackTrace();
		}

		textField.setText("");
		textField.requestFocus();
	}
	
	private void showFileIOExceptionToast() {
		Toast.show("���� ����� ������ �߻��߽��ϴ�. �ܼ� â���� ���� ������ Ȯ���� �� �ֽ��ϴ�.", 7000, WordEditPanel.this);
	}
	
	private class WordList extends JList<String> {
		
		public WordList() {
			super(model);
			setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
			model.addElement("�ε���...");
			new Thread() {
				@Override 
				public void run() {
					Vector<String> list = TextSourceHandler.getInstance().getAll();
					// "�ε���..." ������ �����.
					model.remove(0);
					
					model.addAll(list);
				}
			}.start();
			
			addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					if (!removeButton.isEnabled()) 
						removeButton.setEnabled(true);					
				}
			});
		}
		
	}

}