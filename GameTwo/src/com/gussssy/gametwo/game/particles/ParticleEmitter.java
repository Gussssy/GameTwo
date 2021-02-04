package com.gussssy.gametwo.game.particles;



import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.physics.CollisionType;

public abstract class ParticleEmitter extends GameObject{
	
	// location
	protected int tileX, tileY;
	protected float posX, posY;
	
	// the emitted particles
	//ArrayList<ExplosionParticle> particles = new ArrayList<ExplosionParticle>();
	
	// variables shared by all particle emitter
	int numberOfParticles;
	
	// particle emission controls
	protected int counter;
	protected int cooldown;
	
	
	
	//Debugging
	boolean print = false;
	
	public ParticleEmitter(){
		
		// Set hit box. 
		// This doesnt need to be here.
		addComponent(new AABBComponent(this));
		
		collisionType = CollisionType.EMITTER;
	}
	
	public abstract void emit();
	
	// Dont need this this is a GameObeject abstract method.
	public abstract void update(GameContainer gc, GameManager gm, float dt);
	



	@Override
	public void collision(GameObject other, AABBComponent otherHitBox) {
		
		
		
	}

	public int getTileX() {
		return tileX;
	}

	public int getTileY() {
		return tileY;
	}

	public float getPosX() {
		return posX;
	}

	public float getPosY() {
		return posY;
	}

	public void setTileX(int tileX) {
		this.tileX = tileX;
	}

	public void setTileY(int tileY) {
		this.tileY = tileY;
	}

	public void setPosX(float posX) {
		this.posX = posX;
	}

	public void setPosY(float posY) {
		this.posY = posY;
	}

}
