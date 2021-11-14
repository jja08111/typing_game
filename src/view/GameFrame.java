package view;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

import constant.Icons;

import java.awt.BorderLayout;

public class GameFrame extends JFrame {
	
	/**
	 * 프레임의 폭이다. 
	 */
	public static final int WIDTH = 1000;
	
	/**
	 * 프레임의 높이이다.
	 */
	public static final int HEIGHT = 600;
	
	// TODO: 주석 달기
	
	private JMenuItem startItem = new JMenuItem("Start");
	
	private JMenuItem stopItem = new JMenuItem("Stop");
	
	private JMenuItem exitItem = new JMenuItem("Exit");
	
	private JButton startBtn = new JButton(Icons.normal);
	
	private JButton stopBtn = new JButton("Stop");
	
	private InformationPanel informationPanel = new InformationPanel();
	
	private EditPanel editPanel = new EditPanel();
	
	private GamePanel gamePanel = new GamePanel(informationPanel, editPanel);

	
	public GameFrame() {
		setTitle("타이핑 게임");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		
		initSplitPane();
		initToolBar();
		initMenuBar();
		
		setResizable(false);
		setVisible(true);
	}

	/**
	 * JSplitPane을 생성하여 contentPane의 CENTER에 부착한다. 
	 * 왼쪽에는 {@link GamePanel}을 배치하고 오른쪽에는 {@link InformationPanel}을 배치한다.
	 */
	private void initSplitPane() {
		JSplitPane hPane = new JSplitPane();
		getContentPane().add(hPane, BorderLayout.CENTER);
		hPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		hPane.setDividerLocation(800);
		hPane.setEnabled(false);
		
		hPane.setLeftComponent(gamePanel);
		hPane.setRightComponent(informationPanel);
	}
	
	private void initMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu fileMenu = new JMenu("Game");
		fileMenu.add(startItem);
		fileMenu.add(stopItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);
		
		menuBar.add(fileMenu);
		
		startItem.addActionListener(new StartAction());
	}
	
	private void initToolBar() {
		JToolBar toolBar = new JToolBar();
		toolBar.add(startBtn);
		toolBar.add(stopBtn);
		
		getContentPane().add(toolBar, BorderLayout.NORTH);
		
		startBtn.addActionListener(new StartAction());
		startBtn.setPressedIcon(Icons.pressed);
		startBtn.setRolloverIcon(Icons.over);
	}
	
	private class StartAction implements ActionListener {
		@Override 
		public void actionPerformed(ActionEvent e) {
			gamePanel.startGame();
		}
	}
}
