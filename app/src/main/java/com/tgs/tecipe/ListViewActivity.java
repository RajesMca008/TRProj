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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.tgs.tecipe.util.AppDataBean;
import com.tgs.tecipe.util.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView mListView =null;
    private AdView adView=null;
    private Animation animation;

    private boolean isVeg=true;


    Adptr adptr=null;


    private static final String[] COUNTRIES = new String[] { "Belgium",
            "France", "France_", "Italy", "Germany", "Spain" };

    ArrayList<Item> recipesList=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);



        isVeg=getIntent().getBooleanExtra("IS_VEG",false);

        mListView =(ListView)findViewById(R.id.list_view);

//        Log.d("TEST ","info "+ AppDataBean.getInstance().getVegList().size());

        animation = AnimationUtils.loadAnimation(ListViewActivity.this,R.anim.bottom_up);

        animation.setDuration(1000);


        if(isVeg)
        {
            recipesList=AppDataBean.getInstance().getVegList();
        }
        else {
            recipesList=AppDataBean.getInstance().getNonVegList();
        }


        // setActionbarSearchUI();
       /* MyCustomAdapter adapter=  new MyCustomAdapter(this,recipesList);

        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(this);*/


        adptr=new Adptr(recipesList,this);
        mListView.setAdapter(adptr);

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
                //Log.d("TEST"," :onQueryTextSubmit :"+query);
                adptr.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.d("TEST"," :onQueryTextChange :"+newText);
                adptr.getFilter().filter(newText);
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                adptr.getFilter().filter("");
                // Log.d("TEST"," :setOnCloseListener :");
                return false;
            }
        });

        /*searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));*/

        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Intent intent=new Intent(this,PDFFileDisplay.class);
        startActivity(intent);
    }






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





    public class Adptr extends BaseAdapter implements Filterable {
        public ArrayList<Item> modelValues;

        private Context activity;
        private LayoutInflater layoutinflater;
        private List<Item> mOriginalValues;
        private int PositionSelected = 0;

        public Adptr (ArrayList<Item> modelValues, Context activity) {
            super();
            this.modelValues = modelValues;
            this.activity = activity;


        }

        @Override
        public int getCount() {

            return modelValues.size();
        }

        @Override
        public Object getItem(int position) {

            return modelValues.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            layoutinflater = (LayoutInflater)  activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ViewHolder holder = null;
            Item model = modelValues.get(position);

            if (convertView == null || !(convertView.getTag() instanceof ViewHolder)) {
                convertView = layoutinflater.inflate(R.layout.list_item_new, null);
                holder = new ViewHolder();

                if(isVeg)
                    convertView.findViewById(R.id.strip_color).setBackgroundColor(ContextCompat.getColor(activity,R.color.veg_color));
                else
                    convertView.findViewById(R.id.strip_color).setBackgroundColor(ContextCompat.getColor(activity,R.color.non_veg_color));

                // holder.txtName = (TextView) convertView.findViewById(R.id.row_serch_txt_name);




                convertView.setTag(holder);
                // convertView.setTag(R.id.row_serch_txt_name, holder.txtName);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // holder.txtArtistName.setText("" + modelValue.get_NAME());

            ((TextView)convertView.findViewById(R.id.recipe_name)).setText(modelValues.get(position).getName());
            ((TextView)convertView.findViewById(R.id.recipe_type)).setText(modelValues.get(position).getType());



            return convertView;
        }

        class ViewHolder {
            TextView txtName;


        }

        @Override
        public Filter getFilter() {

            Filter filter = new Filter() {

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    modelValues = (ArrayList<Item>) results.values; // has

                    notifyDataSetChanged();
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults(); // Holds the
                    // results of a
                    // filtering
                    // operation in
                    // values
                    // List<String> FilteredArrList = new ArrayList<String>();
                    List<Item> FilteredArrList = new ArrayList<Item>();

                    if (mOriginalValues == null) {
                        mOriginalValues = new ArrayList<Item>(modelValues); // saves

                    }


                    if (constraint == null || constraint.length() == 0) {

                        // set the Original result to return
                        results.count = mOriginalValues.size();
                        results.values = mOriginalValues;
                    } else {
                        Locale locale = Locale.getDefault();
                        constraint = constraint.toString().toLowerCase(locale);
                        for (int i = 0; i < mOriginalValues.size(); i++) {
                            Item model = mOriginalValues.get(i);

                            String data = model.getName();
                            String category= model.getType();
                            //Log.d("TEST "," Name :"+data);
                            if (data.toLowerCase(locale).contains(constraint.toString()) ||category.toLowerCase(locale).contains(constraint.toString())) {

                                // Log.d("TEST "," Inside :"+data);
                                FilteredArrList.add(model);
                            }
                            else{
                                // Log.d("TEST "," FAIL :"+data.toLowerCase(locale)+ "="+constraint.toString()+"?");
                            }
                        }
                        // set the Filtered result to return
                        results.count = FilteredArrList.size();
                        results.values = FilteredArrList;

                    }
                    return results;
                }
            };
            return filter;
        }

    }
}
