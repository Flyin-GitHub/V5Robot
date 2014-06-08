package com.v5.team;

import robocode.Droid;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.MessageEvent;
import robocode.RobotDeathEvent;
import robocode.TeamRobot;
import robocode.util.Utils;

import com.v5.www.robot.RobotAction;

public class SoldierKamikaze extends TeamRobot implements Droid {
	
	private static double nearDistance = 100.0d * 100.0d;
	private static double aheadDistance = 100.0d;
	private boolean headerDied = false;
	public void run(){
//		setAdjustGunForRobotTurn(true);
//		setAdjustRadarForGunTurn(true);
//		setAdjustRadarForRobotTurn(true);
		setEventPriority();
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
					fire(3);

					setTurnRight(Utils.normalRelativeAngleDegrees(theta - getHeading()));
					double moveAway = Math.sqrt(Math.pow(dx, 2)+Math.pow(dy, 2));
					if (moveAway > 300) {
						moveAway -= 50;
					}
					ahead(moveAway);
					execute();
//					move();
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
	
	@Override
	public void onHitWall(HitWallEvent e) {
		turnRight(Utils.normalRelativeAngleDegrees(e.getBearing() + 180));
		move();
	}

	@Override
	public void onHitByBullet(HitByBulletEvent e) {
//		if(!isTeammate(e.getName())){		
//			turnGunRight(Utils.normalRelativeAngleDegrees(getHeading() - getGunHeading() + e.getBearing()) + 2);
//			fire(3);
//		}	
//		turnRight(e.getBearing() + 90);	
//		move();
	}

	@Override
	public void onHitRobot(HitRobotEvent e) {
		if(!isTeammate(e.getName())){		
			turnGunRight(Utils.normalRelativeAngleDegrees(getHeading() - getGunHeading() + e.getBearing()));
			fire(3);
		}else{
			turnLeft(Utils.normalRelativeAngleDegrees(e.getBearing() + 180));
			move();
		}
	}

	@Override
	public void onRobotDeath(RobotDeathEvent e) {
		super.onRobotDeath(e);
		
	}

	private void move(){
		double heading = getHeadingRadians(); //取得bot方向的弧度数
		double x = getX() + aheadDistance*Math.sin(heading); //移动move后将要达到的x坐标
		double y = getY() + aheadDistance*Math.cos(heading); //移动move后将要达到的y坐标
		double dWidth = getBattleFieldWidth(); //战场的宽度
		double dHeight = getBattleFieldHeight(); //战场的长度
		//当(x,y)超过指定的范围，则反向移动move
		if(x < 50 || x > dWidth-50 || y < 50 || y > dHeight-50){
			back(aheadDistance);
		}else{
			ahead(aheadDistance);
		}
	}
	
	private void setEventPriority(){
		setEventPriority("ScannedRobotEvent",	10); //10
		setEventPriority("HitWallEvent",		20); //30
		setEventPriority("BulletHitEvent",		30); //50
		setEventPriority("BulletHitBulletEvent",40); //50
		setEventPriority("BulletMissedEvent",	50); //60
		setEventPriority("RobotDeathEvent",		60); //70
		setEventPriority("HitByBulletEvent",	70); //40
		setEventPriority("HitRobotEvent",		80); //20
	}
	
	
	
}
