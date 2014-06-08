package com.v5.team;

import robocode.AdvancedRobot;
import robocode.HitRobotEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class Kamikaze extends AdvancedRobot {
	
	int movementDirection = 1;
	int gunDirection = 1;
	public void run(){
		setEventPriority("ScannedRobotEvent",	10); //10
		setEventPriority("HitByBulletEvent",	70); //40
		
		while (true) {
			turnRadarRight(360);
		}
	}

	@Override
	public void onHitRobot(HitRobotEvent e) {
		turnGunRight(Utils.normalRelativeAngleDegrees(getHeading() - getGunHeading() + e.getBearing()));
		fire(3);
	}
	
	@Override
	public void onScannedRobot(ScannedRobotEvent e) {
		
		double enemyBearing = this.getHeading() + e.getBearing();
		// Calculate enemy's position
		double enemyX = getX() + e.getDistance()
				* Math.sin(Math.toRadians(enemyBearing));
		double enemyY = getY() + e.getDistance()
				* Math.cos(Math.toRadians(enemyBearing));
		double dx = enemyX - this.getX();
		double dy = enemyY - this.getY();
		double theta = Math.toDegrees(Math.atan2(dx, dy));
		turnGunRight(Utils.normalRelativeAngleDegrees(theta - getGunHeading())+2);
		fire(1);

		setTurnRight(Utils.normalRelativeAngleDegrees(theta - getHeading()));
		double moveAway = e.getDistance();
		if (moveAway > 300) {
			moveAway -= 50;
		}
		ahead(moveAway);
	}
}
