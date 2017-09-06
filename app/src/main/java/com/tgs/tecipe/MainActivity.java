package com.tgs.tecipe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private SQLiteDatabase mydatabase;
    private ArrayList<MainCategory> mainCategoryList =null;
    private ArrayList<SubCategory> subCategoryList =null;

    private ListView listView;
    private boolean isShowingSubCat=false;
    private String SP_NAME="sp_user";
    InterstitialAd mInterstitialAd;
    SharedPreferences sharedPreferences=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        
        listView=(ListView)findViewById(R.id.list_view);

        listView.setOnItemClickListener(this);


          sharedPreferences=getSharedPreferences(SP_NAME,MODE_PRIVATE);
        boolean bookmarketed=sharedPreferences.getBoolean("BOOKMARK_SAVED",false);


        try {
             if(! new File("/data/data/" + getPackageName() + "/databases/telugu_recipes.db").exists())
            CopyDatabase();
            try {
                OpenMyDatabase();
                showCategories();
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Error Connecting Database");
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            showToast("Database File not Found");
        }

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        requestNewInterstitial();
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                 .build();

        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                finish();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_old, menu);
        menu.getItem(0).setVisible(true);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_bookmark:

                boolean bookmarketed=sharedPreferences.getBoolean("BOOKMARK_SAVED",false);
                int storyID=sharedPreferences.getInt("STORY_ID",0);
                if(bookmarketed) {

                    goToBookmarke(storyID);
                }else {
                    showToast("Not saved any bookmarks");
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goToBookmarke(int storyID) {

        Cursor resultSet = this.mydatabase.rawQuery("Select * from PRODUCTS where ID = "+storyID, null);
        Log.d("check", resultSet.getCount() + "");
        if (resultSet.getCount() > 0) {
            resultSet.moveToFirst();
            SubCategory cat = null;
                cat = new SubCategory();
                cat.setStorieID(resultSet.getInt(resultSet.getColumnIndex("ID")));
                cat.setStorieTitle(resultSet.getString(resultSet.getColumnIndex("NAME")));
                cat.setStorie(resultSet.getString(resultSet.getColumnIndex("DESCRIPTION")));
            //IMAGE_NAME
            cat.setImageName(resultSet.getString(resultSet.getColumnIndex("IMAGE_NAME")));


            resultSet.close();

            Intent intent=new Intent(this,StorieDetailsActivity.class);
            intent.putExtra("SUBCAT",(Serializable)cat);
            intent.putExtra("FROM_BOOKMARK",true);
            startActivity(intent);
        }
    }


    private void showCategories() {

        Cursor resultSet = this.mydatabase.rawQuery("Select * from PRODUCT_TYPE", null);
        Log.d("check", resultSet.getCount() + "");
        if (resultSet.getCount() > 0) {

            mainCategoryList =new ArrayList<MainCategory>();
            MainCategory cat=null;
            while (resultSet.moveToNext()) {
                cat=new MainCategory();
                cat.setCatId(resultSet.getInt(resultSet.getColumnIndex("ID")));
                cat.setCatName(resultSet.getString(resultSet.getColumnIndex("TYPE_NAME")));
                //Log.i("TEST",":"+resultSet.getString(resultSet.getColumnIndex("REMEDIE_NAME")));
                mainCategoryList.add(cat);

            }
            resultSet.close();

            listView.setAdapter(new MyMainCategoryAdapter(this));

        }
        else {

            showToast("No Records Found");
        }
    }

    private void showSubCategoryies(int catID) {

        Cursor resultSet = this.mydatabase.rawQuery("Select * from PRODUCTS where TYPE_ID = "+catID, null);
        Log.d("check", resultSet.getCount() + "");
        if (resultSet.getCount() > 0) {

            subCategoryList =new ArrayList<SubCategory>();
            SubCategory cat=null;
            while (resultSet.moveToNext()) {
                cat=new SubCategory();
                cat.setStorieID(resultSet.getInt(resultSet.getColumnIndex("ID")));
                cat.setStorieTitle(resultSet.getString(resultSet.getColumnIndex("NAME")));
                cat.setStorie(resultSet.getString(resultSet.getColumnIndex("DESCRIPTION")));
                cat.setImageName(resultSet.getString(resultSet.getColumnIndex("IMAGE_NAME")));

                subCategoryList.add(cat);

            }
            resultSet.close();


            listView.setAdapter(new MySubCategoryAdapter(this));

        }
        else {

            showToast("No Records Found");
        }
    }

    public void CopyDatabase() throws IOException {
        openOrCreateDatabase("telugu_recipes.db", 0, null);
        InputStream mInput = getAssets().open("databases/telugu_recipes.db");

        OutputStream mOutput = new FileOutputStream("/data/data/" + getPackageName() + "/databases/telugu_recipes.db");
        byte[] mBuffer = new byte[1024];
        while (true) {
            int mLength = mInput.read(mBuffer);
            if (mLength > 0) {
                mOutput.write(mBuffer, 0, mLength);
            } else {
                mOutput.flush();
                mOutput.close();
                mInput.close();
                return;
            }
        }
    }

    public void OpenMyDatabase() throws Exception {
        this.mydatabase = openOrCreateDatabase("telugu_recipes.db", 0, null);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(view.getTag()!=null && view.getTag().equals("MAIN"))
        {
            String title=((TextView)view.findViewById(R.id.title_text)).getText().toString();
            setTitle(title);
        showSubCategoryies(i+1);
        }
        else {

            Intent intent=new Intent(this,StorieDetailsActivity.class);
            intent.putExtra("SUBCAT",(Serializable)subCategoryList.get(i));
            intent.putExtra("FROM_BOOKMARK",false);
            startActivity(intent);
        }


    }


    class MyMainCategoryAdapter extends BaseAdapter
    {

        private Context mContext=null;


        public MyMainCategoryAdapter(Context mainActivity) {
            mContext=mainActivity;
            isShowingSubCat=false;
            setTitle(getString(R.string.telugu_title));
        }

        @Override
        public int getCount() {
            return mainCategoryList.size();
        }

        @Override
        public Object getItem(int i) {
            return mainCategoryList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view==null)
            {
                view= View.inflate(mContext,R.layout.listview_row,null);
                view.setTag("MAIN");


            }

            TextView textView=(TextView) view.findViewById(R.id.title_text);
            textView.setText(mainCategoryList.get(i).getCatName());
            textView.setTypeface(null, Typeface.BOLD);

            return view;
        }
    }

    private class MySubCategoryAdapter extends BaseAdapter {
        private Context mContext=null;
        private int lastPosition=-1;
        public MySubCategoryAdapter(Context mainActivity) {
            mContext=mainActivity;
            isShowingSubCat=true;
        }


        @Override
        public int getCount() {
            return subCategoryList.size();
        }

        @Override
        public Object getItem(int i) {
            return subCategoryList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view==null)
            {
                view= View.inflate(mContext,R.layout.listview_row,null);
                view.setTag("SUB");

            }
            ((TextView) view.findViewById(R.id.title_text)).setText(subCategoryList.get(i).getStorieTitle());
            CircleImageView image=(CircleImageView) view.findViewById(R.id.profile_image);

            if(!subCategoryList.get(i).getImageName().equals(""))
            {
                try {
                    // get input stream
                    InputStream ims = getAssets().open(subCategoryList.get(i).getImageName());
                    // load image as Drawable
                    Drawable d = Drawable.createFromStream(ims, null);
                    // set image to ImageView
                    image.setImageDrawable(d);
                }
                catch(IOException ex) {

                }
            }
            else {
                image.setImageResource(R.drawable.recipe_icon);
            }

            Animation animation = AnimationUtils.loadAnimation(mContext, (i > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            view.startAnimation(animation);
            lastPosition = i;
            return view;
        }
    }

    @Override
    public void onBackPressed() {
        if(isShowingSubCat)
        {
            listView.setAdapter(new MyMainCategoryAdapter(this));
        }else {
          /*  new AlertDialog.Builder(this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            mydatabase.close();


                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                            }
                            else {
                                MainActivity.super.onBackPressed();
                            }
                        }
                    }).create().show();*/

            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }
            else {
                MainActivity.super.onBackPressed();
            }
        }
    }
}
