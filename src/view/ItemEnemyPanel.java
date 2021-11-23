package view;

import java.awt.Color;

import constant.ColorScheme;
import handler.EnemyHandler;

public abstract class ItemEnemyPanel extends EnemyPanel {

	public ItemEnemyPanel(EnemyHandler handler, UserCharactorPanel userPanel, InformationPanel infoPanel) {
		super(handler, userPanel, infoPanel);
		label.setForeground(Color.white);
		label.setBackground(ColorScheme.itemEnemyBackground);
	}

}
