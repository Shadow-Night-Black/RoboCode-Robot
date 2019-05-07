package shadow;

import java.awt.Graphics2D;

public class Wave {
	protected final Point origin;
	protected double velocity;
	private long tick;

	public Wave(Point centre, double velocity, long tick) {
		super();
		this.origin = centre;
		this.velocity = velocity;
		this.tick = tick;
	}

	public Point getOrigin() {
		return origin;
	}

	public double getVelocity() {
		return velocity;
	}

	public double getDistance(long time) {
		return (time - tick) * velocity;
	}
	
	public void remove() {}
	
	public void paint(Graphics2D g, long time) {
		double radius = (time - tick) * velocity;
		g.drawOval((int)(origin.x - radius), (int)(origin.y - radius), (int)radius * 2, (int)radius *2);
	}
	
}
