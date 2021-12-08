package handler;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import model.RecordItem;

/**
 * 기록을 저장하고 읽어오는 클래스이다.
 */
public class RecordHandler {
	
	private static final String FILE_NAME = "assets/datas/record.txt";
	
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
	 * 저장된 모든 기록을 벡터로 반환한다.
	 * @return 벡터로 된 저장된 모든 기록
	 * @throws IOException 파일을 열고 읽는 과정에서 파일 입출력 예외 발생시 던진다.
	 */
	public static Vector<RecordItem> readAll() throws IOException {
		FileReader reader;
		try {
			reader = new FileReader(FILE_NAME);	
			
			Vector<RecordItem> result = new Vector<RecordItem>();
			String str = "";
			int c;
			
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
			reader.close();
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return null;
	}
}