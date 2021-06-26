package com.gussssy.gametwo.game.level;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.game.EventManager;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.SoundManager;
import com.gussssy.gametwo.game.objects.environment.GameImage;
import com.gussssy.gametwo.game.objects.environment.GameText;
import com.gussssy.gametwo.game.objects.npc.BotBot;
import com.gussssy.gametwo.game.objects.npc.NPCActionType;
import com.gussssy.gametwo.game.objects.npc.NPCSpawner;
import com.gussssy.gametwo.game.objects.npc.SmartBotBot;
import com.gussssy.gametwo.game.particles.ElectricEffect;
import com.gussssy.gametwo.game.pathfinding.PathFinderTwo;

@SuppressWarnings("unused")
public class MyPalace extends Level{
	
	private GameImage trimble = new GameImage("/test/Trimble 320x81 No Halo.png", 1700f, 7480f);
	private GameText text = new GameText(1820f, 7580f);

	public MyPalace(String levelImagePath, String backgroundImagePath, GameManager gm) {
		super(levelImagePath, backgroundImagePath, gm);
		
		
		GameManager.player.setPlayerLocation(95, 479);
		
		SmartBotBot sbb = new SmartBotBot(97,479, gm);
		BotBot bb = new BotBot(97,479, gm);
		sbb.actionType = NPCActionType.PATH;
		gm.addObject(bb);
		
		PathFinderTwo.setPathMap(gm, 97, 479);
		
		
		// Mobs for aesthetics
		gm.addObject(new NPCSpawner(110, 479, "botbot"));
		gm.addObject(new NPCSpawner(115, 479, "botbot"));
		gm.addObject(new NPCSpawner(120, 479, "botbot"));
		gm.addObject(new NPCSpawner(70, 479, "badbotbot"));
		
		
		
		// GameImage testing
		gm.addObject(trimble);
		gm.addObject(text);
		
		
		//SoundManager.growingOfTheWorld.play();
		
		//EventManager.addEvent(new ElectricEffect(97*16, 479*16));
		//EventManager.addEvent(new ElectricEffect(97*16, 478*16));
		//EventManager.addEvent(new ElectricEffect(97*16, 477*16));
		//EventManager.addEvent(new ElectricEffect(97*16, 476*16));
		//EventManager.addEvent(new ElectricEffect(97*16, 475*16));
		//EventManager.addEvent(new ElectricEffect(97*16, 474*16));
		
		EventManager.addEvent(new ElectricEffect(111*16, 468*16));
		EventManager.addEvent(new ElectricEffect(118*16, 471*16));
		EventManager.addEvent(new ElectricEffect(108*16, 471*16));
		EventManager.addEvent(new ElectricEffect(122*16, 468*16));
		EventManager.addEvent(new ElectricEffect(125*16, 470*16));
		EventManager.addEvent(new ElectricEffect(107*16, 469*16));
		
		
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		// TODO Auto-generated method stub
		
	}

}
