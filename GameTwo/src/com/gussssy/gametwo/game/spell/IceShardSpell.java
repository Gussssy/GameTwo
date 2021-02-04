package com.gussssy.gametwo.game.spell;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.engine.audio.SoundClipSet;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.SoundManager;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.objects.projectile.IceShard;

public class IceShardSpell extends Spell {

	
	// FOR SURE THIS SHOULD BE AN ENUMERATION SO I DONT ACIOTVATE A BOOL AND DEACTIVATE A BOOL AT SAME TIME
	private boolean casting = false;
	private boolean charging = false;
	private boolean waitingToRelease = false;
	private boolean releasing = false;
	private boolean waitingForRecall = false;
	private boolean recalling = false;

	// controlling quanitiy and timing
	private int timer = 0;
	private int spwanTimer = 0;
	private int shardCount = 0;
	private int maxShards = 100;
	private int spawnInterval = 20;


	// the ice shards
	private ArrayList<IceShard> iceShards = new ArrayList<IceShard>();
	
	// set of sounds for the spawning of ice shards
	private SoundClipSet spawnSounds = new SoundClipSet();

	public IceShardSpell(GameObject caster) {
		super(caster);
		
		spawnSounds.addSound(SoundManager.iceBreak2);
		spawnSounds.addSound(SoundManager.iceBreak3);
		spawnSounds.addSound(SoundManager.iceBreak4);
		
		
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {


		// Start casting the spell if not already casting
		if(gc.getInput().isButtonDown(3) && !casting){

			casting = true;
			charging = true;
			timer = maxShards * spawnInterval;
			spwanTimer = spawnInterval;
			System.out.println("\nCasting Ice Shard Spell");

		}

		
		
		
		
		// Control the casting of the spell
		
		
		// If the spell is mid cast: 
		if(casting){

			// CHARGING
			// If in the chargind stage
			if(charging){
				
				// start playing the spell charging sound
				//SoundManager.spellCharging1.loop(); // this sound sucks, get rid of it completely

				// decrement counter
				timer--;
				
				// decrement spawnTimer
				spwanTimer--;
				
				
				// is it time to spawn another iceShard?
				if(spwanTimer == 0 && shardCount < maxShards){
					
					// ADD A NEW SHARD
					// in the same location or slightly different? 
					//iceShards.add(new IceShard(-8 + shardCount * 8, caster)); // slightly spaced
					iceShards.add(new IceShard(0, caster));						// spawn in the same spot
					
					// play sounds from the set when an ice shard spawns, random probs better, didnt like hearing same order
					//spawnSounds.playNext();
					spawnSounds.playRandom();
					
					// slightly random spawn interval? i think this could be cool... regular for now
					spwanTimer = spawnInterval;
					shardCount++;	
				}
				
				
				// TODO:  spin the ice shards
				// DONE implemented in IceShard


				// if charging has finished
				if(timer == 0){

					System.out.println("\nCharging finished, ready to release");
					charging = false;
					waitingToRelease = true;

					timer = 20;
					shardCount = iceShards.size();
					System.out.println("\nShardCount: " + shardCount + ", MaxShards: " + maxShards + "\n\t above two values should be the same");
					
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
						
						System.out.println("\nAll shards released");
						releasing = false;
						waitingForRecall = true;
						//SoundManager.spellCharging1.stop();
					}else{
						// No still more shards to release: 
						
						System.out.println("\nReleasing shard. Shards left: " + shardCount);

						// ToDO: release shard towards the cursor
						
						// release the shard
						SoundManager.arrowRelease.play();
						iceShards.get(shardCount-1).releaseShard(gm,gc);
						shardCount--; 
						
						// Still undecided if release intervals should be slightly irregular or not...
						//count = 20;
						timer = ThreadLocalRandom.current().nextInt(10, 20);

					}
				}


			// WAITING FOR RECALL
			}else if(waitingForRecall){

				// wait sometime before recalling? no maybe shards wont recall untill they have finished travelling

				// Bring shards back to caster
				// revaluate path each frame..? each few frames?

				// Recall shards when right mouse button is clicked
				if(gc.getInput().isButtonDown(3)){

					System.out.println("\nRecalling Shards");
					//SoundManager.whoosh1.play(); // hate this sound for recall
					SoundManager.arrowRelease.play();
					SoundManager.portalOpen.play();
					recalling = true;
					waitingForRecall = false;
					timer = 420;
					
					// tell all ice shards to recall
					for(IceShard i : iceShards){
						//i.setWaitingForRecall(false);
						//i.setRecalling(true);
						i.recallShard(gm);
						
					}
				}

			// RECALLING
			} else if(recalling){
				
				// decrement counter
				//timer--;
				
				// terminate recalling // terminate spell cast
				// has recalling finished? in future recall finishes when all shards have returned OR shards have finished recall movement
				if(gc.getInput().isButtonDown(3)){
					System.out.println("\nFinished recalling. Spell cast finished");
					recalling = false;
					casting = false;
					iceShards.clear();
				}
			}

		} // end of casting block
		
		// update the ice shards
		if(!iceShards.isEmpty()){
			for(IceShard i : iceShards){
				i.update(gc, gm, dt);
			}
		}
		
		
		if(gc.getInput().isKeyDown(KeyEvent.VK_P)){
			spawnSounds.playNext();
			
		}
		

	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		for(IceShard i : iceShards){
			i.render(gc, r);
		}

	}
	
	

}
