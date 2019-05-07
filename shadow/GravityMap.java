package shadow;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;

import robocode.AdvancedRobot;

public class GravityMap implements Iterable<Gravitable> {
	private ArrayList<Gravitable> grav;

	public GravityMap(AdvancedRobot r) {
		grav = new ArrayList<Gravitable>();
		Point[] points = { new Point(0, 0),
				new Point(0, r.getBattleFieldHeight()),
				new Point(r.getBattleFieldWidth(), 0),
				new Point(r.getBattleFieldWidth(), r.getBattleFieldHeight()) };
		this.add(new GravityWall(points[0], points[1], -500));
		this.add(new GravityWall(points[0], points[2], -500));
		this.add(new GravityWall(points[1], points[3], -500));
		this.add(new GravityWall(points[2], points[3], -500));
	}

	public void add(Gravitable p) {
		grav.add(p);
	}

	public void remove(Gravitable p) {
		grav.remove(p);
	}

	public double getResultantForce(AdvancedRobot r) {
		Point coords = new Point(r.getX(), r.getY());
		double xForce = 0;
		double yForce = 0;

		for (Gravitable p : grav) {
			xForce += p.getXForce(coords);
			yForce += p.getYForce(coords);
		}


		return Math.atan2(xForce, yForce);
	}

	public Iterator<Gravitable> iterator() {
		return grav.iterator();
	}

	public void paint(Graphics2D g) {
		for (Gravitable gp : grav) {
			gp.paint(g);
		}
	}
}
