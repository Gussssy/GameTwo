package com.gussssy.gametwo.game.objects.projectile;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.engine.gfx.Image;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.particles.ExplosionEmitter;

public class Bomb extends GameObject implements ExplosiveObject{

	private Image bombImage = new Image("/BlueBomb.png");
	private float speed = 100;
	
	private double particleX = 1;
	private double particleY = 1;
	private double radians = 0;
	
	private ExplosionEmitter emitter;


	public Bomb(float posX, float posY, int direction, GameManager gm){

		this.posX = posX;
		this.posY = posY;
		this.direction = direction;
		this.tag = "bomb";

		this.width = bombImage.getWidth();
		this.height = bombImage.getHeight();

		this.tileX = (int) (posX / GameManager.TS);
		this.tileY = (int) (posY / GameManager.TS);
		this.offX = posX % GameManager.TS;
		this.offY = posY % GameManager.TS;
		
		addComponent(new AABBComponent(this));
		
		emitter = new ExplosionEmitter(tileX,tileY, false);  // dont like giving it tile postion here. doesnt make sense. it did make smese for testing but not use in the actual game
		emitter.autoEmit = false;
		gm.addObject(emitter);
		
		
		//particleX = 

	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {

		switch(direction){
		case 0: 
			// fire to right
			offX += speed * dt;
			//System.out.println("Bomb offX: " + offX);
			//System.out.println("speed * dt = " + (speed*dt));
			break;
		case 1: 
			// fire to left
			offX -= speed * dt;
			//System.out.println("Bomb offX: " + offX);
			//System.out.println("speed * dt = " + (speed*dt));
			break;
		}
		
		radians += 0.1;
		
		particleX += Math.cos(radians);
		particleY += Math.sin(radians);
		


		// TILE TRACKING

		// If object has moved entirely into lower tile
		if(offY > GameManager.TS){
			tileY++; //increment to the new tile
			offY -= GameManager.TS;
			//System.out.println("Object moved to lower tile, tileY: " + tileY);
		}

		// If object has moved entirely into above tile
		if(offY < 0){
			tileY--; //decrement to the new tile
			offY += GameManager.TS;
			//System.out.println("Object moved to upper tile, tileY: " + tileY);
		}

		// If object is now entirely into right tile
		if(offX > GameManager.TS){
			tileX++; //increment to the new tile
			offX -= GameManager.TS;
			//System.out.println("Object moved to next right tile, tileX: " + tileX);
		}

		// If the object is now entirely in left tile
		if(offX < 0){
			tileX--; //decrement to the new tile
			offX += GameManager.TS;
			//System.out.println("Object moved to next left tile, tileX: " + tileX);
		}

		posX = tileX * GameManager.TS + offX;
		posY = tileY * GameManager.TS + offY;
		
		
		
		// TILE COLLISION SHOULD GO HERE 
		if(gm.getLevelTileCollision(tileX, tileY)){
			// should move the bomb back so that..
			System.out.println("Bomb destroyed by level tile collision"); 
			if(direction == 0){
				// direction is to the right
				
				//correct postion of the bomb so that explosion does not occur inside solid tiles
				posX -= offX + width; // shift bomb a little to the left
				
			}else{
				// direction is to the left
				
				//correct postion of the bomb so that explosion does not occur inside solid tiles
				posX += (GameManager.TS - offX) + width/2; // shift bomb a little to the right
				
			}
			explode();
		}
		
		//GameManager.debugMessage5 = "Bomb PosX: " + posX;
		//GameManager.debugMessage6 = "Bomb PosY: " + posY;
		//GameManager.debugMessage7 = "Bomb OffX: " + offX;
		//GameManager.debugMessage8 = "Bomb OffY: " + offY;
		//GameManager.debugMessage9 = "Bomb Direction: " + direction;
		
		this.updateComponents(gc, gm, dt);

	}

	@Override
	public void render(GameContainer gc, Renderer r) {

		r.drawImage(bombImage, (int)(posX), (int)(posY));
		r.drawFillRect((int)(posX + particleX), (int)(posY + particleY) , 2, 2, 0xffffffff);
		
		//r.drawFillRect((int)(posX), (int)(posY), 20, 20, 0xff);
		this.renderComponents(gc, r);

	}

	@Override
	public void collision(GameObject other, AABBComponent otherHitBox) {
		
		if(other.getTag() == "player" || other.getTag() == "explosionEmitter")return;
		
		
		
		
		
		 System.out.println("Bomb destroyed by: " + other.getTag());
		 

	}
	
	/**
	 * ExplosiveObject interface method.
	 * - tells the explosion emmiter to emit
	 * - sets the position of the explosion
	 * - sets explosion emmitter will die to true
	 * - sets this object as dead 
	 **/
	@Override
	public void explode(){
		
		// avoid multi explosions.. even though they are cool
		if(dead == true)return;
		
		dead = true;
		emitter.setPosX(this.posX);
		emitter.setPosY(this.posY);
		emitter.emit();
		emitter.setWillDie(true);
	}

}
