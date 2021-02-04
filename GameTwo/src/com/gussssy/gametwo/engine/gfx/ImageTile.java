package com.gussssy.gametwo.engine.gfx;

public class ImageTile extends Image{
	
	private int tileWidth, tileHeight;
	
	
	public ImageTile(String path, int tileWidth, int tileHeight){
		
		super(path);
		
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		
	}
	
	/**
	 * Takes a single tile from the ImageTile and converts it to an Image
	 *  - location of tile specified by tileX and tileY ...? 
	 **/
	public Image getTileImage(int tileX, int tileY){
		
		// makes an array of pixels to store the tile we are extracting
		int[] pixels = new int[tileX * tileY];
		
		for(int y = 0; y < tileHeight; y++){
			for(int x = 0; x < tileWidth; x++){
				
				// extract pixel from ImageTile and store in new pixel array 
				pixels[x + y * tileWidth] = this.getPixels()[(x + tileX * tileWidth) + (y + tileY * tileHeight) * this.getWidth()];
				
			}
		}
		
		return new Image(pixels, tileWidth, tileHeight);
		
	}


	public int getTileWidth(){
		return tileWidth;
	}


	public void setTileWidth(int tileWidth){
		this.tileWidth = tileWidth;
	}


	public int getTileHeight(){
		return tileHeight;
	}


	public void setTileHeight(int tileHeight){
		this.tileHeight = tileHeight;
	}

}
