package shadow;

import java.awt.Graphics2D;

public interface Gravitable {
	abstract double getXForce(Point p);
	abstract double getYForce(Point p);
	abstract void paint(Graphics2D g);
}
