package com.gussssy.gametwo.game.spell;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.SoundManager;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.particles.Spell3Emitter;

public class Spell3 extends Spell {
	
	// Emits the particles for this spell
	Spell3Emitter emitter;
	
	boolean charging = false;
	
	int particleCooldown = 3;
	int particleCounter = particleCooldown;
	int spellCooldown = 120;
	
	

	public Spell3(GameObject caster) {
		super(caster);
		
		emitter = new Spell3Emitter(caster, this);
		
		
	}
	
	

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		emitter.update(gc, gm, dt);
		
	
		if(gc.getInput().isButton(3)){
			
			
			// start charging if RMB was just clicked
			if(charging == false){
				
				//remove particles from last cast
				emitter.reset();
				
				// tell the emitter the spell is charging
				emitter.setCharging(true);
				
				// set charging to true
				charging = true;
				
				// emit the first particle
				emitter.emit();
				
				SoundManager.engineIdle1.play();
				
			}else{
				
				if(particleCounter <= 0){
					emitter.emit();
					particleCounter = particleCooldown;
				}else {
					particleCounter --;
				}
				
			}
			
		// RMB is not down, was the spell charging? if so time to rlease the spell
		} else if(charging == true){
			charging = false;
			emitter.setCharging(false);
			SoundManager.engineIdle1.stop();
			SoundManager.spellCast1.play();
			
		}
		
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		emitter.render(gc, r);
		
	}

	

}
