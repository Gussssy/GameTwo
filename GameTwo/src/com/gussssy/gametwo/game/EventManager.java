package com.gussssy.gametwo.game;

import java.util.ArrayList;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;

/**
 * Manages events.
 * <p>
 * Holds a list containing all currently active events.
 * Updates and renders these events each frame.
 * Adds new Events to the events list.
 * [planned] Removes events that have finished. [not yet, events don't end yet]
 * Removes all Events from the events list when a new level is loaded.
 * <p>
 * Currently the only types of Events include TileDestruction and ElectricEffect
 */
public class EventManager {
	
	
	
	// Contains all currently active events
	private static ArrayList<Event> events = new ArrayList<Event>();
	
	
	
	/**
	 * Updates all the Events in the events list
	 * 
	 * @param gc The GameContainer, ion this context used to access keyboard and mouse input events via the Input class
	 * @param gm The GameManager, in this context used to access the level for level tile collision detection
	 * @param dt time passed since the last update (in seconds)
	 */
	static void update(GameContainer gc, GameManager gm, float dt){
		
		for(Event e : events){
			
			e.update(gc, gm, dt);
		}
		
	}
	
	
	
	/**
	 * Renders all the Events in the events list.
	 * 
	 *  @param r The renderer. 
	 */
	static void render(Renderer r){
		
		for(Event e : events){
			e.render(r);
		}
	}
	
	
	
	/**
	 * Add an Event to the events list.
	 * <p>
	 * Once an Event is added to the events list it will be updated and rendered each frame.
	 * 
	 * @param e The Event to be added to the events list
	 */
	public static void addEvent(Event e){
		events.add(e);
	}
	
	
	
	/**
	 * Removes all events from the events list
	 * <p>
	 * NOTE: Assuming that events are all specific to the currently loaded level.. they are currently but this may change.
	 */
	public static void purge(){
		
		events.clear();
		
	}

}
