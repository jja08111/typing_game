package view.game.enemy;

import java.awt.Color;

import javax.swing.ImageIcon;

import constant.ColorScheme;
import constant.Icons;
import handler.EnemyHandler;
import view.game.InformationPanel;
import view.game.UserCharacterPanel;

/**
 * �״� ��� �÷��̾�� ���� ������ �����ϴ� �߻� ��ü�̴�.
 * {@link EnemyPanel}�� ���ؼ� ���� �ؽ�Ʈ ���� �ٸ���.
 */
public abstract class ItemEnemyPanel extends EnemyPanel {

	public ItemEnemyPanel(ImageIcon icon, EnemyHandler handler, UserCharacterPanel userPanel, InformationPanel infoPanel) {
		super(icon, handler, userPanel, infoPanel);
		wordLabel.setForeground(Color.white);
		wordLabel.setBackground(ColorScheme.ITEM_ENEMY_BACKGROUND);
	}

}