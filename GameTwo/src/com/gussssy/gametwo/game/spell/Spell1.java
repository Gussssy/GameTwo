package com.gussssy.gametwo.game.spell;

import java.awt.event.KeyEvent;
import java.util.concurrent.ThreadLocalRandom;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.SoundManager;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.particles.ClusterParticle;
import com.gussssy.gametwo.game.particles.Particle;

/**
 * Tornado spell  
 **/
public class Spell1 extends Spell {

	// In spell
	//ArrayList<ClusterParticle> particles = new ArrayList<ClusterParticle>();
	
	int cooldown = 8;
	int count = 0;
	boolean soundPlaying = false;
	
	
	public Spell1(GameObject caster){
		
		super(caster);
		
		this.caster = caster;
		
	}
	
	ClusterParticle cp;
	Particle p;

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		
		for(int i = particles.size()-1; i >= 0;  i--){
			
			p = particles.get(i);
			
			// remove the particle if it is now dead
			if(p.dead == true){
				particles.remove(i);
				continue;
			}
			
			p.update(gm, dt);
			
			
			
		}
		
		if(gc.getInput().isKey(KeyEvent.VK_C) || gc.getInput().isButton(3)){
			System.out.println("Cast Spell");
			
			if(count <= 0){
				for(int i = 0; i < 100; i++){
					double x = ThreadLocalRandom.current().nextDouble(caster.getPosX()-5, caster.getPosX()+5);
					double y = ThreadLocalRandom.current().nextDouble(caster.getPosY()-5, caster.getPosY()+5);
					particles.add(new ClusterParticle((float)x, (float)y));
				}
				
				count = cooldown;
			}else{
				count--;
			}
			
		}
		
		// Spell sound
		if(particles.size() != 0 && !soundPlaying ){
			soundPlaying = true;
			SoundManager.wind1.loop();
		}
		
		if(soundPlaying && particles.size() == 0){
			soundPlaying = false;
			SoundManager.wind1.stop();
		}
		
		
		
	}

	

	@Override
	public void render(GameContainer gc, Renderer r) {
		
		for(Particle p : particles){
			
			r.drawImage(((ClusterParticle) p).getClusterImage(), (int)p.posX, (int)p.posY);
		}
		
		
	}

}
