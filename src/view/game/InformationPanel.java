package view.game;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import constant.ColorScheme;
import constant.Icons;
import constant.TextStyle;
import handler.EnemyHandler;
import handler.RecordHandler;
import handler.SoundController;
import handler.Toast;
import model.RecordItem;
import model.Sounds;
import view.MainFrame;

/**
 * {@link GamePanel} �����ʿ� ��ġ�� �г��̴�. 
 * 
 * �̰����� ���� ����, ����, ���� �ܰ踦 �����ش�.
 */
public class InformationPanel extends JPanel {
	
	private static final int MAX_LIFE = 3;
	
	private static final int MAX_STAGE = 5;

	private final MainFrame mainFrame;
	
	private final JMenuItem stopItem;
	
	private int life;
	
	private int score;
	
	private int stage;

	private final CircleCountLabelPanel lifePanel = 
			new CircleCountLabelPanel("����", ColorScheme.SECONDARY, life, MAX_LIFE);
	
	private final ScoreLabelPanel scorePanel = new ScoreLabelPanel("����", score);
	
	private final CircleCountLabelPanel stagePanel = 
			new CircleCountLabelPanel("�ܰ�", ColorScheme.PRIMARY, stage, MAX_STAGE);
	
	private EnemyHandler enemyHandler;

	private final TypingField typingField;
	
	public InformationPanel(MainFrame mainFrame, TypingField typingField, JMenuItem stopItem) {
		this.mainFrame = mainFrame;
		this.typingField = typingField;
		this.stopItem = stopItem;
		setBackground(Color.white);
		setLayout(null);
		
		init();
		
		lifePanel.setLocation(34, 80);
		scorePanel.setLocation(34, 210);
		stagePanel.setLocation(34, 340);
		
		add(lifePanel);
		add(scorePanel);
		add(stagePanel);
	}
	
	public void initEnemyHandler(EnemyHandler enemyHandler) {
		this.enemyHandler = enemyHandler;
	}
	
	public void init() {
		life = 3;
		score = 0;
		stage = 1;
		
		lifePanel.updateGageCount(life);
		scorePanel.setScore(score);
		stagePanel.updateGageCount(stage);
	}
	
	public int getLife() {
		return life;
	}
	
	/**
	 * ������ �ϳ� �����Ѵ�.
	 */
	public void decreaseLife() {
		life -= 1;
		lifePanel.updateGageCount(life);
		
		if (life <= 0) {
			SoundController.play(Sounds.GAME_OVER);
			onGameEnd(Integer.toString(score) + "������ ���� ����!\n������ �̸��� �Է��ϼ���.", Icons.NORMAL_ENEMY, false);
		}
	}
	
	/**
	 * ������ ��� �����ϰ� ������ �����ȴ�.
	 */
	public void decreaseLifeToZero() {
		life = 0;
		lifePanel.updateGageCount(life);
		
		SoundController.play(Sounds.GAME_OVER);
		onGameEnd(Integer.toString(score) + "������ ���� ����!\n������ �̸��� �Է��ϼ���.", Icons.SPECIAL_ENEMY, false);
	}
	
	/**
	 * ������ 10�� �ø���.
	 */
	public void increaseScore() {
		score += 10;
		scorePanel.setScore(score);
	}
	
	/**
	 * ������ 5�� �����Ѵ�.
	 */
	public void decreaseScore() {
		score -= 5;
		if (score < 0) {
			score = 0;
		}
		scorePanel.setScore(score);
	}
	
	/**
	 * �ܰ踦 �ϳ� �ø���. ������ �ܰ迡�� �� �Լ��� �����ϸ� ���� Ŭ���� ������ ���´�.
	 */
	public void increaseStage() {
		stage += 1;
		if (stage > MAX_STAGE) {
			SoundController.play(Sounds.ALL_CLEAR);
			onGameEnd("�����մϴ�! " + Integer.toString(score) + "������ Ŭ���� �߽��ϴ�!\n������ �̸��� �Է��ϼ���.", Icons.USER_CHARACTER, true);
		} else {
			SoundController.play(Sounds.SUCCESS);
			stagePanel.updateGageCount(stage);
			new Thread() {
				@Override
				public void run() {
					typingField.changeToReadyMode("�����̽��ٸ� ���� ���� �ܰ� ����");					
				}
			}.start();
		}
	}
	
	public int getStage() {
		return stage;
	}
	
	/**
	 * ������ ��� �Ұų� ������ ��� �ذ��Ͽ� ������ ���� ��� �����ϴ� �Լ��̴�. 
	 * 
	 * ������ ����� ������ �Է¹޾� �����Ѵ�. �� �� �������� �ʱ�ȭ�Ѵ�.
	 * @param msg ����� ������ �� �Է��� �޽��� 
	 */
	private void onGameEnd(String msg, ImageIcon icon, boolean isAllClear) {
		assert (enemyHandler != null);
		
		typingField.changeToReadyMode();
		
		Thread th = new Thread() {
			@Override
			public void run() {
				enemyHandler.stopGenThread();
				enemyHandler.clear();
				stopItem.setEnabled(false);
				
				new GameEndDialog(mainFrame, msg, icon);
				
				init();
			}
		};
		th.start();
	}

	private class GameEndDialog extends JDialog {
		
		private final int stage;
		
		private final int score;
		
		private JTextField textField = new JTextField(20);
		
		public GameEndDialog(MainFrame mainFrame, String message, ImageIcon icon) {
			super(mainFrame, "���� ����", false);
			this.stage = InformationPanel.this.stage;
			this.score = InformationPanel.this.score;
			setSize(400, 180);
			setResizable(false);
			
			JSplitPane splitPane = new JSplitPane();
			splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			splitPane.setDividerLocation(120);
			splitPane.setEnabled(false);
			splitPane.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
			setContentPane(splitPane);
			
			JPanel rightPane = new JPanel(new BorderLayout());
			initRightPane(rightPane, message);
			
			splitPane.setLeftComponent(new JLabel(icon));
			splitPane.setRightComponent(rightPane);
			setVisible(true);
		}
		
		private void initRightPane(JPanel rightPane, String message) {
			JPanel northPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
			rightPane.add(northPane, BorderLayout.NORTH);
			
			// ���� �ٷ� �ѱ�� ���� HTML, CSS�� �̿��ߴ�.
			message = message.replace("\n", "<br>");
			JLabel label = new JLabel("<html><body><p style='width: 200px;'>" + message + "</p></body></html>");
			label.setFont(TextStyle.BODY_TEXT2);
			label.setSize(200, 40);
			northPane.add(label);

			JPanel southPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			rightPane.add(southPane, BorderLayout.SOUTH);
			
			JButton saveButton = new JButton("����");
			southPane.add(saveButton);
			saveButton.setSize(100, 20);
			saveButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					onClickedSaveButton();
				}
			});
			// �ؽ�Ʈ �ʵ尡 �� ĭ�� ���� ������ �� �� ����. 
			saveButton.setEnabled(false);
			
			JPanel centerPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
			rightPane.add(centerPane, BorderLayout.CENTER);
			
			centerPane.add(textField);
			textField.setPreferredSize(new Dimension(100, 20));
			textField.addKeyListener(new KeyAdapter() {
				@Override 
				public void keyTyped(KeyEvent e) {
					// ���ڿ� ���� �Է¸� ����Ѵ�.
					if (!Character.isLetterOrDigit(e.getKeyChar())) {
						e.consume();
						return;
					}
					
					// �ؽ�Ʈ �ʵ尡 �� ĭ�� ���� ������ �� �� ����. 
					if (!saveButton.isEnabled()) {
						saveButton.setEnabled(true);
					} else if (textField.getText().isBlank() && saveButton.isEnabled()) {
						saveButton.setEnabled(false);
					}
				}
			});
		}
		
		private void onClickedSaveButton() {
			// ������ �޼��� �ܰ�� ���� �ܰ��̴�.
			int clearedStage = stage - 1;
			if (clearedStage < 0) clearedStage = 0;
			
			try {
				RecordHandler.save(new RecordItem(textField.getText(), clearedStage, score));
			} catch (IOException exception) {
				Toast.show("��� ������ �����Ϸ��� ���߿� ���� ����� ������ �߻��߽��ϴ�. �ܼ� �α׸� Ȯ���غ�����.", 7000, InformationPanel.this);
				exception.printStackTrace();
			}
			GameEndDialog.this.dispose();
		}
		
	}
	
}

/**
 * ����� ������ ��Ÿ���� �г��̴�.
 */
class CircleGagePanel extends JPanel {
	
	private final int CIRCLE_DIAMETER = 20;
	
	private static final int HORIZONTAL_PADDING = 2;
	
	private Color color; 
	
	private int count;
	
	private int maxCount;
	
	public CircleGagePanel() {
		setBackground(Color.white);
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	/**
	 * ���� �� �ִ� ���� ������ �����Ѵ�.
	 */
	public void setCount(int count) {
		this.count = count;
	}
	
	/**
	 * ���� �� �ִ� ���� ������ �����ϰ� repaint �Ѵ�.
	 */
	public void updateCount(int count) {
		setCount(count);
		repaint();
	}
	
	public void setMaxScore(int maxScore) {
		this.maxCount = maxScore;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Component parent = getParent();
		final int parentWidth = parent.getWidth();
		// ���� �»���� �������� ������ ���Ѵ�.
		final int gap = (parentWidth - 2 * HORIZONTAL_PADDING - CIRCLE_DIAMETER) / (maxCount - 1);
				
		g.setColor(color);
		for (int i = 0; i < maxCount; ++i) {
			int x = i * gap + HORIZONTAL_PADDING;
			
			if (i + 1 > count) { // count ������ ĭ�� �� ĭ���� ĥ�Ѵ�.
				g.setColor(Color.lightGray);
				g.fillOval(x, 0, CIRCLE_DIAMETER, CIRCLE_DIAMETER);	
				g.setColor(color);
				g.drawOval(x, 0, CIRCLE_DIAMETER, CIRCLE_DIAMETER);	
			} else {
				g.fillOval(x, 0, CIRCLE_DIAMETER, CIRCLE_DIAMETER);	
			}
		}
		
	}
}

/**
 * ��ܿ� ���̺��� ������ �߾ӿ� ������ ī��Ʈ�� ���̴� �г��̴�.
 */
class CircleCountLabelPanel extends LabelPanel {
	
	private CircleGagePanel gage;
	
	/**
	 * @param title Ÿ��Ʋ�� ���� �ؽ�Ʈ�� �����Ѵ�.
	 * @param score ���� �̹����� � ä���� �ִ� �� ���Ѵ�.
	 * @param maxScore �ִ� �� ������ ä�� �� �ִ� �� ���Ѵ�.
	 */
	public CircleCountLabelPanel(String title, Color color, int score, int maxScore) {
		super(title, score);
		gage.setColor(color);
		gage.setCount(score);
		gage.setMaxScore(maxScore);
	}

	public void updateGageCount(int count) {
		gage.updateCount(count);
	}
	
	@Override
	public Component initBottomComponent(int count) {
		gage = new CircleGagePanel();
		return gage;
	}
	
}

/**
 * �߾ӿ� ������ �Ǿ��ִ� ������ �ΰ�, ���ʿ��� Ÿ��Ʋ�� ���� �г��̴�.
 */
class ScoreLabelPanel extends LabelPanel {
	
	protected JLabel scoreLabel;
	
	public ScoreLabelPanel(String title, int score) {
		super(title, score);
		setScore(score);
	}
	
	public void setScore(int score) {
		scoreLabel.setText(Integer.toString(score));
	}
	
	@Override
	public Component initBottomComponent(int score) {
		scoreLabel = new JLabel();
		scoreLabel.setText(Integer.toString(score));	
		scoreLabel.setHorizontalAlignment(JLabel.CENTER);
		scoreLabel.setFont(TextStyle.HEADLINE4);
		return scoreLabel;
	}
	
}

/**
 * ��ܿ� ���̺��� ������ �Ʒ��� ������ ���� �߻� �г��̴�. 
 * �ݵ�� {@link LabelPanel#initBottomComponent(int)}�� �����ؾ� �Ѵ�.
 */
abstract class LabelPanel extends JPanel {
	
	private JLabel titleLabel = new JLabel();

	private Component bottomComp;
	
	public LabelPanel(String title, int score) {
		setBackground(Color.white);
		setSize(120, 80);
		
		titleLabel.setText(title);
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		titleLabel.setForeground(Color.gray);
		// ��Ʈ ����� �����Ѵ�.
		titleLabel.setFont(TextStyle.HEADLINE6);
		
		setLayout(new BorderLayout(0, 12));
		add(titleLabel, BorderLayout.NORTH);
		bottomComp = initBottomComponent(score);
		add(bottomComp, BorderLayout.CENTER);
	}
	
	/**
	 * {@link LabelPanel} Ŭ������ ������ �г� �߾ӿ� ���� �� ������Ʈ�� �ʱ�ȭ�Ѵ�.
	 * @return �г� �߾ӿ� ������ ������Ʈ 
	 */
	public abstract Component initBottomComponent(int score);
}