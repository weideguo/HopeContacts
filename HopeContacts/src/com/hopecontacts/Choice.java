package com.hopecontacts;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.util.EncodingUtils;

public class Choice extends Activity implements OnTouchListener,OnGestureListener{
	private Button qButton;
	private Button aButton;
	private Button dButton;
	private String getName="";
	private TextView text;
	
	private String fileName;
	private String path;
	private String fname;
	private String outFileName;
	private File file;
	
	public int FLING_MIN_DISTANCE=50;
	public int FLING_MIN_VELOCITY=0;
	public GestureDetector gd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choice);
		qButton=(Button) findViewById(R.id.button1);
		aButton=(Button) findViewById(R.id.button2);
		dButton=(Button) findViewById(R.id.button3);
		text=(TextView) findViewById(R.id.textView1);
		/*
		Bundle bundle=this.getIntent().getExtras();
		*/
		try{
			String res;
			FileInputStream fin = openFileInput("name.txt");
	        int length = fin.available();
	        byte [] buffer = new byte[length];
	        fin.read(buffer);    
	        res = EncodingUtils.getString(buffer, "UTF-8");
	        fin.close(); 
			getName="——————"+res;
			text.append(getName);
		}catch(Exception e){
			
		}
		
		
		fileName="hope.s3db";
		path=Environment.getExternalStorageDirectory()+"/hopecontacts/";
		fname="hope.s3db";
		outFileName=path+fname;
		file=new File(outFileName);
		if (!file.exists() ){
			copyDBtoSDcard(fileName,outFileName,path);  
		}
		/*
		else{
			Toast.makeText(Choice.this, "数据已经存在", Toast.LENGTH_SHORT).show();
		}
		*/
		qButton.setOnClickListener(new queryAction());
		aButton.setOnClickListener(new addAction());
		dButton.setOnClickListener(new downloadAction());
		
		gd=new GestureDetector(this,(OnGestureListener)this); 
	    RelativeLayout RL = (RelativeLayout) findViewById(R.id.choiceLoyout);  
	    RL.setOnTouchListener(this);  
	    RL.setLongClickable(true); 
	}
	
	//从assets文件夹下复制文件到SDcard
	private void copyDBtoSDcard(String fileName,String outFileName,String path){
		
		try{
			File path1 = new File(path); 
			if (!path1.exists()) {  
	            path1.mkdirs();
	        } 
			InputStream in = getResources().getAssets().open(fileName);	
			int length = in.available();        
			byte [] buffer = new byte[length];       
			in.read(buffer);
			OutputStream fout = new FileOutputStream(outFileName);
			fout.write(buffer);
			in.close();
			fout.close();
			Toast.makeText(Choice.this, "复制数据成功", Toast.LENGTH_SHORT).show();
			/**/
		}catch(Exception e){
			Toast.makeText(Choice.this, "复制数据失败", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		
	}
		
	class queryAction implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//Toast.makeText(Choice.this, "点击查询", Toast.LENGTH_SHORT).show();
			Intent intent1=new Intent();
			intent1.setClass(Choice.this, Query.class);;
			startActivity(intent1);
			overridePendingTransition( R.anim.in_from_left,R.anim.out_to_right);
		}
		
	}
	
	class addAction implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//Toast.makeText(Choice.this, "点击添加", Toast.LENGTH_SHORT).show();
			Intent intent1=new Intent();
			intent1.setClass(Choice.this, Add.class);;
			startActivity(intent1);
			overridePendingTransition( R.anim.in_from_right,R.anim.out_to_left);
			
		}
		
	}
	
	class downloadAction implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//Toast.makeText(Choice.this, "点击下载", Toast.LENGTH_SHORT).show();
			AlertDialog ad = new AlertDialog.Builder(Choice.this)
    			.setTitle("下载将会替换现有数据！")
    			.setIcon(R.drawable.emoji_11)
    			.setPositiveButton("确定", new downloadOkAction())
    			.setNegativeButton("取消", new downloadCancelAction())
    			.create();
			ad.show();
		}
		
	}
	class downloadOkAction implements DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			downloadFromWeb();
		}
		
	}
	
	private void downloadFromWeb(){
		Toast.makeText(Choice.this, "开始下载", Toast.LENGTH_SHORT).show();
		try{
			new MyAsyncTask().execute();
		}catch(Exception e){
			Toast.makeText(Choice.this, "下载失败", Toast.LENGTH_SHORT);
		}
		
	}
	
	//使用异步操作实现下载
	public class MyAsyncTask extends AsyncTask<String, Integer, byte[]>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //    在onPreExecute()中我们让ProgressDialog显示出来
    		
        }
        
        @Override
        protected byte[] doInBackground(String... params){
        	try{
           //你的耗时操作。网络访问的代码
        		String urlStr="http://ce.sysu.edu.cn/hope/UploadFiles/file/rar/201412/20141224213156222.rar";    //"http://www.baidu.com/img/baidu_sylogo1.gif";
        		
        		URL url=new URL(urlStr);
        		//int DownedFileLength=0;
        		HttpURLConnection conn=(HttpURLConnection)url.openConnection(); 
        		InputStream input=conn.getInputStream(); 
        		int len=conn.getContentLength();
        		byte[] buffer=new byte[len];
           	 	input.read(buffer);
           	 	
           	 	//因为尚未实现，先用临时路径
          	    String path=Environment.getExternalStorageDirectory()+"/hopecontacts/hope.rar";
          	  	File file=new File(path);
          	  	//
          	  	
          	  	OutputStream output=new FileOutputStream(file);
          	  	output.write(buffer);
           
        	}catch (Exception e) {
        		Toast.makeText(Choice.this, "连接失败 ", Toast.LENGTH_SHORT).show();
        	}
        	return null;
        }
        
        @Override
        protected void onProgressUpdate(Integer... values){
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(byte[] result) {
            try{
            	super.onPostExecute(result);
            	Toast.makeText(Choice.this, "下载成功 ", Toast.LENGTH_SHORT).show();
            }catch(Exception e){
            	Toast.makeText(Choice.this, "下载失败 ", Toast.LENGTH_SHORT).show();
            }
        	
            

        }
    }
	
	class downloadCancelAction implements DialogInterface.OnClickListener{
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
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
				Intent intent = new Intent(Choice.this,MainActivity.class);  
				startActivity(intent); 
				overridePendingTransition( R.anim.in_from_right,R.anim.out_to_left);
			}  
			else if ((e2.getX()-e1.getX())>FLING_MIN_DISTANCE && Math.abs(velocityX)>FLING_MIN_VELOCITY) {  
				//向右手势
				Intent intent1 = new Intent(Choice.this,Query.class);  
				startActivity(intent1); 
				overridePendingTransition( R.anim.in_from_left,R.anim.out_to_right);
			} 
		}catch(Exception e){
			Toast.makeText(Choice.this, "滑动换屏失败", Toast.LENGTH_SHORT).show();
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
			Toast.makeText(Choice.this,"onTouchEvent(event)失败！", Toast.LENGTH_SHORT).show();
		}
		return true;
	}
	
	//添加menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		menu.add(0,Menu.FIRST,0,R.string.reset).setIcon(R.drawable.a00);
		menu.add(0,Menu.FIRST+1,0,R.string.about).setIcon(R.drawable.emoji_343);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case Menu.FIRST:
			
			AlertDialog ad2 = new AlertDialog.Builder(Choice.this)
        		.setTitle("重置数据将丢失所有更改的数据！")
        		.setIcon(R.drawable.s3)
        		.setPositiveButton("确定", new okAction())
        		.setNegativeButton("取消", new cancelAction())
        		.create();
			ad2.show();
			
			break;
		case Menu.FIRST+1:
			//Toast.makeText(Choice.this, "关于", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(Choice.this,About.class);  
			startActivity(intent); 
			overridePendingTransition( R.anim.in_from_right,R.anim.out_to_left);
			break;
		}
		
		return true;
		
	}
	
	class okAction implements DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			try{
				copyDBtoSDcard(fileName,outFileName,path); 
				Toast.makeText(Choice.this, "重置成功！", Toast.LENGTH_SHORT).show();
			}catch(Exception e){
				Toast.makeText(Choice.this, "重置失败", Toast.LENGTH_SHORT).show();
			}
			
			
		}
		
	}
	class cancelAction implements DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			Toast.makeText(Choice.this, "选择取消", Toast.LENGTH_SHORT).show();
		}
		
	}
	
}
