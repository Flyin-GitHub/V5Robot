package com.v5.www.robot;

import java.io.Serializable;

import com.v5.www.robot.team.TeamMember.QuadrantType;

public class RobotAction implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private boolean isFormation = false;
	private QuadrantType formationType;
	private Point movePoint;
	private Point shootPoint;
	private int firePower = 1;

	public QuadrantType getFormationType() {
		return formationType;
	}

	public void setFormationType(QuadrantType formationType) {
		this.formationType = formationType;
		this.isFormation = true;
	}
	
	public boolean isFormation() {
		return isFormation;
	}

	public void setFormation(boolean isFormation) {
		this.isFormation = isFormation;
	}
	
	public int getFirePower() {
		return firePower;
	}

	public void setFirePower(int firePower) {
		this.firePower = firePower;
	}

	public Point getMovePoint() {
		return movePoint;
	}

	public void setMovePoint(Point movePoint) {
		this.movePoint = movePoint;
	}

	public Point getShootPoint() {
		return shootPoint;
	}

	public void setShootPoint(Point shootPoint) {
		this.shootPoint = shootPoint;
	}
}
