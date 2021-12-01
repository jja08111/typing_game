package model;

public enum Sounds {
	/**
	 * 버튼을 클릭했을 때 재생할 소리이다.
	 */
	BUTTON_CLICK, 
	
	/**
	 * 메인 게임을 시작할 때 재생할 소리이다.
	 */
	GAME_START,
	
	/**
	 * 메인 게임에서 타이핑 할 때마다 재생할 소리이다.
	 */
	TYPING,
	
	/**
	 * 아이템이 아닌 적을 제거할 때 재생할 소리이다.
	 */
	KILL,
	
	/**
	 * 멈추는 아이템 적을 제거할 때 재생할 소리이다.
	 */
	KILL_STOP_ITEM,
	
	/**
	 * 랜덤으로 3개의 적을 제거하는 적을 제거했을 때 재생할 소리이다.
	 */
	KILL_BOMB_ITEM,
	
	/**
	 * {@link SpecialEnemy}가 생성되었을 때 재생할 소리이다.
	 */
	WARNING,
	
	/**
	 * 모든 단계를 해결한 경우 재생하는 소리이다.
	 */
	ALL_CLEAR,
};
