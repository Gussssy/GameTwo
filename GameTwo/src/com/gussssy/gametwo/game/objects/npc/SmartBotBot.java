package com.gussssy.gametwo.game.objects.npc;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.engine.gfx.Image;
import com.gussssy.gametwo.engine.gfx.Light;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.SoundManager;
import com.gussssy.gametwo.game.Textures;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.components.HealthBar;
import com.gussssy.gametwo.game.debug.DebugPanel;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.objects.projectile.Bullet;
import com.gussssy.gametwo.game.weapon.AimedPistol;

/**
 * A NPC BotBot that uses new AI. Using a seperate NPC for this as to not ruin the old AI while creating new AI 
 **/
public class SmartBotBot extends NPC{

	GameManager gm;

	// smart botbots texture
	private Image botbotImage = Textures.smartBotBot;



	// Path finding variables
	private Image flag = new Image("/flag.png");
	private int targetX, targetY;

	// Only SmartBotBot currently uses the NPC vision class
	NPCVision vision; 														// class that looks for GameObjects in vision of this NPC
	private int visionCounter = ThreadLocalRandom.current().nextInt(0,6);		// timer controlling when this NPCs updates its vision
	private int visionUpdateInterval = 20;										// how many frames between awareness updates, awarenessCounter takes this value after awareness is updated


	// SmartBotBots green light that surrounds it. Only visible at night/when dark so maybe this should always be on...
	Light light = new Light(32, 0xff00ff00);
	
	
	
	// booleans controlling noises made when an enemy is found
	private boolean alerted = false;
	private boolean makeSoundWhenAlerted = false;


	/** 
	 *  SmartBotBot Constructor
	 *  
	 *  @param tileX x location of the tile smartbotbot will spawn on
	 *  @param tileY y location of the tile smartbotbot will spawn on
	 *  @param gm GameManager
	 **/
	public SmartBotBot(int tileX, int tileY, GameManager gm){

		//System.out.println("Making a new SmartBotBot");

		this.gm = gm;
		this.tag = "smartbotbot";
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
		
		this.addComponent(new AABBComponent(this));

		//actionType = NPCActionType.IDLE;
		//actionType = NPCActionType.ATTACK;

		//weapon = new Pistol(this);
		weapon = new AimedPistol(this);
		components.add(weapon);

		// the 'good' team 
		team = 0;
		
		// initial direction
		direction = 0;

		vision = new NPCVision(this);




	}

	
	
	
	/**
	 * SmartBotBot Update.	
	 */
	@Override
	public void update(GameContainer gc, GameManager gm, float dt){

		/*	if(awareOf.size() != 0){
			GameManager.debugMessage9 = "BotBot awareOf " + awareOf.get(0).getTag();
		}else {
			GameManager.debugMessage9 = "BotBot awareOf nothing";
		}*/



		//////////////////////////////////////////////////////////////////////////////////////////
		//								DECISION MAKING 										//


		// Update vision/awareness every 5 frames
		if(visionCounter <= 0){
			vision.updateVision(gm);
			visionCounter = visionUpdateInterval;
			
			
			
			// Making Sounds when a new enemy is found
			
			// if there wasnt an enemy in vision previously and an enemy has just been found, make a sound
			if(vision.isEnemyInVision()){
				
				if(alerted == false){
					alerted = true;
					if(makeSoundWhenAlerted)SoundManager.alertSounds.playRandom();
				}
			} else {
				// no enemies in vision
				alerted = false;
			}
			
			
			
		}else{
			visionCounter--;
		}



		switch(actionType){
		case IDLE:
			// idle around looking for tiles to path to
			idlePathing(gm);
			break;
		case PATH: 
			// only bot bot has any use for this atm
			break;
		case ATTACK:
			// agressive behaviouir
			agressiveBehaviour();
			//aggroIdle(gm);
			break;
		case FOLLOW:
			break;
		case WAIT:
			// do nothing this frame
			break;
		default:
			break;
		}







		// execute any movement
		// 		movement commands set by 
		npcMovement(gc, gm, dt);

		// general npc update. will update components
		npcUpdate(gc, gm, dt);


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


	}


	Optional<GameObject> optionalTarget;
	GameObject targetEnemy;
	boolean targetEnemySet = false;

	/**
	 * The starting point of SmartBotBots agressive behaviour 
	 * 
	 * 
	 * Right Now: very basic, just a starting point
	 * 		- if awareness was juts updated, looks to see if an enemy was found. 
	 * 			- if enemy was found, fire at it
	 * 			- if not continue idle behaviour
	 * 
	 **/
	public void agressiveBehaviour(){
		
		
		// NOTE: maybe rather then only trying to shoot when an enemy was just located, maybe try and shoot again if that particular enemy is still in vision. 
		//			- to do this I sould need to cast a shadow ray to this single enemy via NPCAwareness.
		//			- right now this isnt possible but if I break up castShawdowRays so that  s single shadow ray is cast for s single game object with a seperate method call... 
		//				that would simplify things and give better functionality
		//					- would allow future implementation of pursue
		
	
		// LOOK FOR A TARGET IF VISION/AWARENESS JUST UPDATED
		// if awareness has just been updated, then look to see if there is an enemy to target.
		if(visionCounter == visionUpdateInterval){
			
			// awareness has just been updated
			optionalTarget = vision.getTargetEnemy();
			
			// was an enemy found? 
			if(optionalTarget.isPresent()){
				
				// WOULD BE GOOD IDEA TO STOP PATHING FOR A BIT, AND WHEN RESUMING PATHING, MAYBE RETREAT IF LOW, ADVANCE IF HIGH HEALTH AND NOTHING TO SHOOT
				// feel like we shoul be modifying the path the npc is following, dont just keep running forward
				
				// yes enemy was found
				targetEnemy = optionalTarget.get();
				targetEnemySet = true;
				
				// fire a bullet at the target
				weapon.fireAtTarget(gm, targetEnemy);
				
			}
				
				
				
		}else {
			// there is no target currently and awareness hasnt updated this frame continue idle
			idlePathing(gm);
		}

	}







	@Override
	public void render(Renderer r){


		r.drawImage(botbotImage, (int)posX, (int)posY);
		r.drawLight(light, (int)posX+14, (int)posY+14);

		if(targetTileSet){
			r.drawImage(flag, targetX, targetY);
		}

		this.renderComponents(r);

		// testing new awareness
		vision.renderVision(r);

	}


	
	
	
	
	
	
	int[] incursions = new int[4];
	int smallestIndex;
	int botIncursion, topIncursion, leftIncursion, rightIncursion;


	@Override
	public void collision(GameObject other, AABBComponent otherHitBox){




		if(other.getTag().equals("platform")){
			AABBComponent thisAABB = (AABBComponent) this.findComponent("aabb");
			AABBComponent otherAABB = (AABBComponent) other.findComponent("aabb");

			// how much the object has penetrated top, bottom, left and right
			topIncursion = thisAABB.getStopY() - otherAABB.getStartY();
			botIncursion = otherAABB.getStopY() - thisAABB.getStartY();
			leftIncursion = thisAABB.getStopX() - otherAABB.getStartX();
			rightIncursion = otherAABB.getStopX() -  thisAABB.getStartX();

			DebugPanel.message1 = "Not Colliding";
			DebugPanel.message2 = "Top Incursion: " + Integer.toString(topIncursion);
			DebugPanel.message3 = "Bottom Incursion: " + Integer.toString(botIncursion);
			DebugPanel.message4 = "Left Incursion: " + Integer.toString(leftIncursion);
			DebugPanel.message5 = "Right Incursion: " + Integer.toString(rightIncursion);

			incursions[0] = topIncursion;
			incursions[1] = botIncursion;
			incursions[2] = leftIncursion;
			incursions[3] = rightIncursion;


			smallestIndex = 0;
			//System.out.println("");
			for(int i = 1; i < incursions.length; i++){
				//System.out.println("Comparing current smallest: " + incursions[smallestIndex] + " with: " + incursions[i]);
				if(incursions[i] < incursions[smallestIndex]){
					smallestIndex = i;
					//System.out.println("Smaller, new smallest: " + incursions[i]);

				}else {
					//System.out.println("not smaller, current smallest: " + incursions[smallestIndex]);
				}

			}

			// Top Collision
			if(smallestIndex == 0){
				//System.out.println(" Top Collision");
				DebugPanel.message1 = "Top Collision";
				offY -= topIncursion;
				fallDistance = 0;
				onAABB = true;
				//onGround = true;
				onThisObject = otherAABB;
				setPosition();

			} else if(smallestIndex == 1){
				//System.out.println(" Bottom Collision");
				DebugPanel.message1 = "Bottom Collision";
				offY += botIncursion;
				fallDistance = 0;
				setPosition();

			} else if(smallestIndex == 2){
				//System.out.println(" Left Collision");
				DebugPanel.message1 = "Left Collision";
				offX -= leftIncursion;
				//stuckLeft = true;
				//stuckRight = false;
				//setPosition();

			} else {
				//System.out.println(" Right Collision");
				DebugPanel.message1 = "Right Collision";
				offX += rightIncursion;
				//stuckRight = true;
				//stuckLeft = false; 
				//setPosition();

			}
		}



		if(other.getTag().equals("bullet")){


			if(((Bullet)other).getWeapon().getWielder().getTag() != tag){
				// bullet was fired by npc of a different type
			}else{

				// dw just friendly fire, ignore this bullet
				return; 
			}

			HealthBar hp = (HealthBar)this.findComponent("hp");
			System.out.println("healthPixels before" + hp.getHealthPixels());


			System.out.println("Bullet Hit Bot Bot");
			System.out.println("Was the bullet dead? " + other.isDead());
			//hurt.play();
			System.out.println();
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
