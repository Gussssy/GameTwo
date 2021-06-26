package com.gussssy.gametwo.game.objects.environment;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.engine.gfx.Image;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.objects.GameObject;

/**
 * A moon that will move across the sky slowly. 
 */
public class Moon extends GameObject{
	
	
	// Velocity of the moon across the sky
	float vx = 20f;							// would like vx to be lower but animation looks increasingly jerky below 20
	float vy = 0.01f;
	
	float initX, initY;
	
	// The moons texture. Initialized by the constructor. 
	private Image image; 
	
	
	
	
	
	
	/**
	 * Constructs a new moon at a location specified by posX and posY.  
	 * 
	 * A texture will be loaded from the file location specified by path.
	 * 
	 *   @param path String containing the location of the moon texture to be loaded.
	 *   @param posX the x location where the moon will initially be drawn
	 *   @param posY the y location where the moon will initially be drawn
	 */
	public Moon(String path, float posX, float posY){
		
		image = new Image(path);
		this.posX = posX;
		this.posY = posY;
		
		// Save initial coords
		//initX = posX; 
		//initY = posY;
		
	}
	
	
	/**
	 * Updates the moons position each frame.
	 * 
	 * @param gc the GameContainer. (not used)
	 * @param gm the GameManager. (not used)
	 * @param dt the time passed since last update.
	 */
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		posX += vx*dt;
		posY += vy*dt;
		
		
		
		
		// Return to start x/y when leaving the level
		if(posX > gm.getLevelWidth()*GameManager.TS){
			//posX = initX;
			posX = 0;
		}
		
		if(posY > gm.getLevelHeight()*GameManager.TS){
			//posY = initY;
			posY = 0;
		}
		
		
	}

	
	
	@Override
	public void render(Renderer r) {
		
		r.drawImage(image, (int)posX, (int)posY);
		
	}

	
	
	
	@Override
	public void collision(GameObject other, AABBComponent otherHitBox) {
		// TODO Auto-generated method stub
		
	}

}
