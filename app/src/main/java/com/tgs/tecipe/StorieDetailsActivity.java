package com.tgs.tecipe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class StorieDetailsActivity extends AppCompatActivity {
    private SubCategory subCategory = null;
    String SP_NAME = "sp_user";
    SharedPreferences sharedPreferences = null;
    private boolean fromBookMark = false;
    TextView storie_decs = null;
    int TEXT_MEDIUM = 18;
    int TEXT_LARGE = 26;
    int TEXT_SMALL = 14;
    int textSize = TEXT_MEDIUM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storie_details);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        sharedPreferences = getSharedPreferences(SP_NAME, MODE_PRIVATE);

        textSize = sharedPreferences.getInt("TEXT_SIZE", 18);


        storie_decs = (TextView) findViewById(R.id.storie_desc);

        storie_decs.setTextSize(textSize);
        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView_id);

        subCategory = (SubCategory) getIntent().getSerializableExtra("SUBCAT");

        if (subCategory != null) {
            setTitle(subCategory.getStorieTitle());
            storie_decs.setText(Html.fromHtml(subCategory.getStorie()));
        }


       /* try {
            int id = getResources().getIdentifier(subCategory.getImageName(), "mipmap", getPackageName());
            recipe_img.setImageResource(id);


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/



        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fab.setImageResource(R.mipmap.ic_bookmark);
                int postion = scrollView.getScrollY();

                sharedPreferences.edit().putBoolean("BOOKMARK_SAVED", true).commit();
                sharedPreferences.edit().putInt("STORY_ID", subCategory.getStorieID() + 1).commit();
                sharedPreferences.edit().putInt("SCROLL_POS", postion).commit();

                Snackbar.make(view, "Bookmark saved.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

          FloatingActionButton share = (FloatingActionButton) findViewById(R.id.nav_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final String appPackageName = getPackageName();
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out at: https://play.google.com/store/apps/details?id=" + appPackageName);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                } catch(Exception e) {
                    //e.toString();
                }


            }
        });

        fromBookMark = getIntent().getBooleanExtra("FROM_BOOKMARK", false);

        if (fromBookMark) {
            fab.setImageResource(R.mipmap.ic_bookmark);
            final int scroll_pos = sharedPreferences.getInt("SCROLL_POS", 0);

            scrollView.post(new Runnable() {
                public void run() {
                    scrollView.scrollTo(0, scroll_pos);
                }
            });

        }


        AdView smartBanner=(AdView)findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        smartBanner.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.story_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_small:
                sharedPreferences.edit().putInt("TEXT_SIZE", TEXT_SMALL).commit();
                storie_decs.setTextSize(TEXT_SMALL);
                break;
            case R.id.item_medium:
                sharedPreferences.edit().putInt("TEXT_SIZE", TEXT_MEDIUM).commit();
                storie_decs.setTextSize(TEXT_MEDIUM);
                break;
            case R.id.item_large:
                sharedPreferences.edit().putInt("TEXT_SIZE", TEXT_LARGE).commit();
                storie_decs.setTextSize(TEXT_LARGE);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }



    @Override
    public void onBackPressed() {

        if(fromBookMark)
        {
            finish();
            Intent intent=new Intent(StorieDetailsActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else {
            StorieDetailsActivity.super.onBackPressed();
        }
    }
}
