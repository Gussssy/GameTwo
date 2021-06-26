package com.gussssy.gametwo.game;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.game.objects.GameObject;

public class Camera{
	
	private String targetTag;
	private GameObject target = null;
	
	private float offX, offY;
	
	
	// Camera Adjustments - for altering the the field of vision in particular circumstances
	private int adjustmentX = 0;
	private int adjustmentY = 0;
	
	public boolean freezeCamera = false;
	
	public Camera(String tag){
		this.targetTag = tag;
	}
	
	//private float targetCameraLocationX;
	//private float targetCameraLocationY;
	//private int cameraSpeedConstant = 15;
	
	public void update(GameContainer gc, GameManager gm, float dt){
		
		// if the targhet hasnbt been set, set it
		if(target == null){
			target = gm.getObject(targetTag);
		}
		
		// if the target was not found, return.
		if(target == null)return;
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 									DYNAMIC CAMERA MOVEMENT
		// Camera movement speed will depend on how far the target is from the center of the screen
		// Camera will not follow player exactly, instead camera will 'lag' behind the target.
		
		// I DO NOT LIKE THIS EFFECT, IT NEEDS TO BE FIXED OR DONT USE
		
		// Determine where the camera will move towards
		//targetCameraLocationX = (target.getPosX() - target.getWidth()/2) - gc.getWidth()/2;
		//targetCameraLocationY = (target.getPosY() - target.getHeight()/2) - gc.getHeight()/2;
		
		// Determine how much the camera should move this frame
		//offX -= dt * (offX - targetCameraLocationX) * cameraSpeedConstant;
		//offY -= dt * (offY - targetCameraLocationY) * cameraSpeedConstant;
		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		//											REGULAR CAMERA
		offX = (target.getPosX() - target.getWidth()/2) - gc.getWidth()/2; //center camera in middle of screen by subtracting half width
		offY = (target.getPosY() - target.getHeight()/2) - gc.getHeight()/2;
		
		
		
		
		//Dont show zone outside level
		if(offX < 0){
			offX = 0;
		}
		
		if(offY < 0){
			offY = 0;
		}
		
		if(offX > (gm.getLevelWidth() * GameManager.TS) - gc.getWidth()){
			offX = (gm.getLevelWidth() * GameManager.TS) - gc.getWidth();
			//System.out.println("Level Width: " + gm.getLevelWidth());
		}
		
		if(offY > (gm.getLevelHeight() * GameManager.TS) - gc.getHeight()){
			offY = (gm.getLevelHeight() * GameManager.TS) - gc.getHeight();
			//System.out.println("Level Width: " + gm.getLevelWidth());
		}
		
		
		// apply any camera position adjustments if any
		offX += adjustmentX;
		offY += adjustmentY;
		
		
		
		// Set CamX and CamY
		gc.getRenderer().setCamX((int)offX);
		gc.getRenderer().setCamY((int)offY);
		
	}
	
	/**
	 * Doesnt render anything, just sets camera offsets (camX and camY) for the next frame
	 * - neccesary so text can be rendered stationay on the screen 
	 **/
	public void render(Renderer r){
		r.setCamX((int)offX);
		r.setCamY((int)offY);
		System.out.println("CAMWERA RENDER");
		
	}


	public String getTargetTag(){
		return targetTag;
	}


	public void setTargetTag(String targetTag){
		this.targetTag = targetTag;
	}


	public GameObject getTarget(){
		return target;
	}


	public void setTarget(GameObject target){
		this.target = target;
	}


	public float getOffX(){
		return offX;
	}


	public void setOffX(float offX){
		this.offX = offX;
	}


	public float getOffY(){
		return offY;
	}


	public void setOffY(float offY){
		this.offY = offY;
	}

	/**
	 * @param adjustmentX the adjustmentX to set
	 */
	public void setAdjustmentX(int adjustmentX) {
		this.adjustmentX = adjustmentX;
	}

	/**
	 * @param adjustmentY the adjustmentY to set
	 */
	public void setAdjustmentY(int adjustmentY) {
		this.adjustmentY = adjustmentY;
	}

}
