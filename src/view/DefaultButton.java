package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

import constant.ColorScheme;
import handler.SoundController;
import model.Sounds;

/**
 * 버튼 클릭 효과음을 가졌으며 {@link JButton}을 상속받은 버튼이다.
 */
public class DefaultButton extends JButton {

	public DefaultButton(String label) {
		super(label);
		
		setForeground(ColorScheme.PRIMARY_VARIANT);
		addSoundPlayingToActionListener();
	}
	
	public DefaultButton(ImageIcon icon) {
		super(icon);
		addSoundPlayingToActionListener();
	}
	
	private void addSoundPlayingToActionListener() {
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundController.play(Sounds.BUTTON_CLICK);
			}
		});
	}
	
}
