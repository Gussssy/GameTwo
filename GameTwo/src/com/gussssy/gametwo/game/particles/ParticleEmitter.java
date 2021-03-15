package com.gussssy.gametwo.game.particles;


import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.objects.GameObject;

/**
 * Base class for ParticleEmitters
 * <p>
 * ParticleEmitters manage particles; create, update, render and remove.
 * Particles are held in an array.  
 * ParticleEmitters iterate through the particle array each frame to update, render and remove dead particles.
 * 
 */
public abstract class ParticleEmitter extends GameObject{
	
	
	//Controls printing of debug information to the console.
	boolean print = false;
	
	// location of the particle emitter
	//		the location of the emitter is not always important. 
	// whyis this declared again when is already in GameObject..
	protected int tileX, tileY;
	protected float posX, posY;
	
	
	// variables shared by all particle emitter
	protected int numberOfParticles;
	
	// particle emission controls
	protected int counter;
	protected int cooldown;
	
	
	
	
	/**
	 *  There is no need for this. 
	 */
	public ParticleEmitter(){
		
		// Set hit box. 
		// This doesnt need to be here.
		addComponent(new AABBComponent(this));
		
	}
	
	// ParticleEmitters must implement this. However this does not specify a duration so is only useful for continuous emission or a single emission event. 
	public abstract void emit();
	
	
	/**
	 * Particle should not have collisions. A GameObject may have have a ParticleEmitters it uses in response to a collision however. 
	 * 		TODO: 18/2/21 particle emiiters should not be GameObjects. 
	 * 		- need to consider, if I want all particles attatched to a game object to just dissapear when that object dies, e.g ice wizard snow particles...
	 */
	public void collision(GameObject other, AABBComponent otherHitBox){
		
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
