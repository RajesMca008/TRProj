package com.tgs.tecipe;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity{


	public void showToast(String s) {

	 Toast.makeText(this,s, Toast.LENGTH_LONG).show();

	}
}
