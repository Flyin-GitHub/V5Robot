package com.v5;

/******************************************************************************************
 * enemy information
 ******************************************************************************************
 */
public class Enemy {
	public String name;
	public boolean	live;
	public long		scanTime;
	public double		x,y;
	public double		energy;
	public double		energyDiff;
	public double 	head;
	public double 	headRad;
	public double		bear;
	public double		bearRad;
	public double		absBearRad;
	public double		headDiff;
	public double		velocity;
	public double		velocityA;
	public double		velocityAt;
	public double		velocityB;
	public double		lsVelocityA;
	public double		lsVelocityB;
	public long		lsTime;
	public double		distance;
	public long		lastFireTime;
	public double		lastFireEnergy;
	public boolean	lastFireReplied;
	public long		nextFireTime;
	public long		survivalPoints;
	public long		finalist;
	public long		finalistWinner;
	public long		finalistLooser;
	public double		hitByBulletDamage;
	public double		totalHitByBulletDamage;
	public int		survive;
	public double		wallDistance;
	public double		minWallDistance = 100;
	public long		oVo_battles;
	public int		oVo_workingMove;
	public int		oVo_workingShot;
	public int		oVo_workingFire;
	public long		bulletsHit  [] = new long[6];
	public long		bulletsMiss [] = new long[6];
	public long		bulletsElse [] = new long[6];
	public long		bulletsFired[] = new long[6];
	public long		bulletsBreak[] = new long[6];
	public boolean  isLeader;
}

