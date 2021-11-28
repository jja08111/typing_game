package view;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import constant.ColorScheme;
import constant.TextStyle;
import handler.EnemyHandler;

public class GamePanel extends JPanel {
	
	private final TypingField typingField;
	
	private final InformationPanel informationPanel;

	private final GameGroundPanel groundPanel;
	
	private final EnemyHandler enemyHandler;

	public GamePanel(
			TypingField typingField,
			InformationPanel informationPanel, 
			GameGroundPanel groundPanel,
			EnemyHandler enemyHandler
			) {
		this.typingField = typingField;
		this.informationPanel = informationPanel;
		this.groundPanel = groundPanel;
		this.enemyHandler = enemyHandler;
		
		setLayout(new BorderLayout());
		// Padding을 모든 방향에 8씩 부여한다.
		setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		add(groundPanel, BorderLayout.CENTER);
		add(new InputPanel(), BorderLayout.SOUTH);
	}
	
	private void startGame() {
		enemyHandler.startGenThread();
		typingField.changeToTextFieldMode();
	}
	
	private class InputPanel extends JPanel {
		
		public InputPanel() {
			setLayout(new FlowLayout());
			
			typingField.addKeyListener(new KeyAdapter() {
				@Override 
				public void keyTyped(KeyEvent e) {
					if (e.getKeyChar() == ' ') {
						if (typingField.getIsSimpleMode()) {
							startGame();
							// 공백입력을 무시한다.
							e.consume();
						}
					}
				}
			});
			typingField.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JTextField t = (JTextField)e.getSource();
					String inputWord = t.getText();
					
					if (enemyHandler.kill(inputWord)) { // 사용자가 단어 맞추기 성공한 경우 
						informationPanel.increaseScore();
					} else {
						informationPanel.decreaseScore();
					}
					
					// inputField 텍스트를 비운다.
					t.setText("");
				}
			});
			add(typingField);
		}
	}
	
}
