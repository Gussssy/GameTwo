package com.gussssy.gametwo.game.level;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.gfx.Image;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.objects.environment.Moon;
import com.gussssy.gametwo.game.objects.npc.BotBot;
import com.gussssy.gametwo.game.objects.npc.NPCSpawner;
import com.gussssy.gametwo.game.pathfinding.PathFinderTwo;

public class SpaceStationMap extends Level{
	
	private static Image levelMap = new Image("/level/space_station_map.png");
	private static Image levelBackground = new Image("/nightsky_bg.png");

	public SpaceStationMap(GameManager gm) {
		super(levelMap, levelBackground, gm);
		
		
		gm.addObject(new Moon("/moon_1.png", 50, 125));
		
		Level.gravity = 0.5f;
		
		//gm.addObject(new BotBot()z);
		gm.addObject(new NPCSpawner(19,31, "smartbot"));
		gm.addObject(new NPCSpawner(56,31, "badbotbot"));
		PathFinderTwo.setPathMap(gm, 19, 31);
		
		setPlayerLocation(19,31);
		
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		// TODO Auto-generated method stub
		
	}

}
