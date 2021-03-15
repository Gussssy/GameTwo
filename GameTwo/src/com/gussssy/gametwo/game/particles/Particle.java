package com.gussssy.gametwo.game.particles;

import com.gussssy.gametwo.game.GameManager;

/**
 * Base class for all particles
 * 
 *  Similar to GameObject but much simpler.
 *  
 *  As lightweight as possibe
 **/
public abstract class Particle {
	
	
	// Location Variables
	public float posX;
	public float posY;
	protected int tileX;
	protected int tileY;
	
	// Speed Variables 
	protected float vx,vy;
	
	//
	public boolean dead = false;
	
	/***
	 * Particle update 
	 * 
	 * Note: This is the only update method in the game with this signature (does not include the GameContainer)
	 * is this ok? 
	 **/
	public abstract void update(GameManager gm, float dt);

}
