package view.enemy;

import java.awt.Color;

import constant.ColorScheme;
import handler.EnemyHandler;
import view.InformationPanel;
import view.UserCharactorPanel;

/**
 * 죽는 경우 플레이어에게 좋은 현상을 제공하는 추상 객체이다.
 * {@link EnemyPanel}과 비교해서 배경과 텍스트 색상만 다르다.
 */
public abstract class ItemEnemyPanel extends EnemyPanel {

	public ItemEnemyPanel(EnemyHandler handler, UserCharactorPanel userPanel, InformationPanel infoPanel) {
		super(handler, userPanel, infoPanel);
		label.setForeground(Color.white);
		label.setBackground(ColorScheme.itemEnemyBackground);
	}

}
