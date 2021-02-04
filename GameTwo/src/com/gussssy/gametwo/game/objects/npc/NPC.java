package com.gussssy.gametwo.game.objects.npc;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.SoundManager;
import com.gussssy.gametwo.game.objects.Charachter;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.objects.ObjectType;
import com.gussssy.gametwo.game.pathfinding.Path;
import com.gussssy.gametwo.game.pathfinding.PathFinder;
import com.gussssy.gametwo.game.pathfinding.PathFinderTwo;
import com.gussssy.gametwo.game.weapon.Weapon;

public abstract class NPC extends Charachter {

	

	// Movement Variables - all in charachter now
	//protected boolean onGround;
	//protected float fallDistance = 0;
	//protected float jump = -4;
	//protected float speed = 60; // this is also defined in charachter... 
	//protected boolean onAABB = false;
	//protected AABBComponent onThisObject = null;

	// Path Finding Variables
	protected boolean targetObjectSet;
	protected boolean pathSet = false;
	protected boolean targetTileSet = false;	// for when pathing to a specific tile. npcs use this when idling
	protected int targetTileX, targetTileY;
	protected Path path;						// the path the npc is currently following
	protected String instruction;				// the current instruction given by a path that is being read
	protected int counter = 0;					// counter controlling...?
	
	// NEW EXPERICMNENTAL 21/1/21
	protected int waitTime = 0;
	protected boolean waiting = false;
	protected int maxWaitTime = 300;
	protected int minWaitTime = 100;

	// Path Following Booleans
	protected boolean movingLeft = false;
	protected boolean movingRight = false;
	protected boolean willJump = false;
	

	// The Npcs weapon, if it uses one.  
	protected Weapon weapon;
	protected int range = 5;

	// Activity Variables 
	public NPCActionType actionType = NPCActionType.ATTACK;
	protected boolean taskCompleted = false; 

	// Awareness
	protected ArrayList<GameObject> awareOf = new ArrayList<GameObject>();
	protected ArrayList<GameObject> noticedEnemies = new ArrayList<GameObject>();				// NEW UNUSED, UNTESTED, DELETE IF STILL HERE LATER
	int xDiff, yDiff;
	//protected int team; //moved to charchater to include player
	protected boolean enemyFound = false;
	protected int aggroTimer = 0;
	protected GameObject target;
	int attackRange = 10;
	int detectionRange = 30;	// was 15
	
	// max distance the npc will select to path to
	int pathRange = 50;


	// variables used to select random tile locations to path to when idling
	int tryX = 0;	// randomly generated target tileX value, randomly assigned in idle()
	int tryY = 0;	// randomly generated target tileY value, randomly assigned in idle()
	int attempts = 0; // the numer of times the NPC has attemped to find a random accessible location to path too


	// Abstract methods 
	protected abstract void attack(GameManager gm, int direction);


	public NPC(){
		objectType = ObjectType.NPC;
		
		// Setting movement variables for all npcs atm: 
		speed = 60;
		jump = -4;
		
	}


	/**
	 *  General Update for all NPCs that will take care of actions that occur on all NPCs each frame such as updating components and (maybe) rendering 
	 **/
	protected void npcUpdate(GameContainer gc, GameManager gm, float dt){

		
		// 13/1/21 intend to move this to healthbar so health is only checked when damage is taken
		if(health <= 0){
			//dead = true;
			//SoundManager.dead.stop();
			//SoundManager.dead.play();
		}

		// updateComponents
		updateComponents(gc, gm, dt);

		//updateAwareness
		//updateAwareness(gm);

	}



	protected void npcMovement(GameContainer gc, GameManager gm, float dt){
		//////////////////////////////////////////////////////////////////////////////////////////
		//								Move Left and Right										//

		// move left
		if(gc.getInput().isKey(KeyEvent.VK_J) || movingLeft){
			
			// SET DIRECTION
			direction = 0;

			if(gm.getLevelTileCollision(tileX - 1 , tileY) || gm.getLevelTileCollision(tileX - 1 , tileY + (int)Math.signum((int)offY)) ){

				// there is a solid block to the left

				// keep moving bot bot as collision may not have actually occured yet
				offX -= dt * speed;

				//Cannot get past solid tile on left hand side withput jumping
				//stuckLeft = true; //not using this anymore

				if(offX < -leftRightPadding) {

					// bot bot cannot move any further, he is now touching the left tile
					offX = -leftRightPadding;
				}
			} else {
				// no collision with left neighbouring tile 

				// bot bot is moving left
				offX -= dt * speed;
				//stuckLeft = false;	// cant be stuck if there is nothing solid to the left
			}
		}

		// move rigt
		if(gc.getInput().isKey(KeyEvent.VK_L) || movingRight){
			
			// SET DIRECTION
			direction = 1;

			if(gm.getLevelTileCollision(tileX + 1 , tileY)|| gm.getLevelTileCollision(tileX + 1 , tileY + (int)Math.signum((int)offY))){

				// there is a solid tile to the right
				offX += dt * speed;

				// bot bot wont be able to move the the next tile without jumping
				//stuckRight = true;

				// If bot bot has moved into the tile: 
				if(offX > leftRightPadding){ 

					//botbot has moved to far right and is inside the right neighbouring tile,
					offX = leftRightPadding; // postions bot bot touching the right neighbouring tile
				}
			}else{

				// there is nothing solid to the right
				offX += dt * speed;
				//stuckRight = false;
			}
		}

		//////////////////////////////////////////////////////////////////////////////////////////
		// 									Jump and Gravity									//



		// Check if the player is on standing on AABB
		if(onAABB){

			// TODO:(26/8) pls comment this condition
			if(posX > (onThisObject.getParent().getPosX() - width - leftRightPadding) && posX < onThisObject.getParent().getPosX() + onThisObject.getParent().getWidth() - leftRightPadding){
				onGround = true;

				// BotBot is within x region, but is botbot y still on top of the platform?
				if((onThisObject.getParent().getPosY() - posY) > height + topPadding + 1){

					// BotBot is no longer ontop of the platform as it has moved down faster then the BotBot falls
					onGround = false;
					onAABB = false;
					onThisObject = null;
				} 

			} else {

				// BotBot is not within x range to be on the platform
				onAABB = false;
				onThisObject = null;
				onGround = false;
			}			
		}



		// Calculate the effect of gravity. Wont be applied till later
		fallDistance += dt * gm.getGravity();

		// JUMP only if on the ground
		if((gc.getInput().isKeyDown(KeyEvent.VK_I) || willJump) && onGround){
			//System.out.println("\n\n\nBotBot jumps");
			willJump = false;
			fallDistance = jump;
			onGround = false;

			// currently muting this to avoid cacoffany
			//SoundManager.jump.play();
		}



		//Apply gravity/falling to botbot. Will always apply a little bit, but wont effect position untill sufficiently large
		offY += fallDistance;


		// Check for collisions above
		// if the absolute value off offX is greater then the padding, then get the signum of offX else get the signum of 0
		if(fallDistance < 0){
			if((gm.getLevelTileCollision(tileX, tileY - 1) || gm.getLevelTileCollision(tileX + (int)Math.signum((int)Math.abs(offX) > leftRightPadding ? offX : 0), tileY - 1)) && offY < -topPadding ){
				// Collision with above tile detected
				//System.out.println("Collision with above level tile, fall distance set to 0\n\n");
				fallDistance = 0;
				offY = -topPadding;	
				//System.out.println("Set botbot offY to:" + offY + " at collisions above");
			}
		}

		// Check for collision below 
		// Checks tile directly below and adjacent below tile if player is between two tiles
		if(fallDistance > 0){
			if((gm.getLevelTileCollision(tileX, tileY + 1) || gm.getLevelTileCollision(tileX + (int)Math.signum((int)Math.abs(offX) > leftRightPadding ? offX : 0), tileY + 1)) && offY >= 0 ){
				fallDistance = 0;
				//System.out.println("Set BotBot offY to 0");
				offY = 0;
				onGround = true;
			}else{
				onGround = false;
			}
		}


		//////////////////////////////////////////////////////////////////////////////////////////
		//							Tile Position Update										//

		// If botbot is more than half into neighbouring below tile....
		if(offY > GameManager.TS / 2){
			tileY++; //increment to the new tile
			offY -= GameManager.TS;
			//System.out.println("BotBot moved to lower tile, tileY: " + tileY);
		}

		// If botbot is more than half into neighbouring above tile....
		if(offY < -GameManager.TS / 2){
			tileY--; //decrement to the new tile
			offY += GameManager.TS;
			//System.out.println("BotBot moved to upper tile, tileY: " + tileY);
		}

		// If botbot is more than half into neighbouring right tile....
		if(offX > GameManager.TS / 2){
			tileX++; //increment to the new tile
			offX -= GameManager.TS;
			//System.out.println("BotBot moved to next right tile, tileX: " + tileX);
		}

		// If the botbot is more than half into neighbouring left tile...
		if(offX < -GameManager.TS / 2){
			tileX--; //decrement to the new tile
			offX += GameManager.TS;
			//System.out.println("BotBot moved to next left tile, tileX: " + tileX);
		}

		// Update botbot to new position
		posX = tileX * GameManager.TS + offX;
		posY = tileY * GameManager.TS + offY;


		//							End of Tile Position Update								//
		//////////////////////////////////////////////////////////////////////////////////////
	}
	
	
	/**
	 * Animates walking for NPCs that have a walking animation.
	 * 
	 *  dt: this time passed since last update
	 **/
	public void npcWalkingAnimation(float dt){
		
		//System.out.println("npcWalkingAnimation: npc type: " + tag + ", max frames: " + frames );
		
		if(movingLeft){
			direction = 0;
			animationState += animationSpeed * dt;
			
			if(animationState >= frames){
				animationState = 0;
			}
			
		} else if(movingRight){
			direction = 1;
			animationState += animationSpeed * dt;
			
			if(animationState >= frames){
				animationState = 0;
			}
			
		}else{
			// npc is not moving, assume idle frame
			animationState = 0;
		}
		
	}


	protected void updateAwareness(GameManager gm){

		//reset awareness list
		awareOf.clear();

		// set enemy found to false, if an enemy is found this update, this will be set to true
		enemyFound = false;

		// loop through game objects, if tag is player or npc, check if within awareness radius
		for(GameObject o : gm.getObjects()){

			// NPC detection
			if(o.getObjectType() == ObjectType.NPC){
				// found an NPC

				// is NPC on other team? 
				if(((NPC) o).getTeam() != team){
					// found npc is on the other team
					
					// is the enemy npc in range to be detected?
					if(inRange(o.getTileX(), o.getTileY(), detectionRange,detectionRange)){
						// enemy npc is within range
						//System.out.println(tag + " Found enemy");
						awareOf.add(o);
						target = o;
						enemyFound = true;	
					}	
				}
			}


			// player detection
			if(o.getTag() == "player"){
				// found player
				
				// is the player in within the detection radius?
				if(inRange(o.getTileX(), o.getTileY(), detectionRange, detectionRange)){
					// player is close enough to npc, npc is aware of player
					awareOf.add(o);	
				}
			}
		}
		
		// finished looping through gameobjects
		if(enemyFound == false){
			// if no enemies were found, set target to null
			target = null;
			targetObjectSet = false;
		}

	}

	/**
	 * Paths to the target GameObject and attacks it if in range.
	 * 
	 * This is an old method and is not currently used.
	 **/
	protected void pathToTargetObjectAndAttack(GameManager gm){

		// set target object
		if(!targetObjectSet){
			target = gm.getObject("player");
			targetObjectSet = true;
		}


		// are we in range now to try and attack ? 
		if(Math.abs(target.getTileX() - tileX) < range && Math.abs(target.getTileY() - tileY) < range){
			//npc is in range to attack
			attack(gm,0);
		}


		// if path is not set, find a path
		if(!pathSet){

			// target is location is not set, set it
			path = PathFinder.findPath(gm, this, gm.getLevelTile(target.getTileX(), target.getTileY()));

			if(path != null){
				pathSet = true;
				counter = 120;
			}else{
				System.out.println("Couldnt find a path to the target");
				movingLeft = false; 
				movingRight = false; 
			}


		}else{
			// path set, time to execute path

			//also start counting
			if(counter == 0){
				pathSet = false;
				return;
			}


			// decrement counter so npc wont stay on this path forever when the player is moving
			counter--;

			// get the next instruction by updating path
			instruction = path.update(); 

			// cancel any old movement commands
			movingLeft = false;
			movingRight = false;
			willJump = false;

			// Execute the instruction
			// in some cases this can be called when instruction is null. Neeed to sort this soon. 25/08
			switch(instruction){
			case "up":
				willJump = true;
				break;
			case "down":
				/// hmmmm what goes here
				break;
			case "left":
				movingLeft = true;
				break;
			case "stopLeft":
				movingLeft = false;
				break;
			case "stopRight":
				movingRight = false;
				break;
			case "right":
				movingRight = true;
				break;
			case "wait":
				movingRight = false;
				movingLeft = false;
				break;
			case "recalculate": 
				pathSet = false;
				break;
			case "stop": 
				//System.out.println("Movement was stopped in stop case");
				targetTileSet = false; 
				pathSet = false;
				movingLeft = false;
				movingRight = false;
				break;
			}
		}
	}


	// these variables were used to test if the random location selection is biased towards left or right.
	double totalXD;
	double totalXY;
	double i = 0;

	/**
	 * makes the NPC idle around, choses a random closeby location to path too 
	 **/
	protected void idle(GameManager gm){

		/*
		System.out.println("Starting Idle");
		System.out.println("TileX: " + tileX + ", TileY: " + tileY);
		System.out.println("Target Tile: tileX= " + tryX + ", tileY= " + tryY);
		System.out.println("TagrgetTileSet: " + targetTileSet);
		System.out.println("PathSet: " + pathSet);
		 */

		// testing...
		if(taskCompleted){
			//System.out.println("NPC has completed task, needs a new one");
			//targetTileSet = false;
			//taskCompleted = false;
			
			if(waitTime > 0){
				// keep waiting
				waitTime--;
				return;
			}else{
				taskCompleted = false;
				waiting = false;
			}
			
		}


		// TARGET SETTING
		// has a target location been set?
		if(!targetTileSet){
			// no it hasnt need to set it

			// Dont try set a path while the npc is in the air, this can cause issues when npc falls out of the path and goes completely off the rails
			if(!onGround){
				//npc is airbourne, cant set a path while airbourne
				return;
			}

			// find a close by tile to move too

			// define search area for a place to move too
			int startX = tileX - pathRange;
			int stopX = tileX + pathRange;
			int startY = tileY - pathRange;
			int stopY = tileY + pathRange;
			
			// TESTING TO REMOVE SELECTION BIAS TO THE LEFT
			//stopX +=1;

			// make sure we only access tiles that exist: 
			if(startX < 0)startX = 0;
			if(startY < 0)startY = 0;
			// what about exceeding level bounds?
			if(stopX > gm.getLevelWidth())stopX = gm.getLevelWidth();
			if(stopY > gm.getLevelHeight())stopY = gm.getLevelHeight();

			// keep track of how many times the npc has tried to find a tile, after say 100 times, just give up
			attempts = 0;

			// now find somewhere
			while(!targetTileSet){

				if(attempts > 100){
					//System.out.println("Unable to find a tile to go to after 100 attempts");
				//	System.out.println("tileX: " + tileX + ", tiley" + tileY);
					//actionType = NPCActionType.WAIT;
					//attempts = 0;
					//SoundManager.ohbum.play();
					//dead = true;
					return;
				}

				// increment attempts
				attempts++;


				// WHY IS THIS SO BIASED TP THE LEFT
				tryX = ThreadLocalRandom.current().nextInt(startX, stopX); 
				tryY = ThreadLocalRandom.current().nextInt(startY, stopY);


				//System.out.println("Looking for a new location to move too");
				//System.out.println("Try to access tile: x = " + tryX + ", y = " + tryY);


				// Deal with an issue when npc trys to path to its location resulting in a path with 0 steps
				if(tryX == tileX && tryY == tileY){
					//System.out.println("target location is same as current location");
					continue;
				}



				// if the randomly choosen tile is accessible and the tile below is solid, then npc will path to it
				if(gm.getLevelTile(tryX, tryY).accessible == true && gm.getLevelTileCollision(tryX, tryY+1)){
				//	System.out.println(" BINGO Found a tile to path too ");
					targetTileSet = true;
					attempts = 0;

					// tryinhg to find out why the npcs generally end up on the left, must because of issues with path execution
					// the reults from this found that the random selection did not result in bias towards left
					/**
					double dX = tileX - tryX;
					double dY = tileY - tryY;
					i++;

					totalXD += dX;
					totalXY += dY;
					System.out.println("average dX: " + (totalXD/i));
					System.out.println("average dY: " + (totalXY/i));
					 */

					//gm.addObject(new Donut(tryX, tryY));

				}
			}

		}

		// PATH SETTING and EXECUTING
		if(!pathSet){
			//path = PathFinder.findPath(gm, this, gm.getLevelTile(tryX, tryY));
			//System.out.println("Trying to find a path too: tileX = " + tryX + ", tileY = " + tryY);
			//gm.setGooseLocation(tryX, tryY);
			path = PathFinderTwo.findPath(gm, this, tileX, tileY, tryX, tryY);

			if(path != null){
				// we good
				//System.out.println(" BINGO Found a a path :D ");
				pathSet = true;
			}else {
				//System.out.println("!!!!!!! CANT PATH TO AN ACCESSIBLE LOCATION !!!!!! \n something is very wrong\n targetTile is no longer set to avoid a bad situation");
				targetTileSet = false;
				//System.out.println("Setting tile to inaccessible, sorry for the inconvenience. TileX = " + tryX + ", TileY = " + tryY);
				
				// turn off tile?????
				//gm.getLevelTile(tryX, tryY).accessible = false;
			}
		}else{
			// now pathing stuff
			pathToTargetLocation(path);
		}

		/**
		System.out.println("End Idle");
		System.out.println("Target Tile: tileX= " + tryX + ", tileY= " + tryY);
		 */

	}

	/**
	 * Executes pathing.  
	 **/
	private void pathToTargetLocation(Path path){

		if(!pathSet){
			System.out.println("Tried to path but path isnt set");
			return;
		}

		// get the next instruction by updating path
		//instruction = path.update(); 
		instruction = path.executePath();

		// cancel any old movement commands
		movingLeft = false;
		movingRight = false;
		willJump = false;

		// Execute the instruction
		// in some cases this can be called when instruction is null. Neeed to sort this soon. 25/08
		switch(instruction){
		case "up":
			willJump = true;
			break;
		case "down":
			// should the npc wait while going down?
			break;
		case "left":
			movingLeft = true;
			break;
		case "stopLeft":
			movingLeft = false;
			break;
		case "stopRight":
			movingRight = false;
			break;
		case "right":
			movingRight = true;
			break;
		case "wait":
			movingRight = false;
			movingLeft = false;
			break;
		case "recalculate": 
			pathSet = false;
			break;
		case "stop": 
			//System.out.println("Movement was stopped in stop case");
			targetTileSet = false; 
			pathSet = false;
			movingLeft = false;
			movingRight = false;
			
			// Waiting after completing a path
			taskCompleted = true;
			waiting = true;
			waitTime = ThreadLocalRandom.current().nextInt(minWaitTime, maxWaitTime);
			break;

		}

	}


	/**
	 * Trys to randomly set a target location to an accessible tile that the npc will eventually path too.
	 * biasX and biasY can be used to shift the area in which a target location will be searched for.
	 **/
	protected boolean trySetTargetLocationRandom(GameManager gm, int biasX, int biasY){

		// define search area for a place to move too
		int startX = tileX - 5 + biasX;
		int stopX = tileX + 5 - biasX;
		int startY = tileY -5 + biasY;
		int stopY = tileY + 5 - biasY;

		// make sure we only access tiles that exist: 
		if(startX < 0)startX = 0;
		if(startY < 0)startY = 0;
		// what about exceeding level bounds?
		if(stopX > gm.getLevelWidth())stopX = gm.getLevelWidth();
		if(stopY > gm.getLevelHeight())stopY = gm.getLevelHeight();

		// keep track of how many times the npc has tried to find a tile, after say 100 times, just give up
		attempts = 0;

		boolean locationFound = false;

		while(!locationFound){

			if(attempts > 100){
				System.out.println("Unable to find a tile to go to after 100 attempts");
				System.out.println("tileX: " + tileX + ", tiley" + tileY);
				actionType = NPCActionType.WAIT;
				//attempts = 0;
				SoundManager.ohbum.play();
				return false;
			}

			// increment attempts
			attempts++;

			// WHY IS THIS SO BIASED TP THE LEFT
			tryX = ThreadLocalRandom.current().nextInt(startX, stopX);
			tryY = ThreadLocalRandom.current().nextInt(startY, stopY);


			// Deal with an issue when npc trys to path to its location resulting in a path with 0 steps
			if(tryX == tileX && tryY == tileY){
				//System.out.println("target location is same as current location");
				//continue;
			}

			// if the randomly choosen tile is accessible and the tile below is solid, then npc will path to it
			if(gm.getLevelTile(tryX, tryY).accessible == true && gm.getLevelTileCollision(tryX, tryY+1)){
				//System.out.println(" BINGO Found a tile to path too ");
				targetTileSet = true;
				//return true;
				attempts = 0;
				targetTileX = tryX;
				targetTileY = tryY;
				locationFound = true;

			}
		}

		return false;
	}

	
	
	
	/**
	 * Controls the behaviour of the NPC when in NPCAction.ATTACK mode
	 * 
	 * Implements normal idle behaviour untill an enemy enters the NPCs detection radius/range
	 * 
	 * Once an enemy has been deteced the npc will try to path within attack range. 
	 * 
	 * Once in attack range, the npc will attack in the direction of the enemy npc as long as the enemmy npc is on the same TileY
	 **/
	protected void aggroIdle(GameManager gm){
		
		// aggroTimer is used to reset behaviour after certain intervals, when aggro timer reaches 0. Decremented every update.
		aggroTimer--;

		// are we aware of an enemy
		if(!enemyFound){
			
			// updates thye NPCs aware every 10 updates wheil no enemy has been found
			if(aggroTimer <= 0){
				updateAwareness(gm);
				aggroTimer = 10;
			}
			
			// execute idle behaviour while no enemy has been found.
			idle(gm);
		
		}else{
			// An enemy has been found. 
			// if this enemy is setill within the detection range, path towards it. 
			
			// is the enemy still in range?
			if(inRange(target.getTileX(), target.getTileY(), detectionRange, detectionRange)){
				
				// enemy NPC is still within the detection range
				
				// Path towards the enemy NPC
				
				// Set a path if there is none, or make a new path if it has been awhile since the path was set, the enemy could have moved somwehere else
				if(pathSet == false || aggroTimer <= 0){
					
					// try find a path
						//path = PathFinder.findPath(gm, this, gm.getLevelTile(target.getTileX(), target.getTileY()));
					path = PathFinderTwo.findPath(gm, this, tileX, tileY, target.getTileY(), target.getTileY());
					
					// did we find a path? 
					if(path == null){
						
						// a path wasnt found,  wont try find a path again for 20 updates to avoid looping 
						pathSet = false;
						enemyFound = false;
						aggroTimer = 20;
					}else {
						// a path has been found
						pathSet = true;
						
						// wait 120 updates before restting the path
						aggroTimer = 120;
					}
						
					
				}else{
					// we have a path to the target, execute pathing
					
					// if npc has moved enough towards target attack
																			// 1 for last param, set yLimit for inRange to 1 so that an enemy wont be fired at unless on the same tileY..  
																			//should be 0 but 0 does not work, they dont fire at all.
					if(inRange(target.getTileX(), target.getTileY(), attackRange, 1)){
						
						// stop moving and pathing
						pathSet = false;
						movingLeft = false;
						movingRight = false;
						
						// wait 10 updates before ressetting... for what reason though..? this will put npc bck to idle and awareness wont reset for another 10 updates...
						aggroTimer = 10;
						
						// FIRE pistol / ATTACK
						
						
						
						// determine the direction to fire in
						int fireDirection;
						if(tileX - target.getTileX() < 0){
							// fire to right
							fireDirection = 0;
						}else {
							fireDirection = 1;
						}
						
						// fire weapon
						attack(gm, fireDirection);
						
						
						
						
						
						
						
						
						
						// so the NPC wont sit here and keep attacking (it looks stupid and not what I want)
						enemyFound = false;
						return;
					}
					
					// npc is not close enough to attack, keep pathing
					pathToTargetLocation(path);
					
				}
				
			}else{
				// Enemy was found but is no longer withing detection range, return to idle behaviour
				enemyFound = false;
				idle(gm);
			}
		}
	}
	
	public boolean inRange(int targetTileX, int targetTileY, int xLimit, int yLimit){
		int xDiff = Math.abs(tileX - targetTileX);
		int yDiff = Math.abs(tileY - targetTileY);
		
		// think it should be <= but can't think right now
		if(xDiff < xLimit && yDiff < yLimit){
			return true;
		}else{
			return false;
		}
		
	}

	

	public boolean isOnGround() {
		return onGround;
	}

	public float getFallDistance() {
		return fallDistance;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}




	public int getTeam() {
		return team;
	}

}
