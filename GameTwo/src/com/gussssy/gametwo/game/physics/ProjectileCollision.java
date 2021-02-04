package com.gussssy.gametwo.game.physics;

import com.gussssy.gametwo.game.SoundManager;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.debug.DebugPanel;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.objects.projectile.Projectile;

public class ProjectileCollision {

	private boolean print = false;
	
	
	Projectile projectile;
	AABBComponent hitBox;


	// the distance the object has penetrated the hitbox
	int topDistance, botDistance, leftDistance, rightDistance;

	// the 'time' taken to acheive this distance if only velocity on the respective axis is considered
	float topTime, botTime, leftTime, rightTime;

	// whether or not a collision on the respective side is possible
	boolean topPossible, botPossible, leftPossible, rightPossible;





	/**
	 * ProjectileCollision constructor
	 **/
	public ProjectileCollision(Projectile projectile){

		this.projectile = projectile;

	}

	/**
	 * Process Collision with a stationary object 
	 **/
	public void collisionWithStationary(GameObject other, AABBComponent otherHitBox){

		topPossible = false;
		botPossible = false;
		leftPossible = false;
		rightPossible = false;

		topTime = 9999;
		botTime = 9999;
		leftTime = 9999;
		rightTime = 9999;

		// does this need to be done every time? 
		hitBox = (AABBComponent) projectile.findComponent("aabb");

		
		// this only works for stationary object collisions


		if(print)System.out.println("AABB Collison with Stationary Object: " + other.getTag());

		// Determine dy/dx using hitbox last (This is prone to errors as lastCenters are integers so dy/dx will be whole numbers)
		// Testing a different method below (it worked for tile collision)
		//float dy = hitBox.getCenterY() - hitBox.getLastCenterY();
		//float dx = hitBox.getCenterX() - hitBox.getLastCenterX();
		//GameManager.debugMessage3  = "dx: " + dx;
		//GameManager.debugMessage4  = "dy: " + dy;
		
		float dx = projectile.getVx();
		float dy = projectile.getVy();
		
		if(print)System.out.println("Projectile Collison stationary object processing:\n\t dx: " + dx + "\n\tdy: " + dy );


		// how much the object has penetrated top, bottom, left and right into the other objects hitbox 
		topDistance = hitBox.getStopY() - otherHitBox.getStartY();
		botDistance = otherHitBox.getStopY() - hitBox.getStartY();
		leftDistance = hitBox.getStopX() - otherHitBox.getStartX();
		rightDistance = otherHitBox.getStopX() -  hitBox.getStartX();

		//GameManager.debugMessage5 = "TopDistance: " + topDistance;
		//GameManager.debugMessage6 = "BotDistance: " + botDistance;
		//GameManager.debugMessage7 = "LeftInc: " + leftDistance;
		//GameManager.debugMessage8 = "RightInc: " + rightDistance;


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
				// Top Collision
				projectile.setPosY(projectile.getPosY() - topDistance);
				projectile.invertVY();
				if(print){
					System.out.println("Top Collision: no movement on x axis");
					System.out.println("Vy inverted. vy:" + projectile.getVy());
				}
				
			}

			if(botPossible){
				// Bot Collision
				if(print)System.out.println("Bot Collision");	
				//posY += botDistance;
				projectile.setPosY(projectile.getPosY() + botDistance);
				//vy = -vy;
				projectile.invertVY();
				if(print)System.out.println("Vy inverted. vy:" + projectile.getVy());
			}

			// no movement on Y Axis
		} else if(dy == 0){

			if(leftPossible){
				// Left Collision
				if(print)System.out.println("Left Collision");	
				//posX -= leftDistance;
				projectile.setPosX(projectile.getPosX() - leftDistance);
				projectile.invertVX();
			}

			if(rightPossible){
				// Right Collision
				if(print)System.out.println("Right Collision");
				//posX += rightDistance;
				projectile.setPosX(projectile.getPosX() + rightDistance);
				projectile.invertVX();
			}


			// Movement on both Axis
			// Moving Right and Down:  could be left or top collision
		} else if(dx > 0 && dy > 0){

			if(leftTime < topTime){
				// Left Collision
				//posX -= leftDistance;
				projectile.setPosX(projectile.getPosX() - leftDistance);
				projectile.invertVX();
			}else {
				// Top Collision
				//posY -= topDistance;
				projectile.setPosY(projectile.getPosY() - topDistance);
				//vy = -vy;
				projectile.invertVY();
			}

			// Moving Right and Up : could be left or bot collision
		}else if(dx > 0 && dy < 0 ){

			if(leftTime < botTime){
				// Left Collision
				// left coll
				//posX -= leftDistance;
				projectile.setPosX(projectile.getPosX() - leftDistance);
				projectile.invertVX();
			}else {
				// bot col
				// Bot Collision
				//posY += botDistance;
				projectile.setPosY(projectile.getPosY() + botDistance);
				projectile.invertVY();
			}

			// Moving Left and Down: could be right or top collision
		}else if(dx < 0 && dy > 0){

			if(rightTime < topTime){
				// Right Collision
				// right coll
				//posX += rightDistance;
				projectile.setPosX(projectile.getPosX() + rightDistance);
				projectile.invertVX();
			}else {
				// Top Collision
				// top col;
				//posY -= topDistance;
				projectile.setPosY(projectile.getPosY() - topDistance);
				projectile.invertVY();
			}


			// Moving Left and Up: could be right or bot collision	
		}else if(dx < 0 && dy < 0){

			if(rightTime < botTime){
				// Right Collision
				// right coll
				//posX += rightDistance;
				projectile.setPosX(projectile.getPosX() + rightDistance);
				projectile.invertVX();
			}else {
				// bot col
				// Bot Collision
				//posY += botDistance;
				projectile.setPosY(projectile.getPosY() + botDistance);
				projectile.invertVY();
			}

		}

		// UPDATE THE AABB



	}


	public void collisionWithTileHitBox(AABBComponent tileHitBox){

		topPossible = false;
		botPossible = false;
		leftPossible = false;
		rightPossible = false;

		topTime = 9999;
		botTime = 9999;
		leftTime = 9999;
		rightTime = 9999;

		// does this need to be done every time? 
		hitBox = (AABBComponent) projectile.findComponent("aabb");
		
		
		// Avoid processing collisions with temporary tilehit boxes belonging to a different projectile
		if(projectile.getTileCollider() != tileHitBox.getParent()){
			if(print)System.out.println("XXXXX Tried to collide a projectile with a temporary hitbox made by a different projectile XXXXXX");
			//SoundManager.error1.play();
			return;
		}
		
		

		


		if(print)System.out.println("Projectile AABB Collison with tile");

		// get dy/dx by looking at hitbox last position: prone to errors because this will always give whole numbers, 
		//float dy = hitBox.getCenterY() - hitBox.getLastCenterY();
		//float dx = hitBox.getCenterX() - hitBox.getLastCenterX();
		
		// instead get dx by looking at the projectiles velocity which will not be a whole number 99.999% of the time
		float dx = projectile.getVx();
		float dy = projectile.getVy();
		DebugPanel.message1  = "dx: " + dx;
		DebugPanel.message2  = "dy: " + dy;


		// how much the object has penetrated top, bottom, left and right into the other objects hitbox 
		topDistance = hitBox.getStopY() - tileHitBox.getStartY();
		botDistance = tileHitBox.getStopY() - hitBox.getStartY();
		leftDistance = hitBox.getStopX() - tileHitBox.getStartX();
		rightDistance = tileHitBox.getStopX() -  hitBox.getStartX();

		DebugPanel.message3 = "TopDistance: " + topDistance;
		DebugPanel.message4 = "BotDistance: " + botDistance;
		DebugPanel.message5 = "LeftInc: " + leftDistance;
		DebugPanel.message6 = "RightInc: " + rightDistance;
		
		
		DebugPanel.message11 = "proj old vx: " + projectile.getVx();
		DebugPanel.message12 = "proj old vy: " + projectile.getVy();
		
		//GameManager.debugMessage3 = "proj old vx: " + projectile.getVx();
		//GameManager.debugMessage4 = "proj old vy: " + projectile.getVy();
		//GameManager.debugMessage5 = "- - - - - - - - - - - - - - - - - - ";


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
		
		DebugPanel.message7 = "TopTime: " + topTime;
		DebugPanel.message8 = "BotTime: " + botTime;
		DebugPanel.message9 = "LeftTime: " + leftTime;
		DebugPanel.message10 = "RightTime: " + rightTime;
		


		// PROCESSING

		// Single Axis Collision Processing
		// no movement on X Axis 
		if(dx == 0){

			if(topPossible){
				// Top Collision
				if(print)System.out.println("Top Collision");
				projectile.setPosY(projectile.getPosY() - topDistance);
				projectile.invertVY();
				projectile.setVy(projectile.getVy() * projectile.getElasticity());
			}

			if(botPossible){
				// Bot Collision
				if(print)System.out.println("Bot Collision");	
				projectile.setPosY(projectile.getPosY() + botDistance);
				projectile.invertVY();
				projectile.setVy(projectile.getVy() * projectile.getElasticity());
			}

			// no movement on Y Axis
		} else if(dy == 0){

			if(leftPossible){
				// Left Collision
				if(print)System.out.println("Left Collision");	
				projectile.setPosX(projectile.getPosX() - leftDistance);
				projectile.invertVX();
				projectile.setVx(projectile.getVx() * projectile.getElasticity());
			}

			if(rightPossible){
				// Right Collision
				if(print)System.out.println("Right Collision");
				projectile.setPosX(projectile.getPosX() + rightDistance);
				projectile.invertVX();
				projectile.setVx(projectile.getVx() * projectile.getElasticity());
			}


			// Movement on both Axis
			// Moving Right and Down:  could be left or top collision
		} else if(dx > 0 && dy > 0){

			if(leftTime < topTime){
				// Left Collision
				if(print)System.out.println("Left Collision");
				projectile.setPosX(projectile.getPosX() - leftDistance);
				projectile.invertVX();
				projectile.setVx(projectile.getVx() * projectile.getElasticity());
			}else {
				// Top Collision
				if(print)System.out.println("Top Collision");
				projectile.setPosY(projectile.getPosY() - topDistance);
				projectile.invertVY();
				projectile.setVy(projectile.getVy() * projectile.getElasticity());
			}

			// Moving Right and Up : could be left or bot collision
		}else if(dx > 0 && dy < 0 ){

			if(leftTime < botTime){
				// Left Collision
				if(print)System.out.println("Left Collision");
				projectile.setPosX(projectile.getPosX() - leftDistance);
				projectile.invertVX();
				projectile.setVx(projectile.getVx() * projectile.getElasticity());
			}else {
				// Bot Collision
				if(print)System.out.println("Bot Collision");
				projectile.setPosY(projectile.getPosY() + botDistance);
				projectile.invertVY();
				projectile.setVy(projectile.getVy() * projectile.getElasticity());
			}

			// Moving Left and Down: could be right or top collision
		}else if(dx < 0 && dy > 0){

			if(rightTime < topTime){
				// Right Collision
				if(print)System.out.println("Right Collision");
				projectile.setPosX(projectile.getPosX() + rightDistance);
				projectile.invertVX();
				projectile.setVx(projectile.getVx() * projectile.getElasticity());
			}else {
				// Top Collision
				//posY -= topDistance;
				if(print)System.out.println("Top Collision");
				projectile.setPosY(projectile.getPosY() - topDistance);
				projectile.invertVY();
				projectile.setVy(projectile.getVy() * projectile.getElasticity());
			}


			// Moving Left and Up: could be right or bot collision	
		}else if(dx < 0 && dy < 0){

			if(rightTime < botTime){
				// Right Collision
				if(print)System.out.println("Right Collision");
				projectile.setPosX(projectile.getPosX() + rightDistance);
				projectile.invertVX();
				projectile.setVx(projectile.getVx() * projectile.getElasticity());
			}else {
				// Bot Collision
				if(print)System.out.println("Bot Collision");
				projectile.setPosY(projectile.getPosY() + botDistance);
				projectile.invertVY();
				projectile.setVy(projectile.getVy() * projectile.getElasticity());
			}

		}else {
			System.out.println("Projectile Collision.collisionWithtile(): Not an actual Collision");
			SoundManager.error2.play();
		}

		// UPDATE THE AABB
		//hitBox.updateAfterCollision();
		
		//GameManager.debugMessage6 = "proj new vx: " + projectile.getVx();
		//GameManager.debugMessage7 = "proj new vy: " + projectile.getVy();
		
		




	}

}
