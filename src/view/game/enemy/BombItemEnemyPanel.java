package view.game.enemy;

import constant.Icons;
import handler.EnemyHandler;
import handler.SoundController;
import model.Sounds;
import view.game.InformationPanel;
import view.game.UserCharacterPanel;

/**
 * ó���� �� �������� 3���� ������ ���ÿ� ���ŵǴ� ������ �� Ŭ�����̴�.
 */
public class BombItemEnemyPanel extends ItemEnemyPanel {

	/**
	 * �� ���� ���� �� ���ŵ� ���� �����̴�.
	 */
	private static final int COUNT = 3;
	
	public BombItemEnemyPanel(EnemyHandler handler, UserCharacterPanel userPanel, InformationPanel infoPanel) {
		super(Icons.BOMB_ITEM_ENEMY, handler, userPanel, infoPanel);
	}

	@Override
	public void onKilled() {
		SoundController.play(Sounds.KILL_BOMB_ITEM);
		for (int i = 0; i < COUNT; ++i) {
			handler.removeRandomExcept(getWord());
		}
	}
	
}