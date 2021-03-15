package com.gussssy.gametwo.game.objects.npc;

import java.util.ArrayList;
import java.util.Optional;

import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.SoundManager;
import com.gussssy.gametwo.game.debug.DebugPanel;
import com.gussssy.gametwo.game.level.LevelTile;
import com.gussssy.gametwo.game.objects.Charachter;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.objects.ObjectType;

public class NPCVision {

	// The npc 
	NPC npc;

	// how many tiles away the npc can see, a square region, circle would be better.. later
	int detectionRange;	// NOTE: this is in TILES not PIXELS, may want to change this

	// The GameObjects the npc has vision of
	ArrayList<GameObject> inVision = new ArrayList<GameObject>();

	// Enemies the npc can see
	private ArrayList<GameObject> enemiesInVision = new ArrayList<GameObject>();

	// The GameObjects the npc is in range for detection but may not be in vision
	ArrayList<GameObject> inRange = new ArrayList<GameObject>();



	// Intersection points of shadowRays with tile boundaries
	ArrayList<IntersectionPoint> intersections = new ArrayList<IntersectionPoint>();




	/**
	 * NPCVision constructor. Creates a new NPCVision object that will detect other units in vision of the given NPC. 
	 *  
	 *  @param npc The NPC this class will give vision too.
	 **/
	public NPCVision(NPC npc){

		this.npc = npc;
		detectionRange = npc.detectionRange;
	}


	/**
	 * Updates what objects/charachters the NPC can see.
	 * - finds all npcs within the npcs detection range
	 * - then tests if these npcs are in vision e.g there is not solid terrain blocking vision
	 **/
	public void updateVision(GameManager gm){

		// clear lists from last update
		inVision.clear();
		inRange.clear();
		enemiesInVision.clear();

		// reset level tiles to not checked.
		//  tiles that have been searched after inbtersecting with a shadow ray wil still be marked as checked 
		for(LevelTile tile : gm.getLevelTiles()){ 
			tile.checked = false; 
			tile.colliding = false; // umm why ? 
		}


		// loop through game objects
		for(GameObject o : gm.getObjects()){

			// so the npc wont detect itself
			if(o.id == npc.id)continue;

			// have an object, are we interested in this object?
			// we are interested in NPCs, player or items.
			if(o.getObjectType() == ObjectType.NPC || o.getObjectType() == ObjectType.PLAYER || o.getObjectType() == ObjectType.ITEM ) {

				// we have an object we are interested in.

				// is this object within the detection range of this npc?
				if(inRangeByTiles(o.getTileX(), o.getTileY(), detectionRange, detectionRange)){
					// this object is in range
					inRange.add(o);
				}	
			}	
		}

		DebugPanel.message5 = "inRange objects: " + inRange.size();

		// cast shadow rays to each detected object to see if these objects are in vision
		castShadowRays(gm);
	}




	/**
	 * Tests to see if an object is within the detection range of the npc 
	 **/
	public boolean inRangeByTiles(int targetTileX, int targetTileY, int xLimit, int yLimit){

		int xDiff = Math.abs(npc.getTileX() - targetTileX);
		int yDiff = Math.abs(npc.getTileY() - targetTileY);

		// think it should be <= but can't think right now
		if(xDiff < xLimit && yDiff < yLimit){
			return true;
		}else{
			return false;
		}

	}




	/**
	 *  Cast a ray from the npc to detected npcs.
	 *  
	 *   If this ray is obscured by solid level tiles then the object is not in vision. 
	 *   	--> The object is not added to the inVision list.
	 *   
	 *   If this ray is not obstructed by a solid level tile, the object is in vision.
	 *   	--> The object is added to the inVision list
	 **/
	public void castShadowRays(GameManager gm){

		// algorithm adapted from: 
		// http://www.permadi.com/tutorial/raycast/rayc7.html

		intersections.clear();


		for(GameObject o : inRange){

			// determine impt variables
			int tileSize = GameManager.TS;
			// shift the rays so they start roughly at the eye of the NPC and terminate roughly central to the detected object
			float py = npc.getPosY() + npc.getTopPadding() + 3; // start line from the npcs 'eyes'
			float px = npc.getPosX() + tileSize/2;				// start from x center of tile
			float stopX = o.getPosX() + tileSize/2;
			float stopY = o.getPosY() + 3;
			float dx = (stopX - px);
			float dy = (stopY - py);
			double slope = dy/dx;
			double distanceToObject = Math.sqrt(Math.pow(stopX-px, 2) + Math.pow(stopY-py, 2));
			boolean hidden = false;
			
			// Print to DebugPanel
			DebugPanel.message1 = "Ditance to o: " + distanceToObject;
			//GameManager.debugMessage2 = "Line dy: " + dy;
			//GameManager.debugMessage3 = "Line slope: " + slope;





			// FINDING THE FIRST HORIZONTAL BOUNDARY

			// find the first horizontal tile intersection/tile boundary
			float hy, hx;

			// 1. Get the y coord of the first horizontal tile boundary this line intersects

			// how to do this depends on -/+ slope of line between this npc and the object

			// if the line slopes up (dy is negative, use the first formulae)
			// (casting to int so calculation rounds down)
			if(dy < 0){
				// dy is negative
				hy = (((int)py/tileSize) * tileSize -1); // -1 so intersection occurs at the boundry before entering the next tile
			}else{
				// dy is positive
				hy = (((int)py/tileSize) * tileSize + tileSize);
			}



			// 2. Getting the x coord of the first horixontal tile boundary

			double alpha = Math.atan(slope);
			//GameManager.debugMessage5 = "alpha deg: " + Math.toDegrees(alpha);
			//GameManager.debugMessage6 = "alpha rad: " + alpha;
			hx = (float)(px + ((hy - py)/Math.tan(alpha))); // cast to int will round down



			//GameManager.debugMessage7 = "ix: " + hx + ", tileX: " + (hx/tileSize);

			// get distance from npc of first intersection: 
			double distanceFromNPC = Math.sqrt(Math.pow(hx-px,2) + Math.pow(hy-py, 2));
			DebugPanel.message1 = "dist to h1 : " + distanceFromNPC;



			// FIRST HORIZONTAL INTERSECTION: 
			// add first horizontal intersection to intersections list
			// 	will be a darker green dot
			if(distanceFromNPC < distanceToObject){

				LevelTile temp = gm.getLevelTile((int)hx/tileSize, (int)hy/tileSize);
				temp.checked = true;

				if(temp.isCollision()){
					// this tile blocks vision
					intersections.add(new IntersectionPoint((int)hx, (int)hy, 0xffff0000, distanceFromNPC));
					hidden = true;

				}else {
					// this tile doies not block vision
					intersections.add(new IntersectionPoint((int)hx, (int)hy, 0xff00CC00, distanceFromNPC));
				}

			}else {
				DebugPanel.message2 = "h1 too far";
			}





			// FINDING THE X & Y SPACING BETWEEN HORIZONTAL INTERSECTIONS: 


			//a.  Y distance between horizontal intersections:  yIntervalHorizontal
			double yIntervalHorizontal;

			// This will be the tile size, +/- depending on the slope of the line
			// line 'faces up' / slopes up, dy is negative. 
			// therefore for each horizontal intersection, y decreases by 1 tile:  -16
			if(dy < 0){
				yIntervalHorizontal = -tileSize;
			}else{
				yIntervalHorizontal = tileSize;
			}

			//GameManager.debugMessage8 = "y spacing: " + yIntervalHorizontal;

			// b. X distance between horizontal intersections: xIntervalHorizontal
			double xIntervalHorizontal = tileSize/Math.tan(alpha);

			if(dy < 0)xIntervalHorizontal = -xIntervalHorizontal;



			// Find the actual distance between horizontal intersections
			double totalDistanceInterval = Math.sqrt(Math.pow(xIntervalHorizontal,2) + Math.pow(yIntervalHorizontal, 2));



			// FINDING ALL HORIZONTAL INTERSECTIONS

			// Precalcalcutae variables for the next intersection before the while loop. 
			double nextX = hx + xIntervalHorizontal;									// the x coord for the next horizontal intersection
			double nextY = hy + yIntervalHorizontal;									// the y coord for the next horizontal intersection
			int tileX = (int)nextX/tileSize;											// the tileX value of the next horizontal intersection
			int tileY = (int)nextY/tileSize;											// the tileY value of the next horizontal intersection
			distanceFromNPC = Math.sqrt(Math.pow(nextX-px,2) + Math.pow(nextY-py, 2));	// the distance from the npc to this intersection

			// LOOP TO FIND ALL HORIZONTAL INTERSECTIONS BETWEEN THE NPC AND THE DETECTED OBJECT
			// Each loop will test the previously calculated intersection then add it to intersections
			//		- then, the variables for the next intersection will be calculated
			//			- however if the ditance on the next intersection exceeds distance to the object the loop will terminate
			//				- this loop will terminate after creating and testing the last horizontal intersection between the npc and the detected object
			while(distanceFromNPC < distanceToObject){

				// the previously calculated intersection is within the region between the npc and the detected object
				// (I think because of this we can be sure that the associated tile is not null...  hmmm)
				LevelTile temp = gm.getLevelTile(tileX, tileY);
				if(temp == null){
					// I didnt think this was possible but worth testing for
					System.out.println(" ! ! ! ERROR Level Tile following Shadow ray intersection was null, investigate this further ! ! !");
					System.out.println("\tTile Location that was null: x = " + tileX + ", y = " + tileY);
					SoundManager.error1.play();
					SoundManager.error2.play();
				}else{

					// this is a real tile 
					temp.checked = true;

					if(temp.isCollision()){
						// Yes this tile blocks vision
						intersections.add(new IntersectionPoint((int)nextX, (int)nextY, 0xffff0000, distanceFromNPC));
						hidden = true;
						break;
					}else {
						// No this tile does not block vision
						intersections.add(new IntersectionPoint((int)nextX, (int)nextY, 0xffff8000, distanceFromNPC));
					}
				}

				// now compute values for the next intersection
				nextX += xIntervalHorizontal;
				nextY += yIntervalHorizontal;
				distanceFromNPC += totalDistanceInterval;
				tileX = (int)nextX/tileSize;
				tileY = (int)nextY/tileSize;


			}

			//GameManager.debugMessage9 = "x spacing: " + xIntervalHorizontal;




			
			
			// FINDING VERTICAL INTERSECTIONS

			//1. Finding the first vertical intersection: vx, vy.
			float vx, vy;

			// a. Finding the x coord of the first vertical intersection, vx

			if(dx < 0){
				// ray extends left
				vx = ((int)(px/tileSize) * tileSize -1); 
			}else {
				// ray extends right
				vx = ((int)(px/tileSize) * tileSize + tileSize);
			}


			// b. Finding the y coord of the first vertical intersection, vy

			vy = (float)(py + (vx - px)*Math.tan(alpha));




			// get distance from npc to the first vertical intersection: 
			distanceFromNPC = Math.sqrt(Math.pow(vx-px,2) + Math.pow(vy-py, 2));
			DebugPanel.message3 = "dist to v1 : " + distanceFromNPC;



			// FIRST VERTTICAL INTERSECTION: 
			// add first vertical intersection to intersections list
			// 	will be teal or red if collision
			if(distanceFromNPC < distanceToObject){

				LevelTile temp = gm.getLevelTile((int)vx/tileSize, (int)vy/tileSize);
				temp.checked = true;

				if(temp.isCollision()){
					// this tile blocks vision
					intersections.add(new IntersectionPoint((int)hx, (int)hy, 0xffff0000, distanceFromNPC));
					hidden = true;
					break;

				}else {
					// this tile doies not block vision
					intersections.add(new IntersectionPoint((int)hx, (int)hy, 0xff00ffff, distanceFromNPC));
				}

			}else {
				DebugPanel.message4 = "v1 too far";
			}



			// 2. Find x and y intervals for vertical intersections

			// 	a) x interval 
			double xIntervalVertical;

			if(dx < 0){
				xIntervalVertical = -tileSize;
			} else {
				xIntervalVertical = tileSize;
			}


			//	b) y interval
			double yIntervalVertical = tileSize * Math.tan(alpha);


			if(dx < 0)yIntervalVertical = -yIntervalVertical;


			// Find the actual distance between vertical intersections
			double verticalDistanceInterval = Math.sqrt(Math.pow(xIntervalVertical,2) + Math.pow(yIntervalVertical, 2));



			// FINDING ALL VERTICAL INTERSECTIONS

			// Precalcalcutae variables for the next intersection before the while loop. 
			nextX = vx + xIntervalVertical;												// the x coord for the next vertical intersection
			nextY = vy + yIntervalVertical;												// the y coord for the next vertical intersection
			tileX = (int)nextX/tileSize;												// the tileX value of the next vertical intersection
			tileY = (int)nextY/tileSize;												// the tileY value of the next vertical intersection
			distanceFromNPC = Math.sqrt(Math.pow(nextX-px,2) + Math.pow(nextY-py, 2));	// the distance from the npc to this intersection

			// LOOP TO FIND ALL VERTICAL INTERSECTIONS BETWEEN THE NPC AND THE DETECTED OBJECT
			// Each loop will test the previously calculated intersection then add it to intersections
			//		- then, the variables for the next intersection will be calculated
			//			- however if the ditance on the next intersection exceeds distance to the object the loop will terminate
			//				- this loop will terminate after creating and testing the last vertical intersection between the npc and the detected object
			while(distanceFromNPC < distanceToObject){

				// the previously calculated intersection is within the region between the npc and the detected object
				// (I think because of this we can be sure that the associated tile is not null...  hmmm)
				LevelTile temp = gm.getLevelTile(tileX, tileY);
				if(temp == null){
					// I didnt think this was possible but worth testing for
					System.out.println(" ! ! ! ERROR Level Tile following Shadow ray intersection was null, investigate this further ! ! !");
					System.out.println("\tTile Location that was null: x = " + tileX + ", y = " + tileY);
					SoundManager.error1.play();
					SoundManager.error2.play();
				}else{

					// this is a real tile 
					temp.checked = true;

					if(temp.isCollision()){
						// Yes this tile blocks vision
						intersections.add(new IntersectionPoint((int)nextX, (int)nextY, 0xffff0000, distanceFromNPC));
						hidden = true;
					}else {
						// No this tile does not block vision
						intersections.add(new IntersectionPoint((int)nextX, (int)nextY, 0xffffff00, distanceFromNPC));
					}
				}

				// now compute values for the next intersection
				nextX += xIntervalVertical;
				nextY += yIntervalVertical;
				distanceFromNPC += verticalDistanceInterval;
				tileX = (int)nextX/tileSize;
				tileY = (int)nextY/tileSize;


			}



			// add the object to the awareOf list if it is not hidden.
			if(!hidden){
				inVision.add(o);

				// Could this object be an enemy? First check to make sure the object is a charachter.  
				if(o.getObjectType() == ObjectType.NPC || o.getObjectType() == ObjectType.PLAYER){


					// Downcast o to Charachter then get the team and compare to the npcs team
					if(((Charachter)o).getTeam() != npc.getTeam()){

						// add enemie to enemiesInVision list 
						enemiesInVision.add(o);
					}


				}




			}

		}


	}



	/**
	 * Select an enemy from enemiesInVision 
	 * 
	 * If there are no enemies in vision returns an empty optional.
	 * 
	 * If there is one or more enemies in vision, returns the first enemy GameObject from the list.
	 * 
	 * In the future: would like a better system then this. Maybe try find the closest enemy. Or perhaps an army will share awareness...
	 * 
	 **/
	public Optional<GameObject> getTargetEnemy(){

		// if there are no enemies in vision, return null.
		if(enemiesInVision.isEmpty()){
			return Optional.empty();
		}

		// if there is an enemy in vision, return it.
		return Optional.of(enemiesInVision.get(0));

	}

	
	
	
	/**
	 * 
	 */
	public boolean isEnemyInVision(){
		
		if(enemiesInVision.isEmpty()){
			return false;
			
		}else {
			return true;
		}
		
	}
	
	
	

	/***
	 * When Debug is active, draws lines between the NPC and other units that are within the detection range. 
	 * <ul>
	 * <li> A blue line indicates other unit is in range but no vision, there is terrain blocking vision
	 * <li> A green line indicates the other unit is both in range and in vision.
	 * </ul>
	 */
	public void renderVision(Renderer r){

		if(DebugPanel.showVision){

			for(GameObject o : inRange){
				r.drawLine((int)(npc.getPosX() + GameManager.TS/2), (int)(npc.getPosY() + npc.getTopPadding() + 3 ), (int)(o.getPosX()+GameManager.TS/2), (int)(o.getPosY() + 3), 0xff0000ff);

				//System.out.println("\n\nLine start: x = " + (int)npc.getPosX() + ", y = " + (int)npc.getPosY());
				//System.out.println("Line End: x = " + (int)o.getPosX() + ", y = " +  (int)o.getPosY());
			}



			for(GameObject o : inVision){
				r.drawLine((int)(npc.getPosX() + GameManager.TS/2), (int)(npc.getPosY() + npc.getTopPadding() + 3 ), (int)(o.getPosX()+GameManager.TS/2), (int)(o.getPosY() + 3), 0xff00ff00);

				//System.out.println("\n\nLine start: x = " + (int)npc.getPosX() + ", y = " + (int)npc.getPosY());
				//System.out.println("Line End: x = " + (int)o.getPosX() + ", y = " +  (int)o.getPosY());
			}




			for(IntersectionPoint p : intersections){
				r.drawFillRect(p.x, p.y, 2, 2, p.color);
			}

		}



	}

}
