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
import handler.EnemyHandler;

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

	private final JMenuItem restartItem = new JMenuItem("재시작");
	
	private final JMenuItem stopItem = new JMenuItem("게임취소");
	
	private final JMenuItem exitItem = new JMenuItem("종료");

	private final InformationPanel informationPanel = new InformationPanel();
	
	private final GameGroundPanel groundPanel = new GameGroundPanel();
	
	private final EnemyHandler enemyHandler = new EnemyHandler(groundPanel, informationPanel);
	
	private final GamePanel gamePanel = new GamePanel(informationPanel, groundPanel, enemyHandler);
	
	public GameFrame() {
		setTitle("타이핑 게임");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		
		initMainSplitPane();
		initMenuBar();
		
		setResizable(false);
		setVisible(true);
	}

	/**
	 * JSplitPane을 생성하여 contentPane의 CENTER에 부착한다. 
	 * 왼쪽에는 {@link GamePanel}을 배치하고 오른쪽에는 {@link InformationPanel}을 배치한다.
	 */
	private void initMainSplitPane() {
		JSplitPane hPane = new JSplitPane();
		getContentPane().add(hPane, BorderLayout.CENTER);
		hPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		hPane.setDividerLocation(800);
		hPane.setEnabled(false);
		
		hPane.setLeftComponent(gamePanel);
		hPane.setRightComponent(informationPanel);
		
		informationPanel.initEnemyHandler(enemyHandler);
	}
	
	private void initMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu fileMenu = new JMenu("메뉴");
		fileMenu.add(restartItem);
		fileMenu.add(stopItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);
		
		menuBar.add(fileMenu);
	}
	
	private class StartAction implements ActionListener {
		@Override 
		public void actionPerformed(ActionEvent e) {
			gamePanel.startGame();
		}
	}
	
}
