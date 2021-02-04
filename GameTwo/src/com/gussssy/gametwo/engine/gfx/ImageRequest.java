package com.gussssy.gametwo.engine.gfx;

public class ImageRequest{
	
	public Image image;
	public int offX, offY;
	public int zDepth;

	public ImageRequest(Image image, int offX, int offY, int zDepth){

		//System.out.println("Image request made, offX = " + offX + ", offY = " + offY);
		
		this.image = image;
		this.offX = offX;
		this.offY = offY;
		this.zDepth = zDepth;
		
	}

}
