package com.gussssy.gametwo.game.pathfinding;

import com.gussssy.gametwo.game.GameManager;

public class PathNode {
	
	// The movement
	public Movement movement;
	
	// The previous PossibleMove that was exceuted before this one
	public PathNode previousNode = null;
	
	// tile location
	public int startTileX;
	public int startTileY;
	public int endTileX;
	public int endTileY;
	
	// To determine cost / heuristic 
	public int distanceFromStart;
	int estimatedDistanceToEnd;
	public int totalCost;
	
	// Dealing with falling
	boolean fall = false;
	int tilesDown = 0;
	
	/*public PossibleMove(Movement movement, int distanceFromStart, int estimatedDistanceToEnd){
		
		this.movement = movement;
		this.distanceFromStart = distanceFromStart;
		this.estimatedDistanceToEnd = estimatedDistanceToEnd;
		
		totalCost = distanceFromStart + estimatedDistanceToEnd;
		
	}*/
	
	/**
	 * Primary Constructor.  
	 * 
	 **/
	public PathNode(Movement movement, int startTileX, int startTileY, int endTileX, int endTileY, PathNode previousNode){
		this.movement = movement;
		this.startTileX = startTileX;
		this.startTileY = startTileY;
		this.endTileX = endTileX;
		this.endTileY = endTileY;
		this.previousNode = previousNode;
		
	}
	
	/**
	 *  Secondary constructor. Used when a new possible move contains down movement.
	 *  	- takes an extra parameter tilesDown, the number of tiles falling in this movement  
	 **/
	public PathNode(Movement movement, int startTileX, int startTileY, int endTileX, int endTileY, int tilesDown, PathNode previousMove){
		
		// falling stuff
		fall = true; 
		this.tilesDown = tilesDown;
		
		this.movement = movement;
		this.startTileX = startTileX;
		this.startTileY = startTileY;
		this.endTileX = endTileX;
		this.endTileY = endTileY;
		this.previousNode = previousMove;
		
	}
	
	
	// when a move is executed, mark all intermediate tiles as visited
	public void markIntermediateTilesVisited(GameManager gm){
		
		switch(movement){
		case RIGHT:
			// there is no intermediate tiles
			gm.getLevelTile(endTileX, endTileY).visited = true;
			
			break;
		case LEFT:
			// there is no intermediate tiles
			gm.getLevelTile(endTileX, endTileY).visited = true;
			break;
		default: 
			break;
		}
		
	}
	
	
	public String toString(){
		
		if(previousNode != null){
			return "Node: " + movement + "\n\tStart: ("+startTileX +", "+ startTileY + ")" + "\n\tEnd: (" + endTileX + ", " + endTileY +")" + "\n\tCost: " + totalCost + "\n\tPreviousMovement: " + previousNode.movement;
			
		}else {
			return "Node: " + movement + "\n\tStart: ("+startTileX +", "+ startTileY + ")" + "\n\tEnd: (" + endTileX + ", " + endTileY +")" + "\n\tCost: " + totalCost + "\n\tPreviousMovement: null ";
		}
		
		}
	

}
