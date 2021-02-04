package com.gussssy.gametwo.game.objects.projectile;

import java.util.concurrent.ThreadLocalRandom;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.engine.gfx.Image;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.components.HealthBar;
import com.gussssy.gametwo.game.objects.Charachter;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.objects.ObjectType;
import com.gussssy.gametwo.game.objects.tempobjects.TempLine;
import com.gussssy.gametwo.game.particles.SnowEmitter;

/**
 * IceShard projectile of IceShardSpell
 * - will move with circular motion while charging and then in a straight line towards the cursor when released 
 **/
public class IceShard extends Projectile {

	// the GameObject that cast the spel and spawned this ice shard
	private GameObject caster;

	// the ice shards image
	private Image image = new Image("/particles/ice_shard_1.png");

	// control the postion of the shard relative to the caster
	private int offSetX;
	private int offSetY = -16;

	private float rotationX;
	private float rotationY;

	// boolean defining the state of the ice shard
	private boolean charging = true;		// initially
	private boolean releasing = false;
	private boolean waitingForRecall = false;
	private boolean recalling = false;
	private boolean returned = false;

	// remove this, temporary workaround of sound limits
	//SoundClip iceBreak1 = new SoundClip("/audio/ice_break_1.wav");

	// The total velocity ice shards will have when released, split between x and y axis.
	private float velocity = 400;

	// integers used to determine direction of the ice shard when released
	int mouseX, mouseY, camX, camY, casterX, casterY;

	// Don't need a seperate snowEWmiiter for each ice shard
	SnowEmitter snowMaker = new SnowEmitter();


	// Control frequency of snowflake emission
	private int snowFlakeTimer = 0;
	//private int snowFlakeInterval = 0;	// wont need this? 

	double radians = 0.0;
	
	
	// temp testing
	private boolean recallCollision = true;

	/**
	 * IceShardConstructor 
	 **/
	public IceShard(int offSetX, GameObject caster){

		this.posX = caster.getPosX() + offSetX;
		this.posY = caster.getPosY() + offSetY;
		this.offSetX = offSetX;
		this.caster = caster;
		
		this.tag = "ice_shard";

		// hitbox
		this.width = 9;
		this.height = 9;
		hitBox = new AABBComponent(this);
		addComponent(hitBox);

		// set up snow emiiter
		snowMaker.maxVx = 2;
		snowMaker.minVx = -2;
		snowMaker.maxVy = 10;
		snowMaker.minVy = 5;

		
		




	}


	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {

		// rather then put snowmaker in gameObjects... just so much easier like this. Its goes when the shard goes.
		snowMaker.update(gc, gm, dt);

		//System.out.println(hitBox.toString());

		if(charging){

			//offSetX = 
			rotationX += 2* Math.cos(radians);
			rotationY += 2* Math.sin(radians);

			//System.out.println(offSetX);
			//System.out.println(offSetY);

			radians += 0.2;

			// circular motion:
			posX = caster.getPosX() + offSetX;
			posY = caster.getPosY() + offSetY;

			if(snowFlakeTimer == 0){
				snowMaker.emitParticle(posX + rotationX + 5, posY + rotationY + 5);
				snowFlakeTimer = ThreadLocalRandom.current().nextInt(10, 60);

			}else {
				snowFlakeTimer--;
			}



		}else if(releasing){


			// now moving towards the cursor.. well no just in one direction atm...
			posX += dt*vx;
			posY += dt*vy;

			// tile collison check
			if(gm.getLevelTileCollision((int)(posX/GameManager.TS), (int)(posY/GameManager.TS))){

				System.out.println("\nIceShard has stopped releasing movement");
				System.out.println("\t Hit tile: x = " + (int)posX/GameManager.TS + ", y = " + (int)posY/GameManager.TS);
				waitingForRecall = true;
				releasing = false;
			}

			// emit snow flakes on release
			snowMaker.emitParticle(posX, posY);

		}else if(waitingForRecall){

			// has been released, finished travelling

			// dont release snowflakes while stationary

		}else if(recalling){

			recallShard(gm);
			
			posX += dt*vx;
			posY += dt*vy;

			// emit snow flakes while recalling
			snowMaker.emitParticle(posX, posY);

			// tile collision check
			if(gm.getLevelTileCollision((int)(posX/GameManager.TS), (int)(posY/GameManager.TS)) && recallCollision){

				System.out.println("\nIceShard has stopped recalling movement");
				System.out.println("\t Hit tile: x = " + (int)posX/GameManager.TS + ", y = " + (int)posY/GameManager.TS);
				returned = true;
				recalling = false;
				//iceBreak1.play();
			}

		}else if(returned){
			// has it actually returned or did they get stuck..? 

			// after x amount of time this iceshard will be removed
		}
		
		
		//components
		updateComponents(gc,gm,dt);


	}


	/**
	 * Setup Iceshard to be released. 
	 */
	public void releaseShard(GameManager gm, GameContainer gc){

		// set booleans to move ice spell into release phase
		charging = false;
		releasing = true;

		// --------------------------------------------------------------------
		// Determine velocity for ice shards so they travel towards the cursor

		// get cam offset and location of cursor and caster
		mouseX = gc.getInput().getMouseX();
		mouseY = gc.getInput().getMouseY();
		camX = gc.getRenderer().getCamX();
		camY = gc.getRenderer().getCamY();
		casterX = (int)caster.getPosX();
		casterY = (int)caster.getPosY();

		// XDistance
		// if mouseX > posX : xDistance is positive, mouse is to the right of player
		int xDistance = mouseX - (casterX -camX);

		// YDistance
		// if mouseY > posY
		int yDistance = mouseY - (casterY - camY);


		// DEBUG RENDERING: render lines representing x and y distance and total distance if showDebug is true
		if(GameManager.showDebug){

			System.out.println("DEBUG LINES FOR SHARD RELEASE");
			// render line between npc to mouseX
			gm.addObject(new TempLine(60, (int)caster.getPosX(), (int)caster.getPosY(), mouseX+camX , mouseY + camY, 0xffffffff));			

			// render xDistance
			gm.addObject(new TempLine(60, casterX, casterY, casterX + xDistance , casterY, 0xff00ff00));

			// render yDistance
			gm.addObject(new TempLine(60, casterX, casterY, casterX , casterY + yDistance, 0xffff00ff));
		}


		// Calculate vx and vy
		// use the ratio of x and y distance to set x and y velocity
		vx = velocity * ((float)xDistance/ (float)(Math.abs(xDistance) + Math.abs(yDistance)));
		vy = velocity * ((float)yDistance/ (float)(Math.abs(xDistance) + Math.abs(yDistance)));
		
		System.out.println("\nIceShard Release Velocities:\n\tvx: " + vx + "\n\tvy: " + vy);

		//-------------------------------------------------------------------------------------

	}

	/**
	 * Set up shard for recall..
	 *  - thinking that this method is so similar to above I can break it down with a third method for doing the repitition
	 **/
	public void recallShard(GameManager gm){

		waitingForRecall = false;
		releasing = false; // if the shard was mid release, which it probably isnt 
		recalling = true;

		// need to move towards the player

		int xDistance = (int)(caster.getPosX() - posX);

		int yDistance = (int)(caster.getPosY() - posY);
		
		// testing severity of excessive rounding
		//int xDistance = (int)caster.getPosX() - (int)posX;
		//int yDistance = (int)caster.getPosY() - (int)posY;

		// DEBUG RENDERING: render lines representing x and y distance and total distance if showDebug is true
		if(GameManager.showDebug){

			System.out.println("DEBUG LINES FOR SHARD RELEASE");
			// render line from shard to the caster
			gm.addObject(new TempLine(60, (int)posX, (int)posY, (int)caster.getPosX() , (int)caster.getPosY(), 0xffffffff));			

			// render xDistance
			gm.addObject(new TempLine(60, (int)posX, (int)posY, (int)caster.getPosX() + xDistance , casterY, 0xff00ff00));

			// render yDistance
			gm.addObject(new TempLine(60, (int)posX, (int)posY, (int)caster.getPosX() , (int)caster.getPosY() + yDistance, 0xffff00ff));
		}


		// set velocity to ratio of x and y distance to the target (back to the player)
		vx = velocity * ((float)xDistance/ (float)(Math.abs(xDistance) + Math.abs(yDistance)));
		vy = velocity * ((float)yDistance/ (float)(Math.abs(xDistance) + Math.abs(yDistance)));
		
		System.out.println("\nIceShard Recall Velocities:\n\tvx: " + vx + "\n\tvy: " + vy);


	}

	
	/**
	 * Render the Ice Shard.
	 * 
	 * How the IceShard is rendered depends on the state it is currently in.
	 */
	@Override
	public void render(GameContainer gc, Renderer r) {

		// as is not a game object or a component this has to be called manually... there has to be a way around this
		snowMaker.render(gc, r);

		
		if(charging){
			
			// If charging, Iceshard uses the casters position + offsets to positon above the caster + rotation on x and y
			r.drawImage(image, (int)(caster.getPosX() + rotationX) + offSetX, (int)(caster.getPosY() + rotationY) + offSetY);
		
		}else if(releasing || recalling || waitingForRecall || returned){
			
			// otherwise, ice shard is rendered at its own position. Slightly offset so that it collisdes with tiles from its center. 
			r.drawImage(image, (int)(posX - 4.5f), (int)(posY - 4.5f));
		}

		// render the ice shards hitbox (only renders when showDebug = true)
		renderComponents(gc,r);

	}

	
	/**
	 * IceShard Collision
	 * 
	 *  IceShard hitbox only responds to collisions with Charachter hitboxes
	 */
	@Override
	public void collision(GameObject other, AABBComponent otherHitBox) {

		// COLLISION WITH THE CASTER
		// collision with the caster is important ONLY when the shards are recalling
		// ignore all collision with the caster otherwise.
		if(other.getId() == caster.getId()){
			
			// If shard collides with the caster durting recall, shard returns to charging. 
			if(recalling){
				// shard to return to spinning above caster in charge phase
				recalling = false;
				charging = true;	
			}
			
			return;
		}

		
		// COLLISION WITH CHARACHTERS
		if(other.getObjectType() == ObjectType.NPC || other.getObjectType() == ObjectType.PLAYER ){
			
			Charachter c;

			// If friendly fire is off, avoid damaging team mates
			if(GameManager.friendlyFire == false){
				
				// cast to character, this will work as NPC and player are characters
				c = (Charachter)other;
				
				// Check if the caster is on the same team as the charcter that collided with the shard
				if(c.getTeam() == ((Charachter)caster).getTeam()){
					
					// same team and friedly fire is off, ignore this collison.
					return;
				}
			}

			
			// Valid collision, damage must be applied

			// get the health bar of the charchater that was hit
			HealthBar healthBar  = (HealthBar)other.findComponent("hp");
			
			// apply damage
			GameManager.log.log(((Charachter)other).getTag() + " has been damaged by an ice shard cast by " + caster.getTag());
			healthBar.healthChanged(-20);
		}


	}




	/**
	 * Ice Shards do not bounce off tiles, they get stuck in them so shouldnt be forced to implement this....
	 * This is a property of bouncing projectiles... which this is not. This should be an interface method. 
	 */
	@Override
	protected void tileCollision(GameManager gm) {
		// TODO Auto-generated method stub

	}


	public boolean isCharging() {
		return charging;
	}


	public void setCharging(boolean charging) {
		this.charging = charging;
	}


	public boolean isReleasing() {
		return releasing;
	}


	public void setReleasing(boolean releasing) {
		this.releasing = releasing;
	}


	public boolean isWaitingForRecall() {
		return waitingForRecall;
	}


	public void setWaitingForRecall(boolean waitingForRecall) {
		this.waitingForRecall = waitingForRecall;
	}


	public boolean isRecalling() {
		return recalling;
	}


	public void setRecalling(boolean recalling) {
		this.recalling = recalling;
	}


	public boolean isReturned() {
		return returned;
	}


	public void setReturned(boolean returned) {
		this.returned = returned;
	}


}
