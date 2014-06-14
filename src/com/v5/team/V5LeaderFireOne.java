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
 * add shoot leader logic
 */
public class V5LeaderFireOne extends AbstractRobot {

	private Map<String, RobotInfo> robotInfoMap = new HashMap<String, RobotInfo>();
	private List<RobotInfo> robotInfoList = new LinkedList<RobotInfo>();

	private RobotInfo getRobotInfo(ScannedRobotEvent e) {
		RobotInfo robot = robotInfoMap.get(e.getName());
		if (null == robot) {
			robot = new RobotInfo();
			robot.setName(e.getName());
			if (!isTeammate(e.getName())) {
				robot.setEnemy(true);
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
		RobotInfo robot = getRobotInfo(e);
		if (!isShootRobot(robot)) {
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
			action.setFirePower(robot.isEnemyLeader() ? 3 : 1);
			broadcastMessage(new Message(MessageType.ENEMY_INFO, action));
		} catch (IOException ex) {
			out.println("Unable to send order: ");
			ex.printStackTrace(out);
		}
		super.onScannedRobot(e);
	}
}
