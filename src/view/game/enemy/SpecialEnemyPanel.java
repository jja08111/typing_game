package view.game.enemy;

import java.util.Random;
import java.awt.Container;
import java.awt.Point;

import constant.ColorScheme;
import constant.Icons;
import handler.EnemyHandler;
import handler.SoundController;
import model.Sounds;
import view.game.InformationPanel;
import view.game.UserCharacterPanel;

/**
 * 죽을 때 {@link EnemyPanel}을 3개 생성하는 적이다. 이 적을 제거하지 못해 충돌하는 경우 곧바로 게임은 종료된다.
 */
public class SpecialEnemyPanel extends EnemyPanel {

	/**
	 * 이 특별한 적이 죽은 경우 새로 생성할 새로운 적들의 위치를 결정하기 위해 사용하는 원의 반지름이다.
	 */
	private static final double RADIUS = 50.0;
	
	/**
	 * 이 특별한 적이 죽은 경우 새로 생성할 새로운 적들의 갯수이다.
	 */
	private static final int CHILDREN_COUNT = 3;
	
	/**
	 * 죽을 때 일반 적 3개를 생성하는 여부를 지정한다.
	 */
	private boolean giveBirth = true;
	
	/**
	 * 죽을 때 3개의 {@link EnemyPanel}을 생성하는 특별한 적을 생성한다.
	 */
	public SpecialEnemyPanel(EnemyHandler handler, UserCharacterPanel userPanel, InformationPanel infoPanel) {
		super(Icons.SPECIAL_ENEMY, handler, userPanel, infoPanel);
		wordLabel.setForeground(ColorScheme.ON_SECONDARY);
		wordLabel.setBackground(ColorScheme.SECONDARY_VARIANT);
		
		SoundController.play(Sounds.WARNING);
	}

	/**
	 * 죽을 때 일반 적 3개를 생성하지 않도록 한다.
	 */
	public void doNotGiveBirth() {
		giveBirth = false;
	}
	
	/**
	 * 특별한 적을 제거한다. 이때 3개의 일반 적({@link EnemyPanel})이 생성된다.
	 */
	@Override
	public void isKilled() {
		super.isKilled();
		if (giveBirth) {
			final Random r = new Random();
			final int angleDiff = 360 / CHILDREN_COUNT;
			final int startAngle = r.nextInt(360);
			
			final Container parent = getParent();
			final int minY = parent.getY();
			final int maxY = minY + parent.getHeight() - getHeight();
			
			for (int i = 0; i < CHILDREN_COUNT; ++i) {
				Point center = new Point(getX() + getWidth() / 2, getY() + getHeight() / 2);
				
				// 범위를 벗어나지 않게 한다.
				center.y = Math.max(center.y, minY + (int)RADIUS);
				center.y = Math.min(center.y, maxY - (int)RADIUS);
				
				int angle = startAngle + angleDiff * i; 
				Point point = getCirclePoint(center, angle);
				
				handler.createNormalAt(point.x, point.y);
			}
		}
		super.removeThisFromParent();
	}
	
	/**
	 * 특별한 적은 플레이어와 충돌한 경우 게임이 종료된다.
	 */
	@Override
	protected void onCollidedWithUser() {
		super.onCollidedWithUser();
		if (infoPanel.getLife() > 0) {
			infoPanel.decreaseLifeToZero();
		}
	}
	
	/**
	 * 반지름이 {@code RADIUS}인 원의 방정식을 이용하여 해당 각도에 있는 좌표를 구한다.
	 * @param angle 각도
	 * @return 방정식에 값을 넣은 결과 
	 */
	private Point getCirclePoint(Point center, int angle) {
		double radianAngle = Math.toRadians(angle);
		int x = (int)(Math.cos(radianAngle) * RADIUS) + center.x;
		int y =  (int)(Math.sin(radianAngle) * RADIUS) + center.y;
		
		return new Point(x, y);
	}
	
}
