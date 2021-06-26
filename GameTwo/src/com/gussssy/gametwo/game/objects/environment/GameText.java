package com.gussssy.gametwo.game.objects.environment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.objects.GameObject;

public class GameText extends GameObject {
	
	private String text0;
	private String text;
	private String text2;
	private String text3;
	private String text4;
	private String text5;
	
	
	public GameText(float posX, float posY){
		
		this.posX = posX;
		this.posY = posY;
		
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy--HH-mm-ss");
		Date date = new Date();
		String dateLabel = df.format(date);
		
		
		Date startDate = new Date();
		Date endDate   = new GregorianCalendar(2021, Calendar.MAY, 24, 9,0,0).getTime();

		long duration  = endDate.getTime() - startDate.getTime();

		long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
		long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
		long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
		long diffInDays = TimeUnit.MILLISECONDS.toDays(duration);
		
		
		text = Long.toString(diffInSeconds);
		text0 = df.format(endDate);
		
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		
		Date startDate = new Date();
		Date endDate   = new GregorianCalendar(2021, Calendar.MAY, 24, 9,0,0).getTime();

		long duration  = endDate.getTime() - startDate.getTime();

		long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
		long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
		long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
		long diffInDays = TimeUnit.MILLISECONDS.toDays(duration);
		
		
		text = "S : " + Long.toString(diffInSeconds);
		text2 ="M : " + Long.toString(diffInMinutes);
		text3 ="H : " + Long.toString(diffInHours);
		text4 ="D : " + Long.toString(diffInDays);
		
		text5 = Long.toString(diffInDays) + " : " +  Long.toString(diffInHours%24) + " : " + Long.toString(diffInMinutes%60) + " : " + Long.toString(diffInSeconds%60);
		
	}

	@Override
	public void render(Renderer r) {
		
		r.drawText(text0, (int)posX, (int)posY-20, 0xff000000);
		r.drawText(text, (int)posX, (int)posY, 0xff000000);
		r.drawText(text2, (int)posX, (int)posY + 12, 0xff000000);
		r.drawText(text3, (int)posX, (int)posY + 24, 0xff000000);
		r.drawText(text4, (int)posX, (int)posY + 36, 0xff000000);
		r.drawText(text5, (int)posX, (int)posY + 60, 0xff000000);
		
	}

	@Override
	public void collision(GameObject other, AABBComponent otherHitBox) {
		// TODO Auto-generated method stub
		
	}

}
