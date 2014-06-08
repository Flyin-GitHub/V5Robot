package com.v5.www.robot;

import navigation.CannonFodderState;
import navigation.ExtendedRobot;
import navigation.StateManager;
import navigation.TrackState;
import robocode.ScannedRobotEvent;

/**
 * NavigationStateExample - An example of switching navigation states based on
 *                          past performance.<br>
 * This example is designed for one-on-one encounters only, but could be
 * extended with a little effort to include melee battles.
 */
public class NavigationStateExample extends ExtendedRobot {

	/**
	 * run: NavigationStateExample's default behavior
	 */
	public void run() {
        try {
            // Set up and enable the state manager
            StateManager navigation = new StateManager(this);
            navigation.addState(new CannonFodderState(this));
            navigation.addState(new TrackState(this));
            addCommandListener(navigation);
            enable();

            // Set turret to move independent of body
            setAdjustGunForRobotTurn(true);

            // Main bot execution loop
		    while(true) {
                // Spin gun
                setTurnGunRightRadians(Math.PI);
                // Allow StateManager to do it's thing
		    	executeTurn();
                // Finish the turn
                execute();
	    	}
        } finally {
            // Disable the State Manager
            disable();
        }
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent event) {
		fire(10);
        super.onScannedRobot(event);
	}

}
