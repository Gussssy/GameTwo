package com.gussssy.gametwo.game.pathfinding;

import java.util.ArrayList;

import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.objects.npc.NPC;

public class Path {

	private boolean print = false;
	
	// NEW PATH FINDING VARIABLES
	// the path found by Aa* pathfinding
	ArrayList<PathNode> pathSteps = new ArrayList<PathNode>();
	private int pathProgress = 0;
	PathNode currentPathStep;
	GameManager gm;
	boolean readyToExecuteStep = false;


	

	ArrayList<String> instructions = new ArrayList<String>();
	ArrayList<Movement> moves = new ArrayList<Movement>();
	NPC npc;
	int tileX, tileY, nextTileX, nextTileY;
	int nextX, nextY;

	// when this reaches 0 path is done
	//int progress;
	int moveProg;

	// (26/8) this shouldn't really be set to 'right' but there is a case where this needs to not be null 
	// needs to be fixed but more pressing issues
	String direction = "right";

	Movement currentMovement;

	// Variables used to detect when the NPC is stuck
	private int updatesStationary = 0;
	float offX, offY;

	// path execution variables
	private boolean acrossOne = false; 
	private boolean upOneAcrossOne = false; 
	private boolean upMultipleAcrossOne = false;
	private boolean acrossOneDown = false;
	int jumpHeight = 0;


	String currentStep;
	String nextInstruction;	



	public Path(ArrayList<PathNode> pathSteps, NPC npc, GameManager gm){
		
		if(print)System.out.println("\n\n\n |||| PathConstructor: Initiating A New Path |||");

		this.pathSteps = pathSteps;
		this.npc = npc;
		this.gm = gm;

		//System.out.println(" Initializing Path found by A* Pathfinding");

		if(print){
			for(PathNode pm : pathSteps){
				System.out.println(pm.toString());
			}
		}
		

		// setup
		tileX = npc.getTileX();
		tileY = npc.getTileY();
		pathProgress = 0;
		readyToExecuteStep = false;

		setNextStep(pathProgress);
		
		
		if(print)System.out.println(" ||| Path Initiated |||\n\n\n");

	}

	/**
	 * Set the next step in the path for the npc to execute. 
	 *  !! USEING THE NEW A* Pathing !! 
	 **/
	private void setNextStep(int pathProgress){

		currentPathStep = pathSteps.get(pathProgress);

		if(print){
			System.out.println("Path.setNextStep: Setting Next Path Step / target tile");
			System.out.println("\tCurrent pathProgress: " + pathProgress);
			System.out.println("\tThe next movement: " + currentPathStep.movement);
			System.out.println("\tCurrent NPC tile location: tileX = " + npc.getTileX() + ", tileY = " + npc.getTileY());
		}


		switch(currentPathStep.movement){
		case LEFT:
			nextX = tileX -1;
			nextY = tileY;
			break;
		case UP_LEFT:
			nextX = tileX -1;
			nextY = tileY -1;
			break;
		case UP_UP_LEFT:
			nextX = tileX -1;
			nextY = tileY -2;
			break;
		case UP_UP_UP_LEFT:
			nextX = tileX -1;
			nextY = tileY -3;
			break;
		case RIGHT:
			nextX = tileX +1;
			nextY = tileY;
			break;
		case UP_RIGHT:
			nextX = tileX +1;
			nextY = tileY -1;
			break;
		case UP_UP_RIGHT:
			nextX = tileX +1;
			nextY = tileY -2;
			break;
		case UP_UP_UP_RIGHT:
			nextX = tileX +1;
			nextY = tileY -3;
			break;
		case UP:
			nextX = tileX;
			nextY = tileY -1;
			break;
		case DOWN:
			nextX = tileX;
			nextY = tileY +1;
			break;
		case LEFT_DOWN: 
			nextX = tileX -1;
			nextY = tileY + currentPathStep.tilesDown;
			break;
		case RIGHT_DOWN:
			nextX = tileX +1;
			nextY = tileY + currentPathStep.tilesDown;
			break;
		default:
			break;
		}

		//gm.setFlagLocation(nextX*16, nextY*16);

		if(print){
			//System.out.println("Current Movement: " + currentMovement);
			System.out.println("NPC will move to: tileX = " + nextX + ", tileY = " + nextY);
		}

	}







	public String executePath(){

		// Get the tile location of the NPC for this update
		tileX = npc.getTileX();
		tileY = npc.getTileY();


		////////////////////////////////// ANTI STUCK STUFF /////////////////////////////////////
		// Check if the npc has moved this frame
		if(npc.getOffX() == offX && npc.getOffY() == offY){
			//System.out.println("NPC is stationary");
			updatesStationary++;

			// if the NPC has been stationary for 120 updates, recalculate the path
			if(updatesStationary == 120){
				if(print)System.out.println("NPC has been sationary for 120 updates, need to try something... will try recalculating");
				return "recalculate";
			}
		}

		// set new offSet values so they can be checked for changes next update
		offX = npc.getOffX();
		offY = npc.getOffY();



		// Deal with overshoot on left and right movements
		// It is very likely not just LEFT and RIGHT can lead to overshoot.
		/*if((currentPathStep.movement == Movement.RIGHT && tileX > nextX) || (currentPathStep.movement == Movement.LEFT && tileX < nextX) ){
			if(print)System.out.println("!!!!!! NPC is off course, need to recalculate !!!!!!");
			return "recalculate";
		}*/
		//////////////////////////////////////////////////////////////////////////////////////////////



		/////////////////				COMPLETION CHECKING					////////////////////////////
		// Has the NPC reached the next target tile ? 
		if(tileX == nextX && tileY == nextY){

			if(print)System.out.println("\n\nReached next target tile, movement was: " + currentPathStep.movement);

			// The NPC has reached the target, increment progress
			//System.out.println("PATH PROGRESS ABOUT TO BE INC current value: " + pathProgress);
			pathProgress++;

			// As the NPC has finished a step, a new one has to be set.
			readyToExecuteStep = false;
			
			//System.out.println("pathProgress: " + pathProgress + "pathSteps size: " + pathSteps.size());
			//System.out.println("Location: x: " + tileX + ", y: " + tileY + ", next location: x: " + nextX + ", nextY: " + nextY);

			// Has npc finished this path ? 
			if(pathSteps.size() <= pathProgress){

				// npc has finished this path
				if(print)System.out.println("!!!!!! NPC has Finished path !!!!!!");
				//SoundManager.goose.play();
				return "stop";
				//NPC is finishing 1 step too soon...
			}

			// NPC has not finished the path yet. Load the next pathStep from the array list.
			//System.out.println(" Setting next step.......... crash?");
			setNextStep(pathProgress);
		}



		// PATH TO THE TARGET TILE

		// NULL CHECK - need to determine if this is neccesary now with the new system
		if(currentPathStep == null){
			if(print)System.out.println(" CurrentPathStep was null. 0 length path?");
			return "stop";
		}


		// if the current step has not been set for execution, then it needs to be set. 
		// otherwise dont execute this switch statement, it has already been done for this step.
		/*if(!readyToExecuteStep){*/

			// call reset to set booleans to false;
			reset();
			// NOTE  WE SHOULDNT BE DOING THIS EVERY UPDATE

			// we dont want to be doing this every update, just when a new step is loaded
			switch(currentPathStep.movement){
			case LEFT:
				direction = "left";
				acrossOne = true;
				readyToExecuteStep = true;
				break;

			case UP_LEFT:

				// set direction to left for any following down movements
				direction = "left";
				upOneAcrossOne = true;
				readyToExecuteStep = true;
				break;

				// NOTE, offX matters on bot sides, has to be more than -3 and less then 3. 3 is the value of bot bot left right paddigng

			case UP_UP_LEFT:
				direction = "left";
				jumpHeight = 2;
				upMultipleAcrossOne = true;
				readyToExecuteStep = true;
				break;

			case UP_UP_UP_LEFT:
				direction = "left";
				jumpHeight = 3;
				upMultipleAcrossOne = true;
				readyToExecuteStep = true;
				break;

			case RIGHT:
				direction = "right";
				acrossOne = true;
				readyToExecuteStep = true;
				break;

			case UP_RIGHT:
				direction = "right";
				upOneAcrossOne = true;
				readyToExecuteStep = true;


				// npc needs to jump, once has jumped, wait untill has traveled into next RH tile, then stop right movement
				// move to somewhere in the middle of the tile before jumping
				// note: thisis onlyt important when the neighbouring right tile is solid
				//  [][]
				//  [][]
				break;

			case UP_UP_RIGHT:
				direction = "right";
				jumpHeight = 2;
				upMultipleAcrossOne = true;
				readyToExecuteStep = true;
				break;

			case UP_UP_UP_RIGHT:
				direction = "right";
				jumpHeight = 3;
				upMultipleAcrossOne = true;
				readyToExecuteStep = true;
				break;

			case LEFT_DOWN:
				direction = "left";
				acrossOneDown = true;
				readyToExecuteStep = true;
				break;

			case RIGHT_DOWN:
				direction = "right";
				acrossOneDown = true;
				readyToExecuteStep = true;
				break;

				// NOTE: atm this is not used but I plan to use it when I implement ladders
			case UP:
				return "up";

				// NOTE not using this atm and don;t inted to use in the future. Remove this once everything works. 19/10
			case DOWN:
				// continue last horizontal movement untill npc actually falls....
				if(npc.isOnGround()){
					return direction;
				}else{
					// once falling stop movement and wait to hit ground
					//System.out.println("NPC is now falling and should stop movement in direction");
					return "wait";
				}
			default:
				break;
			}
		/*}*/





		// SIMPLE LEFT OR RIGHT
		// Ececute basic movement left or right
		if(acrossOne){
			return direction;
		}

		// UP ONE ACROSS ONE 
		// Execute up and across one.
		if(upOneAcrossOne){

			// CORRECT OFFSET BEFORE JUMPING
			// moving enough into the tile so that jumping wont collide with a tile above leading to multipel useless jumps
			if(direction == "left"){
				// direction is left
				if(npc.getOffX() > npc.getLeftRightPadding()){
					// need to more left before able to safely jump
					return direction;
				}
			}else{
				// direction must be right
				if(npc.getOffX() < -npc.getLeftRightPadding()){
					// need to more left before able to safely jump
					return direction;
				}
			}

			// at this point we can be sure its time to jump


			// npc needs to jump, if it hasnt jumped already then jump. 
			// also dont jump if have already / dont jump if npc has reached the target y... leads to spam jumping on the edge of the target tile
			if(npc.isOnGround() && tileY != nextY){
				return "up";
			}else if(tileX != nextX){
				return direction;
			}else{
				return "wait";
			}


		}


		// UP MANY ACROSS ONE 
		if(upMultipleAcrossOne){
			//if(print)System.out.println("Path Update, UP Multiple, Across 1.  Case: " + currentPathStep.movement  + ", Npc current location:  tileX: " + tileX + ", tileY: " + tileY );

			// Correcting NPC position on the tile before jumping to avoid unwanted collisions when jumping
			// Is NPC at the base of the jump and not mid jump? 
			if(npc.isOnGround() && tileY - jumpHeight == nextY){

				//do we need left correction? 
				// offX > 3
				if(offX > 3){
					// offX exceeds 3, may need to move left to avoid collision on jump
					return "left";


					// do we need right correction? 
					//offX < -3
				}else if(offX < -3){
					// offX is less then -3, may need to move right to avoid collision
					return "right";

					// if all above conditions are false, it is now safe for the npc to jump
				}else{
					// tell the npc to jump
					return "up";
				}
			}else if(tileY == nextY){
				// npc has jumped and has reached the desired height, start moving in the direction
				return direction;
			}else{
				return "wait";
			}
		}


		if(acrossOneDown){
			if(npc.isOnGround()){
				return direction;
			}else {
				return "wait";
			}
		}

		if(print)System.out.println("SHOULD NOT BE HERE, SOMETHING IS WRONG IN PATH.UPDATE()");
		return "";

	}





	public Path(ArrayList<String> steps, NPC npc){

		this.npc = npc;
		this.offX = npc.getOffX();
		this.offY = npc.getOffY();

		// Print out initial state of path
		if(print){
			System.out.println("Making a new path");
			System.out.println("New path has " + steps.size() + " steps");
			System.out.println("Reading path with contents: ");
			for(String step : steps){
				System.out.println(step);
				instructions.add(step);
			}
		}

		// Construct moves array
		boolean lastWasUp = false;
		int ups = 0;
		for(String s : steps){

			if(s == "up"){
				lastWasUp = true;
				ups++;
				continue;
			}

			if(!lastWasUp){

				// if left right or down add movement
				if(s == "left"){
					moves.add(Movement.LEFT);
				}else if(s == "right"){
					moves.add(Movement.RIGHT);
				}else if(s == "down"){
					moves.add(Movement.DOWN);
				}else if(s == "up"){
					lastWasUp = true;
					System.out.print("Ups value before being modified: " + ups);
					ups++;
					System.out.println("Ups incremented, intial up");
					System.out.print("Ups value after being modified: " + ups);
				}


				// if up, set lastWasUp and start counting
			}else{
				// last was up 

				if(s == "up"){
					// keeping counting up
					if(print)System.out.println("Ups value before being modified: " + ups);
					ups++;
					if(print)System.out.println("Ups incremented, secondary up");
					if(print)System.out.println("Ups value after being modified: " + ups);

				}else if(s == "left"){
					// switch to get left movement
					if(print)System.out.println("Up terminated with left");
					if(print)System.out.println("Ups value: " + ups);
					switch(ups){
					case 1:
						moves.add(Movement.UP_LEFT);
						break;
					case 2:
						moves.add(Movement.UP_UP_LEFT);
						break;
					case 3:
						moves.add(Movement.UP_UP_UP_LEFT);
						break;
					}
					// reset variables
					lastWasUp = false;
					ups = 0;

				}else if(s == "right"){
					//switch to get right movement
					if(print)System.out.println("Up terminated with right");
					if(print)System.out.println("Ups value: " + ups);
					switch(ups){
					case 1:
						moves.add(Movement.UP_RIGHT);
						break;
					case 2:
						moves.add(Movement.UP_UP_RIGHT);
						break;
					case 3:
						moves.add(Movement.UP_UP_UP_RIGHT);
						break;
					}
					// reset variables
					lastWasUp = false; 
					ups = 0;
				}
			}			
		}// end of for each loop on steps 

		// deal with consecutives ups not terminated
		if(lastWasUp){
			switch(ups){
			case 1:
				moves.add(Movement.UP);
				break;
			case 2:
				moves.add(Movement.UP_UP);
				break;
			case 3: 
				moves.add(Movement.UP_UP_UP);
				break;
			}
		}

		// set current movement
		moveProg = 0;
		tileX = npc.getTileX();
		tileY = npc.getTileY();

		setNextMovement(moveProg);

		if(print){
			System.out.println("Printing moves array");
			System.out.println(moves.toString());
			System.out.println();
		}
	}





	/**
	 * Called continuously as the NPC executes the path.
	 * 
	 * Returns a String to the NPC instance who is executing the path, providing an instruction.... 
	 * 
	 **/
	public String update(){

		// Get the tile location of the NPC for this update
		tileX = npc.getTileX();
		tileY = npc.getTileY();

		// Check if the npc has moved this frame
		if(npc.getOffX() == offX && npc.getOffY() == offY){
			//System.out.println("NPC is stationary");
			updatesStationary++;

			// if the NPC has been stationary for 120 updates, recalculate the path
			if(updatesStationary == 120){
				if(print)System.out.println("NPC has been sationary for 120 updates, need to try something... will try recalculating");
				return "recalculate";
			}
		}

		// set new offSet values so they can be checked for changes next update
		offX = npc.getOffX();
		offY = npc.getOffY();



		// Deal with overshoot on left and right movements
		// It is very likely not just LEFT and RIGHT can lead to overshoot.
		if((currentMovement == Movement.RIGHT && tileX > nextX) || (currentMovement == Movement.LEFT && tileX < nextX) ){
			if(print)System.out.println("!!!!!! NPC is off course, need to recalculate !!!!!!");
			return "recalculate";
		}



		// Has the NPC reached the next target tile ? 
		if(tileX == nextX && tileY == nextY){

			if(print)System.out.println("\n\nReached next target tile, movement was: " + currentMovement);

			// The NPC has reached the target, increment progress
			moveProg++;

			// Has npc finished this path ? 
			if(moves.size() == moveProg){

				// npc has finished this path
				if(print)System.out.println("!!!!!! NPC has Finished path !!!!!!");
				return "stop";
				//NPC is finishing 1 step too soon...
			}

			// NPC has not finished the path yet
			setNextMovement(moveProg);
		}



		// PATH TO THE TARGET TILE

		if(currentMovement == null){

			if(print)System.out.println(" Current Movement was null. 0 length path?");
			return "stop";
		}


		// call reset to set booleans to false;
		reset();


		switch(currentMovement){
		case LEFT:
			direction = "left";
			acrossOne = true;

			break;


		case UP_LEFT:

			// set direction to left for any following down movements
			direction = "left";
			upOneAcrossOne = true;


			/*
			// npc needs to jump, once has jumped, wait untill has traveled into next RH tile, then stop right movement

			// if we have travelled enough right, stop going right and wait for npc to fall on target tile
			if(tileX == nextX){
				return "stopLeft";

				// else if NPC hasnt jumped yet, tell NPC to jump
			} else if (npc.isOnGround()){
				return "up";

				// else, NPC has jumped and needs to move left some more
			}else {
				return "left";
			}*/

			break;


			// NOTE, offX matters on bot sides, has to be more than -3 and less then 3. 3 is the value of bot bot left right paddigng


		case UP_UP_LEFT:
			direction = "left";
			jumpHeight = 2;
			upMultipleAcrossOne = true;

			/*
			// Need to jump, but first make sure that we wont collide
			System.out.println("Path Update, Case: UP_UP_LEFT\t\t" + "tileX: " + tileX + ", tileY: " + tileY );
			// if at the base of the jump, move right so that npc wont collide when jumping
			if(offX < -npc.getLeftRightPadding() && tileY -2 == nextY){
				if(npc.isOnGround())return "right";
				System.out.println("Bot bot is at the base of the jump and is correcting his postion. Returning right");
				// but we need to stop going right...

				// NPC can now jump without colliding
			}else if(npc.isOnGround()){
				System.out.println("BotBot is ready to juimp. returning up: ");
				System.out.println("NPC tileX: " + tileX);
				return "up";
			}else{
				// NPC is airbourne

				// if npc has moved up enough and hasnt overshot nextX, move left, else dont do anything
				if(tileY <= nextY && tileX > nextX) {
					System.out.println("Botbot is airbourne and ready to go left. returning left");
					return "left";
				}else {
					System.out.println("Botbot is airbourne but not yet ready to go left, returning wait.");
					return "wait";
				}
			}*/

			break;



		case UP_UP_UP_LEFT:
			direction = "left";
			jumpHeight = 3;
			upMultipleAcrossOne = true;

			/*
			//NOTE I COULD ARRANGE THIS SO ONCE TOP CONDITION IS MET, executes else clause, which proceedes to do the rest of the movemtnt
			// Need to jump, but first make sure that we wont collide
			System.out.println("Path Update, Case: UP_UP_UP_LEFT\t\t" + "tileX: " + tileX + ", tileY: " + tileY );
			// if at the base of the jump, move right so that npc wont collide when jumping
			if(offX < -3 && tileY -3 == nextY){
				if(npc.isOnGround())return "right";
				System.out.println("Bot bot is at the base of the jump and is correcting his postion. Returning right");
				// but we need to stop going right...

				// NPC can now jump without colliding
			}else if(npc.isOnGround()){
				System.out.println("BotBot is ready to juimp. returning up: ");
				System.out.println("NPC tileX: " + tileX);
				return "up";
			}else{
				// NPC is airbourne

				// if npc has moved up enough and hasnt overshot nextX, move left, else dont do anything
				if(tileY <= nextY && tileX > nextX) {
					System.out.println("Botbot is airbourne and ready to go left. returning left");
					return "left";
				}else {
					System.out.println("Botbot is airbourne but not yet ready to go left, returning wait.");
					return "wait";
				}
			}*/

			break;



		case RIGHT:
			direction = "right";
			acrossOne = true;
			break;

		case UP_RIGHT:
			direction = "right";
			upOneAcrossOne = true;


			/*
			// npc needs to jump, once has jumped, wait untill has traveled into next RH tile, then stop right movement
			// move to somewhere in the middle of the tile before jumping
			// note: thisis onlyt important when the neighbouring right tile is solid
			//  [][]
			//  [][]

			// if we have travelled enough right, stop going right and wait for npc to fall on target tile
			if(tileX == nextX){
				return "stopRight";

				// else if NPC hasnt jumped yet, tell NPC to jump
			} else if (npc.isOnGround()){
				return "up";

				// else, NPC has jumped and needs to move right some more
			}else {
				return "right";
			}*/

			break;

		case UP_UP_RIGHT:
			direction = "right";
			jumpHeight = 2;
			upMultipleAcrossOne = true;
			/*
			// Need to jump
			if(npc.isOnGround()){
				return "up";
			}else{
				// NPC is airbourne

				// if npc has moved up enough and hasnt overshot nextX, move right, else dont do anything
				if(tileY >= nextY && tileX < nextX) {
					return "right";
				}else {
					return "wait";
				}
			}*/
			break;

		case UP_UP_UP_RIGHT:
			direction = "right";
			jumpHeight = 3;
			upMultipleAcrossOne = true;

			/*
			// Need to jump
			if(npc.isOnGround()){
				return "up";
			}else{
				// NPC is airbourne

				// if npc has moved up enough and hasnt overshot nextX, move right, else dont do anything
				if(tileY >= nextY && tileX < nextX) {
					return "right";
				}else {
					return "wait";
				}
			}
			 */
			break;

		case UP:
			return "up";

		case DOWN:
			// continue last horizontal movement untill npc actually falls....
			if(npc.isOnGround()){
				return direction;
			}else{
				// once falling stop movement and wait to hit ground
				//System.out.println("NPC is now falling and should stop movement in direction");
				return "wait";
			}
		default:
			break;
		}

		//return"";


		// SIMPLE LEFT OR RIGHT
		// Ececute basic movement left or right
		if(acrossOne){
			return direction;
		}

		// UP ONE ACROSS ONE 
		// Execute up and across one.
		if(upOneAcrossOne){

			// CORRECT OFFSET BEFORE JUMPING
			// moving enough into the tile so that jumping wont collide with a tile above leading to multipel useless jumps
			if(direction == "left"){
				// direction is left
				if(npc.getOffX() > npc.getLeftRightPadding()){
					// need to more left before able to safely jump
					return direction;
				}
			}else{
				// direction must be right
				if(npc.getOffX() < -npc.getLeftRightPadding()){
					// need to more left before able to safely jump
					return direction;
				}
			}

			// at this point we can be sure its time to jump


			// npc needs to jump, if it hasnt jumped already then jump. 
			// also dont jump if have already / dont jump if npc has reached the target y... leads to spam jumping on the edge of the target tile
			if(npc.isOnGround() && tileY != nextY){
				return "up";
			}else if(tileX != nextX){
				return direction;
			}else{
				return "wait";
			}


		}


		// UP MANY ACROSS ONE 
		if(upMultipleAcrossOne){
			if(print)System.out.println("Path Update, UP Multiple, Across 1.  Case: " + currentMovement  + ", Npc current location:  tileX: " + tileX + ", tileY: " + tileY );

			// Correcting NPC position on the tile before jumping to avoid unwanted collisions when jumping
			// Is NPC at the base of the jump and not mid jump? 
			if(npc.isOnGround() && tileY - jumpHeight == nextY){

				//do we need left correction? 
				// offX > 3
				if(offX > 3){
					// offX exceeds 3, may need to move left to avoid collision on jump
					return "left";


					// do we need right correction? 
					//offX < -3
				}else if(offX < -3){
					// offX is less then -3, may need to move right to avoid collision
					return "right";

					// if all above conditions are false, it is now safe for the npc to jump
				}else{
					// tell the npc to jump
					return "up";
				}
			}else if(tileY == nextY){
				// npc has jumped and has reached the desired height, start moving in the direction
				return direction;
			}else{
				return "wait";
			}

			/**if(!npc.isOnGround()){
				//npc is mid jump, need to start moving in direction when at an appropriate height
				if(tileY == nextY){
					return direction;
				}else {
					return "wait";
				}
			}else{
				// npc is on the ground of the next tile?
			}*/

		}

		if(print)System.out.println("SHOULD NOT BE HERE, SOMETHING IS WRONG IN PATH.UPDATE()");
		return "";

	}


	/**
	 * Set the next movement. Old path finding algorithm. 
	 **/
	private void setNextMovement(int moveProg){


		// (26/8) temporary fix for when a 0 length path is created. 
		// not sure right now where the most logical place for this to be dealt with is. 
		if(moves.size() == 0){
			nextX = tileX;
			nextY = tileY;
			return;
		}

		currentMovement = moves.get(moveProg);
		if(print){
			System.out.println("Current moveProg: " + moveProg);
			System.out.println("Setting next target tile");
			System.out.println("Current NPC tile location: tileX = " + npc.getTileX() + ", tileY = " + npc.getTileY());
		}


		switch(currentMovement){
		case LEFT:
			nextX = tileX -1;
			nextY = tileY;
			break;
		case UP_LEFT:
			nextX = tileX -1;
			nextY = tileY -1;
			break;
		case UP_UP_LEFT:
			nextX = tileX -1;
			nextY = tileY -2;
			break;
		case UP_UP_UP_LEFT:
			nextX = tileX -1;
			nextY = tileY -3;
			break;
		case RIGHT:
			nextX = tileX +1;
			nextY = tileY;
			break;
		case UP_RIGHT:
			nextX = tileX +1;
			nextY = tileY -1;
			break;
		case UP_UP_RIGHT:
			nextX = tileX +1;
			nextY = tileY -2;
			break;
		case UP_UP_UP_RIGHT:
			nextX = tileX +1;
			nextY = tileY -3;
			break;
		case UP:
			nextX = tileX;
			nextY = tileY -1;
			break;
		case DOWN:
			nextX = tileX;
			nextY = tileY +1;
			break;
		case LEFT_DOWN: 
			break;
		case RIGHT_DOWN:
			break;
		default:
			break;
		}

		if(print){
			System.out.println("Current Movement: " + currentMovement);
			System.out.println("NPC will move to: tileX = " + nextX + ", tileY = " + nextY);
		}

	}


	/**
	 * Resets the boolean controlling what kind of movement is executed. 
	 * 	- called when a step/movement is completed and a new one is loaded.
	 **/
	private void reset(){

		acrossOne = false;
		upOneAcrossOne = false; 
		upMultipleAcrossOne = false;
		acrossOneDown = false;


	}

}
