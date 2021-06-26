package com.gussssy.gametwo.game.objects.npc;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.engine.gfx.ImageTile;
import com.gussssy.gametwo.game.EventManager;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.Textures;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.particles.TileDestruction;
import com.gussssy.gametwo.game.pathfinding.PathFinderTwo;

/**
 * A relatively fast moving NPC.
 * <p>
 * Currently does not have any attacking ability.
 * <p>
 * Can destroy the block beneath it after completing a path (if enabled)
 */
public class Rabbit extends NPC {
	
	
	// Rabbit Texture (TileImage)
	// 		(With a standing frame and 3 frames for walking)
	private ImageTile rabbitImage = Textures.rabbitTile;
	
	// Because rabbits are being treated as if they were 16*16 pixels whereas they are actually 32*32, the Image is rendered too high. This corrects that.
	private int imageOffY = 16;
	
	// Controls whether or not rabbits destroy the tile below them after finishing a path.
	private boolean dig = false;
	
	
	/**
	 * Constructs a new rabbit at the specified tile location.
	 * 
	 *  @param tileX the x coordinate location of the tile that rabbit will spawn on.
	 *  @param tileY the y coordinate location of the tile that rabbit will spawn on.
	 */
	public Rabbit(int tileX, int tileY){
		
		this.tag = "rabbit";
		
		// set the spawn location.
		this.tileX = tileX;
		this.tileY = tileY;
		
		// set posX to reflect tile location
		this.posX = tileX*GameManager.TS;
		this.posY = tileY*GameManager.TS;
		
		// animation
		animationFrames = 3;
		
		
		// apply padding.
		this.topPadding = 2;
		this.leftRightPadding = 3;
		this.width = 16 - 2*leftRightPadding;
		this.height = 16 - topPadding;
		
		// Super jumps
		jump = -10;
		
		// Sets rabbit speed, faster then most NPCs... NOTE: moved back to 100 from 150 due to missing tiles on paths
		speed = 100;
		
		// Initialize hit-box and add to the components list
		hitBox = new AABBComponent(this);
		addComponent(hitBox);
		
		// Rabbits belong on the 'good' team
		team = 0;
	}

	
	
	
	@Override
	protected void attack(GameManager gm, int direction) {
		// TODO Auto-generated method stub

	}

	
	
	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		
		
		// Destroy the tile below the rabbit after completing a path, if digging is enabled. 
		if(pathingComplete && dig){
			
			EventManager.addEvent(new TileDestruction(gm.getLevelTile(tileX, tileY+1)));
			pathingComplete = false;
			PathFinderTwo.setPathMap(gm, tileX, tileY);
		}
		

		// Behavior determined by NPCs current ActionType
		switch(actionType){
		case IDLE:
			idlePathing(gm);
			break;
		case PATH: 
			break;
		case ATTACK:
			aggroIdle(gm);
			break;
		case FOLLOW:
			break;
		case WAIT:
			break;
		default:
			break;
		}


		// Apply movement commands
		npcMovement(gc, gm, dt);
		
		// Animating movement
		npcWalkingAnimation(dt);

		// General NPC update
		npcUpdate(gc, gm, dt);
		
	}

	@Override
	public void render(Renderer r) {
		
		if(waiting){
			animationState = -1;
		}
		
		r.drawImageTile(rabbitImage, (int)posX, (int)posY - imageOffY, (int)animationState+1, direction);
		
		renderComponents(r);
		
		
		// render path if debug panel is active
		if(GameManager.showDebug){
			renderPath(r);
			vision.renderVision(r);
			
		}
			
		

	}

	@Override
	public void collision(GameObject other, AABBComponent otherHitBox) {
		// TODO Auto-generated method stub

	}

}
