package com.gussssy.gametwo.engine.gfx;

public class Font{
	
	public static final Font STANDARD = new Font("/ComicSans10.png");
	
	private Image fontImage;
	private int[] offsets;
	private int[] widths;
	
	private int blueMarker = 0xff0000ff;
	private int yellowMarker = 0xffffff00;
	//0x indicates the rest of the int is to be read in hex
	// the following two didgits are the alpha value, ff is fully opaque
	// the rest is the rgb values
	
	
	
	public Font(String path){
		
		fontImage = new Image(path);
		
		offsets = new int[256];
		widths = new int[256];
		
		int unicodeIndex = 0;
		
		//Loop through top row looking for blue and yellow markler pixels indicating the (blue) start and end (yellow) of chars
		for(int i = 0; i < fontImage.getWidth(); i++){
			
			//blue marker pixel indicates the start of a new char
			if(fontImage.getPixels()[i] == blueMarker){
				offsets[unicodeIndex] = i;
			}
			
			if(fontImage.getPixels()[i] == yellowMarker){
				widths[unicodeIndex] = i - offsets[unicodeIndex]; //gives the width of the char
				unicodeIndex++;
			}
			
		}
	}



	public Image getFontImage(){
		return fontImage;
	}



	public int[] getOffsets(){
		return offsets;
	}



	public int[] getWidths(){
		return widths;
	}

}
