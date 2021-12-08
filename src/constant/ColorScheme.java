package constant;

import java.awt.Color;

/**
 * 앱의 포인트 색상들을 정의하는 클래스이다.
 */
public final class ColorScheme {
	
	/**
	 * 앱의 주 포인트 색상이다. 어두운 남색과 비슷하다.
	 */
	public static final Color PRIMARY = new Color(23, 63, 95);
	
	/**
	 * {@link ColorScheme#PRIMARY}보다 어두운 색상이다.
	 */
	public static final Color PRIMARY_VARIANT = new Color(0, 25, 53);
	
	/**
	 * 앱의 두 번째 포인트 색상이다. 오랜지색과 유사하다.
	 */
	public static final Color SECONDARY = new Color(237, 85, 59);
	
	/**
	 * {@link ColorScheme#SECONDARY}보다 어두운 색상이다.
	 */
	public static final Color SECONDARY_VARIANT = new Color(180, 29, 17);
	
	/**
	 * {@link ColorScheme#PRIMARY} 위에 있는 컴포넌트의 색상이다. 예를 들어 JLabel이 있다.
	 */
	public static final Color ON_PRIMARY = Color.white;
	
	/**
	 * {@link ColorScheme#SECONDARY} 위에 있는 컴포넌트의 색상이다. 예를 들어 JLabel이 있다.
	 */
	public static final Color ON_SECONDARY = Color.white;
	
	/**
	 * 죽으면 멈추거나 적이 3개 사라지는 적 객체의 텍스트 배경 색상으로 이용한다.
	 */
	public static final Color ITEM_ENEMY_BACKGROUND = new Color(24, 68, 64);
	
}