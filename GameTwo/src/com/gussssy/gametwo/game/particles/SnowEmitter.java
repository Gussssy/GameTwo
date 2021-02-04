package com.gussssy.gametwo.game.particles;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.game.GameManager;

public class SnowEmitter extends ParticleEmitter {

ArrayList<SnowParticle> particles = new ArrayList<SnowParticle>();
	
	public boolean active = false; 

	int startTileX = -16;
	int stopTileX = 34;
	int y = -16;
	
	public int maxParticlesPerEmission = 100; // was 50
	public int minParticlesPerEmission = 75;	// was 20
	public int cooldown = 5;
	int count;
	
	
	public int maxVx = 30;	// was 30
	public int minVx = -10;	// was 0
	public int maxVy = 50;	// was 50
	public int minVy = 10; // was 10
	
	
	// for weather / whole screen snow effects
	public int emissionRangeFromPlayer = 600;

	public SnowEmitter() {
		
		count = cooldown;
		this.tag = "snow_emitter";	
	}
	
	// public void loadConfiguration(SnowConfiguration config) // optionals for all the field values relating to snow emmission

	@Override
	public void emit() {
		
		//System.out.println("Snow Emit. Particles: " + particles.size());
		
		int numberThisEmission = ThreadLocalRandom.current().nextInt(minParticlesPerEmission, maxParticlesPerEmission);
		int randomX;
		
		
		for(int i = 0; i < numberThisEmission; i++){
			
			randomX = ThreadLocalRandom.current().nextInt((int)GameManager.player.getPosX()-emissionRangeFromPlayer, (int)GameManager.player.getPosX()+emissionRangeFromPlayer);
			float vy = (float)(ThreadLocalRandom.current().nextDouble(minVy,maxVy));
			float vx = (float)(ThreadLocalRandom.current().nextDouble(minVx,maxVx));
			
			particles.add(new SnowParticle(randomX, y, vx, vy));
		}
		
		
		
	}
	
	
	public void emitParticle(float posX, float posY){
		
		float vy = (float)(ThreadLocalRandom.current().nextDouble(minVy,maxVy));
		float vx = (float)(ThreadLocalRandom.current().nextDouble(minVx,maxVx));
		
		particles.add(new SnowParticle(posX, posY, vx, vy));
		
	}
	
	SnowParticle p;

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		if(active)count--;
		
		
		if(count <= 0 && active){
			emit();
			count = cooldown;
		}
		
		
		// do i want it trying to do this every update? 
		for(int i = particles.size()-1; i >= 0 ; i-- ){
			
			p = particles.get(i);
			
			
			// remove the particle if it is now dead
			if(p.dead == true){
				particles.remove(i);
				continue;
			}
			
			p.update(gm, dt);
						
		}
		
		
		
		//GameManager.debugMessage2 = "Snow Particles" + particles.size();
		
		
		
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		
		
		for(SnowParticle p : particles){
			r.drawImage(p.snowFlakeImage, (int)p.posX-2, (int)p.posY-1);
		}
		
	}
}
