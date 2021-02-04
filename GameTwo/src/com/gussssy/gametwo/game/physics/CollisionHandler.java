package com.gussssy.gametwo.game.physics;

import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.objects.GameObject;

/**
 * Collision Handler Interface.  
 * 
 *  - Classes implementing Collision Handler will contain methods for processing collisions for a certain type of GameObject. 
 **/
public interface CollisionHandler {
	
	/**
	 * Recieve collisions and delegate to the apprpriate collison method
	 **/
	public void processCollision(GameObject collider);
	
	/**
	 * Collision with a charachter 
	 **/
	//public void CharachterCollision();	// don't know if I will ever implement this, in a 2D world, colliding with charachters may be a bad idea?
	
	/**
	 * Process Collision with stationary objects.
	 *  - determine if top, bottom, left or right collision and then adjust the objects position accordingly. 
	 **/
	public void collisionWithStationaryObject(GameObject other, AABBComponent otherHitBox);
	
	/**
	 * Process a collision with an item. Probably will always just be picking up the item 
	 **/
	public void itemCollision(GameObject item);

}
