package handler;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

/**
 * 텍스트 소스들을 읽어오고 추가 삭제하는 클래스이다.
 * 싱글톤으로 구현한 이유는 한 번만 모든 단어 소스를 읽어오기 위해서이다. Lazy holder 패턴으로 구현했다. 
 */
public class TextSourceHandler {
	
	private static final String FILE_NAME = "words.txt";
	
	private Vector<String> v = new Vector<String>();

	private FileReader reader;
	
	private TextSourceHandler() {
		try {
			reader = new FileReader(FILE_NAME);
			
			String word = "";
			int c;
			while ((c = reader.read()) != -1) {
				// 단어의 끝을 만나면 단어 목록에 넣는다. 
				if ((char)c == '\n') {
					// 텍스트의 `\r`을 포함한 앞, 뒤 공백을 제거한다.
					v.add(word.trim());
					word = "";
				} else {
					word += (char)c;
				}
			}
		} catch (FileNotFoundException e) { // 파일이 없는 경우 새로 샘플을 만든다.
			System.out.println("words.txt 파일이 존재하지 않아 샘플 파일이 생성되었습니다.");
			// TODO: 샘플파일 생성하는 함수 만들기 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static TextSourceHandler getInstance() {
		return LazyHolder.INSTANCE;
	}
	
	private static class LazyHolder { 
		private static final TextSourceHandler INSTANCE = new TextSourceHandler(); 
	}
	
	public String getRandom() {
		int index = (int)(Math.random() * v.size());
		
		return v.get(index);
	}
	
}
