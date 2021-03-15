package com.gussssy.gametwo.game.objects.tempobjects;

import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.objects.GameObject;

/**
 * A Rectangle that will only exist for a specified amount of frames. 
 */
public class TempRectangle extends TempObject {
	
	// The dimensions of this temporary rectangle.
	int height, width;
	
	// The pixel location of this temporary rectangle
	int x, y;

	
	
	/**
	 * Constructs a new temporary rectangle with specified height, width, color and lifetime. 
	 * 
	 *  @param x the x location of this temp rectangle
	 *  @param y the y location of the temp rectangle
	 *  @param width the width of this temp rectangle 
	 *  @param height the height of this temp rectangle
	 *  @param color the color of this temp rectangle
	 *  @param lifetime how many frames this temp rectangle will exists for before being removed from the GameObjects list
	 */
	public TempRectangle(int x, int y, int width, int height, int color, int lifeTime) {
		super(lifeTime);
		
		this.x = x;
		this.y = y;
		this.width = width; 
		this.height = height;
		this.color = color;
		this.lifeTime = lifeTime;
		
		
	}

	@Override
	public void render(Renderer r) {
		
		r.drawRect(x, y, width, height, color);
		
	}

	@Override
	public void collision(GameObject other, AABBComponent otherHitBox) {
		// TODO Auto-generated method stub
		
	}

}
