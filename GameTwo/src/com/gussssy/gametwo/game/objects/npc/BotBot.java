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

	//private Image botbotImage = new Image("/BotBot1.png");
	//private Image botbotImage = new Image("/donut.png");
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

		//super(tileX, tileY, tileX * GameManager.TS, tileY * GameManager.TS, 16, 16, "botbot");
		this.gm = gm;
		this.tag = "botbot";
		this.tileX = tileX;
		this.tileY = tileY;
		this.posX = tileX * GameManager.TS;
		this.posY = tileY * GameManager.TS;
		offX = 0;
		offY = 0;
		
		this.topPadding = 2;
		this.leftRightPadding = 3;
		this.width = 16 - 2*leftRightPadding;
		this.height = 16 - topPadding;
		
		hitBox = new AABBComponent(this);
		addComponent(hitBox);

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
		
		//updateNPC(gc,gm,dt);
		
		//System.out.println("botbot health: " + health);
		
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
		
	/*	if(awareOf.size() != 0){
			GameManager.debugMessage9 = "BotBot awareOf " + awareOf.get(0).getTag();
		}else {
			GameManager.debugMessage9 = "BotBot awareOf nothing";
		}*/
		
		
		
		
		//////////////////////////////////////////////////////////////////////////////////////////
		//								DECISION MAKING 										//
		
		//get a location to move too
			// see if this location is reachable
				// start moving to location
		
		
		switch(actionType){
		case IDLE:
			idle(gm);
			break;
		case PATH: 
			path(gc, gm);
			break;
		case ATTACK:
			
			aggroIdle(gm);
			
			// feel like attack could be a whole class of its own, I do not like that I have multiple processes using the same variables so i have to remeber to reset them
					// howevere thesde processes have so many similarities, they are all Actions/Processes that require path selection, path finding, timing/counting loop repitions to avoid freezing, 
			//pathToTargetObjectAndAttack(gm);
			// aggro idle untill find someone
			// find someone ... if(awaness)
			//  follow(GameObject) if (!inrange), else follow(GameObject enemy) //follow has 2 second timer, but what is we loose site mid pursuit, I think pusiot shopuld stop
					// npc in range
					//   if(tileY == enemyTileY)
								// face towards enemy. 
								// stop movement
								// fire (while above conditions are met)
			
			
			// Where Im at, at the moment
				// aggoIdle will choose locations, .... 
			
			
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
	public void render(GameContainer gc, Renderer r){

		
		// Not animated
		//r.drawImage(botbotImage, (int)posX, (int)posY);
		
		// Animated
		r.drawImageTile(botbotTile, (int)posX, (int)posY, (int)animationState,  direction);
		
		r.drawLight(light, (int)posX+14, (int)posY+14);
		
		if(targetTileSet){
			r.drawImage(flag, targetX, targetY);
		}
		
		this.renderComponents(gc, r);

	}

	
	int[] incursions = new int[4];
	int smallestIndex;
	int botIncursion, topIncursion, leftIncursion, rightIncursion;
	
	
	@Override
	public void collision(GameObject other, AABBComponent otherHitBox){
		
		
		if(other.getTag() == "platform"){
			
			collisionHandler.collisionWithStationaryObject(other, otherHitBox);
			
		}
		
		
		
		
		/**
		 * Bullet Collison 
		 **/
		if(other.getTag().equals("bullet")){


			if(((Bullet)other).getWeapon().getWielder().getTag() != tag){
				// bullet was fired by npc of a different type
			}else{

				// dw just friendly fire, ignore this bullet
				return; 
			}

			// I really hate how this work, finding components in this way seems very silly
			// i almost think they way im using compnents is mostly redundant, only hit boxes need to be updated each update, the rest can wait wait till envoked....

			HealthBar hp = (HealthBar)this.findComponent("hp");
			//System.out.println("healthPixels before" + hp.getHealthPixels());


			//System.out.println("Bullet Hit Bot Bot");
			//System.out.println("Was the bullet dead? " + other.isDead());
			//hurt.play();
			//System.out.println();
			hp.healthChanged(-10);

			if(health < 0){
				SoundManager.dead.play();
				hp.healthChanged(100);
			}

		}

	}

	
	
	
	public void setTargetLocation(int targetX, int targetY){
		this.targetX = targetX;
		this.targetY = targetY;
		targetTileSet = true;
		pathSet = false;
		actionType = NPCActionType.PATH;	
	}

	@Override
	protected void attack(GameManager gm, int direction) {
		weapon.useWeapon(gm, direction);
		
	}


}
