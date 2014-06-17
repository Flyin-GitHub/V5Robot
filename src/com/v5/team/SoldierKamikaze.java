package com.v5.team;

import java.util.Map;

import robocode.Rules;
import robocode.util.Utils;

import com.v5.Enemy;

/**
 * @author Jacky Cai
 *
 */
public class SoldierKamikaze extends AdvanceSoldier {
	
	public void run (){
		init();
		setTurnLeft(getHeading() % 90);
		while(true){
			if(targets.containsKey(leaderName) && getName().equals(getKamikazeName())){
				kamikazeAction();
			}else{
				wallsAction();
			}
		}
	}
	
	private void setMove(double Angel, double Distance) {
		Angel = normalRelativeAngle(Angel);
		if (Math.abs(Angel) > 90) {
			setTurnLeft((normalRelativeAngle(Angel + 180)) * (-1));
			setBack(Distance);
		} else {
			setTurnRight(Angel);
			setAhead(Distance);
		}
		setMaxVelocity(Rules.MAX_VELOCITY);
	}

	private double normalRelativeAngle(double a) {
		while (a > 180)
			a -= 360;
		while (a <= -180)
			a += 360;
		return a;
	}
	
	private void kamikazeAction(){
		
		// Calculate x and y to target
		Enemy enemy = getAimTarget();
		double dx = enemy.x - this.getX();
		double dy = enemy.y - this.getY();
		double theta = Math.toDegrees(Math.atan2(dx, dy));	
		setTurnRight(Utils.normalRelativeAngleDegrees(theta - getHeading()));
		double moveAway = Math.sqrt(Math.pow(dx, 2)+Math.pow(dy, 2));
		setMove(theta - getHeading(), moveAway);
		setGun(getAimTarget());
		execute();
		
	}
	
	private String getKamikazeName(){
		String kamikaze = null;
		Enemy enemy = getAimTarget();
		if(enemy.name.equals(leaderName)){
			double min = 99999d;
			for(Map.Entry<String, Enemy> entry : teammates.entrySet()){
				if(entry.getKey().equals(myLeaderName))
					break;
				double distance = getRange(enemy.x, enemy.y, entry.getValue().x, entry.getValue().y);
				if(distance < min){
					min = distance;
					kamikaze = entry.getKey();
				}
			}
		}
		return kamikaze;
		
	}
	
	private void wallsAction(){
		if(hit){	
			setAhead(aheadDistance);
		}else{
			setBack(aheadDistance);
		}
		//linerMove();
		execute();
		setGun(getAimTarget());	
		execute();
	}
}
