package shadow;

import java.awt.Graphics2D;
import java.util.ArrayList;

import robocode.AdvancedRobot;

public class WaveManager {
	private ArrayList<BulletWave> bWaves;
	private ArrayList<TargetWave> tWaves;
	
	public WaveManager() {
		bWaves = new ArrayList<BulletWave>();
		tWaves = new ArrayList<TargetWave>();
	}
	
	public void add(BulletWave bw){
		bWaves.add(bw);
	}
	
	public void add(TargetWave tw){
		tWaves.add(tw);
	}
	
	public void remove(BulletWave bw) {
		bWaves.remove(bw);
	}
	
	public void remove(TargetWave tw) {
		tWaves.remove(tw);
	}
	
	public void update(GravityMap gravityMap, AdvancedRobot r) {
		BulletWave w;
		for (int i = 0; i < bWaves.size(); i++) {
			w = bWaves.get(i);
			if (w.getDistance(r.getTime()) > 20 + w.getOrigin().distance(new Point(r.getX(), r.getY()))) {
				gravityMap.remove(w.getGravityBullet());
				bWaves.remove(w);
				i--;
			}
		}
		
		for (TargetWave t: tWaves) {
			if (t.getDistance(r.getTime()) < t.getOrigin().distance(t.getTarget().getPoint())) {
				double newPredicition = t.getOrigin().getAbsoluteBearing(t.getTarget().getPoint());
				double newOffset = newPredicition - t.getBearing();
				double guessFactor = Math.max(-1, Math.min(1, newOffset / t.getMaxEscapeAngle())) * t.getHeading();
				int index = (int) Math.round((returnSegment.length -1) / 2 * (guessFactor + 1));
				
			}
		}
	}
	
	public void paint(Graphics2D g, long tick) {
		for (BulletWave b: bWaves) {
			b.paint(g, tick);
		}
		for (TargetWave t: tWaves) {
			t.paint(g, tick);
		}
	}
}
