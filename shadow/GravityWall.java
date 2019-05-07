package shadow;

import java.awt.Graphics2D;


public class GravityWall extends Line implements Gravitable {

	private double strength;
	public GravityWall(Point p1, Point p2, double strength) {
		super(p1, p2);
		this.strength = strength;
	}

	public double getXForce(Point p) {
		Point closest = getClosestPoint(p);
		double distance = p.distance(closest);
		double absBearing =  p.getAbsoluteBearing(closest);

		double force = strength / Math.pow(distance, 3);
		return force * Math.sin(absBearing);
	}
	
	public double getYForce(Point p) {
		Point closest = getClosestPoint(p);
		double distance = p.distance(closest);
		double absBearing = p.getAbsoluteBearing(closest);
		
		double force = strength / Math.pow(distance, 3);
		return force * Math.cos(absBearing);
	}

	public void paint(Graphics2D g) {
		g.draw(this);
	}
}
