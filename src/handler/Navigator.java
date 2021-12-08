package handler;

import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import view.MainFrame;

/**
 * ���� ȭ�鿡�� ������ ��ȯ�� ����ϴ� Ŭ�����̴�.
 */
public class Navigator {
	
	private JFrame rootFrame;
	
	private Navigator(JFrame rootFrame) {
		this.rootFrame = rootFrame;
	}
	
	/**
	 * {@code contextComponent}�� ���� �������� ������ �ִ� {@link Navigator} �ν��Ͻ��� ��ȯ�Ѵ�.
	 * @param contextComponent �������� ���� ������Ʈ 
	 * @return {@link Navigator} �ν��Ͻ�
	 */
	public static Navigator of(JComponent contextComponent) {
		return new Navigator((JFrame) SwingUtilities.getWindowAncestor(contextComponent));
	}
	
	/**
	 * {@code component}���� ȭ���� ��ȯ�Ѵ�. 
	 * @param component ��ȯ�� ������Ʈ 
	 */
	public void to(JComponent component) {
		if (rootFrame == null) {
			System.out.println("�׺�������� rootFrame�� �ʱ�ȭ ���� �ʾҽ��ϴ�. ������Ʈ�� �θ�� ����Ǳ� ���� ����ߴ��� Ȯ���ϼ���.");
			return;
		}
		Container c = rootFrame.getContentPane();
		c.removeAll();
		c.add(component);
		c.revalidate(); 
		c.repaint();
	}
	
	/**
	 * {@code contextComponent}�� ���� �����ӿ��� {@code target}���� ȭ���� ��ȯ�Ѵ�. 
	 * @param contextComponent �������� ���� ������Ʈ 
	 * @param target ��ȯ�� ������Ʈ 
	 */
	public static void to(JComponent contextComponent, JComponent target) {
		Navigator.of(contextComponent).to(target);
	}
	
}