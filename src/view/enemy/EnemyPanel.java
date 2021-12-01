package view.enemy;

import java.awt.Container;
import java.awt.Font;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import constant.ColorScheme;
import constant.Icons;
import handler.EnemyHandler;
import handler.SoundController;
import handler.TextSourceHandler;
import model.Sounds;
import view.CharacterPanel;
import view.InformationPanel;
import view.UserCharacterPanel;

/**
 * 적을 표현하는 패널이다. 
 * 
 * 적마다 각자의 쓰레드를 통해 움직인다. 단어를 가지고 있으며 {@link EnemyHandler}를 통해 
 * 생성, 삭제된다.
 */
public class EnemyPanel extends CharacterPanel implements Runnable {
	
	protected final JLabel wordLabel;
	
	protected final EnemyHandler handler;
	
	private final Thread thread;
	
	private final UserCharacterPanel userPanel;
	
	protected final InformationPanel infoPanel;
	
	/**
	 * 적의 움직임 속도를 결정하는 밀리초 단위 딜레이이다.
	 */
	private int delay;
	
	private boolean isMovingEnabled = true;
	
	/**
	 * 게임 단계에 맞는 적을 생성한다. 생성시 랜덤 텍스트가 생성된다. <br>
	 * 크기는 결정이 되나 <strong>위치는 결정이 안되었기 때문에 따로 지정해주어야 한다.<strong>
	 */
	public EnemyPanel(EnemyHandler handler, UserCharacterPanel userPanel, InformationPanel infoPanel) {
		this(Icons.NORMAL_ENEMY, handler, userPanel, infoPanel);
	}
	
	/**
	 * 게임 단계에 맞는 적을 생성한다. 아이콘을 통해 적의 이미지를 지정할 수 있다. 생성시 랜덤 텍스트가 생성된다. <br>
	 * 크기는 결정이 되나 <strong>위치는 결정이 안되었기 때문에 따로 지정해주어야 한다.<strong>
	 */
	public EnemyPanel(ImageIcon icon, EnemyHandler handler, UserCharacterPanel userPanel, InformationPanel infoPanel) {
		super(icon);
		this.handler = handler;
		this.userPanel = userPanel;
		this.infoPanel = infoPanel;
		setDelayPerStage();
		
		String word = handler.getRandomWord();
		wordLabel = new JLabel(word);
		wordLabel.setForeground(ColorScheme.ON_PRIMARY);
		wordLabel.setBackground(ColorScheme.PRIMARY_VARIANT);
		wordLabel.setSize(getLabelWidth(wordLabel.getText()), 32);
		wordLabel.setLocation(0, 0);
		wordLabel.setHorizontalAlignment(JLabel.CENTER);
		wordLabel.setOpaque(true);
		
		setLayout(null);
		final int height = wordLabel.getHeight() + imageLabel.getHeight();
		// 단어를 보이는 레이블의 너비가 이미지보다 넓은 경우 이미지를 단어가 보이는 레이블 중앙에 둔다.
		if (wordLabel.getWidth() > imageLabel.getWidth()) {
			setSize(wordLabel.getWidth(), height);
			imageLabel.setLocation((wordLabel.getWidth() - imageLabel.getWidth()) / 2, wordLabel.getHeight());
		} else { // 반대의 경우 단어를 보이는 레이블을 이미지 레이블 중앙에 둔다. 
			setSize(imageLabel.getWidth(), height);
			wordLabel.setLocation((imageLabel.getWidth() - wordLabel.getWidth()) / 2, 0);
		}
		
		
		
		add(wordLabel);
		// 단어를 위쪽에 배치한다.
		setComponentZOrder(wordLabel, 0);
		
		thread = new Thread(this);
		// 디버깅을 위해 스레드 이름을 변경한다.
		thread.setName(getClass().getSimpleName() + "-" + word);
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
		return wordLabel.getText();
	}
	
	public void enableMoving() {
		isMovingEnabled = true;
	}
	
	public void disableMoving() {
		isMovingEnabled = false;
	}
	
	/**
	 * 적 인스턴스를 부모 컨테이너로부터 제거하며 이때 스레드가 종료된다.
	 */
	public void removeThisFromParent() {
		Container parent = getParent();
		if (parent != null) {
			parent.remove(this);
			parent.repaint();
		}
	}
	
	/**
	 * 플레이어가 단어를 입력하여 이 적 객체가 제거될 때 호출되는 함수이다.
	 * 정확히 {@link EnemyHandler#kill(String)} 함수에서 호출된다.
	 * 
	 * 주로 이 클래스를 상속하여 특별한 적을 만들 때 이 함수를 오버라이딩하여 구현한다. 
	 */
	public void isKilled() {
		SoundController.play(Sounds.KILL);
	}
	
	private void removeThisFromHandler() {
		handler.remove(this.getWord());
	}
	
	private int getLabelWidth(String string) {
		return string.length() * 8 + 10;
	}
	
	/**
	 * 플레이어가 이 적을 제거하지 못해 플레이어와 충돌한 경우 호출 되는 함수이다.
	 */
	protected void onCollidedWithUser() {
		SoundController.play(Sounds.COLLIDE);
		infoPanel.decreaseScore();
		infoPanel.decreaseLife();
		
		removeThisFromHandler();
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
				onCollidedWithUser();
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
