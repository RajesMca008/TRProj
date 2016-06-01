package com.tgs.tecipe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

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


public class SplashScreenActivity extends AppCompatActivity implements View.OnClickListener{


   // private RelativeLayout mContentLayour=null;
   // private Animation animation =null;

    //private TextView vegText =null;
    //private TextView nonvegText =null;

    private SplashScreenActivity _activity;
    private XMLHandler xmlHandler=null;


    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);

        _activity=this;

        setContentView(R.layout.activity_splash_screen);

       // mContentLayour=(RelativeLayout)findViewById(R.id.main_content_layout);








       //mContentLayour.setVisibility(View.GONE);


        //animation = AnimationUtils.loadAnimation(SplashScreenActivity.this,R.anim.bottom_up);

        //animation.setDuration(1000);

       // vegText =(TextView)findViewById(R.id.veg_text);
       // nonvegText =(TextView)findViewById(R.id.non_text);

        hide();



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

        /////////////////////////////////////////

      /*  final AlphaAnimation   animation1 = new AlphaAnimation(0.0f, 1.0f);
        animation1.setDuration(500);
        animation1.setStartOffset(500);


        final AlphaAnimation animation2 = new AlphaAnimation(1.0f, 0.0f);
        animation2.setDuration(500);
        animation2.setStartOffset(500);*/

        //animation1 AnimationListener
      /*  animation1.setAnimationListener(new Animation.AnimationListener(){

            @Override
            public void onAnimationEnd(Animation arg0) {
                // start animation2 when animation1 ends (continue)
                vegText.startAnimation(animation2);
                nonvegText.startAnimation(animation2);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }

        });



        //animation2 AnimationListener
        animation2.setAnimationListener(new Animation.AnimationListener(){

            @Override
            public void onAnimationEnd(Animation arg0) {
                // start animation1 when animation2 ends (repeat)
                vegText.startAnimation(animation1);
                nonvegText.startAnimation(animation1);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }

        });
*/
       // vegText.startAnimation(animation1);


        goToNextLevel();
    }


   /* Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mContentLayour.setVisibility(View.VISIBLE);
            mContentLayour.startAnimation(animation);
        }


    };*/



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
