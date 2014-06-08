package com.v5.www.robot;

import java.util.HashMap;
import java.util.Map;

import robocode.AdvancedRobot;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;
//import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * DodgeBot - a robot by (your name here)
 */
public class DodgeBot extends AdvancedRobot {

	Map<String, TankInfo> tankMonitor = new HashMap<String, DodgeBot.TankInfo>();

	int movementDirection = 1;
	double minDistance = 300;
	int gunDirection = 1;

	public void run() {
		
		System.out.println("heading" + getHeading());
		out.println("heading 11 " + getHeading());
		
		// while(true) {
//		setAhead(100000);
		setTurnGunRight(360);
		// }
	}

	private TankInfo getTank(ScannedRobotEvent e) {
		TankInfo tank = tankMonitor.get(e.getName());
		if (null == tank) {
			tank = new TankInfo(e.getName(), e.getEnergy());
			tankMonitor.put(e.getName(), tank);
		}
		return tank;
	}

	public void onScannedRobot(ScannedRobotEvent e) {

		System.out.println("Me : " + getX() + "@" + getY());
		System.out.println(getHeading());
		System.out.println("He : " + e.getBearing());
		
		
		
		TankInfo tank = getTank(e);
		double nowEnergy = e.getEnergy();

		// Stay at right angles to the opponent
		setTurnRight(e.getBearing() + 90 - 30 * movementDirection);

		// If the bot has small energy drop,
		// assume it fired
		double changeInEnergy = tank.getPreEnergy() - nowEnergy;
		if (changeInEnergy > 0 && changeInEnergy <= 3) {
			movementDirection = -movementDirection;
			setAhead((e.getDistance() / 4 + 25) * movementDirection);
		}
		// When a bot is spotted,
		// sweep the gun and radar
		gunDirection = -gunDirection;
		setTurnGunRight(99999 * gunDirection);
//		setTurnGunRight(99999);

		// Fire directly at target
		fire(1);
		
		if (minDistance > e.getDistance()) {
			onHitWall(new HitWallEvent(e.getBearing()));
		}

		// Track the energy level
		tank.setPreEnergy(e.getEnergy());
	}

	public void onHitWall(HitWallEvent e) {
		// If he's in front of us, set back up a bit.
		if (e.getBearing() > -90 && e.getBearing() < 90) {
			back(100);
		} // else he's in back of us, so set ahead a bit.
		else {
			ahead(100);
		}
	}

	class TankInfo {
		private double preEnergy = 100;
		private String name = "Null";

		public TankInfo(String name, double preEnergy) {
			this.preEnergy = preEnergy;
			this.name = name;
		}

		public double getPreEnergy() {
			return preEnergy;
		}

		public void setPreEnergy(double previousEnergy) {
			this.preEnergy = previousEnergy;
		}
	}
}
