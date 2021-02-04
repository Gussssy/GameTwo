package com.gussssy.gametwo.game.level;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.SoundManager;
import com.gussssy.gametwo.game.objects.npc.NPCActionType;
import com.gussssy.gametwo.game.objects.npc.SmartBotBot;

@SuppressWarnings("unused")
public class MyPalace extends Level{

	public MyPalace(String levelImagePath, String backgroundImagePath, GameManager gm) {
		super(levelImagePath, backgroundImagePath, gm);
		
		
		GameManager.player.setPlayerLocation(95, 450);
		
		SmartBotBot sbb = new SmartBotBot(97,479, gm);
		sbb.actionType = NPCActionType.PATH;
		gm.addObject(sbb);
		
		//SoundManager.growingOfTheWorld.play();
		
		
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		// TODO Auto-generated method stub
		
	}

}
