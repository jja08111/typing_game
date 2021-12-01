package view.enemy;

import constant.Icons;
import handler.EnemyHandler;
import view.InformationPanel;
import view.UserCharactorPanel;

public class BombItemEnemyPanel extends ItemEnemyPanel {

	/**
	 * 이 적이 죽을 때 제거될 적의 갯수이다.
	 */
	private static final int COUNT = 3;
	
	public BombItemEnemyPanel(EnemyHandler handler, UserCharactorPanel userPanel, InformationPanel infoPanel) {
		super(Icons.BOMB_ITEM_ENEMY, handler, userPanel, infoPanel);
	}

	@Override
	public void isKilled() {		
		for (int i = 0; i < COUNT; ++i) {
			handler.removeRandomExcept(getWord());
		}
	}
	
}
