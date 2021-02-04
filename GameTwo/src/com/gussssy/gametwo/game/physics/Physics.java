package com.gussssy.gametwo.game.physics;

import java.util.ArrayList;

import com.gussssy.gametwo.game.components.AABBComponent;

public class Physics{

	// AABB for rect to rect collision
	private static ArrayList<AABBComponent> aabbList = new ArrayList<AABBComponent>();
	
	public static boolean printX = false;
	public static boolean printY = false;

	public static void addAABBComponent(AABBComponent aabb){
		aabbList.add(aabb);
	}
	


	public static void update(){
		
		// Print parent tags of AABBComponets (hitboxes) in the list.
		/*System.out.println("\nAABBList contents: ");
		for(AABBComponent aabb : aabbList){
			System.out.println(aabb.getParent().getTag());
		}*/

		//////////////////////////////////////////////////////////////////////////////////////
		//									AABBComponent 									//

		for(int i = 0; i < aabbList.size(); i++){
			
			for(int j = i + 1; j < aabbList.size(); j++){
				
				// more efficient to declare c0 and c1 outside of the loop?
				AABBComponent c0 = aabbList.get(i);
				AABBComponent c1 = aabbList.get(j);
				
				if(Math.abs(c0.getCenterX() - c1.getCenterX()) < c0.getHalfWidth() + c1.getHalfWidth()){
					
					if(Math.abs(c0.getCenterY() - c1.getCenterY()) < c0.getHalfHeight() + c1.getHalfHeight()){
						//COLLISION HAS OCCURED
						
						// used to debug grenade tile collision
						/*if(c0.getParent().getTag() == "grenade" && c1.getParent().getTag() == "tile"){
							System.out.println("Physics:  grenade and tile collison");
						}*/
						
						// process the collison
						c0.getParent().collision(c1.getParent(), c1);
						c1.getParent().collision(c0.getParent(), c0);
						
						// set the two hitboxes to collisind so when rendered they are red
						c0.colliding = true;
						c1.colliding = true;
						
					}
				}
			}
		}
		aabbList.clear();

		
	}



}
