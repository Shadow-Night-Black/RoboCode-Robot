package shadow;

import java.awt.geom.Line2D;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Line extends Line2D {
	private Point p1;
	private Point p2;

	public Line(Point p1, Point p2) {
		this.p1 = p1;
		this.p2 = p2;
	}

	public Rectangle2D getBounds2D() {
		return new Rectangle2D.Double(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y);
	}

	public Point2D getP1() {
		return p1;
	}

	public Point2D getP2() {
		return p2;
	}

	public double getX1() {
		return p1.x;
	}

	public double getX2() {
		return p2.x;
	}

	public double getY1() {
		return p1.y;
	}

	public double getY2() {
		return p2.y;
	}

	public void setLine(double x1, double y1, double x2, double y2) {
		p1.setLocation(x1, y1);
		p2.setLocation(x2, y2);
	}

	public Point getClosestPoint(Point p) {

		double apx = p.x - p1.x;
		double apy = p.y - p1.y;
		double abx = p2.x - p1.x;
		double aby = p2.y - p1.y;

		double ab2 = abx * abx + aby * aby;
		double ap_ab = apx * abx + apy * aby;

		double t = ap_ab / ab2;

		if (t < 0)
			t = 0;
		else if (t > 1)
			t = 1;

		return new Point(p1.x + abx * t, p1.y + aby * t);
	}
	
}
