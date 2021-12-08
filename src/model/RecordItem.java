package model;

import java.util.Comparator;
import java.util.Vector;

/**
 * ���� ����� ������ �� �����μ� ���Ǵ� Ŭ�����̴�.
 */
public class RecordItem implements Comparable<RecordItem> {
	
	/**
	 * ����� �޼��� ����� �̸��̴�.
	 */
	private final String name;
	
	/**
	 * ����� �޼��� �ܰ��̴�.
	 */
	private final int stage;
	
	/**
	 * ����� �޼��� �����̴�.
	 */
	private final int score;
	
	/**
	 * ��� ������ �Ǵ� ��ü�� �����Ѵ�.
	 * @param name �̸� 
	 * @param stage �ذ��� �ܰ� 
	 * @param score �޼��� ���� 
	 */
	public RecordItem(String name, int stage, int score) {
		this.name = name;
		this.stage = stage;
		this.score = score;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name + " " + Integer.toString(stage) + " " + Integer.toString(score); 
	}
	
	/**
	 * ���ڿ��� �� ��ü�� ��ȯ�Ͽ� ��ȯ�Ѵ�. ������ "name stage score"�̴�. 
	 * ���Ŀ� ���� �ʴ� ��� null�� ��ȯ�Ѵ�.
	 * @param string ��ȯ�� ���ڿ�
	 * @return ���Ŀ� ������ ��ü ��ȯ�ϰ�, ���� ������ null�� ��ȯ�Ѵ�.
	 */
	public static RecordItem parse(String string) {
		String[] arrString = string.split(" ");
		if (arrString.length != 3) 
			return null;
		int stage, score;
		try {
			stage = Integer.parseInt(arrString[1]);
			score = Integer.parseInt(arrString[2]);	
		} catch (NumberFormatException e) {
			return null;
		}
		return new RecordItem(arrString[0], stage, score);
	}

	/**
	 * �̸�, ����, �ܰ�� �̷���� 1���� ���͸� ��ȯ�Ѵ�. 
	 */
	public Vector<Object> toVector() {
		Vector<Object> result = new Vector<Object>(3);
		result.add(name);
		result.add(score);
		result.add(stage);
		return result;
	}
	
	/**
	 * ����, �ܰ�, �̸� ������ ���Ͽ� ����� ��ȯ�Ѵ�. 
	 * ������ �ܰ�� ������������, �̸��� ������������ ���Ѵ�.
	 * @param other ���� �ٸ� ��ü 
	 * @return ���� ����� ��ȯ�Ѵ�.
	 */
	@Override
	public int compareTo(RecordItem other) {
		return compare(this, other);
	}
	
	private static int compare(RecordItem a, RecordItem b) {
		int scoreCompare = Integer.compare(b.score, a.score);
		if (scoreCompare == 0) {
			int stageCompare = Integer.compare(b.stage, a.stage);
			if (stageCompare == 0)
				return a.name.compareTo(b.name);
			return stageCompare;
		}
		return scoreCompare;
	}
	
	public static Comparator<RecordItem> getComparator() {
		return new Comparator<RecordItem>() {
		    public int compare(RecordItem a, RecordItem b) {
		        return RecordItem.compare(a, b);
		    }
		};
	}
}