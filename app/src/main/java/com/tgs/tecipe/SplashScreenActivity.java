package com.tgs.tecipe;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class SplashScreenActivity extends AppCompatActivity implements View.OnClickListener{


    private RelativeLayout mContentLayour=null;
    private Animation animation =null;

    private TextView vegText =null;
    private TextView nonvegText =null;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        mContentLayour=(RelativeLayout)findViewById(R.id.main_content_layout);

        findViewById(R.id.veg_btn).setOnClickListener(this);
        findViewById(R.id.non_veg_btn).setOnClickListener(this);



        mContentLayour.setVisibility(View.GONE);


        animation = AnimationUtils.loadAnimation(SplashScreenActivity.this,R.anim.bottom_up);

        animation.setDuration(1000);

        vegText =(TextView)findViewById(R.id.veg_text);
        nonvegText =(TextView)findViewById(R.id.non_text);

        hide();

        new Thread()
        {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(500);
                    Message  message=new Message();
                    handler.sendMessage(message);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();



        /////////////////////////////////////////

        final AlphaAnimation   animation1 = new AlphaAnimation(0.0f, 1.0f);
        animation1.setDuration(500);
        animation1.setStartOffset(500);


       final AlphaAnimation animation2 = new AlphaAnimation(1.0f, 0.0f);
        animation2.setDuration(500);
        animation2.setStartOffset(500);

        //animation1 AnimationListener
        animation1.setAnimationListener(new Animation.AnimationListener(){

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

        vegText.startAnimation(animation1);



    }


    Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mContentLayour.setVisibility(View.VISIBLE);
            mContentLayour.startAnimation(animation);
        }


    };



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
                startActivity(intent);
                break;

            case R.id.non_veg_btn:
                break;
        }
    }
}
