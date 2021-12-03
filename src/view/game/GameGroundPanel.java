package view.game;

import javax.swing.JPanel;

import view.game.enemy.EnemyPanel;

/**
 * {@link UserCharacterPanel}과 {@link EnemyHandler}를 통해 
 * 생성된 {@link EnemyPanel}들이 있다.  
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
