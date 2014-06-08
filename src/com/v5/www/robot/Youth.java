package com.v5.www.robot;

import static robocode.util.Utils.normalRelativeAngleDegrees;
import robocode.*;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/JuniorRobot.html

/**
 * Youth - a robot by (your name here)
 */
public class Youth extends AdvancedRobot {
	/**
	 * run: Youth's default behavior
	 */
	public void run() {
		// Initialization of the robot should be put here

		// Some color codes: blue, yellow, black, white, red, pink, brown, grey,
		// orange...
		// Sets these colors (robot parts): body, gun, radar, bullet, scan_arc

		// Robot main loop
		while (true) {
			// Replace the next 4 lines with any behavior you would like
//			ahead(100);
			turnGunRight(360);
//			back(100);
//			turnGunRight(360);
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		double heading = e.getBearing() +getHeading(); 
		double distance = e.getDistance(); // 求得距离
		double ager_bearing = Math.toRadians(heading % 360); // 角度转为弧度
		double genyX = getX() + Math.sin(ager_bearing) * distance;
		double genyY = getY() + Math.cos(ager_bearing) * distance;

		double theta = Math.toDegrees(Math.atan2((genyX - getX()), (genyY - getY())));

		// Turn gun to target
		turnGunRight(normalRelativeAngleDegrees(theta - getGunHeading()));
//		
//		turnGunRight(getHeading());
		double shootDirection = Math.sin((genyY - getY())
				/ (genyX - getX()));
		fire(1);
//		turnGunLeft(shootDirection);
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet() {
		// Replace the next line with any behavior you would like
		back(10);
	}

	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall() {
		// Replace the next line with any behavior you would like
		back(20);
	}
}
