package com.gussssy.gametwo.game.level;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.objects.npc.BadBotBot;
import com.gussssy.gametwo.game.objects.npc.BotBot;
import com.gussssy.gametwo.game.objects.npc.Goose;
import com.gussssy.gametwo.game.objects.npc.IceWizard;
import com.gussssy.gametwo.game.objects.npc.NPCSpawner;
import com.gussssy.gametwo.game.objects.npc.Rabbit;
import com.gussssy.gametwo.game.objects.npc.SmartBotBot;
import com.gussssy.gametwo.game.particles.TundraWeather;
import com.gussssy.gametwo.game.pathfinding.PathFinderTwo;

public class IceSpike extends Level {
	
	// By making these static, they can be used to construct the level. Not using them for now.
	//private static Image levelMap = new Image("/level/icespikemap.png");
	//private static Image levelBackground = new Image("/Area5_bg1.png");
	
	
	
	IceWizard wizard = new IceWizard(23, 103);
	IceWizard wizard1 = new IceWizard(25, 103);
	IceWizard wizard2 = new IceWizard(28, 103);
	IceWizard wizard3 = new IceWizard(29, 103);
	IceWizard wizard4 = new IceWizard(35, 103);
	IceWizard wizard5 = new IceWizard(17, 103);
	IceWizard wizard6 = new IceWizard(10, 103);
	IceWizard wizard7 = new IceWizard(21, 103);
	IceWizard wizard8 = new IceWizard(22, 103);
	IceWizard wizard9 = new IceWizard(10, 103);
	IceWizard wizard10 = new IceWizard(11, 103);
	IceWizard wizard11 = new IceWizard(36, 103);
	IceWizard wizard12 = new IceWizard(38, 103);
	IceWizard wizard13 = new IceWizard(36, 103);
	IceWizard wizard14 = new IceWizard(36, 103);
	IceWizard wizard15 = new IceWizard(36, 103);
	
	Goose goose = new Goose(15, 103);
	Goose goose1 = new Goose(15, 103);
	Goose goose2 = new Goose(15, 103);
	
	BotBot bot = new BotBot(15,103,gm);
	BadBotBot badbot = new BadBotBot(15,103);
	SmartBotBot smartbot = new SmartBotBot(15,103,gm);
	
	Rabbit rabbit = new Rabbit(19,103);
	
	
	
	TundraWeather weather = new TundraWeather(gm);
	
	// spawners
	NPCSpawner spawner = new NPCSpawner(83,99, "badbotbot");
	NPCSpawner wizardSpawner = new NPCSpawner(1,100, "ice_wizard");
	NPCSpawner badSpawner = new NPCSpawner(83,99, "badsmartbot");
	
	
	

	public IceSpike(String levelMapPath, String levelBackgroundPath, GameManager gm) {
		//super(levelMap, levelBackground, gm);
		super(levelMapPath, levelBackgroundPath, gm);
		
		
		GameManager.player.setPlayerLocation(20, 103);
		
		//badbot.setTeam(0);
		
		gm.addObject(wizard);
		/**gm.addObject(wizard1);
		gm.addObject(wizard2);
		gm.addObject(wizard3);
		gm.addObject(wizard4);
		gm.addObject(wizard5);
		gm.addObject(wizard6);
		gm.addObject(wizard7);
		gm.addObject(wizard8);
		gm.addObject(wizard9);
		gm.addObject(wizard10);
		gm.addObject(wizard11);
		gm.addObject(wizard12);
		gm.addObject(wizard13);
		gm.addObject(wizard14);
		gm.addObject(wizard15);*/
		//gm.addObject(goose);
		//gm.addObject(goose1);
		//gm.addObject(goose2);
		//gm.addObject(bot);
		//gm.addObject(smartbot);
		gm.addObject(badbot);
		//gm.addObject(rabbit);
		gm.addObject(spawner);
		gm.addObject(wizardSpawner);
		gm.addObject(badSpawner);
		
		PathFinderTwo.setPathMap(gm, 20, 103);
		
		
		
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		//weather.update(gc, gm, dt);

	}

}
