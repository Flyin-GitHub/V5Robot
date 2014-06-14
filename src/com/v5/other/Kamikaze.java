package com.v5.other;

import robocode.AdvancedRobot;
import robocode.HitRobotEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

/**
 * @author Jacky Cai
 *
 */
public class Kamikaze extends AdvancedRobot {

	int movementDirection = 1;
	int gunDirection = 1;
	double fieldHeight;
	double fieldWidth;

	public void run() {
		fieldHeight = getBattleFieldHeight();
		fieldWidth = getBattleFieldWidth();
		setEventPriority("ScannedRobotEvent", 10); // 10
		setEventPriority("HitByBulletEvent", 70); // 40

		while (true) {
			turnRadarRight(360);
		}
	}

	@Override
	public void onHitRobot(HitRobotEvent e) {
		turnGunRight(Utils.normalRelativeAngleDegrees(getHeading()
				- getGunHeading() + e.getBearing()));
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
		turnGunRight(Utils.normalRelativeAngleDegrees(theta - getGunHeading()) + 2);
		fire(1);

		setTurnRight(Utils.normalRelativeAngleDegrees(theta - getHeading()));
		double moveAway = e.getDistance();
		if (moveAway > 300) {
		}
		setMove(theta - getHeading(), moveAway);
	}

	private void setMove(double Angel, double Distance) {
		Angel = normalRelativeAngle(Angel);
		if (Math.abs(Angel) > 90) {
			setTurnLeft((normalRelativeAngle(Angel + 180)) * (-1));
			setBack(Distance);
		} else {
			setTurnRight(Angel);
			setAhead(Distance);
		}
		if (Math.abs(getDistanceRemaining()) > 300)
			setMaxVelocity(getVelocityMax(8 - Math.abs(getTurnRemaining() / 5)));
		else
			setMaxVelocity(getVelocityMax(8 - Math.abs(getTurnRemaining() / 7)));

	}

	private double normalRelativeAngle(double a) {
		while (a > 180)
			a -= 360;
		while (a <= -180)
			a += 360;
		return a;
	}
	
	private double getVelocityMax(double v_max) {
		/*
		 * if (getEnergy() > 50) return(v_max); else {
		 */
		double v = 8;
		if (wallDistance(getX(), getY()) < 39)
			v = 2 + (wallDistance(getX(), getY()) - 21) / 18 * 6;
		return (Math.min(v_max, v));
		// }
	}

	private double wallDistance(double x, double y) {
		double distX = Math.min(fieldWidth - x, x);
		double distY = Math.min(fieldHeight - y, y);
		return Math.min(distY, distX);
	}
}
