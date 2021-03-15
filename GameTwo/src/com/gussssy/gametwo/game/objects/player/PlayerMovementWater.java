package com.gussssy.gametwo.game.objects.player;

import java.awt.event.KeyEvent;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.debug.DebugPanel;

/**
 * Controls Player Movement when in water 
 **/
public class PlayerMovementWater {
	
	Player player;
	int swimSpeed = 50;
	
	int tileX, tileY;
	float offX, offY;
	int topPadding;
	int sidePadding;

	public PlayerMovementWater(Player player){
		this.player = player;
		
		topPadding = player.getTopPadding();
		sidePadding = player.getLeftRightPadding();
	}
	
	
	
	/**
	 * Moves player in response to keyboard input. Called only when the player is in water
	 **/
	public void movementUpdate(GameContainer gc, GameManager gm, float dt){
		
		tileX = player.getTileX();
		tileY = player.getTileY();
		offX = player.getOffX();
		offY = player.getOffY();
		
		DebugPanel.message8  = "FallDistance: " + player.fallDistance;
		//player.fallDistance = 0;
		
		
		//////////////////////////////////////////////////////////////////////////////////////////
		//								Move Left and Right										//

		// MOVE LEFT
		if(gc.getInput().isKey(KeyEvent.VK_A)){

			// 'A' key is currently down

			// Is there a solid tile to the left?
			if(gm.getLevelTileCollision(player.getTileX() - 1 , player.getTileY()) || gm.getLevelTileCollision(player.getTileX() - 1 , player.getTileY() + (int)Math.signum((int)player.getOffY())) ){

				// there is a solid tile to the left but the player may not yet be touching it
				
				player.setOffX(player.getOffX() - dt * swimSpeed);
				//offX -= dt * speed;
				//GameManager.debugMessage2 = "Left Checking";

				if(player.getOffX() < -player.getLeftRightPadding()) {
					// player has hit the the tile to the left and cannot move any further left
					
					//offX = -leftRightPadding;
					player.setOffX(-player.getLeftRightPadding());
					//GameManager.debugMessage2 = "Left Blocked";
				}

			} else {

				// there is nothing solid to the left, player can move freely
				player.setOffX(player.getOffX() - dt * swimSpeed);
				//GameManager.debugMessage2 = "Left Clear";
				//offX -= dt * speed;
			}
		}

		

		// MOVE RIGHT
		if(gc.getInput().isKey(KeyEvent.VK_D)){

			// 'D' key is currently down
			
			// Is there a solid tile to the right?
			if(gm.getLevelTileCollision(player.getTileX() + 1 , player.getTileY())|| gm.getLevelTileCollision(player.getTileX() + 1 , player.getTileY() + (int)Math.signum((int)player.getOffY()))){

				// there is a solid tile to the right but the player may not yet be touching it
				
				player.setOffX(player.getOffX() + (dt * swimSpeed));
				//offX += dt * speed; 
				//GameManager.debugMessage2 = "Right Checking";


				if(player.getOffX() > player.getLeftRightPadding()){ 
					// player has hit the the tile to the right and cannot move any further right
					
					player.setOffX(player.getLeftRightPadding());
					//GameManager.debugMessage2 = "Right Blocked";
				}

			}else{
				// there is nothing solid to the right, player can move freely
				player.setOffX(player.getOffX() + (dt * swimSpeed));
				//GameManager.debugMessage2 = "Right Clear";
				//offX += dt * speed;
				
			}
		}
		
		
		
		// MOVE RIGHT
		if(gc.getInput().isKey(KeyEvent.VK_D)){
			
			// D key is down
			
			// Can player swim right? 
			
		}
		
		
		
		
		
		
		
		
		
		

		

		// LEFT RIGHT MOVING ANIMATION

		// Animating Left and Right Movement
		if(gc.getInput().isKey(KeyEvent.VK_A)){
			// left walking animation 
			player.setDirection(1);
			player.setAnimationState(player.getAnimationState() + dt * player.getAnimationSpeed());
			if(player.getAnimationState() > 4)player.setAnimationState(0);
		} else if(gc.getInput().isKey(KeyEvent.VK_D)){
			// right walking animation
			player.setDirection(0);
			player.setAnimationState(player.getAnimationState() + dt * player.getAnimationSpeed());
			if(player.getAnimationState() > 4)player.setAnimationState(0);
		} else {
			// stationary 
			player.setAnimationState(0);
		}


		//								End of Move Left and Right								//
		//////////////////////////////////////////////////////////////////////////////////////////





		//////////////////////////////////////////////////////////////////////////////////////////
		// 									Jump and Gravity									//

		

		// NOTE: this should be here..? Do I want the player to sink if not doing anything..? if so then we do need this
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



	// ! ! NOPE NOT WHEN SWIMMING
		// CALCULATE EFFECT OF GRAVITY
		// fallDistane is increased by current fallSpeed * time passed since last update
		//player.fallDistance += dt * gm.getGravity();


		// SWIMMING UP

		/*
		// Swim up if pressing w
		if(gc.getInput().isKey(KeyEvent.VK_W)){
			
			// is there a solid tile above? 
			if(gm.getLevelTileCollision(tileX, tileY + -1)){
				
				// yes there is collision above, but untill head hits above tile...
				player.setOffY(player.getOffY() - dt*swimSpeed);
				
			} else {
				// nothing above, can swim up freely
				player.setOffY(player.getOffY() - dt*swimSpeed);
			}
		}*/
			
			
		
		int secondaryTileX;
			
			// Swim up if pressing w
			if(gc.getInput().isKey(KeyEvent.VK_W)){
				
				if(gm.getLevelTile(tileX, tileY-1).type != -3 && offY < player.getHeight()/2){
					//player.fallDistance = player.jump;
					player.fallDistance = -3;
					player.hasBoost = true;
					
					DebugPanel.message7 = "fs after water jump: " + player.fallDistance;
				}
			
				// is there a solid tile above?
				// are we checking the tile above, or the left above or the right above...
				
				// do we need to check the left hand above tile? 
					// if offX less than -4? (-sidePadding = -4)
				if(offX < -sidePadding){
					// we need to consider the left hand above tile too
					secondaryTileX = tileX-1;
					DebugPanel.message1 = "Above + Left";
					
				// do we need to check the right hand above tile?
				} else if(offX > sidePadding){
					secondaryTileX = tileX +1;
					DebugPanel.message1 = "Above + Right";
					
				}else{
					// Nope we are within the confines of tileX soley
					secondaryTileX = tileX;
					DebugPanel.message1 = "Above Only";
				}
				
				
				// For rendering.. 
				gm.getLevelTile(tileX, tileY-1).checked = true;
				gm.getLevelTile(secondaryTileX, tileY-1).checked = true;
				
				
				
				// redundantly checks the same tile twice when the there is only one tile above the player but it works so this is ok...? 
				if(gm.getLevelTileCollision(tileX, tileY-1) || gm.getLevelTileCollision(secondaryTileX, tileY-1)){
					
					// yes there is a collision above, but we may not have hit it yet
					player.setOffY(player.getOffY() - dt*swimSpeed);
					
					// has the collision occured yet?
					if(offY <= -topPadding){
						
						// yes is has apply correction
						// TEMPORARY FIX, CANT MOVE LEFT AND RIGHT AFTER THIS CORRECTION FOR SOME REASON SO WILL TRY MOVING DOWN AN EXTRA PIXEL - needs to be fixed properly at some stage. 3/11/20
						player.setOffY(-topPadding);
					}
					
				}else {
					// No collision with above tiles possible
					player.setOffY(player.getOffY() - dt*swimSpeed);
				}
				
				
				
			}


		// APPLY FALLING
		if(gc.getInput().isKey(KeyEvent.VK_W))offY += player.fallDistance;
		//player.setOffY(player.getOffY() + player.fallDistance);
		
		if(gc.getInput().isKey(KeyEvent.VK_S)){
			
			player.setOffY(player.getOffY() + dt * swimSpeed);
			
		}


		/*
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
		}*/

		/*
		// COLLISIONS BELOW

		// Check for collision below if the player is falling (positve value for falldistance)
		// Checks tile directly below and adjacent below tile if player is between two tiles
		if(player.fallDistance > 0){
			if((gm.getLevelTileCollision(player.getTileX(), player.getTileY() + 1) || gm.getLevelTileCollision(player.getTileX() + (int)Math.signum((int)Math.abs(player.getOffX()) > player.getLeftRightPadding() ? player.getOffX() : 0), player.getTileY() + 1)) && player.getOffY() >= 0 ){

				// Collision with ground detected

				player.fallDistance = 0;	// dont fall this frame
				player.setOffY(0);	//offY = 0;			// set the player on top of this tile
				player.onGround = true;	// player is on the ground
			} else {
				player.onGround = false;
			}
		}*/

		// animating jump / falling
		if(player.fallDistance > 1){
			player.setAnimationState(1); 
		}


		//								End of Jump and Gravity									//
		//////////////////////////////////////////////////////////////////////////////////////////


		// set the players new position
		player.setPosition();



		

	}
	
	

}
