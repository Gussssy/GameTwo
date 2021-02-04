package com.gussssy.gametwo.game.objects.testObjects;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.objects.projectile.Grenade;
import com.gussssy.gametwo.game.objects.projectile.Projectile;
import com.gussssy.gametwo.game.ui.ClickManager;


//import com.gussssy.gametwo.game.objects.projectile.Projectile;

public class ProjectileLauncher extends GameObject {
	
	ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	
	int cooldown = 10;
	int count = cooldown;
	boolean launchContinuous = false;
	
	// define tiles part of allowed region
	int startTileX, startTileY, stopTileX, stopTileY;
	int tileWidth, tileHeight;
	
	public ProjectileLauncher(int tileX, int tileY, int startTileX, int stopTileX, int startTileY, int stopTileY){
		
		this.tileX = tileX;
		this.tileY = tileY;
		this.posX = tileX *GameManager.TS;
		this.posY = tileY * GameManager.TS;
		width = 8;
		height = 8;
		
		this.startTileX = startTileX;
		this.stopTileX = stopTileX;
		this.startTileY = startTileY;
		this.stopTileY = stopTileY;
		
		tileWidth = stopTileX - startTileX + 1;
		tileHeight = stopTileY - startTileY + 1;
		
	}
	
	

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		if(launchContinuous){
			
			if(count <= 0){
				Grenade g = new Grenade(posX, posY, 100, -30, gm);
				projectiles.add(g);
				gm.addObject(g);
				count = cooldown;
			}else{
				count--;
			}
			
		}else{
			if(gc.getInput().isButtonDown(1)){
				if(ClickManager.objectWasClicked(gc, this)){
					Grenade g = new Grenade(posX, posY, 100, -30, gm);
					projectiles.add(g);
					gm.addObject(g);
				}
			}
		}
		
		if(gc.getInput().isKeyDown(KeyEvent.VK_DELETE)){
			System.out.println("Removing Active Projectiles");
			for(Projectile p : projectiles){
				p.dead = true;
			}
			projectiles.clear();
		}
		
		// Dead check
		// if projectiles has a size of 1, i will initially be 0, will access the element at 0, the only element
		for(int i = projectiles.size()-1; i >= 0; i--){
			if(projectiles.get(i).dead == true){
				projectiles.remove(i);
			}
		}
		
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		
		r.drawFillRect(tileX*GameManager.TS, tileY*GameManager.TS , width, height, 0xff555555);
		
		r.drawRect(startTileX * GameManager.TS, startTileY * GameManager.TS , tileWidth * GameManager.TS,tileHeight * GameManager.TS , 0xffff0000);
		
	}

	@Override
	public void collision(GameObject other, AABBComponent otherHitBox) {
		// TODO Auto-generated method stub
		
	}

}
