package com.v5;

import java.io.Serializable;

import robocode.ScannedRobotEvent;

import com.v5.team.formation.TeamMember.QuadrantType;

public class RobotAction implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private boolean isFormation = false;
	private QuadrantType formationType;
	private Point movePoint;
	private Point shootPoint;
	private int firePower = 1;
	private ScannedRobotEvent event;

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

	public ScannedRobotEvent getEvent() {
		return event;
	}

	public void setEvent(ScannedRobotEvent event) {
		this.event = event;
	}
	
	
}
