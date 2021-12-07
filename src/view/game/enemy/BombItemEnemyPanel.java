package view.game.enemy;

import constant.Icons;
import handler.EnemyHandler;
import handler.SoundController;
import model.Sounds;
import view.game.InformationPanel;
import view.game.UserCharacterPanel;

/**
 * 처리될 때 무작위로 3개의 적들이 동시에 제거되는 아이템 적 클래스이다.
 */
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
