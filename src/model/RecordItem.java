package model;

import java.util.Comparator;
import java.util.Vector;

/**
 * 게임 기록을 저장할 때 단위로서 사용되는 클래스이다.
 */
public class RecordItem implements Comparable<RecordItem> {
	
	/**
	 * 기록을 달성한 사람의 이름이다.
	 */
	private final String name;
	
	/**
	 * 기록을 달성한 단계이다.
	 */
	private final int stage;
	
	/**
	 * 기록을 달성한 점수이다.
	 */
	private final int score;
	
	/**
	 * 기록 단위가 되는 객체를 생성한다.
	 * @param name 이름 
	 * @param stage 해결한 단계 
	 * @param score 달성한 점수 
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
	 * 문자열을 이 객체로 변환하여 반환한다. 형식은 "name stage score"이다. 
	 * 형식에 맞지 않는 경우 null을 반환한다.
	 * @param string 변환할 문자열
	 * @return 형식에 맞으면 객체 반환하고, 맞지 않으면 null을 반환한다.
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
	 * 이름, 점수, 단계로 이루어진 1차원 벡터를 반환한다. 
	 */
	public Vector<Object> toVector() {
		Vector<Object> result = new Vector<Object>(3);
		result.add(name);
		result.add(score);
		result.add(stage);
		return result;
	}
	
	/**
	 * 점수, 단계, 이름 순으로 비교하여 결과를 반환한다. 
	 * 점수와 단계는 내림차순으로, 이름은 오름차순으로 비교한다.
	 * @param other 비교할 다른 객체 
	 * @return 비교한 결과를 반환한다.
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
