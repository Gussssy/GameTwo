package com.gussssy.gametwo.game.objects.player;

import java.awt.event.KeyEvent;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.SoundManager;

public class PlayerMovementBasic {

	Player player;

	public PlayerMovementBasic(Player player){
		this.player = player;

	}

	
	/**
	 * Moves player in response to keyboard input. 
	 **/
	public void movementUpdate(GameContainer gc, GameManager gm, float dt){
		
		
		//////////////////////////////////////////////////////////////////////////////////////////
		//								Move Left and Right										//

		// MOVE LEFT
		if(gc.getInput().isKey(KeyEvent.VK_A)){

			// 'A' key is currently down

			// Is there a solid tile to the left?
			if(gm.getLevelTileCollision(player.getTileX() - 1 , player.getTileY()) || gm.getLevelTileCollision(player.getTileX() - 1 , player.getTileY() + (int)Math.signum((int)player.getOffY())) ){

				// there is a solid tile to the left but the player may not yet be touching it


				
				
				player.setOffX(player.getOffX() - dt * player.getSpeed());
				//offX -= dt * speed;

				if(player.getOffX() < -player.getLeftRightPadding()) {
					// player has hit the the tile to the left and cannot move any further left
					
					//offX = -leftRightPadding;
					player.setOffX(-player.getLeftRightPadding());
				}

			} else {

				// there is nothing solid to the left, player can move freely
				player.setOffX(player.getOffX() - dt * player.getSpeed());
				//offX -= dt * speed;
			}
		}

		

		// MOVE RIGHT
		if(gc.getInput().isKey(KeyEvent.VK_D)){

			// 'D' key is currently down
			

			// Is there a solid tile to the right?
			if(gm.getLevelTileCollision(player.getTileX() + 1 , player.getTileY())|| gm.getLevelTileCollision(player.getTileX() + 1 , player.getTileY() + (int)Math.signum((int)player.getOffY()))){

				// there is a solid tile to the right but the player may not yet be touching it
				
				player.setOffX(player.getOffX() + (dt * player.getSpeed()));
				//offX += dt * speed; 


				if(player.getOffX() > player.getLeftRightPadding()){ 
					// player has hit the the tile to the right and cannot move any further right
					
					//offX = leftRightPadding;
					player.setOffX(player.getLeftRightPadding());
			
				}


			}else{
				// there is nothing solid to the right, player can move freely
				player.setOffX(player.getOffX() + (dt * player.getSpeed()));
				//offX += dt * speed;
				
			}
		}

		

		// LEFT RIGHT MOVING ANIMATION

		// Animating Left and Right Movement
		if(gc.getInput().isKey(KeyEvent.VK_A)){
			// left walking animation 
			player.setDirection(1);
			player.animationState += dt * player.animationSpeed;
			if(player.animationState > 4)player.animationState = 0;
		} else if(gc.getInput().isKey(KeyEvent.VK_D)){
			// right walking animation
			player.setDirection(0);
			player.animationState += dt * player.animationSpeed;
			if(player.animationState > 4)player.animationState = 0;
		} else {
			// stationary 
			player.animationState = 0;
		}


		//								End of Move Left and Right								//
		//////////////////////////////////////////////////////////////////////////////////////////





		//////////////////////////////////////////////////////////////////////////////////////////
		// 									Jump and Gravity									//

		/*
		if(counter > framesPerPrint && debugGravity){

			System.out.println("\n DEBUG GROUND COLLISION\noffX: " + offX + "\noffY: " + offY + "\npadding: " + leftRightPadding);
			System.out.println("Player is on TileX: " + player.getTileX() + ", TileY: " + player.getTileY());
			int inputToSignum = Math.abs((int)offX) > leftRightPadding ? (int)offX : 0;
			System.out.println("input to signum: " + inputToSignum);
			int signumResult = (int)Math.signum(inputToSignum);
			System.out.println("Signum result: " + signumResult);
			System.out.println("Check1: Checking collision tile directly below with player.getTileX(): " + player.getTileX() + ", and tileY: " + (player.getTileY() + 1));
			System.out.println("Check2: Also Checking tileX: " + (player.getTileX() + signumResult) + ", and tileY: " + (player.getTileY() + 1));
		}
		*/


		// Check if the player is standing on an AABB of a platform
		if(player.isOnAABB()){

			// Last frame the player was on the platform but is that still the case? 
			if(player.getPosX() > (player.onThisObject.getParent().getPosX() - player.getWidth() - player.getLeftRightPadding()) && player.getOffX() < player.onThisObject.getParent().getPosX() + player.onThisObject.getParent().getWidth() - player.getLeftRightPadding()){

				// Player still within the x range of the platforms surface
				player.setOnGround(true);
				player.hasBoost = false;

				// BUT is the player's y location still on top of the platform?
				if((player.onThisObject.getParent().getPosY() - player.getPosY()) > player.getHeight() + player.getTopPadding() + 1){

					// Player is no longer ontop of the platform as it has moved down faster then the player falls
					player.onGround = false;
					player.onAABB = false;
					player.onThisObject = null;
				} 

				// Player is still on the platform. Nothing needs to be changed. 

			} else {

				// Player is not within x range to be on the platform
				player.onAABB = false;
				player.onThisObject = null;
				player.onGround = false;
			}			
		}




		// CALCULATE EFFECT OF GRAVITY

		// fallDistane is increased by current fallSpeed * time passed since last update
		player.fallDistance += dt * gm.getGravity();




		// JUMPING

		// Double jump / boost 
		if(gc.getInput().isKeyDown(KeyEvent.VK_W) && player.hasBoost){
			player.fallDistance += player.boostSpeed;
			player.hasBoost = false;
			SoundManager.jetpack.play();
		}


		// Jump only if on the ground
		if(gc.getInput().isKeyDown(KeyEvent.VK_W) && player.onGround){
			player.fallDistance = player.getJump();
			player.hasBoost = true;
			player.onGround = false;
			player.onAABB = false;
			SoundManager.jump.play();
		}


		// APPLY FALLING
		//offY += player.fallDistance;
		player.setOffY(player.getOffY() + player.fallDistance);



		// COLLISIONS ABOVE

		// Check for collisions above only if the player isnt falling down currently (negative value for falldistance)
		//		- positive values for falldistance mean the player is moving up therefore we need to check if there is something above
		if(player.fallDistance < 0){

			// if the absolute value off offX is greater then the padding, then get the signum of offX else get the signum of 0
			if((gm.getLevelTileCollision(player.getTileX(), player.getTileY() - 1) || gm.getLevelTileCollision(player.getTileX() + (int)Math.signum((int)Math.abs(player.getOffX()) > player.getLeftRightPadding() ? player.getOffX() : 0), player.getTileY() - 1)) && player.getOffY() < -player.getTopPadding() ){
				// Collision with above tile detected
				player.fallDistance = 0;
				//offY = -topPadding;	
				player.setOffY(-player.getTopPadding());
			}
		}


		// COLLISIONS BELOW

		// Check for collision below if the player is falling (positve value for falldistance)
		// Checks tile directly below and adjacent below tile if player is between two tiles
		if(player.fallDistance > 0){
			if((gm.getLevelTileCollision(player.getTileX(), player.getTileY() + 1) || gm.getLevelTileCollision(player.getTileX() + (int)Math.signum((int)Math.abs(player.getOffX()) > player.getLeftRightPadding() ? player.getOffX() : 0), player.getTileY() + 1)) && player.getOffY() >= 0 ){

				// Collision with ground detected

				player.fallDistance = 0;	// dont fall this frame
				player.setOffY(0);	//offY = 0;			// set the player on top of this tile
				player.onGround = true;	// player is on the ground
				player.hasBoost = false;	//player cant use boost when on ground, only when airbourne
				//jumping = false;	
			} else {
				player.onGround = false;
			}
		}

		// animating jump / falling
		if(player.fallDistance > 1){
			player.animationState = 1; 
		}


		//								End of Jump and Gravity									//
		//////////////////////////////////////////////////////////////////////////////////////////


		// set the players new position
		player.setPosition();



		/*
		// debug info printing to console
		if(counter > framesPerPrint && debugPlayerMovement){
			System.out.println("\n DEBUG PLAYER MOVEMENT");
			System.out.println("onGround: " + onGround);
			//System.out.println("collidingWithAABB: " + collidingWithAABB);
			System.out.println("fallDistance: " + fallDistance);
			System.out.println("************************************\n\n");
		}
		*/

	}
	
}
