package com.gussssy.gametwo.game.pathfinding;

import java.util.ArrayList;

import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.debug.DebugPanel;
import com.gussssy.gametwo.game.level.LevelTile;
import com.gussssy.gametwo.game.objects.npc.NPC;

public class PathFinder {


	// The movements takern by the current search path
	static ArrayList<String> steps = new ArrayList<String>();

	// The smallest path that has been found
	static ArrayList<String> smallest = new ArrayList<String>();

	private static int minX, maxX, minY, maxY;

	//How many tiles .... how to explain this.... how many tiles extra behind, above and below the npc and the target that will be included in the search area
	private static int areaSize = 10;


	// Variables used to test efficicency of pathfinding
	private static boolean testSpeed = false; 
	private static long duration;
	private static int tests = 0;
	private static long totalDuration;
	private static long averageDuration;
	
	// Print the inner workings of path finding. Will massively slow down path finding. 
	private static boolean print = false; 
	
	// Testing when to give up on path finding, not sure at all what the appropriate value is 
	// max calls is the maximum number of calls before we give up
	// calls is incremented by 1 every time path() is called
	// calls is set to 0 before path is first called
	//static int maxCalls = 3000000;
	//static int maxCalls = 1000000;
	static int maxCalls = 100000;
	static int maxPathLength;
	static int failure = 0;
	static int success = 0;
	static int exceededmaxCalls = 0;
	//static int exceededMaxPathLength = 0;
	static double successRate;
	static boolean pathFound;
	static int calls = 0;
	static boolean stop = false;

	public static Path findPath(GameManager gm, NPC npc, LevelTile targetTile){

		if(print){
			System.out.println("\nFinding a path for " + npc.getTag() + " to get to tile " + targetTile.getTileX() + ", " + targetTile.getTileY());
			System.out.println("Starting PathFinding");
		}
		
		
		// set all tiles to not visited
		setAllTilesToNotVisited(gm);

		// Speed Testing. Log time started pathfinding. 
		if(testSpeed){
			duration = System.nanoTime();
		}

		// maybe test if location is actually reachable
		if(targetTile.isCollision()){
			System.out.println("Target location is not reachable");
			return null;
		}

		// Determine the max pathlength
			// approximate how many steps at most a path should take to get to the target tile
			// 		- setting this to low will lead to no paths being found when there really should be
			//		- setting this too high will lead to no signioficant improvement in performance
		
		// Will try getting the x and y difference and *2 to give the maximum path length... thgis may not be nearly enough... and also could me to much to effect performance
		// how can I assess this...? 
		// count how many failed path finds, record average pathFinding time
		maxPathLength = 4*(Math.abs(npc.getTileX() - targetTile.getTileX()) + Math.abs(npc.getTileY() - targetTile.getTileY()));
		if(print)System.out.println("MaxPathLength: " + maxPathLength);
		
		

		// Determine where the search area begins: 

		// 		on the y axis
		if(npc.getTileX() < targetTile.getTileX()){
			// target is to the right of object
			minX = npc.getTileX() - areaSize;		//startX of search area
			maxX = targetTile.getTileX() + areaSize;
		}else{
			// target is to the left of object
			minX = targetTile.getTileX() - areaSize;
			maxX = npc.getTileX() + areaSize;
		}

		// 		on the x axis
		if(npc.getTileY() < targetTile.getTileY()){
			// target is below object
			minY = npc.getTileY() - areaSize;
			maxY = targetTile.getTileY() + areaSize;
		}else{
			// target is above object
			minY = targetTile.getTileY() - areaSize;
			maxY = npc.getTileY() + areaSize;
		}


		// make sure we only access tiles that exist: 
		if(minX < 0)minX = 0;
		if(minY < 0)minY = 0;

		// Print out current and target location + search area
		// may want this printed to colsole without everything else from pathfinding...
		if(print){
			System.out.println(npc.getTag() + " current location: tileX = " + npc.getTileX() + ", tileY = " + npc.getTileY());
			System.out.println("Target Tile: tileX = " + targetTile.getTileX() + ", tileY= " + targetTile.getTileY());
			System.out.println("Tile X and Y start of search area: tileX = " + minX + ", tileY = " + minY);
			System.out.println("Tile X and Y stop of search area: tileX = " + maxX + ", tileY = " + maxY);
		}
		


		// set calls to 0 to start counting for this pathsearch
		calls = 0;
		stop = false;
		
		// now try find a path: 
		path(gm, npc.getTileX(), npc.getTileY(), targetTile.getTileX(), targetTile.getTileY());
		
		



		// Find time taken for pathfinding, assess success or failure
		if(testSpeed){
			duration = System.nanoTime() - duration;
			totalDuration += duration;
			++tests;
			averageDuration = totalDuration /tests;
			
			// Assess Max Path Length Rule counting success / failures 
			if(pathLength == -1){
				failure ++;
				pathFound = false;
			}else{
				success++;
				pathFound = true;
			}
			
			// determine success rate
			successRate = ((double)success / (double)tests) * 100; 
			
			// print out deets
			System.out.println("\n\n\n Time taken to Path Find: " + duration);
			System.out.println("Target Location: TileX = " + targetTile.getTileX() + ", TileY = " + targetTile.getTileY());
			System.out.println("Start Location: TileX = " + npc.getTileX() + ", TileY = " + npc.getTileY());
			System.out.println("PathFound : " + pathFound );
			System.out.println("PathLength: " + pathLength);
			System.out.println("Average Duration over " + tests + " tests:  " + averageDuration );
			System.out.println("Max Path Length: 2* tileX and tileY diff = " + maxPathLength);
			System.out.println("Total Fails: " + failure);
			System.out.println("Total Success: " + success);
			System.out.println("Success Rate: " + successRate);
			System.out.println("No. Times exceeded max calls: " + exceededmaxCalls);

		}




		// If a path was found, return it.
		if(pathLength != -1){

			// make the new path
			path = new Path(smallest, npc);

			//reset pathfinsing variables for next time
			pathLength = -1;
			steps.clear();
			smallest.clear();

			// return the path we just made
			return path;

		} else {

			// No path was found. Clear steps and return null. 
			steps.clear();
			smallest.clear();
			return null;
		}

	}

	// max of three tiles up possible
	static int tilesUp = 0;
	static boolean canMove = true;
	static boolean hasAirMoved = false;
	static boolean inAir;
	static int pathLength = -1;
	static Path path;
	static boolean canGoUp = false;

	




	public static void path(GameManager gm, int tileX, int tileY, int targetX, int targetY){

		
		
		// if path has been called the maximum number of times, set top to true stop path finding, give up as taking too long!
		if(calls > maxCalls && !stop){
			stop = true;
			System.out.println("pathfinding exceeded max calls. stopping pathfinding");
			exceededmaxCalls++;
		}else {
			calls++;
		}
		
		

		if(print){
			System.out.println("\nPathFinding. tileX: " + tileX + " tileY: " + tileY + "\n steps contents: ");
			System.out.println("Current search path length: " + steps.size());
			System.out.print("[");
			for(String s : steps){
				System.out.print(s + " ");
			}
			System.out.println("]");

		}
		
		// stop pathfinding when it is taking to long
		if(stop){
			return;
		}


		// Print out if a path has been found yet and its size
		if(pathLength != -1){
			if(print)System.out.println("A path of length " + pathLength + " has already been found");
		}else{
			if(print)System.out.println("No path has been found yet");
		}


		// stop extending this node when the path length exceeds the length of a previosuly found path
		if(pathLength != -1 && pathLength <= steps.size()){
			if(print)System.out.println(" A path has already been found that is shorter then the current search path. Terminating this node");
			// XXX ADDED RECENTLY 26/8 1:36am
			if(steps.size() > 0){
				steps.remove(steps.size() -1);
			}
			return;
		}

		
		// stop extending this node when the pathLength exceeds predetermined max length
		
		if(steps.size() > maxPathLength){
			if(print){
				System.out.println("Exceeded predetermined max path length");
			}
			
			// remove the last step
			if(steps.size() > 0){
				steps.remove(steps.size() -1);
			}
			
			// NOTE: do not need to revert this tile to not visted as visted has not yet been set for this tile yet
			
			return;
			
		}
		

		// Reached the target? 
		if((targetX == tileX) && (targetY == tileY)){

			// Reached the target, a new path has been found. 

			if(print){
				System.out.println("\n\n\n\n\n\n\nXXXXXXX Reached target Location XXXXXXXXXXX");
				System.out.println("Number of tiles on this path: " + steps.size());
				DebugPanel.message1 = "PathFound";
				DebugPanel.message2 = "Steps: " + steps.size();
			}



			// Is this the first path we have found? 
			if(pathLength == -1){
				// this is the first path found so far
				if(print)System.out.println("Found the first path. pathlength: " + steps.size());

				// set pathLength to the length of the path so that we don't keep searching for paths once they exceed this length
				pathLength = steps.size();

				// put the contents of steps in smallest
				if(print)System.out.println("Saving this path to smallest.\n Smallest contents: ");
				for(String step : steps){
					smallest.add(step);
					if(print)System.out.println(step);
				}
				if(print)System.out.println("End of smallest (first path saved)");

			}else if(steps.size() < pathLength){
				// Another path has been found, it is either the same size or smaller then the last path found

				if(print)System.out.println("Found a secondary path that was shorter than the previous path. This path size: " + steps.size());
				pathLength = steps.size();

				// put the contents of steps in smallest
				smallest.clear(); //clear smallest first
				for(String step : steps)smallest.add(step);

			}else {

				// Prints the contents of the longer path
				if(print){
					System.out.println("A secondary path was found but it is longer then a previous path. The longer path: ");
					for(String s : steps){
						System.out.print(s + ", ");
					}
					System.out.println("\n\n");

				}

			}

			// if we are returning... we should remove the last step or we have an extra step.... permanently, any new paths are fucked
			//			i havent come across this because my simple test enviroment..... well maybe idk lets see

			// XXX ADDED RECENTLY 26/8 1:36am
			if(steps.size() > 0){
				steps.remove(steps.size() -1);
			}
			return;
		}



		// Outside the search area
		if((tileX < minX) || (tileY < minY) || (tileX > maxX) || (tileY > maxY)){
			//System.out.println("tried to search out of bounds");
			gm.getLevelTile(tileX, tileY).visited = false;
			steps.remove(steps.size()-1);
			if(print)System.out.println("Tried to search out of bounds");
			return;

		}



		// generating null pointers.....
		gm.getLevelTile(tileX, tileY).visited = true;



		// are we on top of a solid tile now?
		// if so, set tilesUp to 0 as we are not travelling up
		if(gm.getLevelTileCollision(tileX, tileY+1)){
			tilesUp = 0;
		}





		//do we have to fall down?
		// we have to fall if:
		//		- there is no collision directly below
		//			AND
		//		- the tile below has not been visited yet

		if(print)System.out.println("\nTrying to go down." + "\tTileX: " + tileX + ", TileY: " + tileY);
		if(!gm.getLevelTileCollision(tileX, tileY + 1) && !gm.getLevelTile(tileX, tileY + 1).visited){
			// the object will fall
			steps.add("down");
			if(print)System.out.println("Going down");
			path(gm, tileX, tileY+1, targetX, targetY); 
		}else{
			if(print)System.out.println("Couldnt go down");
			if(print)System.out.println("There was collsion with below tile:  " + gm.getLevelTileCollision(tileX, tileY + 1) );
			if(print)System.out.println("Below Tile was visited:  " + gm.getLevelTile(tileX, tileY + 1).visited );
		}


		
		
		// MOVE HORIZONTALLY
		if(print)System.out.println("\nTrying to move horizontally" + "\tTileX: " + tileX + ", TileY: " + tileY);
				
		/* if(last was down and not grounded){
		 * 		// cant go left/right as last was down and not grounded
		 * } else if(tried to airmove twice){
		 * 		// cant go left or right as npc has already airmoved
		 * 	} else {
		 * 		// ok can move horizonatlly 
		 * 
		 * 			// HORIZONTAL MOVEMENT CODE
		 * }
		 * 
		 */

		
		// Can the npc go left or right? 
		// a) If the last movement was down, NPC cant go left or right unless on a solid tile
		// b) If the NPC is in the air, if there was already a left/right movement since jumping/up, the NPC cannot move left or right untill on a solid tile
		
		
		// a) Check if last was down and the npc is not grounded, cant go left or right
		if((steps.size() > 1) && steps.get(steps.size()-1) == "down" && !gm.getLevelTileCollision(tileX, tileY+1)){
			
			// last was down and the npc is not grounded
			if(print)System.out.println("NPC last movement was down and NPC is not grounded. Cant go left or right");

		// b) Check if NPC is trying to move left or right for a second time while in the air
		}else if(!gm.getLevelTileCollision(tileX, tileY+1) && steps.size() > 2 && steps.get(steps.size()-2).equals("up") && (steps.get(steps.size()-1).equals("left") || steps.get(steps.size()-1).equals("right"))){
			
			// NPC has already airmoved
			if(print)System.out.println("NPC last has already moved left or right since jumping and is not on a solid tile, cant go left or right again");
		
		
			// a) and b) both false, npc will try to go left/right
		}else {
			
			// NPC can move left or right

			// A* like.. try go in the direction of the target first

			// determine which direction to get to target
			if(targetX < tileX){
				
				// target is located to the left, will try going left first
				if(print)System.out.println("target is to the left, will try go left first");

				// tryLeft
				if(print)System.out.println("Trying to go left" + "\tTileX: " + tileX + ", TileY: " + tileY);
				
				// is left movement possible
				if(!gm.getLevelTileCollision(tileX - 1, tileY) && !gm.getLevelTile(tileX - 1, tileY).visited){
					// right not visited and no collision
					steps.add("left");
					if(print)System.out.println("Going left");
					path(gm, tileX -1, tileY, targetX, targetY);

				}else{
					if(print)System.out.println("couldnt go left");
					if(print)System.out.println("There was collision to the left: " + gm.getLevelTileCollision(tileX - 1, tileY));
					if(print)System.out.println("Left was visited: " + gm.getLevelTile(tileX - 1, tileY).visited);	
				}

				
				
				// left didnt work... try right
				if(print)System.out.println("target is to the left, but going left was unsuccesful. Will try go right");

				// try go right
				if(print)System.out.println("Trying to go right" + "\tTileX: " + tileX + ", TileY: " + tileY);
				
				// is right movement possible? 
				if(!gm.getLevelTileCollision(tileX + 1, tileY) && !gm.getLevelTile(tileX + 1, tileY).visited){
					// left not visited and no collision
					steps.add("right");
					if(print)System.out.println("Going right");
					path(gm, tileX + 1, tileY, targetX, targetY);
				}else {
					if(print)System.out.println("couldnt go right");
					if(print)System.out.println("There was collision to the right: " + gm.getLevelTileCollision(tileX + 1, tileY));
					if(print)System.out.println("Right was visited: " + gm.getLevelTile(tileX + 1, tileY).visited);
				}


			} else {
				
				// targetX is to the right, will try going right first
				if(print)System.out.println("target is to the right, will try go right first");
				
				// try go right
				if(print)System.out.println("Trying to go right" + "\tTileX: " + tileX + ", TileY: " + tileY);
				
				// is right movement possible? 
				if(!gm.getLevelTileCollision(tileX + 1, tileY) && !gm.getLevelTile(tileX + 1, tileY).visited){
					
					// right not visited and no collision
					steps.add("right");
					if(print)System.out.println("Going right");
					path(gm, tileX + 1, tileY, targetX, targetY);
				
				}else {
					if(print)System.out.println("couldnt go right");
					if(print)System.out.println("There was collision to the right: " + gm.getLevelTileCollision(tileX + 1, tileY));
					if(print)System.out.println("Right was visited: " + gm.getLevelTile(tileX + 1, tileY).visited);
				}

				

				// right didnt work, try go left
				if(print)System.out.println("target is to the right, but going right was unsuccesful. Will try go left");

				//try go left
				if(print)System.out.println("Trying to go left" + "\tTileX: " + tileX + ", TileY: " + tileY);
				
				// is left movement possible ? 
				if(!gm.getLevelTileCollision(tileX - 1, tileY) && !gm.getLevelTile(tileX - 1, tileY).visited){
					
					// right not visited and no collision
					steps.add("left");
					if(print)System.out.println("Going left");
					path(gm, tileX -1, tileY, targetX, targetY);

				}else{
					if(print)System.out.println("couldnt go left");
					if(print)System.out.println("There was collision to the left: " + gm.getLevelTileCollision(tileX - 1, tileY));
					if(print)System.out.println("Left was visited: " + gm.getLevelTile(tileX - 1, tileY).visited);	
				}
			}
		}



		/*
		// BROKEN - allows down then horizontal, then down and horizontal again.
		// Check if left or right movement possible
		if(print)System.out.println("\nTrying to move horizontally" + "\tTileX: " + tileX + ", TileY: " + tileY);
		// if there is no collision below (not on a solid tile) && (previous movement was left/right and Up precceeded this, then cant move left or right again untill grounded) 
		// also if last was down, cant go left or right unless grounded
		if(!gm.getLevelTileCollision(tileX, tileY+1) && steps.size() > 2 && steps.get(steps.size()-2).equals("up") && (steps.get(steps.size()-1).equals("left") || steps.get(steps.size()-1).equals("right")) ){
			if(print)System.out.println("Object has airmoved and cant do this again");
		}else{


			// A* like.. try go in the direction of the target first

			// determine which direction to get to target
			if(targetX < tileX){
				// targetX is less than current TileX, try go left
				if(print)System.out.println("target is to the left, will try go left first");

				// tryLeft
				if(print)System.out.println("Trying to go left" + "\tTileX: " + tileX + ", TileY: " + tileY);
				if(!gm.getLevelTileCollision(tileX - 1, tileY) && !gm.getLevelTile(tileX - 1, tileY).visited){
					// right not visited and no collision
					steps.add("left");
					if(print)System.out.println("Going left");
					path(gm, tileX -1, tileY, targetX, targetY);

				}else{
					if(print)System.out.println("couldnt go left");
					if(print)System.out.println("There was collision to the left: " + gm.getLevelTileCollision(tileX - 1, tileY));
					if(print)System.out.println("Left was visited: " + gm.getLevelTile(tileX - 1, tileY).visited);	
				}

				// left didnt work... try right
				if(print)System.out.println("target is to the left, but going left was unsuccesful. Will try go right");

				// try go right
				if(print)System.out.println("Trying to go right" + "\tTileX: " + tileX + ", TileY: " + tileY);
				if(!gm.getLevelTileCollision(tileX + 1, tileY) && !gm.getLevelTile(tileX + 1, tileY).visited){
					// left not visited and no collision
					steps.add("right");
					if(print)System.out.println("Going right");
					path(gm, tileX + 1, tileY, targetX, targetY);
				}else {
					if(print)System.out.println("couldnt go right");
					if(print)System.out.println("There was collision to the right: " + gm.getLevelTileCollision(tileX + 1, tileY));
					if(print)System.out.println("Right was visited: " + gm.getLevelTile(tileX + 1, tileY).visited);
				}


			} else {
				// targetX is to the right of tileX, try right first
				if(print)System.out.println("target is to the right, will try go right first");
				// try go right
				if(print)System.out.println("Trying to go right" + "\tTileX: " + tileX + ", TileY: " + tileY);
				if(!gm.getLevelTileCollision(tileX + 1, tileY) && !gm.getLevelTile(tileX + 1, tileY).visited){
					// left not visited and no collision
					steps.add("right");
					if(print)System.out.println("Going right");
					path(gm, tileX + 1, tileY, targetX, targetY);
				}else {
					if(print)System.out.println("couldnt go right");
					if(print)System.out.println("There was collision to the right: " + gm.getLevelTileCollision(tileX + 1, tileY));
					if(print)System.out.println("Right was visited: " + gm.getLevelTile(tileX + 1, tileY).visited);
				}


				// right didnt work, try go left
				if(print)System.out.println("target is to the right, but going right was unsuccesful. Will try go left");

				//try go left
				if(print)System.out.println("Trying to go left" + "\tTileX: " + tileX + ", TileY: " + tileY);
				if(!gm.getLevelTileCollision(tileX - 1, tileY) && !gm.getLevelTile(tileX - 1, tileY).visited){
					// right not visited and no collision
					steps.add("left");
					if(print)System.out.println("Going left");
					path(gm, tileX -1, tileY, targetX, targetY);

				}else{
					if(print)System.out.println("couldnt go left");
					if(print)System.out.println("There was collision to the left: " + gm.getLevelTileCollision(tileX - 1, tileY));
					if(print)System.out.println("Left was visited: " + gm.getLevelTile(tileX - 1, tileY).visited);	
				}
			}


			//try go left
			if(print)System.out.println("Trying to go left" + "\tTileX: " + tileX + ", TileY: " + tileY);
			if(!gm.getLevelTileCollision(tileX - 1, tileY) && !gm.getLevelTile(tileX - 1, tileY).visited){
				// right not visited and no collision
				steps.add("left");
				if(print)System.out.println("Going left");
				path(gm, tileX -1, tileY, targetX, targetY);

			}else{
				if(print)System.out.println("couldnt go left");
				if(print)System.out.println("There was collision to the left: " + gm.getLevelTileCollision(tileX - 1, tileY));
				if(print)System.out.println("Left was visited: " + gm.getLevelTile(tileX - 1, tileY).visited);	
			}

			// try go right
			if(print)System.out.println("Trying to go right" + "\tTileX: " + tileX + ", TileY: " + tileY);
			if(!gm.getLevelTileCollision(tileX + 1, tileY) && !gm.getLevelTile(tileX + 1, tileY).visited){
				// left not visited and no collision
				steps.add("right");
				if(print)System.out.println("Going right");
				path(gm, tileX + 1, tileY, targetX, targetY);
			}else {
				if(print)System.out.println("couldnt go right");
				if(print)System.out.println("There was collision to the right: " + gm.getLevelTileCollision(tileX + 1, tileY));
				if(print)System.out.println("Right was visited: " + gm.getLevelTile(tileX + 1, tileY).visited);
			}
		}*/




		// MOVE UP ? 
		// cant go left or right, arent falling, so try jump..? 

		//if(print)System.out.println("\nXXXXXXXXXXX About to attempt to go up. TileX = " + tileX + "TileY = " + tileY);
		//if(print)System.out.println("Steps Contents: " + steps.toString());


		// COUNTING UPS
		// read the path backwards and count the number of consecutives ups, if we see three in a row, we cant go up.
		tilesUp = 0;
		for(int i = steps.size()-1; i >= 0; i--){

			// was the previous step up?
			if(steps.get(i) == "up"){
				// yes it was
				tilesUp++;
			}else{
				// no it wasnt, exit the loop
				break;
			}
		}

		// finished counting tilesUp, so can we go up? 
		//System.out.println("Tiles Up: " + tilesUp);
		if(tilesUp >= 3){
			canGoUp = false;
			//if(print)System.out.println("Cant go up");
		}else{
			//if(print)System.out.println("Can go up");
			canGoUp = true;
		}

		if(print)System.out.println("\nTrying to go up" + "\tTileX: " + tileX + ", TileY: " + tileY);
		// try go up, but can only go up if, a) above is not visited, b) above is not solid, c) below is solid to jump off OR npc mid jump d) tilesUp less then 3
		if((!gm.getLevelTile(tileX, tileY-1).visited) && !gm.getLevelTileCollision(tileX, tileY-1) && (gm.getLevelTileCollision(tileX, tileY+1) || tilesUp != 0 ) && canGoUp ){
			//  can go up
			steps.add("up");

			// path up 
			if(print)System.out.println("Going Up");
			path(gm, tileX, tileY-1, targetX, targetY);

		}else{
			if(print)System.out.println("couldnt go up");
			if(print)System.out.println("Above was visited: " + gm.getLevelTile(tileX, tileY-1).visited);
			if(print)System.out.println("There was collision with the above tile: " + gm.getLevelTileCollision(tileX, tileY -1));
			if(print)System.out.println("Not on a solid tile to jump and not mid jump: " + !(gm.getLevelTileCollision(tileX, tileY+1) || tilesUp != 0 ) );
			if(print)System.out.println("TilesUP: " + tilesUp + ", step contents: " + steps.toString());
			if(print)System.out.println("Tried to go up more then 3 tiles: " + !canGoUp);
		}




		// Can't go anywhere from this position
		// remove the last step
		if(steps.size() > 0){
			steps.remove(steps.size() -1);
		}
		gm.getLevelTile(tileX, tileY).visited = false;

	}
	
	
	
	public static void setPathMap(GameManager gm, int tileX, int tileY){
		
		if(print){
			System.out.println("Path Mapping");
			System.out.println("\n\n\n Starting PathMapping");
		}
		

		//PathFinder.object = object;

		// Speed Testing. Log time started pathfinding. 
		if(testSpeed){
			duration = System.nanoTime();
		}

		



		// Define search area: the entire level 
		minX = 0;
		maxX = gm.getLevelWidth();
		minY = 0;
		maxY = gm.getLevelHeight();

		
		// Print out current and target location + search area
		if(print){
			System.out.println("Determining path map for level");
			System.out.println("Start location: tileX = " + tileX + ", tileY= " + tileY);
			System.out.println("Tile X and Y start of search area: tileX = " + minX + ", tileY = " + minY);
			System.out.println("Tile X and Y stop of search area: tileX = " + maxX + ", tileY = " + maxY);
		}
		


		// set calls to 0 to start counting for this pathsearch
		calls = 0;
		stop = false;
		
		// now try find a path: 
		pathMap(gm, tileX, tileY);
		
		



		// Find time taken for pathfinding
		if(testSpeed){
			duration = System.nanoTime() - duration;
			totalDuration += duration;
			++tests;
			averageDuration = totalDuration /tests;
			System.out.println("\n\n\n Time taken to Path Map: " + duration);
			//System.out.println("Average Duration over " + tests + " tests:  " + averageDuration );

		}
		
	}
	
	public static void pathMap(GameManager gm, int tileX, int tileY){

		gm.getLevelTile(tileX, tileY).accessible = true;

		if(print){
			System.out.println("\nPathMapping. tileX: " + tileX + " tileY: " + tileY + "\n steps contents: ");
			System.out.println("Current search path length: " + steps.size());
			System.out.print("[");
			for(String s : steps){
				System.out.print(s + " ");
			}
			System.out.println("]");

		}
		

		// Outside the search area
		if((tileX < minX) || (tileY < minY) || (tileX > maxX) || (tileY > maxY)){
			//System.out.println("tried to search out of bounds");
			gm.getLevelTile(tileX, tileY).visited = false;
			steps.remove(steps.size()-1);
			if(print)System.out.println("Tried to search out of bounds");
			return;

		}



		gm.getLevelTile(tileX, tileY).visited = true;



		// are we on top of a solid tile now?
		// if so, set tilesUp to 0 as we are not travelling up
		if(gm.getLevelTileCollision(tileX, tileY+1)){
			tilesUp = 0;
		}





		//do we have to fall down?
		// we have to fall if:
		//		- there is no collision directly below
		//			AND
		//		- the tile below has not been visited yet

		if(print)System.out.println("\nTrying to go down." + "\tTileX: " + tileX + ", TileY: " + tileY);
		if(!gm.getLevelTileCollision(tileX, tileY + 1) && !gm.getLevelTile(tileX, tileY + 1).visited){
			// the object will fall
			steps.add("down");
			if(print)System.out.println("Going down");
			pathMap(gm, tileX, tileY+1); 
		}else{
			if(print)System.out.println("Couldnt go down");
			if(print)System.out.println("There was collsion with below tile:  " + gm.getLevelTileCollision(tileX, tileY + 1) );
			if(print)System.out.println("Below Tile was visited:  " + gm.getLevelTile(tileX, tileY + 1).visited );
		}


		
		
		// MOVE HORIZONTALLY
		if(print)System.out.println("\nTrying to move horizontally" + "\tTileX: " + tileX + ", TileY: " + tileY);
				
		/* if(last was down and not grounded){
		 * 		// cant go left/right as last was down and not grounded
		 * } else if(tried to airmove twice){
		 * 		// cant go left or right as npc has already airmoved
		 * 	} else {
		 * 		// ok can move horizonatlly 
		 * 
		 * 			// HORIZONTAL MOVEMENT CODE
		 * }
		 * 
		 */

		
		// Can the npc go left or right? 
		// a) If the last movement was down, NPC cant go left or right unless on a solid tile
		// b) If the NPC is in the air, if there was already a left/right movement since jumping/up, the NPC cannot move left or right untill on a solid tile
		
		
		// a) Check if last was down and the npc is not grounded, cant go left or right
		if((steps.size() > 1) && steps.get(steps.size()-1) == "down" && !gm.getLevelTileCollision(tileX, tileY+1)){
			
			// last was down and the npc is not grounded
			if(print)System.out.println("NPC last movement was down and NPC is not grounded. Cant go left or right");

		// b) Check if NPC is trying to move left or right for a second time while in the air
		}else if(!gm.getLevelTileCollision(tileX, tileY+1) && steps.size() > 2 && steps.get(steps.size()-2).equals("up") && (steps.get(steps.size()-1).equals("left") || steps.get(steps.size()-1).equals("right"))){
			
			// NPC has already airmoved
			if(print)System.out.println("NPC last has already moved left or right since jumping and is not on a solid tile, cant go left or right again");
		
		
			// a) and b) both false, npc will try to go left/right
		}else {
			
			// NPC can move left or right		

				// target is located to the left, will try going left first
				if(print)System.out.println("target is to the left, will try go left first");

				// tryLeft
				if(print)System.out.println("Trying to go left" + "\tTileX: " + tileX + ", TileY: " + tileY);
				
				// is left movement possible
				if(!gm.getLevelTileCollision(tileX - 1, tileY) && !gm.getLevelTile(tileX - 1, tileY).visited){
					// right not visited and no collision
					steps.add("left");
					if(print)System.out.println("Going left");
					pathMap(gm, tileX -1, tileY);

				}else{
					if(print)System.out.println("couldnt go left");
					if(print)System.out.println("There was collision to the left: " + gm.getLevelTileCollision(tileX - 1, tileY));
					if(print)System.out.println("Left was visited: " + gm.getLevelTile(tileX - 1, tileY).visited);	
				}

				
				
				// left didnt work... try right
				if(print)System.out.println("target is to the left, but going left was unsuccesful. Will try go right");

				// try go right
				if(print)System.out.println("Trying to go right" + "\tTileX: " + tileX + ", TileY: " + tileY);
				
				// is right movement possible? 
				if(!gm.getLevelTileCollision(tileX + 1, tileY) && !gm.getLevelTile(tileX + 1, tileY).visited){
					// left not visited and no collision
					steps.add("right");
					if(print)System.out.println("Going right");
					pathMap(gm, tileX + 1, tileY);
				}else {
					if(print)System.out.println("couldnt go right");
					if(print)System.out.println("There was collision to the right: " + gm.getLevelTileCollision(tileX + 1, tileY));
					if(print)System.out.println("Right was visited: " + gm.getLevelTile(tileX + 1, tileY).visited);
				}
			
		}


		// MOVE UP ? 
		// cant go left or right, arent falling, so try jump..? 

		//if(print)System.out.println("\nXXXXXXXXXXX About to attempt to go up. TileX = " + tileX + "TileY = " + tileY);
		//if(print)System.out.println("Steps Contents: " + steps.toString());


		// COUNTING UPS
		// read the path backwards and count the number of consecutives ups, if we see three in a row, we cant go up.
		tilesUp = 0;
		for(int i = steps.size()-1; i >= 0; i--){

			// was the previous step up?
			if(steps.get(i) == "up"){
				// yes it was
				tilesUp++;
			}else{
				// no it wasnt, exit the loop
				break;
			}
		}

		// finished counting tilesUp, so can we go up? 
		//System.out.println("Tiles Up: " + tilesUp);
		if(tilesUp >= 3){
			canGoUp = false;
			//if(print)System.out.println("Cant go up");
		}else{
			//if(print)System.out.println("Can go up");
			canGoUp = true;
		}

		if(print)System.out.println("\nTrying to go up" + "\tTileX: " + tileX + ", TileY: " + tileY);
		// try go up, but can only go up if, a) above is not visited, b) above is not solid, c) below is solid to jump off OR npc mid jump d) tilesUp less then 3
		if((!gm.getLevelTile(tileX, tileY-1).visited) && !gm.getLevelTileCollision(tileX, tileY-1) && (gm.getLevelTileCollision(tileX, tileY+1) || tilesUp != 0 ) && canGoUp ){
			//  can go up
			steps.add("up");

			// path up 
			if(print)System.out.println("Going Up");
			pathMap(gm, tileX, tileY-1);

		}else{
			if(print)System.out.println("couldnt go up");
			if(print)System.out.println("Above was visited: " + gm.getLevelTile(tileX, tileY-1).visited);
			if(print)System.out.println("There was collision with the above tile: " + gm.getLevelTileCollision(tileX, tileY -1));
			if(print)System.out.println("Not on a solid tile to jump and not mid jump: " + !(gm.getLevelTileCollision(tileX, tileY+1) || tilesUp != 0 ) );
			if(print)System.out.println("TilesUP: " + tilesUp + ", step contents: " + steps.toString());
			if(print)System.out.println("Tried to go up more then 3 tiles: " + !canGoUp);
		}




		// Can't go anywhere from this position
		// remove the last step
		if(steps.size() > 0){
			steps.remove(steps.size() -1);
		}
		// dont want to set visited to false or this tile will be visited again
		//gm.getLevelTile(tileX, tileY).visited = false;

	}
	
	public static void setAllTilesToNotVisited(GameManager gm){
		for(LevelTile tile : gm.getLevelTiles()){
			tile.visited = false;
		}
	}


}
