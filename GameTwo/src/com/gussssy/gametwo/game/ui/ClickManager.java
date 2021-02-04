package com.gussssy.gametwo.game.ui;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.game.objects.GameObject;

public class ClickManager{
	
	public static boolean objectWasClicked(GameContainer gc, GameObject object){
		
		//if(!gc.getInput().isButtonDown(1))return false;
		
		// add cam correction to mouse location
		int mouseX = gc.getInput().getMouseX()+ gc.getRenderer().getCamX();
		int mouseY = gc.getInput().getMouseY()+ gc.getRenderer().getCamY();
		int objectX = (int)object.getPosX() ;
		int objectY = (int)object.getPosY() ;
		
		System.out.println("MouseX: " + mouseX);
		System.out.println("MouseY: " + mouseY);
		System.out.println("ObjectX: " + objectX);
		System.out.println("ObjectY: " + objectY);
		
		if(mouseX > objectX && mouseX < objectX + object.getWidth() && mouseY > objectY && mouseY < objectY + object.getHeight() ) {
			System.out.println("Object was clicked");
			return true;
		}else{
			System.out.println("Object was NOT clicked");
			return false;
		}
		
		
		
	}

}
