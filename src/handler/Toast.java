package handler;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import constant.TextStyle;

/**
 * ������ �޽����� ����ڿ��� �����ϱ� ���� Ŭ�����̴�.
 */
public class Toast extends JDialog {
	
	private Toast() {
		
	}
	
	/**
	 * �佺Ʈ �޽����� ������ �и��ʵ��� ����.
	 * @param msg ���� �޽��� 
	 * @param millisec �佺Ʈ�� ��� �и��� ���� �Ⱓ 
	 * @param contextComponent {@link JFrame}�� ������� ����� ������Ʈ�̴�. ��Ʈ �����Ӱ� ������ �Ǿ��־�� �Ѵ�.
	 */
	public static void show(String msg, int millisec, JComponent contextComponent) {
		JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(contextComponent);
		if (frame == null) { 
			System.out.println("�׺�������� rootFrame�� �ʱ�ȭ ���� �ʾҽ��ϴ�. ������Ʈ�� �θ�� ����Ǳ� ���� ����ߴ��� Ȯ���ϼ���.");
			return;
		}
		
		Toast toast = new Toast();
		
		// ���̾�α��� ������ �ٸ� ������ �ʰ� �Ѵ�.
		toast.setUndecorated(true);
		
		toast.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.GRAY);
		toast.getContentPane().add(panel, BorderLayout.CENTER);
		
		JLabel toastLabel = new JLabel("");
		toastLabel.setText(msg);
		toastLabel.setFont(TextStyle.BODY_TEXT1);
		toastLabel.setForeground(Color.WHITE);
		
		toast.setAlwaysOnTop(true);
		// �佺Ʈ ũ�⸦ ���Ѵ�.
		toast.setBounds(100, 100, toastLabel.getPreferredSize().width + 20, 32);
		
		// �������� �߾� �ϴܺο� ��ġ��Ų��.
		int y = frame.getY() + frame.getHeight() / 2 - toast.getSize().height / 2;
		int x = frame.getX() + frame.getWidth() / 2 - toast.getSize().width / 2;
		toast.setLocation(x, y + y / 2);
		panel.add(toastLabel);
		toast.setVisible(true);
		
		new Thread(){
		    public void run() {
		        try {
		            Thread.sleep(millisec);
		            toast.dispose();
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
		    }
		}.start();
	}
	
	/**
	 * �佺Ʈ �޽����� 3�ʰ� ����.
	 * @param msg ���� �޽��� 
	 * @param contextComponent {@link JFrame}�� ������� ����� ������Ʈ�̴�. ��Ʈ �����Ӱ� ������ �Ǿ��־�� �Ѵ�.
	 */
	public static void show(String msg, JComponent contextComponent) {
		show(msg, 3000, contextComponent);
	}
	
}