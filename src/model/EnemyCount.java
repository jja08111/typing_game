package model;

import java.util.Random;
import java.util.Vector;

/** 
 * ���� ������ �� ���� ������ ī�����ϱ� ���� �̿��ϴ� Ŭ�����̴�. 
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
	 * @return ���� �� �߿��� ���� �ϳ��� ��� ��ȯ�Ѵ�. ���� ������ ��� 0�� ��� null�� ��ȯ�Ѵ�.
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
	
	/**
	 * @return ���̻� ���� ������ 0�� ��� true�� ��ȯ�Ѵ�.
	 */
	public boolean isEmpty() {
		return normal == 0 && special == 0 && item == 0;
	}
	
}