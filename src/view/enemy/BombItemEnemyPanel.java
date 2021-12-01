package view.enemy;

import constant.Icons;
import handler.EnemyHandler;
import handler.SoundController;
import model.Sounds;
import view.InformationPanel;
import view.UserCharacterPanel;

public class BombItemEnemyPanel extends ItemEnemyPanel {

	/**
	 * 이 적이 죽을 때 제거될 적의 갯수이다.
	 */
	private static final int COUNT = 3;
	
	public BombItemEnemyPanel(EnemyHandler handler, UserCharacterPanel userPanel, InformationPanel infoPanel) {
		super(Icons.BOMB_ITEM_ENEMY, handler, userPanel, infoPanel);
	}

	@Override
	public void isKilled() {
		SoundController.play(Sounds.KILL_BOMB_ITEM);
		for (int i = 0; i < COUNT; ++i) {
			handler.removeRandomExcept(getWord());
		}
	}
	
}
