package com.gussssy.gametwo.game.particles;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.debug.DebugPanel;

public class ClusterParticleEmitter extends ParticleEmitter {
	
	ArrayList<ClusterParticle> particles = new ArrayList<ClusterParticle>();
	
	
	public ClusterParticleEmitter(int tileX, int tileY){
		
		this.tag = "clusteremitter";
		this.tileX = tileX;
		this.tileY = tileY;
		this.posX = tileX * GameManager.TS;
		this.posY = tileY * GameManager.TS;
		
		this.cooldown = 5;
		this.counter = cooldown;
		emit();
		
	}
	

	@Override
	public void emit() {
		
		
		
		//System.out.println("Emitting");
		if(ThreadLocalRandom.current().nextInt(0,10) > 5 ){
			particles.add(new ClusterParticle(posX, posY));
			
		}
		
		
	}
	
	ClusterParticle cp;

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		DebugPanel.message1 = "Particles: " + particles.size();
		
		counter--;
		
		if(counter <= 0){
			emit();
			counter = cooldown;
		}
		
		
		
		for(int i = particles.size()-1; i >= 0 ; i-- ){
			
			cp = particles.get(i);
			
			// remove the particle if it is now dead
			if(cp.dead == true){
				particles.remove(i);
				continue;
			}
			
			cp.update(gm, dt);
		}
		

		
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		
		for(ClusterParticle cp : particles){
			r.drawImage(cp.getClusterImage(), (int)cp.posX, (int)cp.posY);
		}
		
	
		
	}

}
