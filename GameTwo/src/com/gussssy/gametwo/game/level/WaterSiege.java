package com.gussssy.gametwo.game.level;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.gfx.Image;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.objects.npc.Goose;
import com.gussssy.gametwo.game.pathfinding.PathFinderTwo;

public class WaterSiege extends Level {
	
	private static Image levelMap = new Image("/level/WaterSiege.png");
	private static Image levelBackground = new Image("/Area5_bg1.png");
	
	Goose goose = new Goose(20,20);
	Goose goose1 = new Goose(20,20);
	Goose goose2 = new Goose(20,20);
	Goose goose3 = new Goose(20,20);
	Goose goose4 = new Goose(20,20);
	Goose goose5 = new Goose(20,20);
	Goose goose6 = new Goose(20,20);

	public WaterSiege( GameManager gm) {
		super(levelMap, levelBackground, gm);
		
		PathFinderTwo.setPathMap(gm, 1,20);
	
		
		gm.addObject(goose);
		gm.addObject(goose1);
		gm.addObject(goose2);
		gm.addObject(goose3);
		gm.addObject(goose4);
		gm.addObject(goose5);
		gm.addObject(goose6);
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		// TODO Auto-generated method stub
		
	}

}
