package util;
import java.util.Vector;

public class TextSource {
	
	private Vector<String> v = new Vector<String>();
	
	/*
	 * 파일에서 문자열 소스를 읽어들인다.
	 */
	public TextSource() {
		v.add("hello");
		v.add("Game");
		v.add("I love you");
		v.add("Java is Good");
		v.add("Mobile");
		v.add("Computer");
		v.add("Raspberry");
	}
	
	public String get() {
		int index = (int)(Math.random() * v.size());
		
		return v.get(index);
	}
	
}
