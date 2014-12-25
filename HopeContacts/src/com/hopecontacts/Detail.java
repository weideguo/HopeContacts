package com.hopecontacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class Detail extends Activity{
	
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
	
	private String sname="";
	private String smajor="";
	private String ssex="";
	private String sphone="";
	private String semail="";
	private String sqq="";
	private String sbank="";
	private String sid="";
	private String splace="";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_mod_detail_find);
		
		button1=(Button) findViewById(R.id.button1);
		button2=(Button) findViewById(R.id.button2);
		name=(EditText) findViewById(R.id.editText1);
		major=(EditText) findViewById(R.id.editText2);
		sex=(EditText) findViewById(R.id.editText3);
		phone=(EditText) findViewById(R.id.editText4);
		email= (EditText) findViewById(R.id.editText5);
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
		
		button1.setOnClickListener(new backAction());
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
	}
	
	class backAction implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent1=new Intent();
			intent1.setClass(Detail.this, Query.class);;
			startActivity(intent1);
			overridePendingTransition( R.anim.in_from_left,R.anim.out_to_right);
		}
		
	}
}