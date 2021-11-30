package handler;

import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import view.MainFrame;

public class Navigator {
	
	private JFrame rootFrame;
	
	private Navigator(JFrame rootFrame) {
		this.rootFrame = rootFrame;
	}
	
	public static Navigator of(JComponent component) {
		return new Navigator((JFrame) SwingUtilities.getWindowAncestor(component));
	}
	
	public void to(JComponent component) {
		if (rootFrame == null) {
			System.out.println("네비게이터의 rootFrame이 초기화 되지 않았습니다. 컴포넌트가 부모와 연결되기 전에 사용했는지 확인하세요.");
			return;
		}
		Container c = rootFrame.getContentPane();
		c.removeAll();
		c.add(component);
		c.revalidate(); 
		c.repaint();
	}
	
	public static void to(JComponent source, JComponent target) {
		Navigator.of(source).to(target);
	}
	
}
