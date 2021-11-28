package view;

import javax.swing.JLabel;
import javax.swing.JPanel;

import constant.TextStyle;

/**
 * 상단 중앙에 제목을 가진 추상 패널이다. 레이아웃은 null로 지정되어 있다.
 */
public abstract class TitlePanel extends JPanel {

	public TitlePanel(String title) {
		setLayout(null);
		
		JLabel label = new JLabel(title);
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setFont(TextStyle.headline2);
		label.setLocation(300, 88);
		label.setSize(396, 100);
		add(label);
	}
	
}
