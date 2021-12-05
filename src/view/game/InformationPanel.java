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
 * {@link MainFrame} 오른쪽에 위치할 패널이다. 
 * 
 * 이곳에서 남은 생명, 점수, 현재 단계를 보여준다.
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
			new CircleCountLabelPanel("생명", ColorScheme.SECONDARY, life, MAX_LIFE);
	
	private final ScoreLabelPanel scorePanel = new ScoreLabelPanel("점수", score);
	
	private final CircleCountLabelPanel stagePanel = 
			new CircleCountLabelPanel("단계", ColorScheme.PRIMARY, stage, MAX_STAGE);
	
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
	 * 생명을 하나 감소한다.
	 */
	public void decreaseLife() {
		life -= 1;
		lifePanel.updateGageCount(life);
		
		if (life <= 0) {
			SoundController.play(Sounds.GAME_OVER);
			onGameEnd(Integer.toString(score) + "점으로 게임 오버!\n저장할 이름을 입력하세요.", Icons.NORMAL_ENEMY, false);
		}
	}
	
	/**
	 * 생명을 모두 감소하고 게임은 오버된다.
	 */
	public void decreaseLifeToZero() {
		life = 0;
		lifePanel.updateGageCount(life);
		
		SoundController.play(Sounds.GAME_OVER);
		onGameEnd(Integer.toString(score) + "점으로 게임 오버!\n저장할 이름을 입력하세요.", Icons.SPECIAL_ENEMY, false);
	}
	
	/**
	 * 점수를 10점 올린다.
	 */
	public void increaseScore() {
		score += 10;
		scorePanel.setScore(score);
	}
	
	/**
	 * 점수를 5점 감소한다.
	 */
	public void decreaseScore() {
		score -= 5;
		if (score < 0) {
			score = 0;
		}
		scorePanel.setScore(score);
	}
	
	/**
	 * 단계를 하나 올린다. 마지막 단계에서 이 함수를 실행하면 게임 클리어 엔딩이 나온다.
	 */
	public void increaseStage() {
		stage += 1;
		if (stage > MAX_STAGE) {
			SoundController.play(Sounds.ALL_CLEAR);
			onGameEnd("축하합니다! " + Integer.toString(score) + "점으로 클리어 했습니다!\n저장할 이름을 입력하세요.", Icons.USER_CHARACTER, true);
		} else {
			SoundController.play(Sounds.SUCCESS);
			stagePanel.updateGageCount(stage);
			new Thread() {
				@Override
				public void run() {
					typingField.changeToReadyMode("스페이스바를 눌러 다음 단계 시작");					
				}
			}.start();
		}
	}
	
	public int getStage() {
		return stage;
	}
	
	/**
	 * 생명을 모두 잃거나 게임을 모두 해결하여 게임이 끝난 경우 실행하는 함수이다. 
	 * 
	 * 적들을 지우고 점수를 입력받아 저장한다. 그 후 점수들을 초기화한다.
	 * @param msg 기록을 저장할 때 입력할 메시지 
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
		
		private JTextField textField = new JTextField(20);
		
		public GameEndDialog(MainFrame mainFrame, String message, ImageIcon icon) {
			super(mainFrame, "게임 종료", false);
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
			
			// 다음 줄로 넘기기 위해 HTML, CSS를 이용했다.
			message = message.replace("\n", "<br>");
			JLabel label = new JLabel("<html><body><p style='width: 200px;'>" + message + "</p></body></html>");
			label.setFont(TextStyle.BODY_TEXT2);
			label.setSize(200, 40);
			northPane.add(label);

			JPanel southPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			rightPane.add(southPane, BorderLayout.SOUTH);
			
			JButton saveButton = new JButton("저장");
			southPane.add(saveButton);
			saveButton.setSize(100, 20);
			saveButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					onClickedSaveButton();
				}
			});
			// 텍스트 필드가 빈 칸일 때는 저장을 할 수 없다. 
			saveButton.setEnabled(false);
			
			JPanel centerPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
			rightPane.add(centerPane, BorderLayout.CENTER);
			
			centerPane.add(textField);
			textField.setPreferredSize(new Dimension(100, 20));
			textField.addKeyListener(new KeyAdapter() {
				@Override 
				public void keyTyped(KeyEvent e) {
					// 문자와 숫자 입력만 허용한다.
					if (!Character.isLetterOrDigit(e.getKeyChar())) {
						e.consume();
						return;
					}
					
					// 텍스트 필드가 빈 칸일 때는 저장을 할 수 없다. 
					if (!saveButton.isEnabled()) {
						saveButton.setEnabled(true);
					} else if (textField.getText().isBlank() && saveButton.isEnabled()) {
						saveButton.setEnabled(false);
					}
				}
			});
		}
		
		private void onClickedSaveButton() {
			// 실제로 달성한 단계는 이전 단계이다.
			int clearedStage = stage - 1;
			if (clearedStage < 0) clearedStage = 0;
			
			try {
				RecordHandler.save(new RecordItem(textField.getText(), clearedStage, score));
			} catch (IOException exception) {
				Toast.show("기록 파일을 수정하려는 도중에 파일 입출력 에러가 발생했습니다. 콘솔 로그를 확인해보세요.", 7000, InformationPanel.this);
				exception.printStackTrace();
			}
			GameEndDialog.this.dispose();
		}
		
	}
	
}

/**
 * 원들로 점수를 나타내는 패널이다.
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
	 * 현재 차 있는 원의 갯수를 설정한다.
	 */
	public void setCount(int count) {
		this.count = count;
	}
	
	/**
	 * 현재 차 있는 원의 갯수를 설정하고 repaint 한다.
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
		// 원의 좌상단을 기준으로 간격을 정한다.
		final int gap = (parentWidth - 2 * HORIZONTAL_PADDING - CIRCLE_DIAMETER) / (maxCount - 1);
				
		g.setColor(color);
		for (int i = 0; i < maxCount; ++i) {
			int x = i * gap + HORIZONTAL_PADDING;
			
			if (i + 1 > count) { // count 이후의 칸은 빈 칸으로 칠한다.
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
 * 상단에 레이블을 가지고 중앙에 원으로 카운트를 보이는 패널이다.
 */
class CircleCountLabelPanel extends LabelPanel {
	
	private CircleGagePanel gage;
	
	/**
	 * @param title 타이틀로 보일 텍스트를 지정한다.
	 * @param score 원형 이미지가 몇개 채워져 있는 지 정한다.
	 * @param maxScore 최대 몇 개까지 채울 수 있는 지 정한다.
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
 * 중앙에 정수로 되어있는 점수를 두고, 위쪽에는 타이틀을 가진 패널이다.
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
 * 상단에 레이블을 가지고 아래에 점수를 갖는 추상 패널이다. 
 * 반드시 {@link LabelPanel#initBottomComponent(int)}를 구현해야 한다.
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
		// 폰트 사이즈를 변경한다.
		titleLabel.setFont(TextStyle.HEADLINE6);
		
		setLayout(new BorderLayout(0, 12));
		add(titleLabel, BorderLayout.NORTH);
		bottomComp = initBottomComponent(score);
		add(bottomComp, BorderLayout.CENTER);
	}
	
	/**
	 * {@link LabelPanel} 클래스를 생성시 패널 중앙에 부착 할 컴포넌트를 초기화한다.
	 * @return 패널 중앙에 부착할 컴포넌트 
	 */
	public abstract Component initBottomComponent(int score);
}
