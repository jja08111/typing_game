package view.game.enemy;

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
import view.component.CharacterPanel;
import view.game.InformationPanel;
import view.game.UserCharacterPanel;

/**
 * ���� ǥ���ϴ� �г��̴�. 
 * 
 * ������ ������ �����带 ���� �����δ�. �ܾ ������ ������ {@link EnemyHandler}�� ���� 
 * ����, �����ȴ�.
 */
public class EnemyPanel extends CharacterPanel implements Runnable {
	
	protected final JLabel wordLabel;
	
	protected final EnemyHandler handler;
	
	private final Thread thread;
	
	private final UserCharacterPanel userPanel;
	
	protected final InformationPanel infoPanel;
	
	/**
	 * ���� ������ �ӵ��� �����ϴ� �и��� ���� �������̴�.
	 */
	private int delay;
	
	private boolean isMovingEnabled = true;
	
	/**
	 * ���� �ܰ迡 �´� ���� �����Ѵ�. ������ ���� �ؽ�Ʈ�� �����ȴ�. <br>
	 * ũ��� ������ �ǳ� <strong>��ġ�� ������ �ȵǾ��� ������ ���� �������־�� �Ѵ�.<strong>
	 */
	public EnemyPanel(EnemyHandler handler, UserCharacterPanel userPanel, InformationPanel infoPanel) {
		this(Icons.NORMAL_ENEMY, handler, userPanel, infoPanel);
	}
	
	/**
	 * ���� �ܰ迡 �´� ���� �����Ѵ�. �������� ���� ���� �̹����� ������ �� �ִ�. ������ ���� �ؽ�Ʈ�� �����ȴ�. <br>
	 * ũ��� ������ �ǳ� <strong>��ġ�� ������ �ȵǾ��� ������ ���� �������־�� �Ѵ�.<strong>
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
		// �ܾ ���̴� ���̺��� �ʺ� �̹������� ���� ��� �̹����� �ܾ ���̴� ���̺� �߾ӿ� �д�.
		if (wordLabel.getWidth() > imageLabel.getWidth()) {
			setSize(wordLabel.getWidth(), height);
			imageLabel.setLocation((wordLabel.getWidth() - imageLabel.getWidth()) / 2, wordLabel.getHeight());
		} else { // �ݴ��� ��� �ܾ ���̴� ���̺��� �̹��� ���̺� �߾ӿ� �д�. 
			setSize(imageLabel.getWidth(), height);
			wordLabel.setLocation((imageLabel.getWidth() - wordLabel.getWidth()) / 2, 0);
		}
		
		add(wordLabel);
		// �ܾ ���ʿ� ��ġ�Ѵ�.
		setComponentZOrder(wordLabel, 0);
		
		thread = new Thread(this);
		// ������� ���� ������ �̸��� �����Ѵ�.
		thread.setName(getClass().getSimpleName() + "-" + word);
		thread.start();
	}

	private void setDelayPerStage() {
		switch (infoPanel.getStage()) {
		case 1: delay = 40; break;
		case 2: delay = 36; break;
		case 3: delay = 32; break;
		case 4: delay = 30; break;
		case 5: delay = 28; break;
		default: assert(false);
		}
	}
	
	/**
	 * ���� ���� �ܾ ��ȯ�Ѵ�.
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
	 * �� �ν��Ͻ��� �θ� �����̳ʷκ��� �����ϸ� �̶� �����尡 ����ȴ�.
	 */
	public void removeThisFromParent() {
		Container parent = getParent();
		if (parent != null) {
			parent.remove(this);
			parent.repaint();
		}
	}
	
	/**
	 * �÷��̾ �ܾ �Է��Ͽ� �� �� ��ü�� ���ŵ� �� ȣ��Ǵ� �Լ��̸� �� ��ü�� �״� ������ ����Ѵ�.
	 * ��Ȯ�� {@link EnemyHandler#kill(String)} �Լ����� ȣ��ȴ�.
	 * 
	 * �ַ� �� Ŭ������ ����Ͽ� Ư���� ���� ���� �� �� �Լ��� �������̵��Ͽ� �����Ѵ�. 
	 */
	public void onKilled() {
		SoundController.play(Sounds.KILL);
	}
	
	private void removeThisFromHandler() {
		handler.remove(this.getWord());
	}
	
	private int getLabelWidth(String string) {
		return string.length() * 8 + 10;
	}
	
	/**
	 * �÷��̾ �� ���� �������� ���� �÷��̾�� �浹�� ��� ȣ�� �Ǵ� �Լ��̴�.
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
			} catch (InterruptedException e) { // ���� �߻��� ������ ����
				removeThisFromHandler();
				return;
			}
			// �̹� �г��� ȭ��󿡼� ����������� �����尡 ����ִ� ��� ����
			if (getParent() == null) {
				return;
			}
			// �������� �̵��Ѵ�.
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