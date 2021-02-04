package com.gussssy.gametwo.game.particles;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.objects.player.Player;

public class RainEmitter extends ParticleEmitter {
	
	Player player;
	
	ArrayList<RainParticle> particles = new ArrayList<RainParticle>();
	
	public boolean active = true; 
	
	int startTileX = 0;
	int stopTileX = 50;
	int y = -50;
	
	
	public float maxVx = 15;
	public float minVx = 0;
	public float maxVy = 200;
	public float minVy = 150 ;
	
	
	public int maxParticlesPerEmission = 100;
	public int minParticlesPerEmission = 50;
	public int frequency = 30;
	float waitTime;
	float accumulatedTime = 0;
	
	
	// used tpo creat and update poarticles
	RainParticle p;
	float vx, vy;

	public RainEmitter() {
		
		this.tag = "rain_emitter";
		
		//tileY = 0;		// rain is emitted from the top of the screen
		//startTileX = 
		
		waitTime = 1/(float)frequency;
		
		
	}

	@Override
	public void emit() {
		
		//System.out.println("Raim Emitting");
		
		int numberThisEmission = ThreadLocalRandom.current().nextInt(minParticlesPerEmission, maxParticlesPerEmission);
		int randomX;
		
		
		for(int i = 0; i < numberThisEmission; i++){
			
			randomX = ThreadLocalRandom.current().nextInt((int)GameManager.player.getPosX()-800, (int)GameManager.player.getPosX()+800);
			vy = (float)(ThreadLocalRandom.current().nextDouble(minVy,maxVy));
			vx = (float)(ThreadLocalRandom.current().nextDouble(minVx,maxVx));
			
			particles.add(new RainParticle(randomX, vx, vy));
		}
		
		
		
	}
	


	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		//GameManager.debugMessage1 = "Rain Particles: " + particles.size();
		
		if(active)accumulatedTime+= dt;
		
		if(accumulatedTime >= waitTime && active){
			accumulatedTime -= waitTime;
			emit();
		}
		
		// do i want it trying to do this every update? 
		for(int i = particles.size()-1; i >= 0 ; i-- ){
			
			p = particles.get(i);
			p.update(gm, dt);
			
			// remove the particle if it is now dead
			if(p.dead == true){
				particles.remove(i);
				continue;
			}
						
		}
		
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		
		
		for(RainParticle p : particles){
			r.drawFillRect((int)p.posX, (int)p.posY, p.width, p.height, p.color);
		}
		
	}

}
