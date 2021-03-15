package com.gussssy.gametwo.game.particles;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.debug.DebugPanel;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.spell.Spell;

/**
 * Holds the paricles for Spell3 Spell, unamemed and completely experimental. 
 *  
 *  See Spell3 for more details
 * 
 **/
public class Spell3Emitter extends SpellEmitter {

	

	ArrayList<CircularMotionSpellParticle> particles = new ArrayList<CircularMotionSpellParticle>();

	boolean charging = true;

	float vx = 50;
	float vy = 0;
	
	float radians = 0;
	
	float rotationConstant = 1;
	
	// how long the spell particles will exist for after the spell has finished charging and the particles are released
	int timeTillDepawn = 300;
	
	
	int spellDistanceFromPlayer = 16;

	double emissionRadius = 3;
	
	public Spell3Emitter(GameObject caster, Spell spell) {
		super(caster, spell);
		
	}
	
	
	
	
	
	
	/**
	 * Emits a single particle while charging 
	 **/
	@Override
	public void emit(){
		
		
		
		// EMISSION RADIUS
		
		//emissionRadius = particles.size()/3 + 1;
		
		// X Y OFFSETS
		
		double x = ThreadLocalRandom.current().nextDouble(0,emissionRadius);
		@SuppressWarnings("unused")
		double y = ThreadLocalRandom.current().nextDouble(0,emissionRadius);

		
		// MAKE THE PARTICLE
		CircularMotionSpellParticle p = new CircularMotionSpellParticle((float)x,(float)x);
		
		
		
//  MODIFY THE PARTICLE . . . .
		
		
	
		// RADIANS -------------------------------------------------------------------------------
		
		
		/*double radiansX = ThreadLocalRandom.current().nextDouble(0,Math.PI*2);
		double radiansY = ThreadLocalRandom.current().nextDouble(0,Math.PI*2);*/
		
		//double radians = ThreadLocalRandom.current().nextDouble(-Math.PI*2,Math.PI*2);
		
		// set x and y use same radians
				p.radians = (float) ThreadLocalRandom.current().nextDouble(0, 0.5);
				
				// set radians different for x and y
				//p.radiansX = (float)radiansX;
				//p.radiansY = (float)radiansY;
		
		
		
		// MAGNITUDE OF MOTION -------------------------------------------------------------------------------------
		
				
				
				//float magnitude = (float)ThreadLocalRandom.current().nextDouble(-emissionRadius/10,emissionRadius/10);
				//float magnitudeX = (float)ThreadLocalRandom.current().nextDouble(-emissionRadius/10,emissionRadius/10);
				//float magnitudeY = (float)ThreadLocalRandom.current().nextDouble(-emissionRadius/10,emissionRadius/10);
				
				float magnitude = 4;
				
				p.magnitude = magnitude;
				p.magnitudeX = magnitude;
				p.magnitudeY = magnitude;
		
				// different for x and y
				/*
				p.magnitudeX = magnitudeX;
				p.magnitudeY = magnitudeY;
				*/
		
		
		
		particles.add(p);
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {

		
		DebugPanel.message1 = "Spell Particles: " + particles.size();
		DebugPanel.message2 = "spellPosX: " + spellPosX;
		DebugPanel.message3 = "spellPosY: " + spellPosY;
		
		// Spell Location
		if(charging){
			// if the spell is charging, the location of the spell is next to the caster, on the left or right depenmding on direction caster is facing 
			if(caster.getDirection() == 0){
				spellPosX = caster.getPosX() + spellDistanceFromPlayer;
			}else{
				spellPosX = caster.getPosX() - spellDistanceFromPlayer;
			}
			
			spellPosY = caster.getPosY();
		}else{
			
			spellPosX += vx*dt;
			spellPosY += vy*dt;	
		}
		
		
		//radians += 0.1;
		
		
		for(CircularMotionSpellParticle p : particles){
			
			// same rads for x and y
			p.radians += 0.1;
			p.particleOffX += p.magnitudeX * Math.cos(p.radians);
			p.particleOffY += p.magnitudeY * Math.sin(p.radians);
			
			// Different rads for x and y
			/*
			p.radiansX += 0.1;
			p.radiansY += 0.1;
			p.particleOffX += rotationConstant * Math.cos(p.radiansX);
			p.particleOffY += rotationConstant * Math.sin(p.radiansY);
			*/

		}





	}

	@Override
	public void render(Renderer r) {
		

		for(CircularMotionSpellParticle p : particles){
			
			r.drawFillRect((int)(spellPosX + p.particleOffX), (int)(spellPosY + p.particleOffY), 3, 3, 0x9922ff55);

		}

	}

	public boolean isCharging() {
		return charging;
	}

	public void setCharging(boolean charging) {
		this.charging = charging;
	}






	@Override
	public void reset() {
		
		particles.clear();
		
	}

}
