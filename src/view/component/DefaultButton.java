package view.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import constant.ColorScheme;
import handler.SoundController;
import model.Sounds;

/**
 * ��ư Ŭ�� ȿ������ �������� {@link JButton}�� ��ӹ��� ��ư�̴�.
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