package com.v5.team;

import java.io.IOException;

import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

import com.v5.AbstractRobot;
import com.v5.Message;
import com.v5.MessageType;
import com.v5.Point;
import com.v5.RobotAction;


/**
 * @author V5-team
 *
 */
public class LeaderRandom extends AbstractRobot {

	@Override
	public void onRobotDeath(RobotDeathEvent e) {
		super.onRobotDeath(e);
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		if (isTeammate(e.getName())) {
			return;
		}

		double enemyBearing = this.getHeading() + e.getBearing();
		double enemyX = getX() + e.getDistance()
				* Math.sin(Math.toRadians(enemyBearing));
		double enemyY = getY() + e.getDistance()
				* Math.cos(Math.toRadians(enemyBearing));
		try {
			RobotAction action = new RobotAction();
			action.setShootPoint(new Point(enemyX, enemyY));
			action.setFirePower(3);
			broadcastMessage(new Message(MessageType.ENEMY_INFO, action));
		} catch (IOException ex) {
			out.println("Unable to send order: ");
			ex.printStackTrace(out);
		}
		super.onScannedRobot(e);
	}
}
