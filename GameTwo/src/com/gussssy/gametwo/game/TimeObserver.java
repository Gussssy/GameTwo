package com.gussssy.gametwo.game;

/**
 * Objects that will be notified when DayNightClycle changes from day to night 
 **/
public interface TimeObserver {
	
	public void observeChange(boolean night);

}
