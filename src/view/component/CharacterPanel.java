package view.component;

import java.awt.Point;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * ĳ���͸� ��Ÿ���� �г��̴�. 
 */
public class CharacterPanel extends JPanel {
	
	protected final JLabel imageLabel;
	
	/**
	 * ���������� ���̴� ĳ���� �г��� �����Ѵ�. �̹����� �ʺ�� ���̴� 60�̴�. ��� �е��� 20�̴�.
	 * @param imageIcon ĳ������ ������ 
	 */
	public CharacterPanel(ImageIcon imageIcon) {
		this(imageIcon, 60, 20);
	}
	
	/**
	 * ���������� ���̴� ĳ���� �г��� �����Ѵ�.
	 * @param imageIcon ĳ������ ������ 
	 * @param size �̹����� �ʺ�� ���� 
	 * @param topPadding ��ܺ��� ���� ũ��
	 */
	public CharacterPanel(ImageIcon imageIcon, int size, int topPadding) {
		this(imageIcon, size, size, topPadding);
	}
	
	/**
	 * ���������� ���̴� ĳ���� �г��� �����Ѵ�.
	 * @param imageIcon ĳ������ ������ 
	 * @param width �̹����� �ʺ�
	 * @param height �̹����� ���� 
	 * @param topPadding ��ܺ��� ���� ũ��
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