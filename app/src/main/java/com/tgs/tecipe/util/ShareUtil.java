package com.tgs.tecipe.util;

import java.util.List;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
 

public class ShareUtil {

	
	/**
	 * Post on Facebook wall
	 * @param activity
	 * @param requestCode
	 * @param appName
	 * @param description
	 * @param appURL
	 * @param appIconURL
	 */
	
	public static final int fbRequestCode = 1010;
	public static final int twitterRequestCode = 1100;
 
	
	 
	public static String FB_APPID =  "248138842037654"; 
	 
	
	
	
	/*public static void postOnFacebookWall(Activity activity, int requestCode,
			String appName, String description, String appURL, Bitmap appIconURL, String imageURL,String API_KEY) {
		Intent fbintent = new Intent(activity, ShareOnFacebook.class);
		fbintent.putExtra("AppName", appName);
		fbintent.putExtra("Description", description);
		fbintent.putExtra("AppUrl", appURL);
		//fbintent.putExtra("ImageUrl", appIconURL);
		//fbintent.putExtra("ImgUrl", imageURL);
		fbintent.putExtra("API_KEY", API_KEY);
		activity.startActivityForResult(fbintent, requestCode);
	}*/

	/**
	 * Post on TwitterWall
	 * @param activity
	 * @param requestCode
	 * @param appName
	 * @param description
	 * @param appURL
	 * @param appIconURL
	 *//*
	public static void postOnTwitterWall(Activity activity, int requestCode,
			String appName, String description, String appURL, String appIconURL) {
		Intent intentTweet = new Intent(activity, TwitterActivity.class);
		intentTweet.putExtra("AppName", appName);
		intentTweet.putExtra("Description", description);
		intentTweet.putExtra("AppUrl", appURL);
		activity.startActivityForResult(intentTweet, requestCode);
	}

	*//**
	 * Post on Google Plus
	 * @param activity
	 * @param requestCode
	 * @param appName
	 * @param description
	 * @param appURL
	 * @param appIconURL
	 *//*
	public static void postOnGooglePlus(Activity activity, int requestCode,
			String appName, String description, String appURL, String appIconURL){
		if(isPackageExisted("com.google.android.apps.plus",activity)){
			Intent shareIntent = new PlusShare.Builder(activity)
            .setText(appName)
            .setType("image/png")
            .setContentDeepLinkId("appNameID",
            		description+"\n Download Link : "+appIconURL,
            		appURL,
                    Uri.parse(appIconURL))
            .getIntent();
			activity.startActivityForResult(shareIntent, requestCode);
			}
		else{
			Intent it = new Intent();
			activity.setResult(0,it);
		}
	}*/
	
	
	public static boolean isPackageExisted(String targetPackage,Activity activity){
        List<ApplicationInfo> packages;
        PackageManager pm;
            pm = activity.getPackageManager();        
            packages = pm.getInstalledApplications(0);
            for (ApplicationInfo packageInfo : packages) {
        if(packageInfo.packageName.equals(targetPackage)) return true;
        }        
        return false;
        
    }
}
