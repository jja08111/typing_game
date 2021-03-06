package view.component;

import java.awt.Point;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 캐릭터를 나타내는 패널이다. 
 */
public class CharacterPanel extends JPanel {
	
	protected final JLabel imageLabel;
	
	/**
	 * 아이콘으로 보이는 캐릭터 패널을 생성한다. 이미지의 너비와 높이는 60이다. 상단 패딩은 20이다.
	 * @param imageIcon 캐릭터의 아이콘 
	 */
	public CharacterPanel(ImageIcon imageIcon) {
		this(imageIcon, 60, 20);
	}
	
	/**
	 * 아이콘으로 보이는 캐릭터 패널을 생성한다.
	 * @param imageIcon 캐릭터의 아이콘 
	 * @param size 이미지의 너비와 높이 
	 * @param topPadding 상단부의 여백 크기
	 */
	public CharacterPanel(ImageIcon imageIcon, int size, int topPadding) {
		this(imageIcon, size, size, topPadding);
	}
	
	/**
	 * 아이콘으로 보이는 캐릭터 패널을 생성한다.
	 * @param imageIcon 캐릭터의 아이콘 
	 * @param width 이미지의 너비
	 * @param height 이미지의 높이 
	 * @param topPadding 상단부의 여백 크기
	 */
	public CharacterPanel(ImageIcon imageIcon, int width, int height, int topPadding) {
		this.imageLabel = new JLabel();
		imageLabel.setIcon(new ImageIcon(imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT)));
		imageLabel.setSize(width, height);
		imageLabel.setLocation(0, topPadding);
		add(imageLabel);
		
		setOpaque(false);
	}
	
}