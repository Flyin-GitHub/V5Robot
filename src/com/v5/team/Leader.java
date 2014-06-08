package com.v5.team;

import java.io.IOException;

import robocode.DeathEvent;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;
import robocode.util.Utils;

public class Leader extends TeamRobot {

	private static double nearDistance = 50.0d * 50d;
	private static double aheadDistance = 100.0d;
	@Override
	public void run() {
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);
		while(true){
			ahead(aheadDistance);
			double heading = getHeadingRadians(); //ȡ��bot����Ļ�����
			double x = getX() + aheadDistance*Math.sin(heading); //�ƶ�move��Ҫ�ﵽ��x����
			double y = getY() + aheadDistance*Math.cos(heading); //�ƶ�move��Ҫ�ﵽ��y����
			double dWidth = getBattleFieldWidth(); //ս���Ŀ��
			double dHeight = getBattleFieldHeight(); //ս���ĳ���
			//��(x,y)����ָ���ķ�Χ�������ƶ�move
			if(x < 30 || x > dWidth-30 || y < 30 || y > dHeight-30){
				back(aheadDistance);
			}
			turnRadarRight(360);
		}

	}

	@Override
	public void onDeath(DeathEvent e) {
		out.println("[Header:] deied");
		try {
			broadcastMessage(new Message(MessageType.HEADER_DIED, ""));
		} catch (IOException ex) {
			out.println("Unable to send order: ");
			ex.printStackTrace(out);
		}
	}

	@Override
	public void onScannedRobot(ScannedRobotEvent e) {
		

		// Don't fire on teammates
		if (!isTeammate(e.getName())) {	
				
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
			fire(3);
			setTurnRight(Utils.normalRelativeAngleDegrees(theta - getHeading()));
			if((dx*dx + dy*dy) > nearDistance){					
				setTurnRight(180);
			}else{
				if(Utils.getRandom().nextBoolean()){
					setTurnRight(90);
				}else{
					setTurnLeft(90);		
				}
		
			}
			execute();
			
			try {
				// Send enemy position to teammates
				broadcastMessage(new Message(MessageType.ENEMY_INFO, new Point(
						enemyX, enemyY)));
			} catch (IOException ex) {
				out.println("Unable to send order: ");
				ex.printStackTrace(out);
			}

		}

	}
	
	

}
