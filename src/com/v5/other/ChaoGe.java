package com.v5.other;
import robocode.AdvancedRobot;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;
//import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * TbRobot - a robot by (your name here)
 */
public class ChaoGe extends AdvancedRobot
{
	private static final boolean RIGHT = false;
	private static final boolean LEFT = false;

	/**
	 * run: TbRobot's default behavior
	 */
	public void run() {
		// Initialization of the robot should be put here

		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		// setColors(Color.red,Color.blue,Color.green); // body,gun,radar

		// Robot main loop
		while(true) {
			// Replace the next 4 lines with any behavior you would like
			
			System.out.println(getX() + "@" + getY());
			
			ahead(1000);
			turnGunRight(360);
			turnRight(30);
			back(100);
			turnRight(30);
			turnGunRight(360);
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		double heading = e.getBearing() +getHeading(); 
	       double distance = e.getDistance(); // 求得距离
	       double ager_bearing = Math.toRadians(heading % 360); // 角度转为弧度
	       double genyX = getX() + Math.sin(ager_bearing) * distance; 
	       double genyY = getY() + Math.cos(ager_bearing) * distance; 
	       out.println("genyX:"+ Math.round(genyX)); 
	       out.println("genyY:"+ Math.round(genyY)); 
	    
	        if( heading >= 360 ) 
	            heading = heading - 360; 
	        if( heading < 0 ) 
	            heading = heading +360; 
	        double bearing = getRadarHeading() - heading; 
	        double radar_degree; 
	        boolean radar_direction; 
	        if( 0 <= bearing && bearing <= 180 ) 
	        { 
	            radar_direction = LEFT; 
	        } 
	        else if( bearing <= -180 ) 
	        { 
	            radar_direction = LEFT; 
	            bearing = ( 360 + bearing ); 
	        } 
	        else if( bearing < 0 ) 
	        { 
	            radar_direction = RIGHT; 
	            bearing =( -bearing ); 
	        } 
	        else 
	        { 
	            radar_direction = RIGHT; 
	            bearing = (360 - bearing); 
	        } 
	        radar_degree = bearing * 1.3 ; // 加大每一时间周期 (tick) 的扫描范围
	              
	        if( radar_direction == RIGHT ) 
	        { 
	            setTurnRadarRight( radar_degree ); 
	           fire(1); 
	        } 
	        else 
	        { 
	            setTurnRadarLeft( radar_degree ); 
	            fire(1); 
	        }
	}
	
	public void onHitRobot(HitRobotEvent e) {
		//fire(2);
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		back(5);
		turnRight(20);
		back(5);
		turnRight(30);
		ahead(50);
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		back(20);

	}	
}
