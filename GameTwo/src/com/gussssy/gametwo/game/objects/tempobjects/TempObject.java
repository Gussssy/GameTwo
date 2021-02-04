package com.gussssy.gametwo.game.objects.tempobjects;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.objects.GameObject;

/**
 * Superclass for all TempObjects.
 * 
 *  Temp objects are rendered objects that will only exist for a set number of frames determined by the value of the lifeTime variable.
 *  
 **/
public abstract class TempObject extends GameObject{
	
	protected int lifeTime;
	
	public TempObject(int lifeTime){
		this.lifeTime = lifeTime;
	}
	
	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		lifeTime--;
		
		if(lifeTime <= 0)dead = true;
		
	}
	
	
	
	

}
