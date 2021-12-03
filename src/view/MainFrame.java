package view;

import javax.swing.JFrame;

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
	
	public static void main(String[] args) {
		new MainFrame();
	}
	
}
