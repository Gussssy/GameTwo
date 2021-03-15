package com.gussssy.gametwo.game.particles;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.engine.audio.SoundClip;
import com.gussssy.gametwo.engine.gfx.Light;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.objects.GameObject;

public class ExplosionEmitter extends ParticleEmitter{

	
	boolean sound = false;

	// the emitted particles
	ArrayList<ExplosionParticle> particles = new ArrayList<ExplosionParticle>();

	// particle emission controls
	int counter = 300;
	public boolean autoEmit = true;
	int minVelocity = 0;					// max/min velocity... maybe for constant speed particle effects we remove this..
	//int maxVelocity =17;
	int maxVelocity =15;
	//double velocityFallOff = 0.80;
	double velocityFallOff = 0.88;			// really only for explosion effects
	int numberOfParticles = 8000;
	int particleSize = 5;
	int minLifeTime = 120;
	int maxLifeTime = 240;
	int cooldown = 120;
	
	
	// Varied Location variables
	boolean randomLocation = false;
	float explosionPosX, explosionPosY;
	// fix this idiot
	int maxPosX = 50;
	int maxPosY = 0;
	int minPosX = -50;
	int minPosY = -5;
	
	//x 22 range between 74 and 96
	
	
	
	
	SoundClip explosionSound = new SoundClip("/audio/Explosion.wav");
	
	Light light = new Light(130, -1);

	// Death Controls
	//boolean singleUse = true;
	private boolean willDie = false;


	//Debugging
	boolean print = false;

	public ExplosionEmitter(int tileX, int tileY, boolean autoEmit){

		this.tag = "explosionEmitter";
		this.tileX = tileX;
		this.tileY = tileY;
		this.posX = tileX * GameManager.TS;
		this.posY = tileY * GameManager.TS;
		this.width = 32;
		this.height = 32; 
		counter = cooldown;
		this.autoEmit = autoEmit;

		// set up particle collision
		//Physics.add	

	}
	
	/**
	 * Constructor when creating an explosion for a grenade 
	 **/
	public ExplosionEmitter(){
		
	}
	
	
	

	/**
	 * Emits a large number of particles that take a random direction and velocity.
	 * - particle velocity will decrease by an amount proportional to current velocity
	 * - the lifetime of the paricles will also be determined randomly 
	 **/
	public void emit(){
		System.out.println("Emitting");
		//int color = (int)(Math.random() * Integer.MAX_VALUE);
		
		
		if(randomLocation) {
			
			explosionPosX = posX + ThreadLocalRandom.current().nextInt(minPosX*16, maxPosX*16);
			explosionPosY = posY + ThreadLocalRandom.current().nextInt(minPosY*16, maxPosY*16);
			//System.out.println("Set Random Location:  posX = " + explosionPosX + ", posY =  " + explosionPosY);
			
		}else{
			explosionPosY = posY;
			explosionPosX = posX;
			//System.out.println("Didnt set random location");
		}

		for(int i = 0; i <= numberOfParticles; i++){

			// randomly determine angle of the particle
			double theta = ThreadLocalRandom.current().nextDouble(0, 2*Math.PI);

			// randomly determine the velocity of the pariticle
			double velocity = ThreadLocalRandom.current().nextDouble(minVelocity,maxVelocity);

			// H = total velocity
			// A = vy
			// O = vx

			// have angle and velocity (H)
			// sin0 = O/H


			// O
			// sin0 = O/H
			// O = H*sin0
			double vx = velocity * Math.sin(theta);

			// A
			// cos0 = A/H
			// A = H*cos0
			double vy = velocity * Math.cos(theta);

			// assign a random lifetime to the particle, note this likely wont have an effect as main reason for particle death is alpha value hitting 0 (see ExplosionParticle)
			int lifeTime =(int) (ThreadLocalRandom.current().nextDouble(minLifeTime, maxLifeTime)/*velocity*/) + 1; //+1 temporary fix as a life time of 0 leads to infinite lifeTime

			if(print)System.out.println("New Particle: Velocity: " + velocity + ", " + ", LifeTime: " + lifeTime);
			
				
			
				ExplosionParticle p = new ExplosionParticle(explosionPosX, explosionPosY, vx, vy,velocity ,lifeTime, particleSize); 
				particles.add(p); 
				
		


			
		}

		if(sound)explosionSound.play();


	}

	ExplosionParticle p;

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {

		////// need a dead check here or particfle emiiters are a memory leak risk
		// because this particvle emitter out lives the object that spawned it, it must be stored in the GameObjects list seperately from the bomb thats uses this
		// added. but maybe we use an explosion emitter for all explosions rather then a seperate... but then audio issues... idk

		counter--;

		//System.out.println("Explosion Emitter particles: " + particles.size());
		//GameManager.debugMessage6 = "Explosion Particles: " + particles.size();

		if(willDie){
			if(particles.size() == 0)dead = true;
		}

		// emit THIS IS NOT WHAT CONTROLS EMISSION WITH BOMBS see Bomb
		if(counter <= 0 && autoEmit == true){
			emit();
			//willDie = true;		// this doesnt belong here, the emitter only needs to die when spawned by a bomb
			counter = cooldown + ThreadLocalRandom.current().nextInt(-109,0);
			//System.out.println("Will Die has been set");
		}



		// do i want it trying to do this every update? 
		for(int i = particles.size()-1; i >= 0 ; i-- ){

			//particles.get(i).update(gm, dt);
			// get the particle to be updated;
			p = particles.get(i);



			p.update(gm, dt);

			// update particles
			p.lifeTime--;
			if(p.lifeTime == 0)p.dead = true;

			// calculate effects of gravity
			//p.vy += dt * gm.getGravity();

			// velocity fall off
			p.vx = p.vx * velocityFallOff;
			p.vy = p.vy * velocityFallOff;

			// update position
			p.posX += p.vx;
			p.posY += p.vy;

			// determine tile position
			p.tileX = (int)p.posX/16;
			p.tileY = (int)p.posY/16;

			// use tile position to detect collisions with solid tiles
			if(gm.getLevelTileCollision(p.tileX, p.tileY)){
				p.dead = true;
			}

			// remove the particle if it is now dead
			if(p.dead == true){
				particles.remove(i);
				continue;
			}

		}

		//emit = false;

		updateComponents(gc,gm,dt);

		//GameManager.debugMessage9 = "Particles: " + particles.size();

		/*for(Particle p : particles){
				p.update(gm,dt);
			}*/




	}


	@Override
	public void render(Renderer r) {

		//r.drawImage(goose, (int)posX, (int)posY);

		//int color = (int)(Math.random() * Integer.MAX_VALUE);

		// render particles here, not within the Particle Class
		for(ExplosionParticle p : particles){
			r.drawFillRect((int)p.posX, (int)p.posY, p.size, p.size, p.color);
		}
		
		if(particles.size() != 0)r.drawLight(light, (int)explosionPosX, (int)explosionPosY);

		renderComponents(r);

	}

	@Override
	public void collision(GameObject other, AABBComponent otherHitBox) {

		//emit = true;

	}

	public boolean isWillDie() {
		return willDie;
	}

	public void setWillDie(boolean willDie) {
		this.willDie = willDie;
	}

}
