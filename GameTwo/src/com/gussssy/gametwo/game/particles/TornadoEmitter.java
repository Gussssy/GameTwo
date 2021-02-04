package com.gussssy.gametwo.game.particles;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.game.GameManager;

public class TornadoEmitter extends ParticleEmitter {
	
	
	ArrayList<RainParticle> particles = new ArrayList<RainParticle>();
	
	int startTileX = 0;
	int stopTileX = 50;
	int y = -50;
	
	int maxParticlesPerEmission = 500;
	int minParticlesPerEmission = 350;
	int frequency = 60;
	float waitTime;
	float accumulatedTime = 0;

	public TornadoEmitter() {
		
		//tileY = 0;		// rain is emitted from the top of the screen
		//startTileX = 
		
		waitTime = 1/(float)frequency;
		
		
	}

	@Override
	public void emit() {
		
		int numberThisEmission = ThreadLocalRandom.current().nextInt(minParticlesPerEmission, maxParticlesPerEmission);
		int randomX;
		
		
		for(int i = 0; i < numberThisEmission; i++){
			
			randomX = ThreadLocalRandom.current().nextInt(startTileX*GameManager.TS, stopTileX*GameManager.TS);
			
			particles.add(new RainParticle(randomX, 0, 0));
		}
		
		
		
	}
	
	RainParticle p;

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		accumulatedTime+= dt;
		
		if(accumulatedTime >= waitTime){
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

