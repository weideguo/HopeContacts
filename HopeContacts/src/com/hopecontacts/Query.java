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
			String path=Environment.getExternalStorageDirectory()+"/hopecontacts/"; //���Ƶ�SD����·��
			String outFileName=path+"hope.s3db"; //���Ƶ�SD�����ļ���
			String fileName="hope.s3db"; //�ڲ��ļ�����assets��
			File file=new File(outFileName);
			if (!file.exists() ){
				copyDBtoSDcard(fileName,outFileName,path);  
			}
			db=SQLiteDatabase.openOrCreateDatabase(file, null);
			findAll();
		}catch(Exception e){
			Intent intent=new Intent(Query.this,MainActivity.class);
			startActivity(intent);
			Toast.makeText(Query.this, "��ȡ����ʧ��", Toast.LENGTH_SHORT).show();
		}
		
		
		listView.setOnItemLongClickListener(new listAction());
		//listView.setOnItemClickListener(new adapterAction());
		
		gd=new GestureDetector(this,(OnGestureListener)this); 
	    RelativeLayout RL = (RelativeLayout) findViewById(R.id.rl);  
	    RL.setOnTouchListener(this);  
	    RL.setLongClickable(true);  
		
	}
	//�����б���
	class listAction implements AdapterView.OnItemLongClickListener{
		@Override
		public boolean onItemLongClick(AdapterView<?> parent,View v, int pos, long id) {

			cursor.moveToPosition(pos);
			String select_dialog_items[]={"ɾ����Ϣ","�޸���Ϣ","�鿴��ϸ"};
			
			new AlertDialog.Builder(Query.this)
					.setTitle("ѡ�����")
					.setItems(select_dialog_items,new dialogAction()).show();

			return false;
		}
	}

	//�����б�����ѡ���Ķ���
	class dialogAction implements DialogInterface.OnClickListener{
		@Override
		public void onClick(DialogInterface dialog,int which) {

			switch (which) {
			case 0:// ɾ��
				AlertDialog ad = new AlertDialog.Builder(Query.this)
        			.setTitle("ɾ�����ݽ����ɻָ���")
        			.setIcon(R.drawable.a9o)
        			.setPositiveButton("ȷ��", new deleteOkAction())
        			.setNegativeButton("ȡ��", new deleteCancelAction())
        			.create();
				ad.show();
				
				break;
			case 1:// �޸�
				putDataOut(Query.this,Mod.class);
				break;
			case 2:
				//Toast.makeText(Query.this, "�鿴��ϸ", Toast.LENGTH_SHORT).show();
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
				String xyz=cursor.getString(0);  //ʹ���α꼴�ɻ�ȡ��һ���ַ���
				String sql="delete from contacts where name='"+xyz+"'";
				db.execSQL(sql);
				Toast.makeText(Query.this, "ɾ���ɹ�", Toast.LENGTH_SHORT).show();
			}catch(Exception e){
				Toast.makeText(Query.this, "ɾ��ʧ��", Toast.LENGTH_SHORT).show();
			}
			// ���°󶨲�ѯ
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
			//�ж�·���Ƿ����
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
			Toast.makeText(Query.this, "�������ݳɹ�", Toast.LENGTH_SHORT).show();
			/**/
		}catch(Exception e){
			Toast.makeText(Query.this, "��������ʧ��", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		
	}
	
	//��ѯ���
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
				//Toast.makeText(Query.this, "��ѯ�ɹ�", Toast.LENGTH_SHORT).show();
		}catch(Exception e){
			Toast.makeText(Query.this, "��ѯʧ��", Toast.LENGTH_SHORT).show();
		}
		
		
	}
	
	//�����б�ʱ�Ķ���
	class adapterAction implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			// TODO Auto-generated method stub
			
			Toast.makeText(Query.this, mStrings[position], Toast.LENGTH_SHORT).show();
			
		}	
	}

	
	//ʵ�����һ������֣���Ϊʹ�ýӿڣ�Ҫ���غ���
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
				//��������
				Intent intent = new Intent(Query.this,Choice.class);  
				startActivity(intent); 
				overridePendingTransition( R.anim.in_from_right,R.anim.out_to_left);
			}  
			else if ((e2.getX()-e1.getX())>FLING_MIN_DISTANCE && Math.abs(velocityX)>FLING_MIN_VELOCITY) {  
				//��������
				Intent intent_1 = new Intent(Query.this,MainActivity.class);  
				startActivity(intent_1); 
				overridePendingTransition( R.anim.in_from_left,R.anim.out_to_right);
			} 
		}catch(Exception e){
			Toast.makeText(Query.this, "��������ʧ��", Toast.LENGTH_SHORT).show();
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
			Toast.makeText(Query.this,"onTouchEvent(event)ʧ�ܣ�", Toast.LENGTH_SHORT).show();
		}
		return true;
	}
	
	//���һ����������
	//���������б�����
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
			//Toast.makeText(Query.this, "ѡ���һ��", Toast.LENGTH_SHORT).show();
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
        	.setTitle("��������ҵ�����")
    		.setView(textEntryView)
    		.setIcon(R.drawable.emoji_86)
    		.setPositiveButton("ȷ��", new okAction())
    		.setNegativeButton("ȡ��", new cancelAction())
    		.create();
        dlg.show();
        
	}
	
	//�����Ի���ť�Ķ���
	class okAction implements DialogInterface.OnClickListener{
    	public void onClick(DialogInterface dialog, int whichButton) {
            try{
                String nameToFind = nameInQuery.getText().toString();
                showFind(nameToFind);
                //Toast.makeText(Query.this, "�ɹ�", Toast.LENGTH_SHORT).show();
            }catch(Exception e){
            	Toast.makeText(Query.this,"����ʧ��", Toast.LENGTH_SHORT).show();
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


