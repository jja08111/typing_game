package handler;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import model.RecordItem;

/**
 * ����� �����ϰ� �о���� Ŭ�����̴�.
 */
public class RecordHandler {
	
	private static final String FILE_NAME = "assets/datas/record.txt";
	
	/**
	 * ��� �����͸� �����Ѵ�.
	 * @param item ���� ������ ��� �������̴�.
	 * @throws IOException ������ ���� ���� �������� ���� ����� ���� �߻��� ������.
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
	 * ����� ��� ����� ���ͷ� ��ȯ�Ѵ�.
	 * @return ���ͷ� �� ����� ��� ���
	 * @throws IOException ������ ���� �д� �������� ���� ����� ���� �߻��� ������.
	 */
	public static Vector<RecordItem> readAll() throws IOException {
		FileReader reader;
		try {
			reader = new FileReader(FILE_NAME);	
			
			Vector<RecordItem> result = new Vector<RecordItem>();
			String str = "";
			int c;
			
			while ((c = reader.read()) != -1) {
				// ������ ���� ������ ������ ��ȯ�Ͽ� �ִ´�.
				if ((char)c == '\n') {
					// `\r`�� ������ ��, �� ������ ������ ���ڿ��κ��� ��ü�� ��´�.
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