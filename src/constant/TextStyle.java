package constant;

import java.awt.Font;

/**
 * 모든 뷰에서 사용할 텍스트 스타일이다. 여기에 있는 스타일만 사용된다.
 * https://material.io/design/typography/the-type-system.html#type-scale 에서 생성한 스타일을 참고했다.
 */
public final class TextStyle {
	
	public static final Font HEADLINE1 = new Font("SansSerif", Font.PLAIN, 96);
	
	public static final Font HEADLINE2 = new Font("SansSerif", Font.PLAIN, 60);
	
	public static final Font HEADLINE3 = new Font("SansSerif", Font.PLAIN, 48);
	
	public static final Font HEADLINE4 = new Font("SansSerif", Font.PLAIN, 34);
	
	public static final Font HEADLINE5 = new Font("SansSerif", Font.PLAIN, 24);
	
	public static final Font HEADLINE6 = new Font("SansSerif", Font.PLAIN, 20);
	
	public static final Font BODY_TEXT1 = new Font("SansSerif", Font.PLAIN, 16);
	
	public static final Font BODY_TEXT2 = new Font("SansSerif", Font.PLAIN, 14);
	
	public static final Font BUTTON = new Font("SansSerif", Font.BOLD, 14);
	
}