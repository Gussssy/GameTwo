package com.gussssy.gametwo.game.particles;

import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.level.Level;

/**
 * A Particle that is only a single pixel
 * 
 *  NOTE: is it smart to make an object for a single pixel, couldnt I just use multiple arrays for each important variable. 
 *  More effcient but messy..... ??? 
 */
public class PixelParticle extends Particle {
	
	private int color;
	
	boolean falling = true;
	
	public PixelParticle(float posX, float posY, float vx, float vy, int color){
		
		this.posX = posX;
		this.posY = posY;
		this.vx = vx;
		this.vy = vy;
		
		this.color = color;
		
	}

	@Override
	public void update(GameManager gm, float dt) {
		
		if(falling){
			posX += vx * dt;
			vy+= dt*Level.gravity;
			posY += vy;
		}
		
		
		// checking for collsion with tiles:  
		if(gm.getLevelTileCollision((int)(posX/16), (int)(posY/16))){
			falling = false;
		}else{
			falling = true;
		}
		
	}

	/**
	 * Returns an integer representing the colour of this pixel particle
	 * 
	 *  @return color, the color of this pixel particle
	 */
	public int getColor() {
		return color;
	}
	
	
	
	public String toString(){
		
		
		return " . . . PixelParticle . . .\n\tposX: " + posX + "\n\tposY: " + posY + "\n\tcolor: " + color;
	}

}
