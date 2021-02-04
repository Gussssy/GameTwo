package com.gussssy.gametwo.game.level;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.game.DayNightCycle;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.SoundManager;
import com.gussssy.gametwo.game.debug.DebugPanel;
import com.gussssy.gametwo.game.objects.environment.Platform;
import com.gussssy.gametwo.game.objects.npc.BadBotBot;
import com.gussssy.gametwo.game.objects.npc.BotBot;
import com.gussssy.gametwo.game.objects.npc.Goose;
import com.gussssy.gametwo.game.objects.npc.NPCActionType;
import com.gussssy.gametwo.game.objects.npc.SmartBotBot;
import com.gussssy.gametwo.game.objects.projectile.Grenade;
import com.gussssy.gametwo.game.objects.testObjects.CollisionTesterObject;
import com.gussssy.gametwo.game.objects.testObjects.ProjectileLauncher;
import com.gussssy.gametwo.game.pathfinding.PathFinderTwo;

/**
 * A Map used for testing various collison detection systems. 
 */
public class CollisionWorld extends Level {
	
	DayNightCycle cycle;

	public CollisionWorld(String levelImagePath, String backgroundImagePath, GameManager gm) {
		super(levelImagePath, backgroundImagePath, gm);
		
		
		// debug settings
		//GameManager.showDebug = true;
		DebugPanel.showTileCollision = true;
		
		// collision test object
		gm.addObject(new CollisionTesterObject(5,5, gm));
		
		// player
		GameManager.player.setPlayerLocation(7, 47);
		//GameManager.player.setPlayerLocation(2652.0002f, 196.6666f);
		//GameManager.player.setPlayerLocation(2712f, 228.9998f);
		//GameManager.player.setPlayerLocation(2747.0f, 198.9998f);
		GameManager.player.setDirection(1);
		GameManager.player.freeMovementEnabled = true;
		//GameManager.player.
		
		// a friendly bot bot
		BotBot botbot = new BotBot(7,47,gm);
		botbot.actionType = NPCActionType.WAIT;
		gm.addObject(botbot);
		gm.addObject(new SmartBotBot(7,47,gm));
		gm.addObject(new BadBotBot(7,47));
		gm.addObject(new Goose(7,47));
		
		
		// path mapping for npcs
		PathFinderTwo.setPathMap(gm, 7, 47);
		
		// platforms
		gm.addObject(new Platform(11, 40));
		gm.addObject(new Platform(19, 40));
		gm.addObject(new Platform(13, 48));
		gm.addObject(new Platform(16, 46));
		
		// projectile launcher
		gm.addObject(new ProjectileLauncher(167,13,155,179,4,27));
		
		//SoundManager.introChromeSparks.play();
		//SoundManager.onlyTime.play();
		
		cycle = new DayNightCycle(gm);
		
		// grenade modification
		Grenade.setMaxBeeps(3000);
		Grenade.sound = false;
		
		
		
		
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		cycle.update();

	}

}
