package com.v5.team.formation;

import com.v5.RobotAction;
import com.v5.RobotInfo;

import robocode.Droid;
import robocode.MessageEvent;
import robocode.ScannedRobotEvent;

public class TeamMemberLeft extends TeamMember implements Droid {

	private boolean readyFormation = false;
	private RobotInfo myInfo;
	private MemberTpye myType = MemberTpye.LEFT;

	@Override
	public MemberTpye getMemberType() {
		return MemberTpye.LEFT;
	}
	
	public TeamMemberLeft() {

	}

	public void run() {

		initMemberInfo();

			turnGunRight(360);
//			if (myInfo == null) {
//				try {
//					Thread.sleep(5000);
//					continue;
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
			readyFormation = false;
//			while (!readyFormation) {
//				System.out.println("Go-----------------");
//				System.out.println("X:" + getX() + "," + "Y:" + getY());
//				goTo();
//
//				
//				if (myInfo.readyFormation(getX(), getY())) {
//					readyFormation = true;
//				}
//
//			}
	}
	

	public void onScannedRobot(ScannedRobotEvent e) {

		// Calculate enemy bearing
		double enemyBearing = this.getHeading() + e.getBearing();
		// Calculate enemy's position
		double enemyX = getX() + e.getDistance() * Math.sin(Math.toRadians(enemyBearing));
		double enemyY = getY() + e.getDistance() * Math.cos(Math.toRadians(enemyBearing));

//		if (myInfo == null) {
//			goTo(formationPointMap.get(getOppositeQuadrant(enemyX, enemyY)));
//		}
		
//		goTo(getFormationPoint(QuadrantType.QUADRANT_1));
		
		// myInfo
	}
	
	/**
	 * onMessageReceived: What to do when our leader sends a message
	 */
	public void onMessageReceived(MessageEvent e) {
		
		RobotAction action = (RobotAction) e.getMessage();
		doAction(action);
		
//		QuadrantType type = (QuadrantType) e.getMessage();
//		goTo(getFormationPoint(type));
	}

}
