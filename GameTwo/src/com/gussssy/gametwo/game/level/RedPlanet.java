package com.gussssy.gametwo.game.level;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.gfx.Image;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.SoundManager;
import com.gussssy.gametwo.game.objects.environment.Moon;
import com.gussssy.gametwo.game.objects.npc.BadBotBot;
import com.gussssy.gametwo.game.objects.npc.BotBot;
import com.gussssy.gametwo.game.objects.npc.Rabbit;
import com.gussssy.gametwo.game.pathfinding.PathFinderTwo;

public class RedPlanet extends Level{
	
	private static Image levelMap = new Image("/level/red_planet_map.png");
	private static Image levelBackground = new Image("/level/mars_sky_bg_2.png");

	public RedPlanet(GameManager gm) {
		super(levelMap, levelBackground, gm);
		
		// Set gravity to that of mars
		Level.gravity = 3.711f;
		
		// set players spawn location
		setPlayerLocation(157,29);
		
		gm.addObject(new Moon("/moon_1.png", 50, 50));
		gm.addObject(new BadBotBot(10,10));
		gm.addObject(new BotBot(153,35,gm));
		gm.addObject(new Rabbit(193,23));
		
		PathFinderTwo.setPathMap(gm, 0, 28);
		PathFinderTwo.setPathMap(gm, 153, 35);
		PathFinderTwo.setPathMap(gm, 193, 23);
		
		
		//SoundManager.marsSound1.loop();
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		// TODO Auto-generated method stub
		
	}

}
