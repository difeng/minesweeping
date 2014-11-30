package com.difeng.mines;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private GameView gameView;  //game view
	private LEDTextView ledTime;//count　time
	private ImageView startBtn; //start button
	private LEDTextView ledMines; //mines　number
	private TimerTask task;  //timecount task
	private Timer timer;     //timer
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);   
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,   
				WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		//强制竖屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		//screen size
		DisplayMetrics dm=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);  
		if(dm.widthPixels<dm.heightPixels)
		{
			Config.SCREEN_WIDTH=dm.widthPixels;
			Config.SCREEN_HEIGHT=dm.heightPixels;
		}
		else
		{
			Config.SCREEN_WIDTH=dm.heightPixels;
			Config.SCREEN_HEIGHT=dm.widthPixels;    
		}
		setContentView(R.layout.activity_main);
		gameView = (GameView)this.findViewById(R.id.gameview);
		ledTime = (LEDTextView)this.findViewById(R.id.ledleft);
		ledMines = (LEDTextView)this.findViewById(R.id.ledright); 
		startBtn = (ImageView)this.findViewById(R.id.startbtn);
		gameView.setStartBtn(startBtn);
		gameView.setLedTime(ledTime);
		ledMines.setContent("10");
		final Handler handler = new Handler(){
			public void handleMessage(Message msg){
				if(msg.what == 1 && gameView.minesSweep.isRun){
					gameView.times ++;
					ledTime.setContent(String.valueOf(gameView.times));
				}
				super.handleMessage(msg);
			}
		};
		task = new TimerTask(){
			@Override
			public void run() {
				Message message = new Message();
				message.what = 1;
				handler.sendMessage(message);
			}
		};
		timer = new Timer(true);
		timer.schedule(task,1000,1000);
		startBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				gameView.initGame();
			}
		});
	}

}
