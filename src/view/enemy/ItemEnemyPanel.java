package view.enemy;

import java.awt.Color;

import javax.swing.ImageIcon;

import constant.ColorScheme;
import constant.Icons;
import handler.EnemyHandler;
import view.InformationPanel;
import view.UserCharactorPanel;

/**
 * 죽는 경우 플레이어에게 좋은 현상을 제공하는 추상 객체이다.
 * {@link EnemyPanel}과 비교해서 배경과 텍스트 색상만 다르다.
 */
public abstract class ItemEnemyPanel extends EnemyPanel {

	public ItemEnemyPanel(ImageIcon icon, EnemyHandler handler, UserCharactorPanel userPanel, InformationPanel infoPanel) {
		super(icon, handler, userPanel, infoPanel);
		wordLabel.setForeground(Color.white);
		wordLabel.setBackground(ColorScheme.ITEM_ENEMY_BACKGROUND);
	}

}
