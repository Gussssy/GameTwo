package com.gussssy.gametwo.game.pathfinding;

public class FailedPathFind {
	
	int startTileX, startTileY, endTileX, endTileY;
	
	public FailedPathFind(int startTileX, int startTileY, int endTileX, int endTileY){
		
		this.startTileX = startTileX;
		this.startTileY = startTileY;
		this.endTileX = endTileX;
		this.endTileY = endTileY;
		
	}
	
	public String toString(){
		
		return" Failed Path Find: \n\tstart: x = " + startTileX + ", y = " + startTileY + "\n\ttarget: x = " + endTileX + ", y = " + endTileY;
	}

}
