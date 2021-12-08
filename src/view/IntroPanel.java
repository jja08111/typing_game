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
 * 앱을 실행했을 때 제일 먼저 보이는 진입 화면이다.
 */
public class IntroPanel extends TitlePanel {
	
	public IntroPanel(MainFrame mainFrame) {
		super(mainFrame, "타이핑 고스트", new Point(300, 112), false);
		mainFrame.setJMenuBar(null);
		
		initIcons();
		
		DefaultButton wordEditButton = getButton("단어편집");
		DefaultButton recordButton = getButton("기록");
		DefaultButton startButton = getButton("게임시작");
		
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
		// 인트로 화면 진입시 시작 버튼에 포커스가 가도록 한다.
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				startButton.requestFocus();
				removeComponentListener(this);
			}
		});
	}
	
	private void initIcons() {
		addCharacters(Icons.USER_CHARACTER, "플레이어", 120, 190);
		
		addCharacters(Icons.NORMAL_ENEMY, "적", 800, 150);
		addCharacters(Icons.SPECIAL_ENEMY, "특별한 적, 이 적을 제거 못하면 바로 게임오버. 죽을 때 적 3개를 만든다.", 730, 190);
		addCharacters(Icons.STOP_ITEM_ENEMY, "정지 아이템 적, 이 적을 제거시 모든 적이 3초간 멈춘다.", 850, 220);
		addCharacters(Icons.BOMB_ITEM_ENEMY, "폭탄 아이템 적, 이 적을 제거시 3개의 적이 같이 제거된다.", 770, 240);
	}
	
	private void addCharacters(ImageIcon icon, String tooltip, int x, int y) {
		CharacterPanel character = new CharacterPanel(icon, 90, 100, 0);
		character.setLocation(x, y);
		character.setSize(120, 120);
		character.setToolTipText(tooltip);
		add(character);
		// 나중에 추가된 레이블이 가장 위로 올라오도록 한다.
		setComponentZOrder(character, 0);
	}
	
	private DefaultButton getButton(String label) {
		DefaultButton result = new DefaultButton(label);
		result.setFont(TextStyle.BUTTON);
		result.setPreferredSize(new Dimension(200, 40));
		return result;
	}
	
}
