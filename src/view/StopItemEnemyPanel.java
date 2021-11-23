package view;

import java.awt.Color;

import constant.ColorScheme;
import handler.EnemyHandler;

public class StopItemEnemyPanel extends ItemEnemyPanel {

	private static final int STOP_DURATION_MILLISEC = 3000;
	
	public StopItemEnemyPanel(EnemyHandler handler, UserCharactorPanel userPanel, InformationPanel infoPanel) {
		super(handler, userPanel, infoPanel);
		label.setForeground(Color.white);
		label.setBackground(ColorScheme.itemEnemyBackground);
	}

	@Override
	public void removeThisFromParent() {
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
