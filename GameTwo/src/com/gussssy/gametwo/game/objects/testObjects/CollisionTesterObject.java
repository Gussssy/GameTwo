package com.gussssy.gametwo.game.objects.testObjects;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.engine.gfx.ImageTile;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.debug.DebugPanel;
import com.gussssy.gametwo.game.level.LevelTile;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.ui.Button;
import com.gussssy.gametwo.game.ui.ButtonManager;

public class CollisionTesterObject extends GameObject{
	
	AABBComponent hitBox;
	int tileSize = GameManager.TS;
	
	// current location
	// this objects location
	// next frame location
	MoveLocation nextLocation;
	SecondaryCollisionTesterObject nextFrame;
	// resolved location
	MoveLocation resolvedLocation;
	
	boolean followMouse = true;
	
	// velocity 
	float vx, vy;
	
	
	// button variables
	ImageTile up = new ImageTile("/arrow_up_32x32.png", 32, 32);
	ImageTile down = new ImageTile("/arrow_down_32x32.png", 32, 32);
	ImageTile left = new ImageTile("/arrow_left_32x32.png", 32, 32);
	ImageTile right = new ImageTile("/arrow_right_32x32.png", 32, 32);
	ButtonManager buttons = new ButtonManager();
	
	public CollisionTesterObject(int tileX, int tileY, GameManager gm){
		
		this.tag = "collision_tester";
		this.tileX = tileX;
		this.tileY = tileY;
		this.posX = tileX * GameManager.TS;
		this.posY = tileY * GameManager.TS;
		// do i need to worry about offsets? 
		// offstes are so specific to width/height and padding almost makes no sense.. like where the offset counter from... center of the object? 
		
		
		this.width = 20;
		this.height = 50;
		
		
		hitBox = new AABBComponent(this,1);
		addComponent(hitBox);
		
		nextFrame = new SecondaryCollisionTesterObject(posX, posY, width, height, this);
		gm.addObject(nextFrame);
		
		int gameWidth = GameManager.getGameWidth();
		int gameHeight = GameManager.getGameHeight();
		
		buttons.addButton(new DecrementVY(gameWidth/2 - 16, gameHeight - 40 -1 -32, 32, 32, up ));
		buttons.addButton(new IncrementVY(gameWidth/2 - 16, gameHeight - 40, 32, 32, down ));
		buttons.addButton(new DecrementVX(gameWidth/2 - 16 -32 -1, gameHeight - 40, 32, 32, left ));
		buttons.addButton(new IncrementVX(gameWidth/2 - 16 +32 + 1, gameHeight - 40, 32, 32, right ));
		
		
		/*
		buttons.addButton(new DecrementVY(304, 286, 32, 32, up ));
		buttons.addButton(new IncrementVY(304, 318, 32, 32, down ));
		buttons.addButton(new DecrementVX(272, 302, 32, 32, left ));
		buttons.addButton(new IncrementVX(336, 302, 32, 32, right ));
		*/
		
		
	}
	
	LevelTile topLeft, topRight, botLeft, botRight;
	ArrayList<LevelTile> tiles = new ArrayList<LevelTile>();
	

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		if(followMouse){
			posX = gc.getInput().getMouseX() + gc.getRenderer().getCamX();
			posY = gc.getInput().getMouseY() + gc.getRenderer().getCamY();
		}
		
		if(gc.getInput().isKeyDown(KeyEvent.VK_G)){
			System.out.println("CollisionDetectionObject: Toggling Following Mouse");
			if(followMouse)followMouse = false;
			else followMouse = true;
		}
		
		
		
		nextFrame.setPosX(posX + vx);
		nextFrame.setPosY(posY + vy);

		tiles.clear();
		
		
		// How can I intergrate AABB with tile collision?
		// 1. construct AABB for the edges of level tiles: expensive, will be doing so many collision checks
		// 2. Detect all intersecting tiles, checks them and decide on appropritate actions (what im doing now)
		// 3. make temporary hit boxes for the relevant solid tiles

		// detect tiles to check
		if(gc.getInput().isButton(1)){
			// top left corner
			topLeft = gm.getLevelTile((hitBox.getStartX())/tileSize, (hitBox.getStartY())/tileSize);
			topLeft.checked = true;
			topLeft.colliding = topLeft.isCollision();
			tiles.add(topLeft);
			//gm.getLevelTile((int)(posX)/GameManager.TS, (int)(posY)/GameManager.TS).checked = true;

			//top right corner
			topRight = gm.getLevelTile((hitBox.getStartX() + width)/tileSize, (hitBox.getStartY())/tileSize);
			topRight.checked = true;
			topRight.colliding = topRight.isCollision();
			tiles.add(topRight);
			
			//gm.getLevelTile((int)(posX + width)/GameManager.TS, (int)(posY)/GameManager.TS).checked = true;

			// bottom left corner
			botLeft = gm.getLevelTile((hitBox.getStartX())/tileSize, (hitBox.getStartY() + height)/tileSize);
			botLeft.checked = true;
			botLeft.colliding = botLeft.isCollision();
			tiles.add(botLeft);
			//gm.getLevelTile((int)(posX)/GameManager.TS, (int)(posY + height)/GameManager.TS).checked = true;

			// bottom right corner
			botRight = gm.getLevelTile((hitBox.getStartX() + width)/tileSize, (hitBox.getStartY() + height)/tileSize);
			botRight.checked = true;
			botRight.colliding = botRight.isCollision();
			tiles.add(botRight);
			//gm.getLevelTile((int)(posX + width)/GameManager.TS, (int)(posY + height)/GameManager.TS).checked = true;

		}
		
		if(gc.getInput().isButtonDown(3)){
			gm.addObject(new CollisionTesterShadow(posX, posY, width, height, vx,vy));
			System.out.println("SENDING OUT SHADOW");
		}


		
		
		
		buttons.update(gc, gm, dt);
		
		
		
		DebugPanel.message1 = "vx: " + vx;
		DebugPanel.message2 = "vy: " + vy;
		
		
		
		
		updateComponents(gc, gm, dt);
		
	}

	@Override
	public void render(Renderer r) {
		r.drawFillRect((int)posX, (int)posY, width, height, 0xffddddcc);
		//r.drawFillRect((int)(posX + vx), (int)(posY + vy), width, height, 0x55ffffff);
		r.drawLine((int)(posX+width/2), (int)(posY+height/2), (int)(posX + vx + width/2), (int)(posY + vy  + height/2), 0xff000000);
		
		buttons.render(r);
		
		renderComponents(r);
		
	}

	@Override
	public void collision(GameObject other, AABBComponent otherHitBox) {
		
		
	}
	
	
	public void incrementWidth(){
		width ++;
	}
	
	public void decrementWidth(){
		width--;
	}
	
	public void incrementHeight(){
		height++;
	}
	
	public void decrementHeight(){
		height--;
	}
	
	
	// BUTTONS TO CONTROL THE VELOCITY OF COLLISION OBJECT
	
	private class IncrementVX extends Button{

		public IncrementVX(int x, int y, int width, int height, ImageTile imageTile) {
			super(x, y, width, height, imageTile);
		}

		@Override
		public void buttonClicked() {
			vx++;
		}
		
	}
	
	private class IncrementVY extends Button{

		public IncrementVY(int x, int y, int width, int height, ImageTile imageTile) {
			super(x, y, width, height, imageTile);
		}

		@Override
		public void buttonClicked() {
			vy++;
		}
		
	}
	
	private class DecrementVX extends Button{

		public DecrementVX(int x, int y, int width, int height, ImageTile imageTile) {
			super(x, y, width, height, imageTile);
		}

		@Override
		public void buttonClicked() {
			vx--;
		}
		
	}
	
	private class DecrementVY extends Button{

		public DecrementVY(int x, int y, int width, int height, ImageTile imageTile) {
			super(x, y, width, height, imageTile);
		}

		@Override
		public void buttonClicked() {
			vy--;
		}
		
	}
	
	
	
	
	
	
	

}
