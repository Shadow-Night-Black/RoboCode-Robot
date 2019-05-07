package shadow;

import java.awt.Color;
import java.awt.Graphics2D;

import robocode.AdvancedRobot;

public class AntiBulletWave extends BulletWave {
	
	public AntiBulletWave(Point centre, double bearing, double power, AdvancedRobot r) {
		super(mirrored(centre, r), mirroredBearing(centre, bearing, r), power, r);
	}
	
	private static Point mirrored(Point p, AdvancedRobot r) {
		return new Point(2*r.getX() - p.x, 2*r.getY() - p.y);
	}
	
	private static double mirroredBearing(Point p1, double bearing, AdvancedRobot r) {
		Point p2 = mirrored(p1, r);
		double b = 2*Math.PI - p2.getAbsoluteBearing(p1) - (p1.getAbsoluteBearing(p2) - bearing);
		return -b;
	}
	public void paint(Graphics2D g, long tick){
		g.setColor(Color.ORANGE);
		super.paint(g, tick);
		g.setColor(Color.RED);
		gb.paint(g);
	}
}


