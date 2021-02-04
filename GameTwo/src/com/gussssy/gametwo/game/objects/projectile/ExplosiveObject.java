package com.gussssy.gametwo.game.objects.projectile;

public interface ExplosiveObject {
	
	/**
	 * Method which initiates an explosion and ensures once this has finished the Explosion Emitter is removed from the GameObjects and is left withouit references..
	 * - implementations of this method must set the ExplosionEmitters boolean willdie to true so it will be removed once the active particle count hits 0 
	 **/
	public abstract void explode();

}
