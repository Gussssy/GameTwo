package com.gussssy.gametwo.game.level;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.gfx.Image;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.objects.npc.BotBot;
import com.gussssy.gametwo.game.objects.npc.Goose;
import com.gussssy.gametwo.game.objects.npc.Rabbit;
import com.gussssy.gametwo.game.pathfinding.PathFinderTwo;

public class FlatMap extends Level{
	
	private static Image levelMap = new Image("/level/flat_map.png");
	private static Image levelBackground = new Image("/Area5_bg1.png");
	
	private BotBot bot = new BotBot(0,11,gm);
	private Rabbit rabbit = new Rabbit(0,11);
	Goose goose = new Goose(0,11);
	

	public FlatMap(GameManager gm) {
		super(levelMap, levelBackground, gm);
		
		PathFinderTwo.setPathMap(gm, 0, 11);
		
		//gm.addObject(bot);
		//gm.addObject(rabbit);
		//gm.addObject(goose);
		
		
		
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		// TODO Auto-generated method stub
		
	}

}
