package com.hopecontacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.*;

public class About extends Activity implements OnTouchListener,OnGestureListener{
	private Button button;
	
	public int FLING_MIN_DISTANCE=50;
	public int FLING_MIN_VELOCITY=0;
	public GestureDetector gd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		button=(Button) findViewById(R.id.button1);
		button.setOnClickListener(new backAction());
		
		gd=new GestureDetector(this,(OnGestureListener)this); 
	    RelativeLayout RL = (RelativeLayout) findViewById(R.id.aboutLayout);  
	    RL.setOnTouchListener(this);  
	    RL.setLongClickable(true); 
	}
	class backAction implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(About.this,Choice.class);
			startActivity(intent);
			overridePendingTransition( R.anim.in_from_left,R.anim.out_to_right);
		}
		
	}
	
	//左右滑动代码
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		try{
			if((e1.getX()-e2.getX())>FLING_MIN_DISTANCE&&Math.abs(velocityX)>FLING_MIN_VELOCITY){  
				//向左手势
				Intent intent = new Intent(About.this,Query.class);  
				startActivity(intent); 
				overridePendingTransition( R.anim.in_from_right,R.anim.out_to_left);
			}  
			else if ((e2.getX()-e1.getX())>FLING_MIN_DISTANCE && Math.abs(velocityX)>FLING_MIN_VELOCITY) {  
				//向右手势
				Intent intent1 = new Intent(About.this,Choice.class);  
				startActivity(intent1); 
				overridePendingTransition( R.anim.in_from_left,R.anim.out_to_right);
			} 
		}catch(Exception e){
			Toast.makeText(About.this, "滑动换屏失败", Toast.LENGTH_SHORT).show();
		}
		return false;
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		try{
			gd.onTouchEvent(event);
			//Toast.makeText(MainActivity.this,"hello", Toast.LENGTH_SHORT).show();
		}catch(Exception e1){
			Toast.makeText(About.this,"onTouchEvent(event)失败！", Toast.LENGTH_SHORT).show();
		}
		return true;
	}
}
