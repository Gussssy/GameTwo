package com.gussssy.gametwo.engine.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RotatedImageSet{
	
	private int width, height;
	private int[] pixels;
	
	public RotatedImageSet(String path){
		
		BufferedImage buffImage = null;
		
		try{
			
			buffImage = ImageIO.read(Image.class.getResourceAsStream(path));
		
		} catch (IOException e){e.printStackTrace();}
		
		width = buffImage.getWidth();
		height = buffImage.getHeight();
		pixels = buffImage.getRGB(0 ,0 , width, height, null, 0, width);
		
		buffImage.flush();
		
		
		
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

}
