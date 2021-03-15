package com.gussssy.gametwo.game.particles;

import java.util.concurrent.ThreadLocalRandom;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.engine.gfx.Image;
import com.gussssy.gametwo.game.Event;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.Textures;
import com.gussssy.gametwo.game.level.LevelTile;

/**
 * Creates a visual effect when a tile is destroyed. 
 * 
 *  Gives the appearance of the tile collapsing and falling into a pile of rubble.
 * 
 *  The pixels of the destroyed tile's texture are:
 *  	- converted into individual particles that will 
 *  	- have x and y velocity, 
 *  	- be effected by gravity and tile collisons.
 */
public class TileDestruction implements Event{
	
	
	
	// controls printing of debug info to console
	private boolean print = false;
	
	// holds a particle for each pixel in the destroyed tiles texture.
	private PixelParticle[] particles = new PixelParticle[GameManager.TS * GameManager.TS];
	
	// the pixels of the destroyed tiles texture
	private int[] tilePixels;
	
	// the image of the tile being destroyed
	//		- for now, always dirt
	private Image tileImage = Textures.dirtBack;
	
	
	
	
	/**
	 * TileDestruction Constructor
	 * 
	 * - Sets the destroyed tiles type to air.
	 * - Makes a particle for each pixel assigning a random x and v velocity. 
	 * 
	 *  @param tile - the tile that will be destroyed
	 */
	public TileDestruction(LevelTile tile){
		
		// Debugging
		if(print)System.out.println("Tile Destruction Event Occured on level tile:\n\t " + tile.toString() );
		
		// set tile to air, essentially deleting this tile from the level
		tile.type = 0;
		
		
		// get the pixels from the tiles texture image
		//			NOTE: will always be dirt for now
		tilePixels = tileImage.getPixels();
		
		
		// integers for tracking pixel coordinates so particles have the same initial position as their coresponding pixels
		int x = 0;
		int y = 0;
		
		
		// Loop through each pixel in the tile texture image, creating a particle for each pixel
		for(int i = 0; i < tilePixels.length ; i++){
			
			// generate random x and y velocities for the next particle 
			float vy = (float)ThreadLocalRandom.current().nextDouble(-3, 2);
			float vx = (float)ThreadLocalRandom.current().nextDouble(-0.5, 0.5);
			
			// make the particle corresponding to the pixel
			// The location of the particle will initially be the same location as the pixel it weas derived from.
			PixelParticle p = new PixelParticle(tile.getTileX()*GameManager.TS + x, tile.getTileY()*GameManager.TS + y, vx, vy, tilePixels[i]);
			
			// add the new particle to the array
			particles[i] = p;
			
			
			
			// Setting location for the particle generated in the next loop iteration.
			
			// increment x position of the pixel by 1
			x++;
			
			// reach the end of a row, return x to 0 and increment y 
			if(x >= 16){
				x = 0;
				y++;
			}
			
		}
		
	}


	
	
	/**
	 *  Updates all the particles each frame. 
	 *  
	 *  Particles will move with thier velocity untill stopped by collion with a tile.
	 */
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		for(PixelParticle p : particles){
			p.update(gm, dt);
		}
		
	}

	
	

	@Override
	public void endEvent() {
		// TODO Auto-generated method stub
		
	}


	
	
	/**
	 * Render each particle. 
	 * 
	 *  Draws 1*1 rectangles with the color taken from the original texture image. 
	 */
	@Override
	public void render(Renderer r) {
		
		for(PixelParticle p : particles){
			
			r.drawFillRect((int)p.posX, (int)p.posY, 1, 1, p.getColor());
			
		}
		
	}

}
