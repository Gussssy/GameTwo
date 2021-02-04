package com.gussssy.gametwo.game.particles;

public class CircularMotionSpellParticle extends SpellParticle {
	
	// to feed into sin/cos functions for circular motion
	float radians;
	
	// radians if using different radian for x and y
	float radiansX, radiansY;
	
	// the magnitude of the / amplitude..? 
	float magnitude;
	
	float magnitudeX, magnitudeY;

	public CircularMotionSpellParticle(float particleOffX, float particleOffY) {
		super(particleOffX, particleOffY);
		// TODO Auto-generated constructor stub
	}

}
