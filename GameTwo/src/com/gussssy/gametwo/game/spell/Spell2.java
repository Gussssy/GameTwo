package com.gussssy.gametwo.game.spell;

import java.awt.event.KeyEvent;
import java.util.concurrent.ThreadLocalRandom;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.SoundManager;
import com.gussssy.gametwo.game.debug.DebugPanel;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.particles.ClusterParticle;
import com.gussssy.gametwo.game.particles.Particle;

/**
 * Tornado Spell? Same as spell1 
 **/
public class Spell2 extends Spell {

	int cooldown = 8;
	int count = 0;
	boolean soundPlaying = false;

	public Spell2(GameObject caster) {
		super(caster);
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		DebugPanel.message1 = "Particles: " + particles.size();


		for(int i = particles.size()-1; i >= 0;  i--){

			p = particles.get(i);

			// remove the particle if it is now dead
			if(p.dead == true){
				particles.remove(i);
				continue;
			}

			p.update(gm, dt);



		}

		// only want player using this here...
		if(gc.getInput().isKey(KeyEvent.VK_C) || gc.getInput().isButton(3)){
			System.out.println("Cast Spell");

			if(count <= 0){
				for(int i = 0; i < 100; i++){
					double x = ThreadLocalRandom.current().nextDouble(caster.getPosX()-5, caster.getPosX()+5);
					double y = ThreadLocalRandom.current().nextDouble(caster.getPosY()-5, caster.getPosY()+5);
					ClusterParticle cp = new ClusterParticle((float)x, (float)y);
					//cp.type = 2;
					particles.add(cp);
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


	public void castSpell(GameContainer gc){
		
		
			System.out.println("Cast Spell");

			if(count <= 0){
				for(int i = 0; i < 100; i++){
					double x = ThreadLocalRandom.current().nextDouble(caster.getPosX()-5, caster.getPosX()+5);
					double y = ThreadLocalRandom.current().nextDouble(caster.getPosY()-5, caster.getPosY()+5);
					ClusterParticle cp = new ClusterParticle((float)x, (float)y);
					//cp.type = 2;
					particles.add(cp);
				}

				count = cooldown;
			}else{
				count--;
			}

		
		
	}

	@Override
	public void render(GameContainer gc, Renderer r) {

		for(Particle p : particles){

			r.drawImage(((ClusterParticle) p).getClusterImage(), (int)p.posX, (int)p.posY);
		}


	}

}
