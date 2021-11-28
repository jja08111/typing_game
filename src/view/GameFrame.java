package view;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import handler.EnemyHandler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Container;

public class GameFrame extends JFrame {
	
	/**
	 * 프레임의 폭이다. 
	 */
	public static final int WIDTH = 1000;
	
	/**
	 * 프레임의 높이이다.
	 */
	public static final int HEIGHT = 600;

	private final JMenuItem stopItem = new JMenuItem("게임 취소");
	
	private final JMenuItem exitItem = new JMenuItem("메인으로 돌아가기");

	private final TypingField typingField = new TypingField(20);
	
	private final InformationPanel informationPanel = new InformationPanel(typingField);
	
	private final GameGroundPanel groundPanel = new GameGroundPanel();
	
	private final EnemyHandler enemyHandler = new EnemyHandler(groundPanel, informationPanel);
	
	private final GamePanel gamePanel = new GamePanel(typingField, informationPanel, groundPanel, enemyHandler);
	
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
		
		Container c = getContentPane();
		c.add(hPane, BorderLayout.CENTER);
		
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
		fileMenu.add(stopItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);
		
		stopItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					@Override
					public void run() {
						int result = JOptionPane.showConfirmDialog(null, "게임을 취소하며 진행중인 내용은 저장되지 않습니다.", "확인", JOptionPane.YES_NO_OPTION);
						if (result == JOptionPane.YES_OPTION) {
							enemyHandler.stopGenThread();
							enemyHandler.clear();
							informationPanel.init();
							typingField.changeToReadyMode();
						}
					}
				}.start();
			}
		
		});
		
		menuBar.add(fileMenu);
	}
	
}
