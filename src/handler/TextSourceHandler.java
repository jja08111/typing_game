package handler;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

/**
 * �ؽ�Ʈ �ҽ����� �о���� �߰� �����ϴ� Ŭ�����̴�.
 * �̱������� ������ ������ �� ���� ��� �ܾ� �ҽ��� �о���� ���ؼ��̴�. Lazy holder �������� �����ߴ�. 
 */
public class TextSourceHandler {
	
	private static final int INIT_WORD_COUNT = 25000;
	
	private static final String FILE_NAME = "assets/datas/words.txt";
	
	private Vector<String> v = new Vector<String>(INIT_WORD_COUNT);

	private TextSourceHandler() {
		try {
			FileReader reader = new FileReader(FILE_NAME);
			
			String word = "";
			int c;
			while ((c = reader.read()) != -1) {
				// �ܾ��� ���� ������ �ܾ� ��Ͽ� �ִ´�. 
				if ((char)c == '\n') {
					// �ؽ�Ʈ�� `\r`�� ������ ��, �� ������ �����Ѵ�.
					v.add(word.trim());
					word = "";
				} else {
					word += (char)c;
				}
			}
			// ������ �ܾ �ִ´�.
			if (!word.isBlank())
				v.add(word.trim());
			
			reader.close();
		} catch (FileNotFoundException e) { // ������ ���� ��� ���� ������ �����.
			System.out.println("words.txt ������ �������� �ʾ� ���� ������ �����Ǿ����ϴ�.");
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
	
	public synchronized String getRandom() {
		int index = (int)(Math.random() * v.size());
		
		return v.get(index);
	}
	
	public synchronized Vector<String> getAll() {
		return v;
	}
	
	/**
	 * �ܾ� ��� ���Ͽ� ���ο� �ܾ �߰��Ѵ�.
	 * @param word ���� �߰��� �ܾ� 
	 * @throws IOException ���� ���⿡ ������ ��� ������.
	 * @throws IllegalArgumentException �̹� ���Ե� �ܾ ������ ��� ������.
	 */
	public synchronized void add(String word) throws IOException, IllegalArgumentException {
		word = word.trim();
		
		if (v.contains(word))
			throw new IllegalArgumentException("�̹� ���Ե� �ܾ��Դϴ�.");
		v.add(word);
		// append ���� ������ ����.
		FileWriter writer = new FileWriter(FILE_NAME, true);
		writer.write(word + "\n");
		writer.close();
	}
	
	/**
	 * �ܾ� ��� ���Ͽ��� �ܾ �����Ѵ�.
	 * @param word ������ �ܾ� 
	 */
	public synchronized void remove(String word) {
		new Thread() {
			@Override
			public void run() {
				FileWriter writer;
				try {
					writer = new FileWriter(FILE_NAME);
					v.remove(word);
					for (String w : v) {
						writer.append(w + "\n");
					}
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
}