package com.v5.team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RobotComparator implements Comparator<RobotInfo>{
	@Override
	public int compare(RobotInfo o1, RobotInfo o2) {
		return o2.getPriority() - o1.getPriority();
	}
	
	public static void main(String[] args) {
		List<RobotInfo> list = new ArrayList<RobotInfo>();
		RobotInfo robot3 = new RobotInfo();
		list.add(robot3);
		robot3.setEnemy(true);
		
		RobotInfo robot1 = new RobotInfo();
		list.add(robot1);
		robot1.setEnemy(true);
		robot1.setEnemyLeader(true);
		
		RobotInfo robot2 = new RobotInfo();
		list.add(robot2);
		robot2.setEnemy(true);
		
		Collections.sort(list, new RobotComparator());
		for (RobotInfo ro:list) {
			System.out.println(ro.getPriority());
		}
	}
}
