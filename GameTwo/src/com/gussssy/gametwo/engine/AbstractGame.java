package com.gussssy.gametwo.engine;

/**
 * Abstract class for the Game that will interact with the engine via abstract methods: render and update 
 * 
 * These methods will be called by the GameContainer within the game loop, 60 times per second. 
 */
public abstract class AbstractGame{
	
	
	
	/**
	 * Called each frame to update the game
	 */
	public abstract void update(GameContainer gc, float dt);
	
	
	/**
	 * Called each frame to render the game 
	 */
	public abstract void render(GameContainer gc, Renderer r);
	
	
	
	

	
	
	/**
	 * Perform any further setup required after the game has finished construction. 
	 */
	public abstract void init(GameContainer gc);

}
