package view;

import java.awt.Container;

import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import constant.ColorScheme;
import constant.TextStyle;

/**
 * 텍스트 필드 모드와, 텍스트 편집이 불가능한 상태인 심플모드로 전환이 되는 객체이다. <br>
 * 심플모드로 전환시 스페이스바를 눌러 게임 시작이라는 텍스트가 고정된다.<br><br>
 * <b>{@link GamePanel}에서 리스너를 등록하여 사용중이다.</b>
 */
public class TypingField extends JTextField {
	
	public TypingField(int columns) {
		super(columns);
		setFont(TextStyle.headline5);
		setBorder(new LineBorder(ColorScheme.primary, 3));
		changeToReadyMode();
	}
	
	/**
	 * 현재 텍스트가 고정되어있는 대기모드인지 여부를 반환한다.
	 */
	public boolean isReadyMode() {
		return !isEditable();
	}
	
	/**
	 * 게임을 대기하는 모드로 전환하고 "스페이스바를 눌러 게임 시작" 문구를 텍스트에 넣는다.
	 */
	public void changeToReadyMode() {
		changeToReadyMode("스페이스바를 눌러 게임 시작");
	}
	
	/**
	 * 게임을 대기하는 모드로 전환하고 "스페이스바를 눌러 게임 시작" 문구를 텍스트에 넣는다.
	 */
	public void changeToReadyMode(String msg) {
		setText(msg);
		setEditable(false);
		
		Container parent = getParent();
		if (parent != null)
			parent.repaint();
	}
	
	/**
	 * 텍스트 필드 모드로 전환하고 텍스트를 빈칸으로 바꾼다.
	 */
	public void changeToTextFieldMode() {
		setEditable(true);
		setText("");
	}
	
}
