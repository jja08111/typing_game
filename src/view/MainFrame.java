package view;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import constant.TextStyle;
import handler.Navigator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
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

class IntroPanel extends TitlePanel {
	
	public IntroPanel(MainFrame mainFrame) {
		super(mainFrame, "타이핑 게임", new Point(300, 112), false);
		mainFrame.setJMenuBar(null);
		
		JButton wordEditButton = getButton("단어편집");
		JButton recordButton = getButton("기록");
		JButton startButton = getButton("게임시작");
		
		JPanel buttonBarPanel = new JPanel();
		buttonBarPanel.setSize(200, 300);
		buttonBarPanel.setLocation(400, 360);
		buttonBarPanel.setLayout(new FlowLayout());
		buttonBarPanel.add(wordEditButton);
		buttonBarPanel.add(recordButton);
		buttonBarPanel.add(startButton);
		
		add(buttonBarPanel);
		
		wordEditButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Navigator.to(IntroPanel.this, new WordEditPanel(mainFrame));
			}
		});
		recordButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Navigator.to(IntroPanel.this, new RecordPanel(mainFrame));
			}
		});
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Navigator.to(IntroPanel.this, new GamePanel(mainFrame));
			}
		});
		// 인트로 화면 진입시 시작 버튼에 포커스가 가도록 한다.
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
		result.setFont(TextStyle.BUTTON);
		result.setPreferredSize(new Dimension(200, 40));
		return result;
	}
	
}

