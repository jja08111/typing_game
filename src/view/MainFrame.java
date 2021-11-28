package view;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import constant.TextStyle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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

	public void changeToIntroPanel() {
		changeTo(new IntroPanel(this));
	}
	
	public void changeToGamePanel() {
		changeTo(new GamePanel(this));
	}
	
	public void changeToRecordPanel() {
		changeTo(new RecordPanel(this));
	}

	private void changeTo(JComponent comp) {
		Container c = getContentPane();
		
		c.removeAll();
		c.add(comp);
		c.revalidate(); 
		c.repaint();
	}
	
	public static void main(String[] args) {
		new MainFrame();
	}
	
}

class IntroPanel extends DefaultPanel {
	
	public IntroPanel(MainFrame mainFrame) {
		super("타이핑 게임");
		
		JButton wordListButton = getButton("단어목록");
		JButton recordButton = getButton("기록");
		JButton startButton = getButton("게임시작");
		
		JPanel buttonBarPanel = new JPanel();
		buttonBarPanel.setSize(200, 300);
		buttonBarPanel.setLocation(400, 360);
		buttonBarPanel.setLayout(new FlowLayout());
		buttonBarPanel.add(wordListButton);
		buttonBarPanel.add(recordButton);
		buttonBarPanel.add(startButton);
		
		add(buttonBarPanel);
		
		recordButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.changeToRecordPanel();
			}
		});
		
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.changeToGamePanel();
			}
		});
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				startButton.requestFocus();
				removeComponentListener(this);
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

