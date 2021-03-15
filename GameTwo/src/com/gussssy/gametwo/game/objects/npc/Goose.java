package com.gussssy.gametwo.game.objects.npc;

import java.util.concurrent.ThreadLocalRandom;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.SoundManager;
import com.gussssy.gametwo.game.Textures;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.objects.ObjectType;


/**
 * An Goose NPC that honks every once in a while. 
 */
public class Goose extends NPC {
	
	
	
	// Controls timing of honks
	int honkCounter;
	
	
	/**
	 * 
	 */
	public Goose(int tileX, int tileY){
		
		this.tag = "goose";
		this.objectType = ObjectType.NPC;
		
		this.tileX = tileX;
		this.tileY = tileY;
		this.posX = tileX * GameManager.TS;
		this.posY = tileY * GameManager.TS;
		
		// This is the true dimaensions of the goose. 
		/*width = 25;
		height = 32;*/
		
		// These are the dimensions currently being used. They are rather off. 
		this.topPadding = 2;
		this.leftRightPadding = 3;
		this.width = 16 - 2*leftRightPadding;
		this.height = 16 - topPadding;
		
		addComponent(new AABBComponent(this));
		
		// this should be here? it is done in charachter
		//addComponent(new HealthBar(this));
		
		//honkCounter =  ThreadLocalRandom.current().nextInt(16000,48000);
		honkCounter = 10; 
		speed = 120;
		animationSpeed = 12;
	
		
	}
	

	@Override
	protected void attack(GameManager gm, int direction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		switch(actionType){
		case IDLE:
			idlePathing(gm);
			break;
		case PATH: 
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
		
		
		// Animating Left and Right Movement
		if(movingLeft){
			// left walking animation 
			//System.out.println("moving right");
			direction = 1;
			animationState += dt * animationSpeed;
			if(animationState > 4)animationState = 0;
		} else if(movingRight){
			// right walking animation
			//System.out.println("moving left");
			direction = 0;
			animationState += dt * animationSpeed;
			if(animationState > 4)animationState = 0;
		} else {
			// stationary 
			animationState = 0;
		}
		
		
		
		
		// Goose Honking
		
		// Decrement counter
		honkCounter--;
		
		// When counter reaches 0, honk. 
		if(honkCounter < 0){
			
			// Randomly select which honk..
			int whatHonk = ThreadLocalRandom.current().nextInt(0,3);
			
			// NOTE: This should be a soundclip set..
			
			switch(whatHonk){
			case 0:
				SoundManager.goose.play();
				break;
				
			case 1:
				SoundManager.goose2.play();
				break;
			case 2:
				SoundManager.goose3.play();
				break;
			}
			
			// Randomly assign time till next honk. 
			//honkCounter = ThreadLocalRandom.current().nextInt(8000,24000);
			honkCounter = ThreadLocalRandom.current().nextInt(800,2400);
			
		}
		


		// NPC updates
		npcMovement(gc, gm, dt);
		//npcWalkingAnimation(dt); // managed internally for goose. (maybe should change that)
		npcUpdate(gc, gm, dt);

		
	}

	/**
	 * Renders the Goose. 
	 */
	@Override
	public void render(Renderer r) {
		
		r.drawImageTile(Textures.gooseTile, (int)posX, (int)posY-16, (int)animationState, direction);
		renderComponents(r);
		
		
	}

	@Override
	public void collision(GameObject other, AABBComponent otherHitBox) {
		
		// TODO: Goose diesnt collide yet...  7/3/21
		
	}

}
