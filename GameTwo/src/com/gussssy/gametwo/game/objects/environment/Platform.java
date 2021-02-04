package com.gussssy.gametwo.game.objects.environment;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.objects.ObjectType;
import com.gussssy.gametwo.game.physics.CollisionType;

public class Platform extends GameObject{
	
	//private int color = (int)(Math.random() * Integer.MAX_VALUE);
	private int color = 0xff333333;
	boolean stationary = true;
	boolean moveVertical = false;
	boolean moveHorizontal = false;
	float verticalMovementConstant = 2;
	float horizontalMovementConstant = 2;
	//type = ObjectType.ENVIRONMENT;
	
	public Platform(int  tileX, int tileY){
		
		this.tag = "platform";
		this.objectType = ObjectType.ENVIRONMENT;
		this.collisionType = CollisionType.PLATFORM;
		this.width = 32;
		this.height = 16;
		this.posX = tileX * GameManager.TS;
		this.posY = tileY * GameManager.TS;
		
		this.addComponent(new AABBComponent(this));
	}

	float temp = 0;
	boolean delayApplied = false;
	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt){
		
		
		
		// platform movement
		temp += dt;
		
		if(!delayApplied){
			
			
		}
		
		
		if(moveVertical){
			posY += (Math.cos(temp) * verticalMovementConstant);
		}
		
		/*
		if(moveHorizontal){
			posX += (Math.cos(temp) * horizontalMovementConstant);
		}
		
		//System.out.println(temp);
		 */
	
		this.updateComponents(gc, gm, dt);
		
	}

	@Override
	public void render(GameContainer gc, Renderer r){
		
		r.drawFillRect((int)posX, (int)posY, width, height, color);
		this.renderComponents(gc, r);
		
	}

	@Override
	public void collision(GameObject other, AABBComponent otherHitBox){
		
		
		//color = (int)(Math.random() * Integer.MAX_VALUE);
		
	}

	public void setMoveVertical(boolean moveVertical) {
		this.moveVertical = moveVertical;
	}

}
