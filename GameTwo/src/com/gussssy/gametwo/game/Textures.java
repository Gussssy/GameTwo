package com.gussssy.gametwo.game;

import com.gussssy.gametwo.engine.gfx.Image;
import com.gussssy.gametwo.engine.gfx.ImageTile;

public class Textures {
	
	// Level Tiles
	public static Image dirt = new Image("/dirt.png");
	//public static Image grass = new Image("/grassblock1_16x16.png");
	public static Image grass = new Image("/grass4.png");
	public static Image stone = new Image("/stone1.png");
	public static Image brick = new Image("/brick1.png");
	public static Image log = new Image("/log_texture.png");
	//public static Image leaf = new Image("/grassblock1_16x16.png");
	public static Image leaf = new Image("/grass5.png");
	public static Image fence = new Image("/fence.png");
	public static Image dirtBackReinforced = new Image("/dirt_wood.png");
	public static Image water = new Image("/water1.png");
	public static Image snow = new Image("/snow1.png");
	public static Image brickBack = new Image("/brickBack.png");
	public static Image marbleBrick = new Image("/marbleBrick.png");
	public static Image marblePillar = new Image("/pillar.png");
	public static Image ice = new Image("/blocks/ice.png");
	
	
	
	public static Image dirtBack = new Image("/dirt_back.png");
	
	// Player
	public static ImageTile playerTile = new ImageTile("/gusterix_tile.png", 16,16);
	
	
	// NPCs
	public static Image badBotBot = new Image("/BadBotBot.png");
	public static Image botbotImage = new Image("/BotBot1.png");
	public static Image smartBotBot = new Image("/SmartBotBot.png");
	public static ImageTile botbotTile = new ImageTile("/character/botbot_tile.png",16,16);
	// Goose ? 
	
	// Debugging images
	public static Image marker = new Image("/marker.png");
	public static Image node = new Image("/node.png");
	public static Image flag = new Image("/flag.png");
	public static Image tileFrame = new Image("/tile_frame_full.png");
	public static Image tileFrameBlack = new Image("/tile_frame_black.png");
	public static Image tileFrameRed = new Image("/tile_frame_red.png");
	
	
	
	
	public static void init(){
		water.setAlpha(true);
	}
	

}
