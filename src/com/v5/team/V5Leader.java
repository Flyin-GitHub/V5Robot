package com.v5.team;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

import com.v5.AbstractRobot;
import com.v5.Message;
import com.v5.MessageType;
import com.v5.Point;
import com.v5.RobotAction;
import com.v5.RobotComparator;
import com.v5.RobotInfo;


/**
 * @author V5-team
 *
 */
public class V5Leader extends AbstractRobot {

	private Map<String, RobotInfo> robotInfoMap = new HashMap<String, RobotInfo>();
	private List<RobotInfo> robotInfoList = new LinkedList<RobotInfo>();

	private boolean isLeader(double initEnergy) {
		return initEnergy >= 150;
	}

	private RobotInfo getRobotInfo(ScannedRobotEvent e) {
		RobotInfo robot = robotInfoMap.get(e.getName());
		if (null == robot) {
			robot = new RobotInfo();
			robot.setName(e.getName());
			if (!isTeammate(e.getName())) {
				robot.setEnemy(true);
				robot.setEnemyLeader(isLeader(e.getEnergy()));
			}

			robotInfoList.add(robot);
			robotInfoMap.put(e.getName(), robot);
		}
		return robot;
	}

	private boolean isShootRobot(RobotInfo robot) {
		Collections.sort(robotInfoList, new RobotComparator());
		return robotInfoList.get(0).found(robot.getName());
	}
	
	@Override
	public void onRobotDeath(RobotDeathEvent e) {
		RobotInfo robot = robotInfoMap.get(e.getName());
		robotInfoMap.remove(robot);
		robotInfoList.remove(robot);
		super.onRobotDeath(e);
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		
		double enemyBearing = this.getHeading() + e.getBearing();
		double enemyX = getX() + e.getDistance()
				* Math.sin(Math.toRadians(enemyBearing));
		double enemyY = getY() + e.getDistance()
				* Math.cos(Math.toRadians(enemyBearing));
		try {
			RobotAction action = new RobotAction();
			action.setShootPoint(new Point(enemyX, enemyY));
			action.setEvent(e);
			if(isTeammate(e.getName())){
				broadcastMessage(new Message(MessageType.TEAMMATE_INFO, action));
			}else{
				broadcastMessage(new Message(MessageType.ENEMY_INFO, action));
				RobotInfo robot = getRobotInfo(e);
				action.setFirePower(robot.isEnemyLeader() ? 3 : 1);
				if (isShootRobot(robot)) {
					super.onScannedRobot(e);
				}
			}
		} catch (IOException ex) {
			out.println("Unable to send order: ");
			ex.printStackTrace(out);
		}	
		
	}

}
