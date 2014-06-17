package com.v5.team;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import robocode.Droid;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.MessageEvent;
import robocode.RobotDeathEvent;
import robocode.RoundEndedEvent;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;
import robocode.util.Utils;

import com.v5.Enemy;
import com.v5.Message;
import com.v5.MessageType;
import com.v5.Point;
import com.v5.RobotAction;

public class AdvanceSoldier extends TeamRobot implements Droid {
	static double fieldHeight;
	static double fieldWidth;
	protected static final double PI = 3.14159265d;
	protected Map<String,Enemy> targets = new HashMap<String,Enemy>();
	protected Map<String,Enemy> teammates = new HashMap<String,Enemy>();
	protected String leaderName;
	protected String myLeaderName;
	protected boolean hit = true;
	
	protected static final double aheadDistance = 200;
	
	
	protected void init(){
		fieldHeight = getBattleFieldHeight();
		fieldWidth = getBattleFieldWidth();
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);	
		setEventPriority();
	}
	public void run (){
		init();
		setTurnLeft(getHeading() % 90);
		while(true){
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
	public void onMessageReceived(MessageEvent e) {
		if (e.getMessage() instanceof Message) {
			Message message = (Message) e.getMessage();
			MessageType type = message.getType();
			RobotAction action  = (RobotAction)message.getContent();
			switch (type) {
			case ENEMY_INFO:
				updateEnemy(action.getEvent(), action.getShootPoint());
				break;
			case TEAMMATE_INFO:
				updateEnemy(action.getEvent(), action.getShootPoint());
				break;
			case HEADER_DIED:
				break;
			default:
				out.println("[No Surrpoted Message Type:] " + type.toString());
			}
		}
	}
	
	
	protected void updateEnemy(ScannedRobotEvent e, Point point){
		Enemy en;
		if (targets.containsKey(e.getName())) {
			en = (Enemy)targets.get(e.getName());
		} else {
			en = new Enemy();
			targets.put(e.getName(),en);
			en.name	= e.getName();
			en.isLeader =  isLeader(e.getEnergy());
			if(en.isLeader){
				leaderName = e.getName();
			}
		}
		long lastScanTime = en.scanTime;	
		en.live			= true;
		en.headDiff		=  normalizeAngle(e.getHeadingRadians() - en.headRad) / (e.getTime() - lastScanTime);
		en.x				= point.getX();
		en.y				= point.getY();
		en.scanTime			= e.getTime();
		en.energyDiff		= en.energy - e.getEnergy();
		en.energy			= e.getEnergy();
		en.head				= e.getHeading();
		en.headRad			= e.getHeadingRadians();
		en.bear				= e.getBearing();
		en.bearRad			= e.getBearingRadians();
		en.distance			= getRange(getX(), getY(), point.getX(), point.getY());
		en.wallDistance		= wallDistance(en.x, en.y);
		if (en.wallDistance < en.minWallDistance) en.minWallDistance = en.wallDistance;
		en.velocity			= e.getVelocity();
		en.velocityA		+= en.velocity;
		en.velocityAt++;	
	}
	
	
	protected void updateTeammates(ScannedRobotEvent e, Point point){
		Enemy en;
		if (teammates.containsKey(e.getName())) {
			en = (Enemy)teammates.get(e.getName());
		} else {
			en = new Enemy();
			teammates.put(e.getName(),en);
			en.name	= e.getName();
			en.isLeader =  isLeader(e.getEnergy());
			if(en.isLeader){
				myLeaderName = e.getName();
			}
		}
		long lastScanTime = en.scanTime;	
		en.live			= true;
		en.headDiff		=  normalizeAngle(e.getHeadingRadians() - en.headRad) / (e.getTime() - lastScanTime);
		en.x				= point.getX();
		en.y				= point.getY();
		en.scanTime			= e.getTime();
		en.energyDiff		= en.energy - e.getEnergy();
		en.energy			= e.getEnergy();
		en.head				= e.getHeading();
		en.headRad			= e.getHeadingRadians();
		en.bear				= e.getBearing();
		en.bearRad			= e.getBearingRadians();
		en.distance			= getRange(getX(), getY(), point.getX(), point.getY());
		en.wallDistance		= wallDistance(en.x, en.y);
		if (en.wallDistance < en.minWallDistance) en.minWallDistance = en.wallDistance;
		en.velocity			= e.getVelocity();
		en.velocityA		+= en.velocity;
		en.velocityAt++;	
	}
	
	

	/******************************************************************************************
	 * normalize radian angel
	 * @return angel (-PI to PI)
	 ******************************************************************************************
	 */
	protected double normalizeAngle( double r ) {
		while(r > PI)
			r -= 2 * PI;
		while (r <- Math.PI)
		 	r += 2 * PI;
		return r;
	}
	
	/******************************************************************************************
	 * Gets the distance to nearest wall
	 * @return distance
	 ******************************************************************************************
	 */
    protected double wallDistance(double x, double y) {
        double distX = Math.min(fieldWidth - x, x);
        double distY = Math.min(fieldHeight - y, y);
        return Math.min(distY,distX);
    }
    
	/******************************************************************************************
	 * Gets the distance between two x,y coordinates
	 * @return distance
	 ******************************************************************************************
	 */
	protected double getRange(double x1, double y1, double x2, double y2)
	{
		double xo = x2-x1;
		double yo = y2-y1;
		double h = Math.sqrt( xo*xo + yo*yo );
		return h;	
	}
	
	protected boolean isLeader(double initEnergy) {
		return initEnergy >= 150;
	}
	
	public void setGun(Enemy enemy) {
		if (enemy != null) {		
			if (getEnergy() >= 1)	{
				double dx = enemy.x - this.getX();
				double dy = enemy.y - this.getY();
				double theta = Math.toDegrees(Math.atan2(dx, dy));
				//double distance = getRange(enemy.x, enemy.y, this.getX(), this.getY());
				setTurnGunRight(Utils.normalRelativeAngleDegrees(theta - getGunHeading()) );
				setFire(3);
			}		
		}
	}
	
	protected void setEventPriority() {
		setEventPriority("ScannedRobotEvent", 10); // 10
		setEventPriority("HitWallEvent", 20); // 30
		setEventPriority("BulletHitEvent", 30); // 50
		setEventPriority("BulletHitBulletEvent", 40); // 50
		setEventPriority("BulletMissedEvent", 50); // 60
		setEventPriority("RobotDeathEvent", 60); // 70
		setEventPriority("HitByBulletEvent", 70); // 40
		setEventPriority("HitRobotEvent", 80); // 20
	}
	
	
	protected Enemy getAimTarget(){
		if(targets.containsKey(leaderName)){
			return targets.get(leaderName);
		}
		double min = 99999d;
		Enemy enemy = null;
		for(Map.Entry<String, Enemy> entry : targets.entrySet()){
			if(entry.getValue().distance < min){
				min = entry.getValue().distance;
				enemy = entry.getValue();
			}
		}
		return enemy;
	}
	
	@Override
	public void onRobotDeath(RobotDeathEvent e) {
		if(isTeammate(e.getName())){
			teammates.remove(e.getName());
		}else{		
			targets.remove(e.getName());
		}
		super.onRobotDeath(e);
	}
	
	
	@Override
	public void onHitWall(HitWallEvent e) {
		setTurnRight(Utils.normalRelativeAngleDegrees(e.getBearing())+90);	
	}

	@Override
	public void onHitByBullet(HitByBulletEvent e) {
		setTurnRight(e.getBearing() + 90);
		setAhead(10);
		execute();
		//circleMove(e.getBearing() + 180);
		//hit = hit ? false : true;
	}

	@Override
	public void onHitRobot(HitRobotEvent e) {
		if(e.getName().equals(leaderName)){
			if(hit){
				setAhead(aheadDistance*2);
			}else{
				setBack(aheadDistance*2);
			}
		}else{
			hit = hit ? false : true;
		}
	}
	
	
}
