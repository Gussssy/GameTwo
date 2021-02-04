package com.gussssy.gametwo.game.particles;

import com.gussssy.gametwo.game.GameManager;

public class SpellParticle extends Particle {
	
	// particles location relative to the location of the parent spell
	float particleOffX, particleOffY;
	
	

	public SpellParticle(float particleOffX, float particleOffY){
		
		this.particleOffX = particleOffX;
		this.particleOffY = particleOffY;
		
		
	}
	
	
	
	@Override
	public void update(GameManager gm, float dt) {
		
		
	}
	
	

}
