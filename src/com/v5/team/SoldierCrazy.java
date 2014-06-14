package com.v5.team;


import java.awt.Color;

import robocode.MessageEvent;
import robocode.TurnCompleteCondition;
import robocode.util.Utils;

import com.v5.Message;
import com.v5.MessageType;
import com.v5.RobotAction;

/**
 * @author Jacky Cai
 *
 */
public class SoldierCrazy extends AbstractSoldier {
	boolean movingForward;

	public void run() {
		// Set colors
		setBodyColor(new Color(0, 200, 0));
		setGunColor(new Color(0, 150, 50));
		setRadarColor(new Color(0, 100, 100));
		setBulletColor(new Color(255, 255, 100));
		setScanColor(new Color(255, 200, 200));

		// Loop forever
		while (true) {
			setTurnGunRight(360);
			setAhead(40000);
			movingForward = true;
			setTurnRight(90);
			waitFor(new TurnCompleteCondition(this));
			setTurnLeft(180);
			waitFor(new TurnCompleteCondition(this));
			setTurnRight(180);
			waitFor(new TurnCompleteCondition(this));
			setTurnGunRight(360);
		}
	}

	public void onMessageReceived(MessageEvent e) {
		if (e.getMessage() instanceof Message) {
			Message message = (Message)e.getMessage();
			MessageType type = message.getType();
			switch(type){
				case ENEMY_INFO:
					RobotAction action = (RobotAction) message.getContent();
					// Calculate x and y to target
					double dx = action.getShootPoint().getX() - this.getX();
					double dy = action.getShootPoint().getY() - this.getY();
					double theta = Math.toDegrees(Math.atan2(dx, dy));
					turnGunRight(Utils.normalRelativeAngleDegrees(theta - getGunHeading())+2);
					fire(action.getFirePower());
					setTurnRight(Utils.normalRelativeAngleDegrees(theta - getHeading()));
					
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
}
