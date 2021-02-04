package com.gussssy.gametwo.game.level;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.gfx.Image;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.objects.npc.BotBot;
import com.gussssy.gametwo.game.objects.npc.IceWizard;
import com.gussssy.gametwo.game.objects.npc.Rabbit;
import com.gussssy.gametwo.game.pathfinding.PathFinderTwo;

public class RabbitHills extends Level {
	
	private static Image levelMap = new Image("/level/rabbit_hill.png");
	private static Image levelBackground = new Image("/Area5_bg1.png");

	Rabbit rabbit = new Rabbit(15, 20);
	Rabbit rabbit1 = new Rabbit(15, 20);
	Rabbit rabbit2 = new Rabbit(15, 20);
	Rabbit rabbit3 = new Rabbit(15, 20);
	Rabbit rabbit4 = new Rabbit(15, 20);
	Rabbit rabbit5 = new Rabbit(15, 20);
	Rabbit rabbit6 = new Rabbit(15, 20);
	Rabbit rabbit7 = new Rabbit(15, 20);
	Rabbit rabbit8 = new Rabbit(15, 20);
	
	
	Rabbit rabbit9 = new Rabbit(89, 41);
	Rabbit rabbit10 = new Rabbit(89, 41);
	Rabbit rabbit11 = new Rabbit(89, 41);
	Rabbit rabbit12 = new Rabbit(89, 41);
	Rabbit rabbit13 = new Rabbit(89, 41);
	Rabbit rabbit14 = new Rabbit(89, 41);
	Rabbit rabbit15 = new Rabbit(89, 41);
	Rabbit rabbit16 = new Rabbit(89, 41);
	Rabbit rabbit17 = new Rabbit(89, 41);
	Rabbit rabbit18 = new Rabbit(89, 41);
	Rabbit rabbit19 = new Rabbit(89, 41);
	Rabbit rabbit20 = new Rabbit(89, 41);
	Rabbit rabbit21 = new Rabbit(89, 41);
	
	Rabbit rabbit22 = new Rabbit(89, 41);
	Rabbit rabbit23 = new Rabbit(89, 41);
	Rabbit rabbit24 = new Rabbit(89, 41);
	Rabbit rabbit25 = new Rabbit(89, 41);
	Rabbit rabbit26 = new Rabbit(89, 41);
	Rabbit rabbit27 = new Rabbit(89, 41);
	Rabbit rabbit28 = new Rabbit(89, 41);
	Rabbit rabbit29 = new Rabbit(89, 41);
	
	IceWizard wizard = new IceWizard(25, 10);
	
	BotBot bot = new BotBot(25,10,gm);
	


	
	
	
	
	
	public RabbitHills(GameManager gm) {
		super(levelMap, levelBackground, gm);
		
		// tile modification just for this..
		for(LevelTile tile : gm.getLevelTiles()){
			if(tile.getType() == 5){
				// this tile is a log, turn off collision
				tile.setCollision(false);
			}
		}
		
		
		
		PathFinderTwo.setPathMap(gm, 4, 46);
		
		gm.addObject(rabbit);
		gm.addObject(rabbit1);
		gm.addObject(rabbit2);
		gm.addObject(rabbit3);
		gm.addObject(rabbit4);
		gm.addObject(rabbit5);
		gm.addObject(rabbit6);
		gm.addObject(rabbit7);
		gm.addObject(rabbit8);
		gm.addObject(rabbit9);
		gm.addObject(rabbit10);
		gm.addObject(rabbit11);
		gm.addObject(rabbit12);
		gm.addObject(rabbit13);
		gm.addObject(rabbit14);
		gm.addObject(rabbit15);
		gm.addObject(rabbit16);
		gm.addObject(rabbit17);
		gm.addObject(rabbit18);
		gm.addObject(rabbit19);
		gm.addObject(rabbit20);
		gm.addObject(rabbit21);
		gm.addObject(rabbit22);
		gm.addObject(rabbit23);
		gm.addObject(rabbit24);
		gm.addObject(rabbit25);
		gm.addObject(rabbit26);
		gm.addObject(rabbit27);
		gm.addObject(rabbit28);
		gm.addObject(rabbit29);
		//gm.addObject(wizard);
		gm.addObject(bot);
		
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		
	}

}
