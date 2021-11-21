package view;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import constant.ColorScheme;
import constant.TextStyle;
import handler.EnemyHandler;
import handler.TextSourceHandler;

public class GamePanel extends JPanel {
	
	private final JTextField inputField = new JTextField(20);
	
	private final InformationPanel informationPanel;

	private final EnemyHandler enemyHandler;

	public GamePanel(
			InformationPanel informationPanel, 
			GameGroundPanel groundPanel,
			EnemyHandler enemyHandler
			) {
		this.informationPanel = informationPanel;
		this.enemyHandler = enemyHandler;
		
		setLayout(new BorderLayout());
		// Padding을 모든 방향에 8씩 부여한다.
		setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		add(groundPanel, BorderLayout.CENTER);
		add(new InputPanel(), BorderLayout.SOUTH);
	}
	
	public void startGame() {
		enemyHandler.startGenThread();
	}
	
	private class InputPanel extends JPanel {
		public InputPanel() {
			setLayout(new FlowLayout());
			inputField.setFont(TextStyle.headline5);
			inputField.setBorder(new LineBorder(ColorScheme.primary, 3));
			inputField.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JTextField t = (JTextField)e.getSource();
					String inputWord = t.getText();
					
					if (enemyHandler.remove(inputWord)) { // 사용자가 단어 맞추기 성공한 경우 
						informationPanel.increaseScore();
					} else {
						informationPanel.decreaseScore();
					}
					
					// inputField 텍스트를 비운다.
					t.setText("");
				}
			});
			add(inputField);
		}
	}
	
}
