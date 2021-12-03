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

public class WordEditPanel extends TitlePanel {
	
	public static final int MAX_WORD_LENGTH = 30;
	
	private final DefaultListModel<String> model = new DefaultListModel<String>();
	
	private final WordList list = new WordList();
	
	private final JTextField textField = new JTextField(20);
	
	private final JPanel buttonPane = new JPanel();
	
	private final DefaultButton addButton = new DefaultButton("추가");
	
	private final DefaultButton removeButton  = new DefaultButton("삭제");
	
	public WordEditPanel(MainFrame mainFrame) {
		super(mainFrame, "단어 편집", true);
		
		initScrollPane();
		initTextField();
		initAddButton();
		initRemoveButton();
		initButtonPane();
		
		// 텍스트 필드에 포커스를 준다.
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
				switch (e.getKeyChar()) {
				case '\n':
					addWord();
					return;
				case ' ':
					// 공백입력을 무시한다.
					e.consume();
					return;
				}
				
				int inputLength = textField.getText().length();
				// 20자 이상은 입력할 수 없게 한다.
				if (inputLength >= MAX_WORD_LENGTH) 
					e.consume();
				
				// 어느 글자라도 입력된 경우만 추가 버튼을 활성화 시킨다.
				boolean isAddButtonEnabled = addButton.isEnabled();
				if (!isAddButtonEnabled)
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
			}
		});
		// 초기에는 공백을 입력하지 못하도록 비활성화한다.
		addButton.setEnabled(false);
	}
	
	private void initRemoveButton() {
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedValue = list.getSelectedValue();
			
				TextSourceHandler.getInstance().remove(selectedValue);
				model.removeElement(selectedValue);
				Toast.show("단어 '" + selectedValue + "'이(가) 삭제됨", WordEditPanel.this);
			}
		});
		// 리스트에서 아무것도 선택하지 않은 초기에는 비활성화한다.
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
	 * 텍스트 필드에 입력된 단어를 저장한다.
	 */
	private void addWord() {
		String input = textField.getText();
		
		if (input.isBlank()) return;
		// 텍스트 저장에 성공한 경우만 리시트에도 추가한다. 
		try {
			TextSourceHandler.getInstance().add(input);
			
			model.add(model.getSize(), input);	
			// 새로 추가된 단어가 보이게 스크롤 위치를 바꾼다.
			list.ensureIndexIsVisible(model.getSize() - 1);
			Toast.show("단어 추가됨", 2000, WordEditPanel.this);
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
		Toast.show("파일 입출력 에러가 발생했습니다. 콘솔 창에서 에러 내용을 확인할 수 있습니다.", 7000, WordEditPanel.this);
	}
	
	private class WordList extends JList<String> {
		
		public WordList() {
			super(model);
			setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
			model.addElement("로딩중...");
			new Thread() {
				@Override 
				public void run() {
					Vector<String> list = TextSourceHandler.getInstance().getAll();
					// "로딩중..." 문구를 지운다.
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
