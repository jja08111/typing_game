package view.game;

import java.awt.Container;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import constant.ColorScheme;
import constant.TextStyle;
import handler.SoundController;
import model.Sounds;
import view.WordEditPanel;

/**
 * �ؽ�Ʈ �ʵ� ����, �ؽ�Ʈ ������ �Ұ����� ������ ������ ��ȯ�� �Ǵ� ��ü�̴�. <br>
 * ���ø��� ��ȯ�� �����̽��ٸ� ���� ���� �����̶�� �ؽ�Ʈ�� �����ȴ�.<br><br>
 * <b>{@link GamePanel}���� �����ʸ� ����Ͽ� ������̴�.</b>
 */
public class TypingField extends JTextField {
	
	/**
	 * 30�� �̻� �Է��� ���� ���ϰ� ����Ű�� �����̽��ٴ� �Է����� ���ϴ� �ؽ�Ʈ �ʵ带 �����Ѵ�.
	 * @param columns �ؽ�Ʈ �ʵ��� �ʺ� ������ ����
	 */
	public TypingField(int columns) {
		super(columns);
		setFont(TextStyle.HEADLINE5);
		setBorder(new LineBorder(ColorScheme.PRIMARY, 3));
		changeToReadyMode();
		
		addKeyListener(new KeyAdapter() {
			@Override 
			public void keyTyped(KeyEvent e) {
				final char typedChar = e.getKeyChar();
				
				// 30�� �̻� �Է��� �����Ѵ�.
				if (getText().length() > WordEditPanel.MAX_WORD_LENGTH) {
					e.consume();
					return;
				}
				if (!isReadyMode() && typedChar != ' ' && typedChar != '\n') {
					SoundController.play(Sounds.TYPING);
				}
			}
		});
	}
	
	/**
	 * ���� �ؽ�Ʈ�� �����Ǿ��ִ� ��������� ���θ� ��ȯ�Ѵ�.
	 */
	public boolean isReadyMode() {
		return !isEditable();
	}
	
	/**
	 * ������ ����ϴ� ���� ��ȯ�ϰ� "�����̽��ٸ� ���� ���� ����" ������ �ؽ�Ʈ�� �ִ´�.
	 */
	public void changeToReadyMode() {
		changeToReadyMode("�����̽��ٸ� ���� ���� ����");
	}
	
	/**
	 * ������ ����ϴ� ���� ��ȯ�ϰ� ���޵� ���ڿ��� �ؽ�Ʈ�� �ִ´�.
	 */
	public void changeToReadyMode(String msg) {
		setText(msg);
		setEditable(false);
		
		Container parent = getParent();
		if (parent != null)
			parent.repaint();
	}
	
	/**
	 * �ؽ�Ʈ �ʵ� ���� ��ȯ�ϰ� �ؽ�Ʈ�� ��ĭ���� �ٲ۴�.
	 */
	public void changeToTextFieldMode() {
		setEditable(true);
		setText("");
	}
	
}