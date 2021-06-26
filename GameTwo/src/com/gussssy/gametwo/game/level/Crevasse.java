package com.gussssy.gametwo.game.level;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.gfx.Image;
import com.gussssy.gametwo.game.EventManager;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.objects.environment.GameImage;
import com.gussssy.gametwo.game.objects.environment.GameText;
import com.gussssy.gametwo.game.particles.ElectricEffect;
import com.gussssy.gametwo.game.particles.FlameEffectOne;

public class Crevasse extends Level {
	
	private static Image levelMap = new Image("/level/Crevasse.png");
	private static Image levelBackground = new Image("/Area5_bg1.png");
	
	//private GameImage trimble = new GameImage("/test/Trimble 320x81 No Halo.png", 1700f, 7480f);
	private GameImage trimble = new GameImage("/test/Trimble 320x81 No Halo.png", 550f, 700f);
	private GameText text = new GameText(680f, 820f);
	
	
	public Crevasse(GameManager gm) {
		super(levelMap, levelBackground, gm);
		
		
		// GameImage testing
		gm.addObject(trimble);
		gm.addObject(text);
		
		
		// set players spawn location
		setPlayerLocation(47,65);
		
		gm.camera.setAdjustmentY(-100);
		
		//FlameEffectOne flame = new FlameEffectOne();
		//EventManager.addEvent(flame);
		
		FlameEffectOne flame = new FlameEffectOne(600,650);
		FlameEffectOne flame2 = new FlameEffectOne(816,866);
		
		EventManager.addEvent(flame);
		EventManager.addEvent(flame2);
		
		//EventManager.addEvent(new ElectricEffect(47*16, 60*16));
		//EventManager.addEvent(new ElectricEffect(47*16, 60*16));
		//EventManager.addEvent(new ElectricEffect(47*16, 60*16));
		//EventManager.addEvent(new ElectricEffect(47*16, 60*16));
		//EventManager.addEvent(new ElectricEffect(47*16, 60*16));
		//EventManager.addEvent(new ElectricEffect(47*16, 60*16));
	
		
		
	}


	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		// TODO Auto-generated method stub

	}

}
