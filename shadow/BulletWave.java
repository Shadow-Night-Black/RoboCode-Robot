package shadow;

import java.awt.Color;
import java.awt.Graphics2D;

import robocode.AdvancedRobot;
import robocode.Rules;

public class BulletWave extends Wave {

	protected GravityBullet gb;
	protected ShadowBullet sb;
	
	public BulletWave(Point centre, double bearing, double power, AdvancedRobot r) {
		super(centre, Rules.getBulletSpeed(power), r.getTime() - 1);
		this.sb = new ShadowBullet(centre, bearing, power, r.getTime() - 1);
		this.gb = new GravityBullet(sb, -15, r);
	}
	
	public GravityBullet getGravityBullet() {
		return gb;
	}
	
	public void paint(Graphics2D g, long tick){
		g.setColor(Color.GREEN);
		super.paint(g, tick);
		g.setColor(Color.RED);
		gb.paint(g);
	}
}
