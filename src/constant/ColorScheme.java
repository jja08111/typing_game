package constant;

import java.awt.Color;

/**
 * 앱의 포인트 색상들을 정의하는 클래스이다.
 */
public final class ColorScheme {
	
	/**
	 * 앱의 주 포인트 색상이다. 어두운 남색과 비슷하다.
	 */
	public static final Color primary = new Color(23, 63, 95);
	
	/**
	 * {@link ColorScheme#primary}보다 어두운 색상이다.
	 */
	public static final Color primaryVariant = new Color(0, 25, 53);
	
	/**
	 * 앱의 두 번째 포인트 색상이다. 오랜지색과 유사하다.
	 */
	public static final Color secondary = new Color(237, 85, 59);
	
	/**
	 * {@link ColorScheme#secondary}보다 어두운 색상이다.
	 */
	public static final Color secondaryVariant = new Color(180, 29, 17);
	
	/**
	 * {@link ColorScheme#primary} 위에 있는 컴포넌트의 색상이다. 예를 들어 JLabel이 있다.
	 */
	public static final Color onPrimary = Color.white;
	
	/**
	 * {@link ColorScheme#secondary} 위에 있는 컴포넌트의 색상이다. 예를 들어 JLabel이 있다.
	 */
	public static final Color onSecondary = Color.white;
}
