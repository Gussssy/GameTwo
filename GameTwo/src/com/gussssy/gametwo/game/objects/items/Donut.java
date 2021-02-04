package com.gussssy.gametwo.game.objects.items;

public class Donut extends Item {
	
	
	
	public Donut(int tileX, int tileY){
		
		super("/donut.png");
		//System.out.println(" donut constructor");
		posX = tileX * 16;
		posY = tileY * 16;
		
	}
	
}
