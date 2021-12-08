package view.game.enemy;

import java.awt.Color;

import constant.ColorScheme;
import constant.Icons;
import handler.EnemyHandler;
import handler.SoundController;
import model.Sounds;
import view.game.InformationPanel;
import view.game.UserCharacterPanel;

/**
 * �״� ��� ���� ���̴� ��� ������ ��õ��� ���ߴ� ������ ���̴�.
 */
public class StopItemEnemyPanel extends ItemEnemyPanel {

	private static final int STOP_DURATION_MILLISEC = 5000;
	
	public StopItemEnemyPanel(EnemyHandler handler, UserCharacterPanel userPanel, InformationPanel infoPanel) {
		super(Icons.STOP_ITEM_ENEMY, handler, userPanel, infoPanel);
		wordLabel.setForeground(Color.white);
		wordLabel.setBackground(ColorScheme.ITEM_ENEMY_BACKGROUND);
	}

	@Override
	public void onKilled() {
		SoundController.play(Sounds.KILL_STOP_ITEM);
		Thread th = new Thread() {
			@Override
			public void run() {
				handler.disableMovingAll();
				try {
					sleep(STOP_DURATION_MILLISEC);
				} catch (InterruptedException e) {
					return;
				}
				handler.enableMovingAll();
			}
		};
		th.start();
		
		super.removeThisFromParent();
	}
	
}