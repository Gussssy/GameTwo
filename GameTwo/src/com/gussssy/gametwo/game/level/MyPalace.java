package com.gussssy.gametwo.game.level;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.game.EventManager;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.SoundManager;
import com.gussssy.gametwo.game.objects.npc.BotBot;
import com.gussssy.gametwo.game.objects.npc.NPCActionType;
import com.gussssy.gametwo.game.objects.npc.SmartBotBot;
import com.gussssy.gametwo.game.particles.ElectricEffect;

@SuppressWarnings("unused")
public class MyPalace extends Level{

	public MyPalace(String levelImagePath, String backgroundImagePath, GameManager gm) {
		super(levelImagePath, backgroundImagePath, gm);
		
		
		GameManager.player.setPlayerLocation(95, 479);
		
		SmartBotBot sbb = new SmartBotBot(97,479, gm);
		BotBot bb = new BotBot(97,479, gm);
		sbb.actionType = NPCActionType.PATH;
		gm.addObject(bb);
		
		//SoundManager.growingOfTheWorld.play();
		
		EventManager.addEvent(new ElectricEffect(97*16, 479*16));
		EventManager.addEvent(new ElectricEffect(97*16, 478*16));
		EventManager.addEvent(new ElectricEffect(97*16, 477*16));
		EventManager.addEvent(new ElectricEffect(97*16, 476*16));
		EventManager.addEvent(new ElectricEffect(97*16, 475*16));
		EventManager.addEvent(new ElectricEffect(97*16, 474*16));
		
		
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		// TODO Auto-generated method stub
		
	}

}
