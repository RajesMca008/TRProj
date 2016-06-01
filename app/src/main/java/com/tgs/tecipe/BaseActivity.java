package com.tgs.tecipe;

import java.util.regex.Pattern;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;

import com.google.android.gcm.GCMRegistrar;

public class BaseActivity extends AppCompatActivity{


	// label to display gcm messages
	//TextView lblMessage;

	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;

	// Alert dialog manager
	AlertDialogManager alert = new AlertDialogManager();

	// Connection detector
	ConnectionDetector cd;

	public static String name;
	public static String email;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);

		cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			/*alert.showAlertDialog(BaseActivity.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);*/
			// stop executing code by return
			return;
		}

		// Getting name, email from intent
		//Intent i = getIntent();



		//email = i.getStringExtra("email");		

		try{
			// Make sure the device has the proper dependencies.
			GCMRegistrar.checkDevice(this);

			// Make sure the manifest was properly set - comment out this line
			// while developing the app, then uncomment it when it's ready.
			GCMRegistrar.checkManifest(this);

			//lblMessage = (TextView) findViewById(R.id.lblMessage);

			registerReceiver(mHandleMessageReceiver, new IntentFilter(
					CommonUtilities.DISPLAY_MESSAGE_ACTION));

			// Get GCM registration id
			final String regId = GCMRegistrar.getRegistrationId(this);

			System.out.println("TEST Resid"+regId);
			// Check if regid already presents
			if (regId.equals("")) {
				// Registration is not present, register now with GCM
				GCMRegistrar.register(this, CommonUtilities.SENDER_ID);
			} else {
				// Device is already registered on GCM
				if (GCMRegistrar.isRegisteredOnServer(this)) {
					// Skips registration.
					Log.d("BaseActivity ","RegisteredOnServer");
					//Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
				} else {
					Log.d("BaseActivity ","Not RegisteredOnServer");
					// Try to register again, but not in the UI thread.
					// It's also necessary to cancel the thread onDestroy(),
					// hence the use of AsyncTask instead of a raw thread.
					String possibleEmail="Telugu :"+Build.MANUFACTURER;

					try{
						Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
						Account[] accounts = AccountManager.get(this).getAccounts();
						for (Account account : accounts) {
							name=account.name;
							Log.e("TEST ","name "+name);
							if (emailPattern.matcher(account.name).matches()) {
								possibleEmail = account.name;

							}

							if(account.equals("com.whatsapp")){
								String phoneNumber = account.name;
								Log.e("TEST ","Number "+phoneNumber);
							}
						}
					}catch (Exception e)
					{
						e.printStackTrace();
					}
					email= possibleEmail;
					final Context context = this;
					mRegisterTask = new AsyncTask<Void, Void, Void>() {

						@Override
						protected Void doInBackground(Void... params) {
							// Register on our server
							// On server creates a new user


							ServerUtilities.register(context, name, email, regId);
							return null;
						}

						@Override
						protected void onPostExecute(Void result) {
							mRegisterTask = null;
						}

					};
					mRegisterTask.execute(null, null, null);
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Receiving push messages
	 * */
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			try{
				String newMessage = intent.getExtras().getString(CommonUtilities.EXTRA_MESSAGE);
				// Waking up mobile if it is sleeping
				WakeLocker.acquire(getApplicationContext());

				/**
				 * Take appropriate action on this message
				 * depending upon your app requirement
				 * For now i am just displaying it on the screen
				 * */

				// Showing received message
				//lblMessage.append(newMessage + "\n");

				// Releasing wake lock
				WakeLocker.release();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	protected void onDestroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}


}
