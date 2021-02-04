package com.gussssy.gametwo.game.objects.testObjects;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.debug.DebugPanel;
import com.gussssy.gametwo.game.objects.GameObject;

public class SecondaryCollisionTesterObject extends GameObject {
	
	
	AABBComponent hitbox;
	float vx, vy;
	CollisionTesterObject parent; 
	
	MoveLocation resolution;
	
	boolean colliding = false;
	
	
	public SecondaryCollisionTesterObject(float posX, float posY, int width, int height, CollisionTesterObject parent){
		
		this.tag = "collision_tester_secondary";
		this.parent = parent; 
		this.posX = posX;
		this.posY = posY;
		this.tileX = (int)posX/GameManager.TS;
		this.tileY = (int)posY/GameManager.TS;
		
		this.width = width;
		this.height = height;
		
		// hitbox will have last frame postion set manually as this object represents the movement of the primary collison tester object
		hitbox = new AABBComponent(this, 2);
		addComponent(hitbox);
		
		resolution = new MoveLocation(width, height, 0x5500ff00);
		
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		colliding = false; 
		resolution.x = (int)posX;
		resolution.y = (int)posY;
		
		
		// update the hit box
		hitbox.setLastCenterX(parent.hitBox.getCenterX());
		hitbox.setLastCenterY(parent.hitBox.getCenterY());
		updateComponents(gc, gm, dt);
		
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		
		r.drawFillRect((int)(posX), (int)(posY), width, height, 0x55ffffff);
		if(colliding)r.drawFillRect(resolution.x, resolution.y, width, height, 0x5500ff00);


		renderComponents(gc,r);
		
	}
	
	// the distance the object has penetrated the hitbox
	int topDistance, botDistance, leftDistance, rightDistance;
	// the 'time' taken to acheive this distance if only velocity on the respective axis is considered
	float topTime, botTime, leftTime, rightTime;
	// whether or not a collision on the respective side is possible
	boolean topPossible, botPossible, leftPossible, rightPossible;

	@Override
	public void collision(GameObject other, AABBComponent otherHitBox) {
		
		topPossible = false;
		botPossible = false;
		leftPossible = false;
		rightPossible = false;
		
		topTime = 9999;
		botTime = 9999;
		leftTime = 9999;
		rightTime = 9999;
		
		if(other.getTag() == "platform"){
			
			colliding = true;
			
			AABBComponent platformHitbox = (AABBComponent) other.findComponent("aabb");
			float dy = hitbox.getCenterY() - hitbox.getLastCenterY();
			float dx = hitbox.getCenterX() - hitbox.getLastCenterX();
			DebugPanel.message3  = "dx: " + dx;
			DebugPanel.message4  = "dy: " + dy;
			
			
			// how much the object has penetrated top, bottom, left and right into the other objects hitbox 
			topDistance = hitbox.getStopY() - platformHitbox.getStartY();
			botDistance = platformHitbox.getStopY() - hitbox.getStartY();
			leftDistance = hitbox.getStopX() - platformHitbox.getStartX();
			rightDistance = platformHitbox.getStopX() -  hitbox.getStartX();
			
			DebugPanel.message5 = "TopDistance: " + topDistance;
			DebugPanel.message6 = "BotDistance: " + botDistance;
			DebugPanel.message7 = "LeftInc: " + leftDistance;
			DebugPanel.message8 = "RightInc: " + rightDistance;
			
			
			// what side hit the hhitbox first?
			
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
					System.out.println("Top Collision");
					resolution.y = (int)(posY - topDistance);	
				}
				
				if(botPossible){
					System.out.println("Bot Collision");
					resolution.y = (int)(posY + botDistance);	
				}
				
			// no movement on Y Axis
			} else if(dy == 0){

				if(leftPossible){
					System.out.println("Left Collision");
					resolution.x = (int)(posX - leftDistance);	
				}

				if(rightPossible){
					System.out.println("Right Collision");
					resolution.x = (int)(posX + rightDistance);	
				}

			
			// Movement on both Axis
			// Moving Right and Down:  could be left or top collision
			} else if(dx > 0 && dy > 0){
				
				if(leftTime < topTime){
					// left coll
					resolution.x = (int)(posX - leftDistance);
				}else {
					// top col
					resolution.y = (int)(posY - topDistance);
				}
				
			// Moving Right and Up : could be left or bot collision
			}else if(dx > 0 && dy < 0 ){
				
				if(leftTime < botTime){
					// left coll
					resolution.x = (int)(posX - leftDistance);
				}else {
					// bot col
					resolution.y = (int)(posY + botDistance);
				}
				
			// Moving Left and Down: could be right or top collision
			}else if(dx < 0 && dy > 0){
				
				//System.out.println("Moving Left + Down. \n\tRightTime: " + rightTime + "\n\tTopTime: " + topTime);
				
				if(rightTime < topTime){
					// right coll
					resolution.x = (int)(posX + rightDistance);
				}else {
					// top col
					resolution.y = (int)(posY - topDistance);
				}
			
			// Moving Left and Up: could be right or bot collision	
			}else if(dx < 0 && dy < 0){
				
				if(rightTime < botTime){
					// right coll
					resolution.x = (int)(posX + rightDistance);
				}else {
					// bot col
					resolution.y = (int)(posY + botDistance);
				}
				
			}
			
			
			
			
		}
		
		
	}

	public float getVx() {
		return vx;
	}

	public void setVx(float vx) {
		this.vx = vx;
	}

	public float getVy() {
		return vy;
	}

	public void setVy(float vy) {
		this.vy = vy;
	}

}
