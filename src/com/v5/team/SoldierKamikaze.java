package com.v5.team;

import robocode.MessageEvent;
import robocode.Rules;
import robocode.util.Utils;

import com.v5.Message;
import com.v5.MessageType;
import com.v5.RobotAction;

/**
 * @author Jacky Cai
 *
 */
public class SoldierKamikaze extends AbstractSoldier {
	
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
					double moveAway = Math.sqrt(Math.pow(dx, 2)+Math.pow(dy, 2));
					setMove(theta - getHeading(), moveAway);
					break;
				case HEADER_DIED:
					out.println("HEADER DEID");
					headerDied = true;
					break;
				default:
					out.println("[No Surrpoted Message Type:] " + type.toString());
			}			
		}			
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
		setMaxVelocity(Rules.MAX_VELOCITY);
	}

	private double normalRelativeAngle(double a) {
		while (a > 180)
			a -= 360;
		while (a <= -180)
			a += 360;
		return a;
	}
}
