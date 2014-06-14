package com.v5.team.formation;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import robocode.HitByBulletEvent;
import robocode.ScannedRobotEvent;

import com.v5.Point;
import com.v5.RobotAction;
import com.v5.RobotInfo;

public class TeamLeader extends TeamMember {

	private boolean isFormation = false;
	private Map<String, RobotInfo> robotInfoMap = new HashMap<String, RobotInfo>();

	@Override
	public MemberTpye getMemberType() {
		return MemberTpye.LEADER;
	}

	/**
	 * run: Leader's default behavior
	 */
	public void run() {
		initMemberInfo();

		System.out.println("Ready : " + getTeammates());
		setBodyColor(Color.red);
		setGunColor(Color.red);
		setRadarColor(Color.red);
		setScanColor(Color.yellow);
		setBulletColor(Color.yellow);

		while (true) {
			turnGunRight(360);
		}
	}

	private boolean isLeader(double initEnergy) {
		return initEnergy <= 200;
	}

	private RobotInfo getRobotInfo(ScannedRobotEvent e) {
		RobotInfo robot = robotInfoMap.get(e.getName());
		if (null == robot) {
			robot = new RobotInfo();
			if (!isTeammate(e.getName())) {
				robot.setEnemy(true);
				robot.setEnemyLeader(isLeader(e.getEnergy()));
			}

			robotInfoMap.put(e.getName(), robot);
		}
		return robot;
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		RobotInfo robot = getRobotInfo(e);
		if (!robot.isEnemyLeader()) {
			return;
		}

		double heading = e.getBearing() + getHeading();
		double distance = e.getDistance(); // 求得距离
		double ager_bearing = Math.toRadians(heading % 360); // 角度转为弧度
		double genyX = getX() + Math.sin(ager_bearing) * distance;
		double genyY = getY() + Math.cos(ager_bearing) * distance;

		if (!isFormation) {
			try {
				RobotAction action = new RobotAction();
				action.setFormationType(getOppositeQuadrant(genyX, genyY));
				broadcastMessage(action);
				doFormation(action);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			isFormation = true;
		}

		RobotAction action = new RobotAction();
		action.setShootPoint(new Point(genyX, genyY));
		// action.setMovePoint(new Point(genyX, genyY));

		try {
			broadcastMessage(action);
		} catch (IOException ignored) {
			ignored.printStackTrace();
		}
	}

	private void doFormation() throws IOException {
		QuadrantType quadrant = QuadrantType.QUADRANT_1;
		broadcastMessage(quadrant);
		goTo(getFormationPoint(quadrant));

		quadrant = QuadrantType.QUADRANT_2;
		broadcastMessage(quadrant);
		goTo(getFormationPoint(quadrant));

		quadrant = QuadrantType.QUADRANT_3;
		broadcastMessage(quadrant);
		goTo(getFormationPoint(quadrant));

		quadrant = QuadrantType.QUADRANT_4;
		broadcastMessage(quadrant);
		goTo(getFormationPoint(quadrant));
	}

	/**
	 * onHitByBullet: Turn perpendicular to bullet path
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		turnLeft(90 - e.getBearing());
	}
}
