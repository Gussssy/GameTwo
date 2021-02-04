package com.gussssy.gametwo.game.level;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.objects.npc.NPCSpawner;
import com.gussssy.gametwo.game.pathfinding.PathFinderTwo;

@SuppressWarnings("unused")
public class Arena2 extends Level {

	public Arena2(String levelImagePath, String backgroundImagePath, GameManager gm) {
		super(levelImagePath, backgroundImagePath, gm);
		
		//gm.addObject(new NPCSpawner(1,11, "botbot"));
		//gm.addObject(new NPCSpawner(33,11, "badbotbot"));
		//gm.addObject(new NPCSpawner(17,12, "goose"));
		
		PathFinderTwo.setPathMap(gm, 1, 11);
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		
	}

}
