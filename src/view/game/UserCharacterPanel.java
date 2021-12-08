package view.game;

import constant.Icons;
import view.component.CharacterPanel;

/**
 * �÷��̾��� ĳ���͸� ���̴� �г��̴�.
 */
public class UserCharacterPanel extends CharacterPanel {

	public UserCharacterPanel() {
		super(Icons.USER_CHARACTER, 100, 0);
		setSize(100, 100);
	}
	
	/**
	 * @return ĳ������ ������ ���� ��ġ�� ��ȯ�Ѵ�.
	 */
	public int getRightX() {
		return getX() + getWidth();
	}
}