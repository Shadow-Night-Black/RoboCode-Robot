package shadow;

import java.util.ArrayList;
import robocode.ScannedRobotEvent;
import robocode.AdvancedRobot;

public class Enemy {
	private ArrayList<RobotData> data;
	private RobotData lastData;
	private String name;

	public Enemy(ScannedRobotEvent e, AdvancedRobot r) {
		this.data = new ArrayList<RobotData>();
		this.name = e.getName();
		this.addScan(e, r);
	}
	
	public Enemy(AdvancedRobot r) {
		this.data = new ArrayList<RobotData>();
		this.name = r.getName();
		this.addSelfScan(r);
	}

	public void addScan(ScannedRobotEvent e, AdvancedRobot r) {
		lastData = (new RobotData(e, r));
		data.add(lastData);
	}
	
	public void addSelfScan(AdvancedRobot r) {
		lastData = (new RobotData(r));
		data.add(lastData);
	}

	public String getName() {
		return name;
	}
	
	public Point getPoint() {
		return lastData.getPoint();
	}
	
	public double getDistance(Point point) {
		return lastData.getDistance(point);
	}

	public long getLastScanTime() {
		return lastData.getTick();
	}
	
	public double getEnergy() {
		return lastData.getEnergy();
	}

	public double getEnergyChange() {
		if (data.size() > 1) {
			RobotData previous = data.get(data.size() - 2);
			double change = previous.getEnergy() - lastData.getEnergy();
			return change;
		} else return 0;
	}
	
	public void updateEnergy(double amount) {
		lastData.updateEnergy(amount);
	}

	
	public Point getFuturePoint(long tick) {
		return lastData.getFuturePoint(tick);
	}
	
	public RobotData getRobotData(){
		return lastData;
	}
}
