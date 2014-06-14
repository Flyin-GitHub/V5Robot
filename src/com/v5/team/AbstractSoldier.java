package com.v5.team;

import robocode.Droid;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.MessageEvent;
import robocode.RobotDeathEvent;
import robocode.TeamRobot;
import robocode.util.Utils;

import com.v5.Message;
import com.v5.MessageType;
import com.v5.RobotAction;

/**
 * @author V5-team
 *
 */
public class AbstractSoldier extends TeamRobot implements Droid {

	protected double nearDistance = 100.0d * 100.0d;
	protected double aheadDistance = 200.0d;
	protected double fieldHeigh;
	protected double fieldWidth;
	protected boolean headerDied = false;

	protected void init() {
		this.fieldWidth = getBattleFieldWidth(); 
		this.fieldHeigh = getBattleFieldHeight(); 
	}
	
	public void run() {
		init();
		
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);
		setEventPriority();
	}

	public void onMessageReceived(MessageEvent e) {
		if (e.getMessage() instanceof Message) {
			Message message = (Message) e.getMessage();
			MessageType type = message.getType();
			switch (type) {
			case ENEMY_INFO:
				RobotAction action = (RobotAction) message.getContent();
				double dx = action.getShootPoint().getX() - this.getX();
				double dy = action.getShootPoint().getY() - this.getY();
				double theta = Math.toDegrees(Math.atan2(dx, dy));
				
				turnGunRight(Utils.normalRelativeAngleDegrees(theta - getGunHeading()) + 2);
				fire(action.getFirePower());
				
				if ((dx * dx + dy * dy) < nearDistance) {
					setTurnRight(Utils.normalRelativeAngleDegrees(theta - getHeading()) + 180);
				} else {
					if (Utils.getRandom().nextBoolean()) {
						setTurnRight(Utils.normalRelativeAngleDegrees(theta - getHeading()) + 90);
					} else {
						setTurnRight(Utils.normalRelativeAngleDegrees(theta - getHeading()) - 90);
					}
				}
				execute();
				move();
				break;
			case HEADER_DIED:
				headerDied = true;
				break;
			default:
				out.println("[No Surrpoted Message Type:] " + type.toString());
			}
		}
	}

	@Override
	public void onHitWall(HitWallEvent e) {
		turnRight(Utils.normalRelativeAngleDegrees(e.getBearing() + 180));
		move();
	}

	@Override
	public void onHitByBullet(HitByBulletEvent e) {
		if (!isTeammate(e.getName())) {
			turnGunRight(Utils.normalRelativeAngleDegrees(getHeading()
					- getGunHeading() + e.getBearing()) + 2);
			fire(3);
		}
		turnRight(e.getBearing() + 90);
		move();
	}

	@Override
	public void onHitRobot(HitRobotEvent e) {
		if (!isTeammate(e.getName())) {
			turnGunRight(Utils.normalRelativeAngleDegrees(getHeading()
					- getGunHeading() + e.getBearing()));
			fire(3);
		} else {
			turnLeft(Utils.normalRelativeAngleDegrees(e.getBearing() + 180));
			move();
		}
	}

	@Override
	public void onRobotDeath(RobotDeathEvent e) {
		super.onRobotDeath(e);
	}

	protected void move() {
		double heading = getHeadingRadians(); 
		double x = getX() + aheadDistance * Math.sin(heading); 
		double y = getY() + aheadDistance * Math.cos(heading); 

		if (x < 50 || x > fieldWidth - 50 || y < 50 || y > fieldHeigh - 50) {
			back(aheadDistance);
		} else {
			ahead(aheadDistance);
		}
	}

	protected void setEventPriority() {
		setEventPriority("ScannedRobotEvent", 10); // 10
		setEventPriority("HitWallEvent", 20); // 30
		setEventPriority("BulletHitEvent", 30); // 50
		setEventPriority("BulletHitBulletEvent", 40); // 50
		setEventPriority("BulletMissedEvent", 50); // 60
		setEventPriority("RobotDeathEvent", 60); // 70
		setEventPriority("HitByBulletEvent", 70); // 40
		setEventPriority("HitRobotEvent", 80); // 20
	}
}
