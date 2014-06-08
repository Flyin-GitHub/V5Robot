package com.v5.team;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;

public class ExampleRobot extends AdvancedRobot {
	private double eDist; //�Է��ľ���
	private double move; //�ƶ��ľ���
	private double radarMove = 360; //�״��ƶ��ĽǶ�
	private double dFirePower; //����
	
	public void run(){
		eDist = 300;
		while(true){
			//ÿ��һ�����ڣ��˶�����ľ���
			double period = 4*((int)(eDist/80)); //���ڣ�����Խ�ӽ�������Խ�̣��ƶ�ԽƵ��
			//���ڿ�ʼ�����ƶ�
			if(getTime()%period == 0){
			move = (Math.random()*2-1)*(period*8 - 25);
			setAhead(move + ((move >= 0) ? 25: -25));
			}
			//����ײǽ
			double heading = getHeadingRadians(); //ȡ��bot����Ļ�����
			double x = getX() + move*Math.sin(heading); //�ƶ�move��Ҫ�ﵽ��x����
			double y = getY() + move*Math.cos(heading); //�ƶ�move��Ҫ�ﵽ��y����
			double dWidth = getBattleFieldWidth(); //ս���Ŀ��
			double dHeight = getBattleFieldHeight(); //ս���ĳ���
			//��(x,y)����ָ���ķ�Χ�������ƶ�move
			if(x < 30 || x > dWidth-30 || y < 30 || y > dHeight-30){
			setBack(move);
			}
			turnRadarLeft(radarMove); //ת���״�
		}
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		eDist = e.getDistance(); //ȡ�öԷ�����
		radarMove = -radarMove; //�����״�
		double eBearing = e.getBearingRadians(); //ȡ�úͶԷ���ԽǶȵĻ�����
		//��botת����ԵĽǶȣ��Ժ�bot���˶������ԶԷ�ΪԲ�ĵ�Բ���˶�
		setTurnLeftRadians(Math.PI/2 - eBearing);
		//ת���ڹ�ָ��Է�
		setTurnGunRightRadians(robocode.util.Utils.normalRelativeAngle(
		getHeadingRadians() + eBearing - getGunHeadingRadians()));
		//���ݶԷ��������
		dFirePower = 400/eDist;
		if (dFirePower > 3){
			dFirePower = 3;
		}
		execute();
		fire(dFirePower);
	}

}
