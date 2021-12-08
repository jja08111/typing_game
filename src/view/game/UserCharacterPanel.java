package view.game;

import constant.Icons;
import view.component.CharacterPanel;

/**
 * 플레이어의 캐릭터를 보이는 패널이다.
 */
public class UserCharacterPanel extends CharacterPanel {

	public UserCharacterPanel() {
		super(Icons.USER_CHARACTER, 100, 0);
		setSize(100, 100);
	}
	
	/**
	 * @return 캐릭터의 오른쪽 끝의 위치를 반환한다.
	 */
	public int getRightX() {
		return getX() + getWidth();
	}
}