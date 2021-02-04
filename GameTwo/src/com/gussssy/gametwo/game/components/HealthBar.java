package com.gussssy.gametwo.game.components;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.SoundManager;
import com.gussssy.gametwo.game.objects.Charachter;

/**
 * A Health Bar that will be rendered above the parent NPC showing the NPCs current health.
 * 
 * All chnages to an NPCs health is done via this class, not the NPCs class. 
 * 
 * */
public class HealthBar extends Component {
	
	boolean print = false; 
	
	/**
	 * The NPC who's health this health bar will display
	 * */
	public Charachter parent;
	
	/**
	 * The width of the green portion (current health) of the health bar 
	 **/
	private int healthBarPixels = 16;
	
	/**
	 * The parent NPCs health percentage. 
	 **/
	private double healthPercent = 100;
	
	/**
	 * The new width of the green portion of the health bar after a chnage in the parent NPCs health.
	 **/
	private double newHealthBarPixels;
	
	/**
	 * Any additional height needed to position the health bar sensibly above the parent GameObject. 
	 **/
	private int healthBarOffY = 0;
	
	
	
	
	/**
	 * Health Bar constructor 
	 **/
	public HealthBar(Charachter parent){
		this.parent = parent;
		this.tag = "hp";
	}
	
	/**
	 * Changes the NPCs health by the amount of the input value change and updates the graphical representation ofthe health bar to reflect this chnage.  
	 **/
	public void healthChanged(int change){
		
		if(print)System.out.println("HealthParPixels before: " + healthBarPixels);
		
		// set the new underlying health value for the npc
		parent.setHealth(parent.getHealth() + change);
		
		// determine the new size of the green portion of the health bar
		healthPercent = (double)parent.getHealth() / 100;
		newHealthBarPixels = healthPercent * 16.0;
		
		// set new size in pixels of the healthbar after the change in health
		healthBarPixels = (int)newHealthBarPixels;
		
		if(print){
			System.out.println(parent.getTag() + " was just damaged, health is now: " + parent.getHealth());
			System.out.println("new health bar pixels; " + newHealthBarPixels);
			System.out.println("HealthParPixels after: " + healthBarPixels);
		}
		
		
		
	}

	
	/**
	 * Checks to see if the object will die. 
	 **/
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		// prevent player from dying for now
		if(parent.getTag() == "player"){
			return;
		}
		
		// The Charachter will die if health reaches 0
		if(healthPercent <= 0){
			parent.dead = true; 
			SoundManager.dead.stop();
			SoundManager.dead.play();
		}
	}

	
	/**
	 * Renders a health bar above the parent GameObject 
	 **/
	@Override
	public void render(GameContainer gc, Renderer r) {
		
		if(healthPercent == 100)return;
		
		// red bar underlying health bar
		r.drawFillRect((int)parent.getPosX(), (int)parent.getPosY()-2 + healthBarOffY, 16, 1, 0xffff0000);
		
		
		// the green health part
		r.drawFillRect((int)parent.getPosX(), (int)parent.getPosY()-2 + healthBarOffY, (int)healthBarPixels, 1, 0xff00ff00);
		
		/**System.out.println("Render Health Bar. For: " + parent.getTag());
		System.out.println("Health Pixels: " + healthBarPixels);
		System.out.println("PosX of healthbar: " + (parent.getPosX()));
		System.out.println("PosY of healthbar: " + (parent.getPosY()-2));
		System.out.println();*/
		
		
	}

	// why would anything care about the number of pixel in the health bar...
	public int getHealthPixels() {
		return healthBarPixels;
	}

	public int getHealthBarOffY() {
		return healthBarOffY;
	}

	public void setHealthBarOffY(int healthBarOffY) {
		this.healthBarOffY = healthBarOffY;
	}
	
	
	
}
