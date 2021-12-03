package view.game;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;

import handler.EnemyHandler;
import handler.Navigator;
import handler.SoundController;
import model.Sounds;
import view.IntroPanel;
import view.MainFrame;

public class GamePanel extends JPanel {
	
	private final MainFrame mainFrame;
	
	private final JMenuItem stopItem = new JMenuItem("게임 취소");
	
	private final JMenuItem exitItem = new JMenuItem("메인으로 돌아가기");

	private final TypingField typingField = new TypingField(20);
	
	private final InformationPanel informationPanel;
	
	private final GameGroundPanel groundPanel = new GameGroundPanel();
	
	private final EnemyHandler enemyHandler;

	public GamePanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		informationPanel = new InformationPanel(mainFrame, typingField, stopItem);
		enemyHandler = new EnemyHandler(groundPanel, informationPanel);
		 
		initMainSplitPane();
		initMenuBar();
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				typingField.requestFocus();
				removeComponentListener(this);
			}
		});
	}
	
	/**
	 * JSplitPane을 생성하여 contentPane의 CENTER에 부착한다. 
	 * 왼쪽에는 {@link GameGroundPanel}과 {@link InputPanel}을 배치하고 오른쪽에는 {@link InformationPanel}을 배치한다.
	 */
	public void initMainSplitPane() {
		JSplitPane hPane = new JSplitPane();
		
		setLayout(new BorderLayout());
		
		add(hPane, BorderLayout.CENTER);
		
		hPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		hPane.setDividerLocation(800);
		hPane.setEnabled(false);
		
		JPanel vPane = new JPanel();
		vPane.setLayout(new BorderLayout());
		// Padding을 모든 방향에 8씩 부여한다.
		vPane.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		vPane.add(groundPanel, BorderLayout.CENTER);
		vPane.add(new InputPanel(), BorderLayout.SOUTH);
		
		hPane.setLeftComponent(vPane);
		hPane.setRightComponent(informationPanel);
		
		informationPanel.initEnemyHandler(enemyHandler);
	}
	
	public void initMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		mainFrame.setJMenuBar(menuBar);
		
		JMenu fileMenu = new JMenu("메뉴");
		fileMenu.add(stopItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);
		
		// 게임을 시작하지 않은 초기에는 비활성화한다.
		stopItem.setEnabled(false);
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
							stopItem.setEnabled(false);
						}
					}
				}.start();
			}
		});

		exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (typingField.isReadyMode()) {
					Navigator.to(GamePanel.this, new IntroPanel(mainFrame));
				} else {
					new Thread() {
						@Override
						public void run() {
							int result = JOptionPane.showConfirmDialog(null, "게임은 취소되며 진행중인 내용은 저장되지 않습니다.", "확인", JOptionPane.YES_NO_OPTION);
							if (result == JOptionPane.YES_OPTION) {
								enemyHandler.stopGenThread();
								enemyHandler.clear();
								Navigator.to(GamePanel.this, new IntroPanel(mainFrame));
							}
						}
					}.start();
				}
			}
		});
		
		menuBar.add(fileMenu);
	}
	
	private void startGame() {
		SoundController.play(Sounds.GAME_START);
		enemyHandler.startGenThread();
		typingField.changeToTextFieldMode();
	}
	
	private class InputPanel extends JPanel {
		
		public InputPanel() {
			setLayout(new FlowLayout());
			
			typingField.addKeyListener(new KeyAdapter() {
				@Override 
				public void keyTyped(KeyEvent e) {
					final char typedChar = e.getKeyChar();
					
					if (typedChar == ' ') {
						if (typingField.isReadyMode()) {
							startGame();
						}
						
						if (typingField.isReadyMode() && stopItem.isEnabled()) {
							stopItem.setEnabled(false);
						} else if (!typingField.isReadyMode() && !stopItem.isEnabled()) {
							stopItem.setEnabled(true);
						}
						
						// 공백입력을 무시한다.
						e.consume();
					}
				}
			});
			typingField.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JTextField t = (JTextField)e.getSource();
					String inputWord = t.getText();
					final boolean isReadyMode = typingField.isReadyMode();
					
					// 게임이 진행중인 경우
					if (!isReadyMode) {
						// 빈 칸일 때 엔터키를 입력한 경우 무시한다.
						if (typingField.getText().isEmpty()) return;
						// 텍스트 필드 내용을 비운다.
						t.setText("");
					}
					
					if (enemyHandler.kill(inputWord)) { // 사용자가 단어 맞추기 성공한 경우 
						informationPanel.increaseScore();
					} else { // 해당 단어가 없는 경우 
						if (!isReadyMode) {
							informationPanel.decreaseScore();
							SoundController.play(Sounds.WRONG);
						}
					}
				}
			});
			add(typingField);
		}
	}
	
}
