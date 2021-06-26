package com.gussssy.gametwo.game.particles;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.game.Event;
import com.gussssy.gametwo.game.GameManager;

/**
 * First attempt at creating a fire/flame effect.
 * 
 *  For this attempt, a 'flame' will occur over certain range of x values
 *  
 *  The flame will be made up of color changing particles, start white/yellow and become progressively more red.
 */
public class FlameEffectOne implements Event{
	
	
	
	ArrayList<ColorChangingParticle> particles = new ArrayList<ColorChangingParticle>();
	
	int startX, endX, y;
	
	int delay = 1;
	
	
	
	int removeDeadParticlesInterval = 60;
	int removeDeadParticlesTimer = removeDeadParticlesInterval;
	
	
	
	public FlameEffectOne(){
		
		// centered
		//startX = 663;
		//endX = 770;
		
		// large
		startX = 600;
		endX = 866;
		
		y = 1056;
		
	}
	
	
	
	
	
	public FlameEffectOne(int startX, int endX){
		
		
		this.startX = startX;
		this.endX = endX;
		
		y = 1056;
		
	}
	
	
	//||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
	

	@Override
	public void update(GameContainer gc, GameManager gm, float dt){
		
		
		// Add a new particle each frame
		
		// Skip entire Row
		if(ThreadLocalRandom.current().nextBoolean()) {
			
			for(int x = startX; x < endX; x++){
				
				// Skip a single particle
				if(ThreadLocalRandom.current().nextBoolean()) {
					particles.add(new ColorChangingParticle(x, y, 0, -50, 0xffffff55, 0xffff5500, (int)(180 + 100*Math.sin(dt*100)), false));
				}
				
				
			}
			
			
			
		}
		
		
		
		
		
		
		// Update Particles
		
		for(ColorChangingParticle p : particles){
			p.update(gm, dt);
		}
		
		
		
		
		
		
		
		// Remove dead particles once every 60 frames
		
		if(removeDeadParticlesTimer == 0){
			
			// loop backwards and remove dead particles
			for(int i = particles.size()-1; i >= 0; i--){
				
				if(particles.get(i).dead == true){
					particles.remove(i);
				}
				
			}
			
			// reset timer to the interval 
			removeDeadParticlesTimer = removeDeadParticlesInterval; 
			
		}else{
			
			// decrement timer
			removeDeadParticlesTimer --;
			
		}
		
		
	}

	@Override
	public void render(Renderer r) {
		
		// Rendering spark particles
		for(ColorChangingParticle p : particles){
			p.Render(r);
		}
		
	}

	@Override
	public void endEvent() {
		// TODO Auto-generated method stub
		
	}

}
