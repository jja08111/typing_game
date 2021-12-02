package handler;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

/**
 * 텍스트 소스들을 읽어오고 추가 삭제하는 클래스이다.
 * 싱글톤으로 구현한 이유는 한 번만 모든 단어 소스를 읽어오기 위해서이다. Lazy holder 패턴으로 구현했다. 
 */
public class TextSourceHandler {
	
	private static final String FILE_NAME = "assets/datas/words.txt";
	
	private Vector<String> v = new Vector<String>();

	private TextSourceHandler() {
		try {
			FileReader reader = new FileReader(FILE_NAME);
			
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
			reader.close();
		} catch (FileNotFoundException e) { // 파일이 없는 경우 새로 샘플을 만든다.
			System.out.println("words.txt 파일이 존재하지 않아 샘플 파일이 생성되었습니다.");
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
	
	public Vector<String> getAll() {
		return v;
	}
	
	/**
	 * 단어 목록 파일에 새로운 단어를 추가한다.
	 * @param word 새로 추가할 단어 
	 * @throws IOException 파일 열기에 실패한 경우 던진다.
	 * @throws IllegalArgumentException 이미 포함된 단어를 전달한 경우 던진다.
	 */
	public void add(String word) throws IOException, IllegalArgumentException {
		word = word.trim();
		
		if (v.contains(word))
			throw new IllegalArgumentException("이미 포함된 단어입니다.");
		v.add(word);
		// append 모드로 파일을 연다.
		FileWriter writer = new FileWriter(FILE_NAME, true);
		writer.write(word + "\n");
		writer.close();
	}
	
	/**
	 * 단어 목록 파일에서 단어를 삭제한다.
	 * @param word 삭제할 단어 
	 * @throws IOException 파일 입출력에 실패한 경우 던진다.
	 */
	public void remove(String word) throws IOException {
		FileWriter writer = new FileWriter(FILE_NAME);
		v.remove(word);
		for (String w : v) {
			writer.append(w + "\n");
		}
	}
	
}
