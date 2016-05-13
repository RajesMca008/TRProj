package com.tgs.tecipe;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.tgs.tecipe.util.AppDataBean;
import com.tgs.tecipe.util.Item;

import java.util.ArrayList;

public class ListViewActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView mListView =null;
    private AdView adView=null;
    private Animation animation;

    private boolean isVeg=true;


    private static final String[] COUNTRIES = new String[] { "Belgium",
            "France", "France_", "Italy", "Germany", "Spain" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        // setActionbarSearchUI();

        isVeg=getIntent().getBooleanExtra("IS_VEG",false);

         mListView =(ListView)findViewById(R.id.list_view);

//        Log.d("TEST ","info "+ AppDataBean.getInstance().getVegList().size());

        animation = AnimationUtils.loadAnimation(ListViewActivity.this,R.anim.bottom_up);

        animation.setDuration(1000);


          ArrayList<Item> recipesList=null;
        if(isVeg)
        {
            recipesList=AppDataBean.getInstance().getVegList();
        }
        else {
            recipesList=AppDataBean.getInstance().getNonVegList();
        }

        MyCustomAdapter adapter=  new MyCustomAdapter(this,recipesList);


        mListView.setAdapter(adapter);


        mListView.setOnItemClickListener(this);

      //  handleIntent(getIntent());

        // Load an ad into the AdMob banner view.
        adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("85BE868226D2620A881B2A5C9D76AA8C") //we need to remove
                .build();

        adView.loadAd(adRequest);

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
                adView.startAnimation(animation);
            }
        });
    }

   /* private void setActionbarSearchUI() {

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        // actionBar.setDisplayShowTitleEnabled(false);
        // actionBar.setIcon(R.drawable.ic_action_search);

        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.actionbar_layout, null);

        actionBar.setCustomView(v);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);

        AutoCompleteTextView textView = (AutoCompleteTextView) v
                .findViewById(R.id.editText1);
        textView.setAdapter(adapter);
    }
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("TEST"," :onQueryTextSubmit :"+query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("TEST"," :onQueryTextChange :"+newText);
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                Log.d("TEST"," :setOnCloseListener :");
                return false;
            }
        });

       /* searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));*/

        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Intent intent=new Intent(this,PDFFileDisplay.class);
        startActivity(intent);
    }

    class MyCustomAdapter extends BaseAdapter
    {
        ArrayList<Item> arrayList=null;
        Context context=null;

        public MyCustomAdapter(Context context, ArrayList<Item> arrayList)
        {
            this.context=context;

            this.arrayList=arrayList;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return arrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {

            if(view==null)
            {
                view=View.inflate(context,R.layout.list_item_new,null);
                if(isVeg)
                view.findViewById(R.id.strip_color).setBackgroundColor(ContextCompat.getColor(context,R.color.veg_color));
                else
                    view.findViewById(R.id.strip_color).setBackgroundColor(ContextCompat.getColor(context,R.color.non_veg_color));
            }

            ((TextView)view.findViewById(R.id.recipe_name)).setText(arrayList.get(position).getName());
            ((TextView)view.findViewById(R.id.recipe_type)).setText(arrayList.get(position).getType());


            return view;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
       // handleIntent(intent);

        String query = intent.getStringExtra(SearchManager.QUERY);
        Log.d("TEST ","query :"+query);
    }

   /* private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search
            Log.d("TEST ","query :"+query);
        }
    }*/


    /**
     *
     * @param bmp input bitmap
     * @param contrast 0..10 1 is default
     * @param brightness -255..255 0 is default
     * @return new bitmap
     */
    public static Bitmap changeBitmapContrastBrightness(Bitmap bmp, float contrast, float brightness)
    {
        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        contrast, 0, 0, 0, brightness,
                        0, contrast, 0, 0, brightness,
                        0, 0, contrast, 0, brightness,
                        0, 0, 0, 1, 0
                });

        Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        Canvas canvas = new Canvas(ret);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bmp, 0, 0, paint);

        return ret;
    }
}
