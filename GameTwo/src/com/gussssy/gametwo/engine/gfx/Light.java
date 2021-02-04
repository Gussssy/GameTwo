package com.gussssy.gametwo.engine.gfx;

public class Light{
	
	public static final int NONE = 0;
	public static final int FULL = 1;
	
	private int radius, diameter, color;
	private int[] lightMap;
	
	public Light(int radius, int color){
		
		this.radius = radius;
		diameter = radius * 2; //this not needed for diamter as is not a paramater
		this.color = color;
		lightMap = new int[diameter * diameter];
		
		
		// Generate the light map
		// Will be generating a circle, the closer to the center the more close to the 'color' it will be
		for(int y = 0; y < diameter; y++){			// y first as this is less intensive on cache because of how arrays are set up...? 
			for(int x = 0; x < diameter; x++){
				double distance = Math.sqrt((x - radius)*(x - radius) + (y - radius)*(y - radius));
				
				if(distance < radius){
					// pixel within lit area
					double power = 1 - (distance/radius); //if distance < radius, negative power, low distance = higher power
					lightMap[x + y * diameter] = ((int)(((color >> 16) & 0xff) * power ) << 16 | (int)(((color >> 8) & 0xff) * power ) << 8 | (int)((color & 0xff) * power ));
				} else {
					// pixel is NOT withing lit area
					lightMap[x + y * diameter] = 0;
				}
			}
		}
		
	}
	
	public int getLightValue(int x, int y){
		
		if(x < 0 || x >= diameter || y < 0 || y >= diameter)
			return 0;
		
		return lightMap[x + y * diameter];
	}

	public int getRadius(){
		return radius;
	}

	public void setRadius(int radius){
		this.radius = radius;
	}

	public int getDiameter(){
		return diameter;
	}

	public void setDiameter(int diameter){
		this.diameter = diameter;
	}

	public int getColor(){
		return color;
	}

	public void setColor(int color){
		this.color = color;
	}

	public int[] getLightMap(){
		return lightMap;
	}

	public void setLightMap(int[] lightMap){
		this.lightMap = lightMap;
	}

}
