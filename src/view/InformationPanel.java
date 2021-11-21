package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import constant.ColorScheme;
import constant.TextStyle;
import handler.EnemyHandler;
import handler.RecordHandler;
import model.RecordItem;

/**
 * {@link GameFrame} 오른쪽에 위치할 패널이다. 
 * 
 * 이곳에서 남은 생명, 점수, 현재 단계를 보여준다.
 */
public class InformationPanel extends JPanel {
	
	private static final int MAX_LIFE = 3;
	
	private static final int MAX_STAGE = 3;
	
	private int life;
	
	private int score;
	
	private int stage;

	private final CircleCountLabelPanel lifePanel = 
			new CircleCountLabelPanel("생명", ColorScheme.primary, life, MAX_LIFE);
	
	private final ScoreLabelPanel scorePanel = new ScoreLabelPanel("점수", score);
	
	private final CircleCountLabelPanel stagePanel = 
			new CircleCountLabelPanel("단계", ColorScheme.secondary, stage, MAX_STAGE);
	
	private EnemyHandler enemyHandler;
	
	private final RecordHandler recordHandler = RecordHandler.getInstance();
	
	private final TypingField typingField;
	
	public InformationPanel(TypingField typingField) {
		this.typingField = typingField;
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
	
	private void init() {
		life = 3;
		score = 0;
		stage = 1;
		
		lifePanel.updateGageCount(life);
		scorePanel.setScore(score);
		stagePanel.updateGageCount(stage);
	}
	
	/**
	 * 생명을 하나 감소한다.
	 */
	public void decreaseLife() {
		life -= 1;
		lifePanel.updateGageCount(life);
		
		if (life <= 0) {
			onEndGame(Integer.toString(score) + "점으로 게임 오버!\n저장할 이름을 입력하세요.", false);
		}
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
			onEndGame("축하합니다! " + Integer.toString(score) + "점으로 클리어 했습니다!\n저장할 이름을 입력하세요.", true);
		} else {
			stagePanel.updateGageCount(stage);
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
	private void onEndGame(String msg, boolean isAllClear) {
		assert (enemyHandler != null);
		
		typingField.changeToSimpleMode();
		
		enemyHandler.stopGenThread();
		enemyHandler.stopAllEnemies();
		
		String name = JOptionPane.showInputDialog(msg);
		// 실패하여 게임이 끝난 경우 달성한 단계는 이전 단계이다.
		int clearedStage = stage + (isAllClear ? 0 : -1);
		if (clearedStage < 0) clearedStage = 0;
		
		if (name != null) {
			recordHandler.save(new RecordItem(name, clearedStage, score));
		}
		
		// 스레드를 나중에 종료하는 이유는 enemyPanel의 스레드에서 게임 오버를 감지하여 이 함수에 도달 하기 때문이다.
		// 즉, 스레드를 다이어로그가 나오기 전에 종료하면 스레드가 종료되며 다이어로그가 바로 사라지기 때문이다.
		enemyHandler.clear();
		init();
	}
	
}

/**
 * 원들로 점수를 나타내는 패널이다.
 */
class CircleGagePanel extends JPanel {
	
	private static final int CIRCLE_RADIUS = 24;
	
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
		final int gap = (parentWidth - 2 * HORIZONTAL_PADDING) / (maxCount - 1) - CIRCLE_RADIUS / 2;
				
		g.setColor(color);
		for (int i = 0; i < maxCount; ++i) {
			int x = i * gap + HORIZONTAL_PADDING;
			
			if (i + 1 > count) { // count 이후의 칸은 빈 칸으로 칠한다.
				g.setColor(Color.lightGray);
				g.fillOval(x, 0, CIRCLE_RADIUS, CIRCLE_RADIUS);	
				g.setColor(color);
				g.drawOval(x, 0, CIRCLE_RADIUS, CIRCLE_RADIUS);	
			} else {
				g.fillOval(x, 0, CIRCLE_RADIUS, CIRCLE_RADIUS);	
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
		scoreLabel.setFont(TextStyle.headline4);
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
		titleLabel.setFont(TextStyle.headline6);
		
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
