package com.gussssy.gametwo.game.objects.testObjects;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.objects.GameObject;

/**
 * Not really a game object. 
 * Simply a place holder for when an AABBComponent is made for a set of level tiles. 
 * An AABBComponent must be attached to a parent GameObject. 
 * When making temporary hitboxes for GameObjects that collide with level tiles, this class will be the parent.
 **/
public class TileObject extends GameObject {
	
	public TileObject(){
		this.tag = "tile";
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		// this object is not updated as it not intended to be added to the GameObjects list
		
		
	}

	@Override
	public void render(Renderer r) {
		
		// This object is not rendered
		
	}

	@Override
	public void collision(GameObject other, AABBComponent otherHitBox) {
		
		// This object is not effected by collisions
		
	}

}
