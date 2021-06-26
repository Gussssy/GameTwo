package com.gussssy.gametwo.game.particles;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.engine.gfx.ImageTile;
import com.gussssy.gametwo.game.Event;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.SoundManager;

public class ElectricEffect implements Event{
	
	private boolean sound = false;
	
	// animation controls
	float animationState = 0f;
	float animationSpeed = 0.2f;
	boolean renderEffect = false;
	int renderTimer; 
	
	// particle color
	int startColor = 0xffffff00;
	int endColor = 0x00eeff00;
	
	// the location of the effect in the game
	private int locationX, locationY;
	
	// Electric effect texture. Animated using a tile Image
	private ImageTile image = new ImageTile("/particles/electric_effect.png", 16,16);
	
	
	int sparkInterval = 10;
	int sparkTimer = sparkInterval;
	
	// Temporary list to hold 'spark particles'
	ArrayList<ColorChangingParticle> sparks = new ArrayList<ColorChangingParticle>();
	
	
	
	
	
	/**
	 * Creates a new ElectricalEffect at the specified location. (In pixels not tiles)
	 * 
	 *  @param locationX the x location where the effect will be rendered
	 *  @param locationY the Y location where the effect will be rendered
	 */
	public ElectricEffect(int locationX, int locationY){
		
		this.locationX = locationX;
		this.locationY = locationY;
		
	}

	/**
	 * Increments the animation state each frame to animate the effect.
	 */
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		
		
		// Electric wave animation 
		
		// Increment the animation state by the animation speed
		animationState += animationSpeed;
		
		//System.out.println("Animation State: " + animationState);
		
		// when animation state exceeds the number of frames in the tile image, animation state is set back to 0, the first frame. 
		if(animationState >= 9)animationState = 0;
		
		
		// Controlling when to render the effect:
		// (rendering is turned on when a spark is made)
		if(renderTimer == 0){
			renderEffect = false;
		}else {
			renderTimer --;
		}
		
		
	
		
		// Sparking
		
		// Making New Sparks at random time intervals
		sparkTimer --; 
		if(sparkTimer == 0){
			if(sound)SoundManager.electricSparks.playRandom();
			
			int vx = ThreadLocalRandom.current().nextInt(-40, 40);
			int vy = ThreadLocalRandom.current().nextInt(-20, 0);
			int x = ThreadLocalRandom.current().nextInt(0, 16);
			int y = ThreadLocalRandom.current().nextInt(0, 16);
			ColorChangingParticle p = new ColorChangingParticle(locationX + x, locationY + y, vx, vy, startColor, endColor, 120, true);
			sparks.add(p);
			
			vx = ThreadLocalRandom.current().nextInt(-40, 40);
			vy = ThreadLocalRandom.current().nextInt(-20, 0);
			x = ThreadLocalRandom.current().nextInt(0, 16);
			y = ThreadLocalRandom.current().nextInt(0, 16);
			p = new ColorChangingParticle(locationX + x, locationY + y, vx, vy, startColor, endColor, 120, true);
			sparks.add(p);
			
			vx = ThreadLocalRandom.current().nextInt(-40, 40);
			vy = ThreadLocalRandom.current().nextInt(-20, 0);
			x = ThreadLocalRandom.current().nextInt(0, 16);
			y = ThreadLocalRandom.current().nextInt(0, 16);
			p = new ColorChangingParticle(locationX + x, locationY + y, vx, vy, startColor, endColor, 120, true);
			sparks.add(p);
			
			vx = ThreadLocalRandom.current().nextInt(-40, 40);
			vy = ThreadLocalRandom.current().nextInt(-20, 0);
			x = ThreadLocalRandom.current().nextInt(0, 16);
			y = ThreadLocalRandom.current().nextInt(0, 16);
			p = new ColorChangingParticle(locationX + x, locationY + y, vx, vy, startColor, endColor, 120, true);
			sparks.add(p);
			
			// set time till next spark
			sparkTimer = ThreadLocalRandom.current().nextInt(20, 120);
			
			// turn on rendering for next 5 frames
			renderEffect = true;
			renderTimer = 15;
		}
		
		// Updating Current Sparks
		for(ColorChangingParticle p : sparks){
			p.update(gm, dt);
		}
	
		
		
		
	}

	@Override
	public void render(Renderer r) {
		
		// Rendering the electric wave effect
		if(renderEffect)r.drawImageTile(image, locationX, locationY, 0, (int)animationState);
		
		// Rendering spark particles
		for(ColorChangingParticle p : sparks){
			p.Render(r);
		}
		
		
	}

	@Override
	public void endEvent() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the locationY
	 */
	public int getLocationY() {
		return locationY;
	}

	/**
	 * @param locationY the locationY to set
	 */
	public void setLocationY(int locationY) {
		this.locationY = locationY;
	}

	/**
	 * @return the locationX
	 */
	public int getLocationX() {
		return locationX;
	}

	/**
	 * @param locationX the locationX to set
	 */
	public void setLocationX(int locationX) {
		this.locationX = locationX;
	}

}
