package com.difeng.mines;

import java.io.File;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * the textview of led
 * @author difeng
 *
 */
public class LEDTextView extends LinearLayout{
	private int digitLen;     // digital length
    private TextView ledView; // foreground　digital
    private TextView ledBgView; //backgroud digital
    final String ZERO_STR = "88888888888888888888888888888888888888888888888888888888";
	final String FILL_STR = "                ";
    private static final String FONT_DIGITAL_7 = "fonts" + File.separator
			+ "digital-7.ttf"; //font
	public LEDTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
    
	public void init(Context context,int digitLen){
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.ledview, this);
		ledView = (TextView) view.findViewById(R.id.ledview);
		ledBgView = (TextView) view.findViewById(R.id.ledview_bg);
		AssetManager assets = context.getAssets();
		final Typeface font = Typeface.createFromAsset(assets, FONT_DIGITAL_7);
		//设置字体
		ledView.setTypeface(font);
		ledBgView.setTypeface(font);
		ledBgView.setText(ZERO_STR.substring(0,digitLen));
	}
	public LEDTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,  
                R.styleable.LEDTextView);
        digitLen = a.getInt(R.styleable.LEDTextView_digitlen,2);
        a.recycle(); 
        init(context,digitLen);
	}

	public int getDigitLen() {
		return digitLen;
	}

	public void setDigitLen(int digitLen) {
		this.digitLen = digitLen;
	}
    /**
     * 
     * @param led 　digital　in　led
     */
	public void setContent(String led) {
		if(led.length() < digitLen){
			led = FILL_STR.substring(0,digitLen - led.length()) + led;
		}
		ledView.setText(led);
	}
	
}
