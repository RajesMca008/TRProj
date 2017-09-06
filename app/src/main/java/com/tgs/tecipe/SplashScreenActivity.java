package com.tgs.tecipe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.tgs.tecipe.util.AppDataBean;
import com.tgs.tecipe.util.Item;
import com.tgs.tecipe.util.XMLHandler;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import io.supercharge.shimmerlayout.ShimmerLayout;


public class SplashScreenActivity extends AppCompatActivity implements View.OnClickListener{


    private SplashScreenActivity _activity;
    private XMLHandler xmlHandler=null;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);

        _activity=this;

        setContentView(R.layout.activity_splash_screen);


        hide();

        ShimmerLayout shimmerText = (ShimmerLayout) findViewById(R.id.even_more_lay);
        shimmerText.startShimmerAnimation();
        shimmerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3=new Intent(SplashScreenActivity.this,MainActivity.class);
                startActivity(intent3);
            }
        });

        Animation bottomUp = AnimationUtils.loadAnimation(this,
                R.anim.bottom_up);

        shimmerText.startAnimation(bottomUp);
        shimmerText.setVisibility(View.VISIBLE);

        if(AppDataBean.getInstance().getVegList()==null) {
            //Load data
            new BackGroundWork().execute("");
        }
        else {
            findViewById(R.id.veg_btn).setOnClickListener(this);
            findViewById(R.id.non_veg_btn).setOnClickListener(this);

            // mContentLayour.setVisibility(View.VISIBLE);
            // mContentLayour.startAnimation(animation);


        }


        goToNextLevel();
    }


    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

    }


    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.veg_btn:
                Intent intent=new Intent(SplashScreenActivity.this,ListViewActivity.class);
                intent.putExtra("IS_VEG",true);
                startActivity(intent);
                break;

            case R.id.non_veg_btn:

                Intent intent2=new Intent(SplashScreenActivity.this,ListViewActivity.class);
                intent2.putExtra("IS_VEG",false);
                startActivity(intent2);
                break;

        }
    }

    class BackGroundWork extends AsyncTask<String, String, String>
    {
        ProgressDialog dialog=null;


        ArrayList<Item> vegList=null;
        ArrayList<Item> nonVegList=null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            dialog=new ProgressDialog(_activity);
            dialog.setTitle(R.string.loading);
            dialog.setMessage("Loading...");
            dialog.show();
        }
        @Override
        protected String doInBackground(String... params) {

            try {
                SAXParser parser= SAXParserFactory.newInstance().newSAXParser();
                xmlHandler=new XMLHandler();
                parser.parse(_activity.getAssets().open("recipe.xml"), xmlHandler);


                if(xmlHandler.getItemList().size()>0)
                {
                    vegList=new  ArrayList<Item>();
                    nonVegList=new  ArrayList<Item>();

                    for (int i = 0; i < xmlHandler.getItemList().size(); i++) {

                        if( xmlHandler.getItemList().get(i).getCategory().toString().equalsIgnoreCase("veg"))
                        {
                            vegList.add(xmlHandler.getItemList().get(i));
                        }
                        else{
                            nonVegList.add(xmlHandler.getItemList().get(i));

                        }

                    }

                    AppDataBean.getInstance().setVegList(vegList);
                    AppDataBean.getInstance().setNonVegList(nonVegList);

                }

            } catch (ParserConfigurationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SAXException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            //selectItem(nonVegList.get(0),0);

            if(xmlHandler!=null)
            {

                findViewById(R.id.veg_btn).setOnClickListener(_activity);
                findViewById(R.id.non_veg_btn).setOnClickListener(_activity);
              /*  mContentLayour.setVisibility(View.VISIBLE);
                mContentLayour.startAnimation(animation);*/
            }
            dialog.dismiss();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        showInterstitial();
    }

    private void loadInterstitial() {
        // Disable the next level button and load the ad.
        AdRequest adRequest = new AdRequest.Builder()
                // .setRequestAgent("android_studio:ad_template")
                //.addTestDevice("85BE868226D2620A881B2A5C9D76AA8C")
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            //Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
            goToNextLevel();
        }
    }

    private void goToNextLevel() {
        // Show the next level and reload the ad to prepare for the level after.

        mInterstitialAd = newInterstitialAd();
        loadInterstitial();
    }

    private InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

            }

            @Override
            public void onAdClosed() {
                // Proceed to the next level.
                goToNextLevel();
            }
        });
        return interstitialAd;
    }
}
