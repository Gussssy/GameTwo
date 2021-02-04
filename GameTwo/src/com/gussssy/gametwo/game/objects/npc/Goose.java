package com.gussssy.gametwo.game.objects.npc;

import java.util.concurrent.ThreadLocalRandom;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.engine.gfx.ImageTile;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.SoundManager;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.components.HealthBar;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.objects.ObjectType;

public class Goose extends NPC {
	
	private ImageTile gooseTile = new ImageTile("/goose_tile1.png", 25,32);
	//private float animationState = 0;
	
	int honkCounter;
	
	
	
	public Goose(int tileX, int tileY){
		
		this.tag = "goose";
		this.objectType = ObjectType.NPC;
		
		this.tileX = tileX;
		this.tileY = tileY;
		this.posX = tileX * GameManager.TS;
		this.posY = tileY * GameManager.TS;
		
		/*width = 25;
		height = 32;*/
		
		this.topPadding = 2;
		this.leftRightPadding = 3;
		this.width = 16 - 2*leftRightPadding;
		this.height = 16 - topPadding;
		
		this.addComponent(new AABBComponent(this));
		addComponent(new HealthBar(this));
		
		honkCounter =  ThreadLocalRandom.current().nextInt(16000,48000);
		
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
			idle(gm);
			break;
		case PATH: 
			break;
		case ATTACK:
			aggroIdle(gm);
			//pathToTargetObjectAndAttack(gm);
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
		
		
		
		
		// sounds
		honkCounter--;
		if(honkCounter < 0){
			
			int whatHonk = ThreadLocalRandom.current().nextInt(0,3);
			//System.out.println("whatHonk: " + whatHonk);
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
			honkCounter = ThreadLocalRandom.current().nextInt(8000,24000);
			//honk.play();
		}
		



		npcMovement(gc, gm, dt);
		//npcWalkingAnimation(dt);
		npcUpdate(gc, gm, dt);

		
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		
		r.drawImageTile(gooseTile, (int)posX, (int)posY-16, (int)animationState, direction);
		renderComponents(gc,r);
		
		
		
	}

	@Override
	public void collision(GameObject other, AABBComponent otherHitBox) {
		
		if(other.getTag() == "bullet"){
			other.dead = true;
			
			int whatHonk = ThreadLocalRandom.current().nextInt(0,6);
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
			default: 
				break;
			}
			
		}
		
	}

}
