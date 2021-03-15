package com.gussssy.gametwo.game.objects;

import com.gussssy.gametwo.engine.gfx.Light;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.components.HealthBar;
import com.gussssy.gametwo.game.physics.CharachterCollisionHandler;
import com.gussssy.gametwo.game.physics.CollisionType;
import com.gussssy.gametwo.game.weapon.Weapon;

/**
 * Extension of momentum to group all charachter game objects. 
 * 
 *  Charachter Game objects include the player and NPCs
 *  -	They move around the game enviroment.
 *  - They are effected by forces: gravity, explosions, platforms etc
 *  
 *  Possible future feature:
 *  They have mass and momentum.
 **/
public abstract class Charachter extends GameObject {
	
	
	// BASIC CHARACHTER MOVEMENT VARIABLES
	
	// the movement speed of the charachter
	protected float speed = 100;
	
	// falling/jumping variables
	public float fallDistance = 0;		
	public float gravity = 10;
	public float jump = -4;
	public float smallJump = -3;
	public float regularJump = -4;
	public boolean onGround = false;
	
	
	// COLLISION VARIABLES
	
	// AABB Collision Detection Hit box
	public boolean onAABB = false;
	public AABBComponent onThisObject = null;
	protected AABBComponent hitBox;
	protected CharachterCollisionHandler collisionHandler = new CharachterCollisionHandler(this);
	
	// Health:
	protected int health = 100;
	protected HealthBar healthBar = new HealthBar(this);
	
	// Weapon 
	protected Weapon weapon = null;
		
	// Team
	protected int team;
	
	
	// Light - each child class has thier own light atm.
	Light light = new Light(32, 0xffff0000);
	
	
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
	
	// ADVANCED PLAYER MOVEMENT VARIABLES
	
	// Velocity: "v"
	protected float vT = 0;		// total velocity
	protected float vx = 0;		// x velocity
	protected float vy = 0;		// y velocity
		
	
	protected float acceleration = 8;	// this means that it will take 1/4 seconds to reach max velocity... strange way of doing this really
	
	// mass of the charachter
	protected float mass = 80;
	
	// Momentum: "P"
	protected float pT = 0;		// total momentum
	protected float px = 0;		// x momentum
	protected float py = 0;		// y momentum
	protected float angleOFMomentum;
	
	/**Max momentum the charachter can achieve by moving*/
	protected float maxPx = getSpeed() * mass; 
	
	protected float maxSelfMomentum;
	
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
	
	
	
	
	
	
	public Charachter(){
		
		// set up health bar
		addComponent(healthBar);
	}


	/**
	 * Equip the weapon. Remove the old weapon if required. 
	 **/
	public void equipWeapon(Weapon weapon){
		
		//System.out.println("Charachter Equip Weapon. \n\t Equipping: " + weapon.getTag());
		System.out.println("Equip Weapon");
		System.out.println("Incoming weapn: " + weapon.getTag());
		
		// Has a weapon been equipped already?
		if(this.weapon == null){
			// no weapon equipped yet
			this.weapon = weapon;
			addComponent(weapon);
			System.out.println("Initial: Equipped Weapon: " + weapon.getTag());
			
		}else{
			// There was a weapon already equipped, need to remove it first
			
			// remove the current wepon from components
			System.out.println("Removing currently equipped weapon: " + this.weapon.getTag());
			//components.remove(findComponent(weapon.getTag()));
			// remove current weapon from components using its tag
			removeComponent(this.weapon.getTag());
			this.weapon = weapon;
			addComponent(weapon);
			System.out.println("Equipped Weapon new weapon: " + this.weapon.getTag());
		}
		
	}
	
	// TESTING THIS BEING HERE, i do really hate using this method
	// This is used for acharchter collision but maybe this belongs in GameObject.. but projectiles dont use this system.. they determine thier tileLocation from posLocation
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
	
	
	
	public float getSpeed() {
		return speed;
	}



	public boolean isOnAABB() {
		return onAABB;
	}



	public void setOnAABB(boolean onAABB) {
		this.onAABB = onAABB;
	}



	public boolean isOnGround() {
		return onGround;
	}



	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
	}



	public float getJump() {
		return jump;
	}
	
	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}


	public int getTeam() {
		return team;
	}


	public void setTeam(int team) {
		this.team = team;
	}

	

}
