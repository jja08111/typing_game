package handler;

import view.EnemyPanel;
import view.GameGroundPanel;
import view.GamePanel;

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
	
	/**
	 * 적이 생성될 x 좌표 위치이다.
	 */
	private Integer x;
	
	private final Random random = new Random();
	
	private final GameGroundPanel gameGroundPanel;
	
	/**
	 * 적들을 다루는 클래스를 생성한다. 게임 패널에 맞게 적 생성 위치를 지정한다.
	 * @param gamePanel 게임 패널 객체 인스턴스 
	 */
	public EnemyHandler(GameGroundPanel gamePanel) {
		this.gameGroundPanel = gamePanel;
	}
	
	/**
	 * 새로운 적을 1개 생성한다. 
	 * 생성시 랜덤으로 y 위치가 지정되며 단어도 무작위로 선택된다.  
	 * @param stage 현재 게임의 단계 
	 */
	public void create(int stage) {
		// gameGroundPanel의 크기가 결정되고 나서 위치를 정하기 위해 늦게 초기화를 한다.
		if (x == null) {
			// 화면 밖으로 적 시작 x 위치를 설정한다.
			x = gameGroundPanel.getWidth();
		}

		EnemyPanel newEnemy = new EnemyPanel(getRandomWord(), stage);
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
			c.remove(enemyPanel);
			c.repaint();
		});
		enemyMap.clear();
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
}
