package handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeSet;

import model.RecordItem;

public class RecordHandler {
	
	private static final String FILE_NAME = "record.txt";
	
	private FileWriter writer;
	
	private FileReader reader;
	
	private RecordHandler() {
		try {
			// Append 모드로 생성 
			writer = new FileWriter(FILE_NAME, true);
		} catch (IOException e) {
			System.out.println("기록 파일 생성에 실패했습니다.");
			e.printStackTrace();
		}
		
		try {
			reader = new FileReader(FILE_NAME);
		} catch (FileNotFoundException e) {
			System.out.println("기록 파일 불러오기에 실패했습니다.");
			e.printStackTrace();
		}

	}
	
	public static RecordHandler getInstance() {
		return LazyHolder.INSTANCE;
	}
	
	private static class LazyHolder { 
		private static final RecordHandler INSTANCE = new RecordHandler(); 
	}
	
	public void save(RecordItem item) {
		String content = item.toString();
		try {
			writer.write(content, 0, content.length());
			writer.write("\r\n", 0, 2);
			writer.flush(); 
		} catch (IOException e) {
			System.out.println("기록 저장에 실패했습니다.");
			e.printStackTrace();
		}
	}
	
	public TreeSet<RecordItem> readAll() {
		TreeSet<RecordItem> result = new TreeSet<RecordItem>();
		String str = "";
		int c;
		try {
			while ((c = reader.read()) != -1) {
				// 문장의 끝을 만나면 문장을 변환하여 넣는다.
				if ((char)c == '\n') {
					// `\r`을 포함한 앞, 뒤 공백을 제거한 문자열로부터 객체를 얻는다.
					RecordItem item = RecordItem.parse(str.trim());
					result.add(item);
					str = "";
				} else {
					str += (char)c;
				}
			}
		} catch (IOException e) {
			e.printStackTrace(); 
			return null;
		}
		return result;
	}
}
