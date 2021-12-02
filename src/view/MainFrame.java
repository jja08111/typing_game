package view;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import constant.Icons;
import constant.TextStyle;
import handler.Navigator;
import view.game.GamePanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Point;

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
