package com.gussssy.gametwo.game.spell;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.engine.audio.SoundClipSet;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.SoundManager;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.objects.npc.NPC;
import com.gussssy.gametwo.game.objects.projectile.IceShard;
import com.gussssy.gametwo.game.objects.tempobjects.TempRectangle;

/**
 * An offensive spell used to damage enemy characters. 
 * <p>
 * This spell summons IceShards that are released and then recalled, damaging enemy units they pass through.
 * <p>
 * This spell has several phases:
 * <p>
 * Charging Phase: 
 * When cast, the spell will charge for a period of time. During charging, at regular intervals an IceShard will spawn above the casting character.
 * For the player, right click will start charging of the spell.For the player, right click will start charging of the spell.
 * Once spawned, IceShards move with circular motion above the caster until released. 
 * The number of IceShards will initially be three but more experienced characters will be able to spawn more in the future.
 * <p>
 * Waiting to Release Phase:
 * The spell has finished charging. IceShards will continue spinning above the caster until a command occurs to enter the release phase. 
 * <p>
 * Release Phase:
 * Once charged the IceShards can be released. 
 * For the player, a second right click will begin releasing.
 * IceShards are released one at a time at a regular interval.
 * For the player, IceShards released will travel towards the cursor at time of release.
 * IceShards will damage enemy units they pass through.
 * IceShards will continue their velocity until they collide with solid terrain. (Level Tiles that have collision)
 * The release phase ends when all IceShards have been released.
 * <p>
 * Waiting to Recall Phase:
 * Released IceShards will continue with their velocity until stopped by terrain. IceShards embedded in terrain will remain in place until the recall phase.
 * <p>
 * Recall Phase:
 * In the recall phase, IceShards travel back towards the caster.
 * For the player a right click begins the recall phase.
 * Recalling IceShards will change their direction towards the caster if the caster position changes.
 * Recalling IceShards will do damage to enemy units they pass through and stop when they collide with terrain.
 * If they collide with the caster, they will return to circular motion above the caster.
 * <p>
 * If the player cast this spell, another right click will end the spell. Removing all IceShards and their associated snow particles. The spell can now be recast again.
 */
public class IceShardSpell extends Spell {
	
	
	
	// controls printing of info for debugging
	private boolean print = false;
	
	
	// -------------------------------    NPC SPECIFIC     -----------------------------------------------
	// only for when an npc is using this spell
	// can't just hold the caster as a GameObject... unless we cast everytime we want it... hmmm
	NPC npcCaster;
	
	// hold the results of a vision update. Is an optional because the vision update may have found nothing
	Optional<GameObject> possibleTarget;
	
	// For when the NPC is using this spell, target is the GameObject the npc will attack.
	GameObject target;
	
	// How long the NPC will wait before recalling
	int recallTimer;
	int recallDelay = 180; // 3 seconds
	
	// How long before terminating the spell cast after the npc has recalled.
	int endTimer;
	int endDelay = 300; // 5 seconds
	
	// ---------------------------------------------------------------------------------------------------


	
	// Booleans controlling the spell phase
	private boolean casting = false;
	private boolean charging = false;
	private boolean waitingToRelease = false;
	private boolean releasing = false;
	private boolean waitingForRecall = false;
	private boolean recalling = false;

	// variables controlling IceShard quantity and spawn/release timing
	private int timer = 0;
	private int spwanTimer = 0;
	private int shardCount = 0;
	private int maxShards = 3;
	private int spawnInterval = 20;
	private int releaseInterval = 20;


	// the IceShards associated with a cast of IceShardSpell
	private ArrayList<IceShard> iceShards = new ArrayList<IceShard>();

	// set of sounds for the spawning of ice shards
	// NOTE: move this to SoundManager so the same set is used for all instances of this class. 2/3/21
	private SoundClipSet spawnSounds = new SoundClipSet();


	// Whether or not this spell will be cast by the player or by an npc.
	// NOTE: this could be a property of all spells. it seems like the best way to have player and NPCs using the same spell classes.
	// next best option is to make to separate classes, these two clasees would only implement the update part of the spell BUT then would need this class as well. That's three classes instead of 1. 
	//			It would be more efficient, but too messy. 
	private boolean playerControlled;


	
	
	
	/**
	 * Creates a new IceShardSpell object so the 'caster' GameObject can use this spell.
	 * 
	 *  @param caster The GameObject that will be able to cast this spell.
	 */
	public IceShardSpell(GameObject caster){
		super(caster);



		// NPC or Player?
		// is the caster an NPC or the player? This will effect how the spell is updated. 
		if(caster.getTag() == "player"){

			// player is using the spell
			playerControlled = true;

		}else{
			
			playerControlled = false;
			
			if(print)System.out.println("About top cast caster to NPC. Tag: " + caster.getTag());
			
			// An NPC is using this spell. Need to hold an NPC field value to obtain NPCVision results for targeting other units with this spell. 
			npcCaster = (NPC)caster;
		}

		// add IceShard spawning sounds to the SoundClipSet so a random sound can be played whenever an IceShard is spawned.
		spawnSounds.addSound(SoundManager.iceBreak2);
		spawnSounds.addSound(SoundManager.iceBreak3);
		spawnSounds.addSound(SoundManager.iceBreak4);


	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {

		// Junction between player and npc updates for IceShardSpell
		if(playerControlled){
			playerUpdate(gc,gm,dt);
		}else{
			npcUpdate(gc,gm,dt);
		}




		// Common to both NPC and player use of IceShardSpell: 

		// update any active IceShards
		if(!iceShards.isEmpty()){
			for(IceShard i : iceShards){
				i.update(gc, gm, dt);
			}
		}


	}


	/**
	 * Controls casting of ICeShardSpell for the player 
	 */
	private void playerUpdate(GameContainer gc, GameManager gm, float dt){

		// Start casting the spell if not already casting
		if(gc.getInput().isButtonDown(3) && !casting){

			casting = true;
			charging = true;
			timer = maxShards * spawnInterval;
			spwanTimer = spawnInterval;
			if(print)System.out.println("\nCasting Ice Shard Spell");

		}



		// If the spell is mid cast:   
		if(casting){

			// CHARGING
			// If in the charging phase: 
			if(charging){

				// decrement counter, once this reaches 0, the spell has finished the charging phase.
				timer--;

				// decrement spawnTimer
				spwanTimer--;


				// is it time to spawn another iceShard?
				if(spwanTimer == 0 && shardCount < maxShards){

					// ADD A NEW SHARD
					iceShards.add(new IceShard(0, caster));						// spawn in the same spot

					// play a random sound when an ice shard spawns
					spawnSounds.playRandom();

					// slightly random spawn interval? i think this could be cool... regular for now
					spwanTimer = spawnInterval;
					shardCount++;	
				}


				// when timer reaches 0, terminate charging phase 
				if(timer == 0){

					// charging has finished, switch to  "Waiting to Release" Phase.

					if(print)System.out.println("\nCharging finished, ready to release");
					charging = false;
					waitingToRelease = true;

					timer = 20;
					shardCount = iceShards.size();
					if(print)System.out.println("\nShardCount: " + shardCount + ", MaxShards: " + maxShards + "\n\t above two values should be the same");

					// Ready to recall sound
					SoundManager.iceSpell1.play();

				}




				// WAITING TO RELEASE
				// Finished charging, now the ice shards can be released one at a time, 20 frames between.. once intiated
			} else if(waitingToRelease){


				if(gc.getInput().isButtonDown(3)){

					// ENTER RELEASE PHASE
					waitingToRelease = false;
					releasing = true;

					timer = 10;
				}




				// RELEASING	
			}else if(releasing){

				// Decrement timer
				timer--;

				if(timer == 0){

					// Have all shards been released?
					if(shardCount == 0){
						// YES all have been released END THIS PHASE

						if(print)System.out.println("\nAll shards released");
						releasing = false;
						waitingForRecall = true;
						//SoundManager.spellCharging1.stop();
					}else{
						// No still more shards to release: 

						if(print)System.out.println("\nReleasing shard. Shards left: " + shardCount);


						// release the shard towards the curson
						SoundManager.arrowRelease.play();
						iceShards.get(shardCount-1).releaseShardTowardsCursor(gm,gc);
						shardCount--; 

						// Still undecided if release intervals should be slightly irregular or not...
						//count = 20;
						//timer = ThreadLocalRandom.current().nextInt(10, 20);
						timer = releaseInterval;

					}
				}




				// WAITING FOR RECALL
			}else if(waitingForRecall){


				// IceShards can now be recalled when desired.


				// Wait for right click to recall shards, ending this phase and moving to the next phase (recall phase)
				if(gc.getInput().isButtonDown(3)){

					if(print)System.out.println("\nRecalling Shards");

					// play recalling sounds: 
					SoundManager.arrowRelease.play();
					SoundManager.portalOpen.play();

					// update booleans for the next phase (recall phase) 
					recalling = true;
					waitingForRecall = false;
					timer = 420;

					// tell all IceShards to recall
					for(IceShard i : iceShards){
						i.recallShard(gm);

					}
				}




				// RECALLING
			} else if(recalling){

				// decrement counter
				//timer--;

				// terminate recalling // terminate spell cast
				if(gc.getInput().isButtonDown(3)){
					if(print)System.out.println("\nFinished recalling. Spell cast finished");

					// End this phase and end this spell cast.
					recalling = false;
					casting = false;

					// remove all active IceShards
					iceShards.clear();
				}
			}
		}
	}




	


	/**
	 * Controls casting of IceShard spell for npcs 
	 */
	private void npcUpdate(GameContainer gc, GameManager gm, float dt){

		
		// -------------------------------    NPC SPECIFIC      -----------------------------------------------------
		// target setting

		if(!casting){

			// target setting, when a target is found cast the spell
			// If vision was just updated: 
			if(npcCaster.wasVisionUpdatedThisFrame()){

				// Check if the last vision update found anything.
				possibleTarget = npcCaster.getVision().getTargetEnemy();

				if(possibleTarget.isPresent()){
					//target = possibleTarget.get();
					
					// CAST THE SPELL
					if(print)System.out.println("IceWizard has found a target to cast spell on");
					
					// Should the wizard immediatly cast? yes for now.
					casting = true;
					charging = true;
					timer = maxShards * spawnInterval;
					spwanTimer = spawnInterval;
					if(print)System.out.println("\n NPC Casting Ice Shard Spell");
					
				}

			}

		} else {
			
			// 
			if(print)System.out.print(".");
		
			
		}
		
		// -------------------------------------------------------------------------------------------------------



		// If the spell is mid cast:   
		if(casting){

			// CHARGING
			// If in the charging phase: 
			if(charging){

				// decrement counter, once this reaches 0, the spell has finished the charging phase.
				timer--;

				// decrement spawnTimer
				spwanTimer--;


				// is it time to spawn another iceShard?
				if(spwanTimer == 0 && shardCount < maxShards){

					// ADD A NEW SHARD
					iceShards.add(new IceShard(0, caster));						// spawn in the same spot

					// play a random sound when an ice shard spawns
					spawnSounds.playRandom();

					// slightly random spawn interval? i think this could be cool... regular for now
					spwanTimer = spawnInterval;
					shardCount++;	
				}


				// Termination of this phase: 
				// when timer reaches 0, terminate charging phase 
				if(timer == 0){

					// charging has finished, switch to  "Waiting to Release" Phase.

					if(print)System.out.println("\nCharging finished, ready to release");
					charging = false;
					waitingToRelease = true;

					timer = 20;
					shardCount = iceShards.size();
					if(print)System.out.println("\nShardCount: " + shardCount + ", MaxShards: " + maxShards + "\n\t above two values should be the same");

					// Ready to recall sound - DONT WANT THIS WHEN NPC CASTS
					//SoundManager.iceSpell1.play();

				}




				// WAITING TO RELEASE
				// Finished charging, now the ice shards can be released one at a time, 20 frames between.. once intiated
			} else if(waitingToRelease){

				// player initiation of release
				/**
				if(gc.getInput().isButtonDown(3)){

					// ENTER RELEASE PHASE
					waitingToRelease = false;
					releasing = true;

					timer = 10;
				}
				*/
				
				
				
				// -------------------------------    NPC SPECIFIC      -----------------------------------------------------
				
				// npc initiation of release:
				// maybe see if a target is present. wait until a vision update to do this.
				if(npcCaster.wasVisionUpdatedThisFrame()){
					
					possibleTarget = npcCaster.getVision().getTargetEnemy();
					
					if(possibleTarget.isPresent()){
						
						target = possibleTarget.get();
						
						// the wizard has charged up the spell and has found a target to release the IceShards towards.
						gm.addObject(new TempRectangle((int)target.getPosX(), (int)target.getPosY(), 16, 16, 0xffff0000, 60));
						waitingToRelease = false;
						releasing = true;
						
						// a slight delay before releasing the first IceShard
						timer = 20;
						
					}
					
				}else{
					// do nothing just wait for a vision update.
				}

				// -----------------------------------------------------------------------------------------------------------


				// RELEASING	
			}else if(releasing){

				// Decrement timer
				timer--;

				if(timer == 0){

					// Have all shards been released?
					if(shardCount == 0){
						
						// YES all have been released END THIS PHASE

						if(print)System.out.println("\nAll shards released");
						releasing = false;
						waitingForRecall = true;
						
						// -------------------------------    NPC SPECIFIC      ---------------------------------------
						
						// set time before recalling now the release has finsihed?
						recallTimer = recallDelay;
						
						
						// -------------------------------------------------------------------------------------------
						
					
					}else{
						
						// No still more shards to release: 

						if(print)System.out.println("\nReleasing shard. Shards left: " + shardCount);

						// release an IceShard
						SoundManager.arrowRelease.play();
						
						// player release
						//iceShards.get(shardCount-1).releaseShardTowardsCursor(gm,gc);
						
						// -------------------------------    NPC SPECIFIC      ---------------------------------------
						
						// NPC release
						iceShards.get(shardCount-1).releaseShardTowardsTarget(gm,target);
						
						
						// -------------------------------------------------------------------------------------------
						
						
						
						shardCount--; 

						// Still undecided if release intervals should be slightly irregular or not...
						//timer = ThreadLocalRandom.current().nextInt(10, 20); 	// irregular
						timer = releaseInterval;								// regular

					}
				}




				// WAITING FOR RECALL
			}else if(waitingForRecall){


				// IceShards can now be recalled when desired.

				
				// player waiting for recall
				/*
				// Wait for right click to recall shards, ending this phase and moving to the next phase (recall phase)
				if(gc.getInput().isButtonDown(3)){

					System.out.println("\nRecalling Shards");

					// play recalling sounds: 
					SoundManager.arrowRelease.play();
					SoundManager.portalOpen.play();

					// update booleans for the next phase (recall phase) 
					recalling = true;
					waitingForRecall = false;
					timer = 420;

					// tell all IceShards to recall
					for(IceShard i : iceShards){
						i.recallShard(gm);

					}
				}
				*/
				
				// -------------------------------    NPC SPECIFIC      ---------------------------------------
				
				
				// This could be really complicated if I want the NPC to recall the shards in a meaningful fashion..
				
				if(recallTimer == 0){
					
					// NOTE THIS IS JUST REPEAT OF PLAYER CODE
					
					if(print)System.out.println("\nRecalling Shards");

					// play recalling sounds: 
					
					// TEMPORARILY DISABLED
					//SoundManager.arrowRelease.play();
					//SoundManager.portalOpen.play();

					// update booleans for the next phase (recall phase) 
					recalling = true;
					waitingForRecall = false;
					
					// This is NPC specific 
					endTimer = endDelay;

					// tell all IceShards to recall
					for(IceShard i : iceShards){
						i.recallShard(gm);

					}
					
				}else{
					recallTimer --;
				}
				
				
				// ---------------------------------------------------------------------------------------------
				




				// RECALLING
			} else if(recalling){

				

				// terminate spell cast once the endTimer reaches 0
				if(endTimer == 0){
					if(print)System.out.println("\nFinished recalling. Spell cast finished");

					// End this phase and end this spell cast.
					recalling = false;
					casting = false;

					// remove all active IceShards
					iceShards.clear();
				
				}else{
					// decrement
					endTimer--;
				}
			}
		}

	}

	
	
	
	

	/***
	 * Renders the IceShards. 
	 */
	@Override
	public void render(Renderer r) {
		for(IceShard i : iceShards){
			i.render(r);
		}

	}



}
