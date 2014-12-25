package com.hopecontacts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.hopecontacts.Choice.cancelAction;
import com.hopecontacts.Choice.okAction;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Query extends Activity implements OnTouchListener,OnGestureListener{

	public int FLING_MIN_DISTANCE=50;
	public int FLING_MIN_VELOCITY=0;
	public GestureDetector gd;
	
	private ListView listView;
	private String [] mStrings;
	private Cursor cursor;
	private SQLiteDatabase db;
	
	//private View textEntryView;
	private EditText nameInQuery;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query);
		listView=(ListView) findViewById(R.id.listView1);
		try{
			String path=Environment.getExternalStorageDirectory()+"/hopecontacts/"; //复制到SD卡的路径
			String outFileName=path+"hope.s3db"; //复制到SD卡的文件名
			String fileName="hope.s3db"; //内部文件名，assets下
			File file=new File(outFileName);
			if (!file.exists() ){
				copyDBtoSDcard(fileName,outFileName,path);  
			}
			db=SQLiteDatabase.openOrCreateDatabase(file, null);
			findAll();
		}catch(Exception e){
			Intent intent=new Intent(Query.this,MainActivity.class);
			startActivity(intent);
			Toast.makeText(Query.this, "获取数据失败", Toast.LENGTH_SHORT).show();
		}
		
		
		listView.setOnItemLongClickListener(new listAction());
		//listView.setOnItemClickListener(new adapterAction());
		
		gd=new GestureDetector(this,(OnGestureListener)this); 
	    RelativeLayout RL = (RelativeLayout) findViewById(R.id.rl);  
	    RL.setOnTouchListener(this);  
	    RL.setLongClickable(true);  
		
	}
	//长按列表动作
	class listAction implements AdapterView.OnItemLongClickListener{
		@Override
		public boolean onItemLongClick(AdapterView<?> parent,View v, int pos, long id) {

			cursor.moveToPosition(pos);
			String select_dialog_items[]={"删除信息","修改信息","查看详细"};
			
			new AlertDialog.Builder(Query.this)
					.setTitle("选择操作")
					.setItems(select_dialog_items,new dialogAction()).show();

			return false;
		}
	}

	//长按列表弹出框选择后的动作
	class dialogAction implements DialogInterface.OnClickListener{
		@Override
		public void onClick(DialogInterface dialog,int which) {

			switch (which) {
			case 0:// 删除
				AlertDialog ad = new AlertDialog.Builder(Query.this)
        			.setTitle("删除数据将不可恢复！")
        			.setIcon(R.drawable.a9o)
        			.setPositiveButton("确定", new deleteOkAction())
        			.setNegativeButton("取消", new deleteCancelAction())
        			.create();
				ad.show();
				
				break;
			case 1:// 修改
				putDataOut(Query.this,Mod.class);
				break;
			case 2:
				//Toast.makeText(Query.this, "查看详细", Toast.LENGTH_SHORT).show();
				putDataOut(Query.this,Detail.class);
				break;
			}
			
		}
	}
	class deleteOkAction implements DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			try{
				String xyz=cursor.getString(0);  //使用游标即可获取第一个字符串
				String sql="delete from contacts where name='"+xyz+"'";
				db.execSQL(sql);
				Toast.makeText(Query.this, "删除成功", Toast.LENGTH_SHORT).show();
			}catch(Exception e){
				Toast.makeText(Query.this, "删除失败", Toast.LENGTH_SHORT).show();
			}
			// 重新绑定查询
			findAll();
		}
		
	}
	class deleteCancelAction implements DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private void putDataOut(Context from,Class to){
		Intent intent2 = new Intent();  //(Query.this,Detail.class);
		intent2.setClass(from, to);
		intent2.putExtra(SysConst.NAME,cursor.getString(0));
		intent2.putExtra(SysConst.MAJOR,cursor.getString(1));
		intent2.putExtra(SysConst.SEX,cursor.getString(2));
		intent2.putExtra(SysConst.PHONE,cursor.getString(3));
		intent2.putExtra(SysConst.EMAIL,cursor.getString(4));
		intent2.putExtra(SysConst.QQ,cursor.getString(5));
		intent2.putExtra(SysConst.BANK,cursor.getString(6));
		intent2.putExtra(SysConst.ID,cursor.getString(7));
		intent2.putExtra(SysConst.PLACE,cursor.getString(8));
		startActivity(intent2);
		overridePendingTransition( R.anim.in_from_right,R.anim.out_to_left);
	}
	
	private void copyDBtoSDcard(String fileName,String outFileName,String path){
		
		try{
			//判断路径是否存在
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
			Toast.makeText(Query.this, "复制数据成功", Toast.LENGTH_SHORT).show();
			/**/
		}catch(Exception e){
			Toast.makeText(Query.this, "复制数据失败", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		
	}
	
	//查询语句
	private void findAll(){
		String sql="select * from contacts order by name";
		try{
				cursor=db.rawQuery(sql, null);
				
				int i=0;
				int len=0;
				cursor.moveToFirst();
				while(!cursor.isAfterLast()){
					cursor.moveToNext();
					len++;
				}
				mStrings=new String[len];

				cursor.moveToFirst();
				while(!cursor.isAfterLast()){
					mStrings[i]=cursor.getString(0)+"\t\t"+cursor.getString(3);    //+"   "+cursor.getString(2);
					/*
					int len2=cursor.getBlob(1).length;
					byte [] buff=new byte[len2+1];
					buff=cursor.getBlob(1);
					String x=EncodingUtils.getString(buff, "utf-8");
					mStrings[i]=x;
					*/
					cursor.moveToNext();
					i++;
				}
				ArrayAdapter<CharSequence> adapter=new ArrayAdapter<CharSequence>(this,android.R.layout.simple_list_item_1,mStrings);
				listView.setAdapter(adapter);
				//listView.setOnItemClickListener(new adapterAction());
				//Toast.makeText(Query.this, "查询成功", Toast.LENGTH_SHORT).show();
		}catch(Exception e){
			Toast.makeText(Query.this, "查询失败", Toast.LENGTH_SHORT).show();
		}
		
		
	}
	
	//单击列表时的动作
	class adapterAction implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			// TODO Auto-generated method stub
			
			Toast.makeText(Query.this, mStrings[position], Toast.LENGTH_SHORT).show();
			
		}	
	}

	
	//实现左右滑动部分，因为使用接口，要重载函数
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
				Intent intent = new Intent(Query.this,Choice.class);  
				startActivity(intent); 
				overridePendingTransition( R.anim.in_from_right,R.anim.out_to_left);
			}  
			else if ((e2.getX()-e1.getX())>FLING_MIN_DISTANCE && Math.abs(velocityX)>FLING_MIN_VELOCITY) {  
				//向右手势
				Intent intent_1 = new Intent(Query.this,MainActivity.class);  
				startActivity(intent_1); 
				overridePendingTransition( R.anim.in_from_left,R.anim.out_to_right);
			} 
		}catch(Exception e){
			Toast.makeText(Query.this, "滑动换屏失败", Toast.LENGTH_SHORT).show();
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
			Toast.makeText(Query.this,"onTouchEvent(event)失败！", Toast.LENGTH_SHORT).show();
		}
		return true;
	}
	
	//左右滑动代码结束
	//建立弹出列表及操作
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		
		menu.add(0,Menu.FIRST,0,R.string.find);
		menu.add(0,Menu.FIRST+1,0,R.string.choice_add);
		menu.add(0,Menu.FIRST+2,0,R.string.ex);
		menu.add(0,Menu.FIRST+3,0,R.string.exit);
		return super.onCreateOptionsMenu(menu);//true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case Menu.FIRST:
			//Toast.makeText(Query.this, "选择第一个", Toast.LENGTH_SHORT).show();
			openDialog();
			
			break;
		case Menu.FIRST+1:
			Intent intent1 = new Intent(Query.this,Add.class);  
			startActivity(intent1);
			overridePendingTransition( R.anim.in_from_right,R.anim.out_to_left);
			break;
		case Menu.FIRST+2:
			Intent intent2 = new Intent(Query.this,Choice.class);  
			startActivity(intent2); 
			overridePendingTransition( R.anim.in_from_right,R.anim.out_to_left);
			break;
		case Menu.FIRST+3:
			Intent intent3 = new Intent(Query.this,MainActivity.class);  
			startActivity(intent3); 
			break;
		}
		
		return true;
		
	}
	
	public void openDialog(){
		LayoutInflater factory = LayoutInflater.from(Query.this);
        View textEntryView = factory.inflate(R.layout.dialog_query, null);
        nameInQuery= (EditText) textEntryView.findViewById(R.id.editText1);
        AlertDialog dlg = new AlertDialog.Builder(Query.this)
        	.setTitle("请输入查找的名字")
    		.setView(textEntryView)
    		.setIcon(R.drawable.emoji_86)
    		.setPositiveButton("确定", new okAction())
    		.setNegativeButton("取消", new cancelAction())
    		.create();
        dlg.show();
        
	}
	
	//弹出对话框按钮的动作
	class okAction implements DialogInterface.OnClickListener{
    	public void onClick(DialogInterface dialog, int whichButton) {
            try{
                String nameToFind = nameInQuery.getText().toString();
                showFind(nameToFind);
                //Toast.makeText(Query.this, "成功", Toast.LENGTH_SHORT).show();
            }catch(Exception e){
            	Toast.makeText(Query.this,"查找失败", Toast.LENGTH_SHORT).show();
            }   
        }
    }
    
	public void showFind(String nameToFind){
		String sql="select * from contacts where name='"+nameToFind+"'";
		Cursor cursor2=db.rawQuery(sql, null);
		cursor2.moveToFirst();
		Intent intentx=new Intent(Query.this,Detail.class);
		
		intentx.putExtra(SysConst.NAME, cursor2.getString(0).toString());
		intentx.putExtra(SysConst.MAJOR, cursor2.getString(1).toString());
		intentx.putExtra(SysConst.SEX, cursor2.getString(2).toString());
		intentx.putExtra(SysConst.PHONE, cursor2.getString(3).toString());
		intentx.putExtra(SysConst.EMAIL, cursor2.getString(4).toString());
		intentx.putExtra(SysConst.QQ, cursor2.getString(5).toString());
		intentx.putExtra(SysConst.BANK, cursor2.getString(6).toString());
		intentx.putExtra(SysConst.ID, cursor2.getString(7).toString());
		intentx.putExtra(SysConst.PLACE, cursor2.getString(8).toString());
		/**/
		startActivity(intentx);
		
	}
	
    class cancelAction implements  DialogInterface.OnClickListener{
    	public void onClick(DialogInterface dialog, int whichButton) {
           
              
           }
    }
	
	
}


