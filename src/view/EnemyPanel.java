package view;

import java.awt.Container;
import java.awt.Font;
import java.awt.Point;

import javax.swing.JLabel;

import constant.ColorScheme;
import constant.Icons;
import handler.TextSourceHandler;

public class EnemyPanel extends CharactorPanel {
	
	private final JLabel label;
	
	private final EnemyThread thread;
	
	/**
	 * 게임 단계에 맞는 적을 생성한다. 생성시 랜덤 텍스트가 생성된다. <br>
	 * 
	 * 크기는 결정이 되나 <strong>위치는 결정이 안되었기 때문에 따로 지정해주어야 한다.<strong>
	 */
	public EnemyPanel(String word, int stage) {
		super(Icons.normal);
		
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
		
		setComponentZOrder(label, 0);
		
		thread = new EnemyThread(this, stage);
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
	}
	
	private int getLabelWidth(String string) {
		return string.length() * 8 + 10;
	}
	
	private class EnemyThread extends Thread {
		
		private static final int sleepDuration = 300;
		
		private final EnemyPanel panel;
		
		private final int speed;
		
		public EnemyThread(EnemyPanel panel, int stage) {
			this.panel = panel;
			this.speed = getSpeed(stage);
		}
		
		private int getSpeed(int stage) {
			switch (stage) {
			case 1: return 10;
			case 2: return 20;
			case 3: return 30;
			default: assert(false);
			}
			return -1;
		}
		
		@Override 
		public void run() {
			while(true) {
				try {
					sleep(sleepDuration);
				} catch (InterruptedException e) { // 예외 발생시 스레드 종료
					Container parent = panel.getParent();
					parent.remove(panel);
					parent.repaint();
					parent.revalidate();
					return;
				}
				
				panel.setLocation(panel.getX() - speed, panel.getY());
				panel.repaint();
			}
		}
	}
}
