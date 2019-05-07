package shadow;

import robocode.ScannedRobotEvent;
import robocode.AdvancedRobot;

public class RobotData {
	private Point coords;
	private long tick;
	private double heading;
	private double velocity;
	private double energy;


	public RobotData(ScannedRobotEvent e, AdvancedRobot r) {
		this.tick = e.getTime();
		this.heading = e.getHeadingRadians();
		this.velocity = e.getVelocity();
		this.energy = e.getEnergy();
		 
		double x = r.getX() + e.getDistance() * Math.sin(e.getBearingRadians() + r.getHeadingRadians());
		double y = r.getY() + e.getDistance() * Math.cos(e.getBearingRadians() + r.getHeadingRadians());
		this.coords = new Point(x, y);
	}
	
	public RobotData(AdvancedRobot r) {
		this.tick = r.getTime();
		this.heading = r.getHeadingRadians();
		this.velocity = r.getVelocity();
		this.energy = r.getEnergy();
		this.coords = new Point(r.getX(), r.getY());
	}

	public double getX() {
		return coords.x;
	}

	public double getY() {
		return coords.y;
	}
	
	public Point getPoint() {
		return coords;
	}

	public long getTick() {
		return tick;
	}

	public double getHeading() {
		return heading;
	}

	public double getVelocity() {
		return velocity;
	}

	public double getEnergy() {
		return energy;
	}



	public double getDistance(Point point) {
		return coords.distance(point);
	}
	
	private double getFutureX(long tick) {
		long dt = tick - this.tick;
		return coords.x + this.velocity * dt * Math.sin(this.heading);
		
	}

	private double getFutureY(long tick) {
		long dt = tick - this.tick;
		return coords.y + this.velocity * dt * Math.cos(this.heading);
		
	}
	
	public Point getFuturePoint(long tick) {
		return new Point(getFutureX(tick), getFutureY(tick));
	}
	
	public void updateEnergy(double amount) {
		energy += amount;
	}
}


