package com.gussssy.gametwo.game.objects.testObjects;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.objects.GameObject;

/***
 * NOTE: this is not really a game object, they are not added to the GameObjects list 
 **/
public class PathNodeVisualObject extends GameObject {
	
	GameObject parent;
	boolean active = true;
	
	public PathNodeVisualObject(int tileX, int tileY, GameObject parent){
		
		this.tileX = tileX;
		this.tileY = tileY;
		this.parent = parent;
		
		this.width = this.height = GameManager.TS;
		
		addComponent(new AABBComponent(this));
		
		
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		updateComponents(gc, gm, dt);
		
	}

	@Override
	public void render(Renderer r) {
		
		if(active)r.drawFillRect(tileX * GameManager.TS + (GameManager.TS/2), tileY * GameManager.TS + (GameManager.TS/2), 5, 5, 0xff00ff00);
		
	}

	@Override
	public void collision(GameObject other, AABBComponent otherHitBox) {
		
		// If the parent object has collided with the node, remove it.
		if(other.getId() == parent.getId()){
			active = false;
		}else return;
		
		
	}

}
