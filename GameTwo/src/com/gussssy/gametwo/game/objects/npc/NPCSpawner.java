package com.gussssy.gametwo.game.objects.npc;

import java.util.ArrayList;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.engine.gfx.Image;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.SoundManager;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.debug.DebugPanel;
import com.gussssy.gametwo.game.objects.GameObject;

@SuppressWarnings("unused")
public class NPCSpawner extends GameObject{
	
	private Image spawnerImage = new Image("/Spawner1.png");
	private String npcTag;
	private int maxSpawns = 20;
	private int activeSpawns = 0;
	//private int cooldown = 180;
	private int cooldown = 30;
	private boolean onCooldown = false;
	private int counter = cooldown;
	private ArrayList<NPC> spawns = new ArrayList<NPC>();
	
	public NPCSpawner(int tileX, int tileY, String npcTag){
		
		this.tileX = tileX;
		this.tileY = tileY;
		this.npcTag = npcTag;
		this.tag = npcTag + "_spawner";
		
		
		
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		DebugPanel.message1 = "Spawns: " + activeSpawns;
		
		// remove any dead spawned npcs from
		for(int i = spawns.size()-1; i >= 0 ; i-- ){
			if(spawns.get(i).dead){
				spawns.remove(i);
				activeSpawns--;
			}
		}
		
		if(onCooldown){
			
			if(counter == 0){
				onCooldown = false;
			}else {
				counter--;
				return;
			}
		}
		
		// not on cooldown, should a NPC be spawned?
		if(activeSpawns < maxSpawns){
			// can spawn more
			spawn(gm);
		}else{
			//spawned max number 
		}
		
		
		
	}

	@Override
	public void render(Renderer r) {
		
		// -3 on y axis because the spawner image is 16*19 pixels, need to move up by 3
		r.drawImage(spawnerImage, (int)tileX * GameManager.TS, (int)tileY * GameManager.TS -3);
		
	}
	
	
	private void spawn(GameManager gm){
		
		//System.out.println("About to spawn at: " + tileX);
		
		activeSpawns ++;
		onCooldown = true;
		counter = cooldown;
		
		switch(npcTag){
		case "botbot":
			BotBot spawnBot = new BotBot(tileX, tileY, gm);
			gm.addObject(spawnBot);
			spawns.add(spawnBot);
			break;
		case "badbotbot":
			BadBotBot spawnBad = new BadBotBot(tileX, tileY);
			gm.addObject(spawnBad);
			spawns.add(spawnBad);
			break;
		case "goose":
			Goose spawnGoose = new Goose(tileX, tileY);
			gm.addObject(spawnGoose);
			spawns.add(spawnGoose);
			break;
		case"smartbot":
			SmartBotBot smartBotBot = new SmartBotBot(tileX, tileY, gm);
			gm.addObject(smartBotBot);
			spawns.add(smartBotBot);
			break;
			
		case "badsmartbot":
			SmartBotBot badSmartBotBot = new SmartBotBot(tileX, tileY, gm);
			badSmartBotBot.setTeam(1);
			gm.addObject(badSmartBotBot);
			spawns.add(badSmartBotBot);
			break;
		case "ice_wizard" : 
			IceWizard i = new IceWizard(tileX, tileY);
			gm.addObject(i);
			spawns.add(i);
			break;
			
			
		}
		
		//SoundManager.teleport.play();
		
	}
	
	
	
	
	
	
	
	

	@Override
	public void collision(GameObject other, AABBComponent otherHitBox) {
		// TODO Auto-generated method stub
		
	}

	public void setMaxSpawns(int maxSpawns) {
		this.maxSpawns = maxSpawns;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}
	
	
	
	

}
