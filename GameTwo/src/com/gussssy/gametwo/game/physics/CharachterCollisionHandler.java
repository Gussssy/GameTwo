package com.gussssy.gametwo.game.physics;

import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.debug.DebugPanel;
import com.gussssy.gametwo.game.objects.Charachter;
import com.gussssy.gametwo.game.objects.GameObject;

public class CharachterCollisionHandler implements CollisionHandler{
	
	// the Object that this Collision Handler is handling collsions for..... 
	Charachter c;
	AABBComponent hitBox;
	
	
	// the distance the object has penetrated into the hitbox of the other object
	int topDistance, botDistance, leftDistance, rightDistance;
	
	// Velocity of the charachter on each axis when it collided, determined by the change on each axis since the last frame and the current frame
	float dx, dy;

	// the 'time' taken to acheive this distance if only velocity on the respective axis is considered
	//		- this is neccesary to trace back to where the collision first occured to determine which side was collided with
	float topTime, botTime, leftTime, rightTime;

	// whether or not a collision on the respective side is possible
	boolean topPossible, botPossible, leftPossible, rightPossible;
	
	
	
	
	
	/**
	 * CharachterCollisionHandler sole constructor.
	 * - sets the Charachter that this instance will process collisions for.  
	 **/
	public CharachterCollisionHandler(Charachter c){
		
		this.c = c;
		
	}
	
	
	

	@Override
	public void collisionWithStationaryObject(GameObject other, AABBComponent otherHitBox) {
		
		topPossible = false;
		botPossible = false;
		leftPossible = false;
		rightPossible = false;

		topTime = 9999;
		botTime = 9999;
		leftTime = 9999;
		rightTime = 9999;

		// does this need to be done every time? 
		hitBox = (AABBComponent) c.findComponent("aabb");

		
		// this only works for stationary object collisions


		//System.out.println("AABB Collison with Stationary Object: " + other.getTag());

		//AABBComponent platformHitbox = (AABBComponent) other.findComponent("aabb");
		float dy = hitBox.getCenterY() - hitBox.getLastCenterY();
		float dx = hitBox.getCenterX() - hitBox.getLastCenterX();
		DebugPanel.message3  = "dx: " + dx;
		DebugPanel.message4  = "dy: " + dy;


		// how much the object has penetrated top, bottom, left and right into the other objects hitbox 
		topDistance = hitBox.getStopY() - otherHitBox.getStartY();
		botDistance = otherHitBox.getStopY() - hitBox.getStartY();
		leftDistance = hitBox.getStopX() - otherHitBox.getStartX();
		rightDistance = otherHitBox.getStopX() -  hitBox.getStartX();

		DebugPanel.message5 = "TopDistance: " + topDistance;
		DebugPanel.message6 = "BotDistance: " + botDistance;
		DebugPanel.message7 = "LeftInc: " + leftDistance;
		DebugPanel.message8 = "RightInc: " + rightDistance;


		// what side hit the hitbox first?

		// X AXIS
		if(dx != 0){
			// it could be left or right

			// if dx > 0, object is travelling right, could have impacted left side
			if(dx > 0){
				leftPossible = true;
				leftTime = Math.abs(leftDistance/dx);
				// 
			}else {
				rightPossible = true;
				rightTime = Math.abs(rightDistance/dx);
			}
			//  if dx < 0, object is travelling left, could have impacted right side
		}else {
			leftPossible = false;
			rightPossible = false;
		}

		// Y AXIS
		if(dy != 0){
			// it could be left or right

			// if dy > 0, object is travelling down, could have impacted top side
			if(dy > 0){
				topPossible = true;
				topTime = Math.abs(topDistance/dy);
				// 
			}else {
				botPossible = true;
				botTime =Math.abs(botDistance/dy);
			}
			//  if dy < 0, object is travelling up, could have impacted bottom side
		}else {
			topPossible = false;
			botPossible = false;
		}


		// PROCESSING

		// Single Axis Collision Processing
		// no movement on X Axis 
		if(dx == 0){

			if(topPossible){
				// top coll
				System.out.println("Top Collision");
				c.incrementOffY(-topDistance);
				c.fallDistance = 0;
				c.onAABB = true;
				c.onThisObject = otherHitBox;
				c.setPosition();
				
				
				
			}

			if(botPossible){
				System.out.println("Bot Collision");	
				//posY += botDistance;
				c.incrementPosY(botDistance);
				
				
			}

			// no movement on Y Axis
		} else if(dy == 0){

			if(leftPossible){
				System.out.println("Left Collision");	
				//posX -= leftDistance;
				c.incrementOffX(-leftDistance);
				
			}

			if(rightPossible){
				System.out.println("Right Collision");
				//posX += rightDistance;
				c.incrementOffX(rightDistance);
				
			}


			// Movement on both Axis
			// Moving Right and Down:  could be left or top collision
		} else if(dx > 0 && dy > 0){

			if(leftTime < topTime){
				// left coll
				System.out.println("Left Collision");
				//posX -= leftDistance;
				c.incrementOffX(-leftDistance);
				
			}else {
				// top coll
				System.out.println("Top Collision");
				//posY -= topDistance;
				c.incrementOffY(-topDistance);
				
				
			}

			// Moving Right and Up : could be left or bot collision
		}else if(dx > 0 && dy < 0 ){

			if(leftTime < botTime){
				// left coll
				System.out.println("Left Collision");
				//posX -= leftDistance;
				c.incrementOffX(-leftDistance);
				
			}else {
				// bot col
				System.out.println("Bot Collision");
				//posY += botDistance;
				c.incrementPosY(botDistance);
				
			}

			// Moving Left and Down: could be right or top collision
		}else if(dx < 0 && dy > 0){

			if(rightTime < topTime){
				// right coll
				System.out.println("Right Collision");
				//posX += rightDistance;
				c.incrementOffX(rightDistance);
				
			}else {
				// top col;
				System.out.println("Top Collision");
				//posY -= topDistance;
				c.incrementOffY(-topDistance);
				
			}


			// Moving Left and Up: could be right or bot collision	
		}else if(dx < 0 && dy < 0){

			if(rightTime < botTime){
				// right coll
				System.out.println("Right Collision");
				//posX += rightDistance;
				c.incrementOffX(rightDistance);
				
			}else {
				// bot col
				System.out.println("Bot Collision");
				//posY += botDistance;
				c.incrementPosY(botDistance);
				
			}

		}

		// UPDATE THE AABB



		
	}
	
	

	/**
	 * Pick up the item if this charchter is able to pick up items else dont do anything
	 **/
	@Override
	public void itemCollision(GameObject item) {
		// TODO Auto-generated method stub
		
	}
	
	

	
	/**
	 *  The purpose of this if I ever use it: 
	 *  	- all collisions will be funelled into this method and the apprpriate method selected to process incoming collisions
	 *  
	 *   Hmmmmm No i dont like this. The object that is collising should call this to process positional changes but other 
	 *   interactions should be done in thier respective collision methods e.g make a sound specific to the object
	 **/
	@Override
	public void processCollision(GameObject collider) {
		
		//switch(collider.collisionType)
		
	}
	
	

	











	

	
}
