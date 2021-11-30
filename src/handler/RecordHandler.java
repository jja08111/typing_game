package handler;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import model.RecordItem;

public class RecordHandler {
	
	private static final String FILE_NAME = "record.txt";
	
	/**
	 * 기록 데이터를 저장한다.
	 * @param item 새로 저장할 기록 데이터이다.
	 * @throws IOException 파일을 열고 쓰는 과정에서 파일 입출력 예외 발생시 던진다.
	 */
	public static void save(RecordItem item) throws IOException {
		FileWriter writer = new FileWriter(FILE_NAME, true);
		String content = item.toString();
		
		writer.write(content);
		writer.write("\r\n");
		writer.flush(); 
		writer.close();
	}
	
	/**
	 * 저장된 모든 기록을 2차원 벡터로 반환한다. 가로 열은 이름, 점수, 단계 순으로 되어있다.
	 * @return 저장된 2차원 벡터 반환
	 * @throws IOException 파일을 열고 읽는 과정에서 파일 입출력 예외 발생시 던진다.
	 */
	public static Vector<Vector<Object>> readAll() throws IOException {
		FileReader reader = new FileReader(FILE_NAME);
		Vector<Vector<Object>> result = new Vector<Vector<Object>>();
		String str = "";
		int c;
		
		while ((c = reader.read()) != -1) {
			// 문장의 끝을 만나면 문장을 변환하여 넣는다.
			if ((char)c == '\n') {
				// `\r`을 포함한 앞, 뒤 공백을 제거한 문자열로부터 객체를 얻는다.
				RecordItem item = RecordItem.parse(str.trim());
				result.add(item.split());
				str = "";
			} else {
				str += (char)c;
			}
		}
		reader.close();
		return result;
	}
}
