package view;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import util.TextSource;

public class GamePanel extends JPanel {
	
	private JTextField inputField = new JTextField(40);
	
	private JLabel text = new JLabel("타이핑 해보세요");
	
	private InformationPanel scorePanel = new InformationPanel();
	
	private EditPanel editPanel = new EditPanel();
	
	/*
	 * 단어 벡터를 생성하는 클래스 변수이다.
	 */
	private TextSource textSource = new TextSource();
	
	public GamePanel(InformationPanel scorePanel, EditPanel editPanel) {
		this.scorePanel = scorePanel;
		this.editPanel = editPanel;
		
		setLayout(new BorderLayout());
		add(new GameGroundPanel(), BorderLayout.CENTER);
		add(new InputPanel(), BorderLayout.SOUTH);
		
		inputField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JTextField t = (JTextField)e.getSource();
				String inputWord = t.getText();
				
				if (text.getText().equals(inputWord)) { // 사용자가 단어 맞추기 성공한 경우 
					//scorePanel.increase();
					startGame();
				}
				
				// inputField 텍스트를 비운다.
				t.setText("");
			}
		});
	}
	
	public void startGame() {
		// 단어 한 개를 선택한다.
		String word = textSource.get();
		text.setText(word);
		text.setBackground(Color.GREEN);
		text.setOpaque(true);
	}
	
	private class GameGroundPanel extends JPanel {
		public GameGroundPanel() {
			setLayout(null);
			text.setSize(100, 30);
			text.setLocation(100, 10);
			add(text);
		}
	}
	
	private class InputPanel extends JPanel {
		public InputPanel() {
			setLayout(new FlowLayout());
			setBackground(Color.DARK_GRAY);
			add(inputField);
		}
	}
	
}
