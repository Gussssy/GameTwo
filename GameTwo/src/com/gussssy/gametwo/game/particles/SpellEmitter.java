package com.gussssy.gametwo.game.particles;

import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.spell.Spell;

public abstract class SpellEmitter extends ParticleEmitter {
	
	GameObject caster;
	Spell spell;
	
	// the location of the spell - this should be found in spell not spell emitter
	float spellPosX, spellPosY;
	
	public SpellEmitter(GameObject caster, Spell spell){
		this.caster = caster;
		this.spell = spell; 
		
	}
	
	public abstract void reset();
	
	//ArrayList<Particle> particles = new ArrayList<Particle>();


}
