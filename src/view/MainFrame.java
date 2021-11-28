package view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import constant.TextStyle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;

public class MainFrame extends JFrame {
	
	/**
	 * 프레임의 폭이다. 
	 */
	public static final int WIDTH = 1000;
	
	/**
	 * 프레임의 높이이다.
	 */
	public static final int HEIGHT = 600;
	
	public MainFrame() {
		setTitle("타이핑 게임");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		
		getContentPane().add(new IntroPanel(this));
		
		setResizable(false);
		setVisible(true);
	}

	public void changeToGamePanel() {
		Container c = getContentPane();
		
		c.removeAll();
		c.add(new GamePanel(this));
		c.revalidate(); 
		c.repaint();
	}
	
}

class IntroPanel extends TitlePanel {

	private JButton startButton = getButton("게임시작");
	
	public IntroPanel(MainFrame gameFrame) {
		super("타이핑 게임");
		
		JPanel buttonBarPanel = new JPanel();
		buttonBarPanel.setSize(200, 300);
		buttonBarPanel.setLocation(400, 360);
		buttonBarPanel.setLayout(new FlowLayout());
		buttonBarPanel.add(getButton("단어목록"));
		buttonBarPanel.add(getButton("기록"));
		buttonBarPanel.add(startButton);
		
		add(buttonBarPanel);
		
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameFrame.changeToGamePanel();
			}
		});
	}
	
	private JButton getButton(String label) {
		JButton result = new JButton(label);
		result.setFont(TextStyle.button);
		result.setPreferredSize(new Dimension(200, 40));
		return result;
	}
	
}

