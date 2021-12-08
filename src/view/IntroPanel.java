package view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import constant.Icons;
import constant.TextStyle;
import handler.Navigator;
import view.component.CharacterPanel;
import view.component.DefaultButton;
import view.component.TitlePanel;
import view.game.GamePanel;

/**
 * ���� �������� �� ���� ���� ���̴� ���� ȭ���̴�.
 */
public class IntroPanel extends TitlePanel {
	
	public IntroPanel(MainFrame mainFrame) {
		super(mainFrame, "Ÿ���� ��Ʈ", new Point(300, 112), false);
		mainFrame.setJMenuBar(null);
		
		initIcons();
		
		DefaultButton wordEditButton = getButton("�ܾ�����");
		DefaultButton recordButton = getButton("���");
		DefaultButton startButton = getButton("���ӽ���");
		
		JPanel buttonBarPanel = new JPanel();
		buttonBarPanel.setSize(200, 300);
		buttonBarPanel.setLocation(400, 360);
		buttonBarPanel.setLayout(new FlowLayout());
		buttonBarPanel.add(wordEditButton);
		buttonBarPanel.add(recordButton);
		buttonBarPanel.add(startButton);
		
		add(buttonBarPanel);
		
		wordEditButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Navigator.to(IntroPanel.this, new WordEditPanel(mainFrame));
			}
		});
		recordButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Navigator.to(IntroPanel.this, new RecordPanel(mainFrame));
			}
		});
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Navigator.to(IntroPanel.this, new GamePanel(mainFrame));
			}
		});
		// ��Ʈ�� ȭ�� ���Խ� ���� ��ư�� ��Ŀ���� ������ �Ѵ�.
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				startButton.requestFocus();
				removeComponentListener(this);
			}
		});
	}
	
	private void initIcons() {
		addCharacters(Icons.USER_CHARACTER, "�÷��̾�", 120, 190);
		
		addCharacters(Icons.NORMAL_ENEMY, "��", 800, 150);
		addCharacters(Icons.SPECIAL_ENEMY, "Ư���� ��, �� ���� ���� ���ϸ� �ٷ� ���ӿ���. ���� �� �� 3���� �����.", 730, 190);
		addCharacters(Icons.STOP_ITEM_ENEMY, "���� ������ ��, �� ���� ���Ž� ��� ���� 3�ʰ� �����.", 850, 220);
		addCharacters(Icons.BOMB_ITEM_ENEMY, "��ź ������ ��, �� ���� ���Ž� 3���� ���� ���� ���ŵȴ�.", 770, 240);
	}
	
	private void addCharacters(ImageIcon icon, String tooltip, int x, int y) {
		CharacterPanel character = new CharacterPanel(icon, 90, 100, 0);
		character.setLocation(x, y);
		character.setSize(120, 120);
		character.setToolTipText(tooltip);
		add(character);
		// ���߿� �߰��� ���̺��� ���� ���� �ö������ �Ѵ�.
		setComponentZOrder(character, 0);
	}
	
	private DefaultButton getButton(String label) {
		DefaultButton result = new DefaultButton(label);
		result.setFont(TextStyle.BUTTON);
		result.setPreferredSize(new Dimension(200, 40));
		return result;
	}
	
}
