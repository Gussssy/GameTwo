package com.gussssy.gametwo.game.objects.npc;

import java.util.Optional;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.engine.gfx.ImageTile;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.SoundManager;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.spell.IceShardSpell;

public class IceWizard extends NPC {
	
	// The IceWizards image
	private ImageTile iceWizardImage = new ImageTile("/character/ice_wizard_1.png", 16, 20);
	
	// The wizards image is 20 pixels high. The game is desinged around 16*16 objects. This is a work around for this problem.
	private int imageOffY = 4;
	
	//private Spell2 spell = new Spell2(this);
	private IceShardSpell spell = new IceShardSpell(this);
	
	
	
	
	// variables controlling greeting of the player
	
	// testing npc greetings with the ice wizard
	private boolean recentlyGreetedPlayer = false;
	private int playerGreetingTimer = 0;
	private int greetingMinimumInterval = 1800;
	
	// enables wizards greeting the player. It can be a bit much. 
	private boolean greetPlayer = false;

	
	
	public IceWizard(int tileX, int tileY){
		
		System.out.println("IceWizard Constructor");
		
		this.tag = "iceWizard";
		this.tileX = tileX;
		this.tileY = tileY;
		posX = tileX * GameManager.TS;
		posY = tileY * GameManager.TS;
		offX = 0;
		offY = 0;
		
		/*this.width = 16;
		this.height = 20;*/
		
		// taken from botbot constructor
		this.topPadding = 2;
		this.leftRightPadding = 3;
		this.width = 16 - 2*leftRightPadding;
		this.height = 16 - topPadding;
		
		team = 0;
		
		
		// hitbox
		hitBox = new AABBComponent(this);
		addComponent(hitBox);
		
		// health bar
		healthBar.setHealthBarOffY(-2);
		
		// spell 
		addComponent(spell);
		
		// deathSound
		deathSound = SoundManager.wizardAhhShot;
		
	}
	
	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		
		// Greeting the player: this is triggered by collision but a if a greeting occures or not is controlled here. 
		
		if(recentlyGreetedPlayer){
			
			// If the timer has reached 0, another greeting can occur when the player next collides with the ice wizard
			if(playerGreetingTimer == 0){
				
				recentlyGreetedPlayer = false;
				playerGreetingTimer = greetingMinimumInterval;
			
			}else{
				playerGreetingTimer--;
			}
		}
		
		// -------------------------------------------------------------------------------------------------------------
		
		
		
		
		// Vision / Awareness
		// ...
		
		
		// Decsion Making
		switch(actionType){
		case IDLE:
			
			// Basic Idle pathing, no agression. NPC chooses random locations to path too. When a path has been completed, the NPC will wait for a randopm amopunt of time.
			idlePathing(gm);
			break;
		case PATH: 
			break;
		case ATTACK:
			
			// controls pathing and movement
			// aggro idle uses the idel behaviour untill an enemy is found, the n it uses very old code resulting in rather stupid behaviour.
			//aggroIdle(gm);
			rangedAgressivePathing(gm);
			//aggresiveIdleMovement(gm);
			// wizardAgressivePathing(gm);
			
			// Updates the NPCs attack ability.
				// This could do nothing if there is not target
				// or this could result in the cast of a spell,
				// or initiation of the next phase of a spell cast, 
				// or nothing if between phases waiting for the next vision update
				// or it could end a spell cast
			//wizardAttack(gm);
			
			break;
		case FOLLOW:
			break;
		case WAIT:
			break;
		default:
			break;
		}
		
		
		
		
		// The below operations occur regardless of the current NPCState
		
		// Movement
		// actually applies any 
		npcMovement(gc, gm, dt);
		npcWalkingAnimation(dt);
		
		// General NPC update
		npcUpdate(gc, gm, dt); // This is where the spell is updated.
		
		// cast spell if the npc is waiting
		if(waiting){
			
			//spell.castSpell(gc);
			
		}

	}
	
	// testing
	Optional<GameObject> possibleEnemy;
	private boolean casting = false;

	
/**
 * NOT USED, this ended up all being done inside of IceShard spell because it just made sense. 
 * To do that within this method would be really complex, for little organisational benefi.. at least I think...
 * How an NPC uses IceShardSpell is contained within ICeShard spell. 
 * Early NPC use of the pistol was done within the NPC class and a command was sent to fire a bullet in a direction when the NOC had positoned itself in the right place. 
 */	
private void wizardAttack(GameManager gm){
		
		// all this could be done inside IceShardSpell.npcUpdate() ? 
		
		// is the wizard behaving aggressively? 
		
		// if( not currentlyCasting)
		// need to find a target before casting
		
		if(!casting){
			
			// The Wizard is currently not casting. 
			// 			It will keep looking if a target has been found each frame. (This only needs to be done after vision has updated, once every 20 frames)
			//				one way to know if vision has been updated is if the visionCounter == visionUpdateInterval
			//				this could be significantly more efficient because selectTargetEnemy creates an optional object and then unwraps it, 
			//				whereas I can avoid this by simply comparing to integer values. 
			
			// If vision was just updated: 
			if(visionCounter == visionUpdateInterval){
				
				// Check if the last vision update found anything.
				possibleEnemy = vision.getTargetEnemy();
				
				if(possibleEnemy.isPresent()){
					target = possibleEnemy.get();
					casting = true;
					// CAST THE SPELL
				}
				
			}
			
			
		}
		
		
		
		
		
	}
	

// If an enemy is closer then 10 tiles away, the enemy is considered to close.
private int safeRange = 10;

/**
 * When there is no enemy, idels around until an enemy is found
 * 
 * When an enemy is found, stops movement. Or path away from enemy is too close.
 */
private void rangedAgressivePathing(GameManager gm){
	
	// idle if there no enemy has been found.
	if(!enemyFound){
		
		// no enemy atm
		
		// Chekc to see if an enemy has been found
		// this check only occurs after a vision update, so once every 20 frames
		if(visionUpdatedThisFrame){
			possibleEnemy = vision.getTargetEnemy();
			
			if(possibleEnemy.isPresent()){
				
				// an enemy has been found
				enemyFound = true;
				target = possibleEnemy.get();
				
			}
		}else {
			idlePathing(gm);
		}
		
		// 
	} else {
		
		// yes there is an enemy, at least there was !
		
		// check to make sure the enemy is still around
		// This check only occurs after a vision update
		if(visionUpdatedThisFrame){
			possibleEnemy = vision.getTargetEnemy();
			
			if(possibleEnemy.isPresent()){
				
				// yes there is still an enemy present.
				enemyFound = true;
				target = possibleEnemy.get();
				
			}else {
				
				// An enemy is no longer in site, return to idle
				enemyFound = false;
				return;
			}
		}
		
		
		// Either an enemy is in site, or last vision update an enemy was in site.
		
		/** There are three options... or maybe  2
		 * 
		 * 1. Stop Moving. Cancel the current path. Or dont execute the path by NOT calling pathToTargetLocation
		 * 2. The Enemy is too close, move away from the enemy
		 * 3. Move towards enemy...? really..
		 */
		
		double distanceToEnemy;
		
		//distanceToEnemy = 
		
		if(getDistanceToObject(target) < safeRange){
			
			// target is to close
			// select a new path
			pathInstruction = "wait";

			// cancel any old movement commands
			movingLeft = false;
			movingRight = false;
			willJump = false;
			
		}else {
			pathInstruction = "wait";
			

			// cancel any old movement commands
			movingLeft = false;
			movingRight = false;
			willJump = false;
		}
		
		
		
		
		
		
		
	}
	
	
	
}

public double getDistanceToObject(GameObject object){
	
	 return Math.sqrt(Math.pow(object.getPosX()-posX,2) + Math.pow(object.getPosY()-posY, 2));
	
	
	
}

	
	
	@Override
	public void render(Renderer r) {
		
		r.drawImageTile(iceWizardImage, (int)posX, (int)posY-imageOffY, (int)animationState, direction);
		
		renderComponents(r);
		
		if(GameManager.showDebug){
			vision.renderVision(r);
			
			renderPath(r);
			
			// Draws a Red Rectangle over the targeted enemy NPC
			if(enemyFound)r.drawRect((int)target.getPosX(), (int)target.getPosY(), target.getWidth(), target.getHeight(), 0xffff0000);
		}

	}

	@Override
	public void collision(GameObject other, AABBComponent otherHitBox) {
		
		
		if(other.getTag() == "player"){
			
			if(!recentlyGreetedPlayer){
				
				if(greetPlayer)SoundManager.wizardGreetings.playRandom();
				recentlyGreetedPlayer = true;
			}
			
			
		}

	}

	@Override
	protected void attack(GameManager gm, int direction) {
		// TODO Auto-generated method stub
		
	}

}
