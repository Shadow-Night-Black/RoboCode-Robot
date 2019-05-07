package shadow;

public class TargetWave extends Wave {

	private double heading;
	private double bearing;
	private Enemy r;
	
	public TargetWave(Point centre, double velocity, long tick, double heading, double bearing, Enemy r) {
		super(centre, velocity, tick);
		this.heading = heading;
		this.bearing = bearing;
		this.r = r;
	}
	
	public double getHeading() {
		return heading;
	}
	
	public double getBearing() {
		return bearing;
	}
	
	public Enemy getTarget() {
		return r;
	}
	
	public double getMaxEscapeAngle() {
		return Math.asin(8 / velocity);
	}

}
