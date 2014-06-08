package com.v5.www.robot.team;

import static robocode.util.Utils.normalRelativeAngleDegrees;

import java.awt.Desktop.Action;
import java.util.HashMap;
import java.util.Map;

import robocode.TeamRobot;

import com.v5.www.robot.Point;
import com.v5.www.robot.RobotAction;

public abstract class TeamMember extends TeamRobot {

	public enum MemberTpye {
		LEADER, LEFT, MIDDLE, RIGHT
	};

	public enum QuadrantType {
		QUADRANT_1, QUADRANT_2, QUADRANT_3, QUADRANT_4
	};

	public double initAwayWall = 200;
	public double memberSpace = 70;
	public double leaderSpace = 110;
	public final int cantReachCnt = 3;
	protected static Map<MemberTpye, RobotInfo> memberMap = new HashMap<MemberTpye, RobotInfo>();

	protected double maxX;
	protected double maxY;

	protected static Map<MemberTpye, Map<QuadrantType, Point>> formationPointMap = new HashMap<MemberTpye, Map<QuadrantType, Point>>();
	private boolean isInit = false;

	private void init() {
		double farAwayWall = initAwayWall + getHeight();
		maxX = getBattleFieldWidth();
		maxY = getBattleFieldHeight();

		Point pointMid1 = new Point(farAwayWall, maxY - farAwayWall);
		Point pointMid2 = new Point(maxX - farAwayWall, maxY - farAwayWall);
		Point pointMid3 = new Point(maxX - farAwayWall, farAwayWall);
		Point pointMid4 = new Point(farAwayWall, farAwayWall);

		Map<QuadrantType, Point> formationMid = new HashMap<QuadrantType, Point>();
		formationPointMap.put(MemberTpye.MIDDLE, formationMid);
		formationMid.put(QuadrantType.QUADRANT_1, pointMid1);
		formationMid.put(QuadrantType.QUADRANT_2, pointMid2);
		formationMid.put(QuadrantType.QUADRANT_3, pointMid3);
		formationMid.put(QuadrantType.QUADRANT_4, pointMid4);

		Map<QuadrantType, Point> formationLeft = new HashMap<QuadrantType, Point>();
		formationPointMap.put(MemberTpye.LEFT, formationLeft);
		formationLeft.put(QuadrantType.QUADRANT_1, new Point(pointMid1.getX()
				+ memberSpace, pointMid1.getY() + memberSpace));
		formationLeft.put(QuadrantType.QUADRANT_2, new Point(pointMid2.getX()
				+ memberSpace, pointMid2.getY() - memberSpace));
		formationLeft.put(QuadrantType.QUADRANT_3, new Point(pointMid3.getX()
				- memberSpace, pointMid3.getY() - memberSpace));
		formationLeft.put(QuadrantType.QUADRANT_4, new Point(pointMid4.getX()
				- memberSpace, pointMid4.getY() + memberSpace));

		Map<QuadrantType, Point> formationRight = new HashMap<QuadrantType, Point>();
		formationPointMap.put(MemberTpye.RIGHT, formationRight);
		formationRight.put(QuadrantType.QUADRANT_1, new Point(pointMid1.getX()
				- memberSpace, pointMid1.getY() - memberSpace));
		formationRight.put(QuadrantType.QUADRANT_2, new Point(pointMid2.getX()
				- memberSpace, pointMid2.getY() + memberSpace));
		formationRight.put(QuadrantType.QUADRANT_3, new Point(pointMid3.getX()
				+ memberSpace, pointMid3.getY() + memberSpace));
		formationRight.put(QuadrantType.QUADRANT_4, new Point(pointMid4.getX()
				+ memberSpace, pointMid4.getY() - memberSpace));

		Map<QuadrantType, Point> formationLeader = new HashMap<QuadrantType, Point>();
		formationPointMap.put(MemberTpye.LEADER, formationLeader);
		formationLeader.put(QuadrantType.QUADRANT_1, new Point(pointMid1.getX()
				- leaderSpace, pointMid1.getY() + leaderSpace));
		formationLeader.put(QuadrantType.QUADRANT_2, new Point(pointMid2.getX()
				+ leaderSpace, pointMid2.getY() + leaderSpace));
		formationLeader.put(QuadrantType.QUADRANT_3, new Point(pointMid3.getX()
				+ leaderSpace, pointMid3.getY() - leaderSpace));
		formationLeader.put(QuadrantType.QUADRANT_4, new Point(pointMid4.getX()
				- leaderSpace, pointMid4.getY() - leaderSpace));
	}

	protected void doAction(RobotAction action) {
		if (action.isFormation()) {
			doFormation(action);
		} else {
			doNormalAction(action);
		}
	}

	protected void doNormalAction(RobotAction action) {
		// shoot
		Point shootPoint = action.getShootPoint();
		if (null != shootPoint) {
			double theta = Math.toDegrees(Math.atan2(
					shootPoint.getX() - getX(), (shootPoint.getY() - getY())));
			double shootDirection = normalRelativeAngleDegrees(theta - getGunHeading());
			turnGunRight(shootDirection);
			fire(action.getFirePower());
		}

		// move
		Point movePoint = action.getMovePoint();
		if (null != movePoint) {
			goTo(movePoint);
		}
	}

	protected void doFormation(RobotAction action) {
		goTo(getFormationPoint(action.getFormationType()));
	}

	protected void initMemberInfo() {
		if (!isInit) {
			init();
			isInit = true;
		}
	}

	protected Point getFormationPoint(QuadrantType quadrant) {
		return formationPointMap.get(getMemberType()).get(quadrant);
	}

	protected void goTo(Point point) {
		System.out.println("Go  -------------------");
		System.out.println("From " + new Point(getX(), getY()));
		System.out.println("To " + point);
		int i = 0;
		while (!point.isReach(getX(), getY())) {
			if (i++ < cantReachCnt) {
				goToDirectly(point);
			} else {
				goToDirectly(new Point(maxX - point.getX(), maxY - point.getY()));
				i = 0;
			}
		}
	}

	protected void goToDirectly(Point point) {
		System.out.println("Go  -------------------");
		System.out.println("From " + new Point(getX(), getY()));
		System.out.println("To " + point);

		double distanceY = getY() - point.getY();
		double distanceX = getX() - point.getX();

		turnLeft(getHeading());
		ahead(-distanceY);

		turnRight(90);
		ahead(-distanceX);
	}

	public abstract MemberTpye getMemberType();

	public TeamMember() {

	}

	public QuadrantType getQuadrant(double x, double y) {
		if (x < (maxX / 2)) {
			return (y > (maxY / 2)) ? QuadrantType.QUADRANT_1
					: QuadrantType.QUADRANT_4;
		} else {
			return (y > (maxY / 2)) ? QuadrantType.QUADRANT_2
					: QuadrantType.QUADRANT_3;
		}
	}

	public QuadrantType getOppositeQuadrant(double x, double y) {
		return getQuadrant(maxX - x, maxY - y);
	}
}
