package view;

import constant.Icons;

public class UserCharactorPanel extends CharactorPanel {

	public UserCharactorPanel() {
		super(Icons.USER_CHARACTOR);
		setSize(100, 100);
	}
	
	/**
	 * @return 캐릭터의 오른쪽 끝의 위치를 반환한다.
	 */
	public int getRightX() {
		return getX() + getWidth();
	}
}
