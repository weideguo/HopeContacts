package com.hopecontacts;


import java.io.File;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class Mod extends Activity{
	
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
	
	private String sname;
	private String smajor;
	private String ssex;
	private String sphone;
	private String semail;
	private String sqq;
	private String sbank;
	private String sid;
	private String splace;
	private SQLiteDatabase db;
	private ContentValues values1;
	
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
		
		//获取数据
		Bundle bundle=this.getIntent().getExtras();
		sname=bundle.getString(SysConst.NAME);
		smajor=bundle.getString(SysConst.MAJOR);
		ssex=bundle.getString(SysConst.SEX);
		sphone=bundle.getString(SysConst.PHONE);
		semail=bundle.getString(SysConst.EMAIL);
		sqq=bundle.getString(SysConst.QQ);
		sbank=bundle.getString(SysConst.BANK);
		sid=bundle.getString(SysConst.ID);
		splace=bundle.getString(SysConst.PLACE);
				
		setData();
	
		button1.setText("修改");
		button1.setOnClickListener(new modAction());
		button2.setOnClickListener(new backAction());
		
		
	}
	
	private void setData(){
		name.setText(sname);
		major.setText(smajor);
		sex.setText(ssex);
		phone.setText(sphone);
		email.setText(semail);
		qq.setText(sqq);
		bank.setText(sbank);
		id.setText(sid);
		place.setText(splace);
		
		//设置name为不可编辑
		name.setFilters(new InputFilter[] { new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
					// TODO Auto-generated method stub
					return source.length() < 1 ? dest.subSequence(dstart, dend) : "";
				}
			} });
	}
	
	class modAction implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			try{
				try{
					String filepath=Environment.getExternalStorageDirectory()+"/hopecontacts/hope.s3db";
					File file=new File(filepath);
					db=SQLiteDatabase.openOrCreateDatabase(file, null);
					//Toast.makeText(Mod.this, "连接成功", Toast.LENGTH_SHORT).show();
				}catch(Exception e1){
					Toast.makeText(Mod.this, "连接失败", Toast.LENGTH_SHORT).show();
				}
				
				/*
				values1=new ContentValues();
				values1.put(SysConst.MAJOR, major.getText().toString());
				values1.put(SysConst.SEX, sex.getText().toString());
				values1.put(SysConst.PHONE, phone.getText().toString());
				values1.put(SysConst.EMAIL, email.getText().toString());
				values1.put(SysConst.QQ, qq.getText().toString());
				values1.put(SysConst.BANK, bank.getText().toString());
				values1.put(SysConst.ID, id.getText().toString());
				values1.put(SysConst.PLACE, place.getText().toString());
				db.update("contacts", values1, "we", null);
				*/
				
				String smajor2=major.getText().toString();
				String ssex2=sex.getText().toString();
				String sphone2=phone.getText().toString();
				String semail2=email.getText().toString();
				String sqq2=qq.getText().toString();
				String sbank2=bank.getText().toString();
				String sid2=id.getText().toString();
				String splace2=place.getText().toString();
				
				String sql="update contacts set major='"+smajor2+"',sex='"+ssex2+"',phone='"+sphone2+"',email='"+semail2+"',qq='"+sqq2+"',bank='"+sbank2+"',id='"+sid2+"',place='"+splace2+"' where name='"+sname+"'";
				db.execSQL(sql);
				Toast.makeText(Mod.this, "修改成功", Toast.LENGTH_SHORT).show();
			}catch(Exception e){
				Toast.makeText(Mod.this, "修改失败", Toast.LENGTH_SHORT).show();
			}
			
			
		}
		
	}
	
	class backAction implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent1=new Intent();
			intent1.setClass(Mod.this, Query.class);;
			startActivity(intent1);
			overridePendingTransition( R.anim.in_from_left,R.anim.out_to_right);
		}
		
	}

}