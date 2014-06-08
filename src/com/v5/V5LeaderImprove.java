package com.v5;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import robocode.ScannedRobotEvent;

import com.v5.team.RobotComparator;
import com.v5.team.RobotInfo;

/**
 * add shoot leader logic
 */
public class V5LeaderImprove extends AbstractRobot {

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
		if (robotInfoList.get(0).isDisable()) {
			robotInfoMap.remove(robotInfoList.get(0).getName());
			robotInfoList.remove(0);
		}

		Collections.sort(robotInfoList, new RobotComparator());
		return robotInfoList.get(0).found(robot.getName());
	}

	public void onScannedRobot(ScannedRobotEvent e) {

		RobotInfo robot = getRobotInfo(e);
		if (!isShootRobot(robot)) {
			return;
		}

		double enemyBearing = this.getHeading() + e.getBearing();
		// Calculate enemy's position
		double enemyX = getX() + e.getDistance()
				* Math.sin(Math.toRadians(enemyBearing));
		double enemyY = getY() + e.getDistance()
				* Math.cos(Math.toRadians(enemyBearing));
		try {
			// Send enemy position to teammates
			broadcastMessage(new Message(MessageType.ENEMY_INFO, new Point(
					enemyX, enemyY)));
		} catch (IOException ex) {
			out.println("Unable to send order: ");
			ex.printStackTrace(out);
		}
		super.onScannedRobot(e);
	}
}
