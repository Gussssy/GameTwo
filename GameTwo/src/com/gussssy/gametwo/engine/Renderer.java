package com.gussssy.gametwo.engine;

import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.gussssy.gametwo.engine.gfx.Font;
import com.gussssy.gametwo.engine.gfx.Image;
import com.gussssy.gametwo.engine.gfx.ImageRequest;
import com.gussssy.gametwo.engine.gfx.ImageTile;
import com.gussssy.gametwo.engine.gfx.Light;
import com.gussssy.gametwo.engine.gfx.LightRequest;

/**
 * Performs the Rendering. 
 * 
 * Adapted from Maljowops video tutorial series on youtube: 
 * https://www.youtube.com/watch?v=4iPEjFUZNsw&list=PL7dwpoQd3a8j6C9p5LqHzYFSkii6iWPZF&ab_channel=Majoolwip
 * 
 * Much of this class has not changed much from the tutorial. 
 */
public class Renderer{

	// Width and Height
	private int pW, pH;
	
	// Pixels
	private int[] pixels; 
	
	// Depth
	private int[] zBuffer;
	private int zDepth = 0;
	
	// Light 
	private int[] lightMap;
	private int[] lightBlock;

	// Camera offsets
	private int camX, camY;

	// Ambient Color. (this is set by the GameManager after construction)
	private int ambientColor = 0xff232323;
	
	// Font
	private Font font = Font.STANDARD;


	// Contains images that have transperancy. They will be drawn after all oqapue images have been drawn.
	private ArrayList<ImageRequest> imageRequests = new ArrayList<ImageRequest>();
	
	// Contains lights that will be drawn after everything else has been drawn. 
	private ArrayList<LightRequest> lightRequests = new ArrayList<LightRequest>();
	
	// When true, the renderer will process lighting and transparent images
	private boolean processing = false;

	


	/**
	 * Renderer Constructor
	 */
	public Renderer(GameContainer gc){

		// Set width and height
		pW = gc.getWidth();
		pH = gc.getHeight();
		
		//gives int array p, direct access to the pixel data of the image in the window
		pixels = ((DataBufferInt)gc.getWindow().getImage().getRaster().getDataBuffer()).getData(); 

		// Initialise arrays used for lighting and depth. Correspond to the pixels array so are set to the same length. 
		zBuffer = new int[pixels.length];
		lightMap = new int[pixels.length];
		lightBlock = new int[pixels.length];

	}

	/**
	 * Clear the pixels and the associated depth and lightring arrays each frame.  
	 */
	public void clear(){

		for(int i = 0; i < pixels.length; i++){
			//pixels[i] += i;
			pixels[i] = 0;
			zBuffer[i] = 0;
			lightMap[i] = ambientColor;
			lightBlock[i] = 0;
		}
	}



	// Contains the current pixel's index so it isnt recalculated each time it is needed
	// NOTE: is there any benefoit to this being outside the method so we are not declaring an internal variable, thousands of times per frame. 
	private int pixelIndex;

	/**
	 * Set the pixel to the specified colorValue, applies alpha blending if required.
	 **/
	public void setPixel(int x, int y, int colorValue){

		//get the alpha value of the incoming pixel
		int alpha = ((colorValue >> 24) & 0xff);

		
		// If x or y value is out of bounds OR the colour is fully transparent, do not set this pixel
		if((x < 0 || x >= pW || y < 0 || y >= pH) || alpha == 0){
			return;
		}

		// calculate pixel index so that this calculation does not have to be performed several more times. 
		pixelIndex = x + y * pW;
		
		
		// setting z buffer for this pixel
		zBuffer[pixelIndex] = zDepth;

		// Is the current pixel hidden by another pixel of lesser depth?
		if(zBuffer[pixelIndex] > zDepth){
			// Yes this pixel is behind a previosuly set pixel. Do nothing
			return;
		}
 


		// Is this pixel Opaque?
		if(alpha == 255){

			// This pixel is opaque, no blending required
			pixels[pixelIndex] = colorValue; // p[] is 1d, but is storing 2d image. This converts 2d representation to 1d.  


		} else {
			
			// ALPHA BLENDING

			// This pixel is to be transparent, alpha blending is required

			// the color this pixel was previously set too, if at all 
			int setColor = pixels[pixelIndex];

			//acess the red/green/blue componment of already set pixel: (setColor >> 16) & 0xff
			// then SUBTRACT from this the difference between the currently set red/green/blue and the incoming red component
			int newRed = ((setColor >> 16) & 0xff) - (int)((((setColor >> 16) & 0xff) - ((colorValue >> 16) & 0xff)) * (alpha/255f));
			int newGreen = ((setColor >> 8) & 0xff) - (int)((((setColor >> 8) & 0xff) - ((colorValue >> 8) & 0xff)) * (alpha/255f));
			int newBlue = (setColor & 0xff) - (int)(((setColor & 0xff) - (colorValue  & 0xff)) * (alpha/255f));
			

			// Set the pixel with the new blended values
			pixels[pixelIndex] = (255 << 24 | newRed << 16 | newGreen << 8 | newBlue); 
			// in the turial he removed 255 << 24 as is not needed, Will remove when i understand why
		}

	}


	/**
	 * Set Light block. 
	 * 
	 * This method is identical to the tutorial method. I do not use this feature at present.
	 */
	public void setLightBlock(int x, int y, int value){

		if(x < 0 || x >= pW || y < 0 || y >= pH){
			return;
		}

		//again not 100% sure about this
		if(zBuffer[x + y * pW] > zDepth){

			return;
		}

		lightBlock[x + y * pW] = value;

	}

	
	
	/** 
	 * Applys lighting effects.
	 * 
	 *  This has not been modified from the tutorial series I used to start this game.  
	 * 
	 **/
	public void setLightMap(int x, int y, int value){

		
		// This creates a really shitty effect with snowflakes, gives the fringes of the snowflakes way more colour, almost as if the alpha is being pumped up... 
		/*
		// Are we trying to apply light to the background image? 
		if(zBuffer[x + y * pW] == -1){
			// dont apply light where zBuffer is -1
			return;
		}*/
		
		

		if(x < 0 || x >= pW || y < 0 || y >= pH){
			return;
		}

		int baseColor = lightMap[x + y * pW];
		//int finalColor = 0;

		int maxRed = Math.max(((baseColor >> 16) & 0xff), ((value >> 16) & 0xff));
		int maxGreen = Math.max(((baseColor >> 8) & 0xff), ((value >> 8) & 0xff));
		int maxBlue = Math.max((baseColor) & 0xff, (value) & 0xff);

		lightMap[x + y * pW] = (maxRed << 16 | maxGreen << 8 | maxBlue);

	}



	/**
	 * Renders queued images with transperancy and then Renders Lighting Effects.
	 * 
	 * Images with transperancy are stored in an array list for rendering after opaque/non-transparent images have been rendered.
	 * This method sorts the arraylist by descending depth values then renders the images in this order. 
	 **/
	public void process(){

		// set processing to true so images with alpha will be rendered
		processing = true;

		// Sort ImageRequests by decesnding order of zDepth
		Collections.sort(imageRequests, new Comparator<ImageRequest>(){

			@Override
			public int compare(ImageRequest ir0, ImageRequest ir1){

				if(ir0.zDepth > ir1.zDepth){
					// if the depth of ir0 is more then ir1, ir0 must be drawn first
					return -1;
				}else{
					return 1;
				}
			}
		});


		// process images to be drawn with alpha
		for(ImageRequest ir : imageRequests){

			//System.out.println("Image Depth: "+ ir.zDepth);
			setzDepth(ir.zDepth);
			//if(ir.zDepth != 0)System.out.println("Z depth: " + ir.zDepth);
			//System.out.println("Image Request to be processed, offX = " + ir.offX + ", offY = " + ir.offY);
			drawImage(ir.image, ir.offX, ir.offY);
		}

		// processing image requests finished, clear the list
		imageRequests.clear();



		// Process Lighting: Merge Pixels array with LightMap array
		for(int i = 0; i < pixels.length; i++ ){

			// pull out the inbdividaul components from the light map then turn to a float bewtween 0 and 1
			float red = ((lightMap[i] >> 16) & 0xff) / 255f;
			float green = ((lightMap[i] >> 8) & 0xff) / 255f;
			float blue = (lightMap[i] & 0xff) / 255f;

			// (0xff equivalent to 255)

			pixels[i] = ((int)(((pixels[i] >> 16) & 0xff) * red ) << 16 | (int)(((pixels[i] >> 8) & 0xff) * green ) << 8 | (int)((pixels[i] & 0xff) * blue ));
			// modify the set pixel colour by pulling out compoents, bit shift right multiplying by light map values, then reverse bit shift left to store

			// Draw Lighting Here
			for(LightRequest lr : lightRequests){
				drawLightRequest(lr.light, lr.locationX, lr.locationY);
			}
			lightRequests.clear();


		}
		
		// TODO: draw things not to be effected by lighting
		

		// reset processing to false for next frame
		processing = false;

	}

	
	/**
	 * Draws text. 
	 */
	public void drawText(String text, int offX, int offY, int color){

		//Shift image to the current screen space alignment
		offX -= camX;
		offY -= camY;
		
		
		//text = text.toUpperCase();
		//System.out.println(text);
		int offset = 0;



		//loop through chars in the text
		for(int i = 0; i < text.length(); i++)
		{

			//determine index of this char in our fontimage e.g if a space, unicode will be 0. space is unicode 32, so - 32 is 0, the position of space we are using....
			int unicode = text.codePointAt(i);

			//Loop through row
			for(int y = 0; y < font.getFontImage().getHeight(); y++ )
			{
				//Loop through pixels of a row
				for(int x = 0; x < font.getWidths()[unicode]; x++)
				{
					// if pixel is white, this pixel needs to be drawn
					if(font.getFontImage().getPixels()[(x + font.getOffsets()[unicode]) + y * font.getFontImage().getWidth()] == 0xffffffff)
					{
						setPixel(x + offX + offset, y + offY, color);
					}
				}
			}
			
			//shift offset to right edge of last char
			offset += font.getWidths()[unicode];

		}
	}


	/**
	 * Draws an image.  
	 */
	public void drawImage(Image image, int offX, int offY){
		
		

		if(image.isAlpha() && !processing){
			//System.out.println("Making Image Request, offX = " + offX + ", offY = " + offY);
			imageRequests.add(new ImageRequest(image, offX, offY, zDepth));
			return;
		}
		
		//Shift image to the current screen space alignment
		offX -= camX;
		offY -= camY;
		
		//System.out.println("Rendering Image Request, offX = " + offX + ", offY = " + offY + "width: " + image.getWidth());

		int startX = 0;					
		int startY = 0;
		int stopX = image.getWidth();
		int stopY = image.getHeight();

		// Dont render if image entirely off left edge
		if(offX < -stopX){
			//System.out.println("Too far left, wont render");
			return;
		}

		// Don't render if image is entirely off right edge
		if(offX > pW){
			//System.out.println("Too far right, wont render");
			return;
		}

		// Dont render if image entirely above top edge
		if(offY < -stopY){
			//System.out.println("Too high, wont render");
			return;
		}

		// Don't render if image is entirely below bottom edge
		if(offY > pH){
			//System.out.println("Too low, wont render");
			return;
		}

		//Clipping on right edge
		if((image.getWidth() + offX) > pW) //offX + 10 for border
		{
			stopX = pW - offX; //offX - 10 for border
			//System.out.println("Clipping at right edge. Stop X: "+ stopX);
		}

		//Clipping left edge
		if(offX < 0) //0 + 10 for border
		{
			startX = - offX; //offX + 10 for border
			//System.out.println("Clipping at left edge. Start X: "+ startX);
		}

		//Clipping on bottom edge
		if((image.getHeight() + offY) > pH) //offX + 10 for border
		{
			stopY = pH - offY; //offY - 10 for border
			//System.out.println("Clipping at bottom edge. Stop Y: "+ stopY);
		}

		//Clipping on top edge
		if(offY < 0) //0 + 10 for border
		{
			startY = - offY; //offY + 10 for border
			//System.out.println("Clipping at top edge. Start Y: "+ startY);
		}

		//Loop through setting all pixels within the screen
		for(int y = startY; y < stopY; y++)
		{
			for(int x = startX; x < stopX; x++)
			{
				setPixel(x + offX, y + offY, image.getPixels()[x + y * image.getWidth()]);
				setLightBlock(x + offX, y + offY, image.getLightBlock());
			}
		}

	}

	
	/**
	 * Draws a single tile of a tile image. 
	 */
	public void drawImageTile(ImageTile tile, int offX, int offY, int tileX, int tileY){
		

		if(tile.isAlpha() && !processing){
			imageRequests.add(new ImageRequest(tile.getTileImage(tileX, tileY), offX, offY, zDepth));
			return;
		}
		
		//Shift image to the current screen space alignment
		offX -= camX;
		offY -= camY;

		int startX = 0;					
		int startY = 0;
		int stopX = tile.getTileWidth();
		int stopY = tile.getTileHeight();

		// Dont render if image entirely off left edge
		if(offX < -stopX){
			//System.out.println("Too far left, wont render");
			return;
		}

		// Don't render if image is entirely off right edge
		if(offX > pW){
			//System.out.println("Too far right, wont render");
			return;
		}

		// Dont render if image entirely above top edge
		if(offY < -stopY){
			//System.out.println("Too high, wont render");
			return;
		}

		// Don't render if image is entirely below bottom edge
		if(offY > pH){
			//System.out.println("Too low, wont render");
			return;
		}

		//Clipping on right edge
		if((tile.getTileWidth() + offX) > pW) //offX + 10 for border
		{
			stopX = pW - offX; //offX - 10 for border
			//System.out.println("Clipping on right Edge. Stop X: "+ stopX);
		}

		//Clipping left edge
		if(offX < 0) //0 + 10 for border
		{
			startX = - offX; //offX + 10 for border
			//System.out.println("Start X: "+ startX);
		}

		//Clipping on bottom edge
		if((tile.getTileHeight() + offY) > pH) //offX + 10 for border
		{
			stopY = pH - offY; //offY - 10 for border
			//System.out.println("Stop Y: "+ stopY);
		}

		//Clipping on top edge
		if(offY < 0) //0 + 10 for border
		{
			startY = - offY; //offY + 10 for border
			//System.out.println("Start Y: "+ startY);
		}

		//Loop through setting all pixels within the screen
		for(int y = startY; y < stopY; y++)
		{
			for(int x = startX; x < stopX; x++)
			{
				setPixel(x + offX, y + offY, tile.getPixels()[(x + tileX * tile.getTileWidth()) + (y + tileY * tile.getTileHeight()) * tile.getWidth()]);
				setLightBlock(x + offX, y + offY, tile.getLightBlock());
			}
		}

	}


	/**
	 * Draws a hollow rectangle.  
	 */
	public void drawRect(int offX, int offY, int width, int height, int color){	

		//Shift rectangle to the current screen space alignment
		offX -= camX;
		offY -= camY;

		// Dont render if image entirely off left edge
		if(offX < -width){
			//System.out.println("Rect is fully off left edge, will not render"); //works for sure
			return;
		}
		// Dont render if image entirely above top edge
		if(offY < -height){
			//System.out.println("Rect is fully above top edge, will not render"); //works for sure
			return;
		}
		// Don't render if image is entirely off right edge
		if(offX >= pW){
			//System.out.println("Rect is fully off right edge, will not render");
			return;
		}
		// Don't render if image is entirely below bottom edge
		if(offY >= pH){
			//System.out.println("Rect is fully below bottom edge, will not render");
			return;
		}

		// RECT IS WITHIN THE SCREEN SPACE

		// BUT MAY ONLY BE PARTIALLY IN SCREEN

		// CLIPPING:

		// variables below ensure only portions on screen are rendered
		int startX = 0;
		int startY = 0;
		int stopX = width;
		int stopY = height;

		//Clipping on right edge
		if((width + offX) > pW){
			stopX = pW - offX; 
			//System.out.println("Clipping on right edge");
		}
		//Clipping left edge
		if(offX < 0){
			startX = - offX;
			//System.out.println("Clipping on left edge");
		}
		//Clipping on bottom edge
		if((height + offY) > pH){
			stopY = pH - offY;
			//System.out.println("Clipping on bottom edge");
		}
		//Clipping on top edge
		if(offY < 0){
			startY = - offY;
			//System.out.println("Clipping on top edge");
		}
		

		
		
		// Set pixels for this rect
		for(int y = startY; y < stopY; y++)
		{	
			// LEFT EDGE
			setPixel(offX, offY + y, color);			//left edge
			
			// RIGHT EDGE
			setPixel(offX + width-1, offY + y, color);	//right edge
		}
		
		for(int x = startX; x < stopX; x++)
		{
			// TOP EDGE
			setPixel(offX + x, offY, color);			//TOP EDGE
			
			// BOTTOM EDGE
			setPixel(offX + x, offY + height -1, color);	//BOTTOM EDGE
		}
		
		// NOTE: i fixed the issue where it was drawing rects 1 pixel to large on both axis BUT i don'y know why the algoithm was wrong in the first place 
	}


	/**
	 * Draws a filled rectangle.
	 */
	public void drawFillRect(int offX, int offY, int width, int height, int color){

		//Shift rectangle to the current screen space alignment
		offX -= camX;
		offY -= camY;


		// DONT RENDER IF RECT IS NOT WITHIN THE SCREEN SPACE
		// Dont render if image entirely off left edge
		if(offX < -width){
			//System.out.println("Rect is fully off left edge, will not render"); //works for sure
			return;
		}
		// Dont render if image entirely above top edge
		if(offY < -height){
			//System.out.println("Rect is fully above top edge, will not render"); //works for sure
			return;
		}
		// Don't render if image is entirely off right edge
		if(offX >= pW){
			//System.out.println("Rect is fully off right edge, will not render");
			return;
		}
		// Don't render if image is entirely below bottom edge
		if(offY >= pH){
			//System.out.println("Rect is fully below bottom edge, will not render");
			return;
		}


		// RECT IS WITHIN THE SCREEN SPACE

		// BUT MAY BE PARTIALLY OFF SCREEN

		// SO CLIPPING: 

		// variables below ensure only portions on screen are rendered
		int startX = 0;
		int startY = 0;
		int stopX = width;
		int stopY = height;

		//Clipping on right edge
		if((width + offX) > pW){
			stopX = pW - offX; 
			//System.out.println("Clipping on right edge");
		}
		//Clipping left edge
		if(offX < 0){
			startX = - offX;
			//System.out.println("Clipping on left edge");
		}
		//Clipping on bottom edge
		if((height + offY) > pH){
			stopY = pH - offY;
			//System.out.println("Clipping on bottom edge");
		}
		//Clipping on top edge
		if(offY < 0){
			startY = - offY;
			//System.out.println("Clipping on top edge");
		}

		// END OF CLIPPING


		// SET THE PIXELS
		for(int y = startY; y < stopY; y++)
		{
			for(int x = startX; x < stopX; x++)
			{
				setPixel(offX + x, offY + y, color);
			}
		}
	}


	public void drawLight(Light light, int offX, int offY){

		// light must be drawn last so light request is made to be executed after everything else has been rendered
		lightRequests.add(new LightRequest(light, offX, offY));
	}


	private void drawLightRequest(Light l, int offX, int offY){
		
		//Shift light to the current screen space alignment
		offX -= camX;
		offY -= camY;

		for(int i = 0; i <= l.getDiameter(); i++){

			//Drawing 4 lines spaced apart 90o degrees
			//	l.getRadius, l.getRadius -> we are always going to start at the center of our light
			drawLightLine(l, l.getRadius(), l.getRadius(), i, 0, offX, offY); 				//move across top
			drawLightLine(l, l.getRadius(), l.getRadius(), i, l.getDiameter(), offX, offY); // move across bottom 
			drawLightLine(l, l.getRadius(), l.getRadius(), 0, i, offX, offY); 				// move across left 
			drawLightLine(l, l.getRadius(), l.getRadius(), l.getDiameter(), i, offX, offY); // move across right	

		}

	}

	public void drawLightLine(Light l, int x0, int y0, int x1, int y1, int offX, int offY){

		// Algorithm : Brensenham Line Algorithm - uses only integer addition/subtraction and bit shifting, all cheap operations so is efficeint 
		// 		 Is a Incremental error algorithm:  rasterization algorithms which use simple integer arithmetic to update an error term that determines 
		//		 if another quantity is incremented, avoiding the need for expensive division or multiplication operations

		// Start of line:	x0, y0
		// End of line:		x1, y1

		int dx = Math.abs(x1 - x0);
		int dy = Math.abs(y1 - y0);

		int sx = x0 < x1 ? 1 : -1;	//slope x ?  is 1 or -1
		int sy = y0 < y1 ? 1 : -1;	//sloper y ?

		int error = dx - dy;
		int error2;

		int radius = l.getRadius();

		int screenX;
		int screenY;
		int lightValue;

		while(true){

			screenX = x0 - radius + offX;
			screenY = y0 - radius + offY;

			if(screenX < 0 || screenX >= pW || screenY < 0 || screenY >= pH)
				return;

			lightValue = l.getLightValue(x0, y0);

			// if light is now 0, stop drawing line, it is done
			if(lightValue == 0) 
				return;

			if(lightBlock[screenX + screenY * pW] == Light.FULL)
				return;

			setLightMap(screenX, screenY, l.getLightMap()[x0 + y0 * l.getDiameter()]);

			// if we have reached where we are going, break out of the loop
			if(x0 == x1 && y0 == y1)
				break;

			error2 = error + error;

			if(error2 > -1 * dy){
				error -= dy;
				x0 += sx;
			}

			if(error2 < dx){
				error += dx;
				y0 += sy;
			}
		}
	}
	
	public void drawLine(int x0, int y0, int x1, int y1, int color){
		
		
		// shift line to current screen space 
		x0 -= camX;
		y0 -= camY;
		x1 -= camX;
		y1 -= camY;
		
		
		// get the slopes
		int dx = Math.abs(x1 - x0);
		int dy = Math.abs(y1 - y0);

		// are the slopes positive or negative for x and y components? 
		int sx = x0 < x1 ? 1 : -1;	//slope x ?  is 1 or -1
		int sy = y0 < y1 ? 1 : -1;	//slope y ?
		
		int error = dx - dy;
		int error2;
		
		// These are not used? 
		//int lineX = x0;
		//int lineY = y0;
		
		
		while(true){
			
			setPixel(x0, y0, color);
			
			// if we have reached where we are going, break out of the loop
			if(x0 == x1 && y0 == y1)
				break;
			
			// set error2 to be twice the value of error
			error2 = error + error;

			if(error2 > -1 * dy){
				error -= dy;
				x0 += sx;
			}

			if(error2 < dx){
				error += dx;
				y0 += sy;
			}	
		}
		
		
	}



	public int getzDepth(){
		return zDepth;
	}

	public void setzDepth(int zDepth){
		this.zDepth = zDepth;
	}

	public void setAmbientColor(int ambientColor){
		this.ambientColor = ambientColor;
	}

	public int getAmbientColor(){
		return ambientColor;
	}

	public int getCamX(){
		return camX;
	}

	public void setCamX(int camX){
		this.camX = camX;
	}

	public int getCamY(){
		return camY;
	}

	public void setCamY(int camY){
		this.camY = camY;
	}

}
