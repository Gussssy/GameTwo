package com.gussssy.gametwo.game.pathfinding;

import java.awt.event.KeyEvent;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.engine.gfx.Image;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.level.LevelTile;
import com.gussssy.gametwo.game.objects.npc.NPCActionType;

public class PathFindingTester {

	// pathfinding test variables 
	boolean targetSet = false;
	boolean startSet = false;
	int targetX, targetY;
	int startX, startY;

	// Images used to illustrate pathfinding
	private Image goose = new Image("/goose.png");
	//private Image marker = new Image("/marker.png"); // not currently in use 
	//private Image node = new Image("/node.png");		// not currently in use
	private Image flag = new Image("/flag.png");

	// Flag and Goose for viosualizinf path finding
	public boolean showFlag = false;
	int flagTileX = 0;
	int flagTileY = 0;
	int gooseTileX = 0;
	int gooseTileY = 0;


	public void update(GameContainer gc, GameManager gm, float dt){

		// PATH FINDING A STAR ALGORITM TESTING
		// Below are responses to keyboard input of 2,3,4 and 5. Used to set target and stat locations to find a path between
		// KEYDOWN 2:  Set a target location  
		if(gc.getInput().isKeyDown(KeyEvent.VK_2)){


			for(LevelTile tile : gm.getLevelTiles()){
				tile.accessible = false;
				tile.visited = false;
			}

			int x = gc.getInput().getMouseX() + gc.getRenderer().getCamX();
			int y = gc.getInput().getMouseY() + gc.getRenderer().getCamY();
			int tileX = x/GameManager.TS;
			int tileY = y/GameManager.TS;
			targetX = tileX;
			targetY = tileY;
			targetSet = true;

			System.out.println("Setting PathFinding target to: tileX = " + tileX + ", tileY = " + tileY);

		}

		// KEYDOWN 3:  Set a start Location
		if(gc.getInput().isKeyDown(KeyEvent.VK_3)){

			if(targetSet){

				int x = gc.getInput().getMouseX() + gc.getRenderer().getCamX();
				int y = gc.getInput().getMouseY() + gc.getRenderer().getCamY();
				int tileX = x/GameManager.TS;
				int tileY = y/GameManager.TS;
				startX = tileX;
				startY = tileY;
				startSet = true;

				System.out.println("Setting PathFinding start to: tileX = " + tileX + ", tileY = " + tileY);

				PathFinderTwo.initPathFinding(gm, startX, startY, targetX, targetY);

			}else {
				System.out.println("Cant set start of pathfinding untill target is set");
			}	
		}


		// KEYDOWN 4: Perform a single interation of A star pathfinding. Expands the least cost node.
		if(gc.getInput().isKeyDown(KeyEvent.VK_4)){

			if(targetSet && startSet){

				System.out.println("Expanding least cost node: ");
				PathFinderTwo.expandLeastCostNode(gm);

			}else {
				System.out.println("Cant execute pathfinding as either one or both, target and start have not been set");
			}	
		}

		// KEYDOWN 5: Reset pathfinding test. Removes target and start location, set all tiles to accessible. 
		if(gc.getInput().isKeyDown(KeyEvent.VK_5)){

			System.out.println("PathFinding Reset");
			targetSet = false;
			startSet = false;
			gm.setAllTilesAccessible(false);

		}





		

		// Set Test BotBot to path mode (idle behaviour)
		if(gc.getInput().isKeyDown(KeyEvent.VK_9)){

			gm.botbot.actionType = NPCActionType.PATH;
		}



	}




	public void render(GameContainer gc, Renderer r){

		// for illustrating the A Star pathfinding
		if(targetSet)r.drawImage(goose, targetX*GameManager.TS, targetY*GameManager.TS);

		if(showFlag)r.drawImage(flag, flagTileX, flagTileY);

	}

}
