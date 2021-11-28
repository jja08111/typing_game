package view;

import handler.EnemyHandler;

public class BombItemEnemyPanel extends ItemEnemyPanel {

	/**
	 * 이 적이 죽을 때 제거될 적의 갯수이다.
	 */
	private static final int COUNT = 3;
	
	public BombItemEnemyPanel(EnemyHandler handler, UserCharactorPanel userPanel, InformationPanel infoPanel) {
		super(handler, userPanel, infoPanel);
	}

	@Override
	public void isKilled() {		
		Thread th = new Thread() {
			@Override
			public void run() {
				for (int i = 0; i < COUNT; ++i) {
					handler.removeRandom();
				}
			}
		};
		th.start();
	}
	
}
