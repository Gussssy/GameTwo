package com.gussssy.gametwo.game.objects.npc;

import java.awt.event.KeyEvent;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.engine.gfx.Image;
import com.gussssy.gametwo.engine.gfx.ImageTile;
import com.gussssy.gametwo.engine.gfx.Light;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.SoundManager;
import com.gussssy.gametwo.game.Textures;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.components.HealthBar;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.objects.projectile.Bullet;
import com.gussssy.gametwo.game.pathfinding.PathFinderTwo;
import com.gussssy.gametwo.game.weapon.Pistol;

public class BotBot extends NPC{

	private boolean followCursor = false; 
	
	GameManager gm;

	private ImageTile botbotTile = Textures.botbotTile;
	

	// Location variables
	//private int tileX, tileY;	// the x and y coords of the tile this object is in
	//private float offX, offY;	// where this object is located within the tile
	
	
	//private int health = 100;									// moved to NPC

	// Movement Variables
	//private float speed = 50;									// now declared in NPC
	//private float fallDistance = 0;							// now declared in NPC
	//private float gravity = 10;								// moved to GameManager for the time being
	//private float jump = -4;									// moved to NPC
	//private boolean onGround = false;							// moved to NPC
	//private boolean onAABB = false;							// moved to npc
	//private AABBComponent botbotOnThisPlatform = null;
	//private AABBComponent botbotOnThisPlatform = null;			//moved to NPC


	//Activity Booleans
	//private boolean movingLeft = false;	// moved to npc
	//private boolean movingRight = false;	// moved to npc
	//private boolean willJump = false;		// moved to npc
	//private boolean follow = true; 		// not used
	
	//private boolean stuckLeft = false;		// not using anymore
	//private boolean stuckRight = false;		// not using anymore
	
	// Variables for random acitivity - not currently in use
	int action;
	int actionTime = 0;
	boolean executing = false;
	
	// Path finding variables
	private Image flag = new Image("/flag.png");
	//private boolean targetSet = false; 			// moved to npc 
	//private boolean pathSet = false;				// moved to npc
	private int targetX, targetY;
	//private Path path;							// moved to npc
	//private String instruction;					// moved to npc
	
	//Activity Variables
	//NPCActionType actionType;						// moved to npc
	
	// bot bots weapon
	//private Weapon weapon; // moved to npc
	
	Light light = new Light(32, 0xffccffff);
	

	public BotBot(int tileX, int tileY, GameManager gm){

		this.gm = gm;
		this.tag = "botbot";
		
		// set location
		this.tileX = tileX;
		this.tileY = tileY;
		this.posX = tileX * GameManager.TS;
		this.posY = tileY * GameManager.TS;
		offX = 0;
		offY = 0;
		
		// padding and dimensions
		this.topPadding = 2;
		this.leftRightPadding = 3;
		this.width = 16 - 2*leftRightPadding;
		this.height = 16 - topPadding;
		
		// hitbox
		hitBox = new AABBComponent(this);
		addComponent(hitBox);

		// ? why is this here
		this.target = gm.getObject("player");
		
		//actionType = NPCActionType.IDLE;
		//actionType = NPCActionType.ATTACK;
		
		weapon = new Pistol(this);
		components.add(weapon);
		
		team = 0;
		direction = 0;
		
		
		// Pathing
		pathRange = 20; 
		
		//LevelTile targetLocation; 

	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt){
		
		
		
		//GameManager.debugMessage1 = "BotBot tileX: " + tileX;
		//GameManager.debugMessage2 = "BotBot tileY: " + tileY;
		//GameManager.debugMessage3 = "BotBot Follow: " + follow;
		//GameManager.debugMessage3 = "BotBot Moving Left: " + movingLeft;
		//GameManager.debugMessage4 = "BotBot Moving Right: " + movingRight;
		//GameManager.debugMessage3 = "BotBot OffX: " + offX;
		//GameManager.debugMessage4 = "BotBot OffY: " + offY;
		//GameManager.debugMessage6 = "BotBot ActionType: " + actionType;
		//GameManager.debugMessage7 = "BotBot Stuck Right: " + stuckRight;
		//GameManager.debugMessage9 = "BotBot onGround: " + onGround;
		
		
		//////////////////////////////////////////////////////////////////////////////////////////
		//								DECISION MAKING 										//
		
		//get a location to move too
			// see if this location is reachable
				// start moving to location
		
		
		switch(actionType){
		case IDLE:
			idlePathing(gm);
			break;
		case PATH: 
			path(gc, gm);
			break;
		case ATTACK:
			
			aggroIdle(gm);
			
		case FOLLOW:
			break;
		case WAIT:
			break;
		default:
			break;
		}
		
		
		// TEMPORARY
		if(gc.getInput().isKeyDown(KeyEvent.VK_X)){
			System.out.println("CollisionDetectionObject: Toggling Following Mouse");
			if(followCursor)followCursor = false;
			else followCursor = true;
		}
		
		
		if(followCursor){
			
			posX = gc.getInput().getMouseX() + gc.getRenderer().getCamX();
			posY = gc.getInput().getMouseY() + gc.getRenderer().getCamY();
			
			tileX = (int)posX/16;
			tileY = (int)posY/16;
			
			offX = posX%16;
			offY = posY%16;
			
		}else {
			npcMovement(gc, gm, dt);
		}
		
		
		// npcMovement normally here
		npcUpdate(gc, gm, dt);
		
		//
		npcWalkingAnimation(dt);
		
		
		// updateComponents
			// health bar
			// AABBComponent (rect hitbox)
		//this.updateComponents(gc, gm, dt);

	}


	public void setPosition(){

		// If botbot is more than half into neighbouring below tile....
		if(offY > GameManager.TS / 2){
			tileY++; //increment to the new tile
			offY -= GameManager.TS;
		}

		// If botbot is more than half into neighbouring above tile....
		if(offY < -GameManager.TS / 2){
			tileY--; //decrement to the new tile
			offY += GameManager.TS;
		}

		// If botbot is more than half into neighbouring right tile....
		if(offX > GameManager.TS / 2){
			tileX++; //increment to the new tile
			offX -= GameManager.TS;
		}

		// If the botbot is more than half into neighbouring left tile...
		if(offX < -GameManager.TS / 2){
			tileX--; //decrement to the new tile
			offX += GameManager.TS;
		}

		// Update BotBot to new position
		posX = tileX * GameManager.TS + offX;
		posY = tileY * GameManager.TS + offY;
	}
	
	boolean pathFindingInProgress = false;
	
	
	/**
	 * Path to a manually set target tile.
	 * 
	 *  This is old code used for initial path executing. 
	 **/
	private void path(GameContainer gc, GameManager gm){

		// If a target is set, set a path if possible
		if(targetTileSet){

			// if a path is not set, set it. 
			if(!pathSet){

				//System.out.println("target set but no path, calling path");

				// Generate a path if possible
				//path = PathFinder.findPath(gm, this, gm.getLevelTile(targetX/GameManager.TS, targetY/GameManager.TS));
				//pathFindingInProgress = true;

				if(pathFindingInProgress == false){

					// NOPT BELOW LINE: have moved all pathfinding debug to a new class PathFindingTester
					//gm.setFlagLocation(targetX*16, targetY*16);
					pathFindingInProgress = true;
					PathFinderTwo.initPathFinding(gm, tileX, tileY, targetX, targetY);
					return;
				}else{

					if(gc.getInput().isKeyDown(KeyEvent.VK_6)){
						PathFinderTwo.expandLeastCostNode(gm);

					}else {
						return;
					}
				}

				if(pathFindingInProgress)return;



				//gm.setFlagLocation(targetX*16, targetY*16);
				//path = PathFinderTwo.findPath(gm, this, tileX, tileY, targetX, targetY);

				// Was a path found? 
				if(path != null){

					// yes, a path was found
					pathSet = true;
					System.out.println("Found a path");
				}else{

					// no, target location was unreachable
					targetTileSet = false;
					System.out.println("Cannot find a path to this target location");

					// Cancel any movement if a target is set but there is no path
					// there may be a more logical place to put this
					movingLeft = false; 
					movingRight = false; 

					// This seems logical to remove the target if it is not reachable
					targetTileSet = false; 
				}	
			}	
		}


		// Execute the path if it is set
		if(pathSet){

			// get the next instruction by updating path
			//instruction = path.update(); 
			//System.out.println("Instruction Recieved: " + instruction);

			// New A* path execution

			pathInstruction = path.executePath();

			// cancel any old movement commands
			movingLeft = false;
			movingRight = false;
			willJump = false;

			// Execute the instruction
			// in some cases this can be called when instruction is null. Neeed to sort this soon. 25/08
			switch(pathInstruction){
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
				System.out.println("Movement was stopped in stop case");
				targetTileSet = false; 
				pathSet = false;
				movingLeft = false;
				movingRight = false;
				break;
			}

		}
	}

	
	@Override
	public void render(Renderer r){

		// Not animated - old. 
		//r.drawImage(botbotImage, (int)posX, (int)posY);
		
		// Animated - new and will likely keep it this way. Animation may be a bit much.
		r.drawImageTile(botbotTile, (int)posX, (int)posY, (int)animationState,  direction);
		
		// Draw a light around botbot. 
		r.drawLight(light, (int)posX+14, (int)posY+14);
		
		// Render a flag representing botbots target location. IF a target is set. 
		if(targetTileSet){
			r.drawImage(flag, targetX, targetY);
		}
		
		
		// Render Components
		this.renderComponents(r);
		
		// Render Debug info relevant to this botbot
		if(GameManager.showDebug){
			renderPath(r);
			vision.renderVision(r);
			
		}

	}

	
	
	
	
	@Override
	public void collision(GameObject other, AABBComponent otherHitBox){
		
		// Platform Collision
		if(other.getTag() == "platform"){
			
			collisionHandler.collisionWithStationaryObject(other, otherHitBox);		
		}
		
		// BotBot collides with various projectiles as well but that is managed by the Projectile classes. 
		 
	

	}

	
	
	/**
	 * Sets a target location for botbot to path too.
	 * <p>
	 * This is old code but may be used again in the future. Keeping for now.
	 *  
	 */
	public void setTargetLocation(int targetX, int targetY){
		this.targetX = targetX;
		this.targetY = targetY;
		targetTileSet = true;
		pathSet = false;
		actionType = NPCActionType.PATH;	
	}

	
	/**
	 * Old code, will be reworked soon. 7/3/21 
	 */
	@Override
	protected void attack(GameManager gm, int direction) {
		weapon.useWeapon(gm, direction);
		
	}


}
