package com.gussssy.gametwo.game.objects.testObjects;

import java.awt.event.KeyEvent;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.engine.gfx.Image;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.debug.DebugPanel;
import com.gussssy.gametwo.game.objects.GameObject;

public class TestObject extends GameObject {

	private Image img = new Image("/player_1.png");
	public boolean followMouse = true;
	int speedX = 1;
	int speedY = 1;

	private boolean print = false;


	public TestObject(int tileX, int tileY){
		posX = tileX * GameManager.TS;
		posY = tileY * GameManager.TS;
		this.tag = "testObject";
		this.width = 16;
		this.height = 16;
		this.addComponent(new AABBComponent(this));
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {

		// Left
		if(gc.getInput().isKeyDown(KeyEvent.VK_F)){
			posX -= speedX;
			print = true;
		}

		// Right
		if(gc.getInput().isKeyDown(KeyEvent.VK_H)){
			posX += speedX;
			print = true;
		}

		// Up
		if(gc.getInput().isKeyDown(KeyEvent.VK_T)){
			//System.out.println("Speed: " + speed);
			//System.out.println("posY before: " + posY);
			posY -= speedY;
			//System.out.println("posY after: " + posY);
			print = true;
		}

		// Down
		if(gc.getInput().isKeyDown(KeyEvent.VK_G)){
			posY += speedY;
			print = true;
		}

		// Up + Left
		if(gc.getInput().isKeyDown(KeyEvent.VK_R)){
			posX -= speedX;
			posY -= speedY;
			print = true;
		}

		// Up + Right
		if(gc.getInput().isKeyDown(KeyEvent.VK_Y)){
			posX += speedX;
			posY -= speedY;
			print = true;
		}

		// Down + Left
		if(gc.getInput().isKeyDown(KeyEvent.VK_C)){
			posX -= speedX;
			posY += speedY;
			print = true;
		}

		// Down + Right
		if(gc.getInput().isKeyDown(KeyEvent.VK_N)){
			posX += speedX;
			posY += speedY;
			print = true;
		}



		if(gc.getInput().isKeyDown(KeyEvent.VK_X)){
			speedX--;
			//GameManager.debugMessage6 = "TestOb Speed: " + Integer.toString(speed);
		}
		if(gc.getInput().isKeyDown(KeyEvent.VK_Z)){
			//GameManager.debugMessage6 = "TestOb Speed: " + speed;
			speedX++;
		}
		
		if(gc.getInput().isKeyDown(KeyEvent.VK_PERIOD)){
			speedY--;
			//GameManager.debugMessage6 = "TestOb Speed: " + Integer.toString(speed);
		}
		if(gc.getInput().isKeyDown(KeyEvent.VK_COMMA)){
			//GameManager.debugMessage6 = "TestOb Speed: " + speed;
			speedY++;
		}

		// For somew reason that I cannot understand, printing speed to the window is off by 1 if the debug message is set within the braces..??????
		// ok so im having issues with post vs preincrement, it would seem that it is not incremented untill outside of the braces..... I had know idea
		// https://stackoverflow.com/questions/2371118/how-do-the-post-increment-i-and-pre-increment-i-operators-work-in-java
		//GameManager.debugMessage6 = "TestOb SpeedX: " + speedX;
		//GameManager.debugMessage7 = "TestOb SpeedY: " + speedY;
		// I think I know what is happening!
		// - the class field speed is not set untill after the braces but internally, the temporary local variable speed is at the correct value
		// but I believe that when i send speed to the static String, it accesses the class variable speed which is not set till outside the braces.


		//float endX = posX + width
		//GameManager.debugMessage1 = "TestOb posX: " + Float.toString(posX);
		//GameManager.debugMessage2 = "TestOb posY: " + Float.toString(posY);

		// updateComponents
		this.updateComponents(gc, gm, dt);

	}

	@Override
	public void render(Renderer r) {

		r.drawImage(img, (int)posX, (int)posY);

		this.renderComponents(r);

	}

	int[] incursions = new int[4];
	int smallestIndex;
	int botIncursion, topIncursion, leftIncursion, rightIncursion;
	float botMinimisation, topMinimisation, leftMinimisation, rightMinimisation;

	@Override
	public void collision(GameObject other, AABBComponent otherHitBox) {

		//System.out.println("Collision with " + other.getTag());
		if(other.getTag().equals("platform")){
			AABBComponent thisAABB = (AABBComponent) this.findComponent("aabb");
			AABBComponent otherAABB = (AABBComponent) other.findComponent("aabb");

			int dx = thisAABB.getLastCenterX() - thisAABB.getCenterX();
			int dy = thisAABB.getCenterY() - thisAABB.getLastCenterY();

			if(print){
				DebugPanel.message8 = "TestOb dx: " + dx;
				DebugPanel.message9 = "TestOb dy: " + dy;
				//print = false;
			}


			// how much the object has penetrated top, bottom, left and right
			topIncursion = thisAABB.getStopY() - otherAABB.getStartY();
			botIncursion = otherAABB.getStopY() - thisAABB.getStartY();
			leftIncursion = thisAABB.getStopX() - otherAABB.getStartX();
			rightIncursion = otherAABB.getStopX() -  thisAABB.getStartX();

			// Order matters, like this, there is a preference for top/bottom collisions 
			incursions[0] = topIncursion;
			incursions[1] = botIncursion;
			incursions[2] = leftIncursion;
			incursions[3] = rightIncursion;


			smallestIndex = 0;
			//System.out.println("");
			for(int i = 1; i < incursions.length; i++){
				//System.out.println("Comparing current smallest: " + incursions[smallestIndex] + " with: " + incursions[i]);
				if(incursions[i] < incursions[smallestIndex]){
					smallestIndex = i;
					//System.out.println("Smaller, new smallest: " + incursions[i]);

				}else {
					//System.out.println("not smaller, current smallest: " + incursions[smallestIndex]);
				}

			}




			// MINIMISATION
			// if the object is moving fast, using smallest incursion produces incorrect collisions
			// So now it takes into consideration the speed and direction of the object to determine collision
			// by 'moving' the object back along the reverse direction to the point where the collision actually occured
			// What side of the platform was hit first? - the smallest value = the side that was hit first

			// Top and Bot Minimisation
			if(dy != 0){
				// Minimise top and bot
				if(dy < 0){
					// Minimize bot - object is moving up as dy is neg
					botMinimisation = (float)botIncursion / (float)dy;
					topMinimisation = 1000;
					//System.out.println("botMinimisation: " + botMinimisation);

				}else{
					// Minimize top
					topMinimisation = (float)topIncursion / (float)dy;
					botMinimisation = 1000;
					//System.out.println("topMinimisation: " + topMinimisation);

				}
			}else{
				// there is no movement on the y axis, only left and right collisions are possible
				topMinimisation = 1000;
				botMinimisation = 1000;
			}

			// Left and Right Minimisation
			if(dx != 0){
				// Minimise top and bot
				if(dx < 0){
					// Minimize right - object is moving left as dx is negative
					rightMinimisation = (float)rightIncursion / (float)dx;
					leftMinimisation = 1000;
					//System.out.println("rightMinimisation: " + rightMinimisation);

				}else{
					// Minimize left - object is moving right as dx is positive
					leftMinimisation = (float)leftIncursion / (float)dx;
					rightMinimisation = 1000;
					//System.out.println("leftMinimisation: " + leftMinimisation);

				}
			}else{
				// there is no movement on the y axis, only left and right collisions are possible
				leftMinimisation = 1000;
				rightMinimisation = 1000;
			}
			
			if(print){
				System.out.println("\n\n\ntopMinimisation: " + topMinimisation);
				System.out.println("botMinimisation: " + botMinimisation);
				System.out.println("leftMinimisation: " + leftMinimisation);
				System.out.println("rightMinimisation: " + rightMinimisation);
				print = false;
			}
			
			
			if(dy !=0){
				if(dy < 0){
					// potentially top col
					// NOT bot
				}else {
					// potentially bot col
					// NOT TOP
				}
				
			}else {
				// neither top or bot
			}
			
			
			if(dx !=0){
				if(dx < 0){
					// potentially right col
					// NOT LEFT
				}else {
					// potentially left col
					// NOT RIGHT
				}
				
			}else {
				// neither left or right
			}
			
			// Now Find smallest remaining incursion and thats the collision
			// Apply smallest incursion to player and Done




			// Top Collision
			if(smallestIndex == 0){
				DebugPanel.message1 = "Top Collision";

			} else if(smallestIndex == 1){
				DebugPanel.message1 = "Bottom Collision";

			} else if(smallestIndex == 2){
				DebugPanel.message1 = "Left Collision";

			} else {
				DebugPanel.message1 = "Right Collision";

			}

			DebugPanel.message2 = "Top Incursion: " + Integer.toString(topIncursion);
			DebugPanel.message3 = "Bottom Incursion: " + Integer.toString(botIncursion);
			DebugPanel.message4 = "Left Incursion: " + Integer.toString(leftIncursion);
			DebugPanel.message5 = "Right Incursion: " + Integer.toString(rightIncursion);
			DebugPanel.message6 = "- - - - - - - - - - - - - - - -";
			DebugPanel.message7 = "Top Min: " + Integer.toString(topIncursion);
			DebugPanel.message8 = "Bot Min: " + Integer.toString(botIncursion);
			DebugPanel.message9 = "Left Min: " + Integer.toString(leftIncursion);
			DebugPanel.message10 = "Right Min: " + Integer.toString(rightIncursion);



		}

	}

}
