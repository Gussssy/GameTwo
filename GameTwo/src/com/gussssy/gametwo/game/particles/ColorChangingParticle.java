package com.gussssy.gametwo.game.particles;

import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.level.Level;


/**
 * 
 * 
 */
public class ColorChangingParticle extends Particle{
	
	
	// The initial color of this particle 
	private int startColor;
	
	// The color of the particle in its last frame
	private int endColor;
	
	// The color this frame 
	private int currentColor;
	
	
	
	// How many frames before this pixel will complete its color change and no longer be visible 
	private int lifeTime;
	
	
	// Dimensions of the particle
	private int width, height;
	
	
	// Whether or not this particle will be effected by gravity
	private boolean gravity;
	
	// TESTING decellleration
	private float decel = 10;
	
	
	// The current value of each aRGB component this frame
	private float currentRed;
	private float currentBlue;
	private float currentGreen; 
	private float currentAlpha;
	
	// How much each aRGB component changes per frame
	private float redChange;
	private float greenChange;
	private float blueChange;
	private float alphaChange;
	
	
	
	/**
	 * Creates a new color changing particle.
	 * 
	 * @param posX the initial location of the particle
	 * @param posY the initial location of the particle 
	 * @param vx the horizontal velocity of this particle
	 * @param vy the vertical velocity of this particle
	 * @param startColor the initial color of this particle when it is spawned
	 * @param endColor the final color of this particle when it dies 
	 * @param lifeTime how many frames this particle will live for, and how long the particle will take to reach its end color
	 * @param gravity whether or not this particle will be effected by gravity
	 * 
	 */
	public ColorChangingParticle(float posX, float posY, float vx, float vy, int startColor, int endColor, int lifeTime, boolean gravity){
		
		this.posX = posX;
		this.posY = posY;
		this.vx = vx;
		this.vy = vy;
		this.startColor = startColor;
		this.endColor = endColor;
		this.lifeTime = lifeTime;
		this.gravity = gravity;
		
		this.width = 1;
		this.height = 1;
		
		currentColor = startColor;
		
		// determine how much per step color change for red
		int red = (startColor >> 16) & 0xff;
		System.out.println("\nred: " + red);
		currentRed = red;
		
		int redFinal = (endColor >> 16) & 0xff;
		System.out.println("red final: " + redFinal);
		
		redChange = ((float)redFinal - (float)red)/lifeTime;
		System.out.println("Red change per frame: " + redChange );
		
		
		
		
		int green = (startColor >> 8) & 0xff;
		System.out.println("\ngreen: " + green);
		currentGreen = green;
		
		int greenFinal = (endColor >> 8) & 0xff;
		System.out.println("green final: " + greenFinal);
		
		greenChange = ((float)greenFinal - (float)green)/lifeTime;
		System.out.println("Green change per frame: " + greenChange );
		
		
		
		int blue = startColor & 0xff;
		System.out.println("\nblue: " + blue);
		currentBlue = blue; 
		
		int blueFinal = endColor & 0xff;
		System.out.println("blue final: " + blueFinal);
		
		blueChange = ((float)blueFinal - (float)blue)/lifeTime;
		System.out.println("Blue change per frame: " + blueChange );
		
		
		
		int alpha = (startColor >> 24) & 0xff;
		System.out.println("\nalpha: " + alpha);
		currentAlpha = alpha;
		
		int alphaFinal = (endColor >> 24) & 0xff;
		System.out.println("alpha final: " + alphaFinal);
		
		alphaChange = ((float)alphaFinal - (float)alpha)/lifeTime;
		System.out.println("Alpha change per frame: " + alphaChange );
		
		
		
	}
	
	

	@Override
	public void update(GameManager gm, float dt) {
		
		// Death control
		if(lifeTime == 0 ){
			
			dead = true;
			return;
			
			}
		
		lifeTime--;
		
		
		
		// Update the color
		
		// Increment color values
		currentRed += redChange;
		currentGreen += greenChange;
		currentBlue += blueChange;
		currentAlpha += alphaChange; 
		
		// Put the RGB components together for this frame. 
		currentColor = ((int)currentAlpha <<24 | (int)currentRed << 16 | (int)currentGreen << 8 | (int)currentBlue);
		
		
		
		
		// Update position

		posX += vx * dt;
		posY += vy * dt;
		
		// apply gravity if gravity is enabled
		if(gravity)vy += Level.gravity * dt * 10;
		
		// testing slowing down of particles
		vx -= 1 * decel * dt;
		
		
	}
	
	
	public void Render(Renderer r){
		
		if(dead == true)return;
		
		r.drawFillRect((int)posX, (int)posY, width, height, currentColor);
		
	}

}
