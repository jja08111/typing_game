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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import handler.EnemyHandler;
import handler.Navigator;
import handler.SoundController;
import model.Sounds;
import view.IntroPanel;
import view.MainFrame;

/**
 * ���� ������ �����ϴ� �������� ���̴� �г��̴�.
 */
public class GamePanel extends JPanel {
	
	private final MainFrame mainFrame;
	
	private final JMenuItem stopItem = new JMenuItem("���� ���");
	
	private final JMenuItem exitItem = new JMenuItem("�������� ���ư���");

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
	 * JSplitPane�� �����Ͽ� contentPane�� CENTER�� �����Ѵ�. 
	 * ���ʿ��� {@link GameGroundPanel}�� {@link InputPanel}�� ��ġ�ϰ� �����ʿ��� {@link InformationPanel}�� ��ġ�Ѵ�.
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
		// Padding�� ��� ���⿡ 8�� �ο��Ѵ�.
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
		
		JMenu fileMenu = new JMenu("�޴�");
		fileMenu.add(stopItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);
		
		// ������ �������� ���� �ʱ⿡�� ��Ȱ��ȭ�Ѵ�.
		stopItem.setEnabled(false);
		stopItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					@Override
					public void run() {
						int result = JOptionPane.showConfirmDialog(null, "������ ����ϸ� �������� ������ ������� �ʽ��ϴ�.", "Ȯ��", JOptionPane.YES_NO_OPTION);
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
							int result = JOptionPane.showConfirmDialog(null, "������ ��ҵǸ� �������� ������ ������� �ʽ��ϴ�.", "Ȯ��", JOptionPane.YES_NO_OPTION);
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
						
						// �����Է��� �����Ѵ�.
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
					
					// ������ �������� ���
					if (!isReadyMode) {
						// �� ĭ�� �� ����Ű�� �Է��� ��� �����Ѵ�.
						if (typingField.getText().isEmpty()) return;
						// �ؽ�Ʈ �ʵ� ������ ����.
						t.setText("");
					}
					
					if (enemyHandler.kill(inputWord)) { // ����ڰ� �ܾ� ���߱� ������ ��� 
						informationPanel.increaseScore();
					} else { // �ش� �ܾ ���� ��� 
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