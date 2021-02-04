package com.gussssy.gametwo.game.level;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.objects.environment.LampPost;
import com.gussssy.gametwo.game.objects.npc.NPCSpawner;
import com.gussssy.gametwo.game.particles.ExplosionEmitter;
import com.gussssy.gametwo.game.pathfinding.PathFinderTwo;

public class Arena5 extends Level{

	public Arena5(String levelImagePath, String backgroundImagePath, GameManager gm) {
		
		// Call to super to load the level
		super(levelImagePath, backgroundImagePath, gm);
		
		// spawners
		gm.addObject(new NPCSpawner(1,50, "botbot"));
		gm.addObject(new NPCSpawner(151,53, "badbotbot"));
		gm.addObject(new NPCSpawner(85,34, "goose"));
		gm.addObject(new NPCSpawner(52,27, "smartbot"));
		
		// emitters 
		gm.addObject(new ExplosionEmitter(83,31, true));
		
		// other objects
		gm.addObject(new LampPost(1936, 836));
		
		// Path Mapping
		PathFinderTwo.setPathMap(gm, 1, 50);
		PathFinderTwo.setPathMap(gm, 85, 35);	// goose cage
		
		// Set player location
		GameManager.player.setPlayerLocation(1, 51);
		
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		// nothing requiring updating
		
	}

}
