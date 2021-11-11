package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import constants.ColorScheme;

/**
 * {@link GameFrame} 오른쪽에 위치할 패널이다. 
 * 
 * 이곳에서 남은 생명, 점수, 현재 단계를 보여준다.
 */
public class InformationPanel extends JPanel {
	
	private static final int MAX_LIFE = 3;
	
	private static final int MAX_STAGE = 3;
	
	private int life = 3;
	
	private int score = 0;
	
	private int stage = 1;

	private CountLabelPanel lifePanel = 
			new CountLabelPanel("생명", ColorScheme.primary, life, MAX_LIFE);
	
	private ScoreLabelPanel scorePanel = new ScoreLabelPanel("점수", score);
	
	private CountLabelPanel stagePanel = 
			new CountLabelPanel("단계", ColorScheme.secondary, stage, MAX_STAGE);
	
	public InformationPanel() {
		setBackground(Color.white);
		setLayout(null);
		
		lifePanel.setLocation(34, 80);
		scorePanel.setLocation(34, 210);
		stagePanel.setLocation(34, 340);
		
		add(lifePanel);
		add(scorePanel);
		add(stagePanel);
	}
	
	/**
	 * 생명을 하나 감소한다.
	 */
	public void decreaseLife() {
		life -= 1;
		lifePanel.updateGageCount(life);
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
	public void decreaseScrore() {
		// TODO: 구현
	}
	
	/**
	 * 단계를 하나 올린다.
	 */
	public void increaseStage() {
		// TODO: 구현
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
class CountLabelPanel extends LabelPanel {
	
	private CircleGagePanel gage;
	
	/**
	 * @param title 타이틀로 보일 텍스트를 지정한다.
	 * @param score 원형 이미지가 몇개 채워져 있는 지 정한다.
	 * @param maxScore 최대 몇 개까지 채울 수 있는 지 정한다.
	 */
	public CountLabelPanel(String title, Color color, int score, int maxScore) {
		super(title, score);
		gage.setColor(color);
		gage.setCount(score);
		gage.setMaxScore(maxScore);
	}

	public void updateGageCount(int count) {
		gage.updateCount(count);
	}
	
	@Override
	public Component buildBottomComponent(int count) {
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
	public Component buildBottomComponent(int score) {
		scoreLabel = new JLabel();
		scoreLabel.setText(Integer.toString(score));	
		scoreLabel.setHorizontalAlignment(JLabel.CENTER);
		// 폰트 사이즈를 30으로 변경한다.
		scoreLabel.setFont(scoreLabel.getFont().deriveFont(34.0f));
		return scoreLabel;
	}
	
}

/**
 * 상단에 레이블을 가지고 아래에 점수를 갖는 추상 패널이다. 
 * 반드시 {@link LabelPanel#buildBottomComponent(int)}를 구현해야 한다.
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
		titleLabel.setFont(titleLabel.getFont().deriveFont(20.0f));
		
		setLayout(new BorderLayout(0, 12));
		add(titleLabel, BorderLayout.NORTH);
		bottomComp = buildBottomComponent(score);
		add(bottomComp, BorderLayout.CENTER);
	}
	
	/**
	 * {@link LabelPanel} 클래스를 생성시 패널 중앙에 부착 할 컴포넌트를 빌드한다.
	 * @return 패널 중앙에 부착할 컴포넌트 
	 */
	public abstract Component buildBottomComponent(int score);
}
