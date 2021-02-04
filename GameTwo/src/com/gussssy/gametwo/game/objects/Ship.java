package com.gussssy.gametwo.game.objects;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.engine.gfx.Image;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.components.AABBComponent;

public class Ship extends GameObject {
	
	private Image shipImage = new Image("/Ship.png");
	//private boolean occupied;
	
	
	public Ship( int posX, int posY){
	
	this.tag = "ship";	
	this.posX = posX;
	this.posY = posY;
	
	this.width = shipImage.getWidth();
	this.height = shipImage.getHeight();
	
	components.add(new AABBComponent(this));
		
		
		
	}
	

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		// TODO Auto-generated method stub
		
		updateComponents(gc, gm, dt);
		
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		r.drawImage(shipImage, (int)posX, (int)posY);
		renderComponents(gc,r);
		
	}

	@Override
	public void collision(GameObject other, AABBComponent otherHitBox) {
		
		
	} 

}
