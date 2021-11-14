package view;

import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 캐릭터를 나타내는 패널이다. 
 */
public class CharactorPanel extends JPanel {
	
	/**
	 * 아이콘으로 보이는 캐릭터 패널을 생성한다.
	 * @param imageIcon 캐릭터의 아이콘 
	 */
	public CharactorPanel(ImageIcon imageIcon) {
		JLabel imageLabel = new JLabel(imageIcon);
		imageLabel.setSize(60, 60);
		imageLabel.setLocation(0, 20);
		add(imageLabel);
	}
	
}
