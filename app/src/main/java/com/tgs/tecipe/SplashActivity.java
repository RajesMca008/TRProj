package com.tgs.tecipe;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;

public class SplashActivity extends ActionBarActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.splash);
		
		
		 
		new  Thread()
		{
			public void run() {
				try {
					sleep(1500);
					handler.sendMessage(new Message());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();

		 
	}
	
	Handler handler=new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			
			finish();
			Intent intent=new Intent(getApplicationContext(),DrawerActivity.class);
			startActivity(intent);
		}
		};

}