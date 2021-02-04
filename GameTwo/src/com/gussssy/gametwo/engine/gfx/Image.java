package com.gussssy.gametwo.engine.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Image{
	
	private int width, height;
	private int[] pixels;
	private boolean alpha = false;
	private int lightBlock = Light.NONE;
	private boolean lightEffects = true;
	
	/**
	 * Primary Image Construcotr. Makes an image from a file, the location given by the String argument 'path'.
	 */
	public Image(String path){
		
		BufferedImage buffImage = null;
		
		try{
			
			buffImage = ImageIO.read(Image.class.getResourceAsStream(path));
		
		} catch (IOException e){e.printStackTrace();}
		
		width = buffImage.getWidth();
		height = buffImage.getHeight();
		pixels = buffImage.getRGB(0 ,0 , width, height, null, 0, width);
		
		buffImage.flush();
		
	}
	
	/**
	 * Alternate construcor when making an image with a pixel [] as opposed to an image file 
	 * 
	 */
	public Image(int[] pixels, BufferedImage buffImage){
		this.pixels = pixels;
		this.height = buffImage.getHeight();
		this.width = buffImage.getWidth();
	}
	
	/**
	 * Constructor Used by TileImage class to pull out / make an image from a single tile of a tile image 
	 **/
	public Image(int[] pixels, int width, int height){
		
		this.pixels = pixels;
		this.width = width;
		this.height = height;
	}

	public int getWidth(){
		return width;
	}

	public void setWidth(int width){
		this.width = width;
	}

	public int getHeight(){
		return height;
	}

	public void setHeight(int height){
		this.height = height;
	}

	public int[] getPixels(){
		return pixels;
	}

	public void setPixels(int[] pixels){
		this.pixels = pixels;
	}

	public boolean isAlpha(){
		return alpha;
	}

	public void setAlpha(boolean alpha){
		this.alpha = alpha;
	}

	public int getLightBlock(){
		return lightBlock;
	}

	public void setLightBlock(int lightBlock){
		this.lightBlock = lightBlock;
	}

	public boolean isLightEffects() {
		return lightEffects;
	}

	public void setLightEffects(boolean lightEffects) {
		this.lightEffects = lightEffects;
	}

}
