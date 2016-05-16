package com.tgs.tecipe.util;

import android.app.Activity;

public class ShareOnFacebook extends Activity{/*

	private Facebook facebook;//  
//	  private String messageToPost;
	private SharedPreferences sharedPreferences;
	private String descriptionMsg;
	private String appName;
//	private String movieRating;
	private String appUrl;
//	private Bitmap imageUrl;
	private Intent facebookInt;
	private boolean login = false;
	private String urlFromSrvr;
	private String API_KEY;
	 
 
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		descriptionMsg = getIntent().getStringExtra("Description");
		appName = getIntent().getStringExtra("AppName");
		appUrl =  getIntent().getStringExtra("AppUrl");
		appUrl="https://play.google.com/store/apps/details?id="+getPackageName();
		
 
		urlFromSrvr = getIntent().getStringExtra("ImgUrl");//"http://202.89.104.122:8080/ApplicationAddPush/images/GeniousL.png";
		
		API_KEY = getIntent().getStringExtra("API_KEY");
 
		
		facebook = new Facebook(API_KEY);
		
		
		facebookInt = new Intent();
 
		
		//Check if we already have an access token
		sharedPreferences = getPreferences(MODE_PRIVATE);
		String access_token = sharedPreferences.getString("access_token", null);
		long expires = sharedPreferences.getLong("access_expires", 0);

		if(access_token != null) facebook.setAccessToken(access_token);
		if(expires != 0) facebook.setAccessExpires(expires);

		if (facebook.isSessionValid()) {
//			launchPost(descriptionMsg+"\n Rating : "+movieRating, imageUrl, movieRating, movieName);
			if (facebook.getAccessToken() != null) {
				Bundle bundle = new Bundle();
				bundle.putString("message", descriptionMsg);// the message to post to the wall
//				bundle.putString("caption", "");// the message to post to the wall
				bundle.putString("picture", urlFromSrvr);// the message to post to the wall
//				bundle.putByteArray("picture", data);   
				bundle.putString("name", appName);// the message to post to the wall
				bundle.putString("link", appUrl);
				bundle.putString(Facebook.TOKEN,facebook.getAccessToken());
				login =false;
				new PostToWall(bundle).execute();
			}
			
//			finish();
		} else {
			authorizeWithFacebook();
		}
	}
  
	@SuppressWarnings("deprecation")
	protected void authorizeWithFacebook() {
		facebook.authorize(this, new String[] {"offline_access", "publish_stream","read_stream", "user_photos", "publish_checkins","photo_upload"}, new DialogListener() {
			@Override
			public void onComplete(Bundle values) {
				//Save access token on successful login
				saveFacebookAccess();
				if (facebook.getAccessToken() != null) {
					Bundle bundle = new Bundle();
					bundle.putString("message", descriptionMsg);// the message to post to the wall
//					bundle.putString("caption", "");// the message to post to the wall
					bundle.putString("picture", urlFromSrvr);// the message to post to the wall
					bundle.putString("name", appName); 
					bundle.putString("link", appUrl);
					bundle.putString(Facebook.TOKEN,facebook.getAccessToken());
					login = true; 
					new PostToWall(bundle).execute();
					}
				
			}

			@Override
			public void onFacebookError(FacebookError error) {
				facebookInt.putExtra("MSG", "Facebook login error");
				setResult(ShareUtil.fbRequestCode, facebookInt);
				finish();
			}

			@Override
			public void onError(DialogError e) {
				facebookInt.putExtra("MSG", "Login error");
				setResult(ShareUtil.fbRequestCode, facebookInt);
				finish();
			}

			@Override
			public void onCancel() {
				facebookInt.putExtra("MSG", "Login canceled");
				setResult(ShareUtil.fbRequestCode, facebookInt);
				finish();
			}
			
			
		});
	}

	@SuppressWarnings("deprecation")
	protected void saveFacebookAccess() {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("access_token", facebook.getAccessToken());
		editor.putLong("access_expires", facebook.getAccessExpires());
		editor.commit();
	}
	
	private class PostToWall extends AsyncTask<Void, Void, Void>{
		private ProgressDialog fbDialog;
		private String response;
		private Bundle postBndl;
		public PostToWall(Bundle bundle) {
			this.postBndl = bundle;
		}
		
		@Override
		protected void onPreExecute() {
			if(!login){
			fbDialog = ProgressDialog.show(ShareOnFacebook.this, "", "Posting please wait...",true);
			}
			super.onPreExecute();
		}
		@SuppressWarnings("deprecation")
		@Override
		protected Void doInBackground(Void... params) {
			try {
				response = facebook.request("me/feed", postBndl,"POST");
				 Log.v("launchPost", "RESPONSE :: "+ response);
			} catch (Exception e) {
				response = null;
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			if(!login){
			fbDialog.cancel();
			}
			facebookInt.putExtra("MSG", "Posted successfully...");
			setResult(ShareUtil.fbRequestCode, facebookInt);
			finish();
				
			super.onPostExecute(result);
		}
	}
	
	
	@SuppressWarnings("deprecation")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		facebook.authorizeCallback(requestCode, resultCode, data);
	}

*/}