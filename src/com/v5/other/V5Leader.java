package com.v5.other;

import java.io.IOException;

import com.v5.AbstractRobot;
import com.v5.Message;
import com.v5.MessageType;
import com.v5.Point;


import robocode.ScannedRobotEvent;

public class V5Leader extends AbstractRobot{
	
	public void onScannedRobot(ScannedRobotEvent e){
		if (!isTeammate(e.getName())) {	
			double enemyBearing = this.getHeading() + e.getBearing();
			// Calculate enemy's position
			double enemyX = getX() + e.getDistance()
					* Math.sin(Math.toRadians(enemyBearing));
			double enemyY = getY() + e.getDistance()
					* Math.cos(Math.toRadians(enemyBearing));
			try {
				// Send enemy position to teammates
				broadcastMessage(new Message(MessageType.ENEMY_INFO, new Point(enemyX,enemyY)));
			} catch (IOException ex) {
				out.println("Unable to send order: ");
				ex.printStackTrace(out);
			}
			super.onScannedRobot(e);
		}
	}

}
