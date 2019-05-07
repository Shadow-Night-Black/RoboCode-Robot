package shadow;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;

import robocode.ScannedRobotEvent;
import robocode.AdvancedRobot;

public class EnemyManager implements Iterable<Enemy> {

	private ArrayList<Enemy> robots;
	private GravityMap gravityMap;
	private WaveManager waves;
	private AdvancedRobot r;
	private Enemy me;

	public EnemyManager(AdvancedRobot r) {
		robots = new ArrayList<Enemy>(r.getOthers());
		gravityMap = new GravityMap(r);
		waves = new WaveManager();
		this.r = r;
		me = new Enemy(r);
	}
	
	public Enemy getTarget(AdvancedRobot r) {
		return getClosest(new Point(r.getX(), r.getY()));
	}

	public boolean knownEnemy(String name) {
		for (Enemy e: robots) {
			if (e.getName().equals(name))
				return true;
		}
		return false;
	}

	public Enemy getEnemy(String name) {
		for (Enemy e : robots) {
			if (e.getName().equals(name))
				return e;
		}
		return null;
	
	}

	public GravityMap getGravityMap() {
		return gravityMap;
	}

	public void addScan(ScannedRobotEvent e, Shadow s) {
		Enemy enemy;
		me.addSelfScan(s);	
		if (knownEnemy(e.getName())) {
			enemy = getEnemy(e.getName());
			enemy.addScan(e, s);
			if (enemy.getEnergyChange()>0.1 && enemy.getEnergyChange()<3) {
				Point shadow = new Point(s.getX(), s.getY());
				BulletWave HeadOn = new BulletWave(enemy.getPoint(), enemy.getPoint().getAbsoluteBearing(shadow), enemy.getEnergyChange(), s);
				AntiBulletWave AHeadOn = new AntiBulletWave(enemy.getPoint(), enemy.getPoint().getAbsoluteBearing(shadow), enemy.getEnergyChange(), s);
				BulletWave Linear = new BulletWave(enemy.getPoint(), enemy.getPoint().getAbsoluteBearing(s.getLinearTarget(me, enemy.getPoint())), enemy.getEnergyChange(),(AdvancedRobot) s);
				AntiBulletWave ALinear = new AntiBulletWave(enemy.getPoint(), enemy.getPoint().getAbsoluteBearing(s.getLinearTarget(me, enemy.getPoint())), enemy.getEnergyChange(),(AdvancedRobot) s);
				gravityMap.add(HeadOn.getGravityBullet());
				gravityMap.add(AHeadOn.getGravityBullet());
				gravityMap.add(Linear.getGravityBullet());
				gravityMap.add(ALinear.getGravityBullet());
				waves.add(HeadOn);
				waves.add(AHeadOn);
				waves.add(Linear);
				waves.add(ALinear);
			}
		} else {
			enemy = new Enemy(e, s);
			robots.add(enemy);
			gravityMap.add(new GravityBot(enemy, -10));
			
		}
	}

	public void remove(String name) {
		robots.remove(getEnemy(name));
	}

	public Enemy getClosest(Point point) {
		Enemy closest = null;
		double closetDistance = 0;
		double robotDistance = 0;
		for (Enemy e : robots) {
			robotDistance = e.getDistance(point);

			if (closest == null || robotDistance < closetDistance) {
				closest = e;
				closetDistance= robotDistance;
			}
		}
		return closest;
	}
	
	public void updateEnergy(String robot, double amount) {
		getEnemy(robot).updateEnergy(amount);
	}

	public int getNumber() {
		return robots.size();
	}

	public Iterator<Enemy> iterator() {
		return robots.iterator();
	}
	
	public void updateWaves() {
		waves.update(gravityMap, r);
	}
	
	public void paintWaves(Graphics2D g, long time) {
		waves.paint(g, time);
	}
}


