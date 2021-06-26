package com.gussssy.gametwo.game.level;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.gfx.Image;
import com.gussssy.gametwo.game.GameManager;

public class PathFindingMap extends Level {
	
	private static Image levelMap = new Image("/Arena2.png");
	private static Image levelBackground = new Image("/Area5_bg1.png");

	public PathFindingMap(GameManager gm) {
		super(levelMap, levelBackground, gm);
		
		GameManager.renderPathFinding = true;
		
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		// TODO Auto-generated method stub
		
	}

}
