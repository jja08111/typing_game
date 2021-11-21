package view;

import java.awt.Container;
import java.awt.Font;
import java.awt.Point;

import javax.swing.JLabel;

import constant.ColorScheme;
import constant.Icons;
import handler.TextSourceHandler;

/**
 * 적을 표현하는 패널이다. 
 * 
 * 적마다 각자의 쓰레드를 통해 움직인다. 단어를 가지고 있으며 {@link EnemyHandler}를 통해 
 * 생성, 삭제된다.
 */
public class EnemyPanel extends CharactorPanel implements Runnable {
	
	/**
	 * 밀리초 단위로 되어 있는 딜레이 상수이다.
	 */
	private static final int DELAY = 300;
	
	private final JLabel label;
	
	private final Thread thread;
	
	private final UserCharactorPanel userPanel;
	
	private final InformationPanel infoPanel;
	
	private final int speed;
	
	/**
	 * 게임 단계에 맞는 적을 생성한다. 생성시 랜덤 텍스트가 생성된다. <br>
	 * 
	 * 크기는 결정이 되나 <strong>위치는 결정이 안되었기 때문에 따로 지정해주어야 한다.<strong>
	 */
	public EnemyPanel(String word, int stage, UserCharactorPanel userPanel, InformationPanel infoPanel) {
		super(Icons.normal);
		
		this.userPanel = userPanel;
		this.infoPanel = infoPanel;
		this.speed = getSpeedFrom(stage);
		
		label = new JLabel(word);
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

	/**
	 * 적이 가진 단어를 반환한다.
	 */
	public String getWord() {
		return label.getText();
	}
	
	public void interruptThread() {
		thread.interrupt();
		removeThisFromParent();
	}
	
	private int getLabelWidth(String string) {
		return string.length() * 8 + 10;
	}
	
	private void removeThisFromParent() {
		if (this.getParent() != null) {
			Container parent = this.getParent();
			parent.remove(this);
			parent.repaint();	
		}
	}
	
	@Override 
	public void run() {
		while(true) {
			try {
				Thread.sleep(DELAY);
			} catch (InterruptedException e) { // 예외 발생시 스레드 종료
				removeThisFromParent();
				return;
			}
			// 이미 패널이 화면상에서 사라졌음에도 쓰레드가 살아있는 경우 종료
			if (getParent() == null) {
				return;
			}
			// 좌측으로 이동한다.
			setLocation(getX() - speed, getY());
			if (isCollidedWithUser()) {
				infoPanel.decreaseLife();
				infoPanel.decreaseScore();
				
				removeThisFromParent();
				return;
			}
			
			repaint();
		}
	}
	
	private int getSpeedFrom(int stage) {
		switch (stage) {
		case 1: return 100; //TODO : 10으로 변경 
		case 2: return 20;
		case 3: return 30;
		default: assert(false);
		}
		return -1;
	}
	
	private boolean isCollidedWithUser() {
		if (getX() <= userPanel.getRightX())
			return true;
		return false;
	}
	
}
