package model;

import java.util.Random;
import java.util.Vector;

/** 
 * 적을 생성할 때 갯수를 세기 위해 이용하는 클래스이다. 
 */
public final class EnemyCount {

	private int normal;
	
	private int special;
	
	private int item;
	
	public EnemyCount(int normal, int special, int item) {
		this.normal = normal;
		this.special = special;
		this.item = item;
	}
	
	public void decreaseNormal() {
		normal -= 1;
	}

	public void decreaseSpecial() {
		special -= 1;
	}
	
	public void decreaseItem() {
		item -= 1;
	}
	
	private int getTotalCount() {
		return normal + special + item;
	}
	
	private Vector<EnemyType> getAvailableTypes() {
		Vector<EnemyType> result = new Vector<EnemyType>(4);
		if (normal > 0) {
			result.add(EnemyType.NORMAL);
		}
		if (special > 0) { 
			result.add(EnemyType.SPECIAL);
		}
		if (item > 0) {
			result.add(EnemyType.STOP_ITEM);
			result.add(EnemyType.BOMB_ITEM);
		}
		return result;
	}
	
	/**
	 * @return 남은 적 중에서 종류 하나를 골라 반환한다. 남은 갯수가 모두 0인 경우 null을 반환한다.
	 */
	public EnemyType getRandomType() {
		Random random = new Random();
		Vector<EnemyType> types = getAvailableTypes();
		int randomIndex = random.nextInt(getTotalCount());
		EnemyType result = EnemyType.NORMAL;
		
		if (randomIndex < normal) {
			return EnemyType.NORMAL;
		} else if (randomIndex < normal + special) { 
			return EnemyType.SPECIAL;
		} else if (randomIndex < normal + special + item) {
			return random.nextBoolean() ? EnemyType.BOMB_ITEM : EnemyType.STOP_ITEM;
		}
		
		return null;
	}
	
	public boolean isEmpty() {
		return normal == 0 && special == 0 && item == 0;
	}
	
}
