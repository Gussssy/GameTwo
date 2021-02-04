package com.gussssy.gametwo.game.debug;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.objects.player.Player;

/**
 * A display showing containing various buttons to toggle the display/illustration of internal variables for debugging purposes. 
 * 
 * This panel is toggled on and off by GameManager when F3 is pressed.
 * 
 **/
public class DebugPanel {

	// booleans holding the status of the DebugPanel buttons
	public static boolean showAwareness = false; 
	public static boolean showPathMap = false; 
	public static boolean showTileCollision = false; 
	
	// If trhis is true, will print to console infomation about key presses specifically the space bar for jump. 
	public static boolean debugInputLoss = false;

	// Button posiioning and offset
	static int initialButtonOffSet = 15;
	static int buttonSpacing = 5;
	static int buttonWidth = 80;
	static int buttonHeight = 20;
	static int textOffSetY = 5;
	static int textOffSetX = 5;

	// location of awareness button
	static int awarenessX = initialButtonOffSet;
	static int awarenessY = initialButtonOffSet;

	// location of pathMap button
	static int pathMapX = initialButtonOffSet;
	static int pathMapY = awarenessY + buttonHeight + buttonSpacing;
	
	// location of tileCollision button
	static int tileCollisionX = initialButtonOffSet;
	static int tileCollisionY = pathMapY + buttonHeight + buttonSpacing;
	
	
	//Debug Messages
	private static int textX = GameManager.getGameWidth() -150;
	public static String message1 = "1.";
	public static String message2 = "2.";
	public static String message3 = "3.";
	public static String message4 = "4.";
	public static String message5 = "5.";
	public static String message6 = "6.";
	public static String message7 = "7.";
	public static String message8 = "8.";
	public static String message9 = "9.";
	public static String message10 = "10.";
	public static String message11 = "11.";
	public static String message12 = "12.";
	
	
	
	// Player
	private static Player player = GameManager.player; // is it ok to do this? Any risk of player not being inititialised? 
	
	


	
	/**
	 * Update DebugPanel
	 * - respond to clicks potentially on a button 
	 **/
	public static void update(GameContainer gc){

		// Click on the debug button(s) ? 
		if(gc.getInput().isButtonDown(1)){

			// left mouse down, the location: 
			int mouseX = gc.getInput().getMouseX();
			int mouseY = gc.getInput().getMouseY();
			System.out.println("Cursor Location: x = "+ mouseX + ", y = " + mouseY);

			// Awareness Button: is cursor over awareness button?
			if(mouseX >= awarenessX && mouseX <= awarenessX + buttonWidth && mouseY >= awarenessY && mouseY <= awarenessY + buttonHeight){

				// cursor is over the button

				// toggle show awareness
				if(showAwareness) {
					showAwareness = false;
				}else { 
					showAwareness = true;
				}
			}

			// PathMap button
			if(mouseX >= pathMapX && mouseX <= pathMapX + buttonWidth && mouseY >= pathMapY && mouseY <= pathMapY + buttonHeight){

				// cursor is over the button

				// toggle show awareness
				if(showPathMap) {
					showPathMap = false;
				}else { 
					showPathMap = true;
				}
			}

			// Tile Collision Button
			if(mouseX >= tileCollisionX && mouseX <= tileCollisionX + buttonWidth && mouseY >= tileCollisionY && mouseY <= tileCollisionY + buttonHeight){

				// cursor is over the button

				// toggle show awareness
				if(showTileCollision) {
					showTileCollision = false;
				}else { 
					showTileCollision = true;
				}
			}


		}
	}



	public static void render(GameContainer gc, Renderer r){
		
		// Set renderer z depth so that debug items are not effected by lighting effects
		r.setzDepth(-1);

		// render awareness button
		if(showAwareness){
			// button is active so green rectangle
			r.drawFillRect(awarenessX + r.getCamX(), awarenessX + r.getCamY(), buttonWidth, buttonHeight, 0xff00ff00);
		}else{
			// button is not active so red rectangle
			r.drawFillRect(awarenessX + r.getCamX(), awarenessX + r.getCamY(), buttonWidth, buttonHeight, 0xffff0000);
		}
		r.drawText("Awareness", awarenessX + textOffSetX + r.getCamX(), awarenessY + textOffSetY + r.getCamY(), 0xff000000);

		// render pathMap button
		if(showPathMap){
			// button is active so green rectangle
			r.drawFillRect(pathMapX + r.getCamX(), pathMapY + r.getCamY(), buttonWidth, buttonHeight, 0xff00ff00);
		}else{
			// button is not active so red rectangle
			r.drawFillRect(pathMapX + r.getCamX(), pathMapY + r.getCamY(), buttonWidth, buttonHeight, 0xffff0000);
		}
		r.drawText("PathMap", pathMapX + textOffSetX + r.getCamX(), pathMapY + textOffSetY + r.getCamY(), 0xff000000);
		

		// render tile collision button
		if(showTileCollision){
			// button is active so green rectangle
			r.drawFillRect(tileCollisionX + r.getCamX(), tileCollisionY + r.getCamY(), buttonWidth, buttonHeight, 0xff00ff00);
		}else{
			// button is not active so red rectangle
			r.drawFillRect(tileCollisionX + r.getCamX(), tileCollisionY + r.getCamY(), buttonWidth, buttonHeight, 0xffff0000);
		}
		r.drawText("Tile Collision", tileCollisionX + textOffSetX + r.getCamX(), tileCollisionY + textOffSetY + r.getCamY(), 0xff000000);

		
		// return renderer z depth to 0 so that lighting effects will still be applied to further rendering
		r.setzDepth(0);
		
		
		// Deug Messages
		
		r.drawText("OnGround: " + Boolean.toString(player.isOnGround()), r.getCamX() + textX, r.getCamY(), 0xffffffff);
		r.drawText("fallDistance: " + Float.toString(player.getFallDistance()), r.getCamX() + textX, r.getCamY() + 10, 0xffffffff);
		r.drawText("Player posX: " + Float.toString(player.getPosX()), r.getCamX() + textX, r.getCamY() + 20, 0xffffffff);
		r.drawText("Player posY: " + Float.toString(player.getPosY()), r.getCamX() + textX, r.getCamY() + 30, 0xffffffff);
		r.drawText("Player tileX: " + Integer.toString(player.getTileX()), r.getCamX() + textX, r.getCamY() + 40, 0xffffffff);
		r.drawText("Player tileY: " + Integer.toString(player.getTileY()), r.getCamX() + textX, r.getCamY() + 50, 0xffffffff);
		r.drawText("Player offX: " + Float.toString(player.getOffX()), r.getCamX() + textX, r.getCamY() + 60, 0xffffffff);
		r.drawText("Player offY: " + Float.toString(player.getOffY()), r.getCamX() + textX, r.getCamY() + 70, 0xffffffff);
		// need to modify this sice moving from GameManager
		//r.drawText("GameObjects: " + GameManager.getobjects.size(), r.getCamX() + textX, r.getCamY() + 80, 0xffffffff);

		r.drawText(message1, r.getCamX() +textX, r.getCamY() + 100, 0xffffffff);
		r.drawText(message2, r.getCamX() +textX, r.getCamY() + 110, 0xffffffff);
		r.drawText(message3, r.getCamX() +textX, r.getCamY() + 120, 0xffffffff);
		r.drawText(message4, r.getCamX() +textX, r.getCamY() + 130, 0xffffffff);
		r.drawText(message5, r.getCamX() +textX, r.getCamY() + 140, 0xffffffff);
		r.drawText(message6, r.getCamX() +textX, r.getCamY() + 150, 0xffffffff);
		r.drawText(message7, r.getCamX() +textX, r.getCamY() + 160, 0xffffffff);
		r.drawText(message8, r.getCamX() +textX, r.getCamY() + 170, 0xffffffff);
		r.drawText(message9, r.getCamX() +textX, r.getCamY() + 180, 0xffffffff);
		r.drawText(message10, r.getCamX() +textX, r.getCamY() + 190, 0xffffffff);
		r.drawText(message11, r.getCamX() +textX, r.getCamY() + 200, 0xffffffff);
		r.drawText(message12, r.getCamX() +textX, r.getCamY() + 210, 0xffffffff);
		
	}

}
