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
 * ���� �� {@link EnemyPanel}�� 3�� �����ϴ� ���̴�. �� ���� �������� ���� �浹�ϴ� ��� ��ٷ� ������ ����ȴ�.
 */
public class SpecialEnemyPanel extends EnemyPanel {

	/**
	 * �� Ư���� ���� ���� ��� ���� ������ ���ο� ������ ��ġ�� �����ϱ� ���� ����ϴ� ���� �������̴�.
	 */
	private static final double RADIUS = 50.0;
	
	/**
	 * �� Ư���� ���� ���� ��� ���� ������ ���ο� ������ �����̴�.
	 */
	private static final int CHILDREN_COUNT = 3;
	
	/**
	 * ���� �� �Ϲ� �� 3���� �����ϴ� ���θ� �����Ѵ�.
	 */
	private boolean giveBirth = true;
	
	/**
	 * ���� �� 3���� {@link EnemyPanel}�� �����ϴ� Ư���� ���� �����Ѵ�.
	 */
	public SpecialEnemyPanel(EnemyHandler handler, UserCharacterPanel userPanel, InformationPanel infoPanel) {
		super(Icons.SPECIAL_ENEMY, handler, userPanel, infoPanel);
		wordLabel.setForeground(ColorScheme.ON_SECONDARY);
		wordLabel.setBackground(ColorScheme.SECONDARY_VARIANT);
		
		SoundController.play(Sounds.WARNING);
	}

	/**
	 * ���� �� �Ϲ� �� 3���� �������� �ʵ��� �Ѵ�.
	 */
	public void doNotGiveBirth() {
		giveBirth = false;
	}
	
	/**
	 * Ư���� ���� ���ŵȴ�. �̶� 3���� �Ϲ� ��({@link EnemyPanel})�� �����ȴ�.
	 */
	@Override
	public void onKilled() {
		super.onKilled();
		if (giveBirth) {
			final Random r = new Random();
			final int angleDiff = 360 / CHILDREN_COUNT;
			final int startAngle = r.nextInt(360);
			
			final Container parent = getParent();
			final int minY = parent.getY();
			final int maxY = minY + parent.getHeight() - getHeight();
			
			for (int i = 0; i < CHILDREN_COUNT; ++i) {
				Point center = new Point(getX() + getWidth() / 2, getY() + getHeight() / 2);
				
				// ������ ����� �ʰ� �Ѵ�.
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
	 * Ư���� ���� �÷��̾�� �浹�� ��� ������ ����ȴ�.
	 */
	@Override
	protected void onCollidedWithUser() {
		super.onCollidedWithUser();
		if (infoPanel.getLife() > 0) {
			infoPanel.decreaseLifeToZero();
		}
	}
	
	/**
	 * �������� {@code RADIUS}�� ���� �������� �̿��Ͽ� �ش� ������ �ִ� ��ǥ�� ���Ѵ�.
	 * @param angle ����
	 * @return �����Ŀ� ���� ���� ��� 
	 */
	private Point getCirclePoint(Point center, int angle) {
		double radianAngle = Math.toRadians(angle);
		int x = (int)(Math.cos(radianAngle) * RADIUS) + center.x;
		int y =  (int)(Math.sin(radianAngle) * RADIUS) + center.y;
		
		return new Point(x, y);
	}
	
}