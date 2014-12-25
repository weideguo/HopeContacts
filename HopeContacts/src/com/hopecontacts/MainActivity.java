package com.hopecontacts;

import java.io.FileOutputStream;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class MainActivity extends Activity {

	private Button button;
	private EditText nText;
	private EditText pText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		nText=(EditText) findViewById(R.id.nText);
		pText=(EditText) findViewById(R.id.pText);
		button=(Button) findViewById(R.id.button1);
		button.setOnClickListener(new loginButtonListener());
	}
	class loginButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String name=nText.getText().toString();
			String psw=pText.getText().toString();
			if(name.length()>0){
				if(psw.equals("hope8848")){
					try{
						//�����ֱ�����/data/data/files
						String fileName="name.txt";
				        FileOutputStream fout =openFileOutput(fileName, MODE_PRIVATE);
				        String writestr=name;
				        byte [] bytes = writestr.getBytes();
				        fout.write(bytes);
				        fout.close();
						//Toast.makeText(MainActivity.this, "д���ɹ���", Toast.LENGTH_SHORT).show();
						
						Intent intent=new Intent();
						intent.setClass(MainActivity.this,Query.class);
						//intent.putExtra("name", nText.getText().toString());
						startActivity(intent);
						overridePendingTransition( R.anim.in_from_right,R.anim.out_to_left);
						Toast.makeText(MainActivity.this, "��½�ɹ�", Toast.LENGTH_SHORT).show();
					}catch(Exception e){
						Toast.makeText(MainActivity.this, "��½ʧ��", Toast.LENGTH_SHORT).show();
					}	
				}else{
					Toast.makeText(MainActivity.this,"�������", Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(MainActivity.this,"�û�������Ϊ��", Toast.LENGTH_SHORT).show();
			}
			
		}
	}
	
}
