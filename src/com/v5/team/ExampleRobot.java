package com.v5.team;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;

public class ExampleRobot extends AdvancedRobot {
	private double eDist; //对方的距离
	private double move; //移动的距离
	private double radarMove = 360; //雷达移动的角度
	private double dFirePower; //火力
	
	public void run(){
		eDist = 300;
		while(true){
			//每过一个周期，运动随机的距离
			double period = 4*((int)(eDist/80)); //周期；敌人越接近，周期越短，移动越频繁
			//周期开始，则移动
			if(getTime()%period == 0){
			move = (Math.random()*2-1)*(period*8 - 25);
			setAhead(move + ((move >= 0) ? 25: -25));
			}
			//避免撞墙
			double heading = getHeadingRadians(); //取得bot方向的弧度数
			double x = getX() + move*Math.sin(heading); //移动move后将要达到的x坐标
			double y = getY() + move*Math.cos(heading); //移动move后将要达到的y坐标
			double dWidth = getBattleFieldWidth(); //战场的宽度
			double dHeight = getBattleFieldHeight(); //战场的长度
			//当(x,y)超过指定的范围，则反向移动move
			if(x < 30 || x > dWidth-30 || y < 30 || y > dHeight-30){
			setBack(move);
			}
			turnRadarLeft(radarMove); //转动雷达
		}
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		eDist = e.getDistance(); //取得对方距离
		radarMove = -radarMove; //设置雷达
		double eBearing = e.getBearingRadians(); //取得和对方相对角度的弧度数
		//将bot转动相对的角度，以后bot的运动将是以对方为圆心的圆周运动
		setTurnLeftRadians(Math.PI/2 - eBearing);
		//转动炮管指向对方
		setTurnGunRightRadians(robocode.util.Utils.normalRelativeAngle(
		getHeadingRadians() + eBearing - getGunHeadingRadians()));
		//根据对方距离射击
		dFirePower = 400/eDist;
		if (dFirePower > 3){
			dFirePower = 3;
		}
		execute();
		fire(dFirePower);
	}

}
