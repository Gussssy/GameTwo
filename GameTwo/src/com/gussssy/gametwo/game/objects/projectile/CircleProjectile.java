package com.gussssy.gametwo.game.objects.projectile;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.objects.GameObject;

public class CircleProjectile extends Projectile{

	/***
	 *  should this be an interface TileCollider..? 
	 *  
	 *  How do interfaces and inheritance work.... Children will only be forced to implement methods if interfacing parent is abstract.. I think
	 */
	@Override
	protected void tileCollision(GameManager gm) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Renderer r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void collision(GameObject other, AABBComponent otherHitBox) {
		// TODO Auto-generated method stub
		
	}

}
