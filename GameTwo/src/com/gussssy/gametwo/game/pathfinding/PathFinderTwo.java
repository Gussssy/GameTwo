package com.gussssy.gametwo.game.pathfinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.level.LevelTile;
import com.gussssy.gametwo.game.objects.npc.NPC;

public class PathFinderTwo {
	
	static boolean print = false;
	static boolean printInfo = false;

	//static ArrayList<Movement> currentPath = new ArrayList<Movement>();
	static ArrayList<PathNode> nodes = new ArrayList<PathNode>();
	static ArrayList<PathNode> usedNodes = new ArrayList<PathNode>();	// hold all used nodes so that they can be deinitiated to prevent reference cycyle memory issues
	static PathNode currentNode = null;
	static ArrayList<PathNode> newNodes = new ArrayList<PathNode>();
	
	static ArrayList<FailedPathFind> failedPathFinds = new ArrayList<FailedPathFind>();
	
	
	// Variables set by findPath();
	static int targetX, targetY;
	static int startX, startY;
	static int iterations = 0;
	static NPC npc;
	

	// booleans controlling terminations - end conditon of path finding.
	static boolean pathFound = false;
	static boolean pathNotFound = false;
	
	
	// TESTING
	static int totalIterations = 0;
	static int pathsFound = 0;
	static int maxIterationsForSuccess = 0;
	static int maxIterationsForFail = 0;
	static int fails = 0;
	
	

	public static Path findPath(GameManager gm, NPC npc, int startX, int startY, int targetX, int targetY){
		
		// setup stuff
		nodes.clear();
		//path.clear();
		usedNodes.clear();
		currentNode = null;
		iterations = 0;
		PathFinderTwo.targetX = targetX; 
		PathFinderTwo.targetY = targetY;
		PathFinderTwo.startX = startX; 
		PathFinderTwo.startY = startY;
		PathFinderTwo.npc = npc;
		pathFound = false;
		pathNotFound = false;
		gm.setAllTilesVisited(false);
		
		
		
		
		////////////////////////////////////  Dealing with silly erroneous pathfinding attempts /////////////////////////
		
		
		// make sure we arent pathing to a solid tile e.g a tile we cant acutally get too
		if(gm.getLevelTileCollision(targetX, targetY)){
			
				System.out.println("PathFinderTwo ERROR: Cant path to a solid tile");
				System.out.println("Target Tile: x: " + targetX + ",  y: "  + targetY);
			
			return null;
		}
		
		// Dealing with pathing to 'mid air' tiles: find the ground below and path to that.
		if(!gm.getLevelTileCollision(targetX, targetY+1)){
			System.out.println("Attemped to path to a tile with nothing solid below it, that is not currently possibe, will modify target location");
			
			boolean foundGround = false;
			int tilesDown = 2;
			
			while(!foundGround){
				
				if(gm.getLevelTileCollision(targetX, targetY + tilesDown)){
					// found a solid tile below, path to targetY + tilesDown -1
					System.out.println("Have found a suitable tile to path too: target Y = " + (targetY + tilesDown -1));
					foundGround = true;
					
					// set new targetY
					targetY = targetY + tilesDown -1;
					
				}else{
					tilesDown++;
				}	
			}
		}
		
		// dont path to the current location... it will make errors and it shouldnt be occuring
		if(startX == targetX && startY == targetY){
			System.out.println("Tried to path to start location. No pathfinding required");
			return null;
		}
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		// start pathing
		
		// Expand first node
		
		// get possible moves .... the tiles we can travel too
		if(print)System.out.println("PathFinder.findpath: Getting nodes from start position");
		nodes = getReachableNodes(gm, startX, startY, null);
				
		// increment interations to 1
		iterations++;
				
		// mark the start tile/node as visited, it has been expanded.
		gm.getLevelTile(startX, startY).visited = true;
				
		// get the cost of moving to these tiles/nodes
		estimateCost(nodes, targetX, targetY, 0);
				
		// order by this cost 
		sortMovesByCost(nodes);
		
		if(print){
			System.out.println("PathFinder.findPath: About to enter recursion. Printing the nodes we got from the start tile:");
			for(PathNode node : nodes){System.out.println(node.toString());}
			System.out.println("\n\n");
		}
		
		
		
		// RECURSIVE BIT: 
		if(print)System.out.println("PathFinder.findpath: Entering recursive pathfinding...");
		//System.out.println("pathFound: " + pathFound);
		
		//  
		while(!pathFound && !pathNotFound){
			if(print)System.out.println("\n\n\n*** NEW LOOP ITERATION ***Calling Expand. Iterations: " + iterations);
			expandLeastCostNode(gm);
			if(print)System.out.println("\nEnd of this expansion, printing what we got:");
			
			
			// Print the booleans
			if(print){
				System.out.println("\n\n (a) State of booleans: ");
				System.out.println("\tpathFound: " + pathFound);
				System.out.println("\tpathNotFound: " + pathNotFound);
			}
			
			
			// This shouldnt be required anymore, maybe test this.
			/**if(pathNotFound){
				System.out.println("Ending Recursion becuase a path cannot be found");
				break;
			}*/
			
			if(print){
				System.out.println("\n\n (b) Queue Contents so far:");
				System.out.println("\t Size: " + nodes.size());
				for(PathNode node : nodes){
					System.out.println(node.toString());
				}
				System.out.println("End of Queue");
			}


			// Print the path so far 
			if(print){
				System.out.println("\n\n (c) Path Followed so far: ");
				PathNode temp = currentNode;
				System.out.println(temp.movement);
				while(temp.previousNode != null){

					// Print last movement
					System.out.println(temp.previousNode.movement);

					// Now set the current node to be the preccesding node to continue reading backwards
					temp = temp.previousNode;	
				}
				System.out.println("Start.\n\n\n");
			}


		}

		if(print)System.out.println("PathFinder.findpath: Finished Recursive pathfinding");

		if(pathFound){
			if(print)System.out.println("PATHFINDER: A path was found. \n\t targetx: " + targetX + ", targetY: " + targetY + "\n\t startX: " + startX + "\n\t startY: " + startY );
			
			ArrayList<PathNode> path = new ArrayList<PathNode>();
			
			
			// Read backwards through the nodes to find the path that was taken: 
			
			// Start by adding the last expanded node to the path. The final node in the path.
			path.add(currentNode);
			
			// Now loop backwards through the nodes that lead to a succesful path
			//		add the previous node to the start of the list
			//				then set the currentNode to the previousNode
			//					continue untill we find the start node whose previous node is null
			while(currentNode.previousNode != null){
				
				// Add the current nodes preceeding node to the start of the list
				path.add(0, currentNode.previousNode);
				
				// Now set the current node to be the preccesding node to continue reading backwards
				currentNode = currentNode.previousNode;
			}
				
				
			if(print){
				System.out.println("Printing the succesful path:");
				for(PathNode node : path){
					System.out.println(node.toString());
				}
			}
				
				
				
				
				

				if(printInfo){
					totalIterations += iterations;
					pathsFound++;
					if(maxIterationsForSuccess < iterations)maxIterationsForSuccess = iterations;

					System.out.println("\n\n\n--> --> PATHFINDER INFO <-- <--");
					System.out.println("\n\t - - - Attempt Successful - - -");
					if(lastSuccess)
						System.out.println("\n\t - - - Last Attempt Successful - - -");
					else		System.out.println("\n\t! ! ! Last Attempt UNSUCCESSFUL ! ! !");
					System.out.println("\n\tAverage Iterations per attempt: " + (totalIterations/(fails + pathsFound)));
					System.out.println("\n\tMax interations on a successful pathFind: " + maxIterationsForSuccess);
					System.out.println("\n\tMax interations on a unsuccessful pathFind: " + maxIterationsForFail);
					System.out.println("\n\tPathsFound:\t\t" + pathsFound);
					System.out.println("\n\tFails:\t\t\t" + fails);
					System.out.println("--> --> --> <> <-- <-- <--\n\n\n");
					lastSuccess = true;
					
					System.out.println("\n Failed Path Finds: ");
					for(FailedPathFind f : failedPathFinds){
						System.out.println(f.toString());
						
					}
					
				}



				return new Path(path, npc, gm);




		} else if(pathNotFound){
			if(print)System.out.println("PATHFINDER: A path was not found");
			
			//Save the fail so it can be investigated further
			failedPathFinds.add(new FailedPathFind(startX, startY, targetX, targetY));
			


			if(printInfo){
				totalIterations += iterations;
				fails++;
				if(maxIterationsForFail < iterations)maxIterationsForFail = iterations;

				System.out.println("\n\n\n--> --> PATHFINDER INFO <-- <--");
				System.out.println("\n\t! ! ! Attempt UNSUCCESSFUL ! ! !");
				if(lastSuccess)
					System.out.println("\n\t - - - Last Attempt Successful - - -");
				else		System.out.println("\n\t! ! ! Last Attempt UNSUCCESSFUL ! ! !");
				System.out.println("\n\tAverage Iterations per attempt: " + (totalIterations/(fails + pathsFound)));
				System.out.println("\n\tMax interations on a successful pathFind: " + maxIterationsForSuccess);
				System.out.println("\n\tMax interations on a unsuccessful pathFind: " + maxIterationsForFail);
				System.out.println("\n\tPathsFound:\t\t" + pathsFound);
				System.out.println("\n\tFails:\t\t\t" + fails);
				System.out.println("--> --> --> <> <-- <-- <--\n\n\n");
				lastSuccess = false;
				
				System.out.println("\n Failed Path Finds: ");
				for(FailedPathFind f : failedPathFinds){
					System.out.println(f.toString());
					
				}
			}
			
			
			// TODO: set all nodes previousMove to... reference cycle prevention
			return null;
			
			
		}
		
		// this shouldn't be reachable
		return null;
		
	}
	
	static boolean lastSuccess = true;
	/**
	 * Used for testing. Initiates pathfinding so that expandLeastCostnode() can be called for a single iteration after this method has been executed.
	 * - sets everything up so each time expandLeastCostNodes is called, single node expansion occurs.
	 * - this is most useful when boolean GameManager.renderPathFinding is set to true;
	 **/
	public static void initPathFinding(GameManager gm, int startX, int startY, int targetX, int targetY){
		
		// Setup PathFinder
		nodes.clear();	// ensure nodes is clear
		iterations = 0;	// reset iterations counter
		
		// Set the target
		PathFinderTwo.targetX = targetX; 
		PathFinderTwo.targetY = targetY;
		
		
		// Expand the first node:
		
		// get possible moves .... the tiles we can travel too
		nodes = getReachableNodes(gm, startX, startY, null);
		
		// increment interations to 1 as a the first node expansion has occured
		iterations++;
		
		// mark the start node as visited, it has been expanded.
		gm.getLevelTile(startX, startY).visited = true;
		
		// get the cost of moving to the identified nodes
		estimateCost(nodes, targetX, targetY, 0);
		
		// order nodes by thier cost 
		sortMovesByCost(nodes);
		
		// Everything is now in place to pathFind with expandleastCostNode()
		
		
		System.out.println("PathFinder Two ready to go !");

	}
	
	public static void expandLeastCostNode(GameManager gm){
		
		if(print)System.out.println("PathFinder.expandLeastCost: at the start");
		
		
		// temporary just to illustare pathfinding
		//gm.setAllTilesAccessible(false);
		
		// FAIL CHECKING
		if(nodes.size() == 0){
			if(print)System.out.println("XXXX Cannot Find path to target. No more remaining nodes in the queue XXX");
			pathNotFound = true;
			return;
		}
		
		
		// Get the node we are about to expand
		currentNode = nodes.get(0);
		
		
		// COMPLETION CHECKING
		// have we reached target with this node? 
		if((targetX == currentNode.endTileX) && (targetY == currentNode.endTileY)){
			if(print){
				System.out.println("XXX Reached the target XXX");
				System.out.println("Pathfinding took: " + iterations + " iterations");
			}
			pathFound = true;
			return;
		}
		
		
		// used to illustrate pathfinding
		/*for(PathNode pm : nodes){
			gm.getLevelTile(pm.endTileX, pm.endTileY).accessible = true;
		}*/
		
		
		// add this node to the list of used nodes
		usedNodes.add(currentNode);
		
		// remove the expanded node from the 'queue' (ordered array list...)
		nodes.remove(0);
		// increment iterations
		iterations++;
		
		if(print)System.out.println("ExpandLeastCost: expanding node: " + currentNode.toString() );
		// mark the end tile of this possiblemovement as visited
		gm.getLevelTile(currentNode.endTileX, currentNode.endTileY).visited = true;
		
		
		// prints for debugging
		//System.out.println("\n\nExpanding node");
		//System.out.println(toExpand.toString());
		//System.out.println("Current path length: " + toExpand.distanceFromStart);
		
		
		

		// No we havent reached the target, time to expand.
		
		// Determine all possible nodes reachable from the current node
		if(print)System.out.println("Making new nodes:");
		ArrayList<PathNode> newNodes = getReachableNodes(gm, currentNode.endTileX, currentNode.endTileY, currentNode);
		
		if(print)System.out.println("Determining cost for new nodes:");
		// estimate the cost for the new nodes
		estimateCost(newNodes, targetX, targetY, currentNode.distanceFromStart);
		
		if(print)System.out.println("Adding the new nodes to the queue");
		// add the new nodes to the queue
		for(PathNode pm : newNodes){
			nodes.add(pm);
			
		}
		
		if(print)System.out.println("Sorting all nodes by cost now that new nodes have been added");
		//reorder the nodes now that new ones have been added
		sortMovesByCost(nodes);
		
		
		/*System.out.println("\n\n\nNodes in the queue: " );
		for(PossibleMove pm : nodes){
			System.out.println(pm.toString());
			
		}*/
		
		//System.out.println("End of queue\n\n\n");
		
		
		
	}




	/**
	 *  Determines the possible moves that can be made by an NPC from the the current tile position. 
	 *   - a move is possible if collision with level tiles is not a problem
	 *   - the NPC can jump high enough
	 *   - the the tile position at the end of the movement has not already been visited
	 *   
	 *   Returns an arrayList containing the PossibleMoves.
	 **/
	public static ArrayList<PathNode> getReachableNodes(GameManager gm, int tileX, int tileY, PathNode previousMove){

		ArrayList<PathNode> possibleNodes = new ArrayList<PathNode>();
		
		int endTileX;
		int endTileY;
		
		
		// PRETTY IFFY ABOUT THIS CASE ADDED HERE
			// Is it really the job of this method to be worried about what is visited. These tiles may get visited later. So I need to recheck visited before executing one of these steps.
		
		// what if the npc is not on a solid tile when initiating pathfinding..? is this ok? 
			// I probably shouldnt limit pathfinding to only on the ground but the npc will fall anyway..
		
		
		//  Is the NPC in the air right now? 
		
		if(!gm.getLevelTileCollision(tileX, tileY+1)){
			
			if(print)System.out.println("NPC in air must go DOWN");
			
			// npc is in the air
			
			// how far does the npc need to fall?
			// keep going down untill there is collision
			int tilesDown = 1;
			while(!gm.getLevelTileCollision(tileX, tileY + tilesDown)){
				tilesDown ++;
			}
			
			// remove tilesDown overshoot of 1
			tilesDown--;
			
			// in this case if below is visited is not important, for whatever reason, the npc is in the air aand has to fall
			// this will be the only possible move from this tile.
			possibleNodes.add(new PathNode(Movement.DOWN, tileX, tileY, tileX, tileY+tilesDown, tilesDown, previousMove));
			if(print)System.out.println("\t adding node");
			
			
			return possibleNodes;
			
		}
		
		
		
		
		// -	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-
		// 											GOING RIGHT
		
		//Can we go right?
		if(!gm.getLevelTileCollision(tileX + 1, tileY)){
			
			// can go right
			if(print)System.out.println("RIGHT is possible");
			
			// but will npc fall?
			if(!gm.getLevelTileCollision(tileX + 1, tileY +1)){
				
				// yes NPC will fall.
				if(print)System.out.println("\t Right leads to a fall");
				
				// how much will NPC fall?
				// keep going down untill there is collision
				int tilesDown = 1;
				while(!gm.getLevelTileCollision(tileX + 1, tileY + tilesDown)){
					tilesDown ++;
				}
				
				// remove tilesDown overshoot of 1
				tilesDown--;
				
				if(print)System.out.println("\t Right leads to a fall of: " + tilesDown + " tiles");
				
				// Determine end tile location
				endTileX = tileX + 1;
				endTileY = tileY + tilesDown;
				
				// Has this location been visited already? 
				if(!gm.getLevelTile(endTileX, endTileY).visited){
					
					// ADD POSSIBLE MOVEMENT - RIGHT + DOWN 
					possibleNodes.add(new PathNode(Movement.RIGHT_DOWN, tileX, tileY, tileX+1, tileY+tilesDown, tilesDown, previousMove));
					if(print)System.out.println("\t adding node");
					
				}else if(print)System.out.println("\tCant go right + down. End tile was visited");
				
			}else{
				
				// No falling, just try move right.
				
				// Determine end tile location
				endTileX = tileX + 1;
				endTileY = tileY;
				
				// Has this location been visited already? 
				if(!gm.getLevelTile(endTileX, endTileY).visited){
					
					// ADD POSSIBLE MOVEMENT - RIGHT 
					possibleNodes.add(new PathNode(Movement.RIGHT, tileX, tileY, endTileX, endTileY, previousMove));
					if(print)System.out.println("\t adding node");
				
				}else if(print)System.out.println("\tCant go right. End tile was visited");
			}
		}
		
		
		
		
		// -	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	- //
		// 												GOING LEFT										  //
		
		//Can we go left?
			if(!gm.getLevelTileCollision(tileX - 1, tileY)){
					
				// can go left
				if(print)System.out.println("LEFT is possible");
				
				// but will npc fall?
				if(!gm.getLevelTileCollision(tileX - 1, tileY +1)){
					
					// yes NPC will fall.
					if(print)System.out.println("\t Left leads to a fall");
					
					// how much will NPC fall?
					
					// keep going down untill there is collision with a solid tile. 
					int tilesDown = 1;
					while(!gm.getLevelTileCollision(tileX - 1, tileY + tilesDown)){
						tilesDown ++;
					}
					
					// this will always overshoot by 1, so remove overshoot
					tilesDown--;
					
					if(print)System.out.println("Going left leads to a fall of: " + tilesDown + " tiles");
					
					// Determine end tile location
					endTileX = tileX - 1;
					endTileY = tileY + tilesDown;
					
					// Has this location been visited already? 
					if(!gm.getLevelTile(endTileX, endTileY).visited){
						
						// ADD POSSIBLE MOVEMENT - LEFT + DOWN
						//System.out.println("ADDING LEFT DOWN");
						possibleNodes.add(new PathNode(Movement.LEFT_DOWN, tileX, tileY, endTileX, endTileY, tilesDown, previousMove));
						if(print)System.out.println("\t adding node");
					
					}else if(print)System.out.println("\tCant go LEFT + DOWN. End Tile was visited");
					
				}else{
					
					// No falling, just try move left.
					
					// Determine end tile location
					endTileX = tileX - 1;
					endTileY = tileY;
					
					// Has this location been visited already? 
					if(!gm.getLevelTile(endTileX, endTileY).visited){
						
						// ADD POSSIBLE MOVEMENT - LEFT
						possibleNodes.add(new PathNode(Movement.LEFT, tileX, tileY, endTileX, endTileY, previousMove));
						if(print)System.out.println("\t adding node");
						
					}else if(print)System.out.println("\tCant go LEFT. End Tile was visited");
				}
		}



		// -	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	//
		// 												GOING UP ONCE									//
			
		// Can we go up? (above tile is not solid)
		if(!gm.getLevelTileCollision(tileX, tileY -1)){

			// UP ONE
			endTileY = tileY -1;

			// Up One is possible
			//System.out.println("UP ONE is possible");

			// Can we go right? (right hand below tile is sold, AND right hand above tile is not solid)
			if(gm.getLevelTileCollision(tileX + 1, tileY) && !gm.getLevelTileCollision(tileX + 1, tileY -1)){

				// UP ONE RIGHT ONE is possible
				if(print)System.out.println("UP ONE RIGHT ONE is possible");
				endTileX = tileX + 1;
				
				// Has this location been visited already? 
				if(!gm.getLevelTile(endTileX, endTileY).visited){
					
					// ADD POSSIBLE MOVEMENT - UP + RIGHT
					if(print)System.out.println("ADDING UP ONE RIGHT ONE");
					possibleNodes.add(new PathNode(Movement.UP_RIGHT, tileX, tileY, endTileX, endTileY, previousMove));
					if(print)System.out.println("\t adding node");
					
				} else if(print)System.out.println("Cant go UP + RIGHT. End Tile was visited");
				
				
				
			}


			// Can we go left? (left hand below tile is sold, AND left hand above tile is not solid)
			if(gm.getLevelTileCollision(tileX - 1, tileY) && !gm.getLevelTileCollision(tileX - 1, tileY -1)){

				// UP ONE LEFT ONE is possible
				if(print)System.out.println("UP ONE LEFT ONE is possible");
				endTileX = tileX - 1;
				
				// Has this location been visited already? 
				if(!gm.getLevelTile(endTileX, endTileY).visited){
					
					// ADD POSSIBLE MOVEMENT - UP + LEFT
					possibleNodes.add(new PathNode(Movement.UP_LEFT, tileX, tileY, endTileX, endTileY, previousMove));
					if(print)System.out.println("\t adding node");
					
				} else if(print)System.out.println("Cant go UP + LEFT. End Tile was visited");
			}


			
			
			// -	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-//
			//										 GOING UP TWICE											 //
			
			// Can we go up for a second time?
			if(!gm.getLevelTileCollision(tileX, tileY - 2)){

				// UP TWO
				endTileY = tileY -2;

				// Up Two is possible
				//System.out.println("UP TWO is possible");


				// Can we go right? (right hand below tile is sold, AND right hand above tile is not solid)
				if(gm.getLevelTileCollision(tileX + 1, tileY - 1) && !gm.getLevelTileCollision(tileX + 1, tileY -2)){

					// UP TWO RIGHT ONE is possible
					if(print)System.out.println("UP TWO RIGHT ONE is possible");
					endTileX = tileX + 1;
					
					// Has this location been visited already? 
					if(!gm.getLevelTile(endTileX, endTileY).visited){
						possibleNodes.add(new PathNode(Movement.UP_UP_RIGHT, tileX, tileY, endTileX, endTileY, previousMove));
						if(print)System.out.println("\t adding node");
					}else if(print)System.out.println("\t Can't do this, end tile visited.");
				}


				// Can we go left? (left hand below tile is sold, AND left hand above tile is not solid)
				if(gm.getLevelTileCollision(tileX - 1, tileY -1) && !gm.getLevelTileCollision(tileX - 1, tileY -2)){

					// UP TWO LEFT ONE is possible
					if(print)System.out.println("UP THREE LEFT ONE is possible");
					endTileX = tileX - 1;
					
					// Has this location been visited already? 
					if(!gm.getLevelTile(endTileX, endTileY).visited){
						possibleNodes.add(new PathNode(Movement.UP_UP_LEFT, tileX, tileY, endTileX, endTileY, previousMove));
						if(print)System.out.println("\t adding node");
					} else if(print)System.out.println("\t Can't do this, end tile visited.");
				}


				
				// 	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-//	
				// 											GOING UP THRICE											 //
				
				// Can we go up for a third and final time
				if(!gm.getLevelTileCollision(tileX, tileY - 3)){
					
					
					// UP THREE
					endTileY = tileY -3;
					//System.out.println("UP THREE is possible");
					
					
					// UP THREE RIGHT
					// Can we go right? (right hand below tile is sold, AND right hand above tile is not solid)
					if(gm.getLevelTileCollision(tileX + 1, tileY - 2) && !gm.getLevelTileCollision(tileX + 1, tileY -3)){

						// UP THREE RIGHT ONE is possible
						if(print)System.out.println("UP THREE RIGHT ONE is possible");
						endTileX = tileX + 1;
						
						// Has this location been visited already? 
						if(!gm.getLevelTile(endTileX, endTileY).visited){
							possibleNodes.add(new PathNode(Movement.UP_UP_UP_RIGHT, tileX, tileY, endTileX, endTileY, previousMove));
							if(print)System.out.println("\t adding node");
							
						}else if(print)System.out.println("\t Can't do this, end tile visited.");
					}


					// UP THREE LEFT
					// Can we go left? (left hand below tile is sold, AND left hand above tile is not solid)
					if(gm.getLevelTileCollision(tileX - 1, tileY - 2) && !gm.getLevelTileCollision(tileX - 1, tileY -3)){

						// UP THREE LEFT ONE is possible
						if(print)System.out.println("UP THREE LEFT ONE is possible");
						endTileX = tileX - 1;
						
						// Has this location been visited already? 
						if(!gm.getLevelTile(endTileX, endTileY).visited){
							possibleNodes.add(new PathNode(Movement.UP_UP_UP_LEFT, tileX, tileY, endTileX, endTileY, previousMove));
							if(print)System.out.println("\t adding node");
							
						} else if(print)System.out.println("\t Can't do this, end tile visited.");
					}		
				}
			}
		}

		
		
		
		
		
		return possibleNodes;

	}
	
	
	
	/**
	 *  Apply heuristic to possible moves contained in the input array list.  
	 * 
	 **/
	public static void estimateCost( ArrayList<PathNode> possibleMoves, int targetX, int targetY, int pathLength){
		
		
		for(PathNode pm : possibleMoves ){
			
			int costToThisNodeFromPreviousNode = Math.abs(pm.startTileX - pm.endTileX) + Math.abs(pm.startTileY - pm.endTileY);
			
			// first add how much this particular move will cost and add this to the running cost from the start
			pm.distanceFromStart += Math.abs(pm.startTileX - pm.endTileX) + Math.abs(pm.startTileY - pm.endTileY); // += costToThisNodeFromPreviousNode
			
			
			int xCost;
			int yCost;
			
					// cost of this movement				// cost to get to the end after this movement
			xCost = Math.abs(pm.startTileX - pm.endTileX) + Math.abs(pm.endTileX - targetX);
			yCost = Math.abs(pm.startTileY - pm.endTileY) + Math.abs(pm.endTileY - targetY);
			
			
			
			pm.totalCost = pathLength + xCost + yCost;
			
			// addition
			pathLength += costToThisNodeFromPreviousNode;
			
		}
		
		
		
		
		
		
		
		
	}
	
	public static void sortMovesByCost(ArrayList<PathNode> possibleMoves){
		
		
		
		// Sort ImageRequests by decesnding order of zDepth
				Collections.sort(possibleMoves, new Comparator<PathNode>(){

					@Override
					public int compare(PathNode pm0, PathNode pm1){

						if(pm0.totalCost > pm1.totalCost){
							// if the depth of ir0 is more then ir1, ir0 must be drawn first
							return 1;
						}else{
							return -1;
						}
					}
				});
		
		
		
		
		
		
		
		/*
		// Sort ImageRequests by decesnding order of zDepth
				Collections.sort(imageRequests, new Comparator<ImageRequest>(){

					@Override
					public int compare(ImageRequest ir0, ImageRequest ir1){

						if(ir0.zDepth > ir1.zDepth){
							// if the depth of ir0 is more then ir1, ir0 must be drawn first
							return -1;
						}else{
							return 1;
						}
					}
				});*/
		
	}
	
	public static void setPathMap(GameManager gm, int tileX, int tileY){
		
		nodes.clear();
		LevelTile temp;
		PathNode toExpand;
		
		nodes = getReachableNodes(gm, tileX, tileY, null);
		
		// mark end tile of all nodes as visited
		for(PathNode node : nodes){
			temp = gm.getLevelTile(node.endTileX, node.endTileY);
			if(temp == null){
				// tile is OOB, dont mark it
				
			}else {
				temp.visited = true;
				temp.accessible = true;
			}
		}
		
		while(!nodes.isEmpty()){
			
			toExpand = nodes.get(0);
			nodes.remove(0);
			
			newNodes.clear();
			newNodes = getReachableNodes(gm, toExpand.endTileX, toExpand.endTileY, null);
			
			
			// mark end tile of all nodes as visited
			for(PathNode node : newNodes){
				temp = gm.getLevelTile(node.endTileX, node.endTileY);
				if(temp == null){
					// tile is OOB, dont mark it
					
				}else {
					temp.visited = true;
					temp.accessible = true;
					nodes.add(node);
				}
			}
			
			
			
		}
		
		
		
		
		
	}

}
