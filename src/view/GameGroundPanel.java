package view;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * {@link UserCharactorPanel}과 {@link EnemyHandler}를 통해 
 * 생성된 {@link EnemyPanel}들이 있다.  
 */
public class GameGroundPanel extends JPanel {

	private final UserCharactorPanel userPanel = new UserCharactorPanel();
	
	public GameGroundPanel() {
		setLayout(null);
		
		add(userPanel);
		userPanel.setLocation(0, 180);
	}
	
	public UserCharactorPanel getUserPanel() {
		return userPanel;
	}
	
}
