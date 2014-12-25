package com.hopecontacts;


import java.io.File;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class Add extends Activity{
	
	private Button button1;
	private Button button2;
	private EditText name;
	private EditText major;
	private EditText sex;
	private EditText phone;
	private EditText email;
	private EditText qq;
	private EditText bank;
	private EditText id;
	private EditText place;
	private String filepath;
	File file;
	SQLiteDatabase db;
	String table;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_mod_detail_find);
		button1=(Button) findViewById(R.id.button1);
		button2=(Button) findViewById(R.id.button2);
		name=(EditText) findViewById(R.id.editText1);
		major=(EditText) findViewById(R.id.editText2);
		sex=(EditText) findViewById(R.id.editText3);
		phone=(EditText) findViewById(R.id.editText4);
		email=(EditText) findViewById(R.id.editText5);
		qq=(EditText) findViewById(R.id.editText6);
		bank=(EditText) findViewById(R.id.editText7);
		id=(EditText) findViewById(R.id.editText8);
		place=(EditText) findViewById(R.id.editText9);
		
		filepath=Environment.getExternalStorageDirectory()+"/hopecontacts/hope.s3db";
		file=new File(filepath);
		db=SQLiteDatabase.openOrCreateDatabase(file, null);
		table="contacts";
		
		button1.setOnClickListener(new addAction());
		button2.setOnClickListener(new cancelAction());
		
	}
	
	
	class addAction implements OnClickListener{
		
		public boolean nameCheck(){
			String cname=name.getText().toString();
			if(cname.length()>0){
				try{
					String sql="select * from '"+table+"'"+"where name='"+cname+"'";
					Cursor c=db.rawQuery(sql, null);
					c.moveToFirst();
					if(c.getString(0).length()>0){
					Toast.makeText(Add.this, "名字已经存在！", Toast.LENGTH_SHORT).show();
						return false;
					}else{
						return true;
					}
				}catch(Exception e){
					return true;
				}
		
			}	
			else{
				Toast.makeText(Add.this, "名字不能为空！", Toast.LENGTH_SHORT).show();
				return false;
			}
				
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//
			boolean x=nameCheck();
			if(x){
				try{
					ContentValues values=new ContentValues();
					values.put(SysConst.NAME,name.getText().toString());
					values.put(SysConst.MAJOR, major.getText().toString());
					values.put(SysConst.SEX,sex.getText().toString());
					values.put(SysConst.PHONE,phone.getText().toString());
					values.put(SysConst.EMAIL,email.getText().toString());
					values.put(SysConst.QQ,qq.getText().toString());
					values.put(SysConst.BANK,bank.getText().toString());
					values.put(SysConst.ID,id.getText().toString());
					values.put(SysConst.PLACE,place.getText().toString());
					db.insert(table, null, values);
					
					Toast.makeText(Add.this, "添加成功", Toast.LENGTH_SHORT).show();
				}catch(Exception e){
					Toast.makeText(Add.this, "添加失败", Toast.LENGTH_SHORT).show();
				}
			}
			/*
			else{
				Toast toast=Toast.makeText(Add.this, "名字不能为空！", Toast.LENGTH_SHORT);
				toast.show();
			}
			*/	
		}
	}
	
	class cancelAction implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//Toast.makeText(Add.this, "返回", Toast.LENGTH_SHORT).show();
			Intent intent1 = new Intent(Add.this,Query.class);  
			startActivity(intent1); 
			overridePendingTransition( R.anim.in_from_left,R.anim.out_to_right);
		}
		
	}
	
	
}
