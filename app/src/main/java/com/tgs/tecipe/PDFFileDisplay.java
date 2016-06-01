package com.tgs.tecipe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnLoadCompleteListener;
import com.joanzapata.pdfview.listener.OnPageChangeListener;
import com.tgs.tecipe.util.Item;

public class PDFFileDisplay extends AppCompatActivity implements OnLoadCompleteListener, OnPageChangeListener {

    private	PDFView pdfView=null;
    private AdView adView=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        pdfView=(PDFView)findViewById(R.id.pdf_id);

        Log.d("TEST fanalli",":"+getIntent().getSerializableExtra("REC"));
        String fileName=((Item)getIntent().getSerializableExtra("REC")).getLink();

        getSupportActionBar().setTitle(((Item)getIntent().getSerializableExtra("REC")).getName());

        if(fileName!=null && !fileName.equals("")) {
            try {
                pdfView.fromAsset(fileName)
                        // .pages(0, 2, 1, 3, 3, 3)
                        .defaultPage(1)
                        .showMinimap(false)
                        .enableSwipe(true)
                        .onLoad(this)
                        .onPageChange(this)
                        .load();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(this,"Please try again!",Toast.LENGTH_LONG);
            }
        }

        Animation animation=null;

        animation = AnimationUtils.loadAnimation(PDFFileDisplay.this,R.anim.bottom_up);

        animation.setDuration(1000);


        // Load an ad into the AdMob banner view.
        adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice("85BE868226D2620A881B2A5C9D76AA8C") //we need to remove
                .build();

        adView.loadAd(adRequest);

        final Animation finalAnimation = animation;
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                adView.setVisibility(View.GONE);

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                adView.setVisibility(View.GONE);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adView.setVisibility(View.VISIBLE);
                adView.startAnimation(finalAnimation);
            }
        });


    }
    @Override
    public void loadComplete(int nbPages) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onPageChanged(int page, int pageCount) {
        // TODO Auto-generated method stub

    }


}
