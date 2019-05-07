package shadow;

import java.awt.Graphics2D;

import robocode.AdvancedRobot;

public class GravityBullet implements Gravitable{

	private double strength;
	private ShadowBullet b;
	private AdvancedRobot r;
	

	public GravityBullet(ShadowBullet b, double strength, AdvancedRobot r) {
		this.strength = strength;
		this.b = b;
		this.r = r;
	}
	
	private Point getPoint(long time) {
		return b.getPoint(time);
	}
	
	public double getXForce(Point p) {
		double absBearing =  p.getAbsoluteBearing(this.getPoint(r.getTime()));
		double force = strength / Math.pow(p.distance(this.getPoint(r.getTime())), 2);
		return force * Math.sin(absBearing);
	}
	
	public double getYForce(Point p) {
		double absBearing = p.getAbsoluteBearing(this.getPoint(r.getTime()));
		double force = strength / Math.pow(p.distance(this.getPoint(r.getTime())), 2);
		return force * Math.cos(absBearing);
	}

	public void paint(Graphics2D g) {
		g.fillOval((int)this.getPoint(r.getTime()).x, (int)this.getPoint(r.getTime()).y, 7, 7);
	}
}
