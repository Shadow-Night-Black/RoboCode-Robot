package shadow;

import java.awt.Graphics2D;

public class GravityBot implements Gravitable{

	private double strength;
	private Enemy e;
	

	public GravityBot(Enemy e, double strength) {
		this.strength = strength;
		this.e = e;
	}
	
	private Point getPoint() {
		return e.getPoint();
	}
	
	public double getXForce(Point p) {
		double absBearing =  p.getAbsoluteBearing(this.getPoint());
		double force = strength / Math.pow(p.distance(this.getPoint()), 2);
		return force * Math.sin(absBearing);
	}
	
	public double getYForce(Point p) {
		double absBearing = p.getAbsoluteBearing(this.getPoint());
		double force = strength / Math.pow(p.distance(this.getPoint()), 2);
		return force * Math.cos(absBearing);
	}

	public void paint(Graphics2D g) {
		g.fillOval((int)this.getPoint().x, (int)this.getPoint().y, 7, 7);
	}
	
}
