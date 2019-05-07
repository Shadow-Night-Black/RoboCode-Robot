package shadow;

import robocode.AdvancedRobot;
import robocode.Rules;

import java.awt.Color;
import java.awt.Graphics2D;

import robocode.BulletHitEvent;
import robocode.HitByBulletEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

public class Shadow extends AdvancedRobot {
	private EnemyManager robots;

	private byte moveDirection = 1;
	private double moveAngle;

	private enum ScanMode {LOCKON, OPTIMAL, EVERYTHING};
	private ScanMode scanState = ScanMode.EVERYTHING;

	private enum FireMode {HEADON, LINEAR};
	private FireMode fireState = FireMode.LINEAR;

	private enum MoveMode {CIRCULAR, GRAVITY};
	private MoveMode moveState = MoveMode.GRAVITY;

	private Point aimPoint = new Point();
	private Point coords;
	//private GravityPoint random;  //removed random point as it impacted bullet dodging and made it go in to the centre too often

	double moveBearing = 0;

	public void run() {
		this.setColors(Color.DARK_GRAY, Color.BLACK, Color.ORANGE);
		robots = new EnemyManager(this);
		this.coords = new Point(getX(), getY());
		//random = new GravityPoint(getBattleFieldWidth()/2, getBattleFieldHeight()/2, 5);
		//robots.getGravityMap().add(random);

		do {
			radar();
			execute();
		} while (robots.getTarget(this) == null);
		do {
			out.println();

			for(ScannedRobotEvent e: getScannedRobotEvents()) {
				this.onScannedRobot(e);
			}
			
			robots.updateWaves();
			
			radar();
			aim();
			shoot();
			move();

			execute();
		} while (robots.getTarget(this) != null);

	}

	public void onPaint(Graphics2D g) {
		g.setColor(Color.RED);
		g.fillRect((int) aimPoint.x, (int) aimPoint.y, 10, 10);
		g.drawLine((int)coords.x, (int)coords.y, (int)(coords.x + 50 * Math.sin(getHeadingRadians())), (int)(coords.y + 50 * Math.cos(getHeadingRadians())));

		Enemy target = robots.getTarget(this);
		if (target != null) {
			g.drawRect((int) target.getPoint().x - 16, (int) target.getPoint().y - 16, 32, 32);

			g.setColor(Color.CYAN);
			robots.getGravityMap().paint(g);
			g.drawLine((int)coords.x, (int)coords.y, (int)(coords.x + 50 * Math.sin(moveBearing)), (int)(coords.y + 50 * Math.cos(moveBearing)));
		}
		
		g.setColor(Color.BLUE);
		robots.paintWaves(g, getTime());
	}

	private void radar() {
		Enemy target = robots.getTarget(this);

		switch (scanState) {

		case EVERYTHING:

			setAdjustRadarForGunTurn(false);
			setAdjustRadarForRobotTurn(false);
			setTurnRightRadians(Double.POSITIVE_INFINITY);
			setTurnGunRightRadians(Double.POSITIVE_INFINITY);
			setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
			break;

		case LOCKON:
			 double radarTurn = this.coords.getAbsoluteBearing(target.getPoint()) - getRadarHeadingRadians();
		    setTurnRadarRightRadians(normalizeBearing(radarTurn)*2);
		 
			break;

		case OPTIMAL:
			double absMaxBearing = 0;
			int numberScanned = 0;

			for (Enemy e : robots) {
				if (getTime() - e.getLastScanTime() > 12) {
					double bearing = normalizeBearing(this.coords.getAbsoluteBearing(e.getPoint()) - getRadarHeadingRadians());
					if (Math.abs(bearing) > absMaxBearing) {
						absMaxBearing = Math.abs(bearing);
					}
				} else {
					numberScanned++;
				}
			}

			if (numberScanned == getOthers()) {
				scanState = ScanMode.LOCKON;
				radar();
			} else {
				setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
			}
			break;
		}
	}

	private void move() {

		switch (moveState) {

		case CIRCULAR:

			Enemy target = robots.getTarget(this);
			moveAngle = this.coords.getAbsoluteBearing(target.getPoint())
					- this.getHeadingRadians();
			moveAngle += Math.PI / 2 - moveDirection * Math.PI / 8
					* Math.signum(target.getDistance(coords) - 215);
			setTurnRightRadians(normalizeBearing(moveAngle));

			if (this.getVelocity() == 0) {
				moveDirection *= -1;
			}
			setAhead(100 * moveDirection);
			break;

		case GRAVITY:
			
			GravityMap gm = robots.getGravityMap();
			double bearing = normalizeBearing(gm.getResultantForce(this));
			moveBearing = bearing;
			
			if (getTime() % 20 == 0) {
				//double randomX = getBattleFieldWidth()/4 + Math.random() * getBattleFieldWidth()/2;
				//double randomY = getBattleFieldHeight()/4 + Math.random() * getBattleFieldHeight()/2;
				//random.setLocation(randomX, randomY);
			}

			if (Math.abs(normalizeBearing(bearing - getHeadingRadians())) < Math.PI / 2) {
				setTurnRightRadians(normalizeBearing(bearing - getHeadingRadians()));
				setAhead(Double.POSITIVE_INFINITY);
				
			} else {
				setTurnRightRadians(normalizeBearing(bearing + Math.PI - getHeadingRadians()));
				setAhead(Double.NEGATIVE_INFINITY);
			}
			
			out.println("moveBearing is : " + moveBearing);
			out.println("Heading is : " + getHeadingRadians());

			break;
		}

		coords.setLocation(getX(), getY());
	}

	private void aim() {

		Enemy target = robots.getTarget(this);

		switch (fireState) {

		case HEADON:
			setAdjustGunForRobotTurn(true);
			setTurnGunRightRadians(normalizeBearing(this.coords
					.getAbsoluteBearing(target.getPoint())
					- this.getGunHeadingRadians()));
			break;

		case LINEAR:

			Point robotPoint = getLinearTarget(target, this.coords);
			
			double gunBearing = coords.getAbsoluteBearing(robotPoint);
			setAdjustGunForRobotTurn(true);
			setTurnGunRightRadians(normalizeBearing(gunBearing - this.getGunHeadingRadians()));
			aimPoint.setLocation(robotPoint);
			break;

		}
	}
	
	public Point getLinearTarget(Enemy target, Point from) {

		double robotDistance = 0;
		double bulletDistance = 0;
		Point robotPoint = new Point(0, 0);

		long tick = getTime();
		do {
			robotPoint.setLocation(target.getFuturePoint(tick));
			robotDistance = from.distance(robotPoint);
			bulletDistance = (20 - 3 * getFirePower(robotDistance)) * (tick - getTime());
			tick++;

		} while (bulletDistance < robotDistance);
		
		return robotPoint;
	}

	private void shoot() {
		Enemy target = robots.getTarget(this);
		if (target != null) {
			if (getGunHeat() < 0.5) {
				scanState = ScanMode.LOCKON;
				if (getGunHeat() == 0
						&& getTime() - target.getLastScanTime() < 3
						&& Math.abs(getGunTurnRemainingRadians()) < Math.PI / 16) {

					double firePower = getFirePower(target.getDistance(coords));
					setFire(Math.min(firePower, target.getEnergy() / 4));
					resetScanMode();
				}
			}
		}
	}

	private double getFirePower(double distance) {
		return Math.min((400 / distance), 3);
	}

	private void resetScanMode() {
		if (getOthers() == 1) {
			scanState = ScanMode.LOCKON;
		} else if (robots.getNumber() < getOthers()) {
			scanState = ScanMode.EVERYTHING;
		} else
			scanState = ScanMode.OPTIMAL;

	}

	private double normalizeBearing(double angle) {
		while (angle > Math.PI)
			angle -= 2 * Math.PI;
		while (angle < -Math.PI)
			angle += 2 * Math.PI;
		return angle;
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		robots.addScan(e, this);

		resetScanMode();
		if (scanState == ScanMode.LOCKON)
			radar();
		
	}

	public void onRobotDeath(RobotDeathEvent e) {
		robots.remove(e.getName());
	}
	
	public void onBulletHit(BulletHitEvent e) {
		robots.updateEnergy(e.getName(), -Rules.getBulletDamage(e.getBullet().getPower()));
	}
	
	public void onHitByBullet(HitByBulletEvent e) {
		robots.updateEnergy(e.getName(), Rules.getBulletHitBonus(e.getBullet().getPower()));
	}

}
