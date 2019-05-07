package shadow;

import robocode.Rules;

public class ShadowBullet {
	private Point origin;
	private double bearing;
	private double power;
	private double tick;
	
	public ShadowBullet(Point origin, double bearing, double power, long tick) {
		this.origin = origin;
		this.bearing = bearing;
		this.power = power;
		this.tick = tick;
	}

	public Point getPoint(long time) {
		time -= tick;
		return new Point(origin.x + Math.sin(bearing) * time * Rules.getBulletSpeed(power), origin.y + Math.cos(bearing) * time * Rules.getBulletSpeed(power));
	}

	public double getBearing() {
		return bearing;
	}

	public double getPower() {
		return power;
	}
	
	public double getDistance(long time) {
		return (time - tick) * Rules.getBulletSpeed(power);
	}
}
