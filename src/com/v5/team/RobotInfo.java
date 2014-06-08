package com.v5.team;

import com.v5.www.robot.Point;

public class RobotInfo {
	public final static int DISABLE_NOT_FIND_CNT = 15;
	
	private String name;
	private boolean isEnemy;
	private boolean isEnemyLeader;
	private Point preLoation;
	private int priority;
	private int notFind = 0;
	
	@Override
	public String toString() {
		return "Name[" + name + "], Priority[" + priority + "]";
	}
	
	public boolean found(String name) {
		boolean isFound = this.name.equals(name);
		notFind = (isFound) ? 0 : ++notFind;
		return isFound;
	}
	
	public boolean isDisable() {
		return notFind > DISABLE_NOT_FIND_CNT;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEnemy() {
		return isEnemy;
	}

	public void setEnemy(boolean isEnemy) {
		if (isEnemy) 
			priority += 10;
		this.isEnemy = isEnemy;
	}

	public boolean isEnemyLeader() {
		return isEnemyLeader;
	}

	public void setEnemyLeader(boolean isEnemyLeader) {
		if (isEnemyLeader) 
			priority += 20;
		this.isEnemyLeader = isEnemyLeader;
	}

	public Point getPreLoation() {
		return preLoation;
	}

	public void setPreLoation(Point preLoation) {
		this.preLoation = preLoation;
	}
}
