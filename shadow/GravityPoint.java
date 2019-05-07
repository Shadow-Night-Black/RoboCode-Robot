package shadow;

import java.awt.Graphics2D;

public class GravityPoint extends Point implements Gravitable{

	private static final long serialVersionUID = 1L;

	private double strength;
	
	public GravityPoint(double arg0, double arg1, double strength){
		super(arg0, arg1);
		this.strength = strength;
	}
	
	public double getXForce(Point p) {
		double absBearing =  p.getAbsoluteBearing(this);
		double force = strength / Math.pow(this.distance(p), 2);
		return force * Math.sin(absBearing);
	}
	
	public double getYForce(Point p) {
		double absBearing = p.getAbsoluteBearing(this);
		double force = strength / Math.pow(this.distance(p), 2);
		return force * Math.cos(absBearing);
	}

	public void paint(Graphics2D g) {
		g.fillOval((int)x, (int)y, 7, 7);
	}
	
}
