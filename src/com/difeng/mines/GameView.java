package com.difeng.mines;

import java.io.File;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class GameView extends View{
	public int times;
	Paint paint;
	private ImageView startBtn;
	private LEDTextView ledTime;
	public MineSweep minesSweep;
    private static final String FONT_DIGITAL_7 = "fonts" + File.separator
			+ "digital-7.ttf";
    public void setLedTime(LEDTextView ledTime) {
		this.ledTime = ledTime;
	}
	public void setStartBtn(ImageView startBtn) {
		this.startBtn = startBtn;
	}
    public void win(){
    	startBtn.setImageResource(R.drawable.ku);
    }
    public void fail(){
    	startBtn.setImageResource(R.drawable.cry);
    }
	@Override
	public boolean onTouchEvent(MotionEvent event) {
    	int x ,y;
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			x = (int) event.getX();
			y = (int) event.getY();
			Point point  = getTouchPoint(x,y);
			if(point != null){
				if(minesSweep.preOk){
					minesSweep.isRun = true;
					minesSweep.preOk = false;
				}
				if(minesSweep.isRun){
					minesSweep.sweep(point);
					System.out.println(point.row + "," + point.col);
				}
			}else{
				System.out.println("crash");
			}
		}
		return super.onTouchEvent(event);
	}
    public Point getTouchPoint(int x,int y){
    	int row = 0;
    	int col = 0;
    	x -= minesSweep.zoneX;
    	y -= minesSweep.zoneY;
    	if(x > minesSweep.zoneDimesion || y > minesSweep.zoneDimesion || x < 10 || y < 10){
    		return null;
    	}
    	if(x % Config.CELL_DIMENSION >0){
    		col = x / Config.CELL_DIMENSION;
    	}
    	if(y % Config.CELL_DIMENSION >0){
    		row = y / Config.CELL_DIMENSION;
    	}
    	return new Point(row,col);
    }

    public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		Config.CELL_DIMENSION = (Config.SCREEN_WIDTH - 18) / 9;
		minesSweep = new MineSweep(this);
		AssetManager assets = context.getAssets();
		final Typeface font = Typeface.createFromAsset(assets, FONT_DIGITAL_7);
	    paint.setTypeface(font);
	    paint.setTextSize(Config.CELL_DIMENSION);
    }

	
	
    
	protected void onDraw(Canvas canvas)
	{		
		super.onDraw(canvas);
		if(canvas == null)
		{
			return;
		}
		canvas.drawARGB(255,140, 140, 140);
     	minesSweep.drawSelf(canvas, paint);
	}
    /**
     * init　game
     * 
     */
	public void initGame() {
	   minesSweep.initGame();
	   ledTime.setContent("0");
	   times = 0;
	   startBtn.setImageResource(R.drawable.smile);
	   invalidate();
	}
}
