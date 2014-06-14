package com.v5.team;

import java.util.Random;

import robocode.Droid;
import robocode.MessageEvent;
import robocode.TeamRobot;
import robocode.util.Utils;

import com.v5.Message;
import com.v5.MessageType;
import com.v5.Point;

/**
 * @author V5-team
 *
 */
public class SoldierWalls extends TeamRobot implements Droid {

	boolean peek; // Don't turn if there's a robot there
	double moveAmount; // How much to move
	private static double nearDistance = 100.0d * 100.0d;
	private static double aheadDistance = 150.0d;
	private boolean headerDied = false;

	public void run() {
		// Initialize moveAmount to the maximum possible for this battlefield.
		moveAmount = Math.max(getBattleFieldWidth(), getBattleFieldHeight());
		// Initialize peek to false
		peek = false;

		// turnLeft to face a wall.
		// getHeading() % 90 means the remainder of
		// getHeading() divided by 90.
		turnLeft(getHeading() % 90);
		ahead(moveAmount);
		// Turn the gun to turn right 90 degrees.
		peek = true;
		turnGunRight(90);
		turnRight(90);

		while (true) {
			// Look before we turn when ahead() completes.
			peek = true;
			// Move up the wall
			ahead(moveAmount);
			// Don't look now
			peek = false;
			// Turn to the next wall
			turnRight(90);
		}
	}

	public void onMessageReceived(MessageEvent e) {
		if (e.getMessage() instanceof Message) {
			Message message = (Message) e.getMessage();
			MessageType type = message.getType();
			switch (type) {
			case ENEMY_INFO:
				Point p = (Point) message.getContent();
				// Calculate x and y to target
				double dx = p.getX() - this.getX();
				double dy = p.getY() - this.getY();
				double theta = Math.toDegrees(Math.atan2(dx, dy));
				turnGunRight(Utils.normalRelativeAngleDegrees(theta
						- getGunHeading()) + 2);
				fire(3);
//				setTurnRight(Utils.normalRelativeAngleDegrees(theta
//						- getHeading()));
//				if ((dx * dx + dy * dy) > nearDistance) {
//					setTurnRight(180);
//				} else {
//					if (Utils.getRandom().nextBoolean()) {
//						setTurnRight(90);
//					} else {
//						setTurnLeft(90);
//					}
//
//				} // fire
				execute();
				break;
			case HEADER_DIED:
				headerDied = true;
				break;
			default:
				out.println("[No Surrpoted Message Type:] " + type.toString());
			}
		}
	}

	private void move() {
		Random random = Utils.getRandom();
		this.turnRight(random.nextDouble() * 360);
		this.ahead(aheadDistance);
	}

}
