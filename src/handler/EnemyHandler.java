package handler;

import view.game.GameGroundPanel;
import view.game.InformationPanel;
import view.game.UserCharacterPanel;
import view.game.enemy.BombItemEnemyPanel;
import view.game.enemy.EnemyPanel;
import view.game.enemy.SpecialEnemyPanel;
import view.game.enemy.StopItemEnemyPanel;

import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import model.EnemyCount;
import model.EnemyType;

/**
 * �� ��ü���� ���� �� �����ϴ� Ŭ�����̴�.
 */
public final class EnemyHandler {
	
	/**
	 * ���ο� ���� �ܾ ����� �����̴�.
	 */
	private static final TextSourceHandler textSource = TextSourceHandler.getInstance();
	
	private final HashMap<String, EnemyPanel> enemyMap = new HashMap<String, EnemyPanel>();
	
	private EnemyGenerationThread generationThread;
	
	/**
	 * ���� ������ x ��ǥ ��ġ�̴�.
	 */
	private Integer x;
	
	private final Random random = new Random();
	
	private final GameGroundPanel gameGroundPanel;
	
	private final InformationPanel infoPanel;
	
	private boolean isGenerating = false;
	
	/**
	 * ������ �ٷ�� Ŭ������ �����Ѵ�. ���� �гο� �°� �� ���� ��ġ�� �����Ѵ�.
	 * @param gamePanel ���� �г� ��ü �ν��Ͻ� 
	 */
	public EnemyHandler(GameGroundPanel gamePanel, InformationPanel infoPanel) {
		this.gameGroundPanel = gamePanel;
		this.infoPanel = infoPanel;
	}
	
	/**
	 * ���� ������� ������ ������ ��ġ�� ���ο� �Ϲ� ���� 1�� �����Ѵ�. 
	 */
	public void createNormalAt(int x, int y) {
		EnemyPanel newEnemy = new EnemyPanel(this, gameGroundPanel.getUserPanel(), infoPanel);
		newEnemy.setLocation(x, y);
		
		synchronized (enemyMap) {
			enemyMap.put(newEnemy.getWord(), newEnemy);
		}
		gameGroundPanel.add(newEnemy);
	}
	
	/**
	 * word�� key�� ���� ���� �����Ѵ�. <strong>���⼭ {@link EnemyPanel#onKilled()}�Լ��� ȣ��ȴ�.</strong> 
	 * �̶� ���� �гο����� ���ŵȴ�. ���� �ش� ���� ������ ���̾��ٸ� ���� �ܰ�� �����Ѵ�.
	 * @param word ���� ���� �ܾ�
	 * @return �ش� �ܾ ������ �ִ� ���� ���ٸ� false�� ��ȯ�Ѵ�. 
	 */
	public boolean kill(String word) {
		EnemyPanel enemy = enemyMap.get(word);
		if (enemy != null) {
			enemy.onKilled();
			return remove(word);
		}
		return false; 
	}
	
	/**
	 * word�� key�� ���� ���� �����Ѵ�. �̶� ���� �гο����� ���ŵȴ�. 
	 * ���� �ش� ���� ������ ���̾��ٸ� ���� �ܰ�� �����Ѵ�.
	 * @param word ���� ���� �ܾ�
	 * @return �ش� �ܾ ������ �ִ� ���� ���ٸ� false�� ��ȯ�Ѵ�. 
	 */
	public boolean remove(String word) {
		synchronized (enemyMap) {
			if (!enemyMap.containsKey(word)) return false;

			EnemyPanel enemyPanel = enemyMap.get(word);
			enemyPanel.removeThisFromParent();
			enemyMap.remove(word);
		}
		checkIfStageIsCleared();
		return true;
	}
	
	/**
	 * �ش� �ܾ ���� ���� �����ϰ� �������� ���� �ϳ� �����Ѵ�. 
	 * @param word �������� ���� ���� ���� �ܾ�
	 */
	public void removeRandomExcept(String word) {
		synchronized (enemyMap) {
			// ������ ���� �ؽ��ʿ��� ������.
			EnemyPanel exceptedEnemy = enemyMap.remove(word);
			if (exceptedEnemy == null) return;
			
			if (enemyMap.isEmpty()) { // �����ϴ� ���� ���� ���
				// �����ߴ� ���� �ٽ� �ؽ��ʿ� �ִ´�.
				enemyMap.put(exceptedEnemy.getWord(), exceptedEnemy);
				return;
			}
			Object[] values = enemyMap.values().toArray();
			
			EnemyPanel randomEnemy = (EnemyPanel)values[random.nextInt(values.length)];
			randomEnemy.removeThisFromParent();
			enemyMap.remove(randomEnemy.getWord());
			
			// �����ߴ� ���� �ٽ� �ؽ��ʿ� �ִ´�.
			enemyMap.put(exceptedEnemy.getWord(), exceptedEnemy);
		}
		checkIfStageIsCleared();
	}
	
	/**
	 * ���� �ܰ踦 Ŭ���� �ߴ��� Ȯ���ϰ� Ŭ���� �ߴٸ� ���� �ܰ�� �����Ѵ�.
	 */
	private void checkIfStageIsCleared() {
		if (generationThread.getRemainCount().isEmpty() && enemyMap.isEmpty()) {
			infoPanel.increaseStage();
			stopGenThread();
		}
	}
	
	/**
	 * ��� ���� �����ϰ� ȭ�鿡���� �����.
	 */
	public void clear() {
		synchronized (enemyMap) {
			enemyMap.values().forEach(enemyPanel -> {
				// CuncurrentModificationException�� �߻��ϹǷ� �̸� ���� ���� 3���� ���� ������ �ʵ��� ���ش�.
				if (enemyPanel instanceof SpecialEnemyPanel) {
					((SpecialEnemyPanel)enemyPanel).doNotGiveBirth();
				}
				enemyPanel.removeThisFromParent();
			});
			enemyMap.clear(); 
		}
	}
	
	/**
	 * ������ �����ϴ� �����带 �����Ѵ�.
	 */
	public void startGenThread() {
		if (isGenerating) return;
		generationThread = new EnemyGenerationThread();
		generationThread.start();
	}
	
	/**
	 * ������ �����ϴ� �����带 �����Ѵ�.
	 */
	public void stopGenThread() {
		if (!isGenerating) return;
		generationThread.interrupt();
	}
	
	public void enableMovingAll() {
		synchronized (enemyMap) {
			enemyMap.values().forEach(enemy -> enemy.enableMoving());
		}
	}
	
	public void disableMovingAll() {
		synchronized (enemyMap) {
			enemyMap.values().forEach(enemy -> enemy.disableMoving());
		}
	}
	
	/**
	 * ������ �ܾ ��´�. �̶� �ߺ����� �ʴ� �ܾ ��´�.
	 */
	public String getRandomWord() {
		String word;
		while (true) {
			word = textSource.getRandom();
			if (!enemyMap.containsKey(word))
				break;
		}
		return word;
	}
	
	/**
	 * ���� ������ �ֱ⸶�� �����ϴ� �������̴�. ���� ��� �����ϸ� ����ȴ�.
	 * �ܰ踶�� ���� ����, ���� �����ϴ� �ӵ��� �ٸ���. 
	 */
	private class EnemyGenerationThread extends Thread {
		
		/**
		 * ���� ������ �� �߰��� �������� ������ ������ �����̴�. 
		 */
		private static final int RANGE = 300;
		
		/**
		 * ���� �����ϴ� �ӵ��� ������ �����̴�. {@link EnemyGenerationThread.RANGE} ������ 
		 * [delay, delay + RANGE] ������ ������ �����̰� �����ȴ�. 
		 */
		private final int delay;
		
		/**
		 * �߰��� �����Ͼ� �� ���� �����̴�.
		 */
		private EnemyCount remainCount;
		
		public EnemyGenerationThread() {
			this.remainCount = getEnemyCountPer();
			this.delay = getDelayPerStage();
			// ������ �̸��� �����Ѵ�.
			setName(getClass().getSimpleName() + "-" + Integer.toString(delay));
		}
		
		public EnemyCount getEnemyCountPer() {
			switch (infoPanel.getStage()) {
			case 1:
				return new EnemyCount(10, 1, 1);
			case 2:
				return new EnemyCount(10, 2, 2);
			case 3:
				return new EnemyCount(10, 3, 3);
			case 4:
				return new EnemyCount(10, 4, 4);
			case 5:
				return new EnemyCount(10, 5, 5);
			default:
				assert false;
			}
			return null;
		}
		
		public EnemyCount getRemainCount() {
			return remainCount;
		}
		
		private int getDelayPerStage() {
			switch (infoPanel.getStage()) {
			// 10�ʴ� x���� �����Ѵ�.
			case 1: return 1000 * 10 / 2;
			case 2: return 1000 * 10 / 3;
			case 3: return 1000 * 10 / 4;
			case 4: return 1000 * 10 / 5;
			case 5: return 1000 * 10 / 6;
			default: assert(false);
			}
			return -1;
		}
		
		private int getRandomDelay() {
			return delay + (int)(Math.random() * RANGE);
		}
		
		/**
		 * ���ο� ���� 1�� �����Ѵ�. 
		 * ������ �������� y ��ġ�� �����Ǹ� �ܾ �������� ���õȴ�.  
		 * @param type ������ ���� Ÿ��
		 */
		private void create(EnemyType type) {
			// gameGroundPanel�� ũ�Ⱑ �����ǰ� ���� ��ġ�� ���ϱ� ���� �ʰ� �ʱ�ȭ�� �Ѵ�.
			if (x == null) {
				// ȭ�� ������ �� ���� x ��ġ�� �����Ѵ�.
				x = gameGroundPanel.getWidth();
			}
			final UserCharacterPanel userPanel = gameGroundPanel.getUserPanel();

			EnemyPanel newEnemy;
			switch (type) {
			case NORMAL:
				newEnemy = new EnemyPanel(EnemyHandler.this, userPanel, infoPanel);
				remainCount.decreaseNormal();
				break;
			case SPECIAL:
				newEnemy = new SpecialEnemyPanel(EnemyHandler.this, userPanel, infoPanel);
				remainCount.decreaseSpecial();
				break;
			case STOP_ITEM:
				newEnemy = new StopItemEnemyPanel(EnemyHandler.this, userPanel, infoPanel);
				remainCount.decreaseItem();
				break;
			case BOMB_ITEM:
				newEnemy = new BombItemEnemyPanel(EnemyHandler.this, userPanel, infoPanel);
				remainCount.decreaseItem();
				break;
			default:
				assert(false);
				return;
			}
			
			int y = random.nextInt(gameGroundPanel.getHeight() - newEnemy.getHeight());
			newEnemy.setLocation(x, y);
			
			synchronized (enemyMap) {
				enemyMap.put(newEnemy.getWord(), newEnemy);
			}
			gameGroundPanel.add(newEnemy);
		}
		
		@Override 
		public void run() {
			isGenerating = true;
			while (true) {
				
				create(remainCount.getRandomType());
				try {
					sleep(getRandomDelay());
				} catch (InterruptedException e) { // ���ͷ�Ʈ �߻��� ������ ����
					break;
				}

				if (remainCount.isEmpty()) { // ���� ��� �����ϸ� �����Ѵ�.
					break;
				}
			}
			isGenerating = false;
		}
	}
	
}