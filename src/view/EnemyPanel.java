package view;

import java.awt.Container;
import java.awt.Font;
import java.awt.Point;

import javax.swing.JLabel;

import constant.ColorScheme;
import constant.Icons;
import handler.EnemyHandler;
import handler.TextSourceHandler;

/**
 * 적을 표현하는 패널이다. 
 * 
 * 적마다 각자의 쓰레드를 통해 움직인다. 단어를 가지고 있으며 {@link EnemyHandler}를 통해 
 * 생성, 삭제된다.
 */
public class EnemyPanel extends CharactorPanel implements Runnable {
	
	private final JLabel label;
	
	private final Thread thread;
	
	private final EnemyHandler handler;
	
	private final UserCharactorPanel userPanel;
	
	private final InformationPanel infoPanel;
	
	/**
	 * 적의 움직임 속도를 결정하는 밀리초 단위 딜레이이다.
	 */
	private int delay;
	
	private boolean isMovingEnabled = true;
	
	/**
	 * 게임 단계에 맞는 적을 생성한다. 생성시 랜덤 텍스트가 생성된다. <br>
	 * 크기는 결정이 되나 <strong>위치는 결정이 안되었기 때문에 따로 지정해주어야 한다.<strong>
	 */
	public EnemyPanel(EnemyHandler handler, UserCharactorPanel userPanel, InformationPanel infoPanel) {
		super(Icons.normal);
		this.handler = handler;
		this.userPanel = userPanel;
		this.infoPanel = infoPanel;
		
		setDelayPerStage();
		
		label = new JLabel(handler.getRandomWord());
		label.setForeground(ColorScheme.onPrimary);
		label.setBackground(ColorScheme.primaryVariant);
		label.setSize(getLabelWidth(label.getText()), 32);
		label.setLocation(0, 0);
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setOpaque(true);
		
		super.setLayout(null);
		super.setSize(label.getWidth(), label.getHeight() + 20);
		
		add(label);
		// 단어를 위쪽에 배치한다.
		setComponentZOrder(label, 0);
		
		thread = new Thread(this);
		thread.start();
	}

	private void setDelayPerStage() {
		switch (infoPanel.getStage()) {
		case 1: delay = 40; break;
		case 2: delay = 30; break;
		case 3: delay = 20; break;
		default: assert(false);
		}
	}
	
	/**
	 * 적이 가진 단어를 반환한다.
	 */
	public String getWord() {
		return label.getText();
	}
	
	public void stopMoving() {
		isMovingEnabled = false;
	}
	
	/**
	 * 적 인스턴스를 부모 컨테이너로부터 제거하며 이때 스레드가 종료된다.
	 */
	public void end() {
		Container parent = getParent();
		if (parent != null) {
			parent.remove(this);
			parent.repaint();
		}
	}
	
	private void removeThisFromHandler() {
		handler.remove(this.getWord());
	}
	
	private int getLabelWidth(String string) {
		return string.length() * 8 + 10;
	}
	
	@Override 
	public void run() {
		while(true) {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) { // 예외 발생시 스레드 종료
				removeThisFromHandler();
				return;
			}
			// 이미 패널이 화면상에서 사라졌음에도 쓰레드가 살아있는 경우 종료
			if (getParent() == null) {
				return;
			}
			// 좌측으로 이동한다.
			if (isMovingEnabled)
				setLocation(getX() - 1, getY());
			
			if (isCollidedWithUser()) {
				infoPanel.decreaseLife();
				infoPanel.decreaseScore();
				
				removeThisFromHandler();
				return;
			}
			
			repaint();
		}
	}
	
	private boolean isCollidedWithUser() {
		if (getX() <= userPanel.getRightX())
			return true;
		return false;
	}
	
}
