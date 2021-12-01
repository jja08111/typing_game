package view.enemy;

import java.awt.Color;

import constant.ColorScheme;
import constant.Icons;
import handler.EnemyHandler;
import view.InformationPanel;
import view.UserCharacterPanel;

/**
 * 죽는 경우 현재 보이는 모든 적들을 멈추는 아이템 적이다.
 */
public class StopItemEnemyPanel extends ItemEnemyPanel {

	private static final int STOP_DURATION_MILLISEC = 3000;
	
	public StopItemEnemyPanel(EnemyHandler handler, UserCharacterPanel userPanel, InformationPanel infoPanel) {
		super(Icons.STOP_ITEM_ENEMY, handler, userPanel, infoPanel);
		wordLabel.setForeground(Color.white);
		wordLabel.setBackground(ColorScheme.ITEM_ENEMY_BACKGROUND);
	}

	@Override
	public void isKilled() {
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
