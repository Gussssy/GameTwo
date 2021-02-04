package com.gussssy.gametwo.game.level;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.objects.environment.Platform;
import com.gussssy.gametwo.game.objects.items.Donut;
import com.gussssy.gametwo.game.objects.npc.Goose;
import com.gussssy.gametwo.game.objects.npc.NPCSpawner;
import com.gussssy.gametwo.game.objects.npc.SmartBotBot;
import com.gussssy.gametwo.game.pathfinding.PathFinderTwo;

public class Arena6 extends Level {

	public Arena6(String levelImagePath, String backgroundImagePath, GameManager gm) {
		super(levelImagePath, backgroundImagePath, gm);
		
		//gm.addObject(new NPCSpawner(10,5, "goose"));
		gm.addObject(new Goose(10,5));
		gm.addObject(new SmartBotBot(20,17, gm));
		
		NPCSpawner spawner = new NPCSpawner(38,17, "badbotbot");
		spawner.setMaxSpawns(1);
		spawner.setCooldown(120);
		gm.addObject(spawner);
		
		NPCSpawner spawner2 = new NPCSpawner(8,17, "botbot");
		spawner2.setMaxSpawns(1);
		spawner2.setCooldown(120);
		gm.addObject(spawner2);
		
		
		PathFinderTwo.setPathMap(gm, 10, 17);
		
		for(int i = 0; i <= 17; i++){
			gm.addObject(new Platform(1,i));
			gm.addObject(new Platform(998,i));
			gm.addObject(new Platform(60,i));
			
		}
		
		GameManager.player.setPlayerLocation(5, 10);
		
		
		
		// add some donuts: 
		gm.addObject(new Donut(10, 16));
		gm.addObject(new Donut(11, 16));
		gm.addObject(new Donut(12, 16));
		gm.addObject(new Donut(13, 16));
		
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		
		
	}

}
