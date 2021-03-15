package com.gussssy.gametwo.game.level;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.gfx.Image;
import com.gussssy.gametwo.game.DayNightCycle;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.SoundManager;
import com.gussssy.gametwo.game.TimeObserver;
import com.gussssy.gametwo.game.objects.environment.LampPost;
import com.gussssy.gametwo.game.objects.npc.NPCSpawner;
import com.gussssy.gametwo.game.particles.TundraWeather;
import com.gussssy.gametwo.game.pathfinding.PathFinderTwo;

public class SnowMap1 extends Level implements TimeObserver{
	
	TundraWeather weather; 
	DayNightCycle cycle;
	
	Image nightBackground = new Image("/nightsky_bg.png");
	Image dayBackground = new Image("/Area5_bg1.png");

	public SnowMap1(String levelImagePath, String backgroundImagePath, GameManager gm) {
		super(levelImagePath, backgroundImagePath, gm);
		
		//backgroundImage.setAlpha(true);
		
		// player location
		GameManager.player.setPlayerLocation(9, 42);
		
		// Spawners
		NPCSpawner botbotSpawner = new NPCSpawner(9,42, "botbot");
		botbotSpawner.setMaxSpawns(15);
		//gm.addObject(botbotSpawner);
		
		// 
		NPCSpawner smartbotSpawner = new NPCSpawner(10,42, "smartbot");
		smartbotSpawner.setMaxSpawns(15);
		//gm.addObject(smartbotSpawner);
		
		//
		NPCSpawner wizardSpawner = new NPCSpawner(11,42, "ice_wizard");
		wizardSpawner.setMaxSpawns(2);
		gm.addObject(wizardSpawner);
		
		
		// 
		NPCSpawner badbotSpawner = new NPCSpawner(130,33, "badbotbot");
		badbotSpawner.setMaxSpawns(15);
		//gm.addObject(badbotSpawner);
		
		NPCSpawner badSmartBotSpawner = new NPCSpawner(131,33, "badsmartbot");
		badSmartBotSpawner.setMaxSpawns(2);
		gm.addObject(badSmartBotSpawner);
		
		// 
		
		
		
		// PathMap
		PathFinderTwo.setPathMap(gm, 9, 42);
		
		// Weather
		weather = new TundraWeather(gm);
		
		// Audio
		//SoundManager.introChromeSparks.play();
		
		// Dya/Night
		cycle = new DayNightCycle(gm);
		cycle.addObserver(this);
		
		
		// asorted objects
		gm.addObject(new LampPost(928, 388));
		gm.addObject(new LampPost(608, 596));
		
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		// update the weather
		weather.update(gc, gm, dt);
		
		/// update day/night
		//cycle.update();
		
	}

	
	// Observing day and night 
	@Override
	public void observeChange(boolean night){
		
		System.out.println("NOTIFIED Night: " + night);
		
		if(night){
			levelBackground = nightBackground;
			System.out.println("Set night background");
		}else {
			levelBackground = dayBackground;
			System.out.println("Set day background");
		}
		
		
	}

}
