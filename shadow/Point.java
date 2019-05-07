package shadow;

import java.awt.geom.Point2D.Double;

public class Point extends Double {

	private static final long serialVersionUID = 1L;

	public Point() {
		super();
	}

	public Point(double arg0, double arg1) {
		super(arg0, arg1);
	}

	public double getAbsoluteBearing(Point point) {
		return Math.atan2(point.x - this.x, point.y - this.y);
	}
	
	public boolean equals(Point p) {
		return (this.x == p.x && this.y == p.y);
	}

	public double getAbsoluteBearing(double x, double y) {
		return getAbsoluteBearing(new Point(x,y));
	}

}
