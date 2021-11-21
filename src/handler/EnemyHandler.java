package handler;

import view.EnemyPanel;
import view.GameGroundPanel;
import view.GamePanel;
import view.InformationPanel;

import java.awt.Container;
import java.awt.Font;
import java.awt.Point;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JLabel;

import constant.Icons;

/**
 * 적 클래스들을 생성 및 삭제하는 클래스이다.
 */
public final class EnemyHandler {
	
	/**
	 * 새로운 랜덤 단어를 만드는 변수이다.
	 */
	private static final TextSourceHandler textSource = TextSourceHandler.getInstance();
	
	
	private final HashMap<String, EnemyPanel> enemyMap = new HashMap<String, EnemyPanel>();
	
	private EnemyGenerationThread generationThread;
	
	/**
	 * 적이 생성될 x 좌표 위치이다.
	 */
	private Integer x;
	
	private final Random random = new Random();
	
	private final GameGroundPanel gameGroundPanel;
	
	private final InformationPanel infoPanel;
	
	private boolean isGenerating = false;
	
	/**
	 * 적들을 다루는 클래스를 생성한다. 게임 패널에 맞게 적 생성 위치를 지정한다.
	 * @param gamePanel 게임 패널 객체 인스턴스 
	 */
	public EnemyHandler(GameGroundPanel gamePanel, InformationPanel infoPanel) {
		this.gameGroundPanel = gamePanel;
		this.infoPanel = infoPanel;
	}
	
	/**
	 * 새로운 적을 1개 생성한다. 
	 * 생성시 랜덤으로 y 위치가 지정되며 단어도 무작위로 선택된다.  
	 * @param stage 현재 게임의 단계 
	 */
	private void create(int stage) {
		// gameGroundPanel의 크기가 결정되고 나서 위치를 정하기 위해 늦게 초기화를 한다.
		if (x == null) {
			// 화면 밖으로 적 시작 x 위치를 설정한다.
			x = gameGroundPanel.getWidth();
		}

		EnemyPanel newEnemy = new EnemyPanel(getRandomWord(), stage, gameGroundPanel.getUserPanel(), infoPanel);
		final int y = random.nextInt(gameGroundPanel.getHeight() - newEnemy.getHeight());
		newEnemy.setLocation(x, y);
		
		enemyMap.put(newEnemy.getWord(), newEnemy);
		gameGroundPanel.add(newEnemy);
	}

	/**
	 * word를 key로 가진 적을 제거한다. 이때 게임 패널에서도 제거된다.
	 * @param word 적이 가진 단어
	 * @return 해당 단어를 가지고 있는 적이 없다면 false를 반환한다. 
	 */
	public boolean remove(String word) {
		if (!enemyMap.containsKey(word)) return false;
		EnemyPanel enemy = enemyMap.get(word);
		
		Container parent = enemy.getParent();
		parent.remove(enemy);
		parent.repaint();
		enemyMap.remove(word);
		return true;
	}
	
	/**
	 * 모든 적을 삭제하고 화면에서도 지운다.
	 */
	public void clear() {
		Collection<EnemyPanel> set = enemyMap.values();
		set.forEach(enemyPanel -> {
			enemyPanel.interruptThread();
			Container c = enemyPanel.getParent();
			if (c != null) {
				c.remove(enemyPanel);
				c.repaint();
			}
		});
		enemyMap.clear();
	}
	
	/**
	 * 적들을 생성하는 쓰레드를 시작한다.
	 */
	public void startGenThread() {
		if (isGenerating) return;
		isGenerating = true;
		generationThread = new EnemyGenerationThread();
		generationThread.start();
	}
	
	/**
	 * 적들을 생성하던 쓰레드를 종료한다.
	 */
	public void stopGenThread() {
		if (!isGenerating) return;
		isGenerating = false;
		generationThread.interrupt();
	}
	
	/**
	 * 무작위 단어를 얻는다. 이때 중복되지 않는 단어를 얻는다.
	 */
	private String getRandomWord() {
		String word;
		while (true) {
			word = textSource.getRandom();
			if (!enemyMap.containsKey(word))
				break;
		}
		return word;
	}
	
	/**
	 * 적을 일정한 주기마다 생성하는 쓰레드이다. 
	 * 단계마다 적의 갯수, 적이 출현하는 속도가 다르다. 
	 */
	public class EnemyGenerationThread extends Thread {
		
		/**
		 * 랜덤으로 생성할 적의 생성 속도 범위이다. 
		 */
		private static final int RANGE = 200;
		
		/**
		 * 적을 생성하는 속도를 조절할 변수이다. {@link EnemyGenerationThread.RANGE} 변수와 
		 * [delay, delay + RANGE] 범위의 랜덤한 딜레이가 생성된다. 
		 */
		private final int delay;
		
		/**
		 * 추가로 생성하야 할 적의 개수이다.
		 */
		private int count;
		
		public EnemyGenerationThread() {
			this.count = getEnemyCountPerStage();
			this.delay = getDelayPerStage();
		}
		
		private int getEnemyCountPerStage() {
			switch (infoPanel.getStage()) {
			case 1: return 10;
			case 2: return 15;
			case 3: return 20;
			default: assert(false);
			}
			return -1;
		}
		
		private int getDelayPerStage() {
			switch (infoPanel.getStage()) {
			case 1: return 1000 * 10 / 3;
			case 2: return 1000 * 10 / 5;
			case 3: return 1000 * 10 / 7;
			default: assert(false);
			}
			return -1;
		}
		
		private int getRandomDelay() {
			return delay + (int)(Math.random() * RANGE);
		}
		
		@Override 
		public void run() {
			while (true) {
				count--;
				create(infoPanel.getStage());
				try {
					sleep(getRandomDelay());
				} catch (InterruptedException e) { // 인터럽트 발생시 스레드 종료
					return;
				}

				if (count == 0) { // 적을 모두 생성하면 탈출한다.
					break;
				}
			}
		}
	}
	
}
