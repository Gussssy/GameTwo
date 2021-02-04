package com.gussssy.gametwo.game;

import java.util.ArrayList;

import com.gussssy.gametwo.game.debug.DebugPanel;

public class DayNightCycle {
	
	ArrayList<TimeObserver> observers = new ArrayList<TimeObserver>();
	
	public boolean night;
	public boolean day;
	private boolean notified = false;
	
	
	private int nightStart = 1500;
	private int dayLength = 3000;
	
	int count = 2000;
	
	private int ambientColor = 0xffffffff;
	private int light = 255;
	
	private int minimumLight = 1;
	
	int waitTime = 5;
	int waitCounter = waitTime;
	
	
	GameManager gm;
	
	
	public DayNightCycle(GameManager gm){
		this.gm = gm;
		
		if(count < nightStart){
			night = true;
			day = false;
		}else{
			night = false;
			day = true;
		}
		
	}
	
	public void update(){
		
		// decrement counter
		count--;
		
		DebugPanel.message1 = "Time: " + count;
		DebugPanel.message2 = "Light: " + light;
		
		if(count <= nightStart){
			
			// its night time!
			if(night == false){
				night = true;
				//notifyObservers();	// notify observers that it is now night
			}
			
			
			// has it got dark enough? 
			if(light > minimumLight){
				
				// has it been sufficiently long since it got darker? 
				if(waitCounter <= 0){
					
					// yes, darken
					light--;
					ambientColor = (255 << 24 | light << 16 | light << 8 | light);
					gm.setAmbientColor(ambientColor);
					
					waitCounter = waitTime;
					
				}else{
					// no, dont darken this frame
					waitCounter --;
				}	
			}else{
				notifyObservers();
			}
			
			if(count <= 0){
				// time for dawn
				count = dayLength;
				night = false;
				notified = false;
				// notify observers it is now day
				//notifyObservers();
			}
			
			
		}else{
			// its day time
			
			if(light < 255){
				
				// have it been long enough since last lightened?
				if(waitCounter <= 0){
					
					// yes, time to lighten
					light++;
					ambientColor = (255 << 24 | light << 16 | light << 8 | light);
					gm.setAmbientColor(ambientColor);
					
					waitCounter = waitTime;
					
					// testing when to notify
					if(!notified && light > 150){
						notifyObservers();
						notified = true;
					}
					
					
				}else{
					// no, need to wait more
					waitCounter--;
				}
				
			}
			
			
		}
	}

	public void addObserver(TimeObserver observer){
		observers.add(observer);
	}
	
	private void notifyObservers(){
		for(TimeObserver to : observers){
			to.observeChange(night);
		}
	}
	
	public boolean isNight() {
		return night;
	}

}
