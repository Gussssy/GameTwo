package com.gussssy.gametwo.game.level;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.game.GameManager;

public class WaterTestMap extends Level{

	public WaterTestMap(String levelImagePath, String backgroundImagePath, GameManager gm) {
		super(levelImagePath, backgroundImagePath, gm);
		
		
		GameManager.player.setPlayerLocation(3, 10);
		
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		// nothing requiring updating for this level
		
	}

}
