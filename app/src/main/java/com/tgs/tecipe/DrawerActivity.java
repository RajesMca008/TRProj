package com.tgs.tecipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.tgs.tecipe.util.Item;
import com.tgs.tecipe.util.XMLHandler;


public class DrawerActivity extends BaseActivity{


	private DrawerLayout mDrawerLayout;
	private ExpandableListView expListView;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private DrawerActivity _activity=null;
	private XMLHandler handler=null;


	private List<String> listDataHeader;
	InterstitialAd mInterstitialAd;
	private HashMap<String, ArrayList<Item>> listDataChild;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_drawer);

		_activity=this;



		final AdView mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder()
				//.addTestDevice("58F9A1899B419FFC37F85A0E142DE5F4")
				.build();
		mAdView.loadAd(adRequest);
		// AdRequest.Builder.addTestDevice("58F9A1899B419FFC37F85A0E142DE5F4");


		mAdView.setAdListener(new AdListener() {

			@Override
			public void onAdLoaded() {
				mAdView.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAdFailedToLoad(int error) {
				mAdView.setVisibility(View.GONE);
			}

		});


		mInterstitialAd = new InterstitialAd(this);
		mInterstitialAd.setAdUnitId(getString(R.string.banner_ad_unit_id));

	      /*  mInterstitialAd.setAdListener(new AdListener() {
	            @Override
	            public void onAdClosed() {
	                requestNewInterstitial();
	            }
	        });*/

		requestNewInterstitial();

		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		expListView = (ExpandableListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		// set up the drawer's list view with items and click listener

		// enable ActionBar app icon to behave as action to toggle nav drawer

		new BackGroundWork().execute("");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(
				this,                  /* host Activity */
				mDrawerLayout,         /* DrawerLayout object */
				R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
				R.string.drawer_open,  /* "open drawer" description for accessibility */
				R.string.drawer_close  /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);


				//invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mDrawerTitle);
				// invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		/* if (savedInstanceState == null) {
	            selectItem(0);
	        } */
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(expListView);
		//menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch(item.getItemId()) {

			case R.id.share:

				String urlToShare = " market://details?id=com.tgs.tecipe&hl=en";

				Intent sharingIntent = new Intent(Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, urlToShare);
				startActivity(Intent.createChooser(sharingIntent,"Share using"));
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}
	private void selectItem(Item item,int position) {
		// update the main content by replacing fragments

		if(handler!=null)
		{
			//  PlanetFragment fragment = new PlanetFragment();
			PDFDisplayFragment fragment=new PDFDisplayFragment();
			Bundle args = new Bundle();
			// args.putInt(PDFDisplayFragment.recipeName, position);
			//args.putString(PDFDisplayFragment.recipePath, handler.getItemList().get(position).getLink());
			args.putString(PDFDisplayFragment.recipePath,item.getLink());
			fragment.setArguments(args);

			android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

			// update selected item and title, then close the drawer
			expListView.setItemChecked(position, true);
			setTitle(item.getName());
			mDrawerLayout.closeDrawer(expListView);
		}
		else{
			Toast.makeText(_activity, "Data load error...", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
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
				SAXParser parser=SAXParserFactory.newInstance().newSAXParser();
				handler=new XMLHandler();
				parser.parse(_activity.getAssets().open("recipe.xml"), handler);


				if(handler.getItemList().size()>0)
				{
					vegList=new  ArrayList<Item>();
					nonVegList=new  ArrayList<Item>();

					for (int i = 0; i < handler.getItemList().size(); i++) {

						if( handler.getItemList().get(i).getCategory().toString().equalsIgnoreCase("veg"))
						{
							vegList.add(handler.getItemList().get(i));
						}
						else{
							nonVegList.add(handler.getItemList().get(i));

						}

					}
					listDataHeader = new ArrayList<String>();
					listDataChild = new HashMap<String, ArrayList<Item>>();

					// Adding child data
					listDataHeader.add("vegetarian recipes");
					listDataHeader.add("Non-Veg recipes");

					listDataChild.put(listDataHeader.get(0), vegList); // Header, Child data
					listDataChild.put(listDataHeader.get(1), nonVegList);

					//prepareListData();

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

			selectItem(vegList.get(0), 0);
			if(handler!=null)
			{


				com.tgs.tecipe.adapter.ExpandableListAdapter listAdapter = new com.tgs.tecipe.adapter.ExpandableListAdapter(DrawerActivity.this, listDataHeader, listDataChild);
				// expListView.setAdapter(new MyAdapter(_activity,handler));

				expListView.setAdapter(listAdapter);


				// Listview on child click listener
				expListView.setOnChildClickListener(new OnChildClickListener() {

					@Override
					public boolean onChildClick(ExpandableListView parent, View v,
												int groupPosition, int childPosition, long id) {

						try{
							selectItem((Item)(listDataChild.get(listDataHeader.get(groupPosition))).get(childPosition),childPosition);
						}
						catch (Exception e) {
							e.printStackTrace();
						}
						return false;
					}
				});



			}
			dialog.dismiss();
		}
	}

	class MyAdapter extends BaseAdapter{

		Context context=null;
		ArrayList<Item> itemList=null;
		public MyAdapter(Context context, XMLHandler handler) {
			// TODO Auto-generated constructor stub
			itemList=handler.getItemList();
			this.context=context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return itemList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return itemList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View contentView, ViewGroup arg2) {

			View view=null;
			if(contentView==null)
			{
				view=View.inflate(getApplicationContext(), R.layout.list_item_old,null);
				//view.setTag(view);
				contentView=view;
				contentView.setTag(view);
			}
			else
			{
				view=(View)contentView.getTag();
			}

			TextView name_text=null;

			name_text=(TextView)view.findViewById(R.id.item_name);
			name_text.setText(""+itemList.get(arg0).getName());
			//view.setTag(itemList.get(arg0));

			if(itemList.get(arg0).getCategory().toString().equalsIgnoreCase("veg"))
			{
				name_text.setTextColor(Color.GREEN);
			}
			else
			{
				name_text.setTextColor(Color.RED);
			}

			return view;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			backKeyFunctionality();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


	private void backKeyFunctionality() {
		AlertDialog.Builder builder = new AlertDialog.Builder(DrawerActivity.this);
		builder.setTitle("Alert!");
		builder.setIcon(R.mipmap.ic_launcher);
		builder.setMessage("Are you sure want to Exit?")
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						if(mInterstitialAd!=null)
							if (mInterstitialAd.isLoaded()) {
								mInterstitialAd.show();
							}

						finish();
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();

	}

	private void requestNewInterstitial() {
		AdRequest adRequest = new AdRequest.Builder()
				//.addTestDevice("58F9A1899B419FFC37F85A0E142DE5F4")
				.build();

		mInterstitialAd.loadAd(adRequest);
	}

}
