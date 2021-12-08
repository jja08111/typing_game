package view.game;

import javax.swing.JPanel;

import view.game.enemy.EnemyPanel;

/**
 * {@link UserCharacterPanel}�� {@link EnemyHandler}�� ���� 
 * ������ {@link EnemyPanel}���� �ִ�.  
 */
public class GameGroundPanel extends JPanel {

	private final UserCharacterPanel userPanel = new UserCharacterPanel();
	
	public GameGroundPanel() {
		setLayout(null);
		
		add(userPanel);
		userPanel.setLocation(0, 180);
	}
	
	public UserCharacterPanel getUserPanel() {
		return userPanel;
	}
	
}