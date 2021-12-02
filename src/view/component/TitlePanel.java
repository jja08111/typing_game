package view.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import constant.Icons;
import constant.TextStyle;
import handler.Navigator;
import view.IntroPanel;
import view.MainFrame;

/**
 * 상단 중앙에 제목을 가진 추상 패널이다. 레이아웃은 null로 지정되어 있다. 
 * 뒤로가기 버튼이 있으며 이 버튼을 누르면 {@link IntroPanel}로 이동한다.
 */
public abstract class TitlePanel extends JPanel {

	private static final int BACK_BUTTON_SIZE = 32;
	
	public TitlePanel(MainFrame mainFrame, String title, boolean hasBackButton) {
		this(mainFrame, title, new Point(300, 16), hasBackButton);
	}
	
	public TitlePanel(MainFrame mainFrame, String title, Point titleLocation, boolean hasBackButton) {
		setLayout(null);
		
		JLabel label = new JLabel(title);
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setFont(TextStyle.HEADLINE2);
		label.setLocation(titleLocation);
		label.setSize(396, 100);
		add(label);
		
		if (hasBackButton) {
			DefaultButton backButton = new DefaultButton(
					new ImageIcon(Icons.BACK_BUTTON.getImage().getScaledInstance(BACK_BUTTON_SIZE, BACK_BUTTON_SIZE, Image.SCALE_DEFAULT)));
			backButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Navigator.to(TitlePanel.this, new IntroPanel(mainFrame));
				}
			});
			backButton.setSize(BACK_BUTTON_SIZE, BACK_BUTTON_SIZE);
			backButton.setLocation(32, 32);
			backButton.setOpaque(false);
			backButton.setBorderPainted(false);
			add(backButton);
		}
		
	}
	
}
