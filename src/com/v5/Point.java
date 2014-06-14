package com.v5;

import java.io.Serializable;

public class Point implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private double x;
	private double y;
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	
	@Override
	public String toString() {
		return "X[" + x + "],Y[" + y + "]";
	}
	
	public boolean isReach(double x, double y) {
		return this.x == x && this.y == y;
	}
}
