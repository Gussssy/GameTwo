package com.gussssy.gametwo.game.objects.npc;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.engine.gfx.ImageTile;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.objects.GameObject;

public class Rabbit extends NPC {
	
	// Witrhout standing 2 frame walk
	//private ImageTile rabbitImage = new ImageTile("/character/rabbit1.png",32,27);
	
	// With standing and 2 frame walk
	//private ImageTile rabbitImage = new ImageTile("/character/rabbit1_standing.png",32,32);
	
	// With standing and 2 frame walk
	private ImageTile rabbitImage = new ImageTile("/character/rabbit1_4frames.png",32,32);
	
	
	private int imageOffY = 16;
	
	
	public Rabbit(int tileX, int tileY){
		
		this.tag = "rabbit";
		
		this.tileX = tileX;
		this.tileY = tileY;
		this.posX = tileX*GameManager.TS;
		this.posY = tileY*GameManager.TS;
		
		// animation
		frames = 3;
		
		
		this.topPadding = 2;
		this.leftRightPadding = 3;
		this.width = 16 - 2*leftRightPadding;
		this.height = 16 - topPadding;
		
		//jump = -10;
		speed = 150;
		
		// hitbox
		hitBox = new AABBComponent(this);
		addComponent(hitBox);
		
		team = 0;
		
		
		
	}

	@Override
	protected void attack(GameManager gm, int direction) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		// TODO Auto-generated method stub
		
		
		

		// Vision / Awareness
		// ...


		// Decsion Making
		switch(actionType){
		case IDLE:
			idle(gm);
			break;
		case PATH: 
			break;
		case ATTACK:
			aggroIdle(gm);
			break;
		case FOLLOW:
			break;
		case WAIT:
			break;
		default:
			break;
		}


		// Movement
		npcMovement(gc, gm, dt);
		npcWalkingAnimation(dt);

		// General NPC update
		npcUpdate(gc, gm, dt);
		
		

		

	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		
		//System.out.println("Rabbit render: animationState = " + animationState);
		
		// temporary avoidd first frame when moving
		//animationState +=1;
		
		if(waiting){
			animationState = -1;
		}
		
		r.drawImageTile(rabbitImage, (int)posX, (int)posY - imageOffY, (int)animationState+1
				, direction);
		
		renderComponents(gc,r);

	}

	@Override
	public void collision(GameObject other, AABBComponent otherHitBox) {
		// TODO Auto-generated method stub

	}

}
