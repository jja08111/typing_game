package view;

import javax.swing.JFrame;

public class MainFrame extends JFrame {
	
	public MainFrame() {
		setTitle("Ÿ���� ��Ʈ");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 600);
		
		getContentPane().add(new IntroPanel(this));
		
		setResizable(false);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new MainFrame();
	}
	
}
