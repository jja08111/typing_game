package view;

import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import constant.ColorScheme;
import constant.TextStyle;

/**
 * 텍스트 필드 모드와, 텍스트 편집이 불가능한 상태인 심플모드로 전환이 되는 객체이다. <br>
 * 심플모드로 전환시 스페이스바를 눌러 게임 시작이라는 텍스트가 고정된다.
 */
public class TypingField extends JTextField {
	
	public TypingField(int columns) {
		super(columns);
		setFont(TextStyle.headline5);
		setBorder(new LineBorder(ColorScheme.primary, 3));
		changeToSimpleMode();
	}
	
	/**
	 * 현재 텍스트가 고정되어있는 심플모드인지 여부를 반환한다.
	 */
	public boolean getIsSimpleMode() {
		return !isEditable();
	}
	
	/**
	 * 텍스트가 고정되어 수정이 불가능한 심플모드로 전환하고 "스페이스바를 눌러 게임 시작" 
	 * 문구를 텍스트에 넣는다.
	 */
	public void changeToSimpleMode() {
		setEditable(false);
		setText("스페이스바를 눌러 게임 시작");
	}
	
	/**
	 * 텍스트 필드 모드로 전환하고 텍스트를 빈칸으로 바꾼다.
	 */
	public void changeToTextFieldMode() {
		setEditable(true);
		setText("");
	}
	
}
